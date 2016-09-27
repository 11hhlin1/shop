package com.gjj.shop.order;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.index.cheap.CheapFragmentAdapter;
import com.gjj.shop.widget.NavLineView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/8/14.
 */
public class OrderFragment extends BaseFragment implements ViewPager.OnPageChangeListener{


    @Bind(R.id.pay_order_btn)
    TextView payOrderBtn;
    @Bind(R.id.accepting_order_btn)
    TextView acceptingOrderBtn;
    @Bind(R.id.accept_order_btn)
    TextView acceptOrderBtn;
    @Bind(R.id.cancel_order_btn)
    TextView cancelOrderBtn;
//    @Bind(R.id.after_sale_order_btn)
//    TextView afterSaleOrderBtn;
    @Bind(R.id.project_radio_group)
    LinearLayout projectRadioGroup;
    @Bind(R.id.bottom_line)
    View bottomLine;
    @Bind(R.id.tab_line_iv)
    NavLineView mNavLineView;
    @Bind(R.id.top_rl)
    RelativeLayout topRl;
    @Bind(R.id.page_vp)
    ViewPager mPageVp;
    private OrderFragmentAdapter mFragmentAdapter;

    private Fragment[] mFragmentCache;
    private int mRedColor;
    private int mSecondaryGrayColor;
    private int mNavItemWidth;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_order;
    }

    @Override
    public void initView() {
        Resources res = getResources();
        mRedColor = res.getColor(R.color.color_EE394A);
        mSecondaryGrayColor = res.getColor(R.color.secondary_gray);

        mFragmentCache = new Fragment[4];
        initTabLineWidth();
        ViewPager viewPager = mPageVp;
        int index = getArguments().getInt("index");
        payOrderBtn.setTextColor(mRedColor);
        //mDoneRadioBtn.setTextColor(mSecondaryGrayColor);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setOnPageChangeListener(this);
        mFragmentAdapter = new OrderFragmentAdapter(getChildFragmentManager(), mFragmentCache);
        mPageVp.setAdapter(mFragmentAdapter);
        viewPager.setCurrentItem(index);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        float left = mNavItemWidth * (position + positionOffset);
        NavLineView navLineView = mNavLineView;
        navLineView.setNavLeft(left);
        navLineView.setNavRight(left + mNavItemWidth);
        navLineView.invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                acceptOrderBtn.setTextColor(mSecondaryGrayColor);
//                afterSaleOrderBtn.setTextColor(mSecondaryGrayColor);
                cancelOrderBtn.setTextColor(mSecondaryGrayColor);
                acceptingOrderBtn.setTextColor(mSecondaryGrayColor);
                payOrderBtn.setTextColor(mRedColor);
                break;
            case 1:
                acceptOrderBtn.setTextColor(mSecondaryGrayColor);
//                afterSaleOrderBtn.setTextColor(mSecondaryGrayColor);
                cancelOrderBtn.setTextColor(mSecondaryGrayColor);
                payOrderBtn.setTextColor(mSecondaryGrayColor);
                acceptingOrderBtn.setTextColor(mRedColor);

                break;
            case 2:
                cancelOrderBtn.setTextColor(mSecondaryGrayColor);
//                afterSaleOrderBtn.setTextColor(mSecondaryGrayColor);
                acceptingOrderBtn.setTextColor(mSecondaryGrayColor);
                payOrderBtn.setTextColor(mSecondaryGrayColor);
                acceptOrderBtn.setTextColor(mRedColor);
                break;
            case 3:
                acceptOrderBtn.setTextColor(mSecondaryGrayColor);
//                afterSaleOrderBtn.setTextColor(mSecondaryGrayColor);
                acceptingOrderBtn.setTextColor(mSecondaryGrayColor);
                payOrderBtn.setTextColor(mSecondaryGrayColor);
                cancelOrderBtn.setTextColor(mRedColor);
                break;
//            case 4:
//                acceptOrderBtn.setTextColor(mSecondaryGrayColor);
////                afterSaleOrderBtn.setTextColor(mSecondaryGrayColor);
//                acceptingOrderBtn.setTextColor(mSecondaryGrayColor);
//                payOrderBtn.setTextColor(mSecondaryGrayColor);
//                cancelOrderBtn.setTextColor(mRedColor);
//                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 设置滑动条的宽度为屏幕的1/mFragmentList.size();(根据Tab的个数而定)
     */
    private void initTabLineWidth() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mNavItemWidth = dm.widthPixels / mFragmentCache.length;
    }

    @Override
    public void onTitleBtnClick() {
        BaseFragment fragment = (BaseFragment) mFragmentAdapter.getItem(mPageVp.getCurrentItem());
        fragment.onTitleBtnClick();
    }


    @OnClick({R.id.pay_order_btn, R.id.accepting_order_btn, R.id.accept_order_btn, R.id.cancel_order_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_order_btn:
                mPageVp.setCurrentItem(0);
                break;
            case R.id.accepting_order_btn:
                mPageVp.setCurrentItem(1);
                break;
            case R.id.accept_order_btn:
                mPageVp.setCurrentItem(2);
                break;
            case R.id.cancel_order_btn:
                mPageVp.setCurrentItem(3);
                break;
//            case R.id.after_sale_order_btn:
//                mPageVp.setCurrentItem(4);
//                break;
        }
    }
}
