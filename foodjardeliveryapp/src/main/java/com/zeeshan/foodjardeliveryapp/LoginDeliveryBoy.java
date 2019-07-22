package com.zeeshan.foodjardeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zeeshan.foodjardeliveryapp.entities.DeliveryBoy;
import com.zeeshan.foodjardeliveryapp.utils.PreferenceUtils;

public class LoginDeliveryBoy extends AppCompatActivity {

    Button btnLogin;
    DatabaseReference databaseDeliveryBoy;
    EditText ed_Username, ed_Password;
    RadioButton rdoRemember;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean isLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_delivery_boy);
        init();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginStaff();
            }
        });
        sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isLogin = sharedPreferences.getBoolean("isLogin", true);
        if (isLogin) {
            ed_Username.setText(sharedPreferences.getString("username", null));
            ed_Password.setText(sharedPreferences.getString("password", null));
            sharedPreferences.getString("userID", null);
        }
    }

    private void loginStaff() {
        final String userName = ed_Username.getText().toString().trim();
        final String password = ed_Password.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "Please Enter User Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        } else {

            databaseDeliveryBoy.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot userData : dataSnapshot.getChildren()) {
                        DeliveryBoy deliveryBoy = userData.getValue(DeliveryBoy.class);

                        if (userName.equals(deliveryBoy.getBoyName()) && password.equals(deliveryBoy.getBoyPassword())) {
                            Intent intent = new Intent(LoginDeliveryBoy.this, MyOrders.class);

                            if (rdoRemember.isChecked()) {
                                PreferenceUtils.saveUserID(deliveryBoy.getBoyID(), LoginDeliveryBoy.this);
                                PreferenceUtils.saveUsername(userName, LoginDeliveryBoy.this);
                                PreferenceUtils.savePassword(password, LoginDeliveryBoy.this);
                                intent.putExtra("userID", deliveryBoy.getBoyID());
                                intent.putExtra("username", deliveryBoy.getBoyName());
                                MyOrders.loginUserName = deliveryBoy.getBoyName();
                                MyOrders.loginUserID = deliveryBoy.getBoyID();
                            }
                            editor.putBoolean("isLogin", true);
                            editor.putString("userID", deliveryBoy.getBoyID());
                            editor.putString("username", userName);
                            editor.putString("password", password);
                            editor.apply();

                            Toast.makeText(LoginDeliveryBoy.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            intent.putExtra("userID", deliveryBoy.getBoyID());
                            intent.putExtra("username", deliveryBoy.getBoyName());

                            MyOrders.loginUserName = deliveryBoy.getBoyName();
                            MyOrders.loginUserID = deliveryBoy.getBoyID();

                            startActivity(intent);
                            finish();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(LoginDeliveryBoy.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void init() {
        btnLogin = findViewById(R.id.btnLogin);
        ed_Username = findViewById(R.id.ed_Username);
        ed_Password = findViewById(R.id.ed_Password);
        rdoRemember = findViewById(R.id.rdoRemember);
        databaseDeliveryBoy = FirebaseDatabase.getInstance().getReference("Delivery Boys");
        if (PreferenceUtils.getUserID(this) != null) {
            startActivity(new Intent(LoginDeliveryBoy.this, MyOrders.class));
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginDeliveryBoy.this);
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

}
