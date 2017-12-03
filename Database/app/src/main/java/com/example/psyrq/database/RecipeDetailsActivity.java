package com.example.psyrq.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class RecipeDetailsActivity extends AppCompatActivity {

    private Context mContext;

    private Bundle bundle;
    private EditText editableTitle;
    private EditText editableContents;

    private String selectedRecipeTitle;
    private String selectedRecipeContents;

    private SQLiteDatabase db;
    private MyDBOpenHelper myDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        bundle = getIntent().getExtras();
        selectedRecipeTitle = bundle.getString("title");

        mContext = RecipeDetailsActivity.this;
        myDBHelper = new MyDBOpenHelper(mContext, "my.db", null, 1);
        db = myDBHelper.getWritableDatabase();

        selectedRecipeContents = query(selectedRecipeTitle);

        editableTitle = (EditText)findViewById(R.id.EditableTitle);
        editableContents = (EditText)findViewById(R.id.EditableContent);

        editableTitle.setText(selectedRecipeTitle);
        editableContents.setText(selectedRecipeContents);
    }

    private String query(String title) {

        String content = null;
        Cursor cursor = db.rawQuery("select * from recipe where title = ?", new String[]{title});

        if(cursor.moveToFirst())
        {
            content = cursor.getString(cursor.getColumnIndex("contents"));
        }
        cursor.close();

        return content;
    }
}
