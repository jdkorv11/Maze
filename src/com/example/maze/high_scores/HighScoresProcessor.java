package com.example.maze.high_scores;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Abstract class used to process MazeScores
 */
public abstract class HighScoresProcessor {

    /**
     * Returns the top ten scores from a given List of MazeScore objects. If the initial list has
     * fewer than ten scores, the entire list is returned.
     *
     * @param scoreList List<MazeScore> An List of MazeScore objects to get the highest scores from
     * @return List<MazeScores> The 10 highest scores from the given List of scores.
     */
    public static ArrayList<MazeScore> getTop10Scores(ArrayList<MazeScore> scoreList) {
        ArrayList<MazeScore> highScoreList = scoreList;
        while (highScoreList.size() > 10) {
            MazeScore lowScore = getLowestScore(highScoreList);
            highScoreList.remove(lowScore);
        }
        return highScoreList;
    }

    /**
     * Returns the lowest score from a List of MazeScore objects. If there are two tied low scores
     * the last one found is returned. If the list is empty, null is returned
     *
     * @param scoreList List<MazeScore> An List of MazeScore objects to get the lowest score from
     * @return MazeScore The lowest MazeScore
     */
    public static MazeScore getLowestScore(ArrayList<MazeScore> scoreList) {

        MazeScore lowScore = null;
        Iterator<MazeScore> iter = scoreList.iterator();

        while (iter.hasNext()) {
            MazeScore tempScore = iter.next();
            if (lowScore == null || tempScore.getScore() <= lowScore.getScore()) {
                lowScore = tempScore;
            }
        }
        return lowScore;
    }
}
