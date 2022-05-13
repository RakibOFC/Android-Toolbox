package com.rakibofc.androidtoolbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Stopwatch extends AppCompatActivity {

    final CharSequence[] themeMode = {"Dark","Light"};
    int from;

    TextView textViewTime;
    Button buttonReset;
    Button buttonStart;
    long timeProgress;
    boolean isStop, startState;

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

            case R.id.theme:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        this.setTitle("Stopwatch");

        // Value Initialize Stage
        textViewTime = findViewById(R.id.textViewTime);
        buttonReset = findViewById(R.id.buttonReset);
        buttonStart = findViewById(R.id.buttonStart);
        timeProgress = 0;
        startState = false;
        isStop = false;

        // Initially buttonReset is disable
        buttonReset.setEnabled(false);

        buttonReset.setOnClickListener(v -> {

            timeProgress = 0;
            isStop = true;
            buttonReset.setEnabled(false);
            buttonStart.setText(R.string.start);
            textViewTime.setText(R.string._00_00_00);
        });

        // buttonStart
        buttonStart.setOnClickListener(v -> {

            buttonReset.setEnabled(true);

            if (buttonStart.getText().equals("Start") || buttonStart.getText().equals("Resume")) {

                isStop = false;
                buttonStart.setText(R.string.stop);

            } else if (buttonStart.getText().equals("Stop")) {

                isStop = true;
                buttonStart.setText(R.string.resume);

            }
            startStopwatch();
        });
    }

    private void startStopwatch() {

        Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                long HH = TimeUnit.MILLISECONDS.toHours(timeProgress);
                long MM = TimeUnit.MILLISECONDS.toMinutes(timeProgress) % 60;
                long SS = TimeUnit.MILLISECONDS.toSeconds(timeProgress) % 60;

                @SuppressLint("DefaultLocale") String dateTime = String.format("%02d:%02d:%02d", HH, MM, SS);
                textViewTime.setText(dateTime);

                timeProgress += 500;
                handler.postDelayed(this, 500);

                if (isStop) {
                    handler.removeCallbacks(this);
                }

            }
        };
        handler.post(runnable);

    }
}