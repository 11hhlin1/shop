package com.gjj.shop.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.applibrary.http.callback.ListCallback;
import com.gjj.applibrary.http.model.BaseList;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.community.CommunityFragment;
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
 * Created by user on 16/10/16.
 */
public class CommentFragment extends BaseFragment {
    @Bind(R.id.sel_box_1)
    CheckBox selBox1;
    @Bind(R.id.sel_box_2)
    CheckBox selBox2;
    @Bind(R.id.sel_box_3)
    CheckBox selBox3;
    @Bind(R.id.sel_box_4)
    CheckBox selBox4;
    @Bind(R.id.sel_box_5)
    CheckBox selBox5;
    @Bind(R.id.score)
    TextView score;
    @Bind(R.id.desc_tv)
    EditText descTv;
    @Bind(R.id.btn_comment)
    Button btnComment;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_comment;
    }

    @Override
    public void initView() {

    }


    @OnClick(R.id.btn_comment)
    public void onClick() {
        HashMap<String, String> params = new HashMap<>();
        Bundle bundle = getArguments();
        params.put("goodsId", bundle.getString("goodsId"));
        params.put("content", descTv.getText().toString());
        params.put("star", String.valueOf(5));
        params.put("orderId", bundle.getString("orderId"));
        OkHttpUtils.post(ApiConstants.COMMENT_PRODUCT)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
                .execute(new JsonCallback<String>(String.class) {

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                          runOnUiThread(new Runnable() {
                              @Override
                              public void run() {
                                  ToastUtil.shortToast(R.string.success);
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
