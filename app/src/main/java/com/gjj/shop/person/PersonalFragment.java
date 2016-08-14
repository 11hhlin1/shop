package com.gjj.shop.person;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.widget.DrawableCenterTextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chuck on 2016/7/21.
 */
public class PersonalFragment extends BaseFragment {

    @Bind(R.id.avatar)
    ImageView avatar;
    @Bind(R.id.avatar_item)
    RelativeLayout avatarItem;
    @Bind(R.id.my_order_item)
    RelativeLayout myOrderItem;
    @Bind(R.id.pay_order)
    DrawableCenterTextView payOrder;
    @Bind(R.id.accepting_order)
    DrawableCenterTextView acceptingOrder;
    @Bind(R.id.accept_order)
    DrawableCenterTextView acceptOrder;
    @Bind(R.id.cancel_order)
    DrawableCenterTextView cancelOrder;
    @Bind(R.id.after_sale_order)
    DrawableCenterTextView afterSaleOrder;
    @Bind(R.id.collect_item)
    RelativeLayout collectItem;
    @Bind(R.id.contact_item)
    RelativeLayout contactItem;
    @Bind(R.id.about_item)
    RelativeLayout aboutItem;
    @Bind(R.id.setting_item)
    RelativeLayout settingItem;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_person;
    }

    @Override
    public void initView() {
    }

    @OnClick({R.id.avatar_item, R.id.my_order_item, R.id.pay_order, R.id.accepting_order, R.id.accept_order, R.id.cancel_order, R.id.after_sale_order, R.id.collect_item, R.id.contact_item, R.id.about_item, R.id.setting_item})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avatar_item:
                break;
            case R.id.my_order_item:
                break;
            case R.id.pay_order:
                break;
            case R.id.accepting_order:
                break;
            case R.id.accept_order:
                break;
            case R.id.cancel_order:
                break;
            case R.id.after_sale_order:
                break;
            case R.id.collect_item:
                break;
            case R.id.contact_item:
                break;
            case R.id.about_item:
                break;
            case R.id.setting_item:
                break;
        }
    }
}
