package com.gjj.shop.shopping;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseRecyclerViewAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/30.
 */
public class ShoppingAdapter extends BaseRecyclerViewAdapter<ShopInfo>{

    public ShoppingAdapter(Context context, List<ShopInfo> shopInfoList) {
        super(context,shopInfoList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.shopping_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder,position);
        ShopInfo shopInfo = items.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        Glide.with(mContext)
                .load(shopInfo.shopAvatar)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(viewHolder.mShopAvatar);
        viewHolder.mShopName.setText(shopInfo.shopName);
        viewHolder.mProductName.setText(shopInfo.productName);
        Glide.with(mContext)
                .load(shopInfo.productAvatar)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(viewHolder.mProductAvatar);
        viewHolder.mProductArgs.setText(shopInfo.shopAgrs);
        viewHolder.mProductPrice.setText(shopInfo.shopPrice + "");
        viewHolder.mProductAmount.setText(shopInfo.shopAmount + "");
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.shop_avatar)
        ImageView mShopAvatar;

        @Bind(R.id.sel_box)
        CheckBox mSelBox;

        @Bind(R.id.shop_name)
        TextView mShopName;

        @Bind(R.id.product_avatar)
        ImageView mProductAvatar;

        @Bind(R.id.product_name)
        TextView mProductName;

        @Bind(R.id.product_args)
        TextView mProductArgs;

        @Bind(R.id.product_price)
        TextView mProductPrice;

        @Bind(R.id.product_amount)
        TextView mProductAmount;

        @Bind(R.id.plus_btn)
        ImageView mPlus;

        @Bind(R.id.sub_btn)
        ImageView mSub;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
