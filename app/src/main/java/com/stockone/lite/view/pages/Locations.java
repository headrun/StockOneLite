package com.stockone.lite.view.pages;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.blankj.utilcode.util.LogUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stockone.lite.R;
import com.stockone.lite.model.Products;
import com.stockone.lite.model.Zone;
import com.stockone.lite.utils.Constants;
import com.stockone.lite.view.adapter.InventoryAdapter;
import com.stockone.lite.view.adapter.LocationAdapter;
import com.stockone.lite.view.dialogs.AddLocation;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import spencerstudios.com.bungeelib.Bungee;

public class Locations extends AppCompatActivity {

    @BindView(R.id.toolbar_location) Toolbar toolbar;
    @BindView(R.id.rv_locations) RecyclerView recyclerView;
    @BindView(R.id.avi_locations)
    AVLoadingIndicatorView avi;
    private LocationAdapter adapter;
    private List<Zone> list = new ArrayList<>();
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new LocationAdapter(this);
        recyclerView.setAdapter(adapter);

        getLocationList();


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            Bungee.slideRight(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.add_location)
    void addLocation(){

        AddLocation addLocation = new AddLocation(this);
        addLocation.show();
    }

    private void getLocationList(){


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Zone_Path);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                list.clear(); //change here

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.child("userid").getValue() != null) {

                        LogUtils.e(dataSnapshot.child("userid").getValue().equals(auth.getUid()));

                        if (dataSnapshot.child("userid").getValue().equals(auth.getUid())) {

                            Zone zone = dataSnapshot.getValue(Zone.class);
                            dataSnapshot.getChildren();

                            list.add(zone);
                        }

                    }

                }
                adapter.setList(list);
                getDefaultZone();
                avi.hide();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getDefaultZone(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.DEFAULT_ZONE);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Zone zone = dataSnapshot.getValue(Zone.class);
                    dataSnapshot.getChildren();

                    list.add(zone);

                }

                adapter.setList(list);
                avi.hide();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
