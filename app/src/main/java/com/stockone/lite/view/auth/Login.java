package com.stockone.lite.view.auth;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.util.DbUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stockone.lite.R;
import com.stockone.lite.model.TransactionModel;
import com.stockone.lite.model.TrialTimeModel;
import com.stockone.lite.utils.ActivityManager;
import com.stockone.lite.utils.Constants;
import com.stockone.lite.utils.General;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class Login extends AppCompatActivity {

    @BindView(R.id.bg) ImageView background;
    @BindView(R.id.btn_sign_in) Button btnSignIn;
    @BindView(R.id.avi_login) AVLoadingIndicatorView avi;
    @BindView(R.id.otp_rl) RelativeLayout otp_parent;
    @BindView(R.id.phone_rl) RelativeLayout number_parent;
    @BindView(R.id.edt_phone_no) EditText phoneNo;
    @BindView(R.id.edt_otp) EditText otpNo;
    @BindView(R.id.edt_country_code) EditText countryCode;
    @BindView(R.id.avi_otp) AVLoadingIndicatorView avi_otp;

    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private GoogleApiClient mGoogleApiClient;
    private String mVerificationId;
    private FirebaseAuth mAuth;
    boolean isExistence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        avi.hide();
        avi_otp.hide();

        mAuth = FirebaseAuth.getInstance();
        //Get Firebase auth instance
//        mAuth = FirebaseAuth.getInstance();
//        if (mAuth.getCurrentUser() != null) {
//            ActivityManager.HOME(this);
//            finish();
//        }

        Glide.with(this).load(R.drawable.stockonelitebg)
                .apply(bitmapTransform(new BlurTransformation(25, 3)))
                .into(background);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);
                LogUtils.e("onVerificationCompleted:" + phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                LogUtils.e("onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {

                    LogUtils.e("Invalid Request");

                } else if (e instanceof FirebaseTooManyRequestsException) {

                    LogUtils.e("The SMS quota for the project has been exceeded");
                }

                // Show a message and update the UI
                // ...
            }
            @Override
            public void onCodeSent (String s, PhoneAuthProvider.ForceResendingToken token){
                super.onCodeSent(s, token);

                LogUtils.e( "onCodeSent:" + s);

                // Save verification ID and resending token so we can use them later
                mVerificationId = s;
                mResendToken = token;
                number_parent.setVisibility(View.GONE);
                otp_parent.setVisibility(View.VISIBLE);

            }
            @Override
            public void onCodeAutoRetrievalTimeOut (String s){
                super.onCodeAutoRetrievalTimeOut(s);
            }

        };

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.CREDENTIALS_API).
                        enableAutoManage(this,
                                new GoogleApiClient.OnConnectionFailedListener() {
                                    @Override
                                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                                        LogUtils.e("Client connection failed: " + connectionResult.getErrorMessage());
                                    }
                                }).build();

    }

    @OnClick(R.id.btn_confirm)
    void onConfirmOTP(){

        confirmOTP();

    }

    @OnClick(R.id.btn_sign_in)
    void onSignClicked(){

        if (General.validatePhoneNumber(this, phoneNo)){
            btnSignIn.setVisibility(View.GONE);
            avi.show();
            confirmLogin();
            General.hideKeyboard(this, phoneNo);
        }
        else {
            btnSignIn.setVisibility(View.VISIBLE);
            avi.hide();
        }

    }

    private void confirmOTP(){

        String code = otpNo.getText().toString();
        if (TextUtils.isEmpty(code)) {
            General.alertDialog(this, null, "Field cannot be empty");
            avi_otp.hide();
            return;
        }
        avi_otp.show();
        verifyPhoneNumberWithCode(mVerificationId, code);
    }

    private void confirmLogin(){

        String phoneNumber = countryCode.getText().toString()+phoneNo.getText().toString();

        avi.show();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);

    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
        avi_otp.hide();
        ActivityManager.HOME(this);
        finish();
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks,
                token);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            LogUtils.e( "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            LogUtils.e(user.getPhoneNumber());
                            General.storeUserNumber(user.getPhoneNumber());
                            ToastUtils.showLong("Successfully signed In");
                            getTrialTime();
                        } else {
                            LogUtils.e( "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                ToastUtils.showLong("Invalid code.");
                            }
                            ToastUtils.showLong("sign in failed");
                        }

                    }

                });
    }

    @OnClick(R.id.txt_resend_otp)
    void onResendOTP(){

        String phoneNumber = countryCode.getText().toString()+phoneNo.getText().toString();
        resendVerificationCode(phoneNumber, mResendToken);
    }

    private void setFirstUser(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.USER_TIME);

        TrialTimeModel trialTimeModel = new TrialTimeModel();
        trialTimeModel.setUser_id(mAuth.getUid());
        trialTimeModel.setFirstLogin(true);
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.add(Calendar.DATE, +14);
        long result = c.getTimeInMillis();
        trialTimeModel.setTime(result);
        trialTimeModel.setPurchase_token("0");
        trialTimeModel.setMob_no(General.getUserNumber());


        String IDFromServer = databaseReference.push().getKey();

        if (IDFromServer != null) {
            databaseReference.child(IDFromServer).setValue(trialTimeModel);

            ActivityManager.HOME(Login.this);
            finish();
        }
    }

    private void getTrialTime(){

        isExistence = false;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.USER_TIME);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.child("user_id").getValue() != null) {

                        if (dataSnapshot.child("user_id").getValue().equals(mAuth.getUid())) {

                            TrialTimeModel model = dataSnapshot.getValue(TrialTimeModel.class);
                            dataSnapshot.getChildren();

                            if (model.isFirstLogin()){

                                ActivityManager.HOME(Login.this);
                                finish();
                            }else {

                            }

                            isExistence = true;
                        }

                    }

                }

                if (!isExistence){

                    setFirstUser();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
