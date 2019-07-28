package com.zeeshan.foodjardeliveryapp.utils;

import android.app.Application;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zeeshan.foodjardeliveryapp.DeliveryAppPendingOrders;

public class Home extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser!=null){
            startActivity(new Intent(Home.this, DeliveryAppPendingOrders.class));
        }
    }
}