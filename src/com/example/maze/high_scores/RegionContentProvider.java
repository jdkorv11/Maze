package com.example.maze.high_scores;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class RegionContentProvider extends ContentProvider {

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.v("RegionProvider", "delete");
        Regions
                .instance(getContext())
                .getWritableDatabase()
                .delete(DBContract.Region.TABLE, selection,
                        selectionArgs);
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        Log.v("RegionProvider", "getType");
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.v("RegionProvider", "insert");
        Regions.instance(getContext()).getWritableDatabase()
                .insert(DBContract.Region.TABLE, null, values);
        return null;
    }

    @Override
    public boolean onCreate() {
        Log.v("RegionProvider", "onCreate");
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.v("Region" +
                "RegionProvider", "query");
        return Regions
                .instance(getContext())
                .getReadableDatabase()
                .query(DBContract.Region.TABLE, projection, selection,
                        selectionArgs, null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String whereClause,
                      String[] whereArgs) {
        Regions
                .instance(getContext())
                .getWritableDatabase()
                .update(DBContract.Region.TABLE, values, whereClause,
                        whereArgs);
        Log.v("RegionProvider", "update");
        return 0;
    }
    public static class Regions extends SQLiteOpenHelper {
        private static Regions registrar = null;
        public static final String DB_NAME = "HighScores";
        public static final int SCHEMA_VERSION = 1;
        private static final String CREATE_TABLE_SQL = "CREATE TABLE "
                + DBContract.Region.TABLE + "("
                + DBContract.Region._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBContract.Region.EAST_LONG + " INTEGER,"
                + DBContract.Region.WEST_LONG + " INTEGER,"
                + DBContract.Region.NAME + " TEXT);";

        public static Regions instance(Context context) {
            if (registrar == null) {
                registrar = new Regions(context);
            }
            return registrar;
        }

        private Regions(Context context) {
            super(context, DB_NAME, null, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.beginTransaction();
                db.execSQL(CREATE_TABLE_SQL);
                db.setTransactionSuccessful();
                Log.v("DB", "DB created");
                Log.v("DB", "DB filled");
            } catch (Exception e) {
                Log.v("DB", "DB creation failed");
                e.printStackTrace(System.err);
            } finally {
                db.endTransaction();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }


    }
}