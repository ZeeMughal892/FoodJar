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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zeeshan.foodjaradmin.R;
import com.zeeshan.foodjaradmin.entities.DeliveryBoy;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterDeliveryBoy extends AppCompatActivity {

    EditText edBoyName, edEmail, edBoyPassword, edBoyPhoneNumber;
    Button btnRegister;
    DatabaseReference databaseDeliveryBoys;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_delivery_boy);
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
        final String name = edBoyName.getText().toString().trim();
        final String email = edEmail.getText().toString().trim();
        final String password = edBoyPassword.getText().toString().trim();
        final String phone = edBoyPhoneNumber.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please Enter Full Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DeliveryBoy deliveryBoy = new DeliveryBoy(id, name, email, password, phone);

                        databaseDeliveryBoys
                                .child(id)
                                .setValue(deliveryBoy)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(RegisterDeliveryBoy.this, "Delivery Boy Registered Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegisterDeliveryBoy.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }

    }

    private void setUpToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterDeliveryBoy.this,SearchItem.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
            }
        });
    }

    private void init() {
        edBoyName = findViewById(R.id.ed_boyName);
        edEmail = findViewById(R.id.ed_Email);
        edBoyPassword = findViewById(R.id.ed_BoyPassword);
        edBoyPhoneNumber = findViewById(R.id.ed_BoyPhoneNumber);
        btnRegister = findViewById(R.id.btnRegisterBoy);
        databaseDeliveryBoys = FirebaseDatabase.getInstance().getReference("deliveryBoys");
        toolbar = findViewById(R.id.toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

}
