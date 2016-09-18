package com.gjj.shop.category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gjj.applibrary.http.callback.ListCallback;
import com.gjj.applibrary.http.model.BaseList;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseRecyclerViewAdapter;
import com.gjj.shop.base.RecyclerItemOnclickListener;
import com.gjj.shop.index.CommentInfo;
import com.gjj.shop.net.ApiConstants;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Chuck on 2016/9/1.
 */
public class LeftAdapter extends BaseRecyclerViewAdapter<CategoryInfo> {
    private Context mContext;
    private LayoutInflater mInflater;
//    private List<CommunityInfo> mInfoList;

    private RecyclerItemOnclickListener recyclerItemOnclickListener;


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
        viewHolder.cateName.setText(info.name);
        viewHolder.cateName.setTag(position);
        if(position == 0) {
            viewHolder.cateName.setChecked(true);
        }
//        Glide.with(mContext)
//                .load(UrlUtil.getHttpUrl(info.thumbAvatar))
//                .centerCrop()
//                .placeholder(R.mipmap.s_user)
//                .error(R.mipmap.s_user)
//                .bitmapTransform(new GlideCircleTransform(mContext))
//                .into(viewHolder.userAvatar);

    }

    public void setRecyclerItemOnclickListener(RecyclerItemOnclickListener recyclerItemOnclickListener) {
        this.recyclerItemOnclickListener = recyclerItemOnclickListener;
    }

    class RvViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.cate_name)
        RadioButton cateName;

         RvViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
             cateName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                     if(isChecked) {
                         int pos = (int) cateName.getTag();
                         recyclerItemOnclickListener.onItemClick(view,pos);

                     }
                 }
             });
//             view.setOnClickListener(new View.OnClickListener() {
//                 @Override
//                 public void onClick(View v) {
//                     int pos = (int) cateName.getTag();
//                 }
//             });
        }
    }
}
