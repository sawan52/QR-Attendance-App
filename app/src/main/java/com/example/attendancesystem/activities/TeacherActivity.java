package com.example.attendancesystem.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.attendancesystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class TeacherActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private String tInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        setTitle("Teachers Dashboard");

        //Get the bundle
        Bundle bundle = getIntent().getExtras();
        //Extract the data…
        String name = Objects.requireNonNull(bundle).getString("tNAME");
        tInfo = bundle.getString("tID");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("TEACHERS");

        String welcomeMessage = "WELCOME\n" + name;

        TextView userData = findViewById(R.id.student_info_text_view);
        Button takeAttendance = findViewById(R.id.take_attendance_button);
        Button previousRecord = findViewById(R.id.previous_record_button);

        userData.setText(welcomeMessage);

        takeAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTeacherData();
            }
        });

        previousRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TeacherActivity.this, GenerateReportActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getTeacherData() {

        databaseReference.child(tInfo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String tID = dataSnapshot.child("tId").getValue(String.class);
                String tName = dataSnapshot.child("tName").getValue(String.class);
                String tSubject = dataSnapshot.child("tSubject").getValue(String.class);

                Intent intent = new Intent(TeacherActivity.this, TakeAttendanceActivity.class);
                intent.putExtra("tID", tID);
                intent.putExtra("tName", tName);
                intent.putExtra("tSubject", tSubject);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
