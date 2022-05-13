package com.rakibofc.androidtoolbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // Static Value Initialization
    public static SharedPreferences sharedPreferences;
    public static final CharSequence[] themeMode = {"Dark","Light"};
    public static int from;
    public static int checkedItem;

    // Other Value Initialization
    public AlertDialog.Builder alertBuilder;
    public LinearLayout notebook;
    public LinearLayout systemInfo;
    public LinearLayout pdfReader;
    public LinearLayout audioPlayer;
    public LinearLayout stopwatch;
    public LinearLayout timer;

    public final AlphaAnimation itemClick = new AlphaAnimation(2, 0);

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
        setContentView(R.layout.activity_main);

        // Get data from storage
        sharedPreferences = this.getSharedPreferences("com.rakibofc.androidtoolbox", Context.MODE_PRIVATE);

        alertBuilder = new AlertDialog.Builder(this);

        // Set theme color initially
        setInitTheme();

        // Button Value Initialize
        notebook = findViewById(R.id.notebook);
        systemInfo = findViewById(R.id.systemInfo);
        pdfReader = findViewById(R.id.pdfReader);
        audioPlayer = findViewById(R.id.audioPlayer);
        stopwatch = findViewById(R.id.stopwatch);
        timer = findViewById(R.id.timer);

        notebook.setOnClickListener(v -> {

            // Start Notebook Activity
            v.startAnimation(itemClick);
            startActivity(new Intent(MainActivity.this, NotebookLogin.class));
        });

        systemInfo.setOnClickListener(v -> {

            // Start SystemInfo Activity
            v.startAnimation(itemClick);
            startActivity(new Intent(MainActivity.this, SystemInfoActivity.class));
        });

        pdfReader.setOnClickListener(v -> {

            // Start PDFReader Activity
            v.startAnimation(itemClick);
            startActivity(new Intent(MainActivity.this, PDFReader.class));
        });

        audioPlayer.setOnClickListener(v -> {

            // Start AudioPlayer Activity
            v.startAnimation(itemClick);
            startActivity(new Intent(MainActivity.this, AudioPlayerActivity.class));
        });

        stopwatch.setOnClickListener(v -> {

            // Start Notebook Activity
            v.startAnimation(itemClick);
            startActivity(new Intent(MainActivity.this, Stopwatch.class));
        });

        timer.setOnClickListener(v -> {

            // Start Timer Activity
            v.startAnimation(itemClick);
            startActivity(new Intent(MainActivity.this, Timer.class));
        });
    }

    public static void setInitTheme() {

        checkedItem = sharedPreferences.getInt("checkedItem", -1);

        if (checkedItem == -1 || checkedItem == 0) {

            checkedItem = 0;
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else if (checkedItem == 1) {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
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