package com.zeeshan.foodjardeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zeeshan.foodjardeliveryapp.adapter.OrderAdapter;
import com.zeeshan.foodjardeliveryapp.entities.DeliveryBoy;
import com.zeeshan.foodjardeliveryapp.entities.Order;
import com.zeeshan.foodjardeliveryapp.entities.OrderRequest;
import com.zeeshan.foodjardeliveryapp.entities.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrderDetails extends AppCompatActivity {

    public static String OrderID;
    TextView txtOrderId, txtUsername, txtPhoneNumber, txtShopName, txtShopAddress, txtTotalAmount, txtDate, txtOrderStatus;
    Button btnMark;
    RecyclerView recyclerViewOrderItems;
    List<OrderRequest> orderRequestList;
    Toolbar toolbar;
    ProgressBar progressBar;
    DatabaseReference databaseUsers, databaseOrderRequests, databaseDeliveryBoys;
    OrderRequest currentOrder;
    User currentUser;
    OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        init();
        setUpToolbar();
        loadOrder();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewOrderItems.setHasFixedSize(true);
        recyclerViewOrderItems.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        recyclerViewOrderItems.setLayoutManager(mLayoutManager);
        recyclerViewOrderItems.setItemAnimator(new DefaultItemAnimator());

        btnMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String orderId = OrderDetails.OrderID;
                databaseOrderRequests.child(orderId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        currentOrder = dataSnapshot.getValue(OrderRequest.class);
                        String amount = currentOrder.getTotalAmount();
                        String userId = currentOrder.getUserID();
                        String status = "Delivered";
                        String assignTo = MyOrders.loginUserID;
                        String itemCount = currentOrder.getItemCount();
                        List<Order> orders = new ArrayList<>();
                        String boyName = MyOrders.loginUserName;
                        orders = currentOrder.getOrderList();
                        OrderRequest orderRequest = new OrderRequest(orderId, userId, orders, amount, status, assignTo, itemCount);
                        databaseOrderRequests.child(orderId).setValue(orderRequest);
                        Toast.makeText(OrderDetails.this, "Order Marked as Delivered" + boyName, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(OrderDetails.this, MyOrders.class));

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(OrderDetails.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void loadOrder() {
        databaseOrderRequests.child(OrderID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentOrder = dataSnapshot.getValue(OrderRequest.class);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.parseLong(currentOrder.getOrderID()));

                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                String date = mDay + "/" + mMonth + "/" + mYear;

                txtOrderId.setText(currentOrder.getOrderID());
                txtDate.setText(date);
                txtTotalAmount.setText(currentOrder.getTotalAmount());
                txtOrderStatus.setText(currentOrder.getOrderStatus());

                databaseUsers.child(currentOrder.getUserID()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        currentUser = dataSnapshot.getValue(User.class);

                        String name = currentUser.getUserName();
                        String phone = currentUser.getPhoneNumber();
                        String shop = currentUser.getShopName();
                        String address = currentUser.getAddress();

                        txtUsername.setText(name);
                        txtPhoneNumber.setText(phone);
                        txtShopAddress.setText(shop);
                        txtShopName.setText(address);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(OrderDetails.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                orderAdapter = new OrderAdapter(currentOrder.getOrderList());
                recyclerViewOrderItems.setAdapter(orderAdapter);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OrderDetails.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderDetails.this, MyOrders.class));

            }
        });
    }

    private void init() {
        txtOrderId = findViewById(R.id.txtOrderID);
        txtUsername = findViewById(R.id.txtUserName);
        txtPhoneNumber = findViewById(R.id.txtPhone);
        txtShopName = findViewById(R.id.txtShopName);
        txtShopAddress = findViewById(R.id.txtShopAddress);
        txtTotalAmount = findViewById(R.id.txtTotalAmountOrder);
        txtDate = findViewById(R.id.txtDate);
        txtOrderStatus = findViewById(R.id.txtOrderStatus);
        btnMark = findViewById(R.id.btnMark);


        recyclerViewOrderItems = findViewById(R.id.recyclerViewOrderItems);


        progressBar = findViewById(R.id.progressBarOrderDetails);
        orderRequestList = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);
        databaseOrderRequests = FirebaseDatabase.getInstance().getReference("OrderRequests");
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        databaseDeliveryBoys = FirebaseDatabase.getInstance().getReference("Delivery Boys");
    }

}
