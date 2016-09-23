package com.gjj.shop.order;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gjj.applibrary.app.AppLib;
import com.gjj.applibrary.glide.GlideCircleTransform;
import com.gjj.applibrary.util.PreferencesManager;
import com.gjj.applibrary.util.Util;
import com.gjj.shop.R;
import com.gjj.shop.address.AddressFragment;
import com.gjj.shop.address.AddressInfo;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.base.PageSwitcher;
import com.gjj.shop.event.EventOfAddress;
import com.gjj.shop.event.EventOfDefaultAddress;
import com.gjj.shop.index.SelTagInfo;
import com.gjj.shop.model.ProductInfo;
import com.gjj.shop.net.UrlUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 16/8/27.
 */
public class EditOrderFragment extends BaseFragment {
    @Bind(R.id.receive_person)
    TextView receivePerson;
    @Bind(R.id.address_detail)
    TextView addressDetail;
    @Bind(R.id.address_contact_phone)
    TextView contactPhone;
    @Bind(R.id.address_rl)
    RelativeLayout addressRl;
    @Bind(R.id.shop_avatar)
    ImageView shopAvatar;
    @Bind(R.id.shop_name)
    TextView shopName;
    @Bind(R.id.shop_icon_rl)
    RelativeLayout shopIconRl;
    @Bind(R.id.product_avatar)
    ImageView productAvatar;
    @Bind(R.id.product_name)
    TextView productName;
    @Bind(R.id.product_desc)
    TextView productDesc;
    @Bind(R.id.product_price_new)
    TextView productPriceNew;
    @Bind(R.id.product_price_old)
    TextView productPriceOld;
    @Bind(R.id.product_amount)
    TextView productAmount;
    @Bind(R.id.shop_detail_rl)
    RelativeLayout shopDetailRl;
    @Bind(R.id.ali_pay_item)
    RelativeLayout aliPayItem;
    @Bind(R.id.weixin_pay_item)
    RelativeLayout weixinPayItem;
    @Bind(R.id.sure_order)
    Button sureOrder;

    private int mAmount;
    private ProductInfo mProductInfo;
    ArrayList<SelTagInfo> selTagInfoList;
    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_edit_order;
    }

    @Override
    public void initView() {
        AddressInfo addressInfo = (AddressInfo) PreferencesManager.getInstance().get(AddressInfo.class);

        setAddress(addressInfo);
        Bundle bundle = getArguments();
        mAmount = bundle.getInt("amount");

        mProductInfo = (ProductInfo) bundle.getSerializable("product");
        selTagInfoList = bundle.getParcelableArrayList("tagList");
        Activity activity = getActivity();
        Drawable drawable = new ColorDrawable(getResources().getColor(R.color.activity_bg_gray));
        Glide.with(activity)
                .load(UrlUtil.getHttpUrl(mProductInfo.logo))
                .centerCrop()
                .placeholder(drawable)
                .error(drawable)
                .into(productAvatar);
        shopName.setText(mProductInfo.shopName);
        shopName.setText(mProductInfo.shopName);
        Glide.with(activity)
                .load(UrlUtil.getHttpUrl(mProductInfo.shopThumb))
                .centerCrop()
                .bitmapTransform(new GlideCircleTransform(activity))
                .error(new ColorDrawable(AppLib.getResources().getColor(android.R.color.transparent)))
                .into(shopAvatar);
        productName.setText(mProductInfo.name);
        productPriceNew.setText(getString(R.string.money_has_mark, Util.getFormatData(mProductInfo.curPrice)));
        productPriceOld.setText(getString(R.string.money_has_mark, Util.getFormatData(mProductInfo.prePrice)));
        productAmount.setText("x" + mAmount);
        StringBuilder stringBuilder = Util.getThreadSafeStringBuilder();
        for (SelTagInfo selTagInfo: selTagInfoList) {
            stringBuilder.append(selTagInfo.mTitle).append("   ").append(selTagInfo.mSelTag).append("   ");
        }
        productDesc.setText(stringBuilder.toString());
        EventBus.getDefault().register(this);

    }


    @OnClick({R.id.address_rl, R.id.sure_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.address_rl:
                PageSwitcher.switchToTopNavPage(getActivity(),AddressFragment.class, null, getString(R.string.address), "");

                break;
            case R.id.sure_order:
                break;
        }
    }

    void setAddress(AddressInfo addressInfo) {
        if(addressInfo == null)
            return;
        receivePerson.setText(addressInfo.contact);
        StringBuilder address = Util.getThreadSafeStringBuilder();
        address.append(addressInfo.area).append(addressInfo.address);
        addressDetail.setText(address.toString());
        contactPhone.setText(addressInfo.phone);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateAddress(EventOfDefaultAddress event) {
//        setAddress();
        if(getActivity() == null) {
            return;
        }
        setAddress(event.mAddressInfo);
    }
}
