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
import com.zeeshan.foodjaradmin.entities.DeliveryBoy;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterDeliveryBoy extends AppCompatActivity {

    EditText edBoyName, edBoyPassword, edBoyPhoneNumber;
    Button btnRegister;
    DatabaseReference databaseDeliveryBoys;
    Toolbar toolbar;

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
        String name = edBoyName.getText().toString().trim();
        String password = edBoyPassword.getText().toString().trim();
        String phone = edBoyPhoneNumber.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please Enter Staff Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
        } else {
            String id = databaseDeliveryBoys.push().getKey();
            DeliveryBoy deliveryBoy = new DeliveryBoy(id,name,password,phone);
            databaseDeliveryBoys.child(id).setValue(deliveryBoy);
            Toast.makeText(RegisterDeliveryBoy.this, "Delivery Boy Registered Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterDeliveryBoy.this, AllDeliveryBoys.class));
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
                startActivity(new Intent(RegisterDeliveryBoy.this, SearchItem.class));

            }
        });
    }
    private void init() {
        edBoyName = findViewById(R.id.ed_boyName);
        edBoyPassword = findViewById(R.id.ed_BoyPassword);
        edBoyPhoneNumber = findViewById(R.id.ed_BoyPhoneNumber);
        btnRegister = findViewById(R.id.btnRegisterBoy);
        databaseDeliveryBoys = FirebaseDatabase.getInstance().getReference("Delivery Boys");
        toolbar=findViewById(R.id.toolbar);
    }

}
