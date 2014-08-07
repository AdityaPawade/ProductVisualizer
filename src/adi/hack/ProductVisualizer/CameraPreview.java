package adi.hack.ProductVisualizer;

import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: aditya.pawade
 * Date: 08/08/14
 * Time: 1:42 AM
 */
public class CameraPreview implements Camera.PreviewCallback {

    YuvImage image;
    Camera.Size size;

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

        Camera.Parameters parameters = camera.getParameters();
        size = parameters.getPreviewSize();
        image = new YuvImage(data, parameters.getPreviewFormat(),
                size.width, size.height, null);

    }

    public void writeImageToFile() {
        try {

//            Rect rectangle = new Rect();
//            rectangle.bottom = size.height;
//            rectangle.top = 0;
//            rectangle.left = 0;
//            rectangle.right = size.width;
//
//            ByteArrayOutputStream os = new ByteArrayOutputStream();
//            image.compressToJpeg(rectangle, 100, os);
//
////            Matrix matrix = new Matrix();
////            matrix.postRotate(90);
//            byte[] bytes = os.toByteArray();
//            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());

            File file = new File(Environment.getExternalStorageDirectory()
                    .getPath() + "/visualizer.jpg");
            FileOutputStream filecon = new FileOutputStream(file);
            image.compressToJpeg(
                    new Rect(0, 0, image.getWidth(), image.getHeight()), 90,
                    filecon);
        } catch (Exception e) {

        }
    }
}
