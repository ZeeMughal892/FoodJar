package com.zeeshan.foodjaradmin.utils;


import android.app.Application;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zeeshan.foodjaradmin.SearchItem;

public class Home extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser!=null){
            Intent i = new Intent(this, SearchItem.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }
}