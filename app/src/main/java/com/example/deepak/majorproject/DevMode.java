package com.example.deepak.majorproject;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class DevMode extends AppCompatActivity {

    EditText folder_name;
    Button make_folder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_mode);

        folder_name = (EditText)findViewById(R.id.editText);
        make_folder = (Button)findViewById(R.id.button);



        String state;
        state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state))
        {
            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath()+"/MajorProject");
            if(!dir.exists()){
                dir.mkdir();
                //Log.d(root+"/myCapturedImages","@@@@@@@@@@@file made@@@@@@@@@");
            }
        }

//        //File direct = new File(Environment.getExternalStorageDirectory()+"/MajorProject");
//        String root = Environment.getExternalStorageDirectory().toString();
//        File direct = new File(root + "/myCapturedImages");
//        //direct.mkdir();
//        if (!direct.exists()) {
//            //File file = new File(Environment.getExternalStorageDirectory() + "/MajorProject");
//            direct.mkdirs();
//            Log.d(root+"/myCapturedImages","@@@@@@@@@@@file made@@@@@@@@@");
//        }
        make_folder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (folder_name.getText().toString().matches("")) {
                    Toast.makeText(DevMode.this, "You did not enter a username", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    Intent intent = new Intent(DevMode.this, CollectData.class);
                    intent.putExtra("FOLDER_NAME", folder_name.getText().toString());
                    startActivity(intent);
                }

            }
        });
    }
}
