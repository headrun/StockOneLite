package com.stockone.lite.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stockone.lite.R;
import com.stockone.lite.model.SwipeModel;

import java.util.List;

public class CustomSwipeAdapter extends PagerAdapter {

    private List<SwipeModel> list;
    private Context context;
    private LayoutInflater inflater;

    public CustomSwipeAdapter(List<SwipeModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = inflater.inflate(R.layout.swipe_layout, container, false);
        SwipeModel item = list.get(position);
        ImageView imageView = (ImageView) item_view.findViewById(R.id.image_swipe);
        TextView text_swipe = (TextView) item_view.findViewById(R.id.text_swipe);

        imageView.setImageResource(item.getImage());
        text_swipe.setText(item.getText());
        container.addView(item_view);
        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
