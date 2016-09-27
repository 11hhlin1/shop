package com.gjj.shop.order;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.gjj.applibrary.app.AppLib;
import com.gjj.applibrary.util.Util;
import com.gjj.shop.R;
import com.gjj.shop.net.UrlUtil;
import com.gjj.shop.shopping.GoodsInfo;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Chuck on 2016/9/27.
 */
public class GoodItemListAdapter extends BaseAdapter{

        private List<GoodsInfo> mLists;
        private Context mContext;
        private int mPosition;

        public GoodItemListAdapter(Context context, List<GoodsInfo> list, int pos) {
            mContext = context;
            mLists = list;
            mPosition = pos;
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.order_good_list_item, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            GoodsInfo goodsInfo = mLists.get(position);
            Map<String, String> map = (Map<String, String>) JSON.parse(goodsInfo.tags);
            StringBuilder stringBuilder = Util.getThreadSafeStringBuilder();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                stringBuilder.append(entry.getKey()).append(":").append(entry.getValue()).append("    ");
            }
            viewHolder.productDesc.setText(stringBuilder.toString());
            viewHolder.productPriceNew.setText(mContext.getString(R.string.money_has_mark, Util.getFormatData(goodsInfo.curPrice)));
            viewHolder.productPriceOld.setText(mContext.getString(R.string.money_has_mark, Util.getFormatData(goodsInfo.prePrice)));
            viewHolder.productAmount.setText("X " + String.valueOf(goodsInfo.amount));
            viewHolder.productName.setText(goodsInfo.goodsName);
            viewHolder.productPriceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            Glide.with(mContext)
                    .load(UrlUtil.getHttpUrl(goodsInfo.goodsLogoThumb))
                    .centerCrop()
                    .error(new ColorDrawable(AppLib.getResources().getColor(android.R.color.transparent)))
                    .into(viewHolder.productAvatar);

            return convertView;
        }


        class ViewHolder {
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
            @Bind(R.id.shop_detail_rl)
            RelativeLayout shopDetailRl;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

}
