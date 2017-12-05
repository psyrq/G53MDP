package com.example.psyrq.RecipeBook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button btn_insert;
    private Button btn_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_insert = findViewById(R.id.btn_insert);
        btn_query = findViewById(R.id.btn_query);

        //bind action for insert button
        btn_insert.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent insertIntent = new Intent(MainActivity.this, InsertActivity.class);
                startActivity(insertIntent);
            }
        });

        //bind action for query button
        btn_query.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent viewIntent = new Intent(MainActivity.this, ViewActivity.class);
                startActivity(viewIntent);
            }
        });
    }
}