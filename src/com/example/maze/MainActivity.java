package com.example.maze;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.example.maze.high_scores.HighScoresActivity;
import com.example.maze.level_select.LevelSelectActivity;

/**
 * Created by Owner on 3/17/14.
 */
public class MainActivity extends Activity {
    private static final String TAG = "Main Activity";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

    public void onStartGameSelect(View view) {
        Log.v(TAG, "onStartGameSelect");
        startActivity(new Intent(this, LevelSelectActivity.class));
    }

    public void onHighScoresSelect(View view) {
        Log.v(TAG, "onHighScoresSelect");
        startActivity(new Intent(this, HighScoresActivity.class));
    }

    public void onOptionsSelect(View view) {
        Log.v(TAG, "onOptionsSelect");
    }
}