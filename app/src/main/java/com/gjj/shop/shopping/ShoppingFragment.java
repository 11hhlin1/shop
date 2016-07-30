package com.gjj.shop.shopping;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by Chuck on 2016/7/21.
 */
public class ShoppingFragment extends BaseFragment {
    @Bind(R.id.tv_title)
    TextView mTitleTV;
    @Bind(R.id.recyclerView)
    RecyclerView mRecylerView;

    private ShoppingAdapter mAdapter;


    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_shopping;
    }

    @Override
    public void initView() {
        mTitleTV.setText(R.string.shopping);
        LinearLayoutManager staggeredGridLayoutManager = new LinearLayoutManager(getActivity());
        // 设置布局管理器
        mRecylerView.setLayoutManager(staggeredGridLayoutManager);
        mAdapter = new ShoppingAdapter(getActivity(), new ArrayList<ShopInfo>());
        mRecylerView.setAdapter(mAdapter);
    }
}
