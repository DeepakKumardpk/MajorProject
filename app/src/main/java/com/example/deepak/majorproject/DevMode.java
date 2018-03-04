package com.example.deepak.majorproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DevMode extends AppCompatActivity {

    EditText folder_name;
    Button make_folder;
    ListView listView ;
    ListAdapter listAdapter;
    List<String> fileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_mode);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        fileName = new ArrayList<>();
        folder_name = (EditText)findViewById(R.id.editText);
        make_folder = (Button)findViewById(R.id.button);
        getList();
        listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,fileName){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);
                if(position %2 == 1)
                {
                    view.setBackgroundColor(Color.parseColor("#FADBD8"));
                }
                else
                {
                    view.setBackgroundColor(Color.parseColor("#F1948A"));
                }
                return view;
            }
        };
        listView = (ListView)findViewById(R.id.listview);
        listView.setAdapter(listAdapter);


        String state;
        state = Environment.getDataDirectory().toString();
        if(Environment.MEDIA_MOUNTED.equals(state))
        {
            File root = Environment.getDataDirectory();
            File dir = new File(root.getAbsolutePath()+"/MajorProject");
            if(!dir.exists()){
                dir.mkdir();
            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String file_name = String.valueOf(adapterView.getItemAtPosition(i));
                folder_name.setText(file_name);
                Intent intent = new Intent(DevMode.this, LabelImage.class);
                intent.putExtra("FILE_NAME", file_name);
                startActivity(intent);
            }
        });

        make_folder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (folder_name.getText().toString().matches("")) {
//                    for(int i = 0; i < fileName.size(); i++)
//                        Log.d("Files", "FileName:" + fileName.get(i));
                    Toast.makeText(DevMode.this, "You did not enter a username", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                   fileName.add(folder_name.getText().toString());
                    Intent intent = new Intent(DevMode.this, CollectData.class);
                    intent.putExtra("FOLDER_NAME", folder_name.getText().toString());
                    startActivity(intent);
                }

            }
        });
    }

    public void getList(){
        String path = Environment.getExternalStorageDirectory().toString()+"/Pictures/MajorProject";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        for(int i = 0; i < files.length; i++)
        {
            fileName.add(files[i].getName());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //getList();
        //listView.setAdapter(listAdapter);
    }
}