package adi.hack.ProductVisualizer;

/**
 * Created by IntelliJ IDEA.
 * User: aditya.pawade
 * Date: 07/08/14
 * Time: 11:33 PM
 */
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

public class CustomCameraView extends SurfaceView {
	Camera camera;
	SurfaceHolder previewHolder;
	CameraPreview cameraPreview;

	public void zoom() {
		Parameters params=camera.getParameters();
		params.setZoom(params.getMaxZoom());
		camera.setParameters(params);
	}

	public void unZoom() {
		Parameters params=camera.getParameters();
		params.setZoom(0);
		camera.setParameters(params);
	}

	public boolean isZoomed() {
	    Parameters params=camera.getParameters();
        int zoom = params.getZoom();
        if(zoom == 0) return false;
        return true;
    }

    public void writePreviewToFile() {
        cameraPreview.writeImageToFile();
    }

	public CustomCameraView(Context context)
	{
		super(context);
		previewHolder=this.getHolder();
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		SurfaceHolder.Callback surfaceHolderListener=new SurfaceHolder.Callback()
		{
			public void surfaceCreated(SurfaceHolder holder)
			{
				camera=Camera.open();
				try{
					camera.setPreviewDisplay(previewHolder);
				}
				catch (Exception e) {
				}
			}

			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				Parameters params=camera.getParameters();

                params.set("orientation", "portrait");

                Camera.Size bestSize;

                List<Camera.Size> sizeList = params.getSupportedPreviewSizes();
                bestSize = sizeList.get(0);

                for(int i = 1; i < sizeList.size(); i++){
                    if((sizeList.get(i).width * sizeList.get(i).height) >
                            (bestSize.width * bestSize.height)){
                        bestSize = sizeList.get(i);
                    }
                }

                params.setPreviewSize(bestSize.width, bestSize.height);

				params.setPictureFormat(PixelFormat.JPEG);

				//if you want the preview to be zoomed from start :
//				params.setZoom(params.getMaxZoom());

				camera.setParameters(params);
				camera.setDisplayOrientation(90);
				camera.startPreview();
                cameraPreview = new CameraPreview();
                camera.setPreviewCallback(cameraPreview);
			}

			public void surfaceDestroyed(SurfaceHolder holder) {
				camera.setPreviewCallback(null);
				camera.stopPreview();
				camera.release();
			}
		};
		previewHolder.addCallback(surfaceHolderListener);
	}
}
