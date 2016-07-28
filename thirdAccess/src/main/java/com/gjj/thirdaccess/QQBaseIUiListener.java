package com.gjj.thirdaccess;

import android.content.Context;
import android.widget.Toast;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import org.json.JSONObject;

/**
 * Title:过家家-项目经理
 * Description:
 * Copyright: Copyright (c) 2016
 * Company: 深圳市过家家
 * version: 1.0.0.0
 * author: jack
 * createDate 2016/2/19
 */
public class QQBaseIUiListener implements IUiListener {
    private Context mContext;
    private String mScope;
    /**
     * 类型
     */
    //private int mType;

    public QQBaseIUiListener(Context context) {
        super();
        this.mContext = context;
    }

    public QQBaseIUiListener(Context context, String mScope) {
        super();
        this.mContext = context;
        this.mScope = mScope;
    }

    @Override
    public void onComplete(Object response) {
        if (null == response) {
            //Util.showResultDialog(MainActivity.this, "返回为空", "登录失败");
            return;
        }
        JSONObject jsonResponse = (JSONObject) response;
        if (null != jsonResponse && jsonResponse.length() == 0) {
            //Util.showResultDialog(MainActivity.this, "返回为空", "登录失败");
            Toast.makeText(mContext,"返回为空, 登录失败",Toast.LENGTH_LONG).show();
            return;
        }
        //Util.showResultDialog(MainActivity.this, response.toString(), "登录成功");
        doComplete((JSONObject)response);
    }

    protected void doComplete(JSONObject values) {

    }

    @Override
    public void onError(UiError e) {
        //Util.toastMessage(MainActivity.this, "onError: " + e.errorDetail);
       // Util.dismissDialog();
        Toast.makeText(mContext,"onError: " + e.errorDetail,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCancel() {
        //Util.toastMessage(MainActivity.this, "onCancel: ");
        //Util.dismissDialog();
       // if (isServerSideLogin) {
        //    isServerSideLogin = false;
        //}
        Toast.makeText(mContext,"onCancel: ",Toast.LENGTH_LONG).show();
    }


}
