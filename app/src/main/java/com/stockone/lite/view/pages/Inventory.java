package com.stockone.lite.view.pages;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
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
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.stockone.lite.R;
import com.stockone.lite.model.Products;
import com.stockone.lite.utils.ActivityManager;
import com.stockone.lite.utils.Constants;
import com.stockone.lite.utils.General;
import com.stockone.lite.view.adapter.InventoryAdapter;
import com.wang.avi.AVLoadingIndicatorView;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import spencerstudios.com.bungeelib.Bungee;

public class Inventory extends AppCompatActivity {

    @BindView(R.id.toolbar_inventory) Toolbar toolbar;
    @BindView(R.id.rv_inventory) RecyclerView recyclerView;
    @BindView(R.id.txt_inventory)
    TextView txt_inventory;
    @BindView(R.id.lottie_inv)
    LottieAnimationView lottie;
    @BindView(R.id.empty_storage)
    RelativeLayout empty_storage;
    @BindView(R.id.avi_inventory)
    AVLoadingIndicatorView avi;

    private InventoryAdapter adapter;
    private List<Products> list = new ArrayList<>();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        lottie.setAnimation("search_file.json");
        lottie.loop(true);
        lottie.playAnimation();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new InventoryAdapter(this);
        recyclerView.setAdapter(adapter);

        getInventoryItems();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

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

                list.clear(); //change here

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.child("uid").getValue() != null) {

                        if (dataSnapshot.child("uid").getValue().equals(mAuth.getUid())) {

                            Products products = dataSnapshot.getValue(Products.class);
                            dataSnapshot.getChildren();

                            if (Integer.parseInt(dataSnapshot.child("totalAmount").getValue().toString()) <= 0){

                                dataSnapshot.getRef().setValue(null);
                            }

                            list.add(products);
                        }

                    }

                }
                if(list.size() == 0){
                        empty_storage.setVisibility(View.VISIBLE);
                        txt_inventory.setVisibility(View.GONE);
                }else{
                    empty_storage.setVisibility(View.GONE);
                    txt_inventory.setVisibility(View.VISIBLE);

                }
                adapter.setList(list);
                avi.hide();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.dwnld_inventory_excel)
    void ongetExcel(){

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){

                            getExcel(list);
                            LogUtils.e(list.size());

                        }else {

                            General.alertDialog(Inventory.this, null, "Please Enable Storage Permission first");
                        }
                        if(report.isAnyPermissionPermanentlyDenied()){
                            ActivityManager.PERMISSION_TAB(Inventory.this);
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
//                                ActivityManager.PERMISSION_TAB(PermissionActivity.this);
                    }
                }).check();

    }

    private void getExcel(List<Products> list){

        String Fnamexls = "StockInventory" + ".xlsx";

        File sdCard = Environment.getExternalStorageDirectory();

        File directory = new File(sdCard.getAbsolutePath() + "/StockOneLite");
        directory.mkdirs();

        File file = new File(directory, Fnamexls);

        //New Workbook
        HSSFWorkbook wb = new HSSFWorkbook();

        Cell c = null;

        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("StockOne Lite");

        Products model = new Products();

        // Generate column headings
        Row row = sheet1.createRow(0);

        c = row.createCell(0);
        c.setCellValue("SKU ID");

        c = row.createCell(1);
        c.setCellValue("NAME");

        c = row.createCell(2);
        c.setCellValue("MAX AMOUNT");

        c = row.createCell(3);
        c.setCellValue("MIN AMOUNT");

        c = row.createCell(4);
        c.setCellValue("PRICE");

        c = row.createCell(5);
        c.setCellValue("Total AMOUNT");

        c = row.createCell(6);
        c.setCellValue("CREATED DATE");

        sheet1.setColumnWidth(0, (10 * 500));
        sheet1.setColumnWidth(1, (10 * 500));
        sheet1.setColumnWidth(2, (10 * 500));
        sheet1.setColumnWidth(3, (10 * 500));
        sheet1.setColumnWidth(4, (10 * 500));
        sheet1.setColumnWidth(5, (10 * 500));
        sheet1.setColumnWidth(6, (10 * 500));

//        for (int i =0; i< 7; i++){
//
//            CellStyle styleHeading = wb.createCellStyle();
//            Font font = wb.createFont();
//            font.setBold(true);
//            font.setFontName(HSSFFont.FONT_ARIAL);
//            font.setFontHeightInPoints((short) 11);
//            styleHeading.setFont(font);
//            styleHeading.setVerticalAlignment(VerticalAlignment.CENTER);
//            row.getCell(i).setCellStyle(styleHeading);
//        }

        int rownum = 1;
        for (Products p : list){
            Row row1 = sheet1.createRow(rownum++);

            Cell cellId = row1.createCell(0);
            cellId.setCellValue(p.getSKUID());

            Cell cellName = row1.createCell(1);
            cellName.setCellValue(p.getName());

            Cell maxAmt = row1.createCell(2);
            maxAmt.setCellValue(String.valueOf(p.getMaxAmount()));

            Cell minAmt = row1.createCell(3);
            minAmt.setCellValue(String.valueOf(p.getMinAmount()));

            Cell prc = row1.createCell(4);
            prc.setCellValue(String.valueOf(p.getPrice()));

            Cell totalAmt = row1.createCell(5);
            totalAmt.setCellValue(String.valueOf(p.getTotalAmount()));

            Date date = new Date(p.getCreatedDate());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd MMMM,yy");
            String getDate = format.format(date);

            Cell ctdDate = row1.createCell(6);
            ctdDate.setCellValue(getDate);

        }

        // Create a path where we will place our List of objects on external storage
//        File file = new File(context.getExternalFilesDir(null), fileName);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    General.showNotification(Inventory.this, Uri.fromFile(file), "StockInventory.xlsx");
                os.close();
            } catch (Exception ex) {
            }
        }

        ToastUtils.showLong("File saved to your local storage.");
    }

    @OnClick(R.id.btn_in_sku)
    void onEmptyInventory(){

        ActivityManager.IN(this);
        finish();
    }
}
