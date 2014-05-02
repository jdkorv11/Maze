package com.example.maze.high_scores;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import com.example.maze.MainActivity;
import com.example.maze.options.OptionsActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * a class to hold the data for the high scores list
 * <p/>
 * used to provide any class with access to the data
 */
public class HSData {

	private ArrayList<MazeScore> highScores;
	private final ReverseMazeScoreComparator mazeScoreComparator;

	public boolean isHighScore(int score) {
		MazeScore testScore = new MazeScore("dummyName", score);
		@SuppressWarnings("unchecked")
		ArrayList<MazeScore> testList = (ArrayList<MazeScore>) highScores
				.clone();
		testList.add(testScore);
		ArrayList<MazeScore> resultList = HighScoresComparer
				.getTop10Scores(testList);
		return resultList.contains(testScore);
	}

	public void submitScore(MazeScore score) {
		internalSubmitScore(score);

		Context context = MainActivity.appContext;
		context.getContentResolver().delete(DBContract.CONTENT_URI,
				DBContract.HighScores.SCORE, null);
		HSData data = HSData.instance();
		ArrayList<MazeScore> highScores = data.getHighScores();
		for (MazeScore mScore : highScores) {
			ContentValues putScore = new ContentValues();
			putScore.put(DBContract.HighScores.NAME, mScore.getName());
			putScore.put(DBContract.HighScores.SCORE, mScore.getScore());
			context.getContentResolver().insert(DBContract.CONTENT_URI,
					putScore);
		}

		if (MainActivity.prefs.getBoolean(OptionsActivity.SYNC_PREF_KEY, false)) {
			MainActivity.getSyncer().submitScoresForSync(highScores);
		}

	}

	public void internalSubmitScore(MazeScore score) {
		highScores.add(score);
		highScores = HighScoresComparer.getTop10Scores(highScores);
	}

	public ArrayList<MazeScore> getHighScores() {
		Collections.sort(highScores, mazeScoreComparator);
		return highScores;
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
		mazeScoreComparator = new ReverseMazeScoreComparator();
		HighScoreLoader dBScoreLoader = new HighScoreLoader();
		dBScoreLoader.execute(MainActivity.appContext);
	}

	private class HighScoreLoader extends AsyncTask<Context, Integer, Cursor> {
		private Context context;
		private HSData data;

		@Override
		protected Cursor doInBackground(Context... params) {
			data = HSData.instance();
			context = params[0];
			return context.getContentResolver().query(
					DBContract.CONTENT_URI,
					new String[] { DBContract.HighScores._ID,
							DBContract.HighScores.NAME,
							DBContract.HighScores.SCORE }, null, null, null);
		}

		@Override
		protected void onPostExecute(Cursor c) {
			c.moveToFirst();
			for (int i = 0; i < c.getCount(); i++) {
				int score = c.getInt(c
						.getColumnIndex(DBContract.HighScores.SCORE));

				String name = c.getString(c
						.getColumnIndex(DBContract.HighScores.NAME));
				data.internalSubmitScore(new MazeScore(name, score));

				c.moveToNext();
			}
		}
	}

	private class ReverseMazeScoreComparator implements Comparator<MazeScore> {
		/**
		 * Compares 2 MazeScore objects to order them greatest to least
		 * 
		 * @param lhs
		 *            the first MazeScore
		 * @param rhs
		 *            the second MazeScore
		 * @return a negative integer, zero, or a positive integer as the first
		 *         argument is greater than, equal to, or less than the second.
		 */
		@Override
		public int compare(MazeScore lhs, MazeScore rhs) {
			return rhs.getScore() - lhs.getScore();
		}
	}
}
