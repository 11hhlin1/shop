package com.gjj.shop.index;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gjj.applibrary.util.ToastUtil;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.model.ProductInfo;
import com.gjj.shop.widget.UnScrollableGridView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 16/8/7.
 */
public class ShopFragment extends BaseFragment {
    @Bind(R.id.shop_icon)
    ImageView shopIcon;
    @Bind(R.id.shop_name)
    TextView shopName;
    @Bind(R.id.contact_service)
    Button contactService;
    @Bind(R.id.shop_msg)
    TextView shopMsg;
    @Bind(R.id.gridView)
    UnScrollableGridView gridView;
    @Bind(R.id.icon_back_btn)
    ImageView iconBackBtn;
    @Bind(R.id.scrollView)
    ScrollView mScrollView;
    @OnClick(R.id.icon_back_btn)
    void back(){
        onBackPressed();
    }
    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_shop;
    }

    @Override
    public void initView() {
        ArrayList<ProductInfo> list = new ArrayList<ProductInfo>();
        for (int i = 0; i< 10; i++) {
            ProductInfo productInfo = new ProductInfo();
            productInfo.name = "的撒旦";
            productInfo.logo = "";
            list.add(productInfo);
        }
        GridAdapter gridAdapter = new GridAdapter(getActivity(), list);
        gridView.setAdapter(gridAdapter);
        mScrollView.fullScroll(ScrollView.FOCUS_UP);
        gridView.setFocusable(false);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.shortToast(view.getContext(), "product:"+position);
            }
        });
    }


}
