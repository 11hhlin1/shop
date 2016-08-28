package com.gjj.shop.person;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gjj.shop.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/25.
 */
public class OrderGridAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private String[] mNames;
    private int[] mIcons;


    public OrderGridAdapter(Context context, String [] names, int [] icons) {
        mContext = context;
        mIcons = icons;
        mNames = names;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mIcons.length;
    }

    @Override
    public Object getItem(int position) {
        return mIcons[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewTag;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.order_grid_item, null);
            viewTag = new ViewHolder(convertView);
            convertView.setTag(viewTag);
        } else {
            viewTag = (ViewHolder) convertView.getTag();
        }

        viewTag.shopImg.setImageResource(mIcons[position]);
        viewTag.shopName.setText(mNames[position]);
        return convertView;
    }


    static class ViewHolder {
        @Bind(R.id.shop_img)
        ImageView shopImg;
        @Bind(R.id.shop_name)
        TextView shopName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
