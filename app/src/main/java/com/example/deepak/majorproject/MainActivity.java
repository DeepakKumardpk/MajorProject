package com.example.deepak.majorproject;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Camera camera;
    CameraPreview cameraPreview;
    FrameLayout frameLayout;
    public static final int MY_PERMISSIONS_REQUEST_ACCOUNT = 100;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT < 23){
            camera = getCameraInstance();
            cameraPreview = new CameraPreview(this, camera);
            frameLayout = (FrameLayout) findViewById(R.id.surfaceView);
            frameLayout.addView(cameraPreview);
        }
        else {
            if (checkAndRequestPermissions()) {
                camera = getCameraInstance();
                cameraPreview = new CameraPreview(this, camera);
                frameLayout = (FrameLayout) findViewById(R.id.surfaceView);
                frameLayout.addView(cameraPreview);
            }
        }
    }

    private boolean checkAndRequestPermissions() {
        int permissionCAMERA = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);

        int storagePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST_ACCOUNT);
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                Intent intent = new Intent(getBaseContext(),DevMode.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCOUNT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    camera = getCameraInstance();
                    cameraPreview = new CameraPreview(this, camera);
                    frameLayout = (FrameLayout) findViewById(R.id.surfaceView);
                    frameLayout.addView(cameraPreview);
                } else {
                    finish();
                }
                break;
        }
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
        if(camera!=null) {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            cameraPreview = new CameraPreview(this, camera);
            Log.d("Camera", "onRestart OUT Camera, CameraPreview: " + camera + ", " + cameraPreview);
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
}