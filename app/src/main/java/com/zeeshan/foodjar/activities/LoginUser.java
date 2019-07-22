package com.zeeshan.foodjar.activities;

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

import com.zeeshan.foodjar.R;
import com.zeeshan.foodjar.entities.User;
import com.zeeshan.foodjar.utils.PreferenceUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class LoginUser extends AppCompatActivity {

    Button btnSignUp, btnLogin;
    DatabaseReference databaseUsers;
    EditText ed_Username, ed_Password;

    FirebaseStorage firebaseStorage;

    RadioButton rdoRemember;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        init();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginUser.this, RegisterUser.class));
            }
        });

        sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
        isLogin = sharedPreferences.getBoolean("isLogin", true);
        if (isLogin) {
            ed_Username.setText(sharedPreferences.getString("username", null));
            ed_Password.setText(sharedPreferences.getString("password", null));
            sharedPreferences.getString("userID", null);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginUser.this);
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
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogin);
        ed_Username = findViewById(R.id.ed_Username);
        ed_Password = findViewById(R.id.ed_Password);
        rdoRemember = findViewById(R.id.rdoRemember);
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        firebaseStorage = FirebaseStorage.getInstance();

        if (PreferenceUtils.getUserID(this) != null) {
            startActivity(new Intent(LoginUser.this, SearchItem.class));
        }
    }

    private void loginUser() {
        final String userName = ed_Username.getText().toString().trim();
        final String password = ed_Password.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "Please Enter User Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        } else {

            databaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot userData : dataSnapshot.getChildren()) {
                        User user = userData.getValue(User.class);

                        if (userName.equals(user.getUserName()) && password.equals(user.getPassword())) {
                            Intent intent = new Intent(LoginUser.this, SearchItem.class);

                            if (rdoRemember.isChecked()) {
                                PreferenceUtils.saveUserID(user.getUserID(), LoginUser.this);
                                PreferenceUtils.saveUsername(userName, LoginUser.this);
                                PreferenceUtils.savePassword(password, LoginUser.this);
                                intent.putExtra("userID", user.getUserID());
                                intent.putExtra("username", user.getUserName());
                                SearchItem.loginUserName = user.getUserName();
                                SearchItem.loginUserID = user.getUserID();
                            }
                            editor.putBoolean("isLogin", true);
                            editor.putString("userID", user.getUserID());
                            editor.putString("username", userName);
                            editor.putString("password", password);
                            editor.apply();

                            Toast.makeText(LoginUser.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            intent.putExtra("userID", user.getUserID());
                            intent.putExtra("username", user.getUserName());

                            SearchItem.loginUserName = user.getUserName();
                            SearchItem.loginUserID = user.getUserID();

                            startActivity(intent);
                            finish();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(LoginUser.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
