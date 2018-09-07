package com.example.touni.umpirebuddy;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Variable declarations
    private TextView strikeText;
    private TextView ballText;
    private TextView outText;
    private int strikeInt = 0;
    private int ballInt = 0;
    private int outInt = 0;

    private ActionMode mActionModeCountChange;

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String TEXT = "text";

    private String textPersistent;
    private String walkDisplay = "WALK";
    private String outDisplay = "OUT";

    private TextToSpeech TTS;

    private Boolean temp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        strikeText = findViewById(R.id.strikeCount);
        ballText = findViewById(R.id.ballCount);

        //set text views to activity viewable
        strikeText = findViewById(R.id.strikeCount);
        strikeText.setText(String.valueOf(strikeInt));
        ballText = findViewById(R.id.ballCount);
        ballText.setText(String.valueOf(strikeInt));
        outText = findViewById(R.id.outCount);
        outText.setText(String.valueOf(outInt));

        //need to set this text view to the background of the umpire activity or
        //      divided between the strike text and the ball text.
        //add a hint for the long press function?
        TextView mChangeCountMenu = findViewById(R.id.strikeCount);
        mChangeCountMenu.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mActionModeCountChange != null) {
                    return false;
                }

                mActionModeCountChange = startSupportActionMode(mActionModeCallbackCountChange);
                return true;
            }
        });

        loadData();
        updatePersistentViews();
    }

    // Save the data to persistent storage
    @Override
    public void onStop() {
        super.onStop();
        //Log.d(TAG, "onStop() called");
        saveData();
        if (TTS != null) {
            TTS.stop();
            TTS.shutdown();
        }
    }

    // Save the data to persistent storage
    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.d(TAG, "onDestroy() called");
        saveData();
        if (TTS != null) {
            TTS.stop();
            TTS.shutdown();
        }
    }

    private ActionMode.Callback mActionModeCallbackCountChange = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.getMenuInflater().inflate(R.menu.menu_change_count, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.increment_ball_count:
                    if (ballInt < 3) {
                        ballInt++;
                        ballText.setText(String.valueOf(ballInt));
                        actionMode.finish();
                        return true;
                    } else {
                        if (temp) {
                            TTS.speak(walkDisplay, TextToSpeech.QUEUE_FLUSH, null);
                        }
                        AlertDialog.Builder mWalkBuilder = new AlertDialog.Builder(MainActivity.this);

                        mWalkBuilder.setMessage("WALK!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ballInt = 0;
                                strikeInt = 0;
                                ballText.setText(String.valueOf(ballInt));
                                strikeText.setText(String.valueOf(strikeInt));
                            }
                        }).setNegativeButton("", null).setCancelable(false);

                        AlertDialog alert = mWalkBuilder.create();
                        alert.show();
                        actionMode.finish();
                        return true;
                    }

                case R.id.increment_strike_count:
                    if (strikeInt < 2) {
                        strikeInt++;
                        strikeText.setText(String.valueOf(strikeInt));
                        actionMode.finish();
                        return true;
                    } else {
                        if (temp) {
                            TTS.speak(outDisplay, TextToSpeech.QUEUE_FLUSH, null);
                        }
                        AlertDialog.Builder mStrikeBuilder = new AlertDialog.Builder(MainActivity.this);

                        mStrikeBuilder.setMessage("OUT!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                strikeInt = 0;
                                ballInt = 0;
                                strikeText.setText(String.valueOf(strikeInt));
                                ballText.setText(String.valueOf(ballInt));
                            }
                        }).setNegativeButton("", null).setCancelable(false);

                        AlertDialog alert = mStrikeBuilder.create();
                        alert.show();
                        actionMode.finish();
                        return true;
                    }

                case R.id.decrement_ball_count:
                    if (ballInt == 0) {
                        actionMode.finish();
                        return true;
                    }
                    ballInt--;
                    ballText.setText(String.valueOf(ballInt));
                    actionMode.finish();
                    return true;

                case R.id.decrement_strike_count:
                    if (strikeInt == 0) {
                        actionMode.finish();
                        return true;
                    }
                    strikeInt--;
                    strikeText.setText(String.valueOf(strikeInt));
                    actionMode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mActionModeCountChange = null;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_umpire, menu);
        return true;
    }

        public void ballInc (View view) {
        if (ballInt < 3) {
            ballInt++;
            ballText.setText(String.valueOf(ballInt));
        } else {
            AlertDialog.Builder mWalkBuilder = new AlertDialog.Builder(MainActivity.this);

            mWalkBuilder.setMessage("WALK!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ballInt = 0;
                    strikeInt = 0;
                    ballText.setText(String.valueOf(ballInt));
                    strikeText.setText(String.valueOf(strikeInt));
                }
            }).setNegativeButton("", null).setCancelable(false);

            AlertDialog alert = mWalkBuilder.create();
            alert.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Reset:
                // reset all text fields
                strikeInt = 0;
                ballInt = 0;
                outInt = 0;
                strikeText.setText(String.valueOf(strikeInt));
                ballText.setText(String.valueOf(ballInt));
                outText.setText(String.valueOf(outInt));
                return true;

            case R.id.About:
                //open new activity to display info about app
                saveData();
                Intent intent = new Intent(MainActivity.this, About.class);
                startActivity(intent);
                return true;

            case R.id.Settings:
                //open dialog to turn on/off text-to-speech
                AlertDialog.Builder mTextToSpeech = new AlertDialog.Builder(MainActivity.this);

                mTextToSpeech.setMessage("Turn on text-to-speech").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //turn on text to speech
                        turnOnSpeech();
                        temp = true;
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (TTS != null) {
                            TTS.stop();
                            TTS.shutdown();
                        }
                    }
                }).setCancelable(false);

                AlertDialog alert = mTextToSpeech.create();
                alert.show();
                return true;

            default:

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("textViewBallCount", ballInt);
        outState.putInt("textViewStrikeCount", strikeInt);
        outState.putInt("textViewOutCount", outInt);
    }

    // Restores the saved state to a new activity screen
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ballInt = savedInstanceState.getInt("textViewBallCount");
        ballText.setText(String.valueOf(ballInt));
        strikeInt= savedInstanceState.getInt("textViewStrikeCount");
        strikeText.setText(String.valueOf(strikeInt));
        outInt = savedInstanceState.getInt("textViewOutCount");
        outText.setText(String.valueOf(outInt));
    }

    public void ballCountUp(View view) {
        if (ballInt < 3) {
            ballInt++;
            ballText.setText(String.valueOf(ballInt));
        } else {
            if (temp) {
                TTS.speak(walkDisplay, TextToSpeech.QUEUE_FLUSH, null);
            }
            AlertDialog.Builder mWalkBuilder = new AlertDialog.Builder(MainActivity.this);

            mWalkBuilder.setMessage("WALK!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    ballInt = 0;
                    strikeInt = 0;
                    ballText.setText(String.valueOf(ballInt));
                    strikeText.setText(String.valueOf(strikeInt));
                }
            }).setNegativeButton("", null).setCancelable(false);

            AlertDialog alert = mWalkBuilder.create();
            alert.show();
        }
    }

    public void strikeCountUp(View view) {
        if (strikeInt < 2) {
            strikeInt++;
            strikeText.setText(String.valueOf(strikeInt));
        } else {
            if (temp) {
                TTS.speak(outDisplay, TextToSpeech.QUEUE_FLUSH, null);
            }
            AlertDialog.Builder mStrikeBuilder = new AlertDialog.Builder(MainActivity.this);

            mStrikeBuilder.setMessage("OUT!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    strikeInt = 0;
                    ballInt = 0;
                    outInt++;
                    strikeText.setText(String.valueOf(strikeInt));
                    ballText.setText(String.valueOf(ballInt));
                    outText.setText(String.valueOf(outInt));
                }
            }).setNegativeButton("", null).setCancelable(false);

            AlertDialog alert = mStrikeBuilder.create();
            alert.show();
        }
    }

    // Function to store data into persistent data
    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT, outText.getText().toString());

        editor.apply();
    }

    // Load Persistent data
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        textPersistent = sharedPreferences.getString(TEXT, "");
    }

    // Update the persistent data
    private void updatePersistentViews() {
        outText.setText(textPersistent);
    }

    // Enable speech
    private void turnOnSpeech() {
        TTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if (status == TextToSpeech.SUCCESS) {
                    int result = TTS.setLanguage(Locale.UK);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        AlertDialog.Builder error = new AlertDialog.Builder(MainActivity.this);
                        error.setMessage("TextToSpeech is not working").setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).setNegativeButton("", null).setCancelable(false);

                        AlertDialog alert = error.create();
                        alert.show();
                    }
                } else {
                    AlertDialog.Builder error = new AlertDialog.Builder(MainActivity.this);
                    error.setMessage("TextToSpeech is not working").setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setNegativeButton("", null).setCancelable(false);

                    AlertDialog alert = error.create();
                    alert.show();
                }

            }
        });
    }

}
