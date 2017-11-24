package com.example.psyrq.storage;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText fileName;
    private EditText fileContents;
    private Button write;
    private Button clear;
    private Button read;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        bindViews();
    }

    private void bindViews() {
        fileName = (EditText) findViewById(R.id.FileName);
        fileContents = (EditText) findViewById(R.id.Contents);
        clear = (Button) findViewById(R.id.Clear);
        write = (Button) findViewById(R.id.Write);
        read = (Button) findViewById(R.id.Read);

        clear.setOnClickListener(this);
        write.setOnClickListener(this);
        read.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.Clear:
                fileName.setText("");
                fileContents.setText("");
                break;
            case R.id.Read:
                String detail = "";
                FileHelper readHelper = new FileHelper(getApplicationContext());

                try {
                    String fName = fileName.getText().toString();
                    detail = readHelper.read(fName);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), detail, Toast.LENGTH_SHORT).show();
                break;
            case R.id.Write:
                FileHelper writeHelper = new FileHelper(mContext);
                String filename = fileName.getText().toString();
                String filecontents = fileContents.getText().toString();
                File file = new File(filename);
                try {
                    writeHelper.save(filename, filecontents);
                    Toast.makeText(getApplicationContext(), "数据写入成功 " + writeHelper.getFilePath(file), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "数据写入失败", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
