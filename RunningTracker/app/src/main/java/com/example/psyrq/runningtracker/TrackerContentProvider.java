package com.example.psyrq.runningtracker;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by qianruofei on 2018/1/10.
 */

public class TrackerContentProvider extends ContentProvider {

    final static int ID_INSERT = 1;
    final static int ID_QUERY = 2;
    final static int ID_UPDATE = 3;
    final static int ID_DELETE = 4;

    private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    private TrackerDBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;

    static {
        matcher.addURI(TrackerProviderContract.AUTHORITY, "tracker/insert", ID_INSERT);
        matcher.addURI(TrackerProviderContract.AUTHORITY, "tracker/query", ID_QUERY);
        matcher.addURI(TrackerProviderContract.AUTHORITY, "tracker/update", ID_UPDATE);
        matcher.addURI(TrackerProviderContract.AUTHORITY, "tracker/delete", ID_DELETE);
    }

    @Override
    public boolean onCreate() {
        dbOpenHelper = new TrackerDBOpenHelper(this.getContext(), "my.db", null, 1);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        switch(matcher.match(uri)) {
            case ID_QUERY:
                db = dbOpenHelper.getReadableDatabase();
                Cursor cursor = db.query("tracker", projection, selection, selectionArgs, null, null, sortOrder);
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

        switch(matcher.match(uri)) {
            case ID_INSERT:
                db = dbOpenHelper.getWritableDatabase();
                long id = db.insert("tracker", null, values);
                if(id > 0) {
                    Uri trackerUri = ContentUris.withAppendedId(uri, id);
                    getContext().getContentResolver().notifyChange(trackerUri, null);
                    return trackerUri;
                }
            default:
                throw new IllegalArgumentException("unknown uri " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        switch(matcher.match(uri)) {
            case ID_DELETE:
                db = dbOpenHelper.getWritableDatabase();
                int id = db.delete("tracker", selection, selectionArgs);
                getContext().getContentResolver().notifyChange(TrackerProviderContract.BASE_URI, null);
                return id;
            default:
                throw new IllegalArgumentException("unknown uri " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
