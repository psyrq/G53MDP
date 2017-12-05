package com.example.psyrq.RecipeBook;

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

public class RecipeContentProvider extends ContentProvider {

    //codes for four kinds of actions
    final static int ID_INSERT = 1;
    final static int ID_QUERY = 2;
    final static int ID_UPDATE = 3;
    final static int ID_DELETE = 4;

    private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    private MyDBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;

//    final static String AUTHORITY = "com.example.psyrq.myprovider";
//    final static Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    //register all the uris
    static {
        matcher.addURI(ProviderContract.AUTHORITY, "recipe/insert", ID_INSERT);
        matcher.addURI(ProviderContract.AUTHORITY, "recipe/query", ID_QUERY);
        matcher.addURI(ProviderContract.AUTHORITY, "recipe/update", ID_UPDATE);
        matcher.addURI(ProviderContract.AUTHORITY, "recipe/delete", ID_DELETE);
    }

    @Override
    public boolean onCreate() {
        dbOpenHelper = new MyDBOpenHelper(this.getContext(), "my.db", null, 1);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        switch (matcher.match(uri)) {
            case ID_QUERY:
                db = dbOpenHelper.getReadableDatabase();
                Cursor cursor = db.query("recipe", projection, selection, selectionArgs, null, null, sortOrder);
                //cursor.setNotificationUri(getContext().getContentResolver(), BASE_URI);
                return cursor;
            default:
                throw new IllegalArgumentException("unknown uri " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        switch (matcher.match(uri)) {
            case ID_INSERT:
                db = dbOpenHelper.getWritableDatabase();
                long id = db.insert("recipe", null, values);
                if(id > 0) {
                    Uri recipeUri = ContentUris.withAppendedId(uri, id);
                    getContext().getContentResolver().notifyChange(recipeUri, null);
                    return recipeUri;
                }
            default:
                throw new IllegalArgumentException("unknown uri " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        switch (matcher.match(uri)) {
            case ID_DELETE:
                db = dbOpenHelper.getWritableDatabase();
                int id = db.delete("recipe", selection, selectionArgs);
                getContext().getContentResolver().notifyChange(ProviderContract.BASE_URI, null);
                return id;
            default:
                throw new IllegalArgumentException("unknown uri " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        switch (matcher.match(uri)) {
            case ID_UPDATE:
                db = dbOpenHelper.getWritableDatabase();
                int id = db.update("recipe", values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(ProviderContract.BASE_URI, null);
                return id;
            default:
                throw new IllegalArgumentException("unknown uri " + uri);
        }
    }
}
