package com.gjj.shop.index;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gjj.shop.R;
import com.gjj.shop.base.PageSwitcher;
import com.gjj.shop.model.ProductInfo;
import com.gjj.shop.net.UrlUtil;

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

    public void setData(List<ProductInfo> msg) {
        if (msg != mProductList) {
            if (mProductList != null) {
                mProductList.clear();
            }
            this.mProductList = msg;
        }
        notifyDataSetChanged();
    }
    public void addData(List<ProductInfo> albums) {
        if (albums != mProductList) {
            int position = mProductList.size() -1;
            mProductList.addAll(albums);
            notifyDataSetChanged();
        }
    }
    @Override
    public int getCount() {
        return mProductList.size();
    }

    @Override
    public ProductInfo getItem(int position) {
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
                .load(UrlUtil.getHttpUrl(productInfo.logo))
                .centerCrop()
                .placeholder(R.mipmap.cj_sp_01)
                .error(R.mipmap.cj_sp_01)
                .into(viewTag.mProductIv);
        viewTag.mProductName.setText(productInfo.name);
        viewTag.mOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        viewTag.mProductName.setTag(position);

        return convertView;
    }

    class ItemViewTag {
        @Bind(R.id.imageView)
        ImageView mProductIv;

        @Bind(R.id.title)
        TextView mProductName;

        @Bind(R.id.new_price)
        TextView mNewPrice;

        @Bind(R.id.old_price)
        TextView mOldPrice;

        public ItemViewTag(View itemView) {
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) mProductName.getTag();
                    ProductInfo productInfo = getItem(pos);
                    Bundle bundle = new Bundle();
                    bundle.putString("goodsId",String.valueOf(productInfo.goodsId));
                    PageSwitcher.switchToTopNavPageNoTitle((Activity) mContext,ProductDetailFragment.class,bundle,"","");

                }
            });
        }
    }

}
