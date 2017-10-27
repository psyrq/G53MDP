package com.example.qianruofei.fingerpainting;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class BrushSettingsActivity extends AppCompatActivity {

    private SeekBar mySeekBar;
    private Context mContext;
    private TextView curWidth;

    private String[] settings = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brush_settings);

        Bundle bundle = getIntent().getExtras();
        settings = bundle.getStringArray("curBrushInfo");

        final TextView oldWidth = (TextView)findViewById(R.id.CurWidth);
        oldWidth.setText("Current width:" + settings[0] + "px ");

        mySeekBar = (SeekBar)findViewById(R.id.MySeekBar);
        mySeekBar.setProgress(Integer.parseInt(settings[0]));

        final TextView oldShape = (TextView)findViewById(R.id.ShowShape);
        oldShape.setText(settings[1]);

        mContext = BrushSettingsActivity.this;
        seekBarView();
    }

    public void roundShape(View v) {

        final TextView shapeChoice = (TextView)findViewById(R.id.ShowShape);
        String shape = "Round";
        shapeChoice.setText(shape);
    }

    public void squareShape(View v) {

        final TextView shapeChoice = (TextView)findViewById(R.id.ShowShape);
        String shape = "Square";
        shapeChoice.setText(shape);
    }

    public void seekBarView() {
        mySeekBar = (SeekBar)findViewById(R.id.MySeekBar);
        curWidth = (TextView)findViewById(R.id.CurWidth);

        mySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                curWidth.setText("Current width:" + progress + "px ");
                settings[1] = String.valueOf(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(mContext, "press", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(mContext, "loosen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void returnSettings(View v) {

        //settings = new String[2];
        final TextView shapeSetting = (TextView)findViewById(R.id.ShowShape);
        //final EditText widthSetting = (EditText)findViewById(R.id.WidthSetting);

        settings[0] = shapeSetting.getText().toString();
        //settings[1] = String.valueOf(newWidth);
        //settings[1] = String.valueOf(myFingerPainterView.getBrushWidth());

        Bundle bundle = new Bundle();
        Intent result = new Intent();

        bundle.putStringArray("settings", settings);
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
