package com.rakibofc.androidtoolbox;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Timer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        this.setTitle("Timer");
    }
}