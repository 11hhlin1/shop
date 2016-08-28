package com.gjj.shop.address;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjj.shop.R;
import com.gjj.shop.base.BaseRecyclerViewAdapter;
import com.gjj.shop.base.PageSwitcher;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 16/8/27.
 */
public class AddressListAdapter extends BaseRecyclerViewAdapter<AddressInfo> {

    private Context mContext;
    private LayoutInflater mInflater;
//    private List<CommunityInfo> mInfoList;

    public AddressListAdapter(Context context, List<AddressInfo> infoList) {
        super(context, infoList);
        mContext = context;
        mInflater = LayoutInflater.from(context);
//        mInfoList = infoList;
    }

    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.address_item, parent, false);
        return new RvViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RvViewHolder viewHolder = (RvViewHolder) holder;
        AddressInfo info = items.get(position);

    }


     class RvViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.receive_person)
        TextView receivePerson;
        @Bind(R.id.address_detail)
        TextView addressDetail;
        @Bind(R.id.address_rl)
        RelativeLayout addressRl;

        RvViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PageSwitcher.switchToTopNavPage((Activity) mContext,changeAddressFragment.class, null, mContext.getString(R.string.change_address), "");

                }
            });
        }
    }
}
