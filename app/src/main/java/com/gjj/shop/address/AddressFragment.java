package com.gjj.shop.address;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.applibrary.http.callback.ListCallback;
import com.gjj.applibrary.http.model.BaseList;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.shop.R;
import com.gjj.shop.app.BaseApplication;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.base.PageSwitcher;
import com.gjj.shop.community.CommunityAdapter;
import com.gjj.shop.event.EventOfAddress;
import com.gjj.shop.event.UpdateUserInfo;
import com.gjj.shop.model.UserInfo;
import com.gjj.shop.net.ApiConstants;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

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
        doRequest();
        EventBus.getDefault().register(this);

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateAddress(EventOfAddress event) {
        doRequest();
    }
    private void doRequest() {
        showLoadingDialog(R.string.request,false);
        OkHttpUtils.post(ApiConstants.GET_ADDRESS_LIST)
                .tag(this)
                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .execute(new ListCallback<AddressInfo>(AddressInfo.class) {

                    @Override
                    public void onSuccess(final BaseList<AddressInfo> addressInfoBaseList, Call call, Response response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissLoadingDialog();
                                mAdapter.setData(addressInfoBaseList.list);
                            }
                        });
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissLoadingDialog();

                            }
                        });
                    }
                });
    }

}
