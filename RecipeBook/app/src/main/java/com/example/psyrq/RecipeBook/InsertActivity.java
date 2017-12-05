package com.example.psyrq.RecipeBook;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class InsertActivity extends AppCompatActivity {

    private Context mContext;

    private EditText titleText;
    private EditText contentText;

    private SQLiteDatabase db;
    private MyDBOpenHelper myDBHelper;
    private ContentResolver resolver;

    String title;
    String contents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        mContext = InsertActivity.this;
        myDBHelper = new MyDBOpenHelper(mContext, "my.db", null, 1);

        resolver = this.getContentResolver();
        db = myDBHelper.getWritableDatabase();
    }

    public void insertOnClick(View v) {

        titleText = (EditText) findViewById(R.id.EditableTitle);
        contentText = (EditText) findViewById(R.id.ContentArea);

        title = titleText.getText().toString();
        contents = contentText.getText().toString();

        //put inserted value into ContentValue
        ContentValues insertValue = new ContentValues();
        insertValue.put("title", title);
        insertValue.put("contents", contents);

        resolver.insert(ProviderContract.URI.ID_INSERT, insertValue);
        Toast.makeText(getApplicationContext(), "Recipe inserted", Toast.LENGTH_SHORT).show();

        finish();
    }
}
