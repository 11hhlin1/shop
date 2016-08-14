package com.gjj.applibrary.http.callback;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.gjj.applibrary.log.L;
import com.lzy.okhttputils.OkHttpUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/14.
 */
public abstract class ListCallback<T extends BaseList> extends CommonCallback<T>{
    private Class<T> clazz;
    public ListCallback(Class<T> clazz) {
        this.clazz = clazz;
    }
    @Override
    public T parseNetworkResponse(Response response) throws Exception {
        String responseData = response.body().string();
        if (TextUtils.isEmpty(responseData)) return null;

        /**
         * 一般来说，服务器返回的响应码都包含 code，msg，data 三部分，在此根据自己的业务需要完成相应的逻辑判断
         * 以下只是一个示例，具体业务具体实现
         */
        JSONObject jsonObject = new JSONObject(responseData);
        final String msg = jsonObject.optString("msg", "");
        final int code = jsonObject.optInt("code", 0);
        String data = jsonObject.optString("data", "");
        switch (code) {
            case 0:
                /**
                 * code = 0 代表成功，默认实现了Gson解析成相应的实体Bean返回，可以自己替换成fastjson等
                 * 对于返回参数，先支持 String，然后优先支持class类型的字节码，最后支持type类型的参数
                 */
                if (clazz != null) {
//                    JSONObject jsonObj = new JSONObject(data);
//                    if(jsonObj instanceof JSONObject) {

                    T object = JSON.parseObject(data, clazz);
                    L.d("@@@@", object);
//                    } else  {
//                        JSONArray jsonArray = new JSONArray();
//                        object = JSON.parseObject(data, clazz);
////                        object = JSON.parseArray(data, clazz);
//                    }

                    return object;

                }
                break;
            case 104:
                //比如：用户授权信息无效，在此实现相应的逻辑，弹出对话或者跳转到其他页面等,该抛出错误，会在onError中回调。
                throw new IllegalStateException("用户授权信息无效");
            case 105:
                //比如：用户收取信息已过期，在此实现相应的逻辑，弹出对话或者跳转到其他页面等,该抛出错误，会在onError中回调。
                throw new IllegalStateException("用户收取信息已过期");
            case 106:
                //比如：用户账户被禁用，在此实现相应的逻辑，弹出对话或者跳转到其他页面等,该抛出错误，会在onError中回调。
                throw new IllegalStateException("用户账户被禁用");
            case 300:
                //比如：其他乱七八糟的等，在此实现相应的逻辑，弹出对话或者跳转到其他页面等,该抛出错误，会在onError中回调。
                throw new IllegalStateException("其他乱七八糟的等");
            default:
                throw new IllegalStateException("错误代码：" + code + "，错误信息：" + msg);
        }
        //如果要更新UI，需要使用handler，可以如下方式实现，也可以自己写handler
        OkHttpUtils.getInstance().getDelivery().post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(OkHttpUtils.getContext(), "错误代码：" + code + "，错误信息：" + msg, Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }


}
