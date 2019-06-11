package com.stockone.lite.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stockone.lite.R;
import com.stockone.lite.model.Products;
import com.stockone.lite.utils.ActivityManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private Context context;
    private List<Products> list = new ArrayList<>();

    public ProductsAdapter(Context context) {

        this.context = context;
    }

    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_inventory_list, parent, false);

        ProductsAdapter.ViewHolder viewHolder = new ProductsAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ProductsAdapter.ViewHolder holder, final int position) {

        final Products model = list.get(position);

        holder.product_id.setText(model.getSKUID());
        holder.product_name.setText(model.getName());
        holder.product_quantity.setText(model.getTotalAmount()+ " pieces");

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActivityManager.EDIT_PRODUCTS(context, model.getSKUID(), model.getCreatedDate(), model.getName(),
                        model.getMinAmount(), model.getMaxAmount(), model.getPrice(), model.isInactive(), model.getTotalAmount());
            }
        });


    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.product_id)
        TextView product_id;
        @BindView(R.id.product_name) TextView product_name;
        @BindView(R.id.product_quantity) TextView product_quantity;
        @BindView(R.id.card_inventory)
        CardView parent;

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

    public List<Products> getList() {
        return list;
    }

    public void setList(List<Products> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
