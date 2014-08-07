package adi.hack.ProductVisualizer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: aditya.pawade
 * Date: 07/08/14
 * Time: 7:56 PM
 */
public class ImageEditor extends Activity {

    CustomImageView editableImage;
    Bitmap imageBitmap;
    private Point coords = new Point();
    private List<Point> touchedPoints;
    int picw = 0, pich = 0;
    int framew = 0, frameh = 0;
    double scalew = 0, scaleh = 0;
    int[] viewCoords = null;

    private boolean locked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_editor);

        editableImage = (CustomImageView) findViewById(R.id.img_edit);

        touchedPoints = new ArrayList<Point>();

        if(getIntent().hasExtra("byteArray")) {
            imageBitmap = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("byteArray"), 0, getIntent().getByteArrayExtra("byteArray").length);
            editableImage.setImageBitmap(imageBitmap);

            picw = imageBitmap.getWidth();
            pich = imageBitmap.getHeight();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        framew = editableImage.getWidth();
        frameh = editableImage.getHeight();

        scalew = framew / (picw * 1.0);
        scaleh = frameh / (pich * 1.0);

        if(viewCoords == null)
            viewCoords = new int[2];
        editableImage.getLocationOnScreen(viewCoords);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.image_editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_continue:
                Intent i = new Intent(getApplicationContext(), CameraOverlay.class);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 50, bs);
                i.putExtra("byteArray", bs.toByteArray());
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX() - viewCoords[0];
        int y = (int) event.getRawY() - viewCoords[1];

        int scaledX = (int) Math.round(x / scalew);
        int scaledY = (int) Math.round(y / scaleh);

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                locked = true;
                touchedPoints.add(new Point(x, y));
                break;
            case MotionEvent.ACTION_MOVE:
                touchedPoints.add(new Point(x, y));
                break;
            case MotionEvent.ACTION_UP:
                touchedPoints.add(new Point(x, y));
                int pixel = imageBitmap.getPixel(scaledX,scaledY);
                int redValue = Color.red(pixel);
                int blueValue = Color.blue(pixel);
                int greenValue = Color.green(pixel);
                imageBitmap = makeColorTransparent(imageBitmap, redValue - 30, redValue + 30, greenValue - 30, greenValue + 30, blueValue - 30, blueValue + 30,  Color.TRANSPARENT);
                editableImage.setImageBitmap(imageBitmap);
                touchedPoints.clear();
                locked = false;

        }
        return true;
    }

    public Bitmap makeColorTransparent(Bitmap bitmap,int redStart,int redEnd,int greenStart, int greenEnd,int blueStart, int blueEnd,int colorNew) {
        if (bitmap != null) {
            int[] pix = new int[picw * pich];
            bitmap.getPixels(pix, 0, picw, 0, 0, picw, pich);
            for (int y = 0; y < pich; y++) {
                for (int x = 0; x < picw; x++) {
                    int index = y * picw + x;
                    if (
                            ((Color.red(pix[index]) >= redStart)&&(Color.red(pix[index]) <= redEnd))&&
                                    ((Color.green(pix[index]) >= greenStart)&&(Color.green(pix[index]) <= greenEnd))&&
                                    ((Color.blue(pix[index]) >= blueStart)&&(Color.blue(pix[index]) <= blueEnd))
                            ){
                        pix[index] = colorNew;
                    }
                }
            }
            Bitmap bm = Bitmap.createBitmap(pix, picw, pich,Bitmap.Config.ARGB_8888);
            return bm;
        }
        return null;
    }

    public static Bitmap makePixelsTransparent(Bitmap bitmap, List<Point> points, int colorNew) {
        if (bitmap != null) {
            int picw = bitmap.getWidth();
            int pich = bitmap.getHeight();
            int[] pix = new int[picw * pich];
            bitmap.getPixels(pix, 0, picw, 0, 0, picw, pich);
            for (int y = 0; y < pich; y++) {
                for (int x = 0; x < picw; x++) {
                    int index = y * picw + x;
                    pix[index] = colorNew;
                }
            }
            Bitmap bm = Bitmap.createBitmap(pix, picw, pich,Bitmap.Config.ARGB_8888);
            return bm;
        }
        return null;
    }
}
