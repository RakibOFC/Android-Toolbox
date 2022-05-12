package com.rakibofc.androidtoolbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class AudioPlayerActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TextView textViewFileName;
    TextView textViewCurrentDuration;
    TextView textViewFinishTime;
    SeekBar seekBarDuration;
    SeekBar seekBarVolume;
    TextView textViewVolume;
    SeekBar seekBarPitch;
    TextView textViewPitch;
    SeekBar seekBarSpeed;
    TextView textViewSpeed;
    ImageView imageViewPlay, imageViewPause;
    ImageView imageViewPlayback, imageViewPlayForward;

    public boolean isPlayable = false;
    public static final int PICK_AUDIO_FILE = 2;
    public Uri audioUri;
    public String audioUriStr;
    public Handler handler;
    public boolean isFinished = false;

    public PlaybackParams playbackParams;
    public float currentPitch;
    public float currentSpeed;
    public MediaPlayer mediaPlayer;
    public AudioManager audioManager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.open_file_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.openFile:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    }
                }
                showFileChooser();
                break;

            case R.id.darkMode:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;

            case R.id.lightMode:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        this.setTitle("Audio Player");

        // Initialize variables
        sharedPreferences = this.getSharedPreferences("com.rakibofc.androidtoolbox", Context.MODE_PRIVATE);
        audioUriStr = sharedPreferences.getString("audioUri", "");

        textViewFileName = findViewById(R.id.textViewFileName);
        textViewCurrentDuration = findViewById(R.id.textViewCurrentDuration);
        textViewFinishTime = findViewById(R.id.textViewFinishTime);
        seekBarDuration = findViewById(R.id.seekBarDuration);
        textViewVolume = findViewById(R.id.textViewVolume);
        seekBarVolume = findViewById(R.id.seekBarVolume);
        seekBarPitch = findViewById(R.id.seekBarPitch);
        textViewPitch = findViewById(R.id.textViewPitch);
        seekBarSpeed = findViewById(R.id.seekBarSpeed);
        textViewSpeed = findViewById(R.id.textViewSpeed);
        imageViewPlayback = findViewById(R.id.imageViewPlayback);
        imageViewPlayForward = findViewById(R.id.imageViewPlayForward);
        imageViewPlay = findViewById(R.id.imageViewPlay);
        imageViewPause = findViewById(R.id.imageViewPause);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // Set default value of Pitch and Speed
        currentSpeed = currentPitch = 1.0f;

        // Set volume in the SeekBar Start
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);

        // Set currentVolume in TextView
        textViewVolume.setText(String.valueOf(currentVolume));

        seekBarVolume.setMax(maxVolume);
        seekBarVolume.setProgress(currentVolume);

        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                // Set volume form SeekBar
                textViewVolume.setText(String.valueOf(progress));
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // Set volume in the SeekBar - End

        // Play Recent loaded file
        /*Log.e("Uri", audioUriStr);

        if (!audioUriStr.equals("")) {

            *//*Log.e("Uri", audioUriStr);
            audioUri = Uri.fromFile(new File(audioUriStr)); // parse
            setAudioUri();
            audioPlayer();*//*
        }*/
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        startActivityForResult(intent, PICK_AUDIO_FILE);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("Intent Data", data + "");

        if (requestCode == PICK_AUDIO_FILE) {

            if (resultCode == RESULT_OK) {

                audioUri = data.getData();
                sharedPreferences.edit().putString("audioUri", audioUri.toString()).apply();
                // sharedPreferences.edit().putString("audioUri", String.valueOf(data)).apply();
                setAudioUri();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void setAudioUri() {

        isPlayable = true;

        textViewFileName.setText("File: " + getFileName(audioUri.getLastPathSegment()));

        mediaPlayer = MediaPlayer.create(AudioPlayerActivity.this, audioUri);

        // Set audio file duration
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        Date progress = new Date(mediaPlayer.getDuration());
        textViewFinishTime.setText(formatter.format(progress));

        audioPitch();
        audioSpeed();
        audioPlayer();
    }

    private String getFileName(String filePath) {

        StringBuilder fileName = new StringBuilder();
        boolean isStart = false;

        for (int i = 0; i < filePath.length(); i++) {

            if (isStart) {

                fileName.append(filePath.charAt(i));
            }

            if (filePath.charAt(i) == ':' || filePath.charAt(i) == '/') {

                isStart = true;
            }
        }

        return fileName.toString();
    }

    // Audio Pitch Method
    private void audioPitch() {

        // Set Pitch in the SeekBar - Start
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            PlaybackParams playbackParams = mediaPlayer.getPlaybackParams();
            currentPitch = playbackParams.getPitch();
            textViewPitch.setText(String.valueOf(currentPitch));
            seekBarPitch.setProgress((int) currentPitch * 2);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBarPitch.setMin(1);
        }
        seekBarPitch.setMax(8);

        seekBarPitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    playbackParams = mediaPlayer.getPlaybackParams();
                    currentPitch = (float) progress/2f;
                    playbackParams.setPitch(currentPitch);
                    textViewPitch.setText(String.valueOf(currentPitch));
                    if (mediaPlayer.isPlaying()) {

                        mediaPlayer.setPlaybackParams(playbackParams);
                    }

                } else {

                    Toast.makeText(AudioPlayerActivity.this, "Your device not for this feature. Use Android 6.0+", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // Set Pitch in the SeekBar - End
    }


    // Audio Speed Method
    private void audioSpeed(){

        // Set Speed in the SeekBar - Start
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            PlaybackParams playbackParams = mediaPlayer.getPlaybackParams();
            currentSpeed = playbackParams.getSpeed();
            textViewSpeed.setText(String.valueOf(currentSpeed));
            seekBarSpeed.setProgress((int) currentSpeed * 4);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBarSpeed.setMin(1);
        }
        seekBarSpeed.setMax(8);

        seekBarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    playbackParams = mediaPlayer.getPlaybackParams();
                    currentSpeed = (float) progress/4f;
                    playbackParams.setSpeed(currentSpeed);
                    textViewSpeed.setText(String.valueOf(currentSpeed));
                    if (mediaPlayer.isPlaying()) {

                        mediaPlayer.setPlaybackParams(playbackParams);
                    }

                } else {
                    Toast.makeText(AudioPlayerActivity.this, "Your device not for this feature. Use Android 6.0+", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // Set Speed in the SeekBar - End
    }

    private void audioPlayer() {

        imageViewPlay.setOnClickListener(v -> {

            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();

                isFinished = false;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    playbackParams = mediaPlayer.getPlaybackParams();
                    playbackParams.setPitch(currentPitch);
                    playbackParams.setSpeed(currentSpeed);
                    mediaPlayer.setPlaybackParams(playbackParams);
                }
                // Seek_bar set initial state
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    seekBarDuration.setMin(0);
                }
                // Seek_bar set maximum length
                seekBarDuration.setMax(mediaPlayer.getDuration());

                // Seekbar progress and if a user change the duration
                seekBarDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        if (fromUser) {
                            mediaPlayer.seekTo(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        int currentPosition = mediaPlayer.getCurrentPosition();

                        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
                        Date progress = new Date(currentPosition);

                        textViewCurrentDuration.setText(formatter.format(progress));

                        seekBarDuration.setProgress(currentPosition);

                        Log.e("Duration", mediaPlayer.getCurrentPosition() + ", Total Duration: " + mediaPlayer.getDuration());

                        if (mediaPlayer.getCurrentPosition() >= mediaPlayer.getDuration() - 400) {

                            Log.e("Duration", mediaPlayer.getCurrentPosition() + ", Total Duration: " + mediaPlayer.getDuration());
                            imageViewPause.setVisibility(View.INVISIBLE);
                            imageViewPlay.setVisibility(View.VISIBLE);

                            isFinished = true;
                        }

                        handler.postDelayed(this, 1000);

                        if (isFinished) {
                            handler.removeCallbacks(this);
                        }
                    }
                };
                handler.post(runnable);
            }
            imageViewPlay.setVisibility(View.INVISIBLE);
            imageViewPause.setVisibility(View.VISIBLE);
        });

        imageViewPause.setOnClickListener(v -> {

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
            imageViewPause.setVisibility(View.INVISIBLE);
            imageViewPlay.setVisibility(View.VISIBLE);
        });

        imageViewPlayback.setOnClickListener(v -> {

            if (seekBarDuration.getProgress() - 5000 >= 0) {

                seekBarDuration.setProgress(seekBarDuration.getProgress() - 5000);
                mediaPlayer.seekTo(seekBarDuration.getProgress() - 5000);

            } else {
                seekBarDuration.setProgress(0);
                mediaPlayer.seekTo(0);
            }
        });

        imageViewPlayForward.setOnClickListener(v -> {

            if (seekBarDuration.getProgress() + 5000 <= mediaPlayer.getDuration()) {

                seekBarDuration.setProgress(seekBarDuration.getProgress() + 5000);
                mediaPlayer.seekTo(seekBarDuration.getProgress() + 5000);

            } else {
                seekBarDuration.setProgress(mediaPlayer.getDuration());
                mediaPlayer.seekTo(mediaPlayer.getDuration());
            }
        });
    }
}