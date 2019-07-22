package com.zeeshan.foodjardeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zeeshan.foodjardeliveryapp.adapter.OrderRequestAdapter;
import com.zeeshan.foodjardeliveryapp.entities.OrderRequest;
import com.zeeshan.foodjardeliveryapp.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

public class MyOrders extends AppCompatActivity {

    public static String loginUserID;
    public static String loginUserName;

    RecyclerView recyclerViewMyOrder;
    DatabaseReference databaseMyOrderRequests;
    ProgressBar progressBar;
    List<OrderRequest> orderRequestList;

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        init();
        setUpToolbar();
        final Intent intent = getIntent();

        if (intent.hasExtra("userID") && intent.hasExtra("username")) {
            loginUserID = getIntent().getStringExtra("userID");
            loginUserName = getIntent().getStringExtra("username");
        } else {
            loginUserID = PreferenceUtils.getUserID(this);
            loginUserName = PreferenceUtils.getUsername(this);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.recentOrders:
                        startActivity(new Intent(MyOrders.this, MyOldOrders.class));
                        break;
                    case R.id.logout:
                        PreferenceUtils.saveUsername(null, getApplicationContext());
                        PreferenceUtils.savePassword(null, getApplicationContext());
                        PreferenceUtils.saveUserID(null, getApplicationContext());
                        Intent intent1 = new Intent(MyOrders.this, MyOldOrders.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        finish();
                        break;
                }
                return true;
            }
        });

        recyclerViewMyOrder.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewMyOrder.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        recyclerViewMyOrder.setLayoutManager(mLayoutManager);
        recyclerViewMyOrder.setItemAnimator(new DefaultItemAnimator());

        databaseMyOrderRequests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderRequestList.clear();
                String status="Assigned";
                for (DataSnapshot orderDataSnapshot : dataSnapshot.getChildren()) {
                    OrderRequest orderRequest = orderDataSnapshot.getValue(OrderRequest.class);
                    if (MyOrders.loginUserID.equals(orderRequest.getAssignTo()) && status.equals(orderRequest.getOrderStatus())) {
                        orderRequestList.add(orderRequest);
                    }
                }
                OrderRequestAdapter orderRequestAdapter = new OrderRequestAdapter(orderRequestList);
                recyclerViewMyOrder.setAdapter(orderRequestAdapter);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyOrders.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void setUpToolbar() {

        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);

        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_sort_black_24dp);
        actionBarDrawerToggle.syncState();

        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void init() {
        recyclerViewMyOrder = findViewById(R.id.recyclerViewMyOrders);
        progressBar = findViewById(R.id.progressBarMyOrder);
        orderRequestList = new ArrayList<>();
        databaseMyOrderRequests = FirebaseDatabase.getInstance().getReference("OrderRequests");

        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
    }
}

