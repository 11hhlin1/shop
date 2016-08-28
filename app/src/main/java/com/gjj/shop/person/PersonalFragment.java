package com.gjj.shop.person;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gjj.applibrary.glide.GlideCircleTransform;
import com.gjj.shop.R;
import com.gjj.shop.app.BaseApplication;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.base.PageSwitcher;
import com.gjj.shop.community.AddFeedFragment;
import com.gjj.shop.event.UpdateUserInfo;
import com.gjj.shop.index.ShopGridAdapter;
import com.gjj.shop.model.UserInfo;
import com.gjj.shop.net.UrlUtil;
import com.gjj.shop.order.OrderFragment;
import com.gjj.shop.util.CallUtil;
import com.gjj.shop.widget.DrawableCenterTextView;
import com.gjj.shop.widget.UnScrollableGridView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    @Bind(R.id.nick_name)
    TextView mNick;
//    @Bind(R.id.pay_order)
//    DrawableCenterTextView payOrder;
//    @Bind(R.id.accepting_order)
//    DrawableCenterTextView acceptingOrder;
//    @Bind(R.id.accept_order)
//    DrawableCenterTextView acceptOrder;
//    @Bind(R.id.cancel_order)
//    DrawableCenterTextView cancelOrder;
//    @Bind(R.id.after_sale_order)
//    DrawableCenterTextView afterSaleOrder;
    @Bind(R.id.collect_item)
    RelativeLayout collectItem;
    @Bind(R.id.contact_item)
    RelativeLayout contactItem;
    @Bind(R.id.about_item)
    RelativeLayout aboutItem;
    @Bind(R.id.setting_item)
    RelativeLayout settingItem;

    @Bind(R.id.order_grid)
    UnScrollableGridView gridView;
    private int[] mIcons = {R.mipmap.u_icon_01,R.mipmap.u_icon_02,R.mipmap.u_icon_03
    ,R.mipmap.u_icon_04,R.mipmap.u_icon_05};

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_person;
    }

    @Override
    public void initView() {
        String[] mNames = new String[5];
        mNames[0]  = getString(R.string.pay_order);
        mNames[1]  = getString(R.string.accepting_order);
        mNames[2]  = getString(R.string.accept_order);
        mNames[3]  = getString(R.string.cancel_order);
        mNames[4]  = getString(R.string.after_sale_order);
        OrderGridAdapter shopGridAdapter = new OrderGridAdapter(getActivity(), mNames,mIcons);
        gridView.setAdapter(shopGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putInt("index", i);
                PageSwitcher.switchToTopNavPage(getActivity(),OrderFragment.class,bundle,getString(R.string.my_order),null);
            }
        });

        UserInfo userInfo = BaseApplication.getUserMgr().getUser();
        if(userInfo != null) {
            setUserInfo(userInfo);
        }

        EventBus.getDefault().register(this);
//
    }

    @OnClick({R.id.avatar_item, R.id.my_order_item, R.id.collect_item, R.id.contact_item, R.id.about_item, R.id.setting_item})
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.avatar_item:
                PageSwitcher.switchToTopNavPage(getActivity(),PersonalInfoFragment.class,null,getString(R.string.person_info),null);
                break;
            case R.id.my_order_item:
                bundle.putInt("index", 0);
                PageSwitcher.switchToTopNavPage(getActivity(),OrderFragment.class,bundle,getString(R.string.my_order),null);
                break;
            case R.id.collect_item:
                break;
            case R.id.contact_item:
                CallUtil.askForMakeCall(getActivity(),"","400-82838838888");
                break;
            case R.id.about_item:
                PageSwitcher.switchToTopNavPage(getActivity(),AboutFragment.class,bundle,getString(R.string.about_us),null);
                break;
            case R.id.setting_item:
                PageSwitcher.switchToTopNavPage(getActivity(),SettingFragment.class,bundle,getString(R.string.setting),null);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(UpdateUserInfo event) {
        UserInfo userInfo = BaseApplication.getUserMgr().getUser();
        setUserInfo(userInfo);
    }

    void setUserInfo(UserInfo userInfo){
        Glide.with(getActivity())
                .load(UrlUtil.getHttpUrl(userInfo.getAvatar()))
                .centerCrop()
                .placeholder(R.mipmap.s_user)
                .error(R.mipmap.s_user)
                .bitmapTransform(new GlideCircleTransform(getActivity()))
                .into(avatar);
        mNick.setText(userInfo.nickname);
    }
}
