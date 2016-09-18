package com.gjj.shop.category;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gjj.shop.R;
import com.gjj.shop.model.ProductInfo;
import com.gjj.shop.net.UrlUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RightGridAdapter extends BaseAdapter {
    private List<NextCategoryInfo> mProductList;
    private LayoutInflater mInflater;
    private Context mContext;

    public RightGridAdapter(Context context, List<NextCategoryInfo> productList) {
        mContext = context;
        mProductList = productList;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<NextCategoryInfo> msg) {
        if (msg != mProductList) {
            if (mProductList != null) {
                mProductList.clear();
            }
            this.mProductList = msg;
        }
        notifyDataSetChanged();
    }

    public void addData(List<NextCategoryInfo> albums) {
        if (albums != mProductList) {
            int position = mProductList.size() - 1;
            mProductList.addAll(albums);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mProductList.size();
    }

    @Override
    public NextCategoryInfo getItem(int position) {
        return mProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewTag;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.cate_right_grid_item, null);
            viewTag = new ViewHolder(convertView);
            convertView.setTag(viewTag);
        } else {
            viewTag = (ViewHolder) convertView.getTag();
        }
        NextCategoryInfo productInfo = mProductList.get(position);

//        Glide.with(mContext)
//                .load(UrlUtil.getHttpUrl(productInfo.url))
//                .centerCrop()
//                .placeholder(R.mipmap.cj_sp_01)
//                .error(R.mipmap.cj_sp_01)
//                .into(viewTag.image);
        viewTag.text.setText(productInfo.name);

        return convertView;
    }


    class ViewHolder {
        @Bind(R.id.image)
        ImageView image;
        @Bind(R.id.text)
        TextView text;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
