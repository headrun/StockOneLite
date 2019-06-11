package com.stockone.lite.view.pages;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.blankj.utilcode.util.LogUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stockone.lite.R;
import com.stockone.lite.model.TransactionModel;
import com.stockone.lite.utils.Constants;
import com.stockone.lite.view.adapter.TransactionAdapter;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import spencerstudios.com.bungeelib.Bungee;

public class Transactions extends AppCompatActivity {

    @BindView(R.id.toolbar_transactions) Toolbar toolbar;
    @BindView(R.id.rv_transactions) RecyclerView recyclerView;
    @BindView(R.id.empty_storage)
    RelativeLayout empty_storage;
    @BindView(R.id.avi_transactions)
    AVLoadingIndicatorView avi;
    @BindView(R.id.lottie_storage)
    LottieAnimationView lottie;
    private TransactionAdapter adapter;
    private List<TransactionModel> list = new ArrayList<>();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        lottie.setAnimation("search_file.json");
        lottie.loop(true);
        lottie.playAnimation();

        mAuth = FirebaseAuth.getInstance();

        getTransactions();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            Bungee.slideRight(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getTransactions() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Transaction_Path);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        databaseReference.orderByChild("transaction_time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.child("user_id").getValue() != null){

                        if (dataSnapshot.child("user_id").getValue().equals(mAuth.getUid())){

                            TransactionModel transactionModel = dataSnapshot.getValue(TransactionModel.class);
                            dataSnapshot.getChildren();

                            list.add(transactionModel);
                        }
                    }

                }

                if(list.size() == 0){
                        empty_storage.setVisibility(View.VISIBLE);
                }else{
                    empty_storage.setVisibility(View.GONE);
                }
                adapter = new TransactionAdapter(Transactions.this);
                recyclerView.setAdapter(adapter);
                adapter.setList(list);
                avi.hide();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
    }

}
