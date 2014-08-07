package adi.hack.ProductVisualizer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

public class ImageGetter extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_getter);

        Intent intent = getIntent();
        if (savedInstanceState == null && intent != null) {

            if (intent.getAction().equals(Intent.ACTION_SEND)) {

                String message = intent.getStringExtra(Intent.EXTRA_TEXT);
//                messageText.setText(message);
//                receiverText.requestFocus();
            }
        }

        if(getIntent().hasExtra("byteArray")) {
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("byteArray"), 0, getIntent().getByteArrayExtra("byteArray").length);
            Toast.makeText(getApplicationContext(), "lalala", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), "nanana", Toast.LENGTH_SHORT).show();
        }
    }
}
