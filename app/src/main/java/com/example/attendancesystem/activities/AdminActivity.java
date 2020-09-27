package com.example.attendancesystem.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendancesystem.R;

import java.util.Objects;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        setTitle("Admin Dashboard");

        //Get the bundle
        Bundle bundle = getIntent().getExtras();
        //Extract the data…
        String userInfo = Objects.requireNonNull(bundle).getString("NAME");
        String welcomeMessage = "WELCOME\n" + userInfo;

        Button addOrRemoveStudent = findViewById(R.id.add_or_remove_student_button);
        Button addOrRemoveTeacher = findViewById(R.id.add_or_remove_teacher_button);
        Button generateTodayStudent = findViewById(R.id.generate_today_attendance_button);
        Button changePassword = findViewById(R.id.change_password_button);
        TextView userData = findViewById(R.id.student_info_text_view);

        userData.setText(welcomeMessage);

        addOrRemoveTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminActivity.this, AddOrRemoveTeacherActivity.class);
                startActivity(intent);
            }
        });

        addOrRemoveStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminActivity.this, AddOrRemoveStudentActivity.class);
                startActivity(intent);
            }
        });

        generateTodayStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, GenerateReportActivity.class);
                startActivity(intent);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}
