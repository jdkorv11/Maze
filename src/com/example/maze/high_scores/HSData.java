package com.example.maze.high_scores;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import com.example.maze.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * a class to hold the data for the high scores list
 * <p/>
 * used to provide any class with access to the data
 */
public class HSData {

    private int region;

    private ArrayList<MazeScore> highScores;
    private ReverseMazeScoreComparator mazeScoreComparator;
    HighScoreLoader dBScoreLoader;
    HighScoreWriter dBScoreWriter;

    public boolean isHighScore(int score) {
        MazeScore testScore = new MazeScore("dummyName", score, MazeScore.UNSPECIFIED);
        ArrayList<MazeScore> testList = (ArrayList<MazeScore>) highScores.clone();
        testList.add(testScore);
        ArrayList<MazeScore> resultList = HighScoresProcessor.getTop10Scores(testList);
        return resultList.contains(testScore);
    }

    public void submitScore(MazeScore score) {
        internalSubmitScore(score);
        //TODO make able to be done more than once
        dBScoreWriter.execute(MainActivity.appContext);
        dBScoreWriter = new HighScoreWriter();
    }

    private void internalSubmitScore(MazeScore score) {
        highScores.add(score);
        highScores = HighScoresProcessor.getTop10Scores(highScores);
    }

    public ArrayList<MazeScore> getHighScores() {
        Collections.sort(highScores, mazeScoreComparator);
        return highScores;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public int getRegion() {
        return region;
    }

    private static HSData instance;

    /**
     * Returns an instance of SessionHighScores used for scoring the game
     *
     * @return SessionHighScores A singleton instance of SessionHighScores
     */
    public static HSData instance() {
        if (instance == null) {
            instance = new HSData();
        }
        return instance;
    }

    // private constructor to force the use of a single instance
    private HSData() {
        highScores = new ArrayList<MazeScore>();
        mazeScoreComparator = new ReverseMazeScoreComparator();
        dBScoreLoader = new HighScoreLoader();
        dBScoreLoader.execute(MainActivity.appContext);
        dBScoreWriter = new HighScoreWriter();

    }

    private class HighScoreLoader extends AsyncTask<Context, Integer, Cursor> {
        private Context context;
        private HSData data;

        @Override
        protected Cursor doInBackground(Context... params) {
            data = HSData.instance();
            context = params[0];
            Cursor c = context.getContentResolver().query(
                    DBContract.CONTENT_URI,
                    new String[]{DBContract.HighScores._ID, DBContract.HighScores.NAME,
                            DBContract.HighScores.SCORE, DBContract.HighScores.REGION},
                    null, null, null);
            return c;
        }

        @Override
        protected void onPostExecute(Cursor c) {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                int score = c.getInt(c.getColumnIndex(DBContract.HighScores.SCORE));
                if (c.getInt(c.getColumnIndex(DBContract.HighScores.REGION)) == data.getRegion()) {
                    String name = c.getString(c.getColumnIndex(DBContract.HighScores.NAME));
                    data.internalSubmitScore(new MazeScore(name, score, data.getRegion()));
                }
                c.moveToNext();
            }
        }
    }

    private class HighScoreWriter extends AsyncTask<Context, Integer, Boolean> {
        private Context context;
        HSData data;

        @Override
        protected Boolean doInBackground(Context... params) {
            context = params[0];
            data = HSData.instance();
            context.getContentResolver().delete(DBContract.CONTENT_URI, DBContract.HighScores.SCORE, null);
            ArrayList<MazeScore> highScores = data.getHighScores();
            Iterator<MazeScore> hsIter = highScores.iterator();
            while (hsIter.hasNext()) {
                MazeScore score = hsIter.next();
                ContentValues putScore = new ContentValues();
                putScore.put(DBContract.HighScores.NAME, score.getName());
                putScore.put(DBContract.HighScores.SCORE, score.getScore());
                putScore.put(DBContract.HighScores.REGION, score.getRegion());
                context.getContentResolver().insert(DBContract.CONTENT_URI, putScore);
            }
            return true;
        }
    }

    public class ReverseMazeScoreComparator implements Comparator<MazeScore> {
        /**
         * Compares 2 MazeScore objects to order then greatest to least
         *
         * @param lhs the first MazeScore
         * @param rhs the second MazeScore
         * @return a negative integer, zero, or a positive integer as the first argument is greater than, equal to,
         * or less than the second.
         */
        @Override
        public int compare(MazeScore lhs, MazeScore rhs) {
            return rhs.getScore() - lhs.getScore();
        }
    }
}
