package com.gjj.shop.person;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.gjj.applibrary.log.L;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrDefaultHandler;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

/**
 * Created by user on 16/8/27.
 */
public class MyCollectFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener,
        SlideView.OnSlideListener {

    @Bind(R.id.list)
    ListViewCompat mListView;
    @Bind(R.id.collect_layout)
    PtrClassicFrameLayout mCollectLayout;
    private SlideView mLastSlideViewWithStatusOn;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_my_collect;
    }

    @Override
    public void initView() {
        mCollectLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCollectLayout.refreshComplete();
                    }
                },1000);
            }
        });
        List<CollectInfo> list = new ArrayList<>();
        for (int i=0;i<10;i++) {
            list.add(new CollectInfo());
        }
        mListView.setAdapter(new CollectListAdapter(getActivity(), list, this));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.delete) {
            L.e("onClick v= " + v);
            ToastUtil.shortToast(R.string.delete);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onSlide(View view, int status) {
        if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
            mLastSlideViewWithStatusOn.shrink();
        }

        if (status == SLIDE_STATUS_ON) {
            mLastSlideViewWithStatusOn = (SlideView) view;
        }
    }
}
