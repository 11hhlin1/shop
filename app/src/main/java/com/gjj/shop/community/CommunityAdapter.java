package com.gjj.shop.community;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gjj.applibrary.app.AppLib;
import com.gjj.applibrary.glide.GlideCircleTransform;
import com.gjj.applibrary.log.L;
import com.gjj.applibrary.util.Util;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseRecyclerViewAdapter;
import com.gjj.shop.base.PageSwitcher;
import com.gjj.shop.net.UrlUtil;
import com.gjj.shop.widget.UnScrollableGridView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chuck on 2016/7/27.
 */
public class CommunityAdapter extends BaseRecyclerViewAdapter<CommunityInfo> {

    private Context mContext;
    private LayoutInflater mInflater;
//    private List<CommunityInfo> mInfoList;

    public CommunityAdapter(Context context, List<CommunityInfo> infoList) {
        super(context,infoList);
        mContext = context;
        mInflater = LayoutInflater.from(context);
//        mInfoList = infoList;
    }

    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.feed_item, parent, false);
        return new RvViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RvViewHolder viewHolder = (RvViewHolder) holder;
        CommunityInfo info = items.get(position);
        Glide.with(mContext)
                .load(UrlUtil.getHttpUrl(info.thumbAvatar))
                .centerCrop()
                .placeholder(R.mipmap.s_user)
                .error(R.mipmap.s_user)
                .bitmapTransform(new GlideCircleTransform(mContext))
                .into(viewHolder.mAvatar);
        viewHolder.mNickName.setText(info.nickname);
        viewHolder.mDesc.setText(info.details);
        viewHolder.mTime.setText(getTimeLineTitle(info.createTime));
        viewHolder.mGridView.setAdapter(new GridAdapter(mContext, info.thumbList));
        viewHolder.mShareBtn.setTag(info);
    }
    /**
     * 将传入时间与当前时间进行对比，是否今天昨天
     *
     * @param timeMs
     * @return
     */
    private String getTimeLineTitle(long timeMs) {
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTimeInMillis(timeMs);
        L.d("@@@@@timeMs:" + timeMs);
        String dateStr = Util.formatTimeDate(timeMs);
        Calendar targetCalendar = Calendar.getInstance();
        long current = System.currentTimeMillis();
        String currentStr = Util.formatTimeDate(current);
        targetCalendar.setTimeInMillis(current - current % 1000);
        L.d("@@@@@current:" + current);
        //注意Calendar的set方法，不会立即刷新，get() 和 add() 会让 Calendar 立刻刷新
        targetCalendar.set(Calendar.HOUR_OF_DAY, 0);
        targetCalendar.set(Calendar.MINUTE, 0);
        targetCalendar.set(Calendar.SECOND, 0);
        targetCalendar.get(Calendar.HOUR_OF_DAY);
        targetCalendar.get(Calendar.MINUTE);
        targetCalendar.get(Calendar.SECOND);
        if (dateStr.equals(currentStr)) {
            String todaySDF = "HH:mm";
            SimpleDateFormat sfd = new SimpleDateFormat(todaySDF);
            return sfd.format(timeMs);
        } else {
            targetCalendar.add(Calendar.DATE, -1);
            if (dateCalendar.after(targetCalendar)) {
                return AppLib.getString(R.string.yesterday);
            }
        }
        // String otherSDF = "MM.dd";
//        SimpleDateFormat sfd = new SimpleDateFormat(Util.M_D_FORMAT1_STR);
        return Util.formatTimeDate3(timeMs);
    }
    public void addData(List<CommunityInfo> albums) {
        if (albums != items) {
            int position = items.size() -1;
            items.addAll(albums);
            notifyDataSetChanged();
//            notifyItemInserted(position >= 0 ? position : 0);
        }
    }

    class RvViewHolder extends RecyclerView.ViewHolder {

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
                String url = "http://www.android100.org/html/201504/06/132445.html";
                Bitmap bitmap = BitmapFactory.decodeResource(AppLib.getResources(), R.mipmap.nav05);
                PageSwitcher.switchToShareActivity((Activity) mContext,url, info.details, info.details,info.thumbList.get(0),bitmap);
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
                    .load(UrlUtil.getHttpUrl(url))
                    .centerCrop()
                    .placeholder(R.mipmap.s_sq_03)
                    .error(R.mipmap.s_sq_03)
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
