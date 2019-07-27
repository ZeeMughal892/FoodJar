package com.zeeshan.foodjaradmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zeeshan.foodjaradmin.R;
import com.zeeshan.foodjaradmin.entities.Staff;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterStaff extends AppCompatActivity {

    EditText edStaffName, edEmail, edStaffPassword, edPhoneNumber;
    Button btnRegister;
    DatabaseReference databaseStaff;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ProgressBar progressBarRegisterStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_staff);
        init();
        setUpToolbar();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerStaff();
            }
        });
    }

    private void registerStaff() {
        final String staffName = edStaffName.getText().toString().trim();
        final String staffEmail = edEmail.getText().toString().trim();
        final String staffPassword = edStaffPassword.getText().toString().trim();
        final String staffPhone = edPhoneNumber.getText().toString().trim();
        if (TextUtils.isEmpty(staffName)) {
            Toast.makeText(this, "Please Enter Staff Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(staffEmail)) {
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(staffPassword)) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(staffPhone)) {
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
        } else {
            progressBarRegisterStaff.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(staffEmail, staffPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String staffId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        Staff staff = new Staff(staffId, staffName, staffEmail, staffPassword, staffPhone);
                        databaseStaff
                                .child(staffId)
                                .setValue(staff).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(RegisterStaff.this, "Staff Member Registered Successfully", Toast.LENGTH_SHORT).show();
                                progressBarRegisterStaff.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegisterStaff.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBarRegisterStaff.setVisibility(View.GONE);
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private void setUpToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterStaff.this, SearchItem.class));
            }
        });
    }

    private void init() {
        edStaffName = findViewById(R.id.ed_StaffName);
        edEmail = findViewById(R.id.ed_Email);
        edStaffPassword = findViewById(R.id.ed_StaffPassword);
        edPhoneNumber = findViewById(R.id.ed_StaffPhoneNumber);
        btnRegister = findViewById(R.id.btnRegisterStaff);
        databaseStaff = FirebaseDatabase.getInstance().getReference("staff");
        toolbar = findViewById(R.id.toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        progressBarRegisterStaff = findViewById(R.id.progressBarRegisterStaff);
    }

}
