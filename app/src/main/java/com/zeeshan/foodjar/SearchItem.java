package com.zeeshan.foodjar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.widget.SearchView;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.zeeshan.foodjar.adapters.ItemAdapter;
import com.zeeshan.foodjar.entities.OffersModel;
import com.zeeshan.foodjar.entities.Products;
import com.zeeshan.foodjar.utils.PreferenceUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchItem extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseProducts, databaseOffers;
    private ProgressBar progressBar;
    List<Products> productsList;
    SearchView ed_Search;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ImageView imageViewOfferBanner;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    ItemAdapter itemAdapter;
    String stock = "0";
    List<OffersModel> offersModelList;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);

        init();
        setUpToolbar();
        loadOfferBanner();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        Intent intent = new Intent(getApplicationContext(), SearchItem.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.myOrders:
                        startActivity(new Intent(getApplicationContext(), OrderHistory.class));
                        break;
                    case R.id.logout:
                        firebaseAuth.signOut();
                        Intent intent1 = new Intent(SearchItem.this, LoginUser.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                }
                return true;
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        loadItemList();
        ed_Search.setFocusable(true);
        ed_Search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                itemAdapter.getFilter().filter(s);
                return false;
            }
        });
    }
    private void loadOfferBanner() {
           databaseOffers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OffersModel offersModel = snapshot.getValue(OffersModel.class);
                    if (offersModel != null) {
                        offersModelList.add(offersModel);
                    } else {
                        imageViewOfferBanner.setVisibility(View.GONE);
                    }
                }
                if (offersModelList != null) {
                    Picasso.get()
                            .load(offersModelList.get(offersModelList.size() - 1).getOfferImage())
                            .placeholder(R.drawable.ic_noimage)
                            .fit()
                            .centerCrop()
                            .into(imageViewOfferBanner);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SearchItem.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadItemList() {
        databaseProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productsList.clear();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Products products = productSnapshot.getValue(Products.class);
                    if (!stock.equals(products.getItemStock())) {
                        productsList.add(products);
                    }
                }
                itemAdapter = new ItemAdapter(productsList);
                recyclerView.setAdapter(itemAdapter);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SearchItem.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.btnCart) {
            startActivity(new Intent(SearchItem.this, ShoppingCart.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchItem.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to close the FoodJar?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void init() {
        offersModelList = new ArrayList<>();
        databaseProducts = FirebaseDatabase.getInstance().getReference("products");
        databaseOffers = FirebaseDatabase.getInstance().getReference("offers");
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        ed_Search = findViewById(R.id.ed_Search);
        productsList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        imageViewOfferBanner = findViewById(R.id.imageViewOfferBanner);
    }
}