package com.example.attendancesystem.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.attendancesystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePasswordActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText username, newPassword, confirmPassword;
    private String spinnerTxt, user, password, cnfPassword;

    private DatabaseReference studentDatabaseReference, teacherDatabaseReference, adminDatabaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        setTitle("Change Password");
        studentDatabaseReference = FirebaseDatabase.getInstance().getReference().child("STUDENTS");
        teacherDatabaseReference = FirebaseDatabase.getInstance().getReference().child("TEACHERS");
        adminDatabaseReference = FirebaseDatabase.getInstance().getReference().child("ADMIN");

        Button changePassword = findViewById(R.id.change_password_button);
        Spinner profileSpinner = findViewById(R.id.change_password_spinner);
        progressDialog = new ProgressDialog(this);
        username = findViewById(R.id.change_text_username);
        newPassword = findViewById(R.id.change_new_password);
        confirmPassword = findViewById(R.id.change_confirm_password);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.select_login_method, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profileSpinner.setAdapter(adapter);
        profileSpinner.setOnItemSelectedListener(this);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerTxt = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void changePassword() {

        switch (spinnerTxt) {
            case "admin":
                adminPasswordChange();
                break;
            case "teacher":
                teacherPasswordChange();
                break;
            case "student":
                studentPasswordChange();
                break;
            default:
                Toast.makeText(this, "Select profile first", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void studentPasswordChange() {

        user = username.getText().toString();
        password = newPassword.getText().toString();
        cnfPassword = confirmPassword.getText().toString();

        if (user.isEmpty()) {
            username.setError("User Id is required");
            username.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            newPassword.setError("Password is required");
            newPassword.requestFocus();
            return;
        }
        if (cnfPassword.isEmpty()) {
            confirmPassword.setError("Password is required");
            confirmPassword.requestFocus();
            return;
        }
        if (!(password.equals(cnfPassword))) {
            Toast.makeText(this, "Password didn't match", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setTitle("Updating Password");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        studentDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(user).exists()) {
                    progressDialog.dismiss();
                    studentDatabaseReference.child(user).child("sPassword").setValue(cnfPassword);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(ChangePasswordActivity.this, "Student Id not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(ChangePasswordActivity.this, "Something went wrong\nPlease try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void adminPasswordChange() {

        user = username.getText().toString();
        password = newPassword.getText().toString();
        cnfPassword = confirmPassword.getText().toString();

        if (user.isEmpty()) {
            username.setError("User Id is required");
            username.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            newPassword.setError("Password is required");
            newPassword.requestFocus();
            return;
        }
        if (cnfPassword.isEmpty()) {
            confirmPassword.setError("Password is required");
            confirmPassword.requestFocus();
            return;
        }
        if (!(password.equals(cnfPassword))) {
            Toast.makeText(this, "Password didn't match", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setTitle("Updating Password");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        adminDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    progressDialog.dismiss();
                    adminDatabaseReference.child(user).setValue(cnfPassword);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(ChangePasswordActivity.this, "Admin Id not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(ChangePasswordActivity.this, "Something went wrong\nPlease try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void teacherPasswordChange() {

        user = username.getText().toString();
        password = newPassword.getText().toString();
        cnfPassword = confirmPassword.getText().toString();

        if (user.isEmpty()) {
            username.setError("User Id is required");
            username.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            newPassword.setError("Password is required");
            newPassword.requestFocus();
            return;
        }
        if (cnfPassword.isEmpty()) {
            confirmPassword.setError("Password is required");
            confirmPassword.requestFocus();
            return;
        }
        if (!(password.equals(cnfPassword))) {
            Toast.makeText(this, "Password didn't match", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setTitle("Updating Password");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        teacherDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(user).exists()) {
                    teacherDatabaseReference.child(user).child("tPassword").setValue(cnfPassword);
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(ChangePasswordActivity.this, "Teacher Id not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(ChangePasswordActivity.this, "Something went wrong\nPlease try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
