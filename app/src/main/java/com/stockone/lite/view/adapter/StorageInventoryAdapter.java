package com.stockone.lite.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stockone.lite.R;
import com.stockone.lite.model.ProductLocation;
import com.stockone.lite.model.Products;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StorageInventoryAdapter extends RecyclerView.Adapter<StorageInventoryAdapter.ViewHolder> {

    private Context context;
    private List<ProductLocation> list = new ArrayList<>();

    public StorageInventoryAdapter(Context context) {

        this.context = context;
    }

    @Override
    public StorageInventoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_storage_inventory, parent, false);

        StorageInventoryAdapter.ViewHolder viewHolder = new StorageInventoryAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final StorageInventoryAdapter.ViewHolder holder, final int position) {

        final ProductLocation model = list.get(position);

        holder.product_id.setText(model.getProduct_id());
        holder.product_name.setText(model.getProduct_id());
        holder.product_quantity.setText(model.getProduct_quantity()+ " pieces");


    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.product_id_storage)
        TextView product_id;
        @BindView(R.id.product_name_storage) TextView product_name;
        @BindView(R.id.product_quantity_storage) TextView product_quantity;

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