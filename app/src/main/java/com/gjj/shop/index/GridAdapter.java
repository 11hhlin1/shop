package com.gjj.shop.index;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gjj.shop.R;
import com.gjj.shop.model.ProductInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GridAdapter extends BaseAdapter {
    private List<ProductInfo> mProductList = new ArrayList<ProductInfo>();
    private LayoutInflater mInflater;
    private Context mContext;

    public GridAdapter(Context context, List<ProductInfo> productList) {
        mContext = context;
        mProductList = productList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewTag viewTag;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.product_item, null);
            viewTag = new ItemViewTag(convertView) ;
            convertView.setTag(viewTag);
        } else {
            viewTag = (ItemViewTag) convertView.getTag();
        }
        ProductInfo productInfo = mProductList.get(position);
        Glide.with(mContext)
                .load(productInfo.mUrl)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(viewTag.mProductIv);
        viewTag.mProductName.setText(productInfo.mName);
        return convertView;
    }

    class ItemViewTag {
        @Bind(R.id.imageView)
        ImageView mProductIv;

        @Bind(R.id.title)
        TextView mProductName;

        public ItemViewTag(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

}
