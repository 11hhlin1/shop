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
import com.gjj.shop.R;
import com.gjj.shop.base.BaseRecyclerViewAdapter;
import com.gjj.shop.community.CommunityInfo;
import com.gjj.shop.net.UrlUtil;

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


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RvViewHolder viewHolder = (RvViewHolder) holder;
        final CommentInfo info = items.get(position);
//        Glide.with(mContext)
//                .load(UrlUtil.getHttpUrl(info.thumbAvatar))
//                .centerCrop()
//                .placeholder(R.mipmap.s_user)
//                .error(R.mipmap.s_user)
//                .bitmapTransform(new GlideCircleTransform(mContext))
//                .into(viewHolder.userAvatar);

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
