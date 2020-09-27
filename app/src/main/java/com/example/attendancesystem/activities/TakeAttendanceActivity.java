package com.example.attendancesystem.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendancesystem.R;
import com.example.attendancesystem.classes.Attendance;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TakeAttendanceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String periodNumber, teacherName, teacherSubject, classTiming;
    private ProgressDialog progressDialog;

    private DatabaseReference attendanceDatabaseReference;

    private ArrayList<String> studentIDList = new ArrayList<>();
    private ArrayList<String> selectedItemsList = new ArrayList<>();

    private String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    private String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        setTitle("Mark Attendance");

        teacherName = getIntent().getStringExtra("tName");
        teacherSubject = getIntent().getStringExtra("tSubject");

        progressDialog = new ProgressDialog(this);
        Spinner periodSpinner = findViewById(R.id.period_spinner);
        Button submitButton = findViewById(R.id.submit_attendance_button);

        DatabaseReference studentDatabaseReference = FirebaseDatabase.getInstance().getReference().child("STUDENTS");
        attendanceDatabaseReference = FirebaseDatabase.getInstance().getReference().child("ATTENDANCE");

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.period_spinner, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodSpinner.setAdapter(spinnerAdapter);
        periodSpinner.setOnItemSelectedListener(this);

        progressDialog.setTitle("Fetching Data");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        studentDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                studentIDList.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    studentIDList.add(dsp.child("sId").getValue(String.class));
                }
                OnStart(studentIDList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TakeAttendanceActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markStudentsAttendance();

            }
        });
    }

    private void OnStart(ArrayList<String> userList) {

        ListView studentListView = findViewById(R.id.take_students_attendance_listView);
        studentListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = ((TextView) view).getText().toString();

                if (selectedItemsList.contains(selectedItem)) {
                    selectedItemsList.remove(selectedItem);
                } else {
                    selectedItemsList.add(selectedItem);
                }
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.checkable_list_layout, R.id.txt_title, userList);
        studentListView.setAdapter(adapter);

        progressDialog.dismiss();

    }

    private void markStudentsAttendance() {

        if (periodNumber.equals("Select Period")) {
            Toast.makeText(this, "Select a Period first.", Toast.LENGTH_SHORT).show();
        } else {

            for (String items : selectedItemsList) {

                Attendance markAttend = new Attendance(teacherName, teacherSubject, classTiming, currentTime);
                attendanceDatabaseReference.child(currentDate).child(items).child(periodNumber).setValue(markAttend);
            }
            Toast.makeText(this, "Attendance marked successfully!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        periodNumber = parent.getItemAtPosition(position).toString();
        switch (periodNumber) {
            case "Period 1":
                classTiming = "09:00 AM to 09:59 AM";
                break;
            case "Period 2":
                classTiming = "10:00 AM to 10:59 AM";
                break;
            case "Period 3":
                classTiming = "11:00 AM to 11:59 AM";
                break;
            case "Period 4":
                classTiming = "01:00 PM to 01:59 PM";
                break;
            case "Period 5":
                classTiming = "02:00 PM to 02:59 PM";
                break;
            case "Period 6":
                classTiming = "03:00 PM to 03:59 PM";
                break;
            case "Period 7":
                classTiming = "04:00 PM to 04:59 PM";
                break;
            case "Period 8":
                classTiming = "05:00 PM to 05:59 PM";
                break;
            case "Select Period":
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
