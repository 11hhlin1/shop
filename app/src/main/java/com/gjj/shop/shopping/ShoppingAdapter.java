package com.gjj.shop.shopping;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.gjj.applibrary.app.AppLib;
import com.gjj.applibrary.glide.GlideCircleTransform;
import com.gjj.applibrary.util.Util;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseRecyclerViewAdapter;
import com.gjj.shop.model.ProductInfo;
import com.gjj.shop.net.UrlUtil;
import com.gjj.shop.widget.UnScrollableListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/30.
 */
public class ShoppingAdapter extends BaseRecyclerViewAdapter<ShopAdapterInfo>{
//    SelectListener mSelectListener;

    public boolean ismIsEdit() {
        return mIsEdit;
    }

    public void setmIsEdit(boolean mIsEdit) {
        this.mIsEdit = mIsEdit;
    }

    private boolean mIsEdit;

    public ShoppingAdapter(Context context, List<ShopAdapterInfo> shopInfoList,boolean isEdit) {
        super(context,shopInfoList);
        mIsEdit = isEdit;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.shopping_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder,position);
        final ShopAdapterInfo shopAdapterInfo = items.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        Glide.with(mContext)
                .load(UrlUtil.getHttpUrl(shopAdapterInfo.shopThumb))
                .centerCrop()
                .error(new ColorDrawable(AppLib.getResources().getColor(android.R.color.transparent)))
                .into(viewHolder.mShopAvatar);
        viewHolder.mShopName.setText(shopAdapterInfo.shopName);
        viewHolder.mSelBox.setOnCheckedChangeListener(null);
        viewHolder.mSelBox.setChecked(shopAdapterInfo.isSel);
//        viewHolder.mSelBox.setTag(shopAdapterInfo.goodsList);
        final ListAdapter listAdapter = new ListAdapter(this,mContext,shopAdapterInfo.goodsList,position);
        viewHolder.mShopList.setAdapter(listAdapter);

        viewHolder.mSelBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                shopAdapterInfo.isSel = isChecked;
                for (GoodsAdapterInfo goodsAdapterInfo : shopAdapterInfo.goodsList) {
                    goodsAdapterInfo.isSel = isChecked;
                }
                notifyDataSetChanged();
                listAdapter.notifyDataSetChanged();
            }
        });
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.shop_avatar)
        ImageView mShopAvatar;

        @Bind(R.id.sel_box)
        CheckBox mSelBox;

        @Bind(R.id.shop_name)
        TextView mShopName;

        @Bind(R.id.shopping_list)
        UnScrollableListView mShopList;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }
//    public void setmSelectListener(SelectListener listener) {
//        this.mSelectListener = listener;
//    }

//    interface SelectListener {
//        void select(String id);
//        void unSel(String id);
//    }

    class ListAdapter extends BaseAdapter {
        private List<GoodsAdapterInfo> mLists;
        private Context mContext;
        private int mPosition;
        ShoppingAdapter mShoppingAdapter;
        public ListAdapter(ShoppingAdapter shoppingAdapter, Context context, List<GoodsAdapterInfo> list, int pos) {
            mContext = context;
            mLists = list;
            mPosition = pos;
            mShoppingAdapter = shoppingAdapter;
        }

        @Override
        public int getCount() {
            return mLists.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.good_list_item, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            GoodsAdapterInfo goodsAdapterInfo = mLists.get(position);
            GoodsInfo goodsInfo = goodsAdapterInfo.goodsInfo;
            Map<String, String> map = (Map<String, String>) JSON.parse(goodsInfo.tags);
            StringBuilder stringBuilder = Util.getThreadSafeStringBuilder();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                stringBuilder.append(entry.getKey()).append(":").append(entry.getValue()).append("    ");
            }
                viewHolder.productArgs.setText(stringBuilder.toString());
            viewHolder.productPrice.setText(mContext.getString(R.string.money_has_mark, Util.getFormatData(goodsInfo.curPrice)));
            viewHolder.productAmount.setText(String.valueOf(goodsInfo.amount));
        viewHolder.productName.setText(goodsInfo.goodsName);
        Glide.with(mContext)
                .load(UrlUtil.getHttpUrl(goodsInfo.goodsLogoThumb))
                .centerCrop()
                .error(new ColorDrawable(AppLib.getResources().getColor(android.R.color.transparent)))
                .into(viewHolder.productAvatar);
            viewHolder.mPlus.setTag(goodsInfo);
            viewHolder.mSub.setTag(goodsInfo);
            viewHolder.goodsSelBox.setOnCheckedChangeListener(null);
            viewHolder.goodsSelBox.setChecked(goodsAdapterInfo.isSel);
//            viewHolder.goodsSelBox.setTag(position);
            viewHolder.goodsSelBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mLists.get(position).isSel = isChecked;
                    boolean isAllSel = true;
                    for (GoodsAdapterInfo goodsAdapterInfo : mLists) {
                        if(!goodsAdapterInfo.isSel) {
                            isAllSel = false;
                            break;
                        }
                    }
                    items.get(mPosition).isSel = isAllSel;
                    notifyDataSetChanged();
                    mShoppingAdapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }


        class ViewHolder {
            @Bind(R.id.goods_sel_box)
            CheckBox goodsSelBox;
            @Bind(R.id.product_avatar)
            ImageView productAvatar;
            @Bind(R.id.plus_btn)
            ImageView mPlus;
            @Bind(R.id.product_amount)
            TextView productAmount;
            @Bind(R.id.sub_btn)
            ImageView mSub;
            @Bind(R.id.amount_ll)
            LinearLayout amountLl;
            @Bind(R.id.product_name)
            TextView productName;
            @Bind(R.id.product_args)
            TextView productArgs;
            @Bind(R.id.product_price)
            TextView productPrice;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
                mPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mIsEdit) {
                            GoodsInfo goodsInfo = (GoodsInfo) mPlus.getTag();
                            if(goodsInfo != null)
                                goodsInfo.amount++;
                            notifyDataSetChanged();
                        }

                    }
                });
                mSub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mIsEdit) {
                            GoodsInfo goodsInfo = (GoodsInfo) mSub.getTag();
                            if(goodsInfo != null)
                                goodsInfo.amount--;
                        }
                        notifyDataSetChanged();
                    }
                });

            }
        }
    }
}
