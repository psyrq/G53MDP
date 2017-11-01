package com.example.qianruofei.fingerpainting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    Context mContext;

    //  request code for two activities
    static final int ACTIVITY_COLOUR_SELECT_REQUEST_CODE = 1;
    static final int ACTIVITY_BRUSH_SETTINGS_REQUEST_CODE = 2;

    FingerPainterView myFingerPainterView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        //  new a view is not acceptable here because make change to the new
        //  object doesn't mean to make changes to the exist one
        myFingerPainterView = (FingerPainterView)findViewById(R.id.ViewCanvas);
        myFingerPainterView.load(getIntent().getData());

        Log.i("G53MDP", "Main onCreate");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {// save the user preferences

        super.onSaveInstanceState(outState);

        if(myFingerPainterView.getBrush().equals(Paint.Cap.ROUND))
            outState.putString("Shape", "Round");
        else
            outState.putString("Shape", "Square");

        outState.putInt("Colour", myFingerPainterView.getColour());
        outState.putInt("Width", myFingerPainterView.getBrushWidth());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //  load settings
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState.getString("Shape").equals("Round"))
            myFingerPainterView.setBrush(Paint.Cap.ROUND);
        else
            myFingerPainterView.setBrush(Paint.Cap.SQUARE);

        myFingerPainterView.setBrushWidth(savedInstanceState.getInt("Width"));
        myFingerPainterView.setColour(savedInstanceState.getInt("Colour"));
    }

    public void selectColour(View v) {//    send msg to ColourSelectActivity

        Intent intent = new Intent(MainActivity.this, ColourSelectActivity.class);
        startActivityForResult(intent, ACTIVITY_COLOUR_SELECT_REQUEST_CODE);
    }

    public void brushSettings(View v) {//   send msg to BrushSettingsActivity

        String[] brushInfo = new String[2];
        String curBrushShape;

        //  get current brush width
        int curBrushWidth = myFingerPainterView.getBrushWidth();

        //  get current brush shape
        if(Paint.Cap.ROUND.equals(myFingerPainterView.getBrush()))
            curBrushShape = "Round";
        else
            curBrushShape = "Square";

        //put brush shape and width into a string array for BrushSettingsActivity to load
        brushInfo[0] = String.valueOf(curBrushWidth);
        brushInfo[1] = curBrushShape;

        Bundle bundle = new Bundle();
        bundle.putStringArray("curBrushInfo", brushInfo);

        Intent intent = new Intent(MainActivity.this, BrushSettingsActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, ACTIVITY_BRUSH_SETTINGS_REQUEST_CODE);
    }

    public void clear(View v) {
        //clear all the things that have already done on the canvas, include the loaded image
        myFingerPainterView.clearCanvas();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //  get result from ColourSelectActivity
        if(requestCode == ACTIVITY_COLOUR_SELECT_REQUEST_CODE) {

            if(resultCode == RESULT_OK) {

                Bundle bundle = data.getExtras();
                int getColour = bundle.getInt("setColour");
                //  set new colour for brush
                myFingerPainterView.setColour(getColour);
            }
            else if(resultCode == RESULT_CANCELED) {
            }
        }

        //  get result from BrushSettingsActivity
        else if(requestCode == ACTIVITY_BRUSH_SETTINGS_REQUEST_CODE) {

            if(resultCode == RESULT_OK) {

                Bundle bundle = data.getExtras();
                String[] getBrushSettings = bundle.getStringArray("settings");

                myFingerPainterView.setBrushWidth(Integer.parseInt(getBrushSettings[0]));
                if(getBrushSettings[1].equals("Round"))
                    myFingerPainterView.setBrush(Paint.Cap.ROUND);
                else
                    myFingerPainterView.setBrush(Paint.Cap.SQUARE);
            }
            else if(resultCode == RESULT_CANCELED) {

            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.i("G53MDP", "Main onDestroy");
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        Log.i("G53MDP", "Main onPause");
        super.onPause();
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        Log.i("G53MDP", "Main onResume");
        super.onResume();
    }
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        Log.i("G53MDP", "Main onStart");
        super.onStart();
    }
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        Log.i("G53MDP", "Main onStop");
        super.onStop();
    }
}
