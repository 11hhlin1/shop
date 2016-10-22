package com.gjj.shop.order;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.gjj.applibrary.app.AppLib;
import com.gjj.applibrary.glide.GlideCircleTransform;
import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.applibrary.util.Util;
import com.gjj.shop.R;
import com.gjj.shop.address.AddressInfo;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.base.PageSwitcher;
import com.gjj.shop.event.EventOfCancelOrder;
import com.gjj.shop.event.EventOfCheckGoods;
import com.gjj.shop.net.ApiConstants;
import com.gjj.shop.net.UrlUtil;
import com.gjj.shop.util.CallUtil;
import com.gjj.shop.widget.ConfirmDialog;
import com.gjj.shop.widget.UnScrollableListView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Chuck on 2016/9/27.
 */
public class OrderDetailFragment extends BaseFragment {

    @Bind(R.id.receive_person)
    TextView receivePerson;
    @Bind(R.id.address_detail)
    TextView addressDetail;
    @Bind(R.id.address_rl)
    RelativeLayout addressRl;
    @Bind(R.id.shop_avatar)
    ImageView shopAvatar;
    @Bind(R.id.shop_name)
    TextView shopName;
    @Bind(R.id.shop_icon_rl)
    RelativeLayout shopIconRl;
    @Bind(R.id.order_num)
    TextView orderNum;
    @Bind(R.id.order_state)
    TextView orderState;
    @Bind(R.id.order_num_rl)
    RelativeLayout orderNumRl;
    @Bind(R.id.good_list)
    UnScrollableListView goodList;
    @Bind(R.id.qq_ll)
    LinearLayout qqLl;
    @Bind(R.id.tel_ll)
    LinearLayout telLl;
    @Bind(R.id.contact_item)
    LinearLayout contactItem;
    @Bind(R.id.comment_title)
    TextView commentTitle;
    @Bind(R.id.sel_box_1)
    CheckBox selBox1;
    @Bind(R.id.sel_box_2)
    CheckBox selBox2;
    @Bind(R.id.sel_box_3)
    CheckBox selBox3;
    @Bind(R.id.sel_box_5)
    CheckBox selBox5;
    @Bind(R.id.sel_box_4)
    CheckBox selBox4;
    @Bind(R.id.star_item)
    LinearLayout starItem;
    @Bind(R.id.comment_time)
    TextView commentTime;
    @Bind(R.id.comment_desc)
    TextView commentDesc;
    @Bind(R.id.my_comment)
    RelativeLayout myComment;
    @Bind(R.id.bottom_rl)
    RelativeLayout mBottomRl;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.order_amount_tv)
    TextView orderAmount;
    @Bind(R.id.sure_order)
    Button sureOrder;
    @Bind(R.id.order_left_btn)
    Button orderLeftBtn;
    OrderInfo orderInfo;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_order_detail;
    }

    @Override
    public void initView() {
        Bundle bundle = getArguments();
        orderInfo = bundle.getParcelable("orderInfo");
        assert orderInfo != null;
        AddressInfo addressInfo = orderInfo.address;
        if (addressInfo != null) {
            StringBuilder phone = Util.getThreadSafeStringBuilder();
            phone.append("收货人:").append(addressInfo.contact).append("  ").append(addressInfo.phone);
            receivePerson.setText(phone.toString());
            StringBuilder address = Util.getThreadSafeStringBuilder();
            address.append(addressInfo.area).append(addressInfo.address);
            addressDetail.setText(address.toString());
        }
        Glide.with(this)
                .load(UrlUtil.getHttpUrl(orderInfo.shopLogoThumb))
                .centerCrop()
                .bitmapTransform(new GlideCircleTransform(getActivity()))
                .error(new ColorDrawable(AppLib.getResources().getColor(android.R.color.transparent)))
                .into(shopAvatar);
        shopName.setText(orderInfo.shopName);
        orderNum.setText(getString(R.string.order_num, orderInfo.orderId));

        orderAmount.setText(getString(R.string.money_has_mark, String.valueOf(2400)));
        switch (orderInfo.status) {
            case 0:
                orderState.setText(getString(R.string.pay_order));
                myComment.setVisibility(View.GONE);
                sureOrder.setText(getString(R.string.pay));
                orderLeftBtn.setText(getString(R.string.cancel_order_btn));
                orderLeftBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelOrder();
                    }
                });
                sureOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        List<String> orderIdList = new ArrayList<>();
                        orderIdList.add(orderInfo.orderId);
                        bundle.putString("orderIds", JSONArray.toJSONString(orderIdList));
                        PageSwitcher.switchToTopNavPage(getActivity(), ChoosePayWayFragment.class, bundle, getString(R.string.pay), "");
                    }
                });
                break;
            case 1:
                orderState.setText(getString(R.string.accepting_order));
                orderLeftBtn.setVisibility(View.GONE);
                myComment.setVisibility(View.GONE);
                sureOrder.setText(getString(R.string.check_accept_good));
                sureOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                                                onBackPressed();
                                                EventBus.getDefault().post(new EventOfCheckGoods());
                                                ToastUtil.shortToast(getActivity(), "确认成功");
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
                break;
            case 2:
                orderState.setText(getString(R.string.accept_order));
                sureOrder.setText(getString(R.string.apply_after_order));
                sureOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("orderInfo", orderInfo);
                        PageSwitcher.switchToTopNavPage(getActivity(), AfterSaleFragment.class, bundle, getString(R.string.apply_after_order), "");

                    }
                });
                orderLeftBtn.setVisibility(View.GONE);
                break;
            case 3:
                orderState.setText(getString(R.string.cancel_order));
                myComment.setVisibility(View.GONE);
                sureOrder.setVisibility(View.GONE);
                orderLeftBtn.setVisibility(View.GONE);
                break;
            case 4:
                if(orderInfo.afterSaleService != null) {
                    if(orderInfo.afterSaleService.getStatus() == 0) {
                        orderState.setText("处理中");
                    } else {
                        orderState.setText("退款成功");
                    }
                }
                mBottomRl.setVisibility(View.GONE);
                myComment.setVisibility(View.GONE);
                break;
        }

        final GoodItemListAdapter listAdapter = new GoodItemListAdapter(getActivity(), orderInfo.goodsList, -1, "");
        goodList.setAdapter(listAdapter);
    }

    public void cancelOrder() {
        ConfirmDialog confirmDialog = new ConfirmDialog(getActivity(), R.style.white_bg_dialog);
        confirmDialog.setConfirmClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                        onBackPressed();
                                        EventBus.getDefault().post(new EventOfCancelOrder());
                                        ToastUtil.shortToast(getActivity(), "取消成功");
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

    @OnClick(R.id.tel_ll)
    void onTelClick() {
        CallUtil.askForMakeCall(getActivity(), "", orderInfo.shopId);
    }
}
