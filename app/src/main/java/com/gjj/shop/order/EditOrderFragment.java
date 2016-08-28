package com.gjj.shop.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjj.shop.R;
import com.gjj.shop.address.AddressFragment;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.base.PageSwitcher;

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

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_edit_order;
    }

    @Override
    public void initView() {

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
}
