package com.stockone.lite.view.pages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.blankj.utilcode.util.LogUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stockone.lite.R;
import com.stockone.lite.model.ProductLocation;
import com.stockone.lite.utils.Constants;
import com.stockone.lite.view.adapter.ProductInventoryAdapter;
import com.stockone.lite.view.adapter.StorageInventoryAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import spencerstudios.com.bungeelib.Bungee;

public class ProductInventory extends AppCompatActivity {

    @BindView(R.id.toolbar_product_inv) Toolbar toolbar;
    @BindView(R.id.rv_product_inventory) RecyclerView recyclerView;
    private ProductInventoryAdapter adapter;

    private List<ProductLocation> list = new ArrayList<>();

    String pro_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_inventory);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getIntent
        Intent extras = getIntent();
        Bundle bundle = extras.getExtras();
        assert bundle != null;
        pro_id = bundle.getString("pro_id");

        LogUtils.e("PRO_ID ->"+pro_id);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ProductInventoryAdapter(this);
        recyclerView.setAdapter(adapter);

        getProductInventory();


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            Bungee.slideRight(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getProductInventory(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Product_Location_Path);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                list.clear(); //change here

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.child("product_id").getValue() != null) {

                        if (dataSnapshot.child("product_id").getValue().equals(pro_id)) {

                            LogUtils.e("PRO_ID ->" + pro_id);

                            if (Integer.parseInt(dataSnapshot.child("product_quantity").getValue().toString()) <= 0){

                                dataSnapshot.getRef().setValue(null);
                            }

                            LogUtils.e(dataSnapshot.getKey());

                            ProductLocation productLocation = dataSnapshot.getValue(ProductLocation.class);
                            productLocation.getLocation_name();
                            productLocation.getProduct_quantity();

                            list.add(productLocation);
                        }
                    }
//
//                    ProductLocation productLocation = dataSnapshot.getValue(ProductLocation.class);
//                    dataSnapshot.getChildren();

                }
                adapter.setList(list);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
