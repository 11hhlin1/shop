package com.gjj.shop.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chuck on 2016/9/1.
 */
public class PaySuccessFragment extends BaseFragment {
    @Bind(R.id.pay_money)
    TextView payMoney;
    @Bind(R.id.pay_way)
    TextView payWay;
    @Bind(R.id.pay_order_num)
    TextView payOrderNum;
    @Bind(R.id.back_home)
    Button backHome;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_pay_success;
    }

    @Override
    public void initView() {

    }


    @OnClick(R.id.back_home)
    public void onClick() {
        getActivity().finish();
    }
}
