package com.example.maze.level_select;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.maze.R;
import com.example.maze.ThemeDrawables;
import com.example.maze.game_play.MazeActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Activity that gives the player the chance to
 * choose what level he/she wishes to play
 * <p/>
 * Created by Joshua Korver on 2/23/14.
 */

public class LevelSelectActivity extends Activity {

    private static int numOfLevels;
    private RelativeLayout layout;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = (RelativeLayout) getLayoutInflater().inflate(R.layout.level_select, null);
        layout.setBackgroundResource(ThemeDrawables.getBackground());
        setContentView(layout);
        fillLevels();
    }

    private void fillLevels() {

        AssetManager am = getAssets();
        try {
            Scanner indexReader;
            indexReader = new Scanner(am.open("Mazes/LevelIndex"));
            numOfLevels = indexReader.nextInt();
            indexReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GridView gv = (GridView) findViewById(R.id.gridView);

        // set the vertical space to be equal to the horizontal space
        BitmapDrawable bd = (BitmapDrawable) this.getResources().getDrawable(ThemeDrawables.getPreview());
        int itemWidth = bd.getBitmap().getWidth();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        // buffer width to keep grid from getting too close to edge
        int margin = 25;
        int dispWidth = size.x;
        int buffer = 10;
        int numOfCols = (dispWidth - (margin * 2)) / (itemWidth + buffer);
        int blankSpace = dispWidth - (numOfCols * itemWidth);
        int horzGap = (blankSpace - (margin * 2)) / (numOfCols);
        gv.setVerticalSpacing(horzGap);
        gv.setNumColumns(numOfCols);

        // ArrayList of levels to put into the list
        ArrayList<String> levels = new ArrayList<String>();
        for (int i = 1; i <= numOfLevels; i++) {
            levels.add(String.valueOf(i));
        }

        // set put the items in the levels list into the ListView
        //noinspection unchecked
        gv.setAdapter(new LevelSelectArrayAdapter(this, R.layout.level_select_list_item, levels));
    }

    /* when a level is clicked, start MazeActivity at that level */
    public void onLevelSelect(View v) {

        // get the level number from the TextView of the list item clicked
        //noinspection ConstantConditions
        String level = (((TextView) v.findViewById(R.id.level_select_item_level_number)).getText().toString());

        Intent intent = new Intent(this, MazeActivity.class);
        intent.putExtra(MazeActivity.LEVEL, level);

        startActivity(intent);
    }

    public static int getNumOfLevels() {
        return numOfLevels;
    }

    /**
     * Custom ArrayAdapter used to populate a ListView from a List of MazeScores
     * <p/>
     * note: the ListView must use R.layout.high_score_list_item for item layouts
     */
    private class LevelSelectArrayAdapter extends ArrayAdapter<String> {

        public LevelSelectArrayAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.level_select_list_item, parent, false);
                v.setBackgroundResource(ThemeDrawables.getPreview());

                bindView(position, v);
            } else {
                bindView(position, v);
            }
            return v;
        }

        private void bindView(int position, View v) {
            TextView level_num = (TextView) v.findViewById(R.id.level_select_item_level_number);
            level_num.setText(String.valueOf(position + 1));

        }
    }
}
