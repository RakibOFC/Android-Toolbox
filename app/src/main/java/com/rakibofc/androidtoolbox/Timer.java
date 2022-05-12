package com.rakibofc.androidtoolbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Timer extends AppCompatActivity {

    RelativeLayout rvInputTime;
    TextView textViewTime;
    NumberPicker numberPickerHours;
    NumberPicker numberPickerMinutes;
    NumberPicker numberPickerSeconds;
    Button buttonCancel;
    Button buttonStart;
    boolean isRunning = false, isPause = false, isCancel = false;
    long timeInMilliseconds;

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
        setContentView(R.layout.activity_timer);

        this.setTitle("Timer");

        // Value Initialize Stage
        rvInputTime = findViewById(R.id.rvInputTime);
        textViewTime = findViewById(R.id.textViewTime);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonStart = findViewById(R.id.buttonStart);

        numberPickerHours = findViewById(R.id.numberPickerHours);
        numberPickerHours.setMinValue(0);
        numberPickerHours.setMaxValue(99);

        numberPickerMinutes = findViewById(R.id.numberPickerMinutes);
        numberPickerMinutes.setMinValue(0);
        numberPickerMinutes.setMaxValue(59);

        numberPickerSeconds = findViewById(R.id.numberPickerSeconds);
        numberPickerSeconds.setMinValue(0);
        numberPickerSeconds.setMaxValue(59);

        // Initially buttonCancel is disable
        buttonCancel.setEnabled(false);

        buttonCancel.setOnClickListener(v -> {

            isCancel = true;

            buttonCancel.setEnabled(false);

            buttonStart.setText(R.string.start);
            textViewTime.setVisibility(View.GONE);
            rvInputTime.setVisibility(View.VISIBLE);

            isRunning = false;
            isPause = false;
            timeInMilliseconds = 0;
        });

        buttonStart.setOnClickListener(v -> {

            if (!isPause) {

                getInputValue();
                Log.e("Info", "Get Value");
            }

            if (buttonStart.getText().equals("Start")) {

                Log.e("Info", "Start");
                isRunning = true;
                buttonStart.setText(R.string.pause);
                buttonCancel.setEnabled(true);
                textViewTime.setVisibility(View.VISIBLE);
                rvInputTime.setVisibility(View.GONE);


            } else if (buttonStart.getText().equals("Pause")) {

                isRunning = false;
                buttonStart.setText(R.string.resume);

            } else if (buttonStart.getText().equals("Resume")) {

                isRunning = true;
                buttonStart.setText(R.string.pause);
            }
            startCountDownTimer();
        });
    }

    private void getInputValue() {

        int hours = numberPickerHours.getValue();
        int minutes = numberPickerMinutes.getValue();
        int seconds = numberPickerSeconds.getValue();

        rvInputTime.setVisibility(View.GONE);
        textViewTime.setVisibility(View.VISIBLE);

        timeInMilliseconds = ((long) hours * 3600000) + ((long) minutes * 60000) + ((long) seconds * 1000);
    }

    private void startCountDownTimer() {

        CountDownTimer countDownTimer = new CountDownTimer(timeInMilliseconds, 1000) {

            public void onTick(long milliSecondsUntilDOne) {

                // Time Counting
                long HH = TimeUnit.MILLISECONDS.toHours(milliSecondsUntilDOne);
                long MM = TimeUnit.MILLISECONDS.toMinutes(milliSecondsUntilDOne) % 60;
                long SS = TimeUnit.MILLISECONDS.toSeconds(milliSecondsUntilDOne) % 60;

                @SuppressLint("DefaultLocale") String dateTime = String.format("%02d:%02d:%02d", HH, MM, SS);

                if (!isRunning) {

                    isPause = true;
                    timeInMilliseconds = milliSecondsUntilDOne;
                    this.cancel();

                } else if (isCancel) {

                    isCancel = false;
                    this.onFinish();

                } else {

                    textViewTime.setText(dateTime);
                }

                Log.e("Info", "Running");
            }

            public void onFinish() {

                timeInMilliseconds = 0;
                textViewTime.setVisibility(View.GONE);
                rvInputTime.setVisibility(View.VISIBLE);
            }

        };
        countDownTimer.start();
    }
}