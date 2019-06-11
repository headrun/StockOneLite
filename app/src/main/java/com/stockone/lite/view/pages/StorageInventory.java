package com.stockone.lite.view.pages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.blankj.utilcode.util.LogUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stockone.lite.R;
import com.stockone.lite.model.ProductLocation;
import com.stockone.lite.model.Products;
import com.stockone.lite.utils.Constants;
import com.stockone.lite.view.adapter.InventoryAdapter;
import com.stockone.lite.view.adapter.StorageInventoryAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import spencerstudios.com.bungeelib.Bungee;

public class StorageInventory extends AppCompatActivity {

    @BindView(R.id.toolbar_storage_inv) Toolbar toolbar;
    @BindView(R.id.rv_storage_inventory) RecyclerView recyclerView;
    @BindView(R.id.empty_storage)
    RelativeLayout empty_storage;
    @BindView(R.id.lottie_storage)
    LottieAnimationView lottie;
    private StorageInventoryAdapter adapter;

    private List<ProductLocation> list = new ArrayList<>();

    String loc_name;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_inventory);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lottie.setAnimation("search_file.json");
        lottie.loop(true);
        lottie.playAnimation();

        mAuth = FirebaseAuth.getInstance();

        //getIntent
        Intent extras = getIntent();
        Bundle bundle = extras.getExtras();
        assert bundle != null;
        loc_name = bundle.getString("loc_name");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new StorageInventoryAdapter(this);
        recyclerView.setAdapter(adapter);

        getStorageInventory();


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            Bungee.slideRight(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getStorageInventory(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Product_Location_Path);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                list.clear(); //change here

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.child("location_name").getValue() != null) {

                        if (dataSnapshot.child("location_name").getValue().equals(loc_name) && dataSnapshot.child("user_id").getValue().equals(mAuth.getUid())) {

                            LogUtils.e(dataSnapshot.getKey());

                            if (Integer.parseInt(dataSnapshot.child("product_quantity").getValue().toString()) <= 0){

                                dataSnapshot.getRef().setValue(null);
                            }

                            ProductLocation productLocation = dataSnapshot.getValue(ProductLocation.class);
                            productLocation.getProduct_id();
                            productLocation.getProduct_quantity();
                            list.add(productLocation);
                        }
                    }
//
//                    ProductLocation productLocation = dataSnapshot.getValue(ProductLocation.class);
//                    dataSnapshot.getChildren();

                }

                if(list.size() == 0){
                        empty_storage.setVisibility(View.VISIBLE);
                }else{
                    empty_storage.setVisibility(View.GONE);
                }

                adapter.setList(list);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
