package com.example.psyrq.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by qianruofei on 2017/12/1.
 */

public class NameContentProvider extends ContentProvider {

    private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    private MyDBOpenHelper dbOpenHelper;

    static {
        matcher.addURI("com.example.psyrq.myprovider", "person", 1);
    }

    @Override
    public boolean onCreate() {
        dbOpenHelper = new MyDBOpenHelper(this.getContext(), "my.db", null, 1);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        switch (matcher.match(uri)) {
            case 1:
                SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
                long id = db.insert("person", null, values);
                if(id > 0) {
                    Uri nameUri = ContentUris.withAppendedId(uri, id);
                    getContext().getContentResolver().notifyChange(nameUri, null);
                    return nameUri;
                }
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
