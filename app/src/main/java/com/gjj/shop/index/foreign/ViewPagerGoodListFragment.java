package com.gjj.shop.index.foreign;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.widget.NavLineView;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by Chuck on 2016/9/5.
 */
public class ViewPagerGoodListFragment extends BaseFragment implements ViewPager.OnPageChangeListener{

    /**
     * viewPager
     */
    @Bind(R.id.page_vp)
    ViewPager mPageVp;

    /**
     * 分隔线
     */
    @Bind(R.id.tab_line_iv)
    NavLineView mNavLineView;

    @Bind(R.id.project_radio_group)
    LinearLayout mRadioGroup;

    private ViewPagerFragmentAdapter mFragmentAdapter;

    private Fragment[] mFragmentCache;
    private int mRedColor;
    private int mSecondaryGrayColor;
    private int mNavItemWidth;
    private ArrayList<CategoryData> mDataArrayList;
    private TextView mTextViews[];
    @Override
    public int getContentViewLayout() {
        return R.layout.view_pager_good_list;
    }

    @Override
    public void initView() {
        Resources res = getResources();
        mRedColor = res.getColor(R.color.color_EE394A);
        mSecondaryGrayColor = res.getColor(R.color.secondary_gray);

        Bundle bundle = getArguments();
        int firstCate = bundle.getInt("firstCate");
        mDataArrayList = bundle.getParcelableArrayList("data");
        assert mDataArrayList != null;
        int size = mDataArrayList.size();
        mFragmentCache = new Fragment[size];
        initTabLineWidth();

        ViewPager viewPager = mPageVp;
        viewPager.setCurrentItem(0);
        mTextViews = new TextView[size];
        for (int i = 0; i < size; i++) {
            TextView textView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.radio_btn,null);
//            TextView textView = new TextView(getContext());
            mTextViews[i] = textView;
            CategoryData categoryData = mDataArrayList.get(i);
            textView.setId(categoryData.id);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER);
            final int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPageVp.setCurrentItem(finalI);
                }
            });
            mRadioGroup.addView(textView);
            textView.setText(categoryData.name);
        }
        mTextViews[0].setTextColor(mRedColor);
        //mDoneRadioBtn.setTextColor(mSecondaryGrayColor);
        viewPager.setOffscreenPageLimit(mFragmentCache.length);
        viewPager.setOnPageChangeListener(this);
        mFragmentAdapter = new ViewPagerFragmentAdapter(getChildFragmentManager(), mFragmentCache, mDataArrayList, firstCate);
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
        for (int i =0 ; i< mTextViews.length; i++) {
            if(i == position) {
                mTextViews[i].setTextColor(mRedColor);
            } else {
                mTextViews[i].setTextColor(mSecondaryGrayColor);
            }
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
