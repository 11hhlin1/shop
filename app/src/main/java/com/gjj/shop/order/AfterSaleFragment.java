package com.gjj.shop.order;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.event.EventOfCheckGoods;
import com.gjj.shop.net.ApiConstants;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Chuck on 2016/10/17.
 */

public class AfterSaleFragment extends BaseFragment {
    @Bind(R.id.advice_et)
    EditText adviceEt;
    @Bind(R.id.btn_commit)
    Button btnCommit;
    OrderInfo orderInfo;
    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_after_sale;
    }

    @Override
    public void initView() {
        orderInfo = (OrderInfo) getArguments().getParcelable("orderInfo");

    }
    @OnClick(R.id.btn_commit)
    public void onClick() {
        String advice = adviceEt.getText().toString();
        if(TextUtils.isEmpty(advice)) {
            ToastUtil.shortToast(getActivity(), "请填写说明");
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("orderId", orderInfo.orderId);
        params.put("title", "退货退款");
        params.put("content", advice);
        OkHttpUtils.post(ApiConstants.ORDER_AFTERSALE)
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
                                ToastUtil.shortToast(R.string.commit_success);
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
