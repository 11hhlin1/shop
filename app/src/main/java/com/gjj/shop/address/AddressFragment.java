package com.gjj.shop.address;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.base.PageSwitcher;
import com.gjj.shop.community.CommunityAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 16/8/27.
 */
public class AddressFragment extends BaseFragment {
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.add_address_btn)
    Button addAddressBtn;

    @OnClick(R.id.add_address_btn)
    void setAddAddressBtn(){
        PageSwitcher.switchToTopNavPage(getActivity(),AddAddressFragment.class, null, getString(R.string.add_address), getString(R.string.save));

    }
    private AddressListAdapter mAdapter;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_address;
    }

    @Override
    public void initView() {
        List<AddressInfo> infos = new ArrayList<>();
        mAdapter = new AddressListAdapter(getContext(), infos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
//        mRecyclerView.setEmptyView(mFlEmptyView);
        mRecyclerView.setAdapter(mAdapter);

    }


}
