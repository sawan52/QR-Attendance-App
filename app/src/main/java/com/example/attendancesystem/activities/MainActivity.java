package com.example.attendancesystem.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.attendancesystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText userNameEditText, passwordEditText;
    private ProgressDialog progressDialog;
    private String spinnerText, username, password, dbPassword, sName, tID, sId, tName;
    private DatabaseReference databaseReference;
    private Bundle bundle;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Home Screen");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (!isConnected()) buildDialog(this).show();
        else {
            setContentView(R.layout.activity_main);

            progressDialog = new ProgressDialog(this);

            progressDialog = new ProgressDialog(this);
            bundle = new Bundle();

            userNameEditText = findViewById(R.id.userName_Edit_Text);
            passwordEditText = findViewById(R.id.password_Edit_Text);
            Button logIn = findViewById(R.id.logIn_Button);
            Button logOut = findViewById(R.id.log_out_button);
            Spinner loginMethod = findViewById(R.id.logInMethodSpinner);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.select_login_method, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            loginMethod.setAdapter(adapter);
            loginMethod.setOnItemSelectedListener(this);

            logIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogInAccount();
                }
            });

            logOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signOut();
                }
            });
        }
    }

    private void signOut() {

        mAuth.signOut();
        finish();
        sendUserToSignInActivity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser == null) {
            sendUserToSignInActivity();
        }
    }

    private void sendUserToSignInActivity() {

        Intent loginIntent = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(loginIntent);
        finish();
    }

    public boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            return (mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting());

        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or Wifi to access this. Press OK to Exit");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        spinnerText = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void LogInAccount() {

        switch (spinnerText) {
            case "admin":
                adminLogIn();
                break;
            case "teacher":
                teacherLogIn();
                break;
            case "student":
                studentLogIn();
                break;
            default:
                Toast.makeText(this, "Select profile first", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void studentLogIn() {

        username = userNameEditText.getText().toString();
        password = passwordEditText.getText().toString();

        if (username.isEmpty()) {
            userNameEditText.setError("User Id is required");
            userNameEditText.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }

        progressDialog.setTitle("Signing In");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dbRef = databaseReference.child("STUDENTS").child(username);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressDialog.dismiss();
                dbPassword = dataSnapshot.child("sPassword").getValue(String.class);
                sName = dataSnapshot.child("sName").getValue(String.class);
                sId = dataSnapshot.child("sId").getValue(String.class);
                verifyAndLogInStudent();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong\nPlease try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyAndLogInStudent() {

        if (password.equals(dbPassword)) {

            Intent intent = new Intent(MainActivity.this, StudentActivity.class);
            //Add your data to bundle
            bundle.putString("sNAME", sName);
            bundle.putString("sID", sId);
            //Add the bundle to the intent
            intent.putExtras(bundle);
            startActivity(intent);
            Toast.makeText(this, "Logged In Successfully", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Enter correct username and password", Toast.LENGTH_SHORT).show();
        }
    }


    private void teacherLogIn() {

        username = userNameEditText.getText().toString();
        password = passwordEditText.getText().toString();

        if (username.isEmpty()) {
            userNameEditText.setError("User Id is required");
            userNameEditText.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }

        progressDialog.setTitle("Signing In");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dbRef = databaseReference.child("TEACHERS").child(username);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressDialog.dismiss();
                dbPassword = dataSnapshot.child("tPassword").getValue(String.class);
                tID = dataSnapshot.child("tId").getValue(String.class);
                tName = dataSnapshot.child("tName").getValue(String.class);
                verifyAndLogInTeacher();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong\nPlease try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyAndLogInTeacher() {

        if (password.equals(dbPassword)) {

            Intent intent = new Intent(MainActivity.this, TeacherActivity.class);
            //Add your data to bundle
            bundle.putString("tID", tID);
            bundle.putString("tNAME", tName);
            //Add the bundle to the intent
            intent.putExtras(bundle);
            startActivity(intent);
            Toast.makeText(this, "Logged In Successfully", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Enter correct username and password", Toast.LENGTH_SHORT).show();
        }
    }

    private void adminLogIn() {

        username = userNameEditText.getText().toString();
        password = passwordEditText.getText().toString();

        if (username.isEmpty()) {
            userNameEditText.setError("User Id is required");
            userNameEditText.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }

        progressDialog.setTitle("Signing In");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dbRef = databaseReference.child("ADMIN");

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressDialog.dismiss();
                dbPassword = dataSnapshot.child(username).getValue(String.class);
                verifyAndLogInAdmin();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong\nPlease try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyAndLogInAdmin() {

        if (password.equals(dbPassword)) {

            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
            //Add your data to bundle
            bundle.putString("NAME", username);
            //Add the bundle to the intent
            intent.putExtras(bundle);
            startActivity(intent);
            Toast.makeText(this, "Logged In Successfully", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Enter correct username and password", Toast.LENGTH_SHORT).show();
        }
    }

}
