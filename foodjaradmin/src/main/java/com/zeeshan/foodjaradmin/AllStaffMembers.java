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
import com.zeeshan.foodjaradmin.adapter.StaffAdapter;
import com.zeeshan.foodjaradmin.entities.Staff;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllStaffMembers extends AppCompatActivity {

    RecyclerView recyclerViewStaff;
    DatabaseReference databaseStaff;
    StaffAdapter staffAdapter;
    ProgressBar progressBar;
    List<Staff> staffList;
    SearchView ed_Search;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_staff_members);
        init();
        setUpToolbar();
        recyclerViewStaff.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewStaff.setLayoutManager(mLayoutManager);
        recyclerViewStaff.setItemAnimator(new DefaultItemAnimator());

        databaseStaff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                staffList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Staff staff = snapshot.getValue(Staff.class);

                    staffList.add(staff);

                }
                staffAdapter = new StaffAdapter(staffList);
                recyclerViewStaff.setAdapter(staffAdapter);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AllStaffMembers.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
                staffAdapter.getFilter().filter(s);
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
                startActivity(new Intent(AllStaffMembers.this, SearchItem.class));

            }
        });
    }

    private void init() {
        recyclerViewStaff = findViewById(R.id.recyclerViewDeliveryBoys);
        progressBar = findViewById(R.id.progressBarUser);
        staffList = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);
        databaseStaff = FirebaseDatabase.getInstance().getReference("Staff");
        ed_Search=findViewById(R.id.ed_Search);

    }
}
