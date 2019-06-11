package com.stockone.lite.view.pages;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.stockone.lite.R;
import com.stockone.lite.model.ProductLocation;
import com.stockone.lite.model.User;
import com.stockone.lite.utils.ActivityManager;
import com.stockone.lite.utils.Constants;
import com.stockone.lite.utils.General;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import spencerstudios.com.bungeelib.Bungee;

public class Settings extends AppCompatActivity {

    @BindView(R.id.toolbar_settings) Toolbar toolbar;
    @BindView(R.id.company_logo) ImageView logo;
    @BindView(R.id.et_settings_name) EditText company_name;
    @BindView(R.id.et_settings_email) EditText company_email;
    @BindView(R.id.avi_settings) AVLoadingIndicatorView avi;
    public int FILE_SELECT_CODE = 4332;
    String companyName, companyEmail;
    Uri selectedImage;
    User model =  new User();

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    FirebaseAuth auth;
    boolean isPhotoClicked;
    boolean isExistence;
    boolean isImageExist;
    String companyLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.USER_DETAILS);

        auth = FirebaseAuth.getInstance();

        getUserDetails();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            Bungee.slideRight(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_SELECT_CODE && resultCode == Activity.RESULT_OK) {

                //Get image
            selectedImage = data.getData();
            LogUtils.e(selectedImage);
            logo.setImageURI(selectedImage);
            isPhotoClicked = true;

        }


    }


    @OnClick(R.id.btn_upload_logo)
    void onLogoUpload(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), FILE_SELECT_CODE);
    }

    @OnClick(R.id.btn_save_settings)
    void onSaveSettings(){

        companyName = company_name.getText().toString().trim();
        companyEmail = company_email.getText().toString().trim();

        if (TextUtils.isEmpty(companyName)) {
            General.alertDialog(Settings.this, null, "Please enter Company name");
            return;
        }

        if (TextUtils.isEmpty(companyEmail)) {
            General.alertDialog(Settings.this, null, "Please enter companyEmail");
            return;
        }

        if (isPhotoClicked) {
            UploadImageFileToFirebaseStorage();
        }else {

            updateUserDetails();
        }

    }

    private void updateUserDetails(){

        avi.show();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                isExistence = false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("user_id").getValue() != null) {

                        if (dataSnapshot.child("user_id").getValue().equals(auth.getUid())) {

                            if (dataSnapshot.child("company_name").getValue().equals(company_name.getText().toString()) && dataSnapshot.child("email_address").getValue().equals(company_email.getText().toString())){
                                ToastUtils.showLong("Please edit any field first");
                                return;
                            }

                            model.setCompany_logo(companyLogo);
                            model.setCompany_name(company_name.getText().toString());
                            model.setEmail_address(company_email.getText().toString());
                            model.setPhone_no(General.getUserNumber());
                            model.setUser_id(auth.getUid());
                            String IDFromServer = dataSnapshot.getKey();
                            // Adding image upload id s child element into databaseReference.
                            if (IDFromServer != null) {
                                databaseReference.child(IDFromServer).setValue(model);
                                ToastUtils.showLong("Successfully updated");
                                avi.hide();
                            }
                            isExistence = true;

                        }
                    }
//
                }

                if (!isExistence){

                    model.setCompany_logo(" ");
                    model.setCompany_name(company_name.getText().toString());
                    model.setEmail_address(company_email.getText().toString());
                    model.setPhone_no(General.getUserNumber());
                    model.setUser_id(auth.getUid());

                    String IDFromServer = databaseReference.push().getKey();
                    // Adding image upload id s child element into databaseReference.
                    if (IDFromServer != null) {
                        databaseReference.child(IDFromServer).setValue(model);
                        ToastUtils.showLong("Successfully saved");
                        avi.hide();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                avi.hide();
            }
        });

    }

    private void getUserDetails(){

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("user_id").getValue() != null) {

                        if (dataSnapshot.child("user_id").getValue().equals(auth.getUid())) {

                            User user =  dataSnapshot.getValue(User.class);

                            company_name.setText(user.getCompany_name());
                            company_email.setText(user.getEmail_address());

                            if (user.getCompany_logo().equals(" ")){
                                Glide.with(Settings.this).load(R.drawable.stocknew).into(logo);
                            }else {

                                Glide.with(Settings.this).load(user.getCompany_logo()).into(logo);
                                companyLogo = user.getCompany_logo();
                            }

                        }
                    }
//
                }

                avi.hide();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
//        if (selectedImage != null) {

            // Creating second StorageReference.

            avi.show();
            final StorageReference storageReference2nd = storageReference.child(Constants.Storage_Path + System.currentTimeMillis() + "." + "jpg");

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(selectedImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                            final User user = new User();

                            storageReference2nd.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final Uri downloadUrl = uri;

                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {
                                            isImageExist = false;
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                if (dataSnapshot.child("user_id").getValue() != null) {

                                                    if (dataSnapshot.child("user_id").getValue().equals(auth.getUid())) {

                                                        model.setCompany_logo(downloadUrl.toString());
                                                        model.setCompany_name(company_name.getText().toString());
                                                        model.setEmail_address(company_email.getText().toString());
                                                        model.setPhone_no(General.getUserNumber());
                                                        model.setUser_id(auth.getUid());
                                                        String IDFromServer = dataSnapshot.getKey();
                                                        // Adding image upload id s child element into databaseReference.
                                                        if (IDFromServer != null) {
                                                            databaseReference.child(IDFromServer).setValue(model);
                                                            ToastUtils.showLong("Successfully updated");
                                                            avi.hide();
                                                        }
                                                        isImageExist = true;

                                                    }
                                                }
//
                                            }

                                            if (!isImageExist){

                                                model.setCompany_logo(downloadUrl.toString());
                                                model.setCompany_name(company_name.getText().toString());
                                                model.setEmail_address(company_email.getText().toString());
                                                model.setPhone_no(General.getUserNumber());
                                                model.setUser_id(auth.getUid());

                                                String IDFromServer = databaseReference.push().getKey();
                                                // Adding image upload id s child element into databaseReference.
                                                if (IDFromServer != null) {
                                                    databaseReference.child(IDFromServer).setValue(model);
                                                    ToastUtils.showLong("Successfully saved");
                                                    avi.hide();
                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }
                            });
                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Showing exception erro message.
                            ToastUtils.showLong(exception.getMessage());
                            avi.hide();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            avi.hide();
                        }
                    });
    }
}
