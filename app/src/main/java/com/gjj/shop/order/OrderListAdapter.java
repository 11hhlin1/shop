package com.gjj.shop.order;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gjj.applibrary.app.AppLib;
import com.gjj.applibrary.glide.GlideCircleTransform;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseRecyclerViewAdapter;
import com.gjj.shop.net.UrlUtil;
import com.gjj.shop.shopping.ShopInfo;
import com.gjj.shop.widget.UnScrollableListView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/8/14.
 */
public class OrderListAdapter extends BaseRecyclerViewAdapter<OrderInfo> {
    public void setmBtnCallBack(BtnCallBack mBtnCallBack) {
        this.mBtnCallBack = mBtnCallBack;
    }

    BtnCallBack mBtnCallBack;
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
        final OrderInfo orderInfo = items.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        Glide.with(mContext)
                .load(UrlUtil.getHttpUrl(orderInfo.shopLogoThumb))
                .centerCrop()
                .bitmapTransform(new GlideCircleTransform(mContext))
                .error(new ColorDrawable(AppLib.getResources().getColor(android.R.color.transparent)))
                .into(viewHolder.shopAvatar);
        viewHolder.shopName.setText(orderInfo.shopName);
        viewHolder.orderId.setText(mContext.getString(R.string.order_num,orderInfo.orderId));
        switch (orderInfo.status) {
            case 0:
                viewHolder.orderState.setText(mContext.getString(R.string.pay_order));
                viewHolder.cancelBtn.setVisibility(View.VISIBLE);
                viewHolder.payBtn.setVisibility(View.VISIBLE);
                viewHolder.cancelBtn.setText(mContext.getString(R.string.cancel_order_btn));
                viewHolder.payBtn.setText(mContext.getString(R.string.pay));
                break;
            case 1:
                viewHolder.orderState.setText(mContext.getString(R.string.accepting_order));
                break;
            case 2:
                viewHolder.orderState.setText(mContext.getString(R.string.accept_order));
                break;
            case 3:
                viewHolder.orderState.setText(mContext.getString(R.string.cancel_order));
                viewHolder.bottomRl.setVisibility(View.GONE);
                break;
        }
        final GoodItemListAdapter listAdapter = new GoodItemListAdapter( mContext, orderInfo.goodsList, position);
        viewHolder.mGoodList.setAdapter(listAdapter);
        viewHolder.orderId.setTag(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.shop_avatar)
        ImageView shopAvatar;
        @Bind(R.id.shop_name)
        TextView shopName;
        @Bind(R.id.order_id)
        TextView orderId;
        @Bind(R.id.order_state)
        TextView orderState;
        @Bind(R.id.good_list)
        UnScrollableListView mGoodList;
//        @Bind(R.id.product_avatar)
//        ImageView productAvatar;
//        @Bind(R.id.product_name)
//        TextView productName;
//        @Bind(R.id.product_desc)
//        TextView productDesc;
//        @Bind(R.id.product_price_new)
//        TextView productPriceNew;
//        @Bind(R.id.product_price_old)
//        TextView productPriceOld;
//        @Bind(R.id.product_amount)
//        TextView productAmount;
        @Bind(R.id.pay_btn)
        Button payBtn;
        @Bind(R.id.cancel_btn)
        Button cancelBtn;
        @Bind(R.id.bottom_rl)
        RelativeLayout bottomRl;

        @OnClick(R.id.cancel_btn)
        void setCancelBtn() {
            int pos = (int) orderId.getTag();
            mBtnCallBack.cancelOrder(pos);
        }

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface BtnCallBack {
        public void cancelOrder(int pos);
        public void payOrder(int pos);
        public void AdviceOrder(int pos);
        public void CheckGood(int pos);
    }
}
