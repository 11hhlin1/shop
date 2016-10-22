package com.gjj.shop.person;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gjj.applibrary.app.AppLib;
import com.gjj.applibrary.util.Util;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseRecyclerViewAdapter;
import com.gjj.shop.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 16/8/27.
 */
public class NewListAdapter extends BaseRecyclerViewAdapter<NewInfo> {
//    private List<CommunityInfo> mInfoList;

    public NewListAdapter(Context context, List<NewInfo> infoList) {
        super(context, infoList);
//        mInfoList = infoList;
    }

    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.news_item, parent, false);
        return new RvViewHolder(view);
    }

    public void addData(List<NewInfo> albums) {
        if (albums != items) {
            int position = items.size() - 1;
            items.addAll(albums);
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RvViewHolder viewHolder = (RvViewHolder) holder;
        NewInfo info = items.get(position);
        viewHolder.newDesc.setText(info.content);
        viewHolder.newTitle.setText(info.title);
        viewHolder.newsTime.setText(DateUtil.getTimeStr(info.createTime));
    }

    class RvViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.news_time)
        TextView newsTime;
        @Bind(R.id.new_title)
        TextView newTitle;
        @Bind(R.id.new_desc)
        TextView newDesc;
        public RvViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

