package com.example.psyrq.RecipeBook;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RecipeDetailsActivity extends AppCompatActivity {

    private Context mContext;

    private Bundle bundle;
    private EditText editableTitle;
    private EditText editableContents;
    private Button btn_update;
    private Button btn_delete;

    private int selectedId;
    private String[] selectedRecipe = new String[2];

    private String updatedTitle;
    private String updatedContents;

    private SQLiteDatabase db;
    private MyDBOpenHelper myDBHelper;

    ContentResolver resolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        resolver = this.getContentResolver();

        //get selected id
        bundle = getIntent().getExtras();
        selectedId = bundle.getInt("id");

        mContext = RecipeDetailsActivity.this;
        myDBHelper = new MyDBOpenHelper(mContext, "my.db", null, 1);
        db = myDBHelper.getWritableDatabase();

        //call query function to get title and contents of the selectd recipe
        selectedRecipe = query(selectedId);

        editableTitle = (EditText)findViewById(R.id.EditableTitle);
        editableContents = (EditText)findViewById(R.id.EditableContent);

        //set EditText field with the value in selected record
        editableTitle.setText(selectedRecipe[0]);
        editableContents.setText(selectedRecipe[1]);

        btn_update = (Button)findViewById(R.id.updateRecipe);
        btn_delete = (Button)findViewById(R.id.deleteRecipe);
    }

    private String[] query(int id) {

        String[] content = new String[2];
        Cursor cursor = resolver.query(ProviderContract.URI.ID_QUERY, null, null, null, null);

        if(cursor.moveToFirst()) {
            do{
                if(cursor.getInt(cursor.getColumnIndex("recipeId")) == id) {
                    content[0] = cursor.getString(cursor.getColumnIndex("title"));
                    content[1] = cursor.getString(cursor.getColumnIndex("contents"));
                    break;
                }
            } while(cursor.moveToNext());
        }

        return content;
    }

    //update the selected recipe
    public void update(View v) {

        updatedTitle = editableTitle.getText().toString();
        updatedContents = editableContents.getText().toString();

        ContentValues titleValue = new ContentValues();
        titleValue.put("title", updatedTitle);
        titleValue.put("contents", updatedContents);

        resolver.update(ProviderContract.URI.ID_UPDATE, titleValue, "recipeId = ?", new String[]{String.valueOf(selectedId)});
        Toast.makeText(getApplicationContext(), "update finished", Toast.LENGTH_SHORT).show();

        finish();
    }

    //delete the selected recipe
    public void delete(View v) {

        resolver.delete(ProviderContract.URI.ID_DELETE, "recipeId = ?", new String[]{String.valueOf(selectedId)});
        Toast.makeText(getApplicationContext(), "delete finished", Toast.LENGTH_SHORT).show();

        finish();
    }
}
