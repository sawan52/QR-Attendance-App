package com.example.attendancesystem.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attendancesystem.R;
import com.example.attendancesystem.classes.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddOrRemoveStudentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText sName, sId, sPassword;
    private DatabaseReference studentDatabaseReference;
    private String spinnerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_remove_student);

        setTitle("Add or Remove Student");
        studentDatabaseReference = FirebaseDatabase.getInstance().getReference().child("STUDENTS");
        Spinner studentClassSpinner = findViewById(R.id.select_class_student_spinner);

        sName = findViewById(R.id.name_student_editText);
        sId = findViewById(R.id.id_student_editText);
        sPassword = findViewById(R.id.password_student_editText);

        Button sAdd = findViewById(R.id.add_student_button);
        Button sRemove = findViewById(R.id.remove_student_button);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.classes_for_teacher_and_students, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        studentClassSpinner.setAdapter(adapter);
        studentClassSpinner.setOnItemSelectedListener(this);

        sAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent();
            }
        });

        sRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeStudent();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        spinnerText = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void addStudent() {

        String name = sName.getText().toString();
        String id = sId.getText().toString();
        String password = sPassword.getText().toString();

        if (name.isEmpty()) {
            sName.setError("Name is required");
            sName.requestFocus();

        } else if (id.isEmpty()) {
            sId.setError("ID is required");
            sId.requestFocus();

        } else if (password.isEmpty()) {
            sPassword.setError("Password is required");
            sPassword.requestFocus();

        } else if (spinnerText.equals("Select Class")) {
            Toast.makeText(this, "Select a class first", Toast.LENGTH_SHORT).show();

        } else if (!(TextUtils.isEmpty(id))) {
            String currentAttendance = "0.0";
            Student student = new Student(name, id, password, spinnerText, currentAttendance);
            studentDatabaseReference.child(id).setValue(student);
            Toast.makeText(this, "Student added successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Something went wrong\nPlease try again later!", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeStudent() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Student Id");
        builder.setIcon(R.drawable.student_image);
        builder.setMessage("Click OK to remove the student");

        // Set up the input
        final EditText sIdInput = new EditText(this);
        builder.setView(sIdInput);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String m_Text = sIdInput.getText().toString();
                if (m_Text.isEmpty()) {
                    Toast.makeText(AddOrRemoveStudentActivity.this, "To remove student enter Student Id first", Toast.LENGTH_SHORT).show();
                } else {
                    removeStudentEntry(m_Text);
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

    private void removeStudentEntry(final String m_Text) {
        studentDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(m_Text).exists()) {
                    studentDatabaseReference.child(m_Text).setValue(null);
                    Toast.makeText(AddOrRemoveStudentActivity.this, "Student removed successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddOrRemoveStudentActivity.this, "Student Id not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(AddOrRemoveStudentActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class ChangePasswordActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_change_password);
        }
    }
}

