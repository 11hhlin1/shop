package com.gjj.shop.address;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.applibrary.http.callback.ListCallback;
import com.gjj.applibrary.http.model.BaseList;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.model.ProductInfo;
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
 * Created by user on 16/8/28.
 */
public class AddAddressFragment extends BaseFragment {
    @Bind(R.id.name_et)
    EditText nameEt;
    @Bind(R.id.phone_et)
    EditText phoneEt;
    @Bind(R.id.address)
    EditText address;
    @Bind(R.id.address_detail)
    EditText addressDetail;
    @Bind(R.id.default_sel_box)
    CheckBox defaultSelBox;
    @Bind(R.id.save_address)
    Button saveAddress;

    boolean isDefault;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_add_address;
    }

    @Override
    public void initView() {
        defaultSelBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDefault = isChecked;
            }
        });
    }



    @OnClick(R.id.save_address)
    void commit() {
        showLoadingDialog(R.string.committing,false);
        HashMap<String, String> params = new HashMap<>();
        params.put("contact", nameEt.getText().toString());
        params.put("phone", phoneEt.getText().toString());
        params.put("area", address.getText().toString());
        params.put("address", addressDetail.getText().toString());
        params.put("isDefault", String.valueOf(false));
        OkHttpUtils.post(ApiConstants.ADD_ADDRESS)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
                .execute(new JsonCallback<String>(String.class) {


                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissLoadingDialog();
                                ToastUtil.shortToast(R.string.success);
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

                            }
                        });
                    }
                });
    }
}
