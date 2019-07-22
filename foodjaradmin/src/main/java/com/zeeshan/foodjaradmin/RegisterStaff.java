package com.zeeshan.foodjaradmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zeeshan.foodjaradmin.R;
import com.zeeshan.foodjaradmin.entities.Staff;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterStaff extends AppCompatActivity {

    EditText edStaffName, edStaffPassword, edPhoneNumber;
    Button btnRegister;
    DatabaseReference databaseStaff;
    Toolbar toolbar;

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
        String staffName = edStaffName.getText().toString().trim();
        String staffPassword = edStaffPassword.getText().toString().trim();
        String staffPhone = edPhoneNumber.getText().toString().trim();
        if (TextUtils.isEmpty(staffName)) {
            Toast.makeText(this, "Please Enter Staff Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(staffPassword)) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(staffPhone)) {
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
        } else {
            String staffId = databaseStaff.push().getKey();
            Staff staff = new Staff(staffId,staffName,staffPassword,staffPhone,"Staff");
            databaseStaff.child(staffId).setValue(staff);
            Toast.makeText(RegisterStaff.this, "Staff Member Registered Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterStaff.this, AllStaffMembers.class));
            finish();
        }

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
        edStaffName = findViewById(R.id.ed_Username);
        edStaffPassword = findViewById(R.id.ed_Password);
        edPhoneNumber = findViewById(R.id.ed_PhoneNumber);
        btnRegister = findViewById(R.id.btnRegister);
        databaseStaff = FirebaseDatabase.getInstance().getReference("Staff");
        toolbar=findViewById(R.id.toolbar);
    }

}
