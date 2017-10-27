package com.example.qianruofei.fingerpainting;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Paint paint;

    static final int ACTIVITY_COLOUR_SELECT_REQUEST_CODE = 1;
    static final int ACTIVITY_BRUSH_SETTINGS_REQUEST_CODE = 2;

    FingerPainterView myFingerPainterView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  new a view is not acceptable here because make change to the new
        //  object doesn't mean to make changes to the exist one
        myFingerPainterView = (FingerPainterView)findViewById(R.id.ViewCanvas);
    }

    public void selectColour(View v) {//    send msg to ColourSelectActivity

        Intent intent = new Intent(MainActivity.this, ColourSelectActivity.class);
        startActivityForResult(intent, ACTIVITY_COLOUR_SELECT_REQUEST_CODE);
    }

    public void brushSettings(View v) {//   send msg to BrushSettingsActivity

        String[] brushInfo = new String[2];
        String curBrushShape = new String();
        int curBrushWidth = myFingerPainterView.getBrushWidth();

        if(Paint.Cap.ROUND.equals(myFingerPainterView.getBrush()))
            curBrushShape = "Round";
        else
            curBrushShape = "Square";

        brushInfo[0] = String.valueOf(curBrushWidth);
        brushInfo[1] = curBrushShape;

        Bundle bundle = new Bundle();
        bundle.putStringArray("curBrushInfo", brushInfo);

        Intent intent = new Intent(MainActivity.this, BrushSettingsActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, ACTIVITY_BRUSH_SETTINGS_REQUEST_CODE);
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

                if(getBrushSettings[0].equals("Round"))
                    myFingerPainterView.setBrush(Paint.Cap.ROUND);
                else
                    myFingerPainterView.setBrush(Paint.Cap.SQUARE);

                myFingerPainterView.setBrushWidth(Integer.parseInt(getBrushSettings[1]));
            }
            else if(resultCode == RESULT_CANCELED) {

            }
        }
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
