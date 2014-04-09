package com.example.maze.high_scores;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.maze.R;

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

    HSData hsData;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_scores);

        hsData = HSData.instance();

        ListView lv = (ListView) findViewById(R.id.highScoresList);
        CustomArrayAdapter adapter = new CustomArrayAdapter(this, R.layout.high_score_list_item,
                hsData.getHighScores());
        lv.setAdapter(adapter);
    }

    /**
     * Custom ArrayAdapter used to populate a ListView from a List of MazeScores
     *
     * note: the ListView must use R.layout.high_score_list_item for item layouts
     */
    private class CustomArrayAdapter extends ArrayAdapter<MazeScore> {

        public CustomArrayAdapter(Context context, int resource, List<MazeScore> objects) {
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