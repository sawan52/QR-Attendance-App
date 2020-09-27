package com.example.attendancesystem.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancesystem.R;
import com.example.attendancesystem.adapters.StudentsReportAdapter;
import com.example.attendancesystem.classes.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GenerateReportActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private ArrayList<Student> arrayList;
    private StudentsReportAdapter studentsReportAdapter;
    private ProgressBar progressBar;
    private TextView textView, empty_text_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_report);

        setTitle("Generate Report");

        progressBar = findViewById(R.id.progress_bar);
        textView = findViewById(R.id.list_of_students);
        empty_text_view = findViewById(R.id.emptyTextView);
        recyclerView = findViewById(R.id.students_details_recyclerView);
        final EditText percentageEditText = findViewById(R.id.attendance_percentage);
        Button generateButton = findViewById(R.id.generate_button);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("STUDENTS");

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String percent = percentageEditText.getText().toString();
                if (percent.isEmpty()) {
                    percentageEditText.setError("Enter Percentage");
                    percentageEditText.requestFocus();
                } else {
                    double x = Double.valueOf(percent);
                    generateReport(x);
                    textView.setText("List of students having attendance less than or equal to " + percent + "%.");
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        arrayList = new ArrayList<>();
    }

    private void generateReport(final double x) {

        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                arrayList.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    Student student = dsp.getValue(Student.class);
                    if (student != null) {
                        double y = Double.valueOf(student.getsCurrentAttendance());

                        if (x >= y) {
                            arrayList.add(student);
                        }
                    }
                }

                if (!arrayList.isEmpty()) {

                    empty_text_view.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    studentsReportAdapter = new StudentsReportAdapter(GenerateReportActivity.this, arrayList);
                    recyclerView.setAdapter(studentsReportAdapter);

                } else {

                    empty_text_view.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    studentsReportAdapter = new StudentsReportAdapter(GenerateReportActivity.this, arrayList);
                    recyclerView.setAdapter(studentsReportAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
