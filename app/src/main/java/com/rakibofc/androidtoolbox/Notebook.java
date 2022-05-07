package com.rakibofc.androidtoolbox;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Notebook extends AppCompatActivity {

    ListView listViewMyNotes;

    public static ArrayList<String> noteTitles;
    public static ArrayList<String> notes;
    public static ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook);
        this.setTitle("Notebook");

        // Value Initialize Stage
        listViewMyNotes = findViewById(R.id.listViewMyNotes);
        noteTitles = new ArrayList<>();
        noteTitles.add("This is a note 1");
        noteTitles.add("This is a note 2");
        noteTitles.add("This is a note 3");

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, noteTitles);
        listViewMyNotes.setAdapter(arrayAdapter);
    }
}