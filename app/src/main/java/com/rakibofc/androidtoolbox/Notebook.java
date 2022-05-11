package com.rakibofc.androidtoolbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Notebook extends AppCompatActivity {

    ProgressBar progressBar;
    ListView listViewMyNotes;

    public static ArrayList<String> noteIds;
    public static ArrayList<String> noteTitles;
    public static ArrayList<String> notes;
    public static ArrayAdapter arrayAdapter;
    public DatabaseReference databaseReferenceNotes;
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
        progressBar = findViewById(R.id.progressBar);
        listViewMyNotes = findViewById(R.id.listViewMyNotes);
        progressBar = findViewById(R.id.progressBar);
        noteIds = new ArrayList<>();
        noteTitles = new ArrayList<>();
        notes = new ArrayList<>();
        currentUserStr = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReferenceNotes = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserStr).child("notes");

        progressBar.setVisibility(View.VISIBLE);

        databaseReferenceNotes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                noteIds.clear();
                noteTitles.clear();
                notes.clear();
                arrayAdapter.clear();

                for (DataSnapshot noteID : snapshot.getChildren()) {

                    noteIds.add(noteID.getKey());
                    noteTitles.add(noteID.child("title").getValue() + "");
                    notes.add(noteID.child("note").getValue() + "");

                }
                arrayAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, noteTitles);
        listViewMyNotes.setAdapter(arrayAdapter);

        listViewMyNotes.setOnItemClickListener((parent, view, position, id) -> {

            Log.e("Position", position + "");
            Log.e("NoteID", noteIds.get(position) + "");
            Log.e("NoteTitle", noteTitles.get(position) + "");
            Log.e("Note", notes.get(position) + "");

            Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
            intent.putExtra("position", position);
            startActivity(intent);
        });

        // Remove note from database
        listViewMyNotes.setOnItemLongClickListener((parent, view, position, id) -> {

            // Show a dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setIcon(android.R.drawable.ic_delete);

            builder.setIcon(android.R.drawable.ic_delete);
            builder.setTitle("Confirm Delete");
            builder.setMessage("Are you sure you want to delete this note?");
            builder.setPositiveButton("Yes", (dialog, which) -> {


                String tempString = noteTitles.get(position);

                databaseReferenceNotes.child(noteIds.get(position)).removeValue();
                noteIds.remove(position);
                noteTitles.remove(position);
                notes.remove(position);
                arrayAdapter.notifyDataSetChanged();

                Toast.makeText(this, "Note \"" + tempString + "\" has been deleted", Toast.LENGTH_SHORT).show();
            });

            builder.setNegativeButton("No", (dialog, which) -> {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            });

            AlertDialog alert = builder.create();
            alert.setOnShowListener(arg0 -> {
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.alert_text_color));
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.alert_text_color));
            });
            alert.show();

            return true;
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}