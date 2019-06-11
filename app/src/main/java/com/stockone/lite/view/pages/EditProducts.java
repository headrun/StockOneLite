package com.stockone.lite.view.pages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stockone.lite.R;
import com.stockone.lite.model.Products;
import com.stockone.lite.utils.Constants;
import com.zcw.togglebutton.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import spencerstudios.com.bungeelib.Bungee;

public class EditProducts extends AppCompatActivity {

    @BindView(R.id.toolbar_edit_products) Toolbar toolbar;
    @BindView(R.id.text_edit_id) TextView text_edit_id;
    @BindView(R.id.text_edit_created) TextView text_edit_created;
    @BindView(R.id.et_edit_name) EditText et_edit_name;
    @BindView(R.id.et_edit_min_amt) EditText et_edit_min_amt;
    @BindView(R.id.et_edit_max_amt) EditText et_edit_max_amt;
    @BindView(R.id.et_edit_price) EditText et_edit_price;
    @BindView(R.id.toggle_rl) RelativeLayout toggle_rl;
    @BindView(R.id.inactive_switch) ToggleButton toggleButton;

    private Products model = new Products();

    DatabaseReference databaseReference;

    private String pro_id,name;
    private long created;
    private int min_amt, max_amt, totalAmount;
    private double price;
    private Boolean inactive;
    private FirebaseAuth mAuth;
    boolean isInactive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_products);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getIntent
        Intent extras = getIntent();
        Bundle bundle = extras.getExtras();
        assert bundle != null;
        pro_id = bundle.getString("pro_id");
        name = bundle.getString("name");
        created = bundle.getLong("created");
        min_amt = bundle.getInt("min_amt");
        max_amt = bundle.getInt("max_amt");
        totalAmount = bundle.getInt("totalAmount");
        price = bundle.getDouble("price");
        inactive = bundle.getBoolean("inactive");

        text_edit_id.setText(pro_id);
        et_edit_name.setText(name);
        Date date = new Date(created);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd MMMM,yy hh:mm a");
        String getDate = format.format(date);
        text_edit_created.setText(getDate);
        et_edit_min_amt.setText(String.valueOf(min_amt));
        et_edit_max_amt.setText(String.valueOf(max_amt));
        et_edit_price.setText(String.valueOf(price));

        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Product_Path);

        toggleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (!on) {

                            isInactive = false;
                        }
                else{
                    isInactive = true;
                }
            }
        });

        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (inactive) {
            toggleButton.toggleOn();
        } else {

            toggleButton.toggleOff();
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

    @OnClick(R.id.btn_edit_products)
    void onEditProductsClicked(){

        model.setSKUID(pro_id);
        model.setName(name);
//        model.setLocation(sku_location.getText().toString());
        model.setUID(mAuth.getUid());
        model.setMinAmount(Integer.parseInt(et_edit_min_amt.getText().toString()));
        model.setMaxAmount(Integer.parseInt(et_edit_max_amt.getText().toString()));
        model.setCreatedDate(created);
        model.setInactive(isInactive);
        model.setTotalAmount(totalAmount);
        model.setPrice(Double.parseDouble(et_edit_price.getText().toString()));

        databaseReference.child(Constants.KEY).setValue(model);

        ToastUtils.showLong("Saved");


    }
}
