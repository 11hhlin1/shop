package com.gjj.shop.shopping;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
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
import com.gjj.shop.event.EventOfAddCartSuccess;
import com.gjj.shop.event.EventOfCreateOrderSuccess;
import com.gjj.shop.net.ApiConstants;
import com.gjj.shop.order.EditOrderFragment;
import com.gjj.shop.widget.ConfirmDialog;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Chuck on 2016/7/21.
 */
public class ShoppingFragment extends BaseFragment {
    @Bind(R.id.tv_title)
    TextView mTitleTV;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.edit_shop)
    TextView mEditTv;

    @Bind(R.id.state_btn)
    TextView mStateTv;

    private ConfirmDialog mConfirmDialog;
//    private HashMap<Boolean, List<Boolean>> mSelList;

    @OnClick(R.id.edit_shop)
    void editShop() {
        if (isEdit) {
            mEditTv.setText(getString(R.string.edit));
            mStateTv.setText(getString(R.string.balance));
            isEdit = false;
        } else {
            isEdit = true;
            mEditTv.setText(getString(R.string.done));
            mStateTv.setText(getString(R.string.delete));
        }
        mAdapter.setmIsEdit(isEdit);
    }

    @OnClick(R.id.state_btn)
    void handleState() {
        if (isEdit) {
            dismissConfirmDialog();
            ConfirmDialog confirmDialog = new ConfirmDialog(getActivity(), R.style.white_bg_dialog);
            mConfirmDialog = confirmDialog;
            confirmDialog.setConfirmClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<ShopAdapterInfo> shopAdapterInfos = mAdapter.getDataList();
                    List<CartDeleteReq> deleteReqs = new ArrayList<CartDeleteReq>();
                    for (ShopAdapterInfo info : shopAdapterInfos) {
                        for (GoodsAdapterInfo goodsAdapterInfo:info.goodsList) {
                            if (goodsAdapterInfo.isSel) {
                                CartDeleteReq req = new CartDeleteReq();
                                req.goodsId = goodsAdapterInfo.goodsInfo.goodsId;
                                req.tags = goodsAdapterInfo.goodsInfo.tags;
                                deleteReqs.add(req);
                            }
                        }

                    }
                    L.d("@@@@@>" + JSON.toJSONString(deleteReqs));
                    OkHttpUtils.post(ApiConstants.DELETE_CART)
                            .tag(this)
                            .params("goodsList", JSON.toJSONString(deleteReqs))
                            .cacheMode(CacheMode.NO_CACHE)
                            .execute(new JsonCallback<String>(String.class) {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    doRequest();
                                }

                            });
                }
            });
            confirmDialog.setCanceledOnTouchOutside(true);
            confirmDialog.show();
            confirmDialog.setContent(R.string.delete_shop_tip);
        } else {
            //TODO 结算
            Bundle bundle = new Bundle();
            ArrayList<ShopInfo> shopInfoArrayList = new ArrayList<>();
            List<ShopAdapterInfo> shopAdapterInfos = mAdapter.getDataList();
            for (ShopAdapterInfo shopAdapterInfo : shopAdapterInfos) {
                    ShopInfo shopInfo = new ShopInfo();
                    shopInfo.shopId = shopAdapterInfo.shopId;
                    shopInfo.shopName = shopAdapterInfo.shopName;
                    shopInfo.shopImage = shopAdapterInfo.shopImage;
                    shopInfo.shopThumb = shopAdapterInfo.shopThumb;
                    ArrayList<GoodsInfo> goodsList = new ArrayList<>();
                    for (GoodsAdapterInfo goodsAdapterInfo: shopAdapterInfo.goodsList) {
                        if(goodsAdapterInfo.isSel)
                        goodsList.add(goodsAdapterInfo.goodsInfo);
                    }
                    shopInfo.goodsList = goodsList;
                    if(goodsList.size() > 0)
                    shopInfoArrayList.add(shopInfo);

            }
            if(shopInfoArrayList.size() < 1) {
                ToastUtil.shortToast(R.string.choose_product);
                return;
            }
            bundle.putParcelableArrayList("shopInfo", shopInfoArrayList);
            bundle.putBoolean("isFromShopping", true);
            PageSwitcher.switchToTopNavPage(getActivity(), EditOrderFragment.class, bundle, getString(R.string.check_order), "");

        }
    }

    @Bind(R.id.all_sel_box)
    CheckBox mAllSel;

    private ShoppingAdapter mAdapter;
    private boolean isEdit = false;
