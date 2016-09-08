package com.gjj.shop.address;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.applibrary.log.L;
import com.gjj.applibrary.util.PreferencesManager;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.event.EventOfAddress;
import com.gjj.shop.net.ApiConstants;
import com.gjj.shop.shopping.CartDeleteReq;
import com.gjj.shop.shopping.GoodsAdapterInfo;
import com.gjj.shop.shopping.ShopAdapterInfo;
import com.gjj.shop.widget.ConfirmDialog;
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
 * Created by user on 16/8/27.
 */
public class changeAddressFragment extends BaseFragment {
    @Bind(R.id.name)
    EditText nameEt;
    @Bind(R.id.phone)
    EditText phoneEt;
    @Bind(R.id.address)
    EditText address;
    @Bind(R.id.address_detail)
    EditText addressDetail;
    @Bind(R.id.default_sel_box)
    CheckBox defaultSelBox;
    @Bind(R.id.delete_address)
    Button deleteAddress;

    private AddressInfo addressInfo;
    private boolean isDefault;
    private ConfirmDialog mConfirmDialog;


    @Override
    public void onRightBtnClick() {
        super.onRightBtnClick();
        String name = nameEt.getText().toString();
        String phone = phoneEt.getText().toString();
        String area = address.getText().toString();
        String detail = addressDetail.getText().toString();
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)||TextUtils.isEmpty(area)||TextUtils.isEmpty(detail)) {
            ToastUtil.shortToast(R.string.input_address);
            return;
        }
        showLoadingDialog(R.string.committing,false);
        HashMap<String, String> params = new HashMap<>();
        params.put("addressId", String.valueOf(addressInfo.addressId));
        params.put("contact", name);
        params.put("phone", phone);
        params.put("area", area);
        params.put("address", detail);
        params.put("isDefault", String.valueOf(isDefault));
        OkHttpUtils.post(ApiConstants.UPDATE_ADDRESS)
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
                                onBackPressed();
                                EventBus.getDefault().post(new EventOfAddress());
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

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_change_address;
    }

    @Override
    public void initView() {
         Bundle bundle = getArguments();
        addressInfo = (AddressInfo) bundle.getSerializable("addressInfo");
        nameEt.setText(addressInfo.contact);
        phoneEt.setText(addressInfo.phone);
        address.setText(addressInfo.area);
        addressDetail.setText(addressInfo.address);
        long defaultAddressId = PreferencesManager.getInstance().get("defaultAddressId",0L);
        if(defaultAddressId == addressInfo.addressId) {
            defaultSelBox.setChecked(true);
        } else {
            defaultSelBox.setChecked(false);
        }
        defaultSelBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDefault = isChecked;
            }
        });
    }

    @OnClick(R.id.delete_address)
    public void onClick() {
        dismissConfirmDialog();
        ConfirmDialog confirmDialog = new ConfirmDialog(getActivity(), R.style.white_bg_dialog);
        mConfirmDialog = confirmDialog;
        confirmDialog.setConfirmClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog(R.string.committing,false);
                HashMap<String, String> params = new HashMap<>();
                params.put("addressId", String.valueOf(addressInfo.addressId));
                OkHttpUtils.post(ApiConstants.DELETE_ADDRESS)
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
                                        onBackPressed();
                                        EventBus.getDefault().post(new EventOfAddress());
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
        });
        confirmDialog.setCanceledOnTouchOutside(true);
        confirmDialog.show();
        confirmDialog.setContent(R.string.delete_address_tip);

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
