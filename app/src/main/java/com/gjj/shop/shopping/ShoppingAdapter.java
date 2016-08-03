package com.gjj.shop.shopping;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
public class ShoppingAdapter extends BaseRecyclerViewAdapter<ShopAdapterInfo>{
    SelectListener mSelectListener;
    public ShoppingAdapter(Context context, List<ShopAdapterInfo> shopInfoList) {
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
        ShopAdapterInfo shopInfo = items.get(position);
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
        viewHolder.mProductArgs.setText(shopInfo.shopArgs);
        viewHolder.mProductPrice.setText(shopInfo.shopPrice + "");
        viewHolder.mProductAmount.setText(shopInfo.shopAmount + "");
        viewHolder.mPlus.setTag(shopInfo);
        viewHolder.mSub.setTag(shopInfo);
        viewHolder.mSelBox.setChecked(shopInfo.isSel);
        viewHolder.mSelBox.setTag(shopInfo.id);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

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
            mPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShopInfo shopInfo = (ShopInfo) mPlus.getTag();
                    if(shopInfo != null)
                    shopInfo.shopAmount++;
                }
            });
            mSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShopInfo shopInfo = (ShopInfo) mSub.getTag();
                    if(shopInfo != null)
                        shopInfo.shopAmount--;
                }
            });
            mSelBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String id = (String) mSelBox.getTag();
                    if(isChecked) {
                        mSelectListener.select(id);
                    } else {
                        mSelectListener.unSel(id);
                    }
                }
            });
        }
    }
    public void setmSelectListener(SelectListener listener) {
        this.mSelectListener = listener;
    }

    interface SelectListener {
        void select(String id);
        void unSel(String id);
    }
}
