package com.gjj.shop.index;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gjj.applibrary.glide.GlideCircleTransform;
import com.gjj.applibrary.util.Util;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseRecyclerViewAdapter;
import com.gjj.shop.community.CommunityInfo;
import com.gjj.shop.net.UrlUtil;
import com.gjj.shop.util.DateUtil;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Chuck on 2016/9/1.
 */
public class UserAdviceAdapter extends BaseRecyclerViewAdapter<CommentInfo> {
    private Context mContext;
    private LayoutInflater mInflater;
//    private List<CommunityInfo> mInfoList;

    public UserAdviceAdapter(Context context, List<CommentInfo> infoList) {
        super(context, infoList);
        mContext = context;
        mInflater = LayoutInflater.from(context);
//        mInfoList = infoList;
    }

    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.comment_list_item, parent, false);
        return new RvViewHolder(view);
    }

    public void addData(List<CommentInfo> albums) {
        if (albums != items) {
            int position = items.size() -1;
            items.addAll(albums);
            notifyDataSetChanged();
//            notifyItemInserted(position >= 0 ? position : 0);
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RvViewHolder viewHolder = (RvViewHolder) holder;
        final CommentInfo info = items.get(position);
        Glide.with(mContext)
                .load(UrlUtil.getHttpUrl(info.avatar))
                .centerCrop()
                .placeholder(R.mipmap.s_user)
                .error(R.mipmap.s_user)
                .bitmapTransform(new GlideCircleTransform(mContext))
                .into(viewHolder.userAvatar);
        viewHolder.commentTitle.setText(info.nickname);
        viewHolder.commentDesc.setText(info.content);
        viewHolder.commentTime.setText(DateUtil.getTimeStr(info.createTime));
        if(info.star >= 1) {
            viewHolder.selBox1.setChecked(true);
        } else {
            viewHolder.selBox1.setChecked(false);
        }
        if(info.star >= 2) {
            viewHolder.selBox2.setChecked(true);
        } else {
            viewHolder.selBox2.setChecked(false);
        }
        if(info.star >= 3) {
            viewHolder.selBox3.setChecked(true);
        } else {
            viewHolder.selBox3.setChecked(false);
        }
        if(info.star >= 4) {
            viewHolder.selBox4.setChecked(true);
        } else {
            viewHolder.selBox4.setChecked(false);
        }
        if(info.star >= 5) {
            viewHolder.selBox5.setChecked(true);
        } else {
            viewHolder.selBox5.setChecked(false);
        }
    }


     class RvViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.user_avatar)
        ImageView userAvatar;
        @Bind(R.id.comment_title)
        TextView commentTitle;
        @Bind(R.id.sel_box_1)
        CheckBox selBox1;
        @Bind(R.id.sel_box_2)
        CheckBox selBox2;
        @Bind(R.id.sel_box_3)
        CheckBox selBox3;
        @Bind(R.id.sel_box_5)
        CheckBox selBox5;
        @Bind(R.id.sel_box_4)
        CheckBox selBox4;
        @Bind(R.id.star_item)
        LinearLayout starItem;
        @Bind(R.id.comment_time)
        TextView commentTime;
        @Bind(R.id.comment_desc)
        TextView commentDesc;
        @Bind(R.id.my_comment)
        RelativeLayout myComment;

         RvViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
