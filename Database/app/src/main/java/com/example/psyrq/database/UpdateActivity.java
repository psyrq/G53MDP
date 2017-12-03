package com.example.psyrq.database;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class UpdateActivity extends AppCompatActivity {

    private Context mContext;

    private ListView lv;
    private Cursor cursor;
    private Intent intent;
    private Bundle bundle;

    private SQLiteDatabase db;
    private MyDBOpenHelper myDBHelper;

    private int i = 0;
    private String selectedFromList;
    private String[] allRecipesTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        mContext = UpdateActivity.this;
        myDBHelper = new MyDBOpenHelper(mContext, "my.db", null, 1);
        db = myDBHelper.getWritableDatabase();

        allRecipesTitles = new String[getCount()];

        setListViewAdapter();
    }

    private void setListViewAdapter() {

        getAllTitles();
        bundle = new Bundle();
        lv = (ListView) findViewById(R.id.RecipesListView);

        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allRecipesTitles));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {

                selectedFromList = (String) (lv.getItemAtPosition(myItemInt));
                intent = new Intent(UpdateActivity.this, RecipeDetailsActivity.class);
                bundle.putString("title", selectedFromList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public int getCount()
    {
        Cursor cursor =  db.rawQuery("SELECT COUNT (*) FROM recipe",null);
        cursor.moveToFirst();
        int result = cursor.getInt(0);
        cursor.close();
        return result;
    }

    public void getAllTitles() {

        cursor = db.query("recipe", null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                allRecipesTitles[i] = cursor.getString(cursor.getColumnIndex("title"));
                i++;
            } while(cursor.moveToNext());
        }
    }
}
