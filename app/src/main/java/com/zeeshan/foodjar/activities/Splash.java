package com.zeeshan.foodjar.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeeshan.foodjar.R;

public class Splash extends AppCompatActivity {

    TextView txtTitle;
    ImageView imgLogo;
    Intent intent;
    SharedPreferences sharedPreferences;
    Boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        txtTitle = findViewById(R.id.txtTitle);
        imgLogo = findViewById(R.id.imgLogo);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_transition);
        txtTitle.startAnimation(animation);
        imgLogo.startAnimation(animation);

        intent = new Intent(this, LoginUser.class);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();

                } finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();
    }
}
