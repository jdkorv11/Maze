package com.example.maze.high_scores;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * a class to hold the data for the high scores list
 *
 * used to provide any class with access to the data
 */
public class HSData {

    private int region;

    private ArrayList<MazeScore> highScores;
    private ReverseMazeScoreComparator mazeScoreComparator;

    public boolean isHighScore(int score) {
        MazeScore testScore = new MazeScore("dummyName", score, MazeScore.UNSPECIFIED);
        ArrayList<MazeScore> testList = (ArrayList<MazeScore>) highScores.clone();
        testList.add(testScore);
        ArrayList<MazeScore> resultList = HighScoresProcessor.getTop10Scores(testList);
        return resultList.contains(testScore);
    }

    public void submitScore(MazeScore score) {
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


    AsyncTask<Activity, Integer, Cursor> scoreLoader = new HighScoreLoader();

    private class HighScoreLoader extends AsyncTask<Activity, Integer, Cursor> {
        @Override
        protected Cursor doInBackground(Activity... params) {
            return null;
        }
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
        //TODO load data from the database
    }

    public class ReverseMazeScoreComparator implements Comparator<MazeScore> {
        /**
         * Compares 2 MazeScore objects to order then greatest to least
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
