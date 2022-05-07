package com.rakibofc.androidtoolbox;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class NotebookResetPassword extends AppCompatActivity implements View.OnClickListener {

    public EditText editTextEmail;
    public Button buttonResetPassword;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook_reset_password);
        Objects.requireNonNull(getSupportActionBar()).hide();
        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        buttonResetPassword = findViewById(R.id.buttonResetPassword);

        buttonResetPassword.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.buttonResetPassword) {

            String email = editTextEmail.getText().toString().trim();

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                editTextEmail.setError("Enter a valid email address");
                editTextEmail.requestFocus();
                return;
            }

            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {

                if (task.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "Check your email to reset password", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}