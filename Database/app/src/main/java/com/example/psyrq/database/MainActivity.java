package com.example.psyrq.database;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
                Intent insertIntent = new Intent(MainActivity.this, InsertActivity.class);
                startActivity(insertIntent);
//                ContentValues values = new ContentValues();
//                values.put("title", "test title");
//                values.put("contents", "test contents");
//                Uri uri = Uri.parse("content://com.example.psyrq.myprovider/recipe");
//                resolver.insert(uri, values);
//                Toast.makeText(getApplicationContext(), "数据插入成功", Toast.LENGTH_SHORT).show();
            }
        });

        btn_query.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent viewIntent = new Intent(MainActivity.this, UpdateActivity.class);
                startActivity(viewIntent);
//                sb = new StringBuilder();
//                Cursor cursor = db.query("recipe", null, null, null, null, null, null);
//                if (cursor.moveToFirst()) {
//                    do {
//                        int recipeId = cursor.getInt(cursor.getColumnIndex("recipeId"));
//                        String name = cursor.getString(cursor.getColumnIndex("title"));
//                        String content = cursor.getString(cursor.getColumnIndex("contents"));
//                        sb.append("id：" + recipeId + "：" + name + " content: " + content + "\n");
//                    } while (cursor.moveToNext());
//                }
//                cursor.close();
//                Toast.makeText(mContext, sb.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                db.execSQL("drop table if exists recipe");
                db.execSQL("CREATE TABLE recipe(recipeId INTEGER PRIMARY KEY AUTOINCREMENT,title VARCHAR(128), contents VARCHAR(8000))");
                Toast.makeText(mContext, "成功删除全部", Toast.LENGTH_SHORT).show();
            }
        });
    }
}