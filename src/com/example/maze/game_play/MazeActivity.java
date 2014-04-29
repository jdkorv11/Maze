package com.example.maze.game_play;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.widget.*;
import com.example.maze.MainActivity;
import com.example.maze.R;
import com.example.maze.ThemeDrawables;
import com.example.maze.high_scores.HSData;
import com.example.maze.high_scores.HighScoresActivity;
import com.example.maze.high_scores.MazeScore;
import com.example.maze.high_scores.SessionHighScores;
import com.example.maze.level_select.LevelSelectActivity;

import java.io.IOException;
import java.io.InputStream;

public class MazeActivity extends Activity {

    private Maze maze;
    private RelativeLayout layout;
    private GridLayout mazeDisplay;
    private Location currentLocation;
    private boolean alreadyStarted = false;
    private SessionHighScores sessionScores;
    private HSData hsData;
    private String level;

    public static final String LEVEL = "level";
    private static MovementController.MovementListener listener;
    private MovementController eventController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionScores = SessionHighScores.instance();
        hsData = HSData.instance();

        layout = (RelativeLayout) getLayoutInflater().inflate(R.layout.maze_display, null);
        setContentView(layout);
        layout.setBackgroundResource(ThemeDrawables.getBackground());


        mazeDisplay = (GridLayout) findViewById(R.id.mazeHolder);
        mazeDisplay.setUseDefaultMargins(false);

        eventController = new MovementController(this);
        listener = new MovementController.MovementListener() {

            @Override
            public void onStep(int direction) {
                // initialize to face south in case of bad direction
                int guyId = MovementController.SOUTH;
                // switch to filter direction of step
                switch (direction) {
                    case MovementController.WEST:
                        guyId = ThemeDrawables.getGuyWest();
                        break;
                    case MovementController.NORTH:
                        guyId = ThemeDrawables.getGuyNorth();
                        break;
                    case MovementController.EAST:
                        guyId = ThemeDrawables.getGuyEast();
                        break;
                    case MovementController.SOUTH:
                        guyId = ThemeDrawables.getGuySouth();
                        break;
                    default:
                        Log.v("onStep", "invalid direction");
                }
                // display the step taken
                mazeDisplay.removeAllViews();
                displayMaze(guyId);
                mazeDisplay.postInvalidate();

                if (maze.isFinish(currentLocation)) {
                    // change display to a congratulations screen with a play
                    // again button
                    completeLevel();
                }
            }
        };
        level = getIntent().getExtras().getString(LEVEL);
        String levelFileName = String.format("Mazes/Level_%s", level);
        eventController.addMovementListener(listener);
        try {
            AssetManager am = getAssets();
            InputStream is = am.open(levelFileName);
            maze = new Maze(is);
        } catch (IOException e) {
            startActivity(new Intent(this, MainActivity.class));
        }

        TextView title = (TextView) findViewById(R.id.maze_activity_score);
        title.setText("Score to beat: " + String.valueOf(sessionScores.getScore(level)));

        currentLocation = maze.getStart();

