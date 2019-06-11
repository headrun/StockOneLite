package com.stockone.lite.view.pages;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.nononsenseapps.filepicker.Utils;
import com.stockone.lite.R;
import com.stockone.lite.model.ProductLocation;
import com.stockone.lite.model.Products;
import com.stockone.lite.model.TransactionModel;
import com.stockone.lite.utils.ActivityManager;
import com.stockone.lite.utils.Constants;
import com.stockone.lite.utils.General;
import com.stockone.lite.view.adapter.OutAdapter;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import spencerstudios.com.bungeelib.Bungee;

public class Out extends AppCompatActivity {

    @BindView(R.id.toolbar_out)
    Toolbar toolbar;
    @BindView(R.id.out_sku_id)
    EditText out_id;
    @BindView(R.id.rv_out)
    RecyclerView recyclerView;
    @BindView(R.id.text_locations)
    TextView text_locations;
    @BindView(R.id.btn_out_sku) CardView btn_out_sku;
    private List<ProductLocation> list = new ArrayList<>();
    private ArrayList<String> productList = new ArrayList<>();
    private SpinnerDialog productDialog;
    private String sku;
    private OutAdapter adapter;
    DatabaseReference locationRefernce;
    int totalQuantity = 0;
    boolean isSet;
    FirebaseAuth mAuth;
    long time = System.currentTimeMillis();
    TransactionModel transactionModel = new TransactionModel();
    public int FILE_SELECT_CODE = 4332;
    private List<Products> productsList = new ArrayList<>();
    boolean Existence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new OutAdapter(this);
        recyclerView.setAdapter(adapter);

        // location Dialog
        productDialog = new SpinnerDialog(this, productList, "Select or Search SKUs", R.style.DialogAnimations_SmileWindow);

        out_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productDialog.showSpinerDialog();
                if (productList.size() == 0){

                    General.alertDialog(Out.this, null, "No Inventory Added Yet");
                }
            }
        });

        productDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {

                out_id.setText(s);
                sku = s;
                LogUtils.e(sku);
                getLocations(sku);
                text_locations.setVisibility(View.VISIBLE);
                btn_out_sku.setVisibility(View.VISIBLE);

            }
        });

        locationRefernce = FirebaseDatabase.getInstance().getReference(Constants.Product_Location_Path);

        getInventoryItems(null);
        getLocations(null);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            Bungee.slideRight(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getInventoryItems(final String name){

        isSet = true;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Product_Path);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.child("uid").getValue() != null) {

                        if (dataSnapshot.child("uid").getValue().equals(mAuth.getUid())) {

                            Products products = dataSnapshot.getValue(Products.class);
                            dataSnapshot.getChildren();

                            productList.add(products.getSKUID());
                        }

                    }

                    if (dataSnapshot.child("skuid").getValue() != null && isSet) {

                        if (dataSnapshot.child("skuid").getValue().equals(name)) {

                            Products pro = dataSnapshot.getValue(Products.class);
                            try {
                                int totalQuant = pro.getTotalAmount() - totalQuantity;
                                pro.setTotalAmount(totalQuant);
                            } catch (NumberFormatException ex) {

                            }
                            // Getting the ID from firebase database.
                            String IDFromServer = dataSnapshot.getKey();

                            if (IDFromServer != null && isSet) {
                                databaseReference.child(IDFromServer).setValue(pro);
                                totalQuantity=0;
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

    private void getLocations(final String sku){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Product_Location_Path);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                list.clear(); //change here

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.child("product_id").getValue() != null) {

                        if (dataSnapshot.child("product_id").getValue().equals(sku)) {

                            ProductLocation productLocation = dataSnapshot.getValue(ProductLocation.class);
                            LogUtils.e(dataSnapshot.getKey());
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

    @OnClick(R.id.btn_out_sku)
    void onOutButton(){

        General.hideKeyboard(this, OutAdapter.validatn_et);


        for (Map.Entry<String, Object> entry : OutAdapter.edited_value.entrySet()) {

            int i =1;

            ProductLocation mProductLocation= (ProductLocation) entry.getValue();

            try {

                if (Integer.parseInt(mProductLocation.getProduct_edt_qty()) > mProductLocation.getProduct_quantity()) {
                    General.alertDialog(Out.this, null, "Please add proper quantity");
                    return;
                }
            }catch (NumberFormatException e){

            }

            try {
                LogUtils.e("Edited "+i++ +" "+ +Integer.parseInt(mProductLocation.getProduct_edt_qty()));
                mProductLocation.setProduct_quantity(mProductLocation.getProduct_quantity()-Integer.parseInt(mProductLocation.getProduct_edt_qty()));
                totalQuantity = totalQuantity+Integer.parseInt(mProductLocation.getProduct_edt_qty());

            }catch (NumberFormatException e){


            }
            transactionModel.setProduct_loc(mProductLocation.getLocation_name());
            String transcationQuant = "m-"+mProductLocation.getProduct_edt_qty();
            transactionModel.setProduct_quant(transcationQuant);
            LogUtils.e(transcationQuant);
            addTransactions();
            OutAdapter.edited_value.put(entry.getKey(), mProductLocation);

        }

        getLoc();

        LogUtils.e("TotalQuant -> "+totalQuantity);


    }

    @OnClick(R.id.barcode_out)
    void onBarCodeOut(){

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            Intent intent = new Intent(Out.this, ScanBarcodeActivity.class);
                            startActivityForResult(intent, 0);
                        }else {
                        }
                        if(report.isAnyPermissionPermanentlyDenied()){
                            ActivityManager.PERMISSION_TAB(Out.this);
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
//                                ActivityManager.PERMISSION_TAB(PermissionActivity.this);
                    }
                }).check();
    }


    private void getLoc(){

        FirebaseDatabase.getInstance()
                .getReference(Constants.Product_Location_Path)
                .updateChildren(OutAdapter.edited_value).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ToastUtils.showLong("Removed");
                getInventoryItems(sku);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                LogUtils.e(e.getCause().getMessage());
            }
        });

