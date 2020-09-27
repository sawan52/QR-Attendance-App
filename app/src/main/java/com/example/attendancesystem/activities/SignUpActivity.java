package com.example.attendancesystem.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.attendancesystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private EditText signUpEmailId, signUpPasswordEditText, signUpPasswordEditText2;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle("Sign Up");

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        signUpEmailId = findViewById(R.id.signUp_email_editText);
        signUpPasswordEditText = findViewById(R.id.signUp_password_editText);
        signUpPasswordEditText2 = findViewById(R.id.signUp_password2_editText);
        Button signUp = findViewById(R.id.signUp_button);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyAndSignUp();
            }
        });
    }

    private void verifyAndSignUp() {

        String email = signUpEmailId.getText().toString();
        String password = signUpPasswordEditText.getText().toString();
        String password2 = signUpPasswordEditText2.getText().toString();

        if (email.isEmpty()) {
            signUpEmailId.setError("Email is required");
            signUpEmailId.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signUpEmailId.setError("Enter a valid email");
            signUpEmailId.requestFocus();
        } else if (password.isEmpty()) {
            signUpPasswordEditText.setError("Password is required");
            signUpPasswordEditText.requestFocus();
        } else if (password2.isEmpty()) {
            signUpPasswordEditText2.setError("Password is required");
            signUpPasswordEditText2.requestFocus();
        } else if (password.length() < 8) {
            signUpPasswordEditText.setError("Minimum password length should be 8");
            signUpPasswordEditText.requestFocus();
        } else if (!(password.equals(password2))) {
            Toast.makeText(this, "Both entered password didn't match", Toast.LENGTH_SHORT).show();
        } else {

            progressDialog.setMessage("Registering User...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                        sendUserToMainActivity();
                        finish();
                    } else {
                        progressDialog.dismiss();
                        if (task.getException() instanceof FirebaseNetworkException) {
                            Toast.makeText(SignUpActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                        } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(SignUpActivity.this, "You are already registered!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUpActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
        }

    }

    private void sendUserToMainActivity() {

        Intent loginIntent = new Intent(SignUpActivity.this, MainActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(loginIntent);
        finish();
    }
}

