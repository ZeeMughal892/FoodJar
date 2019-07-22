package com.zeeshan.foodjar.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zeeshan.foodjar.R;
import com.zeeshan.foodjar.entities.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterUser extends AppCompatActivity {

    private Button btnSignIn,btnRegister;
    private DatabaseReference databaseUsers;

    private EditText ed_Username,ed_Password,ed_Shopname,ed_PhoneNumber,ed_Address,ed_ReferredBy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        init();
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

    private void init() {
        btnSignIn = findViewById(R.id.btnSignIn);
        ed_Username = findViewById(R.id.ed_Username);
        ed_Password = findViewById(R.id.ed_Password);
        ed_Shopname = findViewById(R.id.ed_ShopName);
        ed_PhoneNumber = findViewById(R.id.ed_PhoneNumber);
        ed_Address = findViewById(R.id.ed_Address);
        ed_ReferredBy = findViewById(R.id.ed_ReferredBy);
        btnRegister = findViewById(R.id.btnRegister);
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
    }
    private void addNewUser(){
        String userName=ed_Username.getText().toString().trim();
        String password=ed_Password.getText().toString().trim();
        String shopName=ed_Shopname.getText().toString().trim();
        String phoneNumber=ed_PhoneNumber.getText().toString().trim();
        String address=ed_Address.getText().toString().trim();
        String referredBy=ed_ReferredBy.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "Please Enter User Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(shopName)) {
            Toast.makeText(this, "Please Enter Shop Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Please Enter Shop Address", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(referredBy)) {
            Toast.makeText(this, "Please Enter Name of Person Who Referred You", Toast.LENGTH_SHORT).show();
        }else {
            String userId = databaseUsers.push().getKey();
            User user = new User(userId,userName,password,"User",shopName,phoneNumber,address,referredBy);
            databaseUsers.child(userId).setValue(user);
            Toast.makeText(RegisterUser.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterUser.this,LoginUser.class));
            finish();
        }

    }


}
