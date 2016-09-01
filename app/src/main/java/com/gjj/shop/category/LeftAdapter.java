package com.gjj.shop.category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gjj.shop.R;
import com.gjj.shop.base.BaseRecyclerViewAdapter;
import com.gjj.shop.index.CommentInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Chuck on 2016/9/1.
 */
public class LeftAdapter extends BaseRecyclerViewAdapter<CategoryInfo> {
    private Context mContext;
    private LayoutInflater mInflater;
//    private List<CommunityInfo> mInfoList;

    public LeftAdapter(Context context, List<CategoryInfo> infoList) {
        super(context, infoList);
        mContext = context;
        mInflater = LayoutInflater.from(context);
//        mInfoList = infoList;
    }

    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.category_left_list_item, parent, false);
        return new RvViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RvViewHolder viewHolder = (RvViewHolder) holder;
        final CategoryInfo info = items.get(position);
//        Glide.with(mContext)
//                .load(UrlUtil.getHttpUrl(info.thumbAvatar))
//                .centerCrop()
//                .placeholder(R.mipmap.s_user)
//                .error(R.mipmap.s_user)
//                .bitmapTransform(new GlideCircleTransform(mContext))
//                .into(viewHolder.userAvatar);

    }

     class RvViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.cate_name)
        TextView cateName;

         RvViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
