package com.stockone.lite.view.pages;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.util.DbUtils;
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
import com.stockone.lite.R;
import com.stockone.lite.model.TrialTimeModel;
import com.stockone.lite.model.User;
import com.stockone.lite.utils.ActivityManager;
import com.stockone.lite.utils.Constants;
import com.stockone.lite.utils.General;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    TextView userName;
    TextView nav_company_name;
    TextView nav_company_email;
    CircleImageView nav_company_logo;
    FirebaseAuth mAuth;
    long todaysTime = System.currentTimeMillis();
    BillingProcessor bp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        
        mAuth = FirebaseAuth.getInstance();

        onPermissionAsked();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        View headerview = navigationView.getHeaderView(0);
        userName = (TextView) headerview.findViewById(R.id.text_user_number);
        nav_company_name = (TextView) headerview.findViewById(R.id.nav_company_name);
        nav_company_email = (TextView) headerview.findViewById(R.id.nav_company_email);
        nav_company_logo = (CircleImageView) headerview.findViewById(R.id.nav_company_logo);


        if (General.getUserNumber() != null) {
            userName.setText(General.getUserNumber());
        }


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


        getUserDetails();

    }


    private void getUserDetails(){


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.USER_DETAILS);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("user_id").getValue() != null) {

                        if (dataSnapshot.child("user_id").getValue().equals(mAuth.getUid())) {

                            User user =  dataSnapshot.getValue(User.class);

                            if (user != null) {
                                nav_company_name.setText(user.getCompany_name());
                                nav_company_email.setText(user.getEmail_address());
                                Glide.with(Home.this).load(user.getCompany_logo()).into(nav_company_logo);
                            }

                            if (nav_company_name.getText().toString().equals(" ")){
                                nav_company_name.setVisibility(View.GONE);
                            }else {
                                nav_company_name.setVisibility(View.VISIBLE);
                            }

                            if (nav_company_email.getText().toString().equals(" ")){
                                nav_company_email.setVisibility(View.GONE);
                            }else {
                                nav_company_email.setVisibility(View.VISIBLE);
                            }

                        }
                    }
//
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTrialTime();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_in) {

            ActivityManager.IN(this);

        } else if (id == R.id.nav_out) {

            ActivityManager.OUT(this);

        } else if (id == R.id.nav_move) {

            ActivityManager.MOVE(this);

        } else if (id == R.id.nav_information) {

            ActivityManager.INFORMATION(this);

        } else if (id == R.id.nav_inventory) {

            ActivityManager.INVENTORY(this);

        } else if (id == R.id.nav_location) {

            ActivityManager.LOCATIONS(this);
        }
        else if (id == R.id.nav_products) {

            ActivityManager.PRODUCTS(this);

        }
        else if (id == R.id.nav_settings) {

            ActivityManager.SETTINGS(this);

        } else if (id == R.id.nav_transaction){

            ActivityManager.TRANSCATIONS(this);

        } else if(id == R.id.nav_count){

            ActivityManager.COUNT(this);

        } else if (id == R.id.nav_subscription){

            ActivityManager.SUBSCRIPTION(this);

        } else if (id == R.id.nav_rate_us){

            General.rateApp(this);
        }else if (id == R.id.nav_logout) {

            FirebaseAuth.getInstance().signOut();
            ActivityManager.LOGIN(this);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @OnClick(R.id.card_view_in)
    void onInClicked(){

        ActivityManager.IN(this);

    }

    @OnClick(R.id.card_view_inventory)
    void onInventoryClicked(){

        ActivityManager.INVENTORY(this);
    }

    @OnClick(R.id.card_view_location)
    void onLocationCliked(){

        ActivityManager.LOCATIONS(this);
    }

    @OnClick(R.id.card_view_out)
    void onOutCliked(){

        ActivityManager.OUT(this);
    }

    @OnClick(R.id.card_view_move)
    void onMoveClicked(){

        ActivityManager.MOVE(this);
    }

    @OnClick(R.id.card_view_info)
    void onInformationCliked(){

        ActivityManager.INFORMATION(this);
    }

    @OnClick(R.id.img_subscription)
    void onSubscriptionClicked(){

        ActivityManager.SUBSCRIPTION(this);
    }

    private void onPermissionAsked(){

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                        }else {
                        }
                        if(report.isAnyPermissionPermanentlyDenied()){
                            ActivityManager.PERMISSION_TAB(Home.this);
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
//                                ActivityManager.PERMISSION_TAB(PermissionActivity.this);
                    }
                }).check();
    }

    private void getTrialTime(){


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.USER_TIME);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.child("user_id").getValue() != null) {

                        if (dataSnapshot.child("user_id").getValue().equals(mAuth.getUid())) {

                            TrialTimeModel model = dataSnapshot.getValue(TrialTimeModel.class);
                            dataSnapshot.getChildren();

                            boolean log = model.getTime() <= todaysTime;

                            LogUtils.e(log);
                            LogUtils.e("MONTHLY -> "+bp.isSubscribed(Constants.SKU_MONTHLY));
                            LogUtils.e("YEARLY -> "+bp.isSubscribed(Constants.SKU_YEARLY));

                            if (log){

                                if (model.getPurchase_token().equals("0")){

                                    ToastUtils.showLong("Your trial period is over");
                                }

                            }

                            if (model.getExpire_time() <= todaysTime){
                                model.setFirstLogin(true);
                                model.setUser_id(mAuth.getUid());
                                model.setMob_no(mAuth.getCurrentUser().getPhoneNumber());
                                model.setTime(model.getTime());
                                model.setPurchase_token("0");
                                model.setPurchase_time(model.getPurchase_time());
                                model.setExpire_time(0);
                                // Getting the ID from firebase database.
                                String IDFromServer = dataSnapshot.getKey();

                                if (IDFromServer != null) {
                                    databaseReference.child(IDFromServer).setValue(model);
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
