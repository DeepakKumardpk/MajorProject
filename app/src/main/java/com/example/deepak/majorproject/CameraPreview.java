package com.example.deepak.majorproject;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

/**
 * Created by anugrah on 3/3/18.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private static Camera mcamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mcamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
//        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        Camera.Parameters param = mcamera.getParameters();

//        List<Camera.Size> sizes = param.getSupportedPictureSizes();
//        Camera.Size msize = null;
//
//        for(Camera.Size size : sizes){
//            msize = size;
//        }

        if (this.getResources().getConfiguration().orientation!= Configuration.ORIENTATION_LANDSCAPE){
            param.set("orientation","portrait");
            mcamera.setDisplayOrientation(90);
            param.setRotation(90);
        }
        else{
            param.set("orientation","landscape");
            mcamera.setDisplayOrientation(0);
            param.setRotation(0);
        }

//        param.setPictureSize(msize.width,msize.height);
        mcamera.setParameters(param);


        try {
            mcamera.setPreviewDisplay(surfaceHolder);
            mcamera.startPreview();
        }catch (Exception e){
            Log.d("Camera", "Error in camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (mHolder.getSurface() == null){
            return;
        }

        try {
            mcamera.stopPreview();
        } catch (Exception e){
            Log.d("Camera", "Error in camera stopping: " + e.getMessage());
        }

        try {
            mcamera.setPreviewDisplay(mHolder);
            mcamera.startPreview();

        } catch (Exception e){
            Log.d("Camera", "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mcamera.stopPreview();
        mcamera.release();
        mcamera = null;
    }
}
