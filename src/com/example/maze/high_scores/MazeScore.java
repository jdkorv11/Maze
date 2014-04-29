package com.example.maze.high_scores;

/**
 * Encapsulates the data needed for a maze score
 */
public class MazeScore {

    private int score;
    private String name;

    /**
     * Returns the value of the score object
     *
     * @return int the value of the score object
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the name of the player associated with the score
     *
     * @return String The player associated with the score
     */
    public String getName() {
        return name;
    }

    /**
     * Creates a new MazeScore object
     *
     * @param name String The name of the player associated with the score
     * @param score int The value of the score
     */
    public MazeScore(String name, int score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public boolean equals(Object obj) {
        MazeScore score1;
        try {
            score1 = (MazeScore) obj;
        } catch (Exception e) {
            return false;
        }
        return this.getScore() == score1.getScore() &&
                this.getName() == score1.getName();
    }

}
