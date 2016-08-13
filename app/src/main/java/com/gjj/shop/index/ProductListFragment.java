package com.gjj.shop.index;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;

/**
 * Created by Chuck on 2016/7/26.
 */
public class ProductListFragment extends BaseFragment {


    @Bind(R.id.product_grid)
    GridView mProductGrid;
    @Bind(R.id.store_house_ptr_frame)
    PtrClassicFrameLayout mPtrFrame;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_product_list;
    }

    @Override
    public void initView() {
        mPtrFrame.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.onRefreshComplete();
                    }
                }, 200);
            }
        });
    }


}
