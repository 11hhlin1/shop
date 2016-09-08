package com.gjj.shop.address;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjj.applibrary.util.PreferencesManager;
import com.gjj.applibrary.util.Util;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseRecyclerViewAdapter;
import com.gjj.shop.base.PageSwitcher;
import com.gjj.shop.base.RecyclerItemOnclickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 16/8/27.
 */
public class AddressListAdapter extends BaseRecyclerViewAdapter<AddressInfo> {

    private Context mContext;
    private LayoutInflater mInflater;

    public void setRecyclerItemOnclickListener(RecyclerItemOnclickListener recyclerItemOnclickListener) {
        this.recyclerItemOnclickListener = recyclerItemOnclickListener;
    }

    //    private List<CommunityInfo> mInfoList;
    private RecyclerItemOnclickListener recyclerItemOnclickListener;

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
        viewHolder.addressContact.setText(info.phone);
        viewHolder.receivePerson.setText(info.contact);
        viewHolder.receivePerson.setTag(position);
        viewHolder.addressRl.setTag(info);
        StringBuilder address = Util.getThreadSafeStringBuilder();
        long defaultAddressId = PreferencesManager.getInstance().get("defaultAddressId",0L);
        if(defaultAddressId == info.addressId) {
            address.append("[默认]");
            address.append(info.area).append(info.address);
            SpannableString spannableInfo = new SpannableString(address.toString());

            ForegroundColorSpan blue = new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_ed394a));
            spannableInfo.setSpan(blue, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.addressDetail.setText(spannableInfo);
        } else {
            address.append(info.area).append(info.address);
            viewHolder.addressDetail.setText(address.toString());
        }

    }


     class RvViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.receive_person)
        TextView receivePerson;
         @Bind(R.id.address_contact)
         TextView addressContact;
        @Bind(R.id.address_detail)
        TextView addressDetail;
         @Bind(R.id.change_address_btn)
         Button changeAddressBtn;
        @Bind(R.id.address_rl)
        RelativeLayout addressRl;

         @OnClick(R.id.change_address_btn)
         void change(){
             AddressInfo addressInfo = (AddressInfo) addressRl.getTag();
             Bundle bundle = new Bundle();
             bundle.putSerializable("addressInfo", addressInfo);
             PageSwitcher.switchToTopNavPage((Activity) mContext,changeAddressFragment.class, bundle, mContext.getString(R.string.change_address), mContext.getString(R.string.save));
         }

        RvViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) receivePerson.getTag();
                    recyclerItemOnclickListener.onItemClick(v, pos);

                }
            });
        }
    }
}
