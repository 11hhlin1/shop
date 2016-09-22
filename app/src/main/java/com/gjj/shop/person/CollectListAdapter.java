package com.gjj.shop.person;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gjj.shop.R;
import com.gjj.shop.community.CommunityInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Chuck on 2016/8/31.
 */
public class CollectListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<CollectInfo> mItems;
    private MyCollectFragment mFragment;
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
        item.slideView = slideView;
        item.slideView.shrink();

//        holder.icon.setImageResource(item.iconRes);
//        holder.title.setText(item.title);
//        holder.msg.setText(item.msg);
//        holder.time.setText(item.time);
        holder.deleteBtn.setTag(position);
        holder.deleteBtn.setOnClickListener(mFragment);

        return slideView;
    }

    static class ViewHolder {
        @Bind(R.id.product_cover)
        ImageView productCover;
        @Bind(R.id.text)
        TextView text;
        @Bind(R.id.new_price)
        TextView newPrice;
        @Bind(R.id.old_price)
        TextView oldPrice;
        @Bind(R.id.delete)
        Button deleteBtn;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
