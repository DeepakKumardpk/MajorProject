package com.example.deepak.majorproject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class CollectData extends AppCompatActivity  {

    TextView label_name;
    ImageButton add_image;
    static String folder_name;
    CameraPreview cameraPreview;
    Camera camera;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    Camera.PictureCallback mPicture = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_data);

        Intent myIntent = getIntent(); // gets the previously created intent
        folder_name = myIntent.getStringExtra("FOLDER_NAME");

        label_name = (TextView)findViewById(R.id.textView);
        add_image = (ImageButton)findViewById(R.id.button3);
        label_name.setText(label_name.getText().toString()+folder_name);
        add_image.setImageResource(R.drawable.mycam);
        camera = getCameraInstance();
        cameraPreview = new CameraPreview(this,camera);
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.layout);
        frameLayout.addView(cameraPreview);


        mPicture= new Camera.PictureCallback() {

            public final String TAG = null;

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {


                File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                if (pictureFile == null){
                    Throwable e=null;
                    Log.d(TAG, "Error creating media file, check storage permissions: " + e.getMessage());
                    return;
                }

                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.d(TAG, "Error accessing file: " + e.getMessage());
                }
            }
        };

        add_image.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Thread t = new Thread() {

                            public void run() {
                                takePicture();

                            }
                        };
                        t.start();
                        // get an image from the camera
                        //camera.takePicture(null, null, mPicture);
                        //camera.release();
                    }
                }
        );

    }

    public void takePicture() {
        TakePictureTask takePictureTask = new TakePictureTask();
        takePictureTask.execute();
    }

    private class TakePictureTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {
            // This returns the preview back to the live camera feed
            camera.startPreview();
        }

        @Override
        protected Void doInBackground(Void... params) {
            camera.takePicture(null, null, mPicture);

            // Sleep for however long, you could store this in a variable and
            // have it updated by a menu item which the user selects.
            try {
                Thread.sleep(3000); // 3 second preview
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

    }


    public static Camera getCameraInstance() {
        Camera c = null;
        try{
            c = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        }catch (Exception e){
            Log.d("Camera", "Error in camera opening: " + e.getMessage());
        }
        return c;
    }
    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MajorProject/"+folder_name);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MajorProject"+folder_name, "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onPause() {
        if (camera != null) {
            camera.stopPreview();
//            camera.release();
//            camera = null;
        }
//        if(cameraPreview!=null){
//            frameLayout.removeView(cameraPreview);
//            cameraPreview=null;
//        }
        super.onPause();
        Log.d("Camera", "onPause OUT Camera, CameraPreview: " + camera + ", " + cameraPreview);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        cameraPreview = new CameraPreview(this,camera);
        Log.d("Camera", "onRestart OUT Camera, CameraPreview: " + camera + ", " + cameraPreview);
    }
}
