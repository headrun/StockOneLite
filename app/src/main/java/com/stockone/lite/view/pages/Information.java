package com.stockone.lite.view.pages;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stockone.lite.R;
import com.stockone.lite.model.ProductLocation;
import com.stockone.lite.model.Products;
import com.stockone.lite.model.TransactionModel;
import com.stockone.lite.model.TrialTimeModel;
import com.stockone.lite.utils.ActivityManager;
import com.stockone.lite.utils.Constants;
import com.stockone.lite.utils.General;
import com.stockone.lite.view.adapter.ProductInventoryAdapter;
import com.stockone.lite.view.adapter.TransactionAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import spencerstudios.com.bungeelib.Bungee;

public class Information extends AppCompatActivity {

    @BindView(R.id.toolbar_information) Toolbar toolbar;
    @BindView(R.id.info_sku_id) EditText info_sku_id;
    @BindView(R.id.info_total_quantity) TextView info_total_quantity;
    @BindView(R.id.total_quant_ll) LinearLayout total_quant_ll;
    @BindView(R.id.location_text) TextView location_text;
    @BindView(R.id.transaction_text) TextView transaction_text;
    @BindView(R.id.rv_info) RecyclerView recyclerView;
    @BindView(R.id.rv_info_transaction) RecyclerView rv_info_transaction;

    private ArrayList<String> productList = new ArrayList<>();
    private SpinnerDialog productDialog;
    private String sku;
    private ProductInventoryAdapter adapter;
    private List<ProductLocation> list = new ArrayList<>();
    private List<TransactionModel> transaction_list = new ArrayList<>();
    private TransactionAdapter transactionAdapter;
    FirebaseAuth mAuth;
    BillingProcessor bp;
    long time = System.currentTimeMillis();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bp = new BillingProcessor(this, Constants.base64EncodedPublicKey, Constants.MERCHANT_ID, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {

                ToastUtils.showLong("Successfully purchased: " + productId);
            }
            @Override
            public void onBillingError(int errorCode, @Nullable Throwable error) {
                ToastUtils.showLong("onBillingError: " + Integer.toString(errorCode));
            }
            @Override
            public void onBillingInitialized() {
//                ToastUtils.showLong("onBillingInitialized");
            }
            @Override
            public void onPurchaseHistoryRestored() {
                LogUtils.e("onPurchaseHistoryRestored");
                for(String sku : bp.listOwnedProducts())
                    LogUtils.e("Owned Managed Product: " + sku);
                for(String sku : bp.listOwnedSubscriptions())
                    LogUtils.e("Owned Subscription: " + sku);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        productDialog = new SpinnerDialog(this, productList, "Select or Search SKUs", R.style.DialogAnimations_SmileWindow);

        info_sku_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productDialog.showSpinerDialog();
                if (productList.size() == 0){

                    General.alertDialog(Information.this, null, "No Inventory Added Yet");
                }
            }
        });

        productDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {

                info_sku_id.setText(s);
                sku = s;

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ProductInventoryAdapter(this);
        recyclerView.setAdapter(adapter);

        recyclerView.setNestedScrollingEnabled(false);
        rv_info_transaction.setNestedScrollingEnabled(false);

        getInventoryItems();


    }

    @OnClick(R.id.btn_get_info)
    void onInfoBtnClicked(){

        String sku_id = info_sku_id.getText().toString().trim();

        if (TextUtils.isEmpty(sku_id)) {
            Toast.makeText(getApplicationContext(), "Please enter SKU id", Toast.LENGTH_SHORT).show();
            return;
        }

        total_quant_ll.setVisibility(View.VISIBLE);
        getInventoryItems();
        getLocationList();
        getTransactions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTrialTime();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            Bungee.slideRight(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getInventoryItems(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Product_Path);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                productList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.child("uid").getValue() != null) {

                        if (dataSnapshot.child("uid").getValue().equals(mAuth.getUid())) {

                            Products products = dataSnapshot.getValue(Products.class);
                            dataSnapshot.getChildren();

                            productList.add(products.getSKUID());

                            if (dataSnapshot.child("skuid").getValue().equals(sku)) {

                                Products products1 = dataSnapshot.getValue(Products.class);

                                info_total_quantity.setText(" " + products1.getTotalAmount());

                            }

                        }
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getLocationList(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Product_Location_Path);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                list.clear(); //change here

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.child("product_id").getValue().equals(sku) && dataSnapshot.child("user_id").getValue().equals(mAuth.getUid())){

                        LogUtils.e("PRO_ID ->"+sku );
                        location_text.setVisibility(View.VISIBLE);
                        ProductLocation productLocation = dataSnapshot.getValue(ProductLocation.class);
                        productLocation.getLocation_name();
                        productLocation.getProduct_quantity();
                        list.add(productLocation);
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

    private void getTransactions() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Transaction_Path);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_info_transaction.setLayoutManager(linearLayoutManager);
        transactionAdapter = new TransactionAdapter(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rv_info_transaction.setAdapter(transactionAdapter);

        databaseReference.orderByChild("product_id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                transaction_list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.child("product_id").getValue().equals(sku) && dataSnapshot.child("user_id").getValue().equals(mAuth.getUid())) {

                        TransactionModel transactionModel = dataSnapshot.getValue(TransactionModel.class);
                        dataSnapshot.getChildren();
                        transaction_text.setVisibility(View.VISIBLE);

                        transaction_list.add(transactionModel);

                        LogUtils.e("Transaction List ->"+transaction_list.size());

                    }

                }
                transactionAdapter.setList(transaction_list);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
    }

    private void getTrialTime(){


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.USER_TIME);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.child("user_id").getValue() != null) {

                        if (dataSnapshot.child("user_id").getValue().equals(mAuth.getUid())) {

                            TrialTimeModel model = dataSnapshot.getValue(TrialTimeModel.class);
                            dataSnapshot.getChildren();

                            boolean log = model.getTime() <= time;

                            LogUtils.e(log);
                            LogUtils.e(!bp.isSubscribed(Constants.SKU_MONTHLY));
                            LogUtils.e(!bp.isSubscribed(Constants.SKU_YEARLY));

                            if (log){

                                if (model.getPurchase_token().equals("0")){

                                    ActivityManager.SUBSCRIPTION(Information.this);
                                    finish();
                                    ToastUtils.showLong("This is a basic subscription function please upgrade");
                                }

                            }

//                            if (log || !bp.isSubscribed(Constants.SKU_MONTHLY) && !bp.isSubscribed(Constants.SKU_YEARLY)){
//
//                                ActivityManager.SUBSCRIPTION(Home.this);
//                            }


                        }

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
