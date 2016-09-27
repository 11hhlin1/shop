package com.gjj.shop.person;

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
import com.gjj.shop.net.ApiConstants;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by user on 16/8/27.
 */
public class AdviceCommitFragment extends BaseFragment {
    @Bind(R.id.advice_et)
    EditText adviceEt;
    @Bind(R.id.btn_commit)
    Button btnCommit;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_commit_advice;
    }

    @Override
    public void initView() {

    }


    @OnClick(R.id.btn_commit)
    public void onClick() {
        String des = adviceEt.getText().toString();
        if (TextUtils.isEmpty(des))
            return;
        HashMap<String, String> params = new HashMap<>();
        params.put("info", des);
        OkHttpUtils.post(ApiConstants.ADD_FEEDBACK)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
                .execute(new JsonCallback<String>(String.class) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.shortToast(R.string.commit_success);
                                onBackPressed();
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
