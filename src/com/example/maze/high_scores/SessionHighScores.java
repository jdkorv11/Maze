package com.example.maze.high_scores;

import android.util.Log;

import java.util.HashMap;

/**
 * A singleton class that stores the local high score for each level
 * as well as the total score
 */
public class SessionHighScores {

    private static SessionHighScores instance = null;

    private final String TAG = "SessionHighScores";

    private HashMap<String, Integer> levelScores;
    private int totalScore = 0;

    /**
     * Submit a score for a level. If the submitted score is higher
     * than the previous score or if there is no previous score for
     * the given level, the submitted score is stored for the level.
     *
     * @param level String The level the given score will be associated with
     * @param score int The score being submitted
     */
    public void submitScore(String level, int score) {
        try {
            int oldScore = levelScores.get(level);
            if (score > oldScore) {
                levelScores.remove(level);
                levelScores.put(level, score);
                totalScore += score - oldScore;
            }
        } catch (Exception e) {
            levelScores.put(level, score);
            totalScore += score;
        } finally {
            Log.v(TAG, "submitted score for level " + level);
            Log.v(TAG, "level " + level + " score = " + String.valueOf(score));
            Log.v(TAG, "Total score = " + String.valueOf(totalScore));
        }
    }

    /**
     * Returns the total score for all the levels completed in the current game
     *
     * @return int the total score for all completed levels
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Returns the current high score for the requested level
     *
     * @param level String The level the given score will be associated with
     * @return int the score for the level requested 0 if level not found
     */
    public int getScore(String level) {
        int score = 0;
        try {
            score = levelScores.get(level);
            Log.v(TAG, "returned score for level " + level);
        } catch (Exception e) {
            Log.v(TAG, "no score for level " + level + ". returned 0");
        }
        return score;
    }

    /**
     * Resets the scores for the current session;
     */
    public void reset() {
        levelScores = new HashMap<String, Integer>();
        totalScore = 0;
        Log.v(TAG, "reset session scores");
    }

    /**
     * Returns an instance of SessionHighScores used for scoring the game
     *
     * @return SessionHighScores A singleton instance of SessionHighScores
     */
    public static SessionHighScores instance() {
        if (instance == null) {
            instance = new SessionHighScores();
        }
        return instance;
    }

    // private constructor to force the use of a single instance
    private SessionHighScores() {
        levelScores = new HashMap<String, Integer>();
    }
}
