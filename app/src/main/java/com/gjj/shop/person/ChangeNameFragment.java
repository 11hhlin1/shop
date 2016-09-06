package com.gjj.shop.person;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.applibrary.http.callback.StringDialogCallback;
import com.gjj.applibrary.log.L;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.shop.R;
import com.gjj.shop.app.BaseApplication;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.event.UpdateUserInfo;
import com.gjj.shop.model.UserInfo;
import com.gjj.shop.net.ApiConstants;
import com.gjj.shop.widget.EditTextWithDel;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/14.
 */
public class ChangeNameFragment extends BaseFragment {

    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.name_et)
    EditTextWithDel nameEt;

    @Override
    public void onRightBtnClick() {
        super.onRightBtnClick();
        HashMap<String, String> params = new HashMap<>();
        params.put("nickname", nameEt.getText().toString());
        List<File> fileList = new ArrayList<>();
//        fileList.add(new File(mPhotoUrl));
        showLoadingDialog(R.string.committing,true);
        OkHttpUtils.post(ApiConstants.UPDATE_USER_INFO)//
                .tag(this)//
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
                .addFileParams("avatar", null)
                .execute(new JsonCallback<UserInfo>(UserInfo.class) {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dismissLoadingDialog();
                        if(response != null)
                            L.d("@@@@@>>", response.code());
                        ToastUtil.shortToast(R.string.fail);
                    }

                    @Override
                    public void onSuccess(UserInfo userInfo, Call call, Response response) {
                        dismissLoadingDialog();
                        ToastUtil.shortToast(R.string.commit_success);
                        onBackPressed();
                        BaseApplication.getUserMgr().saveUserInfo(userInfo);
                        EventBus.getDefault().post(new UpdateUserInfo());
                    }

                });
    }

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_change_name;
    }

    @Override
    public void initView() {

    }


}
