package com.example.deepak.majorproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class CollectData extends AppCompatActivity {

    TextView label_name;
    Button add_image;
    String folder_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_data);

        Intent myIntent = getIntent(); // gets the previously created intent
        folder_name = myIntent.getStringExtra("FOLDER_NAME");

        label_name = (TextView)findViewById(R.id.textView);
        add_image = (Button)findViewById(R.id.button3);

        label_name.setText(label_name.getText().toString()+folder_name);

        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File root = Environment.getExternalStorageDirectory();
                String myDir = root.getAbsolutePath()+"/MajorProject/"+folder_name;
                File dir = new File(myDir);
                if(!dir.exists()){
                    dir.mkdirs();
                    //Log.d(root+"/myCapturedImages","@@@@@@@@@@@file made@@@@@@@@@");
                }

            }
        });
    }
    public void SaveImage(Bitmap showedImgae){

        File root = Environment.getExternalStorageDirectory();
        String myDir = root.getAbsolutePath()+"/MajorProject/"+folder_name;
        File dir = new File(myDir);
        if(!dir.exists()){
            dir.mkdirs();
            //Log.d(root+"/myCapturedImages","@@@@@@@@@@@file made@@@@@@@@@");
        }
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "FILENAME-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            showedImgae.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Toast.makeText(CollectData.this, "Image Saved", Toast.LENGTH_SHORT).show();
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
