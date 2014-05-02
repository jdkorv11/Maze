package com.example.maze;

import java.util.Iterator;

import org.apache.http.impl.io.ContentLengthInputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import com.example.maze.high_scores.HighScoresActivity;
import com.example.maze.high_scores.HSSyncer;
import com.example.maze.level_select.LevelSelectActivity;
import com.example.maze.options.OptionsActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi.ContentsResult;
import com.google.android.gms.drive.DriveApi.DriveIdResult;
import com.google.android.gms.drive.DriveApi.MetadataBufferResult;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveFolder.DriveFileResult;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

/**
 * Created by Owner on 3/17/14.
 */
public class MainActivity extends Activity implements ConnectionCallbacks,
		OnConnectionFailedListener {

	private static final String TAG = "Main Activity";
	private static final int RESOLVE_CONNECTION_REQUEST_CODE = 0;
	public static Context appContext;
	public static SharedPreferences prefs;
	private GoogleApiClient mGoogleApiClient;

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

		mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Drive.API)
				.addScope(Drive.SCOPE_FILE).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).build();
	}

	@Override
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
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
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (connectionResult.hasResolution()) {
			try {
				connectionResult.startResolutionForResult(this,
						RESOLVE_CONNECTION_REQUEST_CODE);
			} catch (IntentSender.SendIntentException e) {
				// Unable to resolve, message user appopriately
			}
		} else {
			GooglePlayServicesUtil.getErrorDialog(
					connectionResult.getErrorCode(), this, 0).show();
		}
	}

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		switch (requestCode) {
		case RESOLVE_CONNECTION_REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				mGoogleApiClient.connect();
			}
			break;
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}

	private final String fileName = "Maze_Scores_Do_Not_Modify";

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		Query query = new Query.Builder().addFilter(
				Filters.eq(SearchableField.TITLE, fileName)).build();
		Drive.DriveApi.query(mGoogleApiClient, query).setResultCallback(
				new ResultCallback<MetadataBufferResult>() {

					@Override
					public void onResult(MetadataBufferResult result) {
						// TODO Auto-generated method stub
						int count = result.getMetadataBuffer().getCount();
						if (count == 0) {
							// TODO make needed file
							Log.v("", "empty iter");
						}
						Iterator<Metadata> iter = result.getMetadataBuffer()
								.iterator();
						if (iter.hasNext()) {
							DriveId id = iter.next().getDriveId();
							HSSyncer.setDriveFile(Drive.DriveApi.getFile(
									mGoogleApiClient, id));
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
				// showMessage("Error while trying to create new file contents");
				return;
			}
			if (HSSyncer.getDriveFile() == null) {
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
				// showMessage("Error while trying to create the file");
				return;
			}
			// showMessage("Created a file: " +
			// result.getDriveFile().getDriveId());
		}
	};
}