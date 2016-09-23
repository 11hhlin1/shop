package com.gjj.shop.person;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gjj.applibrary.app.AppLib;
import com.gjj.applibrary.glide.GlideCircleTransform;
import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.applibrary.log.L;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.applibrary.util.Util;
import com.gjj.shop.R;
import com.gjj.shop.base.PageSwitcher;
import com.gjj.shop.community.CommunityInfo;
import com.gjj.shop.index.ProductDetailFragment;
import com.gjj.shop.model.ProductInfo;
import com.gjj.shop.net.ApiConstants;
import com.gjj.shop.net.UrlUtil;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Chuck on 2016/8/31.
 */
public class CollectListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<CollectInfo> mItems;
    private MyCollectFragment mFragment;

    public void setmClickCallBack(ClickCallBack mClickCallBack) {
        this.mClickCallBack = mClickCallBack;
    }

    private ClickCallBack mClickCallBack;
    public CollectListAdapter(Context context, List<CollectInfo> items, MyCollectFragment fragment) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mItems = items;
        mFragment = fragment;
    }
    public CollectInfo getData(int position) {
        if (null == mItems) {
            return null;
        }
        return mItems.get(position);
    }

    public List<CollectInfo> getDataList() {
        return mItems;
    }
    public void addData(List<CollectInfo> albums) {
        if (albums != mItems) {
            int position = mItems.size() -1;
            mItems.addAll(albums);
            notifyDataSetChanged();
//            notifyItemInserted(position >= 0 ? position : 0);
        }
    }
    public void setData(List<CollectInfo> msg) {
        if (msg != mItems) {
            if (mItems != null) {
                mItems.clear();
            }
            this.mItems = msg;
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public CollectInfo getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        SlideView slideView = (SlideView) convertView;
        if (slideView == null) {
            View itemView = mInflater.inflate(R.layout.collect_list_item, null);

            slideView = new SlideView(mContext);
            slideView.setContentView(itemView);

            holder = new ViewHolder(slideView);
            slideView.setOnSlideListener(mFragment);
            slideView.setTag(holder);
        } else {
            holder = (ViewHolder) slideView.getTag();
        }
        CollectInfo item = mItems.get(position);
        ProductInfo productInfo = item.product;
        item.slideView = slideView;
        item.slideView.shrink();
        Glide.with(mContext)
                .load(UrlUtil.getHttpUrl(productInfo.logo))
                .centerCrop()
                .error(new ColorDrawable(AppLib.getResources().getColor(android.R.color.transparent)))
                .into(holder.productCover);
        holder.newPrice.setText(mContext.getString(R.string.money_has_mark, Util.getFormatData(productInfo.curPrice)));
        holder.oldPrice.setText(mContext.getString(R.string.money_has_mark, Util.getFormatData(productInfo.prePrice)));
        holder.deleteBtn.setTag(position);
        holder.text.setText(productInfo.name);
//        holder.deleteBtn.setOnClickListener(mFragment);
//        holder.root.setOnClickListener(mFragment);
        return slideView;
    }

    class ViewHolder {
        @Bind(R.id.product_cover)
        ImageView productCover;
        @Bind(R.id.text)
        TextView text;
        @Bind(R.id.tags)
        TextView tags;
        @Bind(R.id.new_price)
        TextView newPrice;
        @Bind(R.id.old_price)
        TextView oldPrice;
        @Bind(R.id.delete)
        TextView deleteBtn;
        @Bind(R.id.collect_rl)
        RelativeLayout root;

        @OnClick(R.id.delete)
        void setDeleteBtn() {
            int position = (int) deleteBtn.getTag();
            mClickCallBack.delete(position);
        }

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = (int) deleteBtn.getTag();
//                    CollectInfo collectInfo = getItem(position);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("product",collectInfo.product);
//                    PageSwitcher.switchToTopNavPageNoTitle((Activity)mContext,ProductDetailFragment.class,bundle,"","");
//                }
//            });
        }


    }

    public interface ClickCallBack {
       void delete(int pos);
    }
}
