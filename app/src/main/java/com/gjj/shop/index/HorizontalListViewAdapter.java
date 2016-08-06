package com.gjj.shop.index;


  
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;  
import android.view.ViewGroup;  
import android.widget.BaseAdapter;  
import android.widget.ImageView;  
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gjj.applibrary.glide.GlideCircleTransform;
import com.gjj.shop.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HorizontalListViewAdapter extends BaseAdapter{  
    private String[] mImages;
    private String[] mTitles;  
    private Context mContext;  
    private LayoutInflater mInflater;  

    public HorizontalListViewAdapter(Context context, String[] titles, String[] imgUrl){
        this.mContext = context;  
        this.mImages = imgUrl;
        this.mTitles = titles;  
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);  
    }  
    @Override  
    public int getCount() {  
        return mImages.length;
    }  
    @Override  
    public Object getItem(int position) {  
        return position;  
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
          
        holder.mTitle.setText(mTitles[position]);
        Glide.with(mContext)
                .load(mImages[position])
                .centerCrop()
                .placeholder(R.mipmap.scyh3)
                .error(R.mipmap.scyh3)
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