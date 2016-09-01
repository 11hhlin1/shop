package com.gjj.shop.search;

import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.index.GridAdapter;

import butterknife.Bind;
import butterknife.OnClick;
import cn.finalteam.loadingviewfinal.GridViewFinal;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;

/**
 * Created by Chuck on 2016/9/1.
 */
public class SearchFragment extends BaseFragment {
    private static final int SIZE = 20;
    @Bind(R.id.product_grid)
    GridViewFinal mProductGrid;
    @Bind(R.id.store_house_ptr_frame)
    PtrClassicFrameLayout mPtrLayout;
    private GridAdapter mAdapter;

    @OnClick(R.id.back_icon)
    void back() {
        onBackPressed();
    }
    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_search;
    }

    @Override
    public void initView() {

    }
}
