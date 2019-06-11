package com.stockone.lite.view.pages;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseData;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.anjlab.android.iab.v3.PurchaseState;
import com.anjlab.android.iab.v3.SkuDetails;
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
import com.stockone.lite.model.SubscriptionModel;
import com.stockone.lite.model.TrialTimeModel;
import com.stockone.lite.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import spencerstudios.com.bungeelib.Bungee;

public class Subscription extends AppCompatActivity {

    @BindView(R.id.toolbar_subscription) Toolbar toolbar;
    @BindView(R.id.upper_ll) LinearLayout subscription_text_parent;
    @BindView(R.id.monthly_rl) RelativeLayout monthly_rl;
    @BindView(R.id.yearly_rl) RelativeLayout yearly_rl;
    @BindView(R.id.trial_date) TextView trial_date;
    @BindView(R.id.subscription_status) TextView subscription_status;

    FirebaseAuth mAuth;

    BillingProcessor bp;

    private boolean readyToPurchase = false;

    long toadysTime = System.currentTimeMillis();

    boolean isSet;
    String purchaseToken = "0";
    boolean onPurchase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        getTrialTime();

        if(!BillingProcessor.isIabServiceAvailable(this)) {
            ToastUtils.showLong("In-app billing service is unavailable, please upgrade Android Market/Play to version >= 3.9.16");
        }


        bp = new BillingProcessor(this, Constants.base64EncodedPublicKey, Constants.MERCHANT_ID, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {

                ToastUtils.showLong("Successfully purchased: " + productId);
                onPurchase = true;
                transactionDetails(Constants.SKU_MONTHLY);
                transactionDetails(Constants.SKU_YEARLY);
                if (details != null) {
                    purchaseToken = details.purchaseInfo.purchaseData.purchaseToken;
                    getTrialTime();
                }

            }
            @Override
            public void onBillingError(int errorCode, @Nullable Throwable error) {
                ToastUtils.showLong("onBillingError: " + Integer.toString(errorCode));
            }
            @Override
            public void onBillingInitialized() {
//                ToastUtils.showLong("onBillingInitialized");
                readyToPurchase = true;
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

        transactionDetails(Constants.SKU_MONTHLY);
        transactionDetails(Constants.SKU_YEARLY);

        LogUtils.e("Monthly Purchase->"+bp.isPurchased(Constants.SKU_MONTHLY));
        LogUtils.e("Yearly Purchase->"+bp.isPurchased(Constants.SKU_YEARLY));

        LogUtils.e(bp.isSubscribed(Constants.SKU_YEARLY));
        LogUtils.e(bp.isSubscribed(Constants.SKU_MONTHLY));
    }

    private void transactionDetails(String type){

        TransactionDetails transactionDetails = bp.getSubscriptionTransactionDetails(type);

        if (transactionDetails != null) {
            LogUtils.e("initializePaymentSetup: " + transactionDetails.toString());
            LogUtils.e("initializePaymentSetup: " + transactionDetails.purchaseInfo.toString());

            LogUtils.e("Purchase Time ->"+transactionDetails.purchaseInfo.purchaseData.purchaseTime.getTime());
            LogUtils.e("Order Id ->"+transactionDetails.purchaseInfo.purchaseData.orderId);
            LogUtils.e("PayLoad ->"+transactionDetails.purchaseInfo.purchaseData.developerPayload);
            LogUtils.e("Package Name ->"+transactionDetails.purchaseInfo.purchaseData.packageName);
            LogUtils.e("Purchase State ->"+transactionDetails.purchaseInfo.purchaseData.purchaseState);
            LogUtils.e("Auto Renewing ->"+transactionDetails.purchaseInfo.purchaseData.autoRenewing);
            LogUtils.e("Product Id ->"+transactionDetails.purchaseInfo.purchaseData.productId);
            LogUtils.e("Purchase Token ->"+transactionDetails.purchaseInfo.purchaseData.purchaseToken);
            LogUtils.e("Contents ->"+transactionDetails.purchaseInfo.purchaseData.describeContents());

            Date date = new Date(transactionDetails.purchaseInfo.purchaseData.purchaseTime.getTime());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd MMMM,yy");
            String getDate = format.format(date);

            subscription_status.setVisibility(View.VISIBLE);
            String product_id = transactionDetails.purchaseInfo.purchaseData.productId;
            if (product_id.equals(Constants.SKU_YEARLY)) {
                subscription_status.setText("You have successfully purchased Yearly Subscription on "+getDate);
            }else {
                subscription_status.setText("You have successfully purchased Monthly Subscription on "+getDate);
            }
        }else {

            subscription_status.setVisibility(View.GONE);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            Bungee.slideRight(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.monthly_card)
    void onMonthlyButtonCliked() {

        if (!readyToPurchase) {
            ToastUtils.showLong("Billing not initialized.");
            return;
        }

        bp.purchase(this, Constants.SKU_MONTHLY);
        bp.subscribe(this, Constants.SKU_MONTHLY);
//        bp.subscribe(this, "YOUR SUBSCRIPTION ID FROM GOOGLE PLAY CONSOLE HERE");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick(R.id.yearly_card)
    void onYearlyButtonCliked(){

        if (!readyToPurchase) {
            ToastUtils.showLong("Billing not initialized.");
            return;
        }

        bp.purchase(this, Constants.SKU_YEARLY);
        bp.subscribe(this, Constants.SKU_YEARLY);
    }

    private void getTrialTime(){

        isSet = true;
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.USER_TIME);
        databaseReference.keepSynced(true);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.child("user_id").getValue() != null) {

                        if (dataSnapshot.child("user_id").getValue().equals(mAuth.getUid()) && isSet) {

                            TrialTimeModel model = dataSnapshot.getValue(TrialTimeModel.class);
                            dataSnapshot.getChildren();

                            Date date = new Date(model.getTime());
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd MMMM,yy");
                            String getDate = format.format(date);
                            trial_date.setText("Trial due date: "+getDate);

                            Date date1 = new Date(toadysTime);
                            String getTodayDate = format.format(date1);

                            if (model.getTime() <= toadysTime){

                                subscription_text_parent.setVisibility(View.INVISIBLE);
                            }

                            if (onPurchase) {

                                model.setFirstLogin(true);
                                model.setUser_id(mAuth.getUid());
                                model.setMob_no(mAuth.getCurrentUser().getPhoneNumber());
                                model.setTime(model.getTime());
                                model.setPurchase_token(purchaseToken);
                                model.setPurchase_time(toadysTime);
                                if (bp.isSubscribed(Constants.SKU_MONTHLY)){
                                    Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                                    c.add(Calendar.MONTH, +1);
                                    long result = c.getTimeInMillis();
                                    model.setExpire_time(result);

                                }
                                if (bp.isSubscribed(Constants.SKU_YEARLY)){
                                    Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                                    c.add(Calendar.YEAR, +1);
                                    long result = c.getTimeInMillis();
                                    model.setExpire_time(result);

                                }
                                // Getting the ID from firebase database.
                                String IDFromServer = dataSnapshot.getKey();

                                if (IDFromServer != null && isSet) {
                                    databaseReference.child(IDFromServer).setValue(model);
                                    isSet = false;
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

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }
}
