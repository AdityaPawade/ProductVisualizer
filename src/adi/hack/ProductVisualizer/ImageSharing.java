package adi.hack.ProductVisualizer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: aditya.pawade
 * Date: 08/08/14
 * Time: 12:46 AM
 */
public class ImageSharing extends Activity {

    Button btn_share;

    ImageView img_share;

    Bitmap imageBitmap;
    Bitmap overlayBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.image_sharing);

        img_share = (ImageView) findViewById(R.id.img_share);

        if(getIntent().hasExtra("byteArray")) {
            imageBitmap = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("byteArray"), 0, getIntent().getByteArrayExtra("byteArray").length);

            Bitmap camFile = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()
                    .getPath() + "/visualizer.jpg");

            Matrix matrix = new Matrix();

            matrix.postRotate(90);

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(camFile,imageBitmap.getHeight(),imageBitmap.getWidth(),true);

            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

            overlayBitmap = overlay(rotatedBitmap, imageBitmap);
            img_share.setImageBitmap(overlayBitmap);

//            img_share.setImageBitmap(imageBitmap);
        }

        btn_share=(Button)findViewById(R.id.btn_share);
        btn_share.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Capturing !", Toast.LENGTH_SHORT).show();
                String file = writeBitmapToFile(overlayBitmap);

                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file));//add image path
                startActivity(Intent.createChooser(share, "Share image using"));
            }
        });

    }

    private Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, new Matrix(), null);
        return bmOverlay;
    }

    private String writeBitmapToFile(Bitmap bitmap) {
        try {
            //create a file to write bitmap data
            String filename = Environment.getExternalStorageDirectory().getPath() + "/visualizerOutput.jpg";
            FileOutputStream out = new FileOutputStream(new File(filename));
//            imageview.setImageBitmap(b1);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            return filename;
        } catch (Exception e) {
            return null;
        }
    }
}