//    private Map<String, Boolean> mSel;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_shopping;
    }

    @Override
    public void initView() {
        mTitleTV.setText(R.string.shopping);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        mShopIDList = new ArrayList<>();
//        mSelList = new HashMap<>();
        // 设置布局管理器
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new ShoppingAdapter(getActivity(), new ArrayList<ShopAdapterInfo>(),isEdit);
        mRecyclerView.setAdapter(mAdapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin_20p);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        EventBus.getDefault().register(this);

        mAllSel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    List<ShopAdapterInfo> list = mAdapter.getDataList();
                    if (!Util.isListEmpty(list)) {
                        for (ShopAdapterInfo info : list) {
                              info.isSel = isChecked;
                              for (GoodsAdapterInfo goodsAdapterInfo : info.goodsList) {
                                  goodsAdapterInfo.isSel = isChecked;
                              }
                        }
                    }
                    mAdapter.notifyDataSetChanged();

            }
        });
        doRequest();


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(EventOfAddCartSuccess event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                doRequest();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(EventOfCreateOrderSuccess event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                doRequest();
            }
        });
    }
    private void doRequest() {
        OkHttpUtils.get(ApiConstants.CART_LIST)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new ListCallback<ShopInfo>(ShopInfo.class) {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        ToastUtil.shortToast(R.string.fail);
                    }

                    @Override
                    public void onSuccess(BaseList<ShopInfo> shopInfoBaseList, Call call, Response response) {
                        if(Util.isListEmpty(shopInfoBaseList.list)) {
                            mAdapter.setData(new ArrayList<ShopAdapterInfo>());
                            return;
                        }
                        List<ShopAdapterInfo> adapterInfos = new ArrayList<ShopAdapterInfo>();
                        for (ShopInfo shopInfo : shopInfoBaseList.list) {
                            ShopAdapterInfo shopAdapterInfo = new ShopAdapterInfo();
                            shopAdapterInfo.shopId = shopInfo.shopId;
                            shopAdapterInfo.shopImage = shopInfo.shopImage;
                            shopAdapterInfo.shopName = shopInfo.shopName;
                            shopAdapterInfo.shopThumb =shopInfo.shopThumb;
                            List<GoodsAdapterInfo> goodsAdapterInfos = new ArrayList<GoodsAdapterInfo>();
                            for (GoodsInfo goodsInfo : shopInfo.goodsList) {
                                GoodsAdapterInfo goodsAdapterInfo = new GoodsAdapterInfo();
                                goodsAdapterInfo.goodsInfo = goodsInfo;
                                goodsAdapterInfos.add(goodsAdapterInfo);
                            }
                            shopAdapterInfo.goodsList = goodsAdapterInfos;
                            adapterInfos.add(shopAdapterInfo);
//                            List<Boolean> booleanList = new ArrayList<Boolean>(shopInfo.goodsList.size());
//                            mSelList.put(false,booleanList);
                        }
//                        mAdapter.setmSelList(mSelList);
                        mAdapter.setData(adapterInfos);
                    }

                });
    }

    /**
     * dismiss确认对话框
     */
    private void dismissConfirmDialog() {
        ConfirmDialog confirmDialog = mConfirmDialog;
        if (null != confirmDialog && confirmDialog.isShowing()) {
            confirmDialog.dismiss();
            mConfirmDialog = null;
        }
    }



}
