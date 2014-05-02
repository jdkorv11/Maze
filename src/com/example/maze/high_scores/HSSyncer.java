package com.example.maze.high_scores;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;

import com.example.maze.AppInstanceData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi.ContentsResult;
import com.google.android.gms.drive.DriveApi.MetadataBufferResult;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder.DriveFileResult;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

/**
 * Created by Owner on 4/24/14.
 */
public class HSSyncer implements ConnectionCallbacks,
		OnConnectionFailedListener {

	private static DriveFile driveFile;
	private static GoogleApiClient mGoogleApiClient;
	private Activity activity;
	private final String fileName = "Maze_Scores_do_not_modify";
	public static final int RESOLVE_CONNECTION_REQUEST_CODE = 23;
	private static JSONObject jsonScoresList;
	public final static int MODE_READ = 0;
	public final static int MODE_WRITE = 1;
	private int mode;

	public static JSONObject getJSONScores() {
		return jsonScoresList;
	}

	public static void connect() {
		mGoogleApiClient.disconnect();
		mGoogleApiClient.connect();
	}

	public HSSyncer(Activity activity) {
		this.activity = activity;

		mGoogleApiClient = new GoogleApiClient.Builder(activity)
				.addApi(Drive.API).addScope(Drive.SCOPE_FILE)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).build();
	}

	public void pullScores() {
		mode = MODE_READ;
		connect();
	}

	public void submitScoresForSync(ArrayList<MazeScore> scores) {
		jsonScoresList = buildJsonScoreList(scores);
		mode = MODE_WRITE;
		connect();
	}

	private JSONObject buildJsonScoreList(ArrayList<MazeScore> scores) {

		JSONArray jsonScoreArr = new JSONArray();
		for (MazeScore score : scores) {
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
		return jsonScores;
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (connectionResult.hasResolution()) {
			try {
				connectionResult.startResolutionForResult(activity,
						RESOLVE_CONNECTION_REQUEST_CODE);
			} catch (IntentSender.SendIntentException e) {
			}
		} else {
			GooglePlayServicesUtil.getErrorDialog(
					connectionResult.getErrorCode(), activity, 0).show();
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		Query query = new Query.Builder().addFilter(
				Filters.eq(SearchableField.TITLE, fileName)).build();
		Drive.DriveApi.query(mGoogleApiClient, query).setResultCallback(
				new ResultCallback<MetadataBufferResult>() {

					@Override
					public void onResult(MetadataBufferResult result) {
						Iterator<Metadata> iter = result.getMetadataBuffer()
								.iterator();

						if (iter.hasNext()) {
							DriveId id = iter.next().getDriveId();
							driveFile = Drive.DriveApi.getFile(
									mGoogleApiClient, id);
							switch (mode) {
							case HSSyncer.MODE_READ:
								new RetrieveDriveFileContentsAsyncTask(activity)
										.execute(id);
								break;
							case HSSyncer.MODE_WRITE:
								new EditContentsAsyncTask(activity)
										.execute(driveFile);
							}
						} else {
							driveFile = null;
						}
					}
				});
		Drive.DriveApi.newContents(mGoogleApiClient).setResultCallback(
				contentsCallback);
	}

	final private ResultCallback<ContentsResult> contentsCallback = new ResultCallback<ContentsResult>() {
		@Override
		public void onResult(ContentsResult result) {
			if (!result.getStatus().isSuccess()) {
				return;
			}
			if (driveFile == null) {
				MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
						.setTitle(fileName).setMimeType("text/plain")
						.setStarred(true).build();
				// create a file on root folder
				Drive.DriveApi
						.getRootFolder(mGoogleApiClient)
						.createFile(mGoogleApiClient, changeSet,
								result.getContents())
						.setResultCallback(fileCallback);
			}
		}
	};

	final private ResultCallback<DriveFileResult> fileCallback = new ResultCallback<DriveFileResult>() {
		@Override
		public void onResult(DriveFileResult result) {
			if (!result.getStatus().isSuccess()) {
				return;
			}
		}
	};

	@Override
	public void onConnectionSuspended(int arg0) {
	}

	public class EditContentsAsyncTask extends
			ApiClientAsyncTask<DriveFile, Void, Boolean> {

		private static final String TAG = "EditContentsAsyncTask";

		public EditContentsAsyncTask(Context context) {
			super(context);
		}

		@Override
		protected Boolean doInBackgroundConnected(DriveFile... args) {
			DriveFile file = args[0];
			try {
				ContentsResult contentsResult = file.openContents(
						getGoogleApiClient(), DriveFile.MODE_WRITE_ONLY, null)
						.await();
				if (!contentsResult.getStatus().isSuccess()) {
					return false;
				}
				OutputStream outputStream = contentsResult.getContents()
						.getOutputStream();
				outputStream.write(HSSyncer.getJSONScores().toString()
						.getBytes());
				com.google.android.gms.common.api.Status status = file
						.commitAndCloseContents(getGoogleApiClient(),
								contentsResult.getContents()).await();
				return status.getStatus().isSuccess();
			} catch (IOException e) {
				Log.e(TAG, "IOException while appending to the output stream",
						e);
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (!result) {
				return;
			}
		}
	}

	final private class RetrieveDriveFileContentsAsyncTask extends
			ApiClientAsyncTask<DriveId, Boolean, String> {

		public RetrieveDriveFileContentsAsyncTask(Context context) {
			super(context);
		}

		@Override
		protected String doInBackgroundConnected(DriveId... params) {
			String contents = null;
			DriveFile file = Drive.DriveApi.getFile(getGoogleApiClient(),
					params[0]);
			ContentsResult contentsResult = file.openContents(
					getGoogleApiClient(), DriveFile.MODE_READ_ONLY, null)
					.await();
			if (!contentsResult.getStatus().isSuccess()) {
				return null;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					contentsResult.getContents().getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line;
			try {
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				contents = builder.toString();
			} catch (IOException e) {
				Log.e("ReadDriveFile",
						"IOException while reading from the stream", e);
			}

			file.discardContents(getGoogleApiClient(),
					contentsResult.getContents()).await();
			return contents;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result == null || result.length() == 0) {
				return;
			}
			storeDownloadedScores(result);
		}
	}

	public GoogleApiClient getGoogleApiClient() {
		return mGoogleApiClient;
	}

	public void storeDownloadedScores(String JSONScores) {
		HSData hsData = HSData.instance();
		try {
			JSONObject highScoresData = new JSONObject(JSONScores);
			JSONArray scoresArr = highScoresData
					.getJSONArray(DBContract.HighScores.TABLE);
			for (int i = 0; i < scoresArr.length(); i++) {
				JSONObject jsonScore = scoresArr.getJSONObject(i);
				MazeScore score = new MazeScore(
						jsonScore.getString(DBContract.HighScores.NAME),
						jsonScore.getInt(DBContract.HighScores.SCORE));
				hsData.internalSubmitScore(score);
				AppInstanceData.setSyncDownloaded(true);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
