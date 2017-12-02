package com.example.psyrq.database;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Context mContext;

    private Button btn_insert;
    private Button btn_query;
    private Button btn_delete;

    private SQLiteDatabase db;
    private MyDBOpenHelper myDBHelper;
    private StringBuilder sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = MainActivity.this;
        myDBHelper = new MyDBOpenHelper(mContext, "my.db", null, 1);

        btn_insert = (Button) findViewById(R.id.btn_insert);
        btn_query = (Button) findViewById(R.id.btn_query);
        btn_delete = (Button) findViewById(R.id.btn_delete);

        //读取content provider 数据
        final ContentResolver resolver = this.getContentResolver();
        db = myDBHelper.getWritableDatabase();

        btn_insert.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("name", "测试");
                Uri uri = Uri.parse("content://com.example.psyrq.myprovider/person");
                resolver.insert(uri, values);
                Toast.makeText(getApplicationContext(), "数据插入成功", Toast.LENGTH_SHORT).show();
            }
        });

        btn_query.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sb = new StringBuilder();
                Cursor cursor = db.query("person", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        int pid = cursor.getInt(cursor.getColumnIndex("personid"));
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        sb.append("id：" + pid + "：" + name + "\n");
                    } while (cursor.moveToNext());
                }
                cursor.close();
                Toast.makeText(mContext, sb.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                db.execSQL("drop table if exists person;");
                db.execSQL("CREATE TABLE person(personid INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(20))");
                Toast.makeText(mContext, "成功删除全部", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public long getCount()
    {
        //SQLiteDatabase db = myDBHelper.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT COUNT (*) FROM person",null);
        cursor.moveToFirst();
        long result = cursor.getLong(0);
        cursor.close();
        return result;
    }
}
