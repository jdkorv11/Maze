package com.example.maze.high_scores;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import com.example.maze.R;

/**
 * Created by Joshua Korver on 3/17/14.
 *
 * Activity to display the high scores
 *
 * the scores are stored locally on the device database
 * as well as online to let users compete with each other
 */
public class HighScoresActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_scores);
    }

    AsyncTask<Activity, Integer, Cursor> scoreLoader = new HighScoreLoader();

    private class HighScoreLoader extends AsyncTask<Activity, Integer, Cursor> {
        @Override
        protected Cursor doInBackground(Activity... params) {
            return null;
        }
    }
}