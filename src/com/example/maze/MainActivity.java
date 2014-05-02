package com.example.maze;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.maze.high_scores.HSSyncer;
import com.example.maze.high_scores.HighScoresActivity;
import com.example.maze.level_select.LevelSelectActivity;
import com.example.maze.options.OptionsActivity;

/**
 * Created by Owner on 3/17/14.
 */
public class MainActivity extends Activity {

	private static final String TAG = "Main Activity";
	public static Context appContext;
	public static SharedPreferences prefs;
	private static HSSyncer syncer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appContext = getApplicationContext();
		// make all static classes have the proper values from preferences
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		int theme = Integer.valueOf(prefs.getString(
				OptionsActivity.THEME_PREF_KEY, "0"));
		ThemeDrawables.setTheme(theme);
		RelativeLayout layout = (RelativeLayout) getLayoutInflater().inflate(
				R.layout.main_activity, null);
		layout.setBackgroundResource(ThemeDrawables.getBackground());
		setContentView(layout);

		if (MainActivity.prefs.getBoolean(OptionsActivity.SYNC_PREF_KEY, false)) {
			//TODO make pull scores
            syncer = new HSSyncer(this);
            syncer.pullScores();
        }

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
		startActivity(new Intent(this, OptionsActivity.class));
	}
	
	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		switch (requestCode) {
		case HSSyncer.RESOLVE_CONNECTION_REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				HSSyncer.connect();
			}
			break;
		}
	}

	public static HSSyncer getSyncer() {
		return syncer;
	}
}