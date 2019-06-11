package com.stockone.lite.view.pages;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.stockone.lite.model.Zone;
import com.stockone.lite.utils.ActivityManager;
import com.stockone.lite.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import spencerstudios.com.bungeelib.Bungee;

public class Count extends AppCompatActivity {

    @BindView(R.id.toolbar_count) Toolbar toolbar;
    @BindView(R.id.count_location_name) EditText count_location_name;
    @BindView(R.id.count_product_name) EditText count_product_name;
    @BindView(R.id.count_sku_quant) EditText count_sku_quant;
    ArrayList<String> locationList = new ArrayList<>();
    ArrayList<String> productList = new ArrayList<>();
    SpinnerDialog locationDialog, productDialog;
    private FirebaseAuth mAuth;
    String locationName, productName;
    String loc, name,quant;
    String firebaseId;
    long time = System.currentTimeMillis();
    int actualProductQuant;
    int updateInventoryCount;
    boolean isDecreased, isIncreased, isSet, isQuant;
    String edited;
    BillingProcessor bp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

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

        getLocationList();

        // location Dialog
        locationDialog = new SpinnerDialog(this, locationList, "Select or Search location", R.style.DialogAnimations_SmileWindow);

        count_location_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationDialog.showSpinerDialog();
            }
        });

        locationDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {

                count_location_name.setText(s);
                LogUtils.e(s);
                locationName = s;
                count_product_name.setText("");
                count_sku_quant.setText("");
                getStorageInventory();
            }
        });

        // Product Dialog
        productDialog = new SpinnerDialog(this, productList, "Select or Search products", R.style.DialogAnimations_SmileWindow);

        count_product_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getTextValidation();

                if (TextUtils.isEmpty(loc)) {
                    Toast.makeText(getApplicationContext(), "Please select location", Toast.LENGTH_SHORT).show();
                    return;
                }

                productDialog.showSpinerDialog();
            }
        });

        productDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {

                count_product_name.setText(s);
                productName = s;
                getStorageInventory();
                isQuant = true;
            }
        });

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

    private void getStorageInventory(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Product_Location_Path);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                productList.clear(); //change here

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.child("location_name").getValue() != null) {

                        if (dataSnapshot.child("location_name").getValue().equals(locationName) && dataSnapshot.child("user_id").getValue().equals(mAuth.getUid())) {

                            LogUtils.e(dataSnapshot.getKey());
                            ProductLocation productLocation = dataSnapshot.getValue(ProductLocation.class);

                            productList.add(productLocation.getProduct_id());

                            if (dataSnapshot.child("product_id").getValue().equals(productName)) {

                                if (isQuant) {
                                    count_sku_quant.setText("" + productLocation.getProduct_quantity());
                                    isQuant = false;
                                }
                                actualProductQuant = productLocation.getProduct_quantity();
                                firebaseId = productLocation.getFirebase_key();
                            }

                            edited = productLocation.getProduct_edt_qty();

                        }

                    }

                }

                getInventoryItems(productName);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void defaultLocationList(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.DEFAULT_ZONE);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Zone zone = dataSnapshot.getValue(Zone.class);
                    dataSnapshot.getChildren();

                    locationList.add(zone.getLocation());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getTextValidation(){

        loc = count_location_name.getText().toString().trim();
        name = count_product_name.getText().toString().trim();
        quant = count_sku_quant.getText().toString().trim();

    }

    private void getLocationList(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Zone_Path);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                defaultLocationList();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.child("userid").getValue() != null) {

                        if (dataSnapshot.child("userid").getValue().equals(mAuth.getUid())) {

                            Zone zone = dataSnapshot.getValue(Zone.class);
                            dataSnapshot.getChildren();

                            locationList.add(zone.getLocation());
                        }
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.btn_change_count)
    void onChangeCountClicked(){

        getTextValidation();

        if (TextUtils.isEmpty(loc)) {
            Toast.makeText(getApplicationContext(), "Please select location", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Please select product", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(quant)) {
            Toast.makeText(getApplicationContext(), "Please select amount", Toast.LENGTH_SHORT).show();
            return;
        }

        changeActualQuantity();

    }

    private void changeActualQuantity(){

        DatabaseReference locationReference = FirebaseDatabase.getInstance().getReference(Constants.Product_Location_Path);

        ProductLocation productLocation = new ProductLocation();
        try {
            productLocation.setProduct_quantity(Integer.parseInt(count_sku_quant.getText().toString().trim()));

        }catch (NumberFormatException e){

        }
        productLocation.setCareated(time);
        productLocation.setFirebase_key(firebaseId);
        productLocation.setProduct_id(count_product_name.getText().toString().trim());
        productLocation.setLocation_name(count_location_name.getText().toString().trim());
        productLocation.setUser_id(mAuth.getUid());

        try {

            if (actualProductQuant == Integer.parseInt(count_sku_quant.getText().toString())){

                updateInventoryCount = productLocation.getProduct_quantity();

            } else if (actualProductQuant > Integer.parseInt(count_sku_quant.getText().toString())){

                try {
                    updateInventoryCount = actualProductQuant - Integer.parseInt(count_sku_quant.getText().toString());
                }catch (NumberFormatException e){

                }

                isDecreased = true;
                setDestinTransaction();

            } else if (actualProductQuant < Integer.parseInt(count_sku_quant.getText().toString())){

                try {
                    updateInventoryCount = Integer.parseInt(count_sku_quant.getText().toString()) - actualProductQuant;
                }catch (NumberFormatException e){

                }

                isIncreased = true;

                setSourceTransaction();
            }

            productLocation.setProduct_edt_qty(String.valueOf(updateInventoryCount));

        }catch (NumberFormatException e){

        }

        if (firebaseId != null) {
            locationReference.child(firebaseId).setValue(productLocation);
            getStorageInventory();
            ToastUtils.showLong("Quantity Changed");
            count_location_name.setText("");
            count_product_name.setText("");
            count_sku_quant.setText("");
        }

    }

    private void getInventoryItems(final String name){

        isSet = true;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Product_Path);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    if (dataSnapshot.child("skuid").getValue() != null) {

                        if (dataSnapshot.child("skuid").getValue().equals(name) && isSet) {

                            com.stockone.lite.model.Products pro = dataSnapshot.getValue(Products.class);

                            if (isDecreased) {
                                try {
                                    int totalQuant = pro.getTotalAmount() - Integer.parseInt(edited);
                                    pro.setTotalAmount(totalQuant);
                                } catch (NumberFormatException ex) {

                                }
                                isDecreased = false;
                            }

                            if (isIncreased) {
                                try {
                                    int totalQuant = pro.getTotalAmount() + Integer.parseInt(edited);
                                    pro.setTotalAmount(totalQuant);
                                } catch (NumberFormatException ex) {

                                }
                                isIncreased = false;
                            }

                            // Getting the ID from firebase database.
                            String IDFromServer = dataSnapshot.getKey();

                            if (IDFromServer != null) {
                                databaseReference.child(IDFromServer).setValue(pro);
                                isSet = false;
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

    private void setSourceTransaction(){

        DatabaseReference transactionReference = FirebaseDatabase.getInstance().getReference(Constants.Transaction_Path);

        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setProduct_id(productName);
        transactionModel.setProduct_name(productName);
        transactionModel.setUser_id(mAuth.getUid());
        transactionModel.setTransaction_time(time);
        transactionModel.setProduct_loc(count_location_name.getText().toString());
        String transcationQuant = "c+"+updateInventoryCount;
        transactionModel.setProduct_quant(transcationQuant);
        LogUtils.e(transcationQuant);


        String IDFromServer = transactionReference.push().getKey();

        if (IDFromServer != null) {
            transactionReference.child(IDFromServer).setValue(transactionModel);
        }

    }

    private void setDestinTransaction(){

        DatabaseReference transactionReference = FirebaseDatabase.getInstance().getReference(Constants.Transaction_Path);

        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setProduct_id(productName);
        transactionModel.setProduct_name(productName);
        transactionModel.setUser_id(mAuth.getUid());
        transactionModel.setTransaction_time(time);
        transactionModel.setProduct_loc(count_location_name.getText().toString());
        String transcationQuant = "c-"+updateInventoryCount;
        transactionModel.setProduct_quant(transcationQuant);
        LogUtils.e(transcationQuant);


        String IDFromServer = transactionReference.push().getKey();

        if (IDFromServer != null) {
            transactionReference.child(IDFromServer).setValue(transactionModel);
        }
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

                                LogUtils.e("Purchase Token->"+model.getPurchase_token());

                                if (model.getPurchase_token().equals("0")){

                                    ActivityManager.SUBSCRIPTION(Count.this);
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
