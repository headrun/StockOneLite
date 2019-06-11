package com.stockone.lite.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stockone.lite.R;
import com.stockone.lite.model.ProductLocation;
import com.stockone.lite.presenter.OnEditTextChanged;
import com.stockone.lite.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OutAdapter extends RecyclerView.Adapter<OutAdapter.ViewHolder> {

    private Context context;
    public static List<ProductLocation> list = new ArrayList<>();
//    public static String edited_value;
    public static Map<String, Object> edited_value = new LinkedHashMap<>();
    private OnEditTextChanged onEditTextChanged;
    String quantity;
    public static EditText validatn_et;


    public OutAdapter(Context context) {

        this.context = context;
    }

    @Override
    public OutAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_out_list, parent, false);

        OutAdapter.ViewHolder viewHolder = new OutAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final OutAdapter.ViewHolder holder, final int position) {

        final ProductLocation model = list.get(position);

        validatn_et = holder.edit_quantity;

        holder.location_name.setText(model.getLocation_name());
        holder.location_quant.setText(""+model.getProduct_quantity() +" pieces");

        holder.edit_quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {


                    quantity = editable.toString();
                    model.setProduct_edt_qty(quantity);
                edited_value.put(model.getFirebase_key(), model);

            }
        });



    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.out_storage_location) TextView location_name;
        @BindView(R.id.out_storage_quant) TextView location_quant;
        @BindView(R.id.out_storage_quantity) EditText edit_quantity;

        public ViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public List<ProductLocation> getList() {
        return list;
    }

    public void setList(List<ProductLocation> list) {
        this.list = list;
        notifyDataSetChanged();
    }


}