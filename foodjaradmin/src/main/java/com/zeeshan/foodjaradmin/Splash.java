package com.zeeshan.foodjaradmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zeeshan.foodjaradmin.R;

public class Splash extends AppCompatActivity {

    TextView txtTitle;
    ImageView imgLogo;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        txtTitle = findViewById(R.id.txtTitle);
        imgLogo = findViewById(R.id.imgLogo);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_transition);
        txtTitle.startAnimation(animation);
        imgLogo.startAnimation(animation);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            intent = new Intent(Splash.this, SearchItem.class);
        } else {
            intent = new Intent(this, LoginAdmin.class);
        }
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
