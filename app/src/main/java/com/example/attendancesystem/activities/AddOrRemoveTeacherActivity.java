package com.example.attendancesystem.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.attendancesystem.R;
import com.example.attendancesystem.classes.Teacher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddOrRemoveTeacherActivity extends AppCompatActivity {

    private EditText tName, tId, tSubject, tPassword;
    private DatabaseReference teacherDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_remove_teacher);

        setTitle("Add or Remove Teacher");
        teacherDatabaseReference = FirebaseDatabase.getInstance().getReference().child("TEACHERS");

        tName = findViewById(R.id.name_teacher_editText);
        tId = findViewById(R.id.id_teacher_editText);
        tSubject = findViewById(R.id.subject_teacher_editText);
        tPassword = findViewById(R.id.password_teacher_editText);

        Button tAdd = findViewById(R.id.add_teacher_button);
        Button tRemove = findViewById(R.id.remove_teacher_button);

        tAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTeacher();
            }
        });

        tRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTeacher();
            }
        });
    }


    private void addTeacher() {

        String name = tName.getText().toString();
        String id = tId.getText().toString();
        String subject = tSubject.getText().toString();
        String password = tPassword.getText().toString();

        if (name.isEmpty()) {
            tName.setError("Name is required");
            tName.requestFocus();

        } else if (id.isEmpty()) {
            tId.setError("ID is required");
            tId.requestFocus();

        } else if (password.isEmpty()) {
            tPassword.setError("Password is required");
            tPassword.requestFocus();

        } else if (subject.isEmpty()) {
            tSubject.setError("Subject is required");
            tSubject.requestFocus();

        } else if (!(TextUtils.isEmpty(id))) {
            Teacher teacher = new Teacher(name, id, subject, password);
            teacherDatabaseReference.child(id).setValue(teacher);
            Toast.makeText(this, "Teacher added successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Something went wrong\nPlease try again later!", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeTeacher() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Teacher Id");
        builder.setIcon(R.drawable.teacher_image);
        builder.setMessage("Click OK to remove the teacher");

        // Set up the input
        final EditText tIdInput = new EditText(this);
        builder.setView(tIdInput);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = tIdInput.getText().toString();
                if (m_Text.isEmpty()) {
                    Toast.makeText(AddOrRemoveTeacherActivity.this, "To remove teacher enter Teacher Id first", Toast.LENGTH_SHORT).show();
                } else {
                    removeTeacherEntry(m_Text);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void removeTeacherEntry(final String m_Text) {
        teacherDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(m_Text).exists()) {
                    teacherDatabaseReference.child(m_Text).setValue(null);
                    Toast.makeText(AddOrRemoveTeacherActivity.this, "Teacher removed successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddOrRemoveTeacherActivity.this, "Teacher Id not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(AddOrRemoveTeacherActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

