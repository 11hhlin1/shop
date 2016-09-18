package com.gjj.shop.category;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;

import com.gjj.applibrary.http.callback.ListCallback;
import com.gjj.applibrary.http.model.BaseList;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.base.RecyclerItemOnclickListener;
import com.gjj.shop.net.ApiConstants;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Chuck on 2016/8/26.
 */
public class ProductCategoryFragment extends BaseFragment implements RecyclerItemOnclickListener{
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.grid)
    GridView mGridView;
    LeftAdapter mAdapter;
    RightGridAdapter mGridViewAdapter;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_product_cate;
    }

    @Override
    public void initView() {
        mAdapter = new LeftAdapter(getContext(), new ArrayList<CategoryInfo>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
//        mRecyclerView.setEmptyView(mFlEmptyView);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setRecyclerItemOnclickListener(this);


        mGridViewAdapter = new RightGridAdapter(getActivity(),new ArrayList<NextCategoryInfo>());
        mGridView.setAdapter(mGridViewAdapter);
        showLoadingDialog(R.string.request,false);
        HashMap<String, String> params = new HashMap<>();
        params.put("pid", String.valueOf(0));
        OkHttpUtils.get(ApiConstants.SORT_LEFT_LIST)
                .tag(this)
                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .params(params)
                .execute(new ListCallback<CategoryInfo>(CategoryInfo.class) {
                    @Override
                    public void onSuccess(final BaseList<CategoryInfo> categoryInfoBaseList, Call call, Response response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissLoadingDialog();
                                mAdapter.setData(categoryInfoBaseList.list);
                            }
                        });
                    }

                    @Override
                    public void onCacheSuccess(BaseList<CategoryInfo> categoryInfoBaseList, Call call) {
                        super.onCacheSuccess(categoryInfoBaseList, call);
//                        handleData(start,communityInfoBaseList);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissLoadingDialog();
                                ToastUtil.shortToast(R.string.fail);
                            }
                        });

                    }

                });
    }


    @Override
    public void onItemClick(View view, int position) {
        showLoadingDialog(R.string.request,false);

        HashMap<String, String> params = new HashMap<>();
        params.put("pid", String.valueOf(mAdapter.getData(position).id));
        OkHttpUtils.get(ApiConstants.SORT_LEFT_LIST)
                .tag(this)
                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .params(params)
                .execute(new ListCallback<NextCategoryInfo>(NextCategoryInfo.class) {
                    @Override
                    public void onSuccess(final BaseList<NextCategoryInfo> categoryInfoBaseList, Call call, Response response) {
//                        handleData(start,communityInfoBaseList);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissLoadingDialog();
                                mGridViewAdapter.setData(categoryInfoBaseList.list);
                            }
                        });
                    }

                    @Override
                    public void onCacheSuccess(BaseList<NextCategoryInfo> nextCategoryInfoBaseList, Call call) {
                        super.onCacheSuccess(nextCategoryInfoBaseList, call);
//                        handleData(start,communityInfoBaseList);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissLoadingDialog();
                                ToastUtil.shortToast(R.string.fail);
                            }
                        });

                    }

                });
    }
}
