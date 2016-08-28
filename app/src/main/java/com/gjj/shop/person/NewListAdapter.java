package com.gjj.shop.person;

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
import com.gjj.applibrary.util.Util;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseRecyclerViewAdapter;
import com.gjj.shop.base.PageSwitcher;
import com.gjj.shop.community.CommunityInfo;
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
 * Created by user on 16/8/27.
 */
public class NewListAdapter  extends BaseRecyclerViewAdapter<NewInfo> {

    private Context mContext;
    private LayoutInflater mInflater;
//    private List<CommunityInfo> mInfoList;

    public NewListAdapter(Context context, List<NewInfo> infoList) {
        super(context,infoList);
        mContext = context;
        mInflater = LayoutInflater.from(context);
//        mInfoList = infoList;
    }

    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.news_item, parent, false);
        return new RvViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RvViewHolder viewHolder = (RvViewHolder) holder;
        NewInfo info = items.get(position);

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
        Calendar targetCalendar = Calendar.getInstance();
        long current = System.currentTimeMillis();
        targetCalendar.setTimeInMillis(current - current % 1000);
        //注意Calendar的set方法，不会立即刷新，get() 和 add() 会让 Calendar 立刻刷新
        targetCalendar.set(Calendar.HOUR_OF_DAY, 0);
        targetCalendar.set(Calendar.MINUTE, 0);
        targetCalendar.set(Calendar.SECOND, 0);
        targetCalendar.get(Calendar.HOUR_OF_DAY);
        targetCalendar.get(Calendar.MINUTE);
        targetCalendar.get(Calendar.SECOND);
        if (dateCalendar.equals(targetCalendar)) {
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
        return Util.formatTimeDate3_MD(timeMs);
    }


    class RvViewHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.news_time)
        TextView mTime;
        @Bind(R.id.new_desc)
        TextView mDesc;

        public RvViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}

