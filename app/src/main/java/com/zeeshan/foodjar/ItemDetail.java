package com.zeeshan.foodjar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zeeshan.foodjar.database.Database;
import com.zeeshan.foodjar.entities.Order;
import com.zeeshan.foodjar.entities.Products;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ItemDetail extends AppCompatActivity {

    ImageView imgItem;
    TextView txtItemName, txtItemCategory, txtItemPrice, txtItemQuantity, txtDescription;
    Button btnAddToCart;
    ElegantNumberButton numberButton;
    static public String itemID;
    Products currentItem;
    DatabaseReference databaseOrders, databaseProducts;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        init();
        loadItemDetails();
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemsToCart();
            }
        });
        setUpToolbar();
    }

    private void setUpToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ItemDetail.this, SearchItem.class));

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btnCart) {
            startActivity(new Intent(ItemDetail.this, ShoppingCart.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadItemDetails() {
        databaseProducts.child(itemID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentItem = dataSnapshot.getValue(Products.class);
                if (currentItem != null) {
                    String Price;
                    String Items = "ITEMS";
                    String Stock = "In Stock : " + currentItem.getItemStock() + " " + currentItem.getItemUnit() + " Left";

                    if (Items.equals(currentItem.getItemUnit())) {
                        Price = "Rs. " + currentItem.getItemPrice() + " /Item";
                    } else {
                        Price = "Rs. " + currentItem.getItemPrice() + " /" + currentItem.getItemUnit();
                    }
                    txtItemName.setText(currentItem.getItemName());
                    txtItemCategory.setText(currentItem.getItemCategory());
                    txtItemPrice.setText(Price);
                    txtDescription.setText(currentItem.getItemDescription());
                    txtItemQuantity.setText(Stock);
                    numberButton.setRange(1, Integer.parseInt(currentItem.getItemStock()));
                    Picasso.get()
                            .load(currentItem.getItemImage())
                            .fit()
                            .centerCrop()
                            .placeholder(R.drawable.ic_noimage)
                            .into(imgItem);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ItemDetail.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        databaseProducts = FirebaseDatabase.getInstance().getReference("products");
        databaseOrders = FirebaseDatabase.getInstance().getReference("orders");
        imgItem = findViewById(R.id.image_item_detail);
        txtItemName = findViewById(R.id.txtItemNameDetails);
        txtItemCategory = findViewById(R.id.txtItemCategoryDetails);
        txtItemPrice = findViewById(R.id.txtItemPriceDetails);
        txtItemQuantity = findViewById(R.id.txtItemQuantityDetails);
        txtDescription = findViewById(R.id.txtItemDescriptionDetails);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        toolbar = findViewById(R.id.toolbar);
        numberButton = findViewById(R.id.numberButton);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    private void addItemsToCart() {
        new Database(this).addToCart(new Order(
                itemID,
                currentItem.getItemName(),
                currentItem.getItemCategory(),
                currentItem.getItemPrice(),
                numberButton.getNumber(),
                currentItem.getItemStockPerPack(),
                currentItem.getItemUnit(),
                currentItem.getItemImage(),
                firebaseUser.getUid(),
                currentItem.getItemDescription()
        ));
        Toast.makeText(ItemDetail.this, "Added to Shopping Cart", Toast.LENGTH_SHORT).show();
    }
}
