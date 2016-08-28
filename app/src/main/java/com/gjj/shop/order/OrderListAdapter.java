package com.gjj.shop.order;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gjj.shop.R;
import com.gjj.shop.base.BaseRecyclerViewAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/14.
 */
public class OrderListAdapter extends BaseRecyclerViewAdapter<OrderInfo> {

    public OrderListAdapter(Context context, List<OrderInfo> orderInfoList) {
        super(context, orderInfoList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.order_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.shop_avatar)
        ImageView shopAvatar;
        @Bind(R.id.shop_name)
        TextView shopName;
        @Bind(R.id.order_id)
        TextView orderId;
        @Bind(R.id.order_state)
        TextView orderState;
        @Bind(R.id.product_avatar)
        ImageView productAvatar;
        @Bind(R.id.product_name)
        TextView productName;
        @Bind(R.id.product_desc)
        TextView productDesc;
        @Bind(R.id.product_price_new)
        TextView productPriceNew;
        @Bind(R.id.product_price_old)
        TextView productPriceOld;
        @Bind(R.id.product_amount)
        TextView productAmount;
        @Bind(R.id.pay_btn)
        Button payBtn;
        @Bind(R.id.cancel_btn)
        Button cancelBtn;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


//    class ViewHolder extends RecyclerView.ViewHolder {
//
//        @Bind(R.id.shop_avatar)
//        ImageView mShopAvatar;
//
//        @Bind(R.id.sel_box)
//        CheckBox mSelBox;
//
//        @Bind(R.id.shop_name)
//        TextView mShopName;
//
//        @Bind(R.id.product_avatar)
//        ImageView mProductAvatar;
//
//        @Bind(R.id.product_name)
//        TextView mProductName;
//
//        @Bind(R.id.product_args)
//        TextView mProductArgs;
//
//        @Bind(R.id.product_price)
//        TextView mProductPrice;
//
//        @Bind(R.id.product_amount)
//        TextView mProductAmount;
//
//        @Bind(R.id.plus_btn)
//        ImageView mPlus;
//
//        @Bind(R.id.sub_btn)
//        ImageView mSub;
//        public ViewHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//        }
//    }
}
