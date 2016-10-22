package com.gjj.shop.search;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.gjj.applibrary.http.callback.ListCallback;
import com.gjj.applibrary.http.model.BaseList;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.index.GridAdapter;
import com.gjj.shop.model.ProductInfo;
import com.gjj.shop.net.ApiConstants;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.finalteam.loadingviewfinal.GridViewFinal;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrDefaultHandler;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Chuck on 2016/9/1.
 */
public class SearchFragment extends BaseFragment {
    @Bind(R.id.product_grid)
    GridViewFinal mProductGrid;
    @Bind(R.id.search_et)
    EditText mSearchEt;
    private GridAdapter mAdapter;

    @OnClick(R.id.back_icon)
    void back() {
        onBackPressed();
    }
    @OnClick(R.id.right_btn)
    void search() {
        String searchText = mSearchEt.getText().toString();
        if(TextUtils.isEmpty(searchText)) {
            ToastUtil.shortToast(R.string.input_prduct_name);
            return;
        }
        hideKeyboardForCurrentFocus();
        showLoadingDialog(R.string.committing, false);
        HashMap<String, String> params = new HashMap<>();
        params.put("searchContent", searchText);
        params.put("index", String.valueOf(0));
        params.put("size", String.valueOf(100));
        OkHttpUtils.get(ApiConstants.PRODUCT_LIST)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
                .execute(new ListCallback<ProductInfo>(ProductInfo.class) {
                    @Override
                    public void onSuccess(final BaseList<ProductInfo> productInfoBaseList, Call call, Response response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissLoadingDialog();
                                handleData(productInfoBaseList);
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
    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_search;
    }

    @Override
    public void initView() {

        mAdapter = new GridAdapter(getActivity(), new ArrayList<ProductInfo>());
        mProductGrid.setAdapter(mAdapter);

    }

    private void handleData(BaseList<ProductInfo> baseList) {
        List<ProductInfo> infoList = new ArrayList<ProductInfo>();
        if (baseList != null) {
            infoList = baseList.list;
        }
        mAdapter.setData(infoList);
    }
}
