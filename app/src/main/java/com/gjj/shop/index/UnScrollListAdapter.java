package com.gjj.shop.index;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gjj.shop.R;
import com.gjj.shop.event.EventOfUpdateTags;
import com.gjj.shop.net.UrlUtil;
import com.gjj.shop.photo.PhotoData;
import com.gjj.shop.photo.PhotoViewActivity;
import com.gjj.shop.widget.UnScrollableGridView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 16/9/4.
 */
public class UnScrollListAdapter extends BaseAdapter {
    private List<TagInfo> mLists;
    private Context mContext;

    public UnScrollListAdapter(Context context, List<TagInfo>  list) {
        mContext = context;
        mLists = list;
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.pop_list_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TagInfo tagInfo = mLists.get(position);
        viewHolder.tagTitle.setText(tagInfo.mTitle);
        if(viewHolder.mGridView.getTag() == null) {
            GridAdapter adapter = new GridAdapter(mContext, tagInfo.mList,position);
            viewHolder.mGridView.setAdapter(adapter);
            viewHolder.mGridView.setTag(adapter);

        } else {
            GridAdapter adapter = (GridAdapter) viewHolder.mGridView.getTag();
            viewHolder.mGridView.setAdapter(adapter);
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tag_title)
        TextView tagTitle;
        @Bind(R.id.tag_grid)
        UnScrollableGridView mGridView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    class GridAdapter extends BaseAdapter {
        private List<String> mTagValues = new ArrayList<String>();
        private LayoutInflater mInflater;
        private Context mContext;
        private int mPosition;

        public GridAdapter(Context context, List<String> imageList,int pos) {
            mContext = context;
            mTagValues = imageList;
            mInflater = LayoutInflater.from(context);
            mPosition = pos;
        }

        @Override
        public int getCount() {
            return mTagValues.size();
        }

        @Override
        public Object getItem(int position) {
            return mTagValues.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ItemViewTag viewTag;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.textview_item, null);
                viewTag = new ItemViewTag(convertView) ;
                convertView.setTag(viewTag);
            } else {
                viewTag = (ItemViewTag) convertView.getTag();
            }
            viewTag.textView.setTag(R.id.list_index, position);
            viewTag.textView.setText(mTagValues.get(position));
            if(mLists.get(mPosition).mIndex == position) {
                viewTag.textView.setTextColor(mContext.getResources().getColor(R.color.white));
                viewTag.textView.setBackgroundResource(R.drawable.rect_red_bg);
            } else {
                viewTag.textView.setTextColor(mContext.getResources().getColor(R.color.color_666666));
                viewTag.textView.setBackgroundResource(R.drawable.rect_white_has_storke);
            }
            return convertView;
        }

        class ItemViewTag {
            @Bind(R.id.textView)
            TextView textView;

            public ItemViewTag(final View itemView) {
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) textView.getTag(R.id.list_index);
                        mLists.get(mPosition).mIndex = position;
                        mLists.get(mPosition).mSelTag = mTagValues.get(position);
                        EventBus.getDefault().post(new EventOfUpdateTags());
                        notifyDataSetChanged();
                    }
                });
            }
        }

    }


}
