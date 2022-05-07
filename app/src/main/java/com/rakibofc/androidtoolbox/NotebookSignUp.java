package com.rakibofc.androidtoolbox;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class NotebookSignUp extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    public EditText editTextName, editTextEmail, editTextPassword;
    public Button buttonSignup;
    public TextView textViewSignIn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook_sign_up);
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Value initialize stage
        mAuth = FirebaseAuth.getInstance();
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignup = findViewById(R.id.buttonSignup);
        textViewSignIn = findViewById(R.id.textViewSignIn);

        editTextPassword.setOnKeyListener(this);
        buttonSignup.setOnClickListener(this);
        textViewSignIn.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.buttonSignup:
                userSignUp();
                break;

            case R.id.textViewSignIn:
                finish();
                break;
        }
    }

    private void userSignUp() {

        String fullName = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (fullName.isEmpty()) {

            editTextName.setError("Enter your name");
            editTextName.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            editTextEmail.setError("Enter a valid email address");
            editTextEmail.requestFocus();
            return;
        }

        if (password.length() < 6) {

            editTextPassword.setError("Minimum length of a password should be 6");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                User user = new User(fullName, email);

                FirebaseDatabase.getInstance().getReference("users")
                        .child(FirebaseAuth.getInstance()
                                .getCurrentUser().getUid()).setValue(user)
                        .addOnCompleteListener(task1 -> {

                            if (task.isSuccessful() && task1.isSuccessful()) {

                                // successfully added message
                                Toast.makeText(getApplicationContext(), "Sign Up successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(NotebookSignUp.this, NotebookLogin.class));
                            } else {

                                // delete user if task.isSuccessful?
                                // Toast error message
                                Toast.makeText(getApplicationContext(), Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            } else {

                if (task.getException() instanceof FirebaseAuthUserCollisionException) {

                    editTextEmail.setError("User is already registered");
                    editTextEmail.requestFocus();

                } else {

                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_ENTER) {

            userSignUp();
        }
        return false;
    }
}