//        locationRefernce.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                    if (dataSnapshot.child("product_id").getValue().equals(sku)){
//
//                        FirebaseDatabase.getInstance()
//                                .getReference(Constants.Product_Location_Path).
//                                setValue(OutAdapter.edited_value).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                LogUtils.e("success");
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                LogUtils.e(e.getCause().getMessage());
//                            }
//                        });
//
//                    }
//
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    private void addTransactions(){


        DatabaseReference transactionReference = FirebaseDatabase.getInstance().getReference(Constants.Transaction_Path);

        transactionModel.setProduct_id(sku);
        transactionModel.setProduct_name(sku);
        transactionModel.setUser_id(mAuth.getUid());
        transactionModel.setTransaction_time(time);


        String IDFromServer = transactionReference.push().getKey();

        if (IDFromServer != null) {
            transactionReference.child(IDFromServer).setValue(transactionModel);
        }


    }

                                                       /* Excel Part*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == FILE_SELECT_CODE && resultCode == Activity.RESULT_OK && data.getData() != null) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<Uri> files = Utils.getSelectedFilesFromResult(data);
                        for (Uri uri: files) {
                            File file = Utils.getFileForUri(uri);
                            LogUtils.e("PATH ->"+file.getPath());
                            readExcelData(file.getPath());
                        }
                    }
                }, 300);

            }

            if (resultCode == Activity.RESULT_OK && requestCode == 0){
                String result=data.getStringExtra("barcode");
                out_id.setText(result);
                getLocations(result);
                text_locations.setVisibility(View.VISIBLE);
                btn_out_sku.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick(R.id.dwnld_out_excel)
    void downloadOutExcel(){

        onInitializingHelp();
    }

    private void onInitializingHelp() {

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){

                            final CharSequence[] items = {"Download Excel file", "Upload Excel File"};
                            AlertDialog.Builder builder = new AlertDialog.Builder(Out.this);
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    if (items[item].equals("Download Excel file")) {

                                        downloadexcel();

                                    } else if (items[item].equals("Upload Excel File")) {
                                        Intent i = new Intent(Out.this, FilePickerActivity.class);
                                        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                                        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                                        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
                                        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath()+ "/StockOneLite/");

                                        startActivityForResult(i, FILE_SELECT_CODE);
//                    startActivity(Intent.createChooser(intent, "Open folder"));
//
//                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                    intent.setType("file/*");
//                    startActivityForResult(intent,FILE_SELECT_CODE);

                                    }
                                }
                            });
                            builder.show();

                        }else {

                            General.alertDialog(Out.this, null, "Please Enable Storage Permission first");
                        }
                        if(report.isAnyPermissionPermanentlyDenied()){
                            ActivityManager.PERMISSION_TAB(Out.this);
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
//                                ActivityManager.PERMISSION_TAB(PermissionActivity.this);
                    }
                }).check();

    }

    private void downloadexcel(){

        String Fnamexls = "StockOut" + ".xlsx";

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

        // Generate column headings
        Row row = sheet1.createRow(0);

        c = row.createCell(0);
        c.setCellValue("SKU ID");

        c = row.createCell(1);
        c.setCellValue("Quantity");

        c = row.createCell(2);
        c.setCellValue("Location");

        sheet1.setColumnWidth(0, (10 * 500));
        sheet1.setColumnWidth(1, (10 * 500));
        sheet1.setColumnWidth(2, (10 * 500));

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
                    General.showNotification(Out.this, Uri.fromFile(file), "StockOut.xlsx");
                os.close();
            } catch (Exception ex) {
            }
        }

//        WorkbookSettings wbSettings = new WorkbookSettings();
//        wbSettings.setLocale(new Locale("en", "EN"));
//
//        WritableFont titleFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, true, UnderlineStyle.NO_UNDERLINE, Colour.GREEN);
//        WritableCellFormat titleformat = new WritableCellFormat(titleFont);
//        WritableWorkbook workbook;
//        try {
//            workbook = Workbook.createWorkbook(file, wbSettings);
//            WritableSheet sheet = workbook.createSheet("StockOne Lite", 0);
//            Label label0 = new Label(0, 0, "SKU ID", titleformat);
//            Label label3 = new Label(1, 0, "Quantity", titleformat);
//            Label label5 = new Label(2, 0, "Location", titleformat);
//            try {
//                sheet.addCell(label0);
//                sheet.addCell(label3);
//                sheet.addCell(label5);
//            } catch (RowsExceededException e) {
//                e.printStackTrace();
//            } catch (WriteException e) {
//                e.printStackTrace();
//            }
//            workbook.write();
//
//            try {
//                General.showNotification(Out.this, Uri.fromFile(file), "StockOut.xlsx");
//                workbook.close();
//            } catch (WriteException e) {
//
//                e.printStackTrace();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        ToastUtils.showLong("File saved to your local storage.");
    }

    private void readExcelData(String filePath) {
        LogUtils.e("readExcelData: Reading Excel File.");

        //decarle input file
        File inputFile = new File(filePath);

        try {
            if (POIFSFileSystem.hasPOIFSHeader(new BufferedInputStream( new FileInputStream(inputFile)))){
                General.alertDialog(Out.this,null, "Your excel is empty please fill it and then upload it");
                return;
            }
            InputStream inputStream = new FileInputStream(inputFile);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            StringBuilder sb = new StringBuilder();

            //outter loop, loops through rows
            for (int r = 1; r < rowsCount; r++) {
                XSSFRow row = sheet.getRow(r);
                int cellsCount = row.getPhysicalNumberOfCells();
                //inner loop, loops through columns
                for (int c = 0; c < cellsCount; c++) {
                    //handles if there are to many columns on the excel sheet.
                    if(c>3){
                        LogUtils.e("readExcelData: ERROR. Excel File Format is incorrect! " );
                        ToastUtils.showLong("ERROR: Excel File Format is incorrect!");
                        break;
                    }else{
                        String value = getCellAsString(row, c, formulaEvaluator);
                        String cellInfo = "r:" + r + "; c:" + c + "; v:" + value;
                        LogUtils.e("readExcelData: Data from row: " + cellInfo);
                        sb.append(value + ", ");
                    }
                }
                sb.append(":");
            }
            LogUtils.e("readExcelData: STRINGBUILDER: " + sb.toString());

            parseStringBuilder(sb);

        }catch (FileNotFoundException e) {
            LogUtils.e("readExcelData: FileNotFoundException. " + e.getMessage() );
            ToastUtils.showLong("No such file directory found");
        } catch (IOException e) {
            LogUtils.e("readExcelData: Error reading inputstream. " + e.getMessage() );
        }
    }

    private String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = ""+cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if(HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("MM/dd/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = ""+numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = ""+cellValue.getStringValue();
                    break;
                default:
            }
        } catch (NullPointerException e) {

            LogUtils.e("getCellAsString: NullPointerException: " + e.getMessage() );
        }
        return value;
    }

    public void parseStringBuilder(StringBuilder mStringBuilder){
        LogUtils.e("parseStringBuilder: Started parsing.");

        isSet = true;
        // splits the sb into rows.
        String[] rows = mStringBuilder.toString().split(":");

        productsList.clear();

        //Add to the ArrayList<XYValue> row by row
        for(int i=0; i<rows.length; i++) {
            //Split the columns of the rows

            Products model = new Products();

            String[] columns = rows[i].split(",");

            //use try catch to make sure there are no "" that try to parse into doubles.
            try {
                final String id = columns[0];
                final String quantity = columns[1];
                final String location = columns[2];

                setProductexcelLoc(location.trim(), id, (int) Double.parseDouble(quantity));

                String cellInfo = "(id,quantity,location): (" + id + "," + quantity + "," + location + ")";
                LogUtils.e("ParseStringBuilder: Data from row: " + cellInfo);

                LogUtils.e("ZERO ->"+Double.parseDouble(quantity));

                model.setSKUID(id);
                model.setTotalAmount((int) Double.parseDouble(quantity));
                model.setLocation(location);

                //add the the uploadData ArrayList
                productsList.add(model);


                //Products DB
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Product_Path);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                            if (dataSnapshot.child("skuid").getValue() != null && isSet) {

                                if (dataSnapshot.child("skuid").getValue().equals(id)) {

                                    Products pro = dataSnapshot.getValue(Products.class);
                                    try {
                                        int totalQuant = pro.getTotalAmount() - (int) Double.parseDouble(quantity);
                                        pro.setTotalAmount(totalQuant);
                                    } catch (NumberFormatException ex) {

                                    }
                                    // Getting the ID from firebase database.
                                    String IDFromServer = dataSnapshot.getKey();
                                    if (IDFromServer != null && isSet) {
                                        databaseReference.child(IDFromServer).setValue(pro);

                                    }

                                }
                            }

                        }

                        addTransactions(id, quantity.trim(), location.trim());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            } catch (NumberFormatException e) {

                LogUtils.e("parseStringBuilder: NumberFormatException: " + e.getMessage());

            }
        }

        printDataToLog();
    }

    private void setProductexcelLoc(final String location, final String product_id, final int quantity){

        final DatabaseReference locationReference = FirebaseDatabase.getInstance().getReference(Constants.Product_Location_Path);
        locationReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("location_name").getValue() != null) {

                        if (dataSnapshot.child("location_name").getValue().equals(location.trim()) && dataSnapshot.child("product_id").getValue().equals(product_id)) {

                            LogUtils.e("PRODUCT ->"+product_id);

                            final ProductLocation productLocation = new ProductLocation();
                            productLocation.setCareated(time);
                            productLocation.setUser_id(mAuth.getUid());
                            productLocation.setLocation_name(location);
                            productLocation.setProduct_id(product_id);
                            productLocation.setProduct_edt_qty("0");

                            try {
                                int totalQuant = Integer.parseInt(dataSnapshot.child("product_quantity").getValue().toString()) - quantity;
                                LogUtils.e("Total Count - " + totalQuant);
                                productLocation.setProduct_quantity(totalQuant);
                            } catch (NumberFormatException ex) {

                            }
                            // Getting the ID from firebase database.
                            String IDFromServer = dataSnapshot.getKey();
                            productLocation.setFirebase_key(IDFromServer);

                            if (IDFromServer != null) {
                                locationReference.child(IDFromServer).setValue(productLocation);
                                LogUtils.e("Submitting");
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

    private void printDataToLog() {
        LogUtils.e("printDataToLog: Printing data to log...");

        ToastUtils.showLong(productsList.size()+ " items removed successfully");

        for(int i = 0; i< productsList.size(); i++){
            String id = productsList.get(i).getSKUID();
            String quantity = String.valueOf(productsList.get(i).getTotalAmount());
            String location = productsList.get(i).getLocation();
            LogUtils.e("printDataToLog: (id, quantity, location): (" + id + "," + quantity + "," + location +")");
        }
    }

    private void addTransactions(String product_id, String quantity, String location){


        DatabaseReference transactionReference = FirebaseDatabase.getInstance().getReference(Constants.Transaction_Path);

        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setProduct_id(product_id);
        transactionModel.setProduct_name(product_id);
        transactionModel.setUser_id(mAuth.getUid());
        transactionModel.setTransaction_time(time);
        transactionModel.setProduct_loc(location);
        String transcationQuant = "m-"+quantity;
        transactionModel.setProduct_quant(transcationQuant.trim());
        LogUtils.e(transcationQuant);


        String IDFromServer = transactionReference.push().getKey();

        if (IDFromServer != null) {
            transactionReference.child(IDFromServer).setValue(transactionModel);
        }


    }

}
