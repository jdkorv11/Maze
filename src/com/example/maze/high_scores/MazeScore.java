package com.example.maze.high_scores;

/**
 * Encapsulates the data needed for a maze score
 */
public class MazeScore {
    // Region constants
    public static final int UNSPECIFIED = 0;
    public static final int WEST = 1;
    public static final int CENTRAL = 2;
    public static final int EAST = 3;

    private int score;
    private int region;
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
     * Returns the integer representing the region
     * UNSPECIFIED = 0
     * WEST = 1
     * CENTRAL = 2
     * EAST = 3
     *
     * @return the int indicating the score's region
     */
    public int getRegion() {
        return region;
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
     * @param region int The region the player is within (use class constants)
     */
    public MazeScore(String name, int score, int region) {
        this.name = name;
        this.score = score;
        this.region = region;
    }

    @Override
    public boolean equals(Object obj) {
        MazeScore score1;
        try {
            score1 = (MazeScore) obj;
        } catch (Exception e) {
            return false;
        }
        if (this.getScore() == score1.getScore() &&
                this.getName() == score1.getName() &&
                this.getRegion() == score1.getRegion()) {
            return true;
        } else {
            return false;
        }
    }

}
