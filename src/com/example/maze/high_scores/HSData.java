package com.example.maze.high_scores;

import java.util.ArrayList;

/**
 * a class to hold the data for the high scores list
 *
 * used to provide any class with access to the data
 */
public class HSData {

    private int region;

    private ArrayList<MazeScore> highScores;

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
    }
}
