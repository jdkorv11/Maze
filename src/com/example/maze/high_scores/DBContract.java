package com.example.maze.high_scores;

import android.net.Uri;

/**
 * A contract for accesing the Database tables
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
    public static final String REGION = "region";
    }

    public class Region{
        public static final String TABLE = "region";
        public static final String _ID = "_id";
        public static final String NAME = "name";
        public static final String EAST_LONG = "east_longitude";
        public static final String WEST_LONG = "west_longitude";
    }
}
