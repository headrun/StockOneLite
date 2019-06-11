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
import com.stockone.lite.model.Zone;
import com.stockone.lite.utils.ActivityManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private Context context;
    private List<Zone> list = new ArrayList<>();

    public LocationAdapter(Context context) {

        this.context = context;
    }

    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_location_list, parent, false);

        LocationAdapter.ViewHolder viewHolder = new LocationAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final LocationAdapter.ViewHolder holder, final int position) {

        final Zone model = list.get(position);

        holder.location_name.setText(model.getLocation());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.STORAGE_INVENTORY(context, model.getLocation());
            }
        });


    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.location_name)
        TextView location_name;
        @BindView(R.id.card_location)
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

    public List<Zone> getList() {
        return list;
    }

    public void setList(List<Zone> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}