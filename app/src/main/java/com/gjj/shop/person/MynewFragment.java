package com.gjj.shop.person;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.gjj.applibrary.http.callback.ListCallback;
import com.gjj.applibrary.http.model.BaseList;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.index.GridAdapter;
import com.gjj.shop.index.ProductListFragment;
import com.gjj.shop.model.ProductInfo;
import com.gjj.shop.net.ApiConstants;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import cn.finalteam.loadingviewfinal.GridViewFinal;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrDefaultHandler;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by user on 16/8/27.
 */
public class MynewFragment extends BaseFragment{
    @Bind(R.id.recyclerView)
    RecyclerViewFinal mRecyclerView;
    @Bind(R.id.news_layout)
    PtrClassicFrameLayout mPtrLayout;
    private NewListAdapter mAdapter;
    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_my_news;
    }

    @Override
    public void initView() {

        mAdapter = new NewListAdapter(getActivity(), new ArrayList<NewInfo>());
//        mRecyclerView.setEmptyView(mFlEmptyView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        Bundle bundle = getArguments();

        setSwipeRefreshInfo();
    }

    private void setSwipeRefreshInfo() {
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                requestData(mAdapter.getItemCount() - 1);
            }
        });

        mPtrLayout.setLastUpdateTimeRelateObject(this);
//        mPtrLayout.disableWhenHorizontalMove(true);
//        mPtrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                requestData(0);
//            }
//        });
        mPtrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                requestData(0);
            }
        });
        mPtrLayout.autoRefresh();
    }

    private void requestData(final int start) {
        HashMap<String, String> params = new HashMap<>();
        params.put("index", String.valueOf(start));
        params.put("size", String.valueOf(ProductListFragment.SIZE));
        OkHttpUtils.get(ApiConstants.MESSAGE_INFOLIST)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
                .execute(new ListCallback<NewInfo>(NewInfo.class) {
                    @Override
                    public void onSuccess(BaseList<NewInfo> productInfoBaseList, Call call, Response response) {
                        handleData(start,productInfoBaseList);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (start == 0) {
                                    mPtrLayout.refreshComplete();
                                } else {
                                    mRecyclerView.onLoadMoreComplete();
                                }
                                ToastUtil.shortToast(R.string.fail);
                            }
                        });
                    }
                });
    }

    private void handleData(int start, BaseList<NewInfo> baseList) {
        if (start == 0) {
            mPtrLayout.refreshComplete();
        } else {
            mRecyclerView.onLoadMoreComplete();
        }
        List<NewInfo> infoList = new ArrayList<NewInfo>();
        if (baseList != null) {
            infoList = baseList.list;
        }
        if (start == 0) {
            mAdapter.setData(infoList);
        } else {
            mAdapter.addData(infoList);
        }
        if (infoList.size() < ProductListFragment.SIZE) {
            mRecyclerView.setHasLoadMore(false);
        } else {
            mRecyclerView.setHasLoadMore(true);
        }
    }
}
