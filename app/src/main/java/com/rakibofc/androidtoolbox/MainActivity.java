package com.rakibofc.androidtoolbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // Button Initialize
    public Button notebook;
    public Button systemInfo;
    public Button pdfReader;
    public Button audioPlayer;
    public Button stopwatch;
    public Button timer;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

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
        setContentView(R.layout.activity_main);

        /*// Button Value Initialize
        notebook = findViewById(R.id.notebook);
        systemInfo = findViewById(R.id.systemInfo);
        pdfReader = findViewById(R.id.pdfReader);
        audioPlayer = findViewById(R.id.audioPlayer);
        stopwatch = findViewById(R.id.stopwatch);
        timer = findViewById(R.id.timer);

        notebook.setOnClickListener(v -> {

            // Start Notebook Activity
            startActivity(new Intent(MainActivity.this, NotebookLogin.class));
        });

        systemInfo.setOnClickListener(v -> {

            // Start SystemInfo Activity
            startActivity(new Intent(MainActivity.this, SystemInfoActivity.class));
        });

        pdfReader.setOnClickListener(v -> {

            // Start PDFReader Activity
            startActivity(new Intent(MainActivity.this, PDFReader.class));
        });

        audioPlayer.setOnClickListener(v -> {

            // Start AudioPlayer Activity
            startActivity(new Intent(MainActivity.this, AudioPlayerActivity.class));
        });

        stopwatch.setOnClickListener(v -> {

            // Start Notebook Activity
            startActivity(new Intent(MainActivity.this, Notebook.class));
        });

        timer.setOnClickListener(v -> {

            // Start Timer Activity
            startActivity(new Intent(MainActivity.this, Timer.class));
        });*/
    }
}