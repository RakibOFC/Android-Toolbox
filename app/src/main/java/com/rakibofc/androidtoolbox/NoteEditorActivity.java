package com.rakibofc.androidtoolbox;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class NoteEditorActivity extends AppCompatActivity {

    public DatabaseReference databaseReferenceAccessNote;
    public String currentUserStr;
    public EditText editTextNoteTitle;
    public EditText editTextNoteEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        this.setTitle("Notebook Editor");

        // Value Initialize Stage
        editTextNoteTitle = findViewById(R.id.editTextNoteTitle);
        editTextNoteEditor = findViewById(R.id.editTextNoteEditor);

        // Generate New Note ID
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmmssddMMyyyy");
        Date date = new Date();
        String noteID = "" + simpleDateFormat.format(date);

        currentUserStr = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReferenceAccessNote = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserStr).child("notes").child(noteID);

        // Store Message in Database
        // databaseReferenceNoteID.setValue(messageSenderSide);

        // Save Note Title Automatically on Database
        editTextNoteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                databaseReferenceAccessNote.child("title").setValue(editTextNoteTitle.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Save Note Text Automatically on DataBase
        editTextNoteEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                databaseReferenceAccessNote.child("note").setValue(editTextNoteEditor.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    // If Back_Button_Pressed
    @Override
    public void onBackPressed() {

        String alertMsg = "";

        if (editTextNoteTitle.getText().toString().isEmpty() || editTextNoteEditor.getText().toString().isEmpty()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Confirm Exit");

            if (editTextNoteTitle.getText().toString().isEmpty()){

                alertMsg = "Note title is empty. Do you want to Exit?";

            } else if (editTextNoteEditor.getText().toString().isEmpty()){

                alertMsg = "Note body is empty. Do you want to Exit?";
            }

            builder.setMessage(alertMsg);
            builder.setPositiveButton("Yes", (dialog, which) -> {
                //if user pressed "yes", then he is allowed to exit from application
                finish();
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

        } else {

            super.onBackPressed();
        }
    }
}