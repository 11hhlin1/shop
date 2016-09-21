package com.gjj.shop.index;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.gjj.applibrary.http.callback.ListCallback;
import com.gjj.applibrary.http.model.BaseList;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.base.SpaceItemDecoration;
import com.gjj.shop.community.CommunityAdapter;
import com.gjj.shop.community.CommunityInfo;
import com.gjj.shop.index.foreign.CategoryData;
import com.gjj.shop.model.ProductInfo;
import com.gjj.shop.net.ApiConstants;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.loadingviewfinal.GridViewFinal;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrDefaultHandler;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Chuck on 2016/7/26.
 */
public class ProductListFragment extends BaseFragment {

    public static final int SIZE = 20;
    @Bind(R.id.product_grid)
    GridViewFinal mProductGrid;
    @Bind(R.id.store_house_ptr_frame)
    PtrClassicFrameLayout mPtrLayout;
    private GridAdapter mAdapter;
//    CategoryData mCategoryData;
    private int mSortId;
    private int mType;
    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_product_list;
    }

    @Override
    public void initView() {

        mAdapter = new GridAdapter(getActivity(), new ArrayList<ProductInfo>());
//        mRecyclerView.setEmptyView(mFlEmptyView);
        mProductGrid.setAdapter(mAdapter);
        Bundle bundle = getArguments();
        mSortId = bundle.getInt("sortId");
        mType = bundle.getInt("type");
        setSwipeRefreshInfo();
    }

    private void setSwipeRefreshInfo() {
        mProductGrid.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                requestData(mAdapter.getCount() - 1);
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
        params.put("size", String.valueOf(SIZE));
        params.put("sortId", String.valueOf(mSortId));
        params.put("type", String.valueOf(mType));
        OkHttpUtils.get(ApiConstants.PRODUCT_LIST)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
                .execute(new ListCallback<ProductInfo>(ProductInfo.class) {
                    @Override
                    public void onSuccess(BaseList<ProductInfo> productInfoBaseList, Call call, Response response) {
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
                                    mProductGrid.onLoadMoreComplete();
                                }
                                ToastUtil.shortToast(R.string.fail);
                            }
                        });
                    }
                });
    }

    private void handleData(int start, BaseList<ProductInfo> baseList) {
        if (start == 0) {
            mPtrLayout.refreshComplete();
        } else {
            mProductGrid.onLoadMoreComplete();
        }
        List<ProductInfo> infoList = new ArrayList<ProductInfo>();
        if (baseList != null) {
            infoList = baseList.list;
        }
        if (start == 0) {
            mAdapter.setData(infoList);
        } else {
            mAdapter.addData(infoList);
        }
        if (infoList.size() < SIZE) {
            mProductGrid.setHasLoadMore(false);
        } else {
            mProductGrid.setHasLoadMore(true);
        }
    }

}
