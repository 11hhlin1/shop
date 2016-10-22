package com.gjj.shop.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.gjj.applibrary.app.AppLib;
import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.applibrary.log.L;
import com.gjj.applibrary.task.ForegroundTaskExecutor;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.net.ApiConstants;
import com.gjj.shop.wxapi.ShareConstant;
import com.gjj.thirdaccess.WeiXinAccess;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Chuck on 2016/10/14.
 */

public class ChoosePayWayFragment extends BaseFragment {
    @Bind(R.id.ali_pay_check)
    CheckBox aliPayCheck;
    @Bind(R.id.ali_pay_item)
    RelativeLayout aliPayItem;
    @Bind(R.id.weixin_pay_check)
    CheckBox weixinPayCheck;
    @Bind(R.id.weixin_pay_item)
    RelativeLayout weixinPayItem;
    @Bind(R.id.pay_btn)
    Button payBtn;
    String payInfo;
    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_pay_sure;
    }

    @Override
    public void initView() {
        payInfo = getArguments().getString("orderIds");
        aliPayCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    weixinPayCheck.setChecked(false);
                }
            }
        });
        weixinPayCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    aliPayCheck.setChecked(false);
                }
            }
        });
    }

    @OnClick(R.id.pay_btn)
    public void onClick() {
        if(weixinPayCheck.isChecked()) {
            OkHttpUtils.post(ApiConstants.PAY_BY_WEIXIN)
                    .tag(this)
                    .cacheMode(CacheMode.NO_CACHE)
                    .params("orderIds", payInfo)
//                .upJson(JSON.toJSONString(commitOrderReq))
                    .execute(new JsonCallback<String>(String.class) {

                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            WeiXinAccess weiXinAccess = new WeiXinAccess(AppLib.getContext(), ShareConstant.WEXINAPPID);
                            JSONObject jsonObject = JSON.parseObject(s);
                            weiXinAccess.pay2weixin(jsonObject.getString("partnerid"),jsonObject.getString("prepayid"), jsonObject.getString("noncestr"),jsonObject.getString("timestamp"),jsonObject.getString("sign") );
                        }
                    });

        } else {
            OkHttpUtils.post(ApiConstants.PAY_BY_ALIPAY)
                    .tag(this)
                    .cacheMode(CacheMode.NO_CACHE)
                    .params("orderIds", payInfo)
//                .upJson(JSON.toJSONString(commitOrderReq))
                    .execute(new JsonCallback<String>(String.class) {

                        @Override
                        public void onSuccess(final String orderInfo, Call call, Response response) {
                        ForegroundTaskExecutor.executeTask(new Runnable() {
                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(getActivity());
                                Map<String, String> result = alipay.payV2(orderInfo,true);
                                L.d("@@@" + result);
                            }
                        });

                        }
                    });
        }
    }
}
