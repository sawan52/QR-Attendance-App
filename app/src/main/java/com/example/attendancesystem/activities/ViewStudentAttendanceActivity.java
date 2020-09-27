package com.example.attendancesystem.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancesystem.R;
import com.example.attendancesystem.adapters.AbsentAttendanceAdapter;
import com.example.attendancesystem.adapters.PresentAttendanceAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewStudentAttendanceActivity extends AppCompatActivity {

    private DatabaseReference attendanceDbRef;
    private String userId, date;
    private EditText dateEditText;
    private RecyclerView presentAttendanceRecyclerView, absentAttendanceRecyclerView;
    private PresentAttendanceAdapter presentAttendanceAdapter;
    private AbsentAttendanceAdapter absentAttendanceAdapter;

    private ArrayList<String> presentArrayList, absentArrayList;
    private ProgressDialog progressDialog;

    private String[] allSubjects;
    private Set<String> set, newSet;

    public static boolean isValidDate(String d) {

        String regex = "^(3[01]|[12][0-9]|0[1-9])-(1[0-2]|0[1-9])-[0-9]{4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher((CharSequence) d);
        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student_attendance);

        setTitle("View Attendance");

        Bundle bundle = getIntent().getExtras();
        userId = Objects.requireNonNull(bundle).getString("sID");

        attendanceDbRef = FirebaseDatabase.getInstance().getReference().child("ATTENDANCE");
        DatabaseReference teacherDbRef = FirebaseDatabase.getInstance().getReference().child("TEACHERS");

        progressDialog = new ProgressDialog(this);
        dateEditText = findViewById(R.id.attendance_DD_MM_YYYY_editTextView);
        presentAttendanceRecyclerView = findViewById(R.id.present_attendance_recyclerView);
        absentAttendanceRecyclerView = findViewById(R.id.absent_attendance_recyclerView);
        Button fetchAttendance = findViewById(R.id.fetch_attendance_button);

        teacherDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                allSubjects = new String[count];
                int x = 0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    allSubjects[x] = dataSnapshot1.child("tSubject").getValue(String.class);
                    x++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fetchAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAttendance();
            }
        });

        presentAttendanceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        presentArrayList = new ArrayList<>();

        absentAttendanceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        absentArrayList = new ArrayList<>();
    }


    private void fetchAttendance() {

        date = dateEditText.getText().toString();

        if (date.isEmpty()) {
            dateEditText.setError("Enter date first");
            dateEditText.requestFocus();
            return;
        }
        if (!(isValidDate(date))) {
            dateEditText.setError("Enter correct Date format");
            dateEditText.requestFocus();
        } else {
            progressDialog.setTitle("Fetching Data");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            attendanceDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    int count = (int) dataSnapshot.getChildrenCount();
                    String[] allDates = new String[count];
                    int x = 0;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        allDates[x] = dataSnapshot1.getKey();
                        x++;
                    }

                    boolean found = false;
                    for (String s : allDates) {
                        if (s.equals(date)) {
                            found = true;
                            break;
                        }
                    }

                    if (found) {
                        presentArrayList.clear();
                        absentArrayList.clear();

                        int counter = (int) dataSnapshot.child(date).child(userId).getChildrenCount();
                        String[] presentStatus = new String[counter];
                        int y = 0;
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.child(date).child(userId).getChildren()) {
                            presentStatus[y] = dataSnapshot1.child("teacherSubject").getValue(String.class);
                            y++;
                        }

                        set = new HashSet<>(allSubjects.length);
                        newSet = new HashSet<>(allSubjects.length);
                        Collections.addAll(newSet, presentStatus);

                        Collections.addAll(set, allSubjects);
                        for (String newStr : presentStatus) {
                            set.remove(newStr);
                        }

                        presentArrayList.addAll(newSet);
                        absentArrayList.addAll(set);

                        presentAttendanceAdapter = new PresentAttendanceAdapter(ViewStudentAttendanceActivity.this, presentArrayList);
                        presentAttendanceRecyclerView.setAdapter(presentAttendanceAdapter);

                        absentAttendanceAdapter = new AbsentAttendanceAdapter(ViewStudentAttendanceActivity.this, absentArrayList);
                        absentAttendanceRecyclerView.setAdapter(absentAttendanceAdapter);
                        progressDialog.dismiss();

                    } else {

                        presentArrayList.clear();
                        absentArrayList.clear();

                        presentAttendanceAdapter = new PresentAttendanceAdapter(ViewStudentAttendanceActivity.this, presentArrayList);
                        presentAttendanceRecyclerView.setAdapter(presentAttendanceAdapter);

                        absentAttendanceAdapter = new AbsentAttendanceAdapter(ViewStudentAttendanceActivity.this, absentArrayList);
                        absentAttendanceRecyclerView.setAdapter(absentAttendanceAdapter);

                        Toast.makeText(ViewStudentAttendanceActivity.this, "DATA DOES NOT EXIST", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(ViewStudentAttendanceActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}

