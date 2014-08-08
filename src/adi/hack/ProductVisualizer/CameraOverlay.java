package adi.hack.ProductVisualizer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.*;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.ByteArrayOutputStream;

public class CameraOverlay extends Activity {

    CustomCameraView cv;
    PowerManager.WakeLock wakeLock;

    RelativeLayout augView;
    Button btn_capture;
    ImageView img_overlay;

    Bitmap imageBitmap;

    Point coordInit = new Point();
    Point coordEnd = new Point();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.aug_view);
        cv=new CustomCameraView(getApplicationContext());

        RelativeLayout relLayout=(RelativeLayout)findViewById(R.id.aug_camview);
        cv.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        relLayout.addView(cv);

        relLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        coordInit.x = x;
                        coordInit.y = y;
                        break;
                    case MotionEvent.ACTION_UP:
                        coordEnd.x = x;
                        coordEnd.y = y;

                }
                return true;
            }
        });

        augView = (RelativeLayout) findViewById(R.id.aug_camview);

        btn_capture=(Button)findViewById(R.id.btn_capture);
        btn_capture.setVisibility(View.VISIBLE);
        btn_capture.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                btn_capture.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Capturing !", Toast.LENGTH_SHORT).show();
                Bitmap screenShot = getScreenShot(augView);
                Intent i = new Intent(getApplicationContext(), ImageSharing.class);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                screenShot.compress(Bitmap.CompressFormat.PNG, 50, bs);
                i.putExtra("byteArray", bs.toByteArray());
                startActivity(i);
            }
        });

        img_overlay = (ImageView) findViewById(R.id.img_overlay);

        if(getIntent().hasExtra("byteArray")) {
            imageBitmap = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("byteArray"), 0, getIntent().getByteArrayExtra("byteArray").length);
            img_overlay.setImageBitmap(imageBitmap);
        }

        img_overlay.bringToFront();
        btn_capture.bringToFront();
    }

    public Bitmap getScreenShot(View view) {
        cv.writePreviewToFile();
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public void shiftImageMargin(int marginX, int marginY) {
        LinearLayout.LayoutParams layoutParams =(LinearLayout.LayoutParams) img_overlay.getLayoutParams();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        lp.setMargins(left, top, right, bottom);
        img_overlay.setLayoutParams(lp);
    }
}

