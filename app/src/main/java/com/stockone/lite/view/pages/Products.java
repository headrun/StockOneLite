package com.stockone.lite.view.pages;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stockone.lite.R;
import com.stockone.lite.utils.Constants;
import com.stockone.lite.utils.General;
import com.stockone.lite.view.adapter.InventoryAdapter;
import com.stockone.lite.view.adapter.ProductsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import spencerstudios.com.bungeelib.Bungee;

public class Products extends AppCompatActivity {

    @BindView(R.id.toolbar_products) Toolbar toolbar;
    @BindView(R.id.pro_sku_id) EditText pro_sku_id;
    @BindView(R.id.text_products) TextView text_products;
    @BindView(R.id.rv_products) RecyclerView recyclerView;

    private ProductsAdapter adapter;
    private List<com.stockone.lite.model.Products> list = new ArrayList<>();
    private ArrayList<String> productList = new ArrayList<>();
    private SpinnerDialog productDialog;
    private String sku;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ProductsAdapter(this);
        recyclerView.setAdapter(adapter);

        productDialog = new SpinnerDialog(this, productList, "Select or Search SKUs", R.style.DialogAnimations_SmileWindow);

        pro_sku_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productDialog.showSpinerDialog();
                if (productList.size() == 0){

                    General.alertDialog(Products.this, null, "No Inventory Added Yet");
                }
            }
        });

        productDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {

                pro_sku_id.setText(s);
                sku = s;

            }
        });

        getInventoryItems();


    }

    private void getInventoryItems(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Product_Path);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.child("uid").getValue() != null){

                        if (dataSnapshot.child("uid").getValue().equals(mAuth.getUid())){

                            com.stockone.lite.model.Products products = dataSnapshot.getValue(com.stockone.lite.model.Products.class);
                            dataSnapshot.getChildren();
                            productList.add(products.getSKUID());
                        }

                    }

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getProducts(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Product_Path);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    if (dataSnapshot.child("skuid").getValue() != null) {

                        if (dataSnapshot.child("skuid").getValue().equals(sku)) {

                            com.stockone.lite.model.Products products = dataSnapshot.getValue(com.stockone.lite.model.Products.class);
                            LogUtils.e(dataSnapshot.child("skuid").getValue());
                            Constants.KEY = dataSnapshot.getKey();

                            list.add(products);

                        }
                    }

                }

                adapter.setList(list);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            Bungee.slideRight(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_get_products)
    void onClickProducts(){

        text_products.setVisibility(View.VISIBLE);
        getProducts();

    }
}
