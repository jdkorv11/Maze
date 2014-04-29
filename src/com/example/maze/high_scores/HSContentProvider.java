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
        ScoreDB
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
        ScoreDB.instance(getContext()).getWritableDatabase()
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
        return ScoreDB
                .instance(getContext())
                .getReadableDatabase()
                .query(DBContract.HighScores.TABLE, projection, selection,
                        selectionArgs, null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String whereClause,
                      String[] whereArgs) {
        ScoreDB
                .instance(getContext())
                .getWritableDatabase()
                .update(DBContract.HighScores.TABLE, values, whereClause,
                        whereArgs);
        Log.v("CoursesProvider", "update");
        return 0;
    }

    public static class ScoreDB extends SQLiteOpenHelper {
        private static ScoreDB scoreDB = null;
        public static final String DB_NAME = "HighScores";
        public static final int SCHEMA_VERSION = 1;
        private static final String CREATE_TABLE_SQL = "CREATE TABLE "
                + DBContract.HighScores.TABLE + "("
                + DBContract.HighScores._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBContract.HighScores.SCORE + " INTEGER,"
                + DBContract.HighScores.NAME + " TEXT);";
        private static final String FILL_TABLE_SQL = "INSERT INTO " +
                DBContract.HighScores.TABLE + " (" +
                DBContract.HighScores.NAME + "," +
                DBContract.HighScores.SCORE + ") VALUES ('Player', 0);";

        public static ScoreDB instance(Context context) {
            if (scoreDB == null) {
                scoreDB = new ScoreDB(context);
            }
            return scoreDB;
        }

        private ScoreDB(Context context) {
            super(context, DB_NAME, null, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.beginTransaction();
                db.execSQL(CREATE_TABLE_SQL);
                Log.v("DB", "DB created");
                for (int i = 0; i < 10; i++) {
                    db.execSQL(FILL_TABLE_SQL);
                    Log.v("DB", "inserted row");
                }
                Log.v("DB", "successfully completed insertion");
                db.setTransactionSuccessful();
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