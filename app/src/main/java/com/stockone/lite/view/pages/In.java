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
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.nononsenseapps.filepicker.Utils;
import com.stockone.lite.R;
import com.stockone.lite.model.ProductLocation;
import com.stockone.lite.model.Products;
import com.stockone.lite.model.TransactionModel;
import com.stockone.lite.model.Zone;
import com.stockone.lite.presenter.OnBarCodeScanner;
import com.stockone.lite.utils.ActivityManager;
import com.stockone.lite.utils.Constants;
import com.stockone.lite.utils.General;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import spencerstudios.com.bungeelib.Bungee;

public class In extends AppCompatActivity {

    @BindView(R.id.toolbar_in) Toolbar toolbar;
    @BindView(R.id.in_sku_id) EditText sku_id;
    @BindView(R.id.in_sku_amt) EditText sku_amt;
    @BindView(R.id.in_sku_location) EditText sku_location;
    @BindView(R.id.btn_in_sku_add) CardView add;
    @BindView(R.id.barcode_in) ImageView barcode_in;
    DatabaseReference databaseReference, locationReference;

    ArrayList<String> locationList = new ArrayList<>();
    SpinnerDialog locationDialog;
    private FirebaseAuth mAuth;
    private Products model = new Products();
    private ProductLocation productLocation = new ProductLocation();


    long time = System.currentTimeMillis();
    boolean isExistence;
    boolean Existence;
    boolean isLocationAdded;
    String sku;
    String amount;
    OnBarCodeScanner onBarCodeScanner;
    public static String RESULT_IN = "";

    public int FILE_SELECT_CODE = 4332;
    List<Products> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        // location Dialog
        locationDialog = new SpinnerDialog(this, locationList, "Select or Search location", R.style.DialogAnimations_SmileWindow);

        sku_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationDialog.showSpinerDialog();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Product_Path);
        locationReference = FirebaseDatabase.getInstance().getReference(Constants.Product_Location_Path);

        locationDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {

                sku_location.setText(s);
                LogUtils.e(s);
                model.setLocation(s);

            }
        });

        getLocationList();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

    }

    @Override
    protected void onResume() {
        super.onResume();

//            String result=getIntent().getStringExtra("barcode");
//            sku_id.setText(result);


    }

    @OnClick(R.id.barcode_in)
    void onBarcodeScannedIn(){

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            Intent intent = new Intent(In.this, ScanBarcodeActivity.class);
                            startActivityForResult(intent, 0);
                        }else {
                        }
                        if(report.isAnyPermissionPermanentlyDenied()){
                            ActivityManager.PERMISSION_TAB(In.this);
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
//                                ActivityManager.PERMISSION_TAB(PermissionActivity.this);
                    }
                }).check();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            Bungee.slideRight(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_in_sku_add)
    void onSKUadded(){

        sku = sku_id.getText().toString().trim();
        amount = sku_amt.getText().toString().trim();

        if (TextUtils.isEmpty(sku)) {
            General.alertDialog(In.this, null, "Please enter SKU id");
            return;
        }

        if (TextUtils.isEmpty(amount)) {
            General.alertDialog(In.this, null, "Please enter required amount");
            return;
        }

        model.setSKUID(sku_id.getText().toString());
        model.setName(sku_id.getText().toString());
        model.setLocation(sku_location.getText().toString());
        model.setUID(mAuth.getUid());
        model.setMinAmount(0);
        model.setMaxAmount(0);
        model.setCreatedDate(time);
        model.setInactive(false);
        model.setPrice(0.0);


        checkProductsDb(sku);
        defaultLocationList();

//        setProductLoc();

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

    private void checkProductLoc(final String name, final String product_id){

        locationReference = FirebaseDatabase.getInstance().getReference(Constants.Product_Location_Path);
        locationReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Existence = false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("location_name").getValue() != null) {

                        LogUtils.e(dataSnapshot.child("location_name").getValue().equals(name) && !dataSnapshot.child("product_id").getValue().equals(product_id));

                        if (dataSnapshot.child("location_name").getValue().equals(name) && dataSnapshot.child("product_id").getValue().equals(product_id)) {

                            LogUtils.e("PRODUCT ->"+product_id);

                            try {
                                int totalQuant = Integer.parseInt(amount) + Integer.parseInt(dataSnapshot.child("product_quantity").getValue().toString());
                                LogUtils.e("Total Count - " + totalQuant);
                                productLocation.setProduct_quantity(totalQuant);
                            } catch (NumberFormatException ex) {

                            }
                            // Getting the ID from firebase database.
                            String IDFromServer = dataSnapshot.getKey();
                            productLocation.setFirebase_key(IDFromServer);

                            if (IDFromServer != null) {
                                locationReference.child(IDFromServer).setValue(productLocation);
                            }

                            Existence = true;

                        }
                    }
//

                }

                LogUtils.e("Exist ->" +Existence);

                if (!Existence){

                    try{
                        int  totalQuant = Integer.parseInt(amount);
                        LogUtils.e("Total - "+ totalQuant);
                        productLocation.setProduct_quantity(totalQuant);
                    }catch(NumberFormatException ex){

                    }

                    // Getting the ID from firebase database.
                    String IDFromServer = locationReference.push().getKey();
                    productLocation.setFirebase_key(IDFromServer);

                    if (IDFromServer != null) {
                        locationReference.child(IDFromServer).setValue(productLocation);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setProductLoc(){

        productLocation.setCareated(time);
        productLocation.setUser_id(mAuth.getUid());
        productLocation.setLocation_name(sku_location.getText().toString());
        productLocation.setProduct_id(sku);
        productLocation.setProduct_edt_qty("0");

        checkProductLoc(sku_location.getText().toString(), sku);

    }

    private void checkProductsDb(final String name){

        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Product_Path);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                isExistence = false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.child("name").getValue().equals(name)) {

                        try {
                            int totalQuant = Integer.parseInt(sku_amt.getText().toString()) + Integer.parseInt(dataSnapshot.child("totalAmount").getValue().toString());
                            model.setTotalAmount(totalQuant);
                        } catch (NumberFormatException ex) {

                        }
                        // Getting the ID from firebase database.
                        String IDFromServer = dataSnapshot.getKey();

                        if (IDFromServer != null) {
                            databaseReference.child(IDFromServer).setValue(model);
                        }

                        sku_id.setText("");
                        sku_amt.setText("");
                        ToastUtils.showLong("Added");
                        General.hideKeyboard(In.this, sku_id);
                        isExistence = true;
                    }
//
                }
                if (!isExistence){
                    try{
                            int  totalQuant = Integer.parseInt(sku_amt.getText().toString());
                            LogUtils.e("Total - "+ totalQuant);
                            model.setTotalAmount(Integer.parseInt(sku_amt.getText().toString()));
                        }catch(NumberFormatException ex){

                        }
                        // Getting the ID from firebase database.
                        String IDFromServer = databaseReference.push().getKey();

                        if (IDFromServer != null) {
                            databaseReference.child(IDFromServer).setValue(model);
                        }

                        sku_id.setText("");
                        sku_amt.setText("");
                        ToastUtils.showLong("Added");
                        General.hideKeyboard(In.this, sku_id);
                }

                setProductLoc();
                addTransactions(sku, amount, sku_location.getText().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addTransactions(String product_id, String quantity, String location){


        DatabaseReference transactionReference = FirebaseDatabase.getInstance().getReference(Constants.Transaction_Path);

        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setProduct_id(product_id);
        transactionModel.setProduct_name(product_id);
        transactionModel.setUser_id(mAuth.getUid());
        transactionModel.setTransaction_time(time);
        transactionModel.setProduct_loc(location);
        String transcationQuant = "p+"+quantity;
        transactionModel.setProduct_quant(transcationQuant.trim());
        LogUtils.e(transcationQuant);


        String IDFromServer = transactionReference.push().getKey();

        if (IDFromServer != null) {
            transactionReference.child(IDFromServer).setValue(transactionModel);
        }


    }

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
                sku_id.setText(result);
            }
        }
    }

    private void onInitializingHelp() {

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){

                            final CharSequence[] items = {"Download Excel file", "Upload Excel File"};
                            AlertDialog.Builder builder = new AlertDialog.Builder(In.this);
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    if (items[item].equals("Download Excel file")) {

                                        downloadexcel();

                                    } else if (items[item].equals("Upload Excel File")) {

                                        // This always works
                                        Intent i = new Intent(In.this, FilePickerActivity.class);
                                        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                                        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                                        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
                                        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath()+ "/StockOneLite/");

                                        startActivityForResult(i, FILE_SELECT_CODE);

                                    }
                                }
                            });
                            builder.show();

                        }else {

                            General.alertDialog(In.this, null, "Please Enable Storage Permission first");
                        }
                        if(report.isAnyPermissionPermanentlyDenied()){
                            ActivityManager.PERMISSION_TAB(In.this);
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
//                                ActivityManager.PERMISSION_TAB(PermissionActivity.this);
                    }
                }).check();

    }

    @OnClick(R.id.dwnld_in_excel)
    void onInExcelDownload(){

        onInitializingHelp();
    }

    private void downloadexcel(){

        String Fnamexls = "StockIn" + ".xlsx";

        File sdCard = Environment.getExternalStorageDirectory();

        File directory = new File(sdCard.getAbsolutePath() + "/StockOneLite");
        directory.mkdirs();

        File file = new File(directory, Fnamexls);

        //New Workbook
        HSSFWorkbook wb = new HSSFWorkbook();

        Cell c = null;

//        //Cell style for header row
//        CellStyle cs = wb.createCellStyle();
//        cs.setFillForegroundColor(HSSFColor.LIME.index);
//        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

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
                    General.showNotification(In.this, Uri.fromFile(file), "StockIn.xlsx");
                    os.close();
            } catch (Exception ex) {
            }
        }

//        WorkbookSettings wbSettings = new WorkbookSettings();
//        wbSettings.setLocale(new Locale("en", "EN"));
//
//        WritableFont titleFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, true, UnderlineStyle.NO_UNDERLINE, Colour.GREEN);
//        WritableCellFormat titleformat = new WritableCellFormat(titleFont);
//
//        WritableWorkbook workbook;
//        try {
//            workbook = Workbook.createWorkbook(file, wbSettings);
//            WritableSheet sheet = workbook.createSheet("StockOne Lite", 0);
//            Label label0 = new Label(0, 0, "SKU ID", titleformat);
//            Label label3 = new Label(1, 0, "Quantity", titleformat);
//            Label label5 = new Label(2, 0, "Location", titleformat);
//
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
//                General.showNotification(In.this, Uri.fromFile(file), "StockIn.xlsx");
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
                General.alertDialog(In.this,null, "Your excel is empty please fill it and then upload it");
                return;
            }
            LogUtils.e(POIFSFileSystem.hasPOIFSHeader(new BufferedInputStream( new FileInputStream(inputFile))));
//            LogUtils.e(POIXMLDocument.OLE_OBJECT_REL_TYPE(new BufferedInputStream( new FileInputStream(inputFile)));
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

        // splits the sb into rows.
        String[] rows = mStringBuilder.toString().split(":");

        list.clear();

        LogUtils.e("Rows ->"+rows.length);

