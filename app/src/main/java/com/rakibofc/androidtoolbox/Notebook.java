package com.rakibofc.androidtoolbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Notebook extends AppCompatActivity {

    ListView listViewMyNotes;

    public static ArrayList<String> noteTitles;
    public static ArrayList<String> notes;
    public static ArrayAdapter arrayAdapter;
    public DatabaseReference databaseReference;
    public String currentUserStr;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.notebook_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.addNewNote:

                Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
                intent.putExtra("position", -1);
                startActivity(intent);
                return true;

            case R.id.darkMode:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                return true;

            case R.id.lightMode:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                return true;

            case R.id.logout:
                NotebookLogin.sharedPreferences.edit().putBoolean("loggedIn", false).apply();
                NotebookLogin.sharedPreferences.edit().putString("userID", null).apply();
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook);
        this.setTitle("Notebook");

        // Value Initialize Stage
        listViewMyNotes = findViewById(R.id.listViewMyNotes);
        noteTitles = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        currentUserStr = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        noteTitles.add("This is a note 1");
        noteTitles.add("This is a note 2");
        noteTitles.add("This is a note 3");

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, noteTitles);
        listViewMyNotes.setAdapter(arrayAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}