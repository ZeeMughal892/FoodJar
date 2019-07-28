package com.zeeshan.foodjar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.service.Common;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.zeeshan.foodjar.Interface.RecyclerHelperTouchItemListener;
import com.zeeshan.foodjar.adapters.CartAdapter;
import com.zeeshan.foodjar.adapters.CartViewHolder;
import com.zeeshan.foodjar.database.Database;
import com.zeeshan.foodjar.entities.Order;
import com.zeeshan.foodjar.entities.OrderRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zeeshan.foodjar.entities.Products;
import com.zeeshan.foodjar.utils.RecyclerItemTouchHelper;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShoppingCart extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseOrderRequest, databaseProducts;

    Button btnPlaceOrder;
    TextView txtTotalAmount, txtTotalLabel, txtTotalVAT, txtVat;
    public static List<Order> orderList = new ArrayList<>();
    CartAdapter cartAdapter;
    ProgressBar progressBarCart;
    ImageView imgEmptyCart;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    List<Products> productsList;
    String Stock;
    Products products, currentProduct;
    int remainingStock;
    int amount = 0, vat = 0, totalAmount = 0;
    ConstraintLayout rootLayout;

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
                Intent intent=new Intent(ShoppingCart.this,SearchItem.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void placeOrder() {
        final String orderID = String.valueOf(System.currentTimeMillis());
        final OrderRequest orderRequest = new OrderRequest(
                orderID,
                firebaseUser.getUid(),
                orderList,
                String.valueOf(vat),
                String.valueOf(totalAmount),
                "PENDING",
                "None",
                String.valueOf(orderList.size())
        );
        AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingCart.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Your Order has been placed. Order will be delivered in 48 hours.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        for (int i = 0; i < orderList.size(); i++) {
                            final String itemId = orderList.get(i).getItemID();
                            final String itemName = orderList.get(i).getItemName();
                            final String itemCategory = orderList.get(i).getItemCategory();
                            final String itemPrice = orderList.get(i).getItemPrice();
                            final String itemQuantity = orderList.get(i).getItemQuantity();
                            final String itemQuantityPerPack = orderList.get(i).getItemQuantityPerPack();
                            final String itemUnit = orderList.get(i).getItemUnit();
                            final String itemImage = orderList.get(i).getItemImage();
                            final String itemDescription = "";

                            Stock = productsList.get(i).getItemStock();
                            remainingStock = Integer.parseInt(Stock) - Integer.parseInt(itemQuantity);
                            products = new Products(itemId, itemName, itemCategory, String.valueOf(remainingStock), itemQuantityPerPack, itemUnit, itemPrice, itemImage, itemDescription);
                            databaseProducts.child(itemId).setValue(products);
                        }
                        databaseOrderRequest.child(orderID).setValue(orderRequest);
                        new Database(getBaseContext()).cleanCart();
                        Intent intent = new Intent(ShoppingCart.this, SearchItem.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void loadListItem() {
        orderList = new Database(this).getCarts(firebaseUser.getUid());


        if (orderList == null || orderList.isEmpty()) {
            txtTotalLabel.setVisibility(View.GONE);
            txtTotalAmount.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            imgEmptyCart.setVisibility(View.VISIBLE);
            btnPlaceOrder.setText(R.string.continue_shopping);
            progressBarCart.setVisibility(View.GONE);
            txtVat.setVisibility(View.GONE);
            txtTotalVAT.setVisibility(View.GONE);
        } else {
            cartAdapter = new CartAdapter(orderList);
            recyclerView.setAdapter(cartAdapter);
            progressBarCart.setVisibility(View.GONE);

            for (int i = 0; i < orderList.size(); i++) {
                databaseProducts.child(orderList.get(i).getItemID()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        currentProduct = dataSnapshot.getValue(Products.class);
                        if (currentProduct != null) {
                            productsList.add(currentProduct);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ShoppingCart.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            amount = 0;
            vat = 0;
            totalAmount = 0;

            for (Order order : orderList) {
                amount += (Integer.parseInt(order.getItemPrice())) * (Integer.parseInt(order.getItemQuantity()));
            }
            vat = (amount * 5) / 100;
            txtTotalVAT.setText(" SAR " + vat);
            totalAmount = vat + amount;
            txtTotalAmount.setText(" SAR " + totalAmount);
        }
    }

    @Override
    public void onBackPressed() {
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        txtTotalLabel = findViewById(R.id.txtTotalLabel);
        recyclerView = findViewById(R.id.recyclerViewCart);
        databaseOrderRequest = FirebaseDatabase.getInstance().getReference("orderRequests");
        databaseProducts = FirebaseDatabase.getInstance().getReference("products");
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        toolbar = findViewById(R.id.toolbar);
        progressBarCart = findViewById(R.id.progressBarCart);
        imgEmptyCart = findViewById(R.id.imgEmpty);
        productsList = new ArrayList<>();
        txtTotalVAT = findViewById(R.id.txtTotalVAT);
        txtVat = findViewById(R.id.txtVAT);
        rootLayout=findViewById(R.id.rootLayout);
    }

}
