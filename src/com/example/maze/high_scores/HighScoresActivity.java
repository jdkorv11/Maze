package com.example.maze.high_scores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.example.maze.MainActivity;
import com.example.maze.R;
import com.example.maze.ThemeDrawables;
import com.example.maze.level_select.LevelSelectActivity;

import java.util.List;

/**
 * Created by Joshua Korver on 3/17/14.
 * <p/>
 * Activity to display the high scores
 * <p/>
 * the scores are stored locally on the device database
 * as well as online to let users compete with each other
 */
public class HighScoresActivity extends Activity {

    private HSData hsData;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout layout = (FrameLayout) getLayoutInflater().inflate(R.layout.high_scores, null);
        layout.setBackgroundResource(ThemeDrawables.getBackground());
        setContentView(layout);

        hsData = HSData.instance();

        ListView lv = (ListView) findViewById(R.id.highScoresList);
        lv.setBackgroundResource(ThemeDrawables.getWall());
        HighScoresArrayAdapter adapter = new HighScoresArrayAdapter(this, R.layout.high_score_list_item,
                hsData.getHighScores());
        lv.setAdapter(adapter);
    }

    public void toMainMenu(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void toLevelSelect(View v) {
        startActivity(new Intent(this, LevelSelectActivity.class));
    }

    /**
     * Custom ArrayAdapter used to populate a ListView from a List of MazeScores
     *
     * note: the ListView must use R.layout.high_score_list_item for item layouts
     */
    private class HighScoresArrayAdapter extends ArrayAdapter<MazeScore> {

        public HighScoresArrayAdapter(Context context, int resource, List<MazeScore> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.high_score_list_item, parent,false);

                bindView(position, v);
            } else
            {
                bindView(position, v);
            }
            return v;
        }

        private void bindView(int position, View v) {
            MazeScore score = super.getItem(position);
            if (score != null) {
                TextView name = (TextView) v.findViewById(R.id.hslist_item_name);
                if (name != null) {
                    name.setText(score.getName());
                }
                TextView score_tv = (TextView) v.findViewById(R.id.hslist_item_score);
                if (score_tv != null) {
                    score_tv.setText(String.valueOf(score.getScore()));
                }
            }
        }
    }
}