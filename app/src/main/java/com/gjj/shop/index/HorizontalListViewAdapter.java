package com.gjj.shop.index;


  
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;  
import android.view.ViewGroup;  
import android.widget.BaseAdapter;  
import android.widget.ImageView;  
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gjj.applibrary.app.AppLib;
import com.gjj.applibrary.glide.GlideCircleTransform;
import com.gjj.shop.R;
import com.gjj.shop.net.UrlUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HorizontalListViewAdapter extends BaseAdapter{  
//    private String[] mImages;
//    private String[] mTitles;
    private Context mContext;  
    private LayoutInflater mInflater;
    private List<ShopInfo> mShopInfos;

    public HorizontalListViewAdapter(Context context, List<ShopInfo> shopInfos){
        this.mContext = context;  
//        this.mImages = imgUrl;
//        this.mTitles = titles;
        mShopInfos = shopInfos;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);  
    }
    public void setData(List<ShopInfo> msg) {
        if (msg != mShopInfos) {
            if (mShopInfos != null) {
                mShopInfos.clear();
            }
            this.mShopInfos = msg;
        }
        notifyDataSetChanged();
    }

    @Override  
    public int getCount() {  
        return mShopInfos.size();
    }  
    @Override  
    public ShopInfo getItem(int position) {
        return mShopInfos.get(position);
    }  
  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
  
        ViewHolder holder;  
        if(convertView==null){  
            convertView = mInflater.inflate(R.layout.index_shop_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);  
        }else{  
            holder=(ViewHolder)convertView.getTag();  
        }
        ShopInfo shopInfo = getItem(position);
        holder.mTitle.setText(shopInfo.name);
        Glide.with(mContext)
                .load(UrlUtil.getHttpUrl(shopInfo.logo))
                .centerCrop()
                .error(new ColorDrawable(AppLib.getResources().getColor(android.R.color.transparent)))
                .bitmapTransform(new GlideCircleTransform(mContext))
                .into(holder.mImage);
  
        return convertView;  
    }  
  
    class ViewHolder {
         @Bind(R.id.shop_name)
         TextView mTitle;
         @Bind(R.id.shop_img)
         ImageView mImage;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }  

}