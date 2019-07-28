package com.zeeshan.foodjaradmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zeeshan.foodjaradmin.adapter.OrderRequestAdapter;
import com.zeeshan.foodjaradmin.entities.OrderRequest;

import java.util.ArrayList;
import java.util.List;

public class AdminDeliveredOrders extends AppCompatActivity {
    RecyclerView recyclerViewOrder;
    DatabaseReference databaseOrderRequests;
    ProgressBar progressBar;
    List<OrderRequest> orderRequestList;
    Toolbar toolbar;
    String status = "DELIVERED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivered_orders);
        init();
        setUpToolbar();
        recyclerViewOrder.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewOrder.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        recyclerViewOrder.setLayoutManager(mLayoutManager);
        recyclerViewOrder.setItemAnimator(new DefaultItemAnimator());

        databaseOrderRequests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderRequestList.clear();
                for (DataSnapshot orderDataSnapshot : dataSnapshot.getChildren()) {
                    OrderRequest orderRequest = orderDataSnapshot.getValue(OrderRequest.class);
                    if (status.equals(orderRequest.getOrderStatus())) {
                        orderRequestList.add(orderRequest);
                    }
                }
                OrderRequestAdapter orderRequestAdapter = new OrderRequestAdapter(orderRequestList);
                recyclerViewOrder.setAdapter(orderRequestAdapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminDeliveredOrders.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }
    @Override
    public void onBackPressed() {

    }
    private void setUpToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminDeliveredOrders.this,SearchItem.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {
        recyclerViewOrder = findViewById(R.id.recyclerViewOrders);
        progressBar = findViewById(R.id.progressBarOrder);
        orderRequestList = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);
        databaseOrderRequests = FirebaseDatabase.getInstance().getReference("orderRequests");

    }
}