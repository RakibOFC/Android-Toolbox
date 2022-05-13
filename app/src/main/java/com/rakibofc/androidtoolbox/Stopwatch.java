package com.rakibofc.androidtoolbox;

import static com.rakibofc.androidtoolbox.MainActivity.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import java.util.concurrent.TimeUnit;

public class Stopwatch extends AppCompatActivity {

    public AlertDialog.Builder alertBuilder;
    public TextView textViewTime;
    public Button buttonReset;
    public Button buttonStart;
    public long timeProgress;
    public boolean isStop, startState;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.theme) {
            changeThemeMode();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        this.setTitle("Stopwatch");

        // Value Initialize Stage
        alertBuilder = new AlertDialog.Builder(this);
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

    private void changeThemeMode() {

        alertBuilder.setCancelable(false);
        alertBuilder.setTitle("Theme");
        alertBuilder.setSingleChoiceItems(themeMode, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (themeMode[which].equals("Dark")) {

                    from = 2;

                } else if (themeMode[which].equals("Light")) {

                    from = 1;
                }
            }
        });
        alertBuilder.setPositiveButton("Change", (dialog, which) -> {

            if (from == 2) {
                sharedPreferences.edit().putInt("checkedItem", 0).apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else if (from == 1) {
                sharedPreferences.edit().putInt("checkedItem", 1).apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        alertBuilder.setNegativeButton("Cancel", (dialog, which) -> {
            //if user select "No", just cancel this dialog and continue with app
            dialog.cancel();
        });

        AlertDialog alert = alertBuilder.create();
        alert.setOnShowListener(arg0 -> {
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(0xff138f87);
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(0xff138f87);
        });
        alert.show();
    }
}