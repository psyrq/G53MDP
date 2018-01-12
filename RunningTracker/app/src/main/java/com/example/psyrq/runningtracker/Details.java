package com.example.psyrq.runningtracker;

import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Details extends AppCompatActivity {

    Context mContext;

    private SQLiteDatabase db;
    private TrackerDBOpenHelper myDBHelper;
    private ContentResolver resolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mContext = Details.this;
        myDBHelper = new TrackerDBOpenHelper(mContext, "tracker.db", null, 1);

        resolver = this.getContentResolver();
        db = myDBHelper.getReadableDatabase();
    }

    private void ShowDetails(View v) {

    }
}
