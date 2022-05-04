package com.rakibofc.androidtoolbox;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Stopwatch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        this.setTitle("Stopwatch");
    }
}