package com.example.attendancesystem.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendancesystem.R;
import com.example.attendancesystem.classes.Attendance;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class StudentActivity extends AppCompatActivity {

    private DatabaseReference attendanceDatabaseReference;
    private DatabaseReference studentDatabaseReference;
    private String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    private String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

    private SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");

    private String userId;
    private int noOfDays;
    private double initialTotal, finalTotal;

    private TextView userAttendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        setTitle("Student Dashboard");

        //Get the bundle
        Bundle bundle = getIntent().getExtras();
        //Extract the data…
        String userName = Objects.requireNonNull(bundle).getString("sNAME");
        userId = Objects.requireNonNull(bundle).getString("sID");
        String welcomeMessage = "WELCOME\n" + userName;

        attendanceDatabaseReference = FirebaseDatabase.getInstance().getReference().child("ATTENDANCE");
        studentDatabaseReference = FirebaseDatabase.getInstance().getReference().child("STUDENTS");

        Button scanQr = findViewById(R.id.scan_qr_button);
        Button viewAttendance = findViewById(R.id.view_attendance_button);

        TextView userData = findViewById(R.id.student_info_text_view);
        userAttendance = findViewById(R.id.user_attendance_text_view);

        userData.setText(welcomeMessage);
        refreshAttendance();

        scanQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanQR();
            }
        });

        viewAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToViewStudentAttendanceActivity();
            }
        });
    }

    private void sendToViewStudentAttendanceActivity() {

        Intent intent = new Intent(StudentActivity.this, ViewStudentAttendanceActivity.class);
        intent.putExtra("sID", userId);
        startActivity(intent);
    }

    private void refreshAttendance() {

        studentDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String totalAttendance = dataSnapshot.child(userId).child("sCurrentAttendance").getValue(String.class);
                double tAttend = Double.parseDouble(totalAttendance);
                if (tAttend >= 75.0) {
                    userAttendance.setTextColor(getResources().getColor(R.color.green_text));
                    userAttendance.setText("Total Attendance: " + totalAttendance + "%");
                } else {
                    userAttendance.setTextColor(getResources().getColor(R.color.red_text));
                    userAttendance.setText("Total Attendance: " + totalAttendance + "%");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void calculateTotalAttendance(final String userId) {

        attendanceDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {

                    noOfDays = (int) dataSnapshot.getChildrenCount();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.child(userId).hasChildren()) {

                            int count = (int) ds.child(userId).getChildrenCount();
                            double count_X = (double) (count * 100) / 8;
                            initialTotal = initialTotal + count_X;
                        }
                    }

                    finalTotal = initialTotal / noOfDays;
                    studentDatabaseReference.child(userId).child("sCurrentAttendance").setValue(String.format("%.2f", finalTotal));
                    refreshAttendance();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void scanQR() {

        IntentIntegrator intentIntegrator = new IntentIntegrator(StudentActivity.this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.setCameraId(0);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("Scanning");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        final IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null && intentResult.getContents() != null) {

            new AlertDialog.Builder(StudentActivity.this)
                    .setTitle("Click OK to mark your attendance")
                    .setIcon(R.drawable.app_icon)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String scanData = intentResult.getContents();
                            try {
                                parseScanDataAndMarkAttendance(scanData);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void parseScanDataAndMarkAttendance(String scanData) throws ParseException {

        String[] attendanceData = scanData.split("\n");

        String verifyQR = attendanceData[0];
        final String period = attendanceData[1];
        String checkDate = attendanceData[2];
        String startTime = attendanceData[3];
        String stopTime = attendanceData[4];
        final String subjectName = attendanceData[5];
        final String teacherName = attendanceData[6];

        Date time1 = new SimpleDateFormat("HH:mm:ss").parse(startTime);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(time1);
        calendar1.add(Calendar.DATE, 1);

        Date time2 = new SimpleDateFormat("HH:mm:ss").parse(stopTime);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(time2);
        calendar2.add(Calendar.DATE, 1);

        Date d = new SimpleDateFormat("HH:mm:ss").parse(currentTime);
        Calendar calendar3 = Calendar.getInstance();
        calendar3.setTime(d);
        calendar3.add(Calendar.DATE, 1);

        Date x = calendar3.getTime();

        final String _12HourCurrentTime = _12HourSDF.format(_24HourSDF.parse(currentTime));
        final String _12HourStartTime = _12HourSDF.format(_24HourSDF.parse(startTime));
        final String _12HourStopTime = _12HourSDF.format(_24HourSDF.parse(stopTime));

        if (verifyQR.equals("SRMCEM122registrar@srmcem.ac.in")) {
            if ((x.after(calendar1.getTime()) && x.before(calendar2.getTime())) && currentDate.equals(checkDate)) {

                attendanceDatabaseReference.child(currentDate).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(period).exists()) {
                            Toast.makeText(StudentActivity.this, "Attendance is already marked", Toast.LENGTH_SHORT).show();
                        } else {
                            Attendance markAttendance = new Attendance(teacherName, subjectName, _12HourStartTime + " to " + _12HourStopTime, _12HourCurrentTime);
                            attendanceDatabaseReference.child(currentDate).child(userId).child(period).setValue(markAttendance);
                            Toast.makeText(StudentActivity.this, "Attendance is marked successfully", Toast.LENGTH_SHORT).show();
                            calculateTotalAttendance(userId);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(StudentActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(this, "QR Code Expired", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_LONG).show();
        }
    }
}
