package com.gjj.shop.person;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/8/14.
 */
public class ChangePswFragment extends BaseFragment {
    @Bind(R.id.et_sms_code)
    EditText etSmsCode;
    @Bind(R.id.et_psw)
    EditText etPsw;
    @Bind(R.id.et_psw_check)
    EditText etPswCheck;
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
    }
}
