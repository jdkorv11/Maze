package com.example.maze.high_scores;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class HSContentProvider extends ContentProvider {

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.v("ScoresProvider", "delete");
        Registrar
                .instance(getContext())
                .getWritableDatabase()
                .delete(DBContract.HighScores.TABLE, selection,
                        selectionArgs);
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        Log.v("ScoresProvider", "getType");
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.v("ScoresProvider", "insert");
        Registrar.instance(getContext()).getWritableDatabase()
                .insert(DBContract.HighScores.TABLE, null, values);
        return null;
    }

    @Override
    public boolean onCreate() {
        Log.v("ScoresProvider", "onCreate");
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.v("ScoresProvider", "query");
        return Registrar
                .instance(getContext())
                .getReadableDatabase()
                .query(DBContract.HighScores.TABLE, projection, selection,
                        selectionArgs, null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String whereClause,
                      String[] whereArgs) {
        Registrar
                .instance(getContext())
                .getWritableDatabase()
                .update(DBContract.HighScores.TABLE, values, whereClause,
                        whereArgs);
        Log.v("CoursesProvider", "update");
        return 0;
    }

    public static class Registrar extends SQLiteOpenHelper {
        private static Registrar registrar = null;
        public static final String DB_NAME = "HighScores";
        public static final int SCHEMA_VERSION = 1;
        private static final String CREATE_TABLE_SQL = "CREATE TABLE "
                + DBContract.HighScores.TABLE + "("
                + DBContract.HighScores._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBContract.HighScores.REGION + " TEXT,"
                + DBContract.HighScores.SCORE + " INTEGER,"
                + DBContract.HighScores.NAME + " TEXT);";

        public static Registrar instance(Context context) {
            if (registrar == null) {
                registrar = new Registrar(context);
            }
            return registrar;
        }

        private Registrar(Context context) {
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