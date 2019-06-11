package com.stockone.lite.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.stockone.lite.R;
import com.stockone.lite.model.TransactionModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private Context context;
    private List<TransactionModel> list = new ArrayList<>();

    public TransactionAdapter(Context context) {

        this.context = context;
    }

    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_transaction_layout, parent, false);

        TransactionAdapter.ViewHolder viewHolder = new TransactionAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TransactionAdapter.ViewHolder holder, final int position) {

        final TransactionModel model = list.get(position);

        holder.transcation_pro_id.setText(model.getProduct_id());
        holder.transcation_pro_name.setText(model.getProduct_name());

        String productQuant = removeFirstChar(model.getProduct_quant());
        holder.transcation_pro_quant.setText(productQuant+ " pieces");
        holder.transcation_pro_loc.setText(model.getProduct_loc());

        Date date = new Date(model.getTransaction_time());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd MMMM,yy hh:mm a");
        String getDate = format.format(date);
        holder.transcation_pro_time.setText(getDate);

        String sign = Character.toString(model.getProduct_quant().charAt(0));

        if (sign.equals("m")){

            holder.img_transaction.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_out));
        }else if (sign.equals("p")){

            holder.img_transaction.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_in));

        }else if (sign.equals("c")){

            holder.img_transaction.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_count));
        }



    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.transcation_pro_id) TextView transcation_pro_id;
        @BindView(R.id.transcation_pro_name) TextView transcation_pro_name;
        @BindView(R.id.transcation_pro_quant) TextView transcation_pro_quant;
        @BindView(R.id.transcation_pro_loc) TextView transcation_pro_loc;
        @BindView(R.id.transcation_pro_time) TextView transcation_pro_time;
        @BindView(R.id.img_transaction) ImageView img_transaction;

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

    public List<TransactionModel> getList() {
        return list;
    }

    public void setList(List<TransactionModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public String removeFirstChar(String s){
        return s.substring(1);
    }
}