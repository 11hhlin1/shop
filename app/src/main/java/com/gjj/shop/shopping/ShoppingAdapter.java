package com.gjj.shop.shopping;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gjj.shop.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/30.
 */
public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder>{

    private Context mContext;
    private LayoutInflater mInflater;
    private List<ShopInfo> mShopList;

    public ShoppingAdapter(Context context, List<ShopInfo> productList) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mShopList = productList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.shopping_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ShopInfo shopInfo = mShopList.get(position);
//        Glide.with(mContext)
//                .load(productInfo.mUrl)
//                .centerCrop()
//                .placeholder(R.mipmap.ic_launcher)
//                .error(R.mipmap.ic_launcher)
//                .into(holder.mProductIv);
//        holder.mProductName.setText(productInfo.mName);
    }

    @Override
    public int getItemCount() {
        return mShopList.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.imageView)
        ImageView mProductIv;
        @Bind(R.id.title)
        TextView mProductName;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
