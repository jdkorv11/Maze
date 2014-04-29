package com.example.maze.high_scores;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Owner on 4/24/14.
 */
class HSSyncer {

    public void submitScoresForSync(ArrayList<MazeScore> scores) {
        JSONObject jsonScoresList = buildJsonScoreList(scores);
    }

    private JSONObject buildJsonScoreList(ArrayList<MazeScore> scores) {

        JSONArray jsonScoreArr = new JSONArray();
        for (MazeScore score: scores) {
            JSONObject jsonScore = new JSONObject();
            try {
                jsonScore.put(DBContract.HighScores.NAME, score.getName());
                jsonScore.put(DBContract.HighScores.SCORE, score.getScore());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonScoreArr.put(jsonScore);
        }
        JSONObject jsonScores = new JSONObject();
        try {
            jsonScores.put(DBContract.HighScores.TABLE, jsonScoreArr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String test = jsonScores.toString();

        return jsonScores;
    }
}