        displayMaze(ThemeDrawables.getGuySouth());
    }

    private void completeLevel() {
        int score = calculateScore();
        sessionScores.submitScore(level, score);

        int totalScore = sessionScores.getTotalScore();
        // if there is a new high score, bring the score submission dialog up
        // else bring up the normal end of level dialog
        if (hsData.isHighScore(totalScore)) {
            offerSubmitScore(totalScore, score);
        } else {
            displayEndOfLevelOptions(totalScore, score);
        }
    }

    private void offerSubmitScore(final int totalScore, final int score) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You got a new High Score!");
        builder.setMessage("Level Score: " + score +
                "\nTotal Score: " + totalScore +
                "\n\nEnd your game and submit score?");
        builder.setNegativeButton("Keep Playing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                displayEndOfLevelOptions(totalScore, score);
            }
        });
        builder.setPositiveButton("Submit Score", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                sessionScores.reset();
                submitPlayerScore(totalScore, score);
            }
        });
        builder.create();
        builder.show();
    }

    private void submitPlayerScore(final int totalScore, int score) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.score_submit_dialog);
        dialog.setTitle("New High Score!");

        TextView tv = (TextView) dialog.findViewById(R.id.submit_dialog_score);
        tv.setText("Total Score: " + totalScore + "\nLevel Score: " + score);

        Button button = (Button) dialog.findViewById(R.id.submit_score_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String player;
                EditText et = ((EditText) dialog.findViewById(R.id.submit_dialog_player));
                String enteredName = et.getText().toString();
                if (enteredName.length() > 0) {
                    player = enteredName;
                } else {
                    player = et.getHint().toString();
                }
                hsData.submitScore(new MazeScore(player, totalScore));
                dialog.dismiss();
                startActivity(new Intent(MazeActivity.this, HighScoresActivity.class));
            }
        });
        dialog.show();
    }

    private void displayEndOfLevelOptions(int totalScore, int score) {
        // alert dialog to contain end of game options
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Level Completed\nScore: " + score + " Total: " + totalScore)
                .setItems(R.array.level_complete_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        final int nextLevel = 0;
                        final int pickLevel = 1;
                        final int mainMenu = 2;
                        final int highScores = 3;
                        switch (which) {
                            case (nextLevel):
                                if (Integer.valueOf(level) < LevelSelectActivity.getNumOfLevels()) {
                                    int gotoLevel = Integer.valueOf(level) + 1;
                                    Intent intent = new Intent(getBaseContext(), MazeActivity.class);
                                    intent.putExtra(MazeActivity.LEVEL, String.valueOf(gotoLevel));
                                    startActivity(intent);
                                } else {
                                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                                }
                                break;
                            case (pickLevel):
                                startActivity(new Intent(getBaseContext(), LevelSelectActivity.class));
                                break;
                            case (mainMenu):
                                startActivity(new Intent(getBaseContext(), MainActivity.class));
                                break;
                            case (highScores):
                                startActivity(new Intent(getBaseContext(), HighScoresActivity.class));
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void move(View v) {
        int direction = -1;
        if (!alreadyStarted) {
            startTimer();
            alreadyStarted = true;
        }
        switch (v.getId()) {
            case R.id.leftBtn:
                if (maze.canGetWest(currentLocation)) {
                    direction = MovementController.WEST;
                    currentLocation = maze.getWest(currentLocation);
                }
                break;
            case R.id.upBtn:
                if (maze.canGetNorth(currentLocation)) {
                    direction = MovementController.NORTH;
                    currentLocation = maze.getNorth(currentLocation);
                }
                break;
            case R.id.rightBtn:
                if (maze.canGetEast(currentLocation)) {
                    direction = MovementController.EAST;
                    currentLocation = maze.getEast(currentLocation);
                }
                break;
            case R.id.downBtn:
                if (maze.canGetSouth(currentLocation)) {
                    direction = MovementController.SOUTH;
                    currentLocation = maze.getSouth(currentLocation);
                }
                break;
            default:
                Log.v("MazeActivity", "invalid call to move method");
        }
        eventController.takeStep(direction);
    }

    private Time startTime;
    private Time endTime;

    private void startTimer() {
        startTime = new Time();
        endTime = new Time();
        startTime.setToNow();
    }

    private int calculateScore() {
        endTime = new Time();
        endTime.setToNow();

        // the elapsedTime in milliseconds
        long elapsedTime = (endTime.toMillis(true) - startTime.toMillis(true));

        // calculate the score so a shorter time results in a higher score
        double score = ((5000.0 / (5000.0 + elapsedTime))) * 5000;
        Log.v("MazeActivity", "Score = " + String.valueOf(score));
        return (int) score;
    }

    private Bitmap finishImg;
    private Bitmap startImg;
    private Bitmap wallImg;
    private Bitmap pathImg;
    private Bitmap guyImg;

    private void displayMaze(int guyDrawableId) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int dispWidth = size.x;
        mazeDisplay.setColumnCount(maze.getWidth());
        mazeDisplay.setRowCount(maze.getHeight());
        int imageSize = (dispWidth - 20) / maze.getWidth();

        finishImg = BitmapFactory.decodeResource(getResources(), ThemeDrawables.getFinish());
        finishImg = Bitmap.createScaledBitmap(finishImg, imageSize, imageSize, true);
        startImg = BitmapFactory.decodeResource(getResources(), ThemeDrawables.getStart());
        startImg = Bitmap.createScaledBitmap(startImg, imageSize, imageSize, true);
        wallImg = BitmapFactory.decodeResource(getResources(), ThemeDrawables.getWall());
        wallImg = Bitmap.createScaledBitmap(wallImg, imageSize, imageSize, true);
        pathImg = BitmapFactory.decodeResource(getResources(), ThemeDrawables.getPath());
        pathImg = Bitmap.createScaledBitmap(pathImg, imageSize, imageSize, true);
        guyImg = BitmapFactory.decodeResource(getResources(), guyDrawableId);
        guyImg = Bitmap.createScaledBitmap(guyImg, imageSize, imageSize, true);

        for (int i = 0; i < maze.getHeight(); i++) {
            for (int j = 0; j < maze.getWidth(); j++) {
                Location l = maze.getLocation(i, j);
                switch (l.getType()) {
                    case FINISH:
                        ImageView finishView = new ImageView(this);
                        finishView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        if (currentLocation == l) {
                            finishView.setImageBitmap(guyImg);
                        } else {
                            finishView.setImageBitmap(finishImg);
                        }

                        mazeDisplay.addView(finishView);
                        break;
                    case PATHWAY:
                        ImageView pathView = new ImageView(this);
                        pathView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        if (currentLocation == l) {
                            pathView.setImageBitmap(guyImg);
                        } else {
                            pathView.setImageBitmap(pathImg);
                        }

                        mazeDisplay.addView(pathView);
                        break;
                    case START:
                        ImageView startView = new ImageView(this);
                        startView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        if (currentLocation == l) {
                            startView.setImageBitmap(guyImg);
                        } else {
                            startView.setImageBitmap(startImg);
                        }

                        mazeDisplay.addView(startView);
                        break;
                    case WALL:
                        ImageView wallView = new ImageView(this);
                        wallView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        wallView.setImageBitmap(wallImg);

                        mazeDisplay.addView(wallView);
                        break;
                }
            }
        }
    }
}
