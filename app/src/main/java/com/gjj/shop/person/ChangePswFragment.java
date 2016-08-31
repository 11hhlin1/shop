package com.gjj.shop.person;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.applibrary.log.L;
import com.gjj.applibrary.util.MD5Util;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.applibrary.util.Util;
import com.gjj.shop.R;
import com.gjj.shop.app.BaseApplication;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.model.UserInfo;
import com.gjj.shop.net.ApiConstants;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/14.
 */
public class ChangePswFragment extends BaseFragment {


    @Bind(R.id.et_old_psw)
    EditText etOldPsw;
    @Bind(R.id.et_new_psw)
    EditText etNewPsw;
    @Bind(R.id.et_new_psw_again)
    EditText etNewPswAgain;
    @Bind(R.id.btn_commit)
    Button btnCommit;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_change_psw;
    }

    @Override
    public void initView() {

    }

    @OnClick(R.id.btn_commit)
    public void onClick() {
//        String phone = etSmsCode.getText().toString().trim().replaceAll(" ", "");
//        if (TextUtils.isEmpty(phone)) {
//            ToastUtil.shortToast(R.string.hint_login_username);
//            return;
//        }
//        if (!Util.isMobileNO(phone)) {
//            ToastUtil.shortToast(R.string.enter_mobile_error);
//            return;
//        }
        final String oldPsw = etOldPsw.getText().toString().trim().replaceAll(" ", "");
        String newPsw = etNewPsw.getText().toString().trim().replaceAll(" ", "");
        String newPswAgain = etNewPswAgain.getText().toString().trim().replaceAll(" ", "");
        if (TextUtils.isEmpty(oldPsw) || TextUtils.isEmpty(newPsw)) {
            ToastUtil.shortToast(R.string.hint_login_pwd);
            return;
        }

        if(!newPsw.equals(newPswAgain)) {
            ToastUtil.shortToast(R.string.enter_psw_error);
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        UserInfo userInfo = BaseApplication.getUserMgr().getUser();
        params.put("username", userInfo.getPhone());
        params.put("password_old", MD5Util.md5Hex(oldPsw));
        params.put("password_new", MD5Util.md5Hex(newPsw));
        showLoadingDialog(R.string.committing, true);

//        final JSONObject jsonObject = new JSONObject(params);
        OkHttpUtils.post(ApiConstants.CHANGE_PSW)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
//                .postJson(jsonObject.toString())
                .execute(new JsonCallback<UserInfo>(UserInfo.class) {
                    @Override
                    public void onResponse(boolean isFromCache, UserInfo rspInfo, Request request, @Nullable Response response) {
                        dismissLoadingDialog();
                        ToastUtil.shortToast(R.string.commit_success);
                        onBackPressed();
                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        dismissLoadingDialog();
                        if (response != null)
                            L.d("@@@@@>>", response.code());
                    }
                });
    }

}