//        if (rows.length <= 2){
//            General.alertDialog(In.this,null, "Your excel is empty please fill it and then upload it");
//            return;
//        }

        //Add to the ArrayList<XYValue> row by row
        for(int i=0; i<rows.length; i++) {
            //Split the columns of the rows


            final Products model = new Products();

            String[] columns = rows[i].split(",");

                //use try catch to make sure there are no "" that try to parse into doubles.
                try {
                    final String id = columns[0];
                    final String quantity = columns[1];
                    final String location = columns[2];

                    setProductexcelLoc(location.trim(), id, (int) Double.parseDouble(quantity));

                    addNewLocation(location.trim());

                    LogUtils.e("Location ->"+ location + " id -> "+id);

                    String cellInfo = "(id,quantity,location): (" + id + "," + quantity + "," + location + ")";
                    LogUtils.e("ParseStringBuilder: Data from row: " + cellInfo);

                    LogUtils.e("ZERO ->"+Double.parseDouble(quantity));

                    try {
                        model.setTotalAmount((int) Double.parseDouble(quantity));
                    } catch (NumberFormatException e) {

                        e.printStackTrace();
                    }
                    model.setSKUID(id);
                    model.setName(id);
                    model.setLocation(location);
                    model.setUID(mAuth.getUid());
                    model.setMinAmount(0);
                    model.setMaxAmount(0);
                    model.setCreatedDate(time);
                    model.setInactive(false);
                    model.setPrice(0.0);

                    //add the the uploadData ArrayList
                    list.add(model);

                    databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Product_Path);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            isExistence = false;
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                if (dataSnapshot.child("name").getValue() != null) {

                                    if (dataSnapshot.child("name").getValue().equals(model.getName())) {

                                        try {
                                            int totalQuant = (int) Double.parseDouble(quantity) + Integer.parseInt(dataSnapshot.child("totalAmount").getValue().toString());
                                            model.setTotalAmount(totalQuant);
                                        } catch (NumberFormatException ex) {

                                        }
                                        // Getting the ID from firebase database.
                                        String IDFromServer = dataSnapshot.getKey();

                                        if (IDFromServer != null) {
                                            databaseReference.child(IDFromServer).setValue(model);
                                        }

                                        isExistence = true;
//                                        ToastUtils.showLong("Added");
                                    }
//
                                }

                            }

                            LogUtils.e("Existence ->" +isExistence);

                            if (!isExistence){

                                try{
                                    int  totalQuant = Integer.parseInt(sku_amt.getText().toString());
                                    LogUtils.e("Total - "+ totalQuant);
                                    model.setTotalAmount((int) Double.parseDouble(quantity));
                                }catch(NumberFormatException ex){

                                }
                                // Getting the ID from firebase database.
                                String IDFromServer = databaseReference.push().getKey();

                                if (IDFromServer != null) {
                                    databaseReference.child(IDFromServer).setValue(model);
                                }

                            }
                            addTransactions(id, quantity.trim(), location.trim());

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } catch (NumberFormatException e) {

                    LogUtils.e("parseStringBuilder: NumberFormatException: " + e.getMessage());

                }
        }

        printDataToLog();
    }

    private void setProductexcelLoc(final String location, final String product_id, final int quantity){

        final ProductLocation productLocation = new ProductLocation();
        productLocation.setCareated(time);
        productLocation.setUser_id(mAuth.getUid());
        productLocation.setLocation_name(location);
        productLocation.setProduct_id(product_id);
        productLocation.setProduct_edt_qty("0");

        locationReference = FirebaseDatabase.getInstance().getReference(Constants.Product_Location_Path);
        locationReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Existence = false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("location_name").getValue() != null) {

                        LogUtils.e(dataSnapshot.child("location_name").getValue().equals(location) && !dataSnapshot.child("product_id").getValue().equals(product_id));

                        if (dataSnapshot.child("location_name").getValue().equals(location) && dataSnapshot.child("product_id").getValue().equals(product_id)) {

                            LogUtils.e("PRODUCT ->"+product_id);

                            try {
                                int totalQuant = quantity + Integer.parseInt(dataSnapshot.child("product_quantity").getValue().toString());
                                LogUtils.e("Total Count - " + totalQuant);
                                productLocation.setProduct_quantity(totalQuant);
                            } catch (NumberFormatException ex) {

                            }
                            // Getting the ID from firebase database.
                            String IDFromServer = dataSnapshot.getKey();
                            productLocation.setFirebase_key(IDFromServer);

                            if (IDFromServer != null) {
                                locationReference.child(IDFromServer).setValue(productLocation);
                            }

                            Existence = true;

                        }
                    }
//

                }

                LogUtils.e("Exist ->" +Existence);

                if (!Existence){

                    try{
                        productLocation.setProduct_quantity(quantity);
                    }catch(NumberFormatException ex){

                    }

                    // Getting the ID from firebase database.
                    String IDFromServer = locationReference.push().getKey();
                    productLocation.setFirebase_key(IDFromServer);

                    if (IDFromServer != null) {
                        locationReference.child(IDFromServer).setValue(productLocation);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void printDataToLog() {
        LogUtils.e("printDataToLog: Printing data to log...");

        ToastUtils.showLong(list.size()+ " items added successfully");

        for(int i = 0; i< list.size(); i++){
            String id = list.get(i).getSKUID();
            String quantity = String.valueOf(list.get(i).getTotalAmount());
            String location = list.get(i).getLocation();
            LogUtils.e("printDataToLog: (id, quantity, location): (" + id + "," + quantity + "," + location +")");
        }
    }

    private void addNewLocation(final String location){

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Zone_Path);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                isLocationAdded = false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.child("location").getValue() != null) {

                        if (dataSnapshot.child("location").getValue().equals(location)) {

                            Zone model = new Zone();

                            model.setLocation(location);
                            model.setUserid(mAuth.getUid());

                            // Getting the ID from firebase database.
                            String IDFromServer = dataSnapshot.getKey();

                            if (IDFromServer != null) {
                                databaseReference.child(IDFromServer).setValue(model);
                            }

                            isLocationAdded = true;
//                                        ToastUtils.showLong("Added");
                        }
//
                    }

                }

                LogUtils.e("Existence ->" +isExistence);

                if (!isLocationAdded){

                    Zone model = new Zone();

                    model.setLocation(location);
                    model.setUserid(mAuth.getUid());
                    // Getting the ID from firebase database.
                    String IDFromServer = databaseReference.push().getKey();

                    if (IDFromServer != null) {
                        databaseReference.child(IDFromServer).setValue(model);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
