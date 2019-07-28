package com.zeeshan.foodjar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.zeeshan.foodjar.entities.DeliveryBoy;
import com.zeeshan.foodjar.entities.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class RegisterUser extends AppCompatActivity {

    private Button btnSignIn, btnRegister;
    private DatabaseReference databaseUsers, databaseDeliveryBoys;
    ProgressBar progressBarRegister;
    private EditText ed_FullName, ed_Email, ed_Password, ed_ShopName, ed_PhoneNumber, ed_Address;
    private Spinner spinnerReferredBy;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    List<DeliveryBoy> deliveryBoyList;
    List<String> boyNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        init();
        loadSpinnerWithBoysNames();
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterUser.this, LoginUser.class));
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewUser();
            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUser.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to close the FoodJar?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void loadSpinnerWithBoysNames() {
        databaseDeliveryBoys.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DeliveryBoy deliveryBoy = snapshot.getValue(DeliveryBoy.class);
                    deliveryBoyList.add(deliveryBoy);
                }
                boyNames.clear();
                for (int i = 0; i < deliveryBoyList.size(); i++) {
                    boyNames.add(deliveryBoyList.get(i).getBoyName());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RegisterUser.this, android.R.layout.simple_spinner_dropdown_item, boyNames);
                spinnerReferredBy.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RegisterUser.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNewUser() {

        final String fullName = ed_FullName.getText().toString().trim();
        final String email = ed_Email.getText().toString().trim();
        final String password = ed_Password.getText().toString().trim();
        final String shopName = ed_ShopName.getText().toString().trim();
        final String phoneNumber = ed_PhoneNumber.getText().toString().trim();
        final String address = ed_Address.getText().toString().trim();

        final String referredBy = spinnerReferredBy.getSelectedItem().toString().trim();

        if (TextUtils.isEmpty(fullName)) {
            Toast.makeText(this, "Please Enter Your Full Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please Enter Your Email Address", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(shopName)) {
            Toast.makeText(this, "Please Enter Shop Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Please Enter Shop Address", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(referredBy)) {
            Toast.makeText(this, "Please Select Name of Person Who Referred You", Toast.LENGTH_SHORT).show();
        } else {
            progressBarRegister.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                User user = new User(userId, fullName, email, password, shopName, phoneNumber, address, referredBy);
                                databaseUsers
                                        .child(userId)
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBarRegister.setVisibility(View.GONE);
                                        Toast.makeText(RegisterUser.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterUser.this, LoginUser.class));
                                        finish();
                                    }
                                });
                            } else {
                                Toast.makeText(RegisterUser.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBarRegister.setVisibility(View.GONE);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegisterUser.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBarRegister.setVisibility(View.GONE);
                }
            });
        }
    }

    private void init() {
        boyNames = new ArrayList<>();
        btnSignIn = findViewById(R.id.btnSignIn);
        ed_FullName = findViewById(R.id.ed_FullName);
        ed_Email = findViewById(R.id.ed_Email);
        ed_Password = findViewById(R.id.ed_Password);
        ed_ShopName = findViewById(R.id.ed_ShopName);
        ed_PhoneNumber = findViewById(R.id.ed_PhoneNumber);
        ed_Address = findViewById(R.id.ed_Address);
        spinnerReferredBy = findViewById(R.id.spinnerReferredBy);
        btnRegister = findViewById(R.id.btnRegister);
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseDeliveryBoys = FirebaseDatabase.getInstance().getReference("deliveryBoys");
        deliveryBoyList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        progressBarRegister = findViewById(R.id.progressBarRegister);
    }

}
