package com.gjj.shop.index;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gjj.shop.R;
import com.gjj.shop.model.ProductInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/3/10 上午11:01
 */
public class AdviceProductAdapter extends RecyclerView.Adapter<AdviceProductAdapter.RvViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<ProductInfo> mProductList;

    public AdviceProductAdapter(Context context, List<ProductInfo> productList) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mProductList = productList;
    }

    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.product_item, parent, false);
        return new RvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RvViewHolder holder, int position) {
        ProductInfo productInfo = mProductList.get(position);
        Glide.with(mContext)
                .load(productInfo.logo)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.mProductIv);
        holder.mProductName.setText(productInfo.name);
        holder.mOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    class RvViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.imageView)
        ImageView mProductIv;
        @Bind(R.id.title)
        TextView mProductName;
        @Bind(R.id.new_price)
        TextView mNewPrice;
        @Bind(R.id.old_price)
        TextView mOldPrice;

        public RvViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
