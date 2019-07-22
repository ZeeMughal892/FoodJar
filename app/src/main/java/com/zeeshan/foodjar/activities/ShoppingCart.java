package com.zeeshan.foodjar.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zeeshan.foodjar.R;
import com.zeeshan.foodjar.adapters.CartAdapter;
import com.zeeshan.foodjar.database.Database;
import com.zeeshan.foodjar.entities.Order;
import com.zeeshan.foodjar.entities.OrderRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShoppingCart extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseOrderRequest;

    Button btnPlaceOrder;
    TextView txtTotalAmount, txtTotalLabel;
    public static List<Order> orderList = new ArrayList<>();
    CartAdapter cartAdapter;
    ProgressBar progressBarCart;
    ImageView imgEmptyCart;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        init();
        setUpToolbar();
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        loadListItem();


        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnPlaceOrder.getText().equals("Place Order")) {
                    placeOrder();
                } else {
                    startActivity(new Intent(ShoppingCart.this, SearchItem.class));
                }
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
                Intent intent = new Intent(ShoppingCart.this, SearchItem.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void placeOrder() {
        OrderRequest orderRequest = new OrderRequest(
                String.valueOf(System.currentTimeMillis()),
                SearchItem.loginUserID,
                orderList,
                txtTotalAmount.getText().toString(),
                "PENDING", "None",
                String.valueOf(orderList.size())
        );


        //Submit to Firebase
        databaseOrderRequest.child(String.valueOf(System.currentTimeMillis()))
                .setValue(orderRequest);

        //Delete ShoppingCart
        new Database(getBaseContext()).cleanCart();
        Toast.makeText(ShoppingCart.this, "Thank you, Order Placed Successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ShoppingCart.this, SearchItem.class));
        finish();
    }

    private void loadListItem() {
        orderList = new Database(this).getCarts(SearchItem.loginUserID);

        if (orderList == null || orderList.isEmpty()) {
            txtTotalLabel.setVisibility(View.INVISIBLE);
            txtTotalAmount.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            imgEmptyCart.setVisibility(View.VISIBLE);
            btnPlaceOrder.setText(R.string.continue_shopping);
            progressBarCart.setVisibility(View.INVISIBLE);

        } else {
            cartAdapter = new CartAdapter(orderList);
            recyclerView.setAdapter(cartAdapter);
            progressBarCart.setVisibility(View.INVISIBLE);

            int total = 0;
            for (Order order : orderList) {
                total += (Integer.parseInt(order.getItemPrice())) * (Integer.parseInt(order.getItemQuantity()));
                Locale locale = new Locale("en", "US");
                NumberFormat fnt = NumberFormat.getCurrencyInstance(locale);

                txtTotalAmount.setText(fnt.format(total));
            }
        }
    }

    private void init() {
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        txtTotalLabel = findViewById(R.id.txtTotalLabel);
        recyclerView = findViewById(R.id.recyclerViewCart);
        databaseOrderRequest = FirebaseDatabase.getInstance().getReference("OrderRequests");
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        toolbar=findViewById(R.id.toolbar);
        progressBarCart = findViewById(R.id.progressBarCart);
        imgEmptyCart = findViewById(R.id.imgEmpty);
    }
}
