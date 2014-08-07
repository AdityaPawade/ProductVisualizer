package adi.hack.ProductVisualizer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: aditya.pawade
 * Date: 07/08/14
 * Time: 6:43 PM
 */
public class ImageChooser extends Activity {

    int PHOTO_PICKER_ID = 1;
    Button upload, edit;
    ImageView previewImage;
    private Bitmap bitmap;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_chooser);

        upload = (Button) findViewById(R.id.btn_upload);
        edit = (Button) findViewById(R.id.btn_edit);
        previewImage = (ImageView) findViewById(R.id.img_preview);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent,
                        "Complete action using"), PHOTO_PICKER_ID);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitmap == null) {
                    Toast.makeText(getApplicationContext(), "Select an Image First", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(getApplicationContext(), ImageEditor.class);
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, bs);
                    i.putExtra("byteArray", bs.toByteArray());
                    startActivity(i);
                }
            }
        });

        Intent intent = getIntent();
        if (intent.getType() != null && intent.getType().indexOf("image/") != -1) {
        Bundle extras = intent.getExtras();
         if (extras.containsKey(Intent.EXTRA_STREAM)) {
            Uri uri = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);

            try {
                InputStream is = getContentResolver().openInputStream(uri);
//                Drawable myDrawable = Drawable.createFromStream(is, "srcName");
                bitmap = BitmapFactory.decodeStream(is);
                previewImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PHOTO_PICKER_ID) {
            try {
                // We need to recyle unused bitmaps
                if (bitmap != null) {
                    bitmap.recycle();
                }
                InputStream stream = getContentResolver().openInputStream(
                        data.getData());
                bitmap = BitmapFactory.decodeStream(stream);
                stream.close();
                previewImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
