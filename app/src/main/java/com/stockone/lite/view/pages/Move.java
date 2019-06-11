package com.stockone.lite.view.pages;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nononsenseapps.filepicker.Utils;
import com.stockone.lite.R;
import com.stockone.lite.model.ProductLocation;
import com.stockone.lite.model.Products;
import com.stockone.lite.model.TransactionModel;
import com.stockone.lite.model.TrialTimeModel;
import com.stockone.lite.model.Zone;
import com.stockone.lite.utils.ActivityManager;
import com.stockone.lite.utils.Constants;
import com.stockone.lite.utils.General;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import spencerstudios.com.bungeelib.Bungee;

public class Move extends AppCompatActivity {

    @BindView(R.id.toolbar_move) Toolbar toolbar;
    @BindView(R.id.move_sku_id) EditText move_sku_id;
    @BindView(R.id.move_sku_quant) EditText move_sku_quant;
    @BindView(R.id.move_source_loc) EditText move_source_loc;
    @BindView(R.id.move_destin_loc) EditText move_destin_loc;

    private ArrayList<String> locationList = new ArrayList<>();
    private ArrayList<String> sourceList = new ArrayList<>();
    private ArrayList<String> productList = new ArrayList<>();
    private SpinnerDialog productDialog, locationDialog, loc_dialog;
    String sku;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    long time = System.currentTimeMillis();
    boolean isExistence;
    boolean isSet;
    ProductLocation productLocation = new ProductLocation();
    ProductLocation pro = new ProductLocation();
    String IDFromServer;
    private ArrayList<Integer> quantityList = new ArrayList<>();
    int selectPos = 0;
    BillingProcessor bp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);
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

        // Product Dialog
        productDialog = new SpinnerDialog(this, productList, "Select or Search SKUs", R.style.DialogAnimations_SmileWindow);

        move_sku_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productDialog.showSpinerDialog();
                if (productList.size() == 0){

                    General.alertDialog(Move.this, null, "No Inventory Added Yet");
                }
            }
        });

        productDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {

                move_sku_id.setText(s);
                sku = s;
                getSourceLocation(sku);

            }
        });

        // location Dialog
        locationDialog = new SpinnerDialog(this, locationList, "Select or Search location", R.style.DialogAnimations_SmileWindow);

        move_destin_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationDialog.showSpinerDialog();
            }
        });

        locationDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {

                move_destin_loc.setText(s);
                LogUtils.e(s);

            }
        });

        loc_dialog = new SpinnerDialog(this, sourceList, "Select or Search location", R.style.DialogAnimations_SmileWindow);

        move_source_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loc_dialog.showSpinerDialog();
                if (sourceList.size() == 0){

                    General.alertDialog(Move.this, null, "Please select SKU id first");
                }
            }
        });

        loc_dialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {

                move_source_loc.setText(s);
                selectPos = i;
                LogUtils.e(s);

            }
        });

        getInventoryItems();
        getLocationList();

        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Product_Location_Path);
        mAuth = FirebaseAuth.getInstance();

        transactionDetails(Constants.SKU_MONTHLY);
        transactionDetails(Constants.SKU_YEARLY);


    }

    private void transactionDetails(String type){

        TransactionDetails transactionDetails = bp.getSubscriptionTransactionDetails(type);

        if (transactionDetails != null) {
            LogUtils.e("initializePaymentSetup: " + transactionDetails.toString());
            LogUtils.e("initializePaymentSetup: " + transactionDetails.purchaseInfo.toString());

            LogUtils.e("Purchase Token ->"+transactionDetails.purchaseInfo.purchaseData.purchaseToken);

        }else {


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {

            if (resultCode == Activity.RESULT_OK && requestCode == 0){
                String result=data.getStringExtra("barcode");
                move_sku_id.setText(result);
                getSourceLocation(result);
            }
        }
    }

    @OnClick(R.id.barcode_move)
    void onBarcodeMove(){

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            Intent intent = new Intent(Move.this, ScanBarcodeActivity.class);
                            startActivityForResult(intent, 0);
                        }else {
                        }
                        if(report.isAnyPermissionPermanentlyDenied()){
                            ActivityManager.PERMISSION_TAB(Move.this);
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
//                                ActivityManager.PERMISSION_TAB(PermissionActivity.this);
                    }
                }).check();
    }

    @OnClick(R.id.btn_move_sku)
    void onBtnMove(){

        String quantity = move_sku_quant.getText().toString().trim();
        String source = move_source_loc.getText().toString().trim();
        String destination = move_destin_loc.getText().toString().trim();

        if (TextUtils.isEmpty(quantity)) {
            General.alertDialog(Move.this,null, "Please enter the quantity value");
            return;
        }

        if (source.equals(destination)){

            General.alertDialog(Move.this,null, "Source and destination location should not be same.");
            return;
        }
        try {
            if (Integer.parseInt(quantity) > quantityList.get(selectPos)){

                General.alertDialog(Move.this,null, "Please add correct quantity.");
                return;
            }
        }catch (NumberFormatException e){


        }

        productLocation.setCareated(time);
        productLocation.setLocation_name(move_destin_loc.getText().toString());
        productLocation.setProduct_id(sku);
        productLocation.setUser_id(mAuth.getUid());
        productLocation.setProduct_edt_qty("0");

        setDestinLoc(move_destin_loc.getText().toString(), sku);


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

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Zone_Path);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                locationList.clear();
                locationList.add("Default Storage Location");

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

    private void getSourceLocation(final String sku){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Product_Location_Path);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                sourceList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.child("product_id").getValue().equals(sku)){

                        ProductLocation productLocation1 = dataSnapshot.getValue(ProductLocation.class);
                        sourceList.add(productLocation1.getLocation_name());
                        quantityList.add(productLocation1.getProduct_quantity());

                    }
