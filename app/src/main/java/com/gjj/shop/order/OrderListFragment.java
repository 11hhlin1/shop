package com.gjj.shop.order;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrDefaultHandler;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;

/**
 * Created by Administrator on 2016/8/14.
 */
public class OrderListFragment extends BaseFragment {
    @Bind(R.id.order_list)
    RecyclerView orderList;
    @Bind(R.id.store_house_ptr_frame)
    PtrClassicFrameLayout mPtrFrame;
    private int mIndex;

    @SuppressLint("ValidFragment")
    public OrderListFragment(int index) {
        mIndex = index;
    }
    public OrderListFragment() {
    }
    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_order_list;
    }

    @Override
    public void initView() {
//        mPtrFrame.setOnRefreshListener(new OnDefaultRefreshListener() {
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mPtrFrame.onRefreshComplete();
//                    }
//                }, 200);
//            }
//        });

        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPtrFrame.refreshComplete();
            }
        });
    }

}
