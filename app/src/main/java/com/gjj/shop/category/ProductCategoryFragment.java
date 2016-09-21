package com.gjj.shop.category;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gjj.applibrary.http.callback.ListCallback;
import com.gjj.applibrary.http.model.BaseList;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.base.PageSwitcher;
import com.gjj.shop.base.RecyclerItemOnclickListener;
import com.gjj.shop.index.ProductListFragment;
import com.gjj.shop.index.foreign.CategoryData;
import com.gjj.shop.index.foreign.ViewPagerGoodListFragment;
import com.gjj.shop.net.ApiConstants;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private LeftAdapter mAdapter;
    private RightGridAdapter mGridViewAdapter;

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
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setRecyclerItemOnclickListener(this);


        mGridViewAdapter = new RightGridAdapter(getActivity(),new ArrayList<NextCategoryInfo>());
        mGridView.setAdapter(mGridViewAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("sortId", mGridViewAdapter.getItem(position).id);
                bundle.putInt("type", 1);
                PageSwitcher.switchToTopNavPage(getActivity(),ProductListFragment.class,bundle,mGridViewAdapter.getItem(position).name,"");
            }
        });

        showLoadingDialog(R.string.request,false);
        HashMap<String, String> params = new HashMap<>();
        params.put("pid", String.valueOf(0));
        params.put("type", String.valueOf(1));
        OkHttpUtils.get(ApiConstants.SORT_LEFT_LIST)
                .tag(this)
                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .params(params)
                .execute(new ListCallback<CategoryInfo>(CategoryInfo.class) {
                    @Override
                    public void onSuccess(final BaseList<CategoryInfo> categoryInfoBaseList, Call call, Response response) {
                        handleLeftData(categoryInfoBaseList.list);
                        onItemClick(mGridView.getChildAt(0),0);
                    }

                    @Override
                    public void onCacheSuccess(final BaseList<CategoryInfo> categoryInfoBaseList, Call call) {
                        super.onCacheSuccess(categoryInfoBaseList, call);
                        handleLeftData(categoryInfoBaseList.list);

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


    private void handleLeftData(final List<CategoryInfo> list){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissLoadingDialog();
                mAdapter.setData(list);
            }
        });
    }
    @Override
    public void onItemClick(View view, int position) {
        showLoadingDialog(R.string.request,false);

        HashMap<String, String> params = new HashMap<>();
        params.put("pid", String.valueOf(mAdapter.getData(position).id));
        params.put("type", String.valueOf(1));
        OkHttpUtils.get(ApiConstants.SORT_LEFT_LIST)
                .tag(this)
                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .params(params)
                .execute(new ListCallback<NextCategoryInfo>(NextCategoryInfo.class) {
                    @Override
                    public void onSuccess(final BaseList<NextCategoryInfo> categoryInfoBaseList, Call call, Response response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissLoadingDialog();
                                mGridViewAdapter.setData(categoryInfoBaseList.list);
                            }
                        });
                    }

                    @Override
                    public void onCacheSuccess(final BaseList<NextCategoryInfo> nextCategoryInfoBaseList, Call call) {
                        super.onCacheSuccess(nextCategoryInfoBaseList, call);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissLoadingDialog();
                                mGridViewAdapter.setData(nextCategoryInfoBaseList.list);
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
                                ToastUtil.shortToast(R.string.fail);
                            }
                        });

                    }

                });
    }


}
