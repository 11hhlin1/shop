package com.gjj.shop.order;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.applibrary.http.callback.ListCallback;
import com.gjj.applibrary.http.model.BaseList;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.applibrary.util.Util;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.base.SpaceItemDecoration;
import com.gjj.shop.community.CommunityAdapter;
import com.gjj.shop.net.ApiConstants;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrDefaultHandler;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/14.
 */
public class OrderListFragment extends BaseFragment implements OrderListAdapter.BtnCallBack {
    @Bind(R.id.order_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.store_house_ptr_frame)
    PtrClassicFrameLayout mPtrLayout;
    private int mIndex;
    private OrderListAdapter mAdapter;

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

        mAdapter = new OrderListAdapter(getContext(), new ArrayList<OrderInfo>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
//        mRecyclerView.setEmptyView(mFlEmptyView);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setmBtnCallBack(this);
//        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin_20p);
//        mRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        mRecyclerView.setItemAnimator(null);
        mPtrLayout.setLastUpdateTimeRelateObject(this);

        mPtrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                requestData();
            }
        });
        mPtrLayout.autoRefresh();
    }

    private void requestData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("status", String.valueOf(mIndex));
        OkHttpUtils.get(ApiConstants.ORDER_LIST)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
                .execute(new ListCallback<OrderInfo>(OrderInfo.class) {

                    @Override
                    public void onSuccess(final BaseList<OrderInfo> orderInfoBaseList, Call call, Response response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mPtrLayout.refreshComplete();
                                if (orderInfoBaseList != null && !Util.isListEmpty(orderInfoBaseList.list)) {
                                    mAdapter.setData(orderInfoBaseList.list);
                                }
                                ToastUtil.shortToast(R.string.success);
                            }
                        });
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.shortToast(R.string.fail);
                                mPtrLayout.refreshComplete();
                            }
                        });

                    }
                });
    }

    @Override
    public void cancelOrder(int pos) {
        OrderInfo orderInfo = mAdapter.getData(pos);
        HashMap<String, String> params = new HashMap<>();
        params.put("orderId", String.valueOf(orderInfo.orderId));
        OkHttpUtils.post(ApiConstants.CANCEL_ORDER)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
                .execute(new JsonCallback<String>(String.class) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.shortToast(R.string.success);
                            }
                        });
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.shortToast(R.string.fail);
                            }
                        });

                    }
                });
    }

    @Override
    public void payOrder(int pos) {

    }

    @Override
    public void AdviceOrder(int pos) {

    }

    @Override
    public void CheckGood(int pos) {

    }
}