//
//                    ProductLocation productLocation = dataSnapshot.getValue(ProductLocation.class);
//                    dataSnapshot.getChildren();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setDestinLoc(final String name, final String product_id){

        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Product_Location_Path);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                isExistence = false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.child("location_name").getValue() != null) {

                        LogUtils.e(dataSnapshot.child("location_name").getValue()+ name);
                        LogUtils.e(dataSnapshot.child("product_id").getValue()+ product_id);
                        LogUtils.e(dataSnapshot.child("location_name").getValue().equals(name));
                        LogUtils.e(dataSnapshot.child("product_id").getValue().equals(product_id));

                        if (dataSnapshot.child("location_name").getValue().equals(name) && dataSnapshot.child("product_id").getValue().equals(product_id)) {

                            try {
                                int totalQuant = Integer.parseInt(move_sku_quant.getText().toString()) + Integer.parseInt(dataSnapshot.child("product_quantity").getValue().toString());
                                productLocation.setProduct_quantity(totalQuant);
                            } catch (NumberFormatException ex) {

                            }
                            // Getting the ID from firebase database.
                            String IDFromServer = dataSnapshot.getKey();
                            productLocation.setFirebase_key(IDFromServer);

                            if (IDFromServer != null) {
                                databaseReference.child(IDFromServer).setValue(productLocation);
                            }

                            ToastUtils.showLong("Moved");
                            isExistence = true;

                        }
                    }
//

                }

                LogUtils.e("Existence ->" +isExistence);

                if (!isExistence){

                    try{
                        int  totalQuant = Integer.parseInt(move_sku_quant.getText().toString());
                        LogUtils.e("Total - "+ totalQuant);
                        productLocation.setProduct_quantity(totalQuant);
                    }catch(NumberFormatException ex){

                    }
                    // Getting the ID from firebase database.
                    String IDFromServer = databaseReference.push().getKey();
                    productLocation.setFirebase_key(IDFromServer);

                    if (IDFromServer != null) {
                        databaseReference.child(IDFromServer).setValue(productLocation);
                    }

                    ToastUtils.showLong("Moved");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        deleteFromLocation(move_source_loc.getText().toString());
        setDestinTransaction();
        setSourceTransaction();
    }

    private void deleteFromLocation(final String name){

        pro.setLocation_name(move_source_loc.getText().toString());
        pro.setProduct_id(sku);

        pro.setUser_id(mAuth.getUid());

        pro.setCareated(time);

        isSet = true;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Product_Location_Path);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (isSet) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        if (dataSnapshot.child("location_name").getValue() != null) {

                            if (dataSnapshot.child("location_name").getValue().equals(name) && dataSnapshot.child("product_id").getValue().equals(sku)) {

                                ProductLocation productLocation1 = dataSnapshot.getValue(ProductLocation.class);

                                IDFromServer = dataSnapshot.getKey();

                                try {

                                    int totalQuant = 0;
                                    if (productLocation1 != null) {
                                        LogUtils.e(productLocation1.getProduct_quantity());
                                        totalQuant = productLocation1.getProduct_quantity() - Integer.parseInt(move_sku_quant.getText().toString());
                                    }
                                    LogUtils.e(totalQuant);
                                    pro.setProduct_quantity(totalQuant);

                                } catch (NumberFormatException ex) {

                                }
                            }
                        }

                    }
                }

                if (IDFromServer != null && isSet) {

                    databaseReference.child(IDFromServer).setValue(pro);
                    isSet = false;

                }

                ToastUtils.showLong("Moved");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getTrialTime();
    }

    private void setSourceTransaction(){

        DatabaseReference transactionReference = FirebaseDatabase.getInstance().getReference(Constants.Transaction_Path);

        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setProduct_id(sku);
        transactionModel.setProduct_name(sku);
        transactionModel.setUser_id(mAuth.getUid());
        transactionModel.setTransaction_time(time);
        transactionModel.setProduct_loc(move_destin_loc.getText().toString());
        String transcationQuant = "p+"+move_sku_quant.getText().toString();
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
        transactionModel.setProduct_id(sku);
        transactionModel.setProduct_name(sku);
        transactionModel.setUser_id(mAuth.getUid());
        transactionModel.setTransaction_time(time);
        transactionModel.setProduct_loc(move_source_loc.getText().toString());
        String transcationQuant = "m-"+move_sku_quant.getText().toString();
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

                                if (model.getPurchase_token().equals("0")){

                                    ActivityManager.SUBSCRIPTION(Move.this);
                                    finish();
                                    ToastUtils.showLong("This is a basic subscription function please upgrade");
                                }

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

}
