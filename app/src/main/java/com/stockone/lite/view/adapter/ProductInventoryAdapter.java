package com.stockone.lite.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stockone.lite.R;
import com.stockone.lite.model.ProductLocation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductInventoryAdapter extends RecyclerView.Adapter<ProductInventoryAdapter.ViewHolder> {

    private Context context;
    private List<ProductLocation> list = new ArrayList<>();

    public ProductInventoryAdapter(Context context) {

        this.context = context;
    }

    @Override
    public ProductInventoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product_inventory, parent, false);

        ProductInventoryAdapter.ViewHolder viewHolder = new ProductInventoryAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ProductInventoryAdapter.ViewHolder holder, final int position) {

        final ProductLocation model = list.get(position);

        holder.product_name.setText(model.getLocation_name());
        holder.product_count.setText("("+model.getProduct_quantity()+ " pieces)");


    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.inventory_product_name) TextView product_name;
        @BindView(R.id.inventory_product_count) TextView product_count;

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
