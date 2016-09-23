package com.gjj.shop.index;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gjj.applibrary.app.AppLib;
import com.gjj.applibrary.glide.GlideCircleTransform;
import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.applibrary.http.callback.ListCallback;
import com.gjj.applibrary.http.model.BaseList;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.model.ProductInfo;
import com.gjj.shop.net.ApiConstants;
import com.gjj.shop.net.UrlUtil;
import com.gjj.shop.util.CallUtil;
import com.gjj.shop.widget.UnScrollableGridView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.loadingviewfinal.GridViewFinal;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by user on 16/8/7.
 */
public class ShopFragment extends BaseFragment {
    @Bind(R.id.shop_icon)
    ImageView shopIcon;
    @Bind(R.id.shop_name)
    TextView shopName;
    @Bind(R.id.contact_service)
    Button contactService;
    @Bind(R.id.shop_msg)
    TextView shopMsg;
    @Bind(R.id.gridView)
    UnScrollableGridView gridView;
    @Bind(R.id.icon_back_btn)
    ImageView iconBackBtn;
    @Bind(R.id.scrollView)
    ScrollView mScrollView;
    private GridAdapter mAdapter;
    private ShopInfo mShopInfo;


    @OnClick(R.id.icon_back_btn)
    void back(){
        onBackPressed();
    }

    @OnClick(R.id.contact_service)
    void setContactService(){
        CallUtil.askForMakeCall(getActivity(), "", mShopInfo.contactPhone);
    }
    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_shop;
    }

    @Override
    public void initView() {
        ArrayList<ProductInfo> list = new ArrayList<ProductInfo>();
//        for (int i = 0; i< 10; i++) {
//            ProductInfo productInfo = new ProductInfo();
//            productInfo.name = "的撒旦";
//            productInfo.logo = "";
//            list.add(productInfo);
//        }
        mAdapter = new GridAdapter(getActivity(), list);
        gridView.setAdapter(mAdapter);
        mScrollView.fullScroll(ScrollView.FOCUS_UP);
        gridView.setFocusable(false);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.shortToast(view.getContext(), "product:"+position);
            }
        });
        mShopInfo = (ShopInfo) getArguments().getSerializable("mShopInfo");
        assert mShopInfo != null;
        shopName.setText(mShopInfo.name);
        Glide.with(this)
                .load(UrlUtil.getHttpUrl(mShopInfo.image))
                .centerCrop()
                .error(new ColorDrawable(AppLib.getResources().getColor(android.R.color.transparent)))
                .bitmapTransform(new GlideCircleTransform(getContext()))
                .into(shopIcon);
        shopMsg.setText(mShopInfo.details);
        requestData(0);
//        gridView.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void loadMore() {
//                requestData(mAdapter.getCount() - 1);
//            }
//        });
//        getShopInfo();
    }

    private void getShopInfo() {
        HashMap<String, String> params = new HashMap<>();
        params.put("shopId", "771231952710664192");

        OkHttpUtils.get(ApiConstants.SHOP_INFO)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
                .execute(new JsonCallback<ShopInfo>(ShopInfo.class) {
                    @Override
                    public void onSuccess(ShopInfo shopInfo, Call call, Response response) {
                        ToastUtil.shortToast(R.string.success);

                    }

                });
    }


    private void requestData(final int start) {
        showLoadingDialog(R.string.committing,false);
        HashMap<String, String> params = new HashMap<>();
        params.put("shopId", String.valueOf(mShopInfo.shopId));
        params.put("index", String.valueOf(start));
        params.put("size", String.valueOf(1000));
        OkHttpUtils.get(ApiConstants.PRODUCT_LIST)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
                .execute(new ListCallback<ProductInfo>(ProductInfo.class) {
                    @Override
                    public void onSuccess(BaseList<ProductInfo> productInfoBaseList, Call call, Response response) {
                        dismissLoadingDialog();
                        handleData(start,productInfoBaseList);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                if (start != 0) {
//                                    gridView.onLoadMoreComplete();
//                                }
                                ToastUtil.shortToast(R.string.fail);
                            }
                        });
                    }
                });
    }
    private void handleData(int start, BaseList<ProductInfo> baseList) {
//        if (start != 0) {
//            gridView.onLoadMoreComplete();
//        }
        List<ProductInfo> infoList = new ArrayList<ProductInfo>();
        if (baseList != null) {
            infoList = baseList.list;
        }
        if (start == 0) {
            mAdapter.setData(infoList);
        } else {
            mAdapter.addData(infoList);
        }
//        if (infoList.size() < ProductListFragment.SIZE) {
//            gridView.setHasLoadMore(false);
//        } else {
//            gridView.setHasLoadMore(true);
//        }
    }

}
