package com.gjj.shop.order;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.gjj.applibrary.app.AppLib;
import com.gjj.applibrary.glide.GlideCircleTransform;
import com.gjj.applibrary.util.Util;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseRecyclerViewAdapter;
import com.gjj.shop.net.UrlUtil;
import com.gjj.shop.shopping.GoodsInfo;
import com.gjj.shop.shopping.ShopInfo;
import com.gjj.shop.widget.UnScrollableListView;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 16/9/24.
 */
public class GoodListAdapter extends BaseRecyclerViewAdapter<ShopInfo> {
//    SelectListener mSelectListener;


    public GoodListAdapter(Context context, List<ShopInfo> shopInfoList) {
        super(context, shopInfoList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.order_shop_list_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        final ShopInfo shopAdapterInfo = items.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        Glide.with(mContext)
                .load(UrlUtil.getHttpUrl(shopAdapterInfo.shopThumb))
                .centerCrop()
                .bitmapTransform(new GlideCircleTransform(mContext))
                .error(new ColorDrawable(AppLib.getResources().getColor(android.R.color.transparent)))
                .into(viewHolder.mShopAvatar);
        viewHolder.mShopName.setText(shopAdapterInfo.shopName);
//        viewHolder.mSelBox.setTag(shopAdapterInfo.goodsList);
        final GoodItemListAdapter listAdapter = new GoodItemListAdapter( mContext, shopAdapterInfo.goodsList, position);
        viewHolder.mShopList.setAdapter(listAdapter);


    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.shop_avatar)
        ImageView mShopAvatar;

        @Bind(R.id.shop_name)
        TextView mShopName;

        @Bind(R.id.shopping_list)
        UnScrollableListView mShopList;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }



}