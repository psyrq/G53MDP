package com.example.qianruofei.fingerpainting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ColourSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colour_select);
    }

    public void ReturnRed(View v) {

        Bundle bundle = new Bundle();
        bundle.putInt("setColour", 0xFFFF0000);
        Intent result = new Intent();
        result.putExtras(bundle);
        setResult(MainActivity.RESULT_OK, result);
        finish();
    }

    public void ReturnGreen(View v) {

        Bundle bundle = new Bundle();
        bundle.putInt("setColour", 0xFF00FF00);
        Intent result = new Intent();
        result.putExtras(bundle);
        setResult(MainActivity.RESULT_OK, result);
        finish();
    }

    public void ReturnBlue(View v) {

        Bundle bundle = new Bundle();
        bundle.putInt("setColour", 0xFF0000FF);
        Intent result = new Intent();
        result.putExtras(bundle);
        setResult(MainActivity.RESULT_OK, result);
        finish();
    }

    public void ReturnBlack(View v) {

        Bundle bundle = new Bundle();
        bundle.putInt("setColour", 0xFF000000);
        Intent result = new Intent();
        result.putExtras(bundle);
        setResult(MainActivity.RESULT_OK, result);
        finish();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

}
