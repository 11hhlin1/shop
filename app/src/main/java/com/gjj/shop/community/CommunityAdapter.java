package com.gjj.shop.community;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gjj.shop.R;
import com.gjj.shop.widget.UnScrollableGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chuck on 2016/7/27.
 */
public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.RvViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<CommunityInfo> mInfoList;

    public CommunityAdapter(Context context, List<CommunityInfo> infoList) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mInfoList = infoList;
    }

    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.feed_item, parent, false);
        return new RvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RvViewHolder holder, int position) {
        CommunityInfo info = mInfoList.get(position);
        Glide.with(mContext)
                .load(info.avatar)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.mAvatar);
        holder.mNickName.setText(info.nickname);
        holder.mDesc.setText(info.desc);
        holder.mTime.setText(info.time + "");
        holder.mGridView.setAdapter(new GridAdapter(mContext, info.imgaeList));
        holder.mShareBtn.setTag(info);
    }

    @Override
    public int getItemCount() {
        return mInfoList.size();
    }

    static class RvViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.avatar)
        ImageView mAvatar;
        @Bind(R.id.nickname)
        TextView mNickName;
        @Bind(R.id.desc)
        TextView mDesc;
        @Bind(R.id.image_grid)
        UnScrollableGridView mGridView;
        @Bind(R.id.time)
        TextView mTime;
        @Bind(R.id.share_btn)
        ImageView mShareBtn;
        @OnClick(R.id.share_btn)
        void share() {
            CommunityInfo info = (CommunityInfo)mShareBtn.getTag();
            if(info != null) {

            }
        }
        public RvViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class GridAdapter extends BaseAdapter {
        private List<String> mImageList = new ArrayList<String>();
        private LayoutInflater mInflater;
        private Context mContext;

        public GridAdapter(Context context, List<String> imageList) {
            mContext = context;
            mImageList = imageList;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public Object getItem(int position) {
            return mImageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemViewTag viewTag;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.image_item, null);
                viewTag = new ItemViewTag(convertView) ;
                convertView.setTag(viewTag);
            } else {
                viewTag = (ItemViewTag) convertView.getTag();
            }
            String url = mImageList.get(position);
            Glide.with(mContext)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(viewTag.imageView);
            return convertView;
        }

        class ItemViewTag {
            @Bind(R.id.imageView)
            ImageView imageView;

            public ItemViewTag(View itemView) {
                ButterKnife.bind(this, itemView);
            }
        }

    }
}
