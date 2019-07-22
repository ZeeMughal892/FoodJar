package com.zeeshan.foodjaradmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zeeshan.foodjaradmin.R;
import com.zeeshan.foodjaradmin.adapter.DeliveryBoyAdapter;
import com.zeeshan.foodjaradmin.entities.DeliveryBoy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllDeliveryBoys extends AppCompatActivity {

    RecyclerView recyclerViewDeliveryBoys;
    DatabaseReference databaseDeliveryBoys;
    DeliveryBoyAdapter deliveryBoyAdapter;
    ProgressBar progressBar;
    List<DeliveryBoy> deliveryBoyList;
    SearchView ed_Search;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_delivery_boys);
        init();
        setUpToolbar();

        recyclerViewDeliveryBoys.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewDeliveryBoys.setLayoutManager(mLayoutManager);
        recyclerViewDeliveryBoys.setItemAnimator(new DefaultItemAnimator());

        databaseDeliveryBoys.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                deliveryBoyList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DeliveryBoy deliveryBoy = snapshot.getValue(DeliveryBoy.class);

                    deliveryBoyList.add(deliveryBoy);

                }
                deliveryBoyAdapter = new DeliveryBoyAdapter(deliveryBoyList);
                recyclerViewDeliveryBoys.setAdapter(deliveryBoyAdapter);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AllDeliveryBoys.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        ed_Search.setFocusable(true);
        ed_Search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                deliveryBoyAdapter.getFilter().filter(s);
                return false;
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
                startActivity(new Intent(AllDeliveryBoys.this, SearchItem.class));

            }
        });
    }

    private void init() {
        recyclerViewDeliveryBoys = findViewById(R.id.recyclerViewDeliveryBoys);
        progressBar = findViewById(R.id.progressBarDeliveryBoys);
        deliveryBoyList = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);
        databaseDeliveryBoys = FirebaseDatabase.getInstance().getReference("Delivery Boys");
        ed_Search=findViewById(R.id.ed_Search);

    }
}
