package com.gjj.shop.index.cheap;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.widget.NavLineView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Chuck on 2016/8/1.
 */
public class CheapShopListFragment extends BaseFragment implements ViewPager.OnPageChangeListener{

    /**
     * viewPager
     */
    @Bind(R.id.page_vp)
    ViewPager mPageVp;

    @Bind(R.id.one_radio_btn)
    TextView mOneBuyBtn;

    @Bind(R.id.nine_radio_btn)
    TextView mNineBuyBtn;

    @Bind(R.id.start_radio_btn)
    TextView mStartUpBtn;
    /**
     * 分隔线
     */
    @Bind(R.id.tab_line_iv)
    NavLineView mNavLineView;
    @OnClick(R.id.one_radio_btn)
    void oneBuy() {
        mPageVp.setCurrentItem(0);
    }

    @OnClick(R.id.nine_radio_btn)
    void nineBuy() {
        mPageVp.setCurrentItem(1);
    }

    @OnClick(R.id.start_radio_btn)
    void StartBuy() {
        mPageVp.setCurrentItem(2);
    }

    private CheapFragmentAdapter mFragmentAdapter;

    private Fragment[] mFragmentCache;
    private int mRedColor;
    private int mSecondaryGrayColor;
    private int mNavItemWidth;
    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_cheap_shop;
    }

    @Override
    public void initView() {
        Resources res = getResources();
        mRedColor = res.getColor(R.color.color_EE394A);
        mSecondaryGrayColor = res.getColor(R.color.secondary_gray);

        mFragmentCache = new Fragment[3];
        initTabLineWidth();
        ViewPager viewPager = mPageVp;
        viewPager.setCurrentItem(0);
        mOneBuyBtn.setTextColor(mRedColor);
        //mDoneRadioBtn.setTextColor(mSecondaryGrayColor);
        viewPager.setOffscreenPageLimit(mFragmentCache.length);
        viewPager.setOnPageChangeListener(this);
        mFragmentAdapter = new CheapFragmentAdapter(getChildFragmentManager(), mFragmentCache);
        mPageVp.setAdapter(mFragmentAdapter);
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
                mStartUpBtn.setTextColor(mSecondaryGrayColor);
                mNineBuyBtn.setTextColor(mSecondaryGrayColor);
                mOneBuyBtn.setTextColor(mRedColor);
                break;
            case 1:
                mStartUpBtn.setTextColor(mSecondaryGrayColor);
                mOneBuyBtn.setTextColor(mSecondaryGrayColor);
                mNineBuyBtn.setTextColor(mRedColor);
                break;
            case 2:
                mNineBuyBtn.setTextColor(mSecondaryGrayColor);
                mOneBuyBtn.setTextColor(mSecondaryGrayColor);
                mStartUpBtn.setTextColor(mRedColor);
                break;
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
}
