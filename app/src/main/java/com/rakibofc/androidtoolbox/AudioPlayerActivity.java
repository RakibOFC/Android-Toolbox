package com.rakibofc.androidtoolbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
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

import java.text.SimpleDateFormat;
import java.util.Date;

public class AudioPlayerActivity extends AppCompatActivity {

    // Light Mode Color: Maybe green #138f87

    TextView textViewCurrentDuration;
    TextView textViewFinishTime;
    SeekBar seekBarDuration;
    ImageView imageViewPlay, imageViewPause;

    private String fileName;
    public boolean isPlayable = false, isPlay;
    public int maxVolume;
    public int currentVolume;
    public float audioPitch;
    public static final int PICK_AUDIO_FILE = 2;
    public Uri audioUri;
    public Handler handler;

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
        textViewCurrentDuration = findViewById(R.id.textViewCurrentDuration);
        textViewFinishTime = findViewById(R.id.textViewFinishTime);
        seekBarDuration = findViewById(R.id.seekBarDuration);
        imageViewPlay = findViewById(R.id.imageViewPlay);
        imageViewPause = findViewById(R.id.imageViewPause);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        startActivityForResult(intent, PICK_AUDIO_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_AUDIO_FILE) {

            if (resultCode == RESULT_OK) {

                isPlayable = true;
                audioUri = data.getData();
                mediaPlayer = MediaPlayer.create(AudioPlayerActivity.this, audioUri);

                // Set audio file duration
                SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
                Date progress = new Date(mediaPlayer.getDuration());
                textViewFinishTime.setText(formatter.format(progress));

                audioPlayer();
            }
        }
    }

    private void audioPlayer() {

        imageViewPlay.setOnClickListener(v -> {

            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();

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
                Runnable run = new Runnable() {
                    @Override
                    public void run() {

                        int currentPosition = mediaPlayer.getCurrentPosition();

                        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
                        Date progress = new Date(currentPosition);

                        textViewCurrentDuration.setText(formatter.format(progress));

                        seekBarDuration.setProgress(currentPosition);
                        handler.postDelayed(this, 1000);

                    }
                };
                handler.post(run);
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
    }
}