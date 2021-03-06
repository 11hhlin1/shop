package com.gjj.shop.order;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.applibrary.http.callback.ListCallback;
import com.gjj.applibrary.http.model.BaseList;
import com.gjj.applibrary.log.L;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.applibrary.util.Util;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.base.PageSwitcher;
import com.gjj.shop.base.SpaceItemDecoration;
import com.gjj.shop.community.CommunityAdapter;
import com.gjj.shop.event.EventOfCancelOrder;
import com.gjj.shop.event.EventOfChangeTab;
import com.gjj.shop.event.EventOfCheckGoods;
import com.gjj.shop.net.ApiConstants;
import com.gjj.shop.shopping.CartDeleteReq;
import com.gjj.shop.shopping.GoodsAdapterInfo;
import com.gjj.shop.shopping.ShopAdapterInfo;
import com.gjj.shop.widget.ConfirmDialog;
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

    public OrderListFragment() {
    }
    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_order_list;
    }

    @Override
    public void initView() {
        mIndex = getArguments().getInt("index");
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
        EventBus.getDefault().register(this);
        mPtrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                requestData();
            }
        });
        mPtrLayout.autoRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(EventOfCheckGoods event) {
        if(getActivity() == null)
            return;
        if(mIndex == 1 || mIndex == 2) {
            requestData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(EventOfCancelOrder event) {
        if(getActivity() == null)
            return;
        if(mIndex == 0) {
            requestData();
        }
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
                                } else {
                                    mAdapter.setData(new ArrayList<OrderInfo>());
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
    public void cancelOrder(final int pos) {
        ConfirmDialog confirmDialog = new ConfirmDialog(getActivity(), R.style.white_bg_dialog);
        confirmDialog.setConfirmClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderInfo orderInfo = mAdapter.getData(pos);
                HashMap<String, String> params = new HashMap<>();
                params.put("orderId", orderInfo.orderId);
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
                                        requestData();
                                        ToastUtil.shortToast(getActivity(),"取消成功");
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
        });
        confirmDialog.setCanceledOnTouchOutside(true);
        confirmDialog.show();
        confirmDialog.setContent(R.string.cancel_order_tip);

    }

    @Override
    public void payOrder(int pos) {
        OrderInfo orderInfo = mAdapter.getData(pos);
        Bundle bundle = new Bundle();
        List<String> orderIdList = new ArrayList<>();
        orderIdList.add(orderInfo.orderId);
        bundle.putString("orderIds", JSONArray.toJSONString(orderIdList));
        PageSwitcher.switchToTopNavPage(getActivity(),ChoosePayWayFragment.class, bundle, getString(R.string.pay), "");


    }

    @Override
    public void AdviceOrder(int pos) {

    }

    @Override
    public void CheckGood(int pos) {
        OrderInfo orderInfo = mAdapter.getData(pos);
        HashMap<String, String> params = new HashMap<>();
        params.put("orderId", orderInfo.orderId);
        OkHttpUtils.post(ApiConstants.RECEIVE_ORDER)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
                .execute(new JsonCallback<String>(String.class) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                requestData();
                                ToastUtil.shortToast(getActivity(),"确认成功");
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
}
