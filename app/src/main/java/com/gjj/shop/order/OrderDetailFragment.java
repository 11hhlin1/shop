package com.gjj.shop.order;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gjj.applibrary.app.AppLib;
import com.gjj.applibrary.glide.GlideCircleTransform;
import com.gjj.applibrary.util.Util;
import com.gjj.shop.R;
import com.gjj.shop.address.AddressInfo;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.net.UrlUtil;
import com.gjj.shop.widget.UnScrollableListView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Chuck on 2016/9/27.
 */
public class OrderDetailFragment extends BaseFragment {

    @Bind(R.id.receive_person)
    TextView receivePerson;
    @Bind(R.id.address_detail)
    TextView addressDetail;
    @Bind(R.id.address_rl)
    RelativeLayout addressRl;
    @Bind(R.id.shop_avatar)
    ImageView shopAvatar;
    @Bind(R.id.shop_name)
    TextView shopName;
    @Bind(R.id.shop_icon_rl)
    RelativeLayout shopIconRl;
    @Bind(R.id.order_num)
    TextView orderNum;
    @Bind(R.id.order_state)
    TextView orderState;
    @Bind(R.id.order_num_rl)
    RelativeLayout orderNumRl;
    @Bind(R.id.good_list)
    UnScrollableListView goodList;
    @Bind(R.id.qq_ll)
    LinearLayout qqLl;
    @Bind(R.id.tel_ll)
    LinearLayout telLl;
    @Bind(R.id.contact_item)
    RelativeLayout contactItem;
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
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.sure_order)
    Button sureOrder;
    @Bind(R.id.order_left_btn)
    Button orderLeftBtn;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_order_detail;
    }

    @Override
    public void initView() {
         Bundle bundle = getArguments();
         OrderInfo orderInfo = bundle.getParcelable("orderInfo");
         assert orderInfo != null;
         AddressInfo addressInfo = orderInfo.address;
        if(addressInfo !=null) {
            StringBuilder phone = Util.getThreadSafeStringBuilder();
            phone.append("收货人:").append(addressInfo.contact).append("  ").append(addressInfo.phone);
            receivePerson.setText(phone.toString());
            StringBuilder address = Util.getThreadSafeStringBuilder();
            address.append(addressInfo.area).append(addressInfo.address);
            addressDetail.setText(address.toString());
        }



        Glide.with(this)
                .load(UrlUtil.getHttpUrl(orderInfo.shopLogoThumb))
                .centerCrop()
                .bitmapTransform(new GlideCircleTransform(getActivity()))
                .error(new ColorDrawable(AppLib.getResources().getColor(android.R.color.transparent)))
                .into(shopAvatar);
        shopName.setText(orderInfo.shopName);
        orderNum.setText(getString(R.string.order_num,orderInfo.orderId));
        switch (orderInfo.status) {
            case 0:
                orderState.setText(getString(R.string.pay_order));
                break;
            case 1:
                orderState.setText(getString(R.string.accepting_order));
                break;
            case 2:
                orderState.setText(getString(R.string.accept_order));
                break;
            case 3:
                orderState.setText(getString(R.string.cancel_order));
                break;
        }

        final GoodItemListAdapter listAdapter = new GoodItemListAdapter(getActivity(), orderInfo.goodsList,-1,"");
        goodList.setAdapter(listAdapter);
    }
}
