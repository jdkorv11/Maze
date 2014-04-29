package com.example.maze.high_scores;

import android.net.Uri;

/**
 * A contract for accessing the Database tables
 */
public class DBContract {

    private DBContract() {
    }

    public static final String AUTHORITY = "com.example.maze.high_scores";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public class HighScores {
    public static final String TABLE = "high_scores";
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String SCORE = "score";
    }
}
