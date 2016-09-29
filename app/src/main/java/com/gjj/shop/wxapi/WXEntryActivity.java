package com.gjj.shop.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;


import com.gjj.applibrary.app.AppLib;
import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.applibrary.log.L;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.applibrary.util.Util;
import com.gjj.shop.R;
import com.gjj.shop.app.BaseApplication;
import com.gjj.shop.main.MainActivity;
import com.gjj.shop.model.UserInfo;
import com.gjj.shop.net.ApiConstants;
import com.gjj.thirdaccess.QQAccess;
import com.gjj.thirdaccess.SinaAccess;
import com.gjj.thirdaccess.WeiXinAccess;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler, IWeiboHandler.Response, AdapterView.OnItemClickListener {

    @Bind(R.id.share_gridview)
    GridView mGridView;


    private List<Map<String, Object>> mDataList = new ArrayList<>();
    private SimpleAdapter mSimpleAdapter;

    private int[] mIconResId = {R.mipmap.weixin, R.mipmap.pyq, R.mipmap.qqz, R.mipmap.weibo};

    private String[] mIconName = new String[5];

    private String[] tag = {"image", "text"};
    private SinaAccess mSinaAccess;

    @Bind(R.id.root)
    View mMenuView;

    @Bind(R.id.pop_layout)
    View mPopLayout;

    @OnClick(R.id.share_cancel_btn)
    void cancel() {
        finish();
    }

    private BaseUiListener mQQShareListener;

    private Bitmap mBitmap;
    private String mUrl;
    private String mTitle;
    private String mDescription;
    private String mBitmapUrl;
    private int mNodeID;


    private static final String mLocalUrl = "http://image.guojj.com/exhibition/20/27/5114a05bd53556007995bb3bd8062093_180x180.jpg";
    private int mIndex;

    private WeiXinAccess mWeiXinAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_share);
        initView();

        initArguments();


        // 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
        // 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
        // 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
        // 失败返回 false，不调用上述回调
        if (savedInstanceState != null && mSinaAccess != null) {
            IWeiboShareAPI iWeiboShareAPI = mSinaAccess.getmWeiboShareAPI();
            if (iWeiboShareAPI != null) {
                iWeiboShareAPI.handleWeiboResponse(getIntent(), this);
            }
        }
		getWeiXinAccess().getmIWXAPI().handleIntent(getIntent(), this);

    }

    private void initView() {
        ButterKnife.bind(this);
        int[] to = {R.id.image, R.id.text};
        String weixinFriendTitle = getString(R.string.share_item_weixin_friend_title);
        String weixinTitle = getString(R.string.share_item_weixin_title);
//        String qqFriendTitle = getString(R.string.share_item_qq_friend_title);
        String qzoneTitle = getString(R.string.share_item_qzone_title);
        String sinaTitle = getString(R.string.share_item_sina_title);

        mIconName = new String[]{weixinFriendTitle, weixinTitle, qzoneTitle, sinaTitle};
        getData();
        mSimpleAdapter = new SimpleAdapter(this, mDataList, R.layout.share_gird_item, tag, to);
        mGridView.setAdapter(mSimpleAdapter);
        mGridView.setOnItemClickListener(this);

//        mMenuView.setOnTouchListener((v, event) -> {
//            if (event.getAction() == MotionEvent.ACTION_UP) {
//                int height = mPopLayout.getTop();
//                int y = (int) event.getY();
//                if (y < height) {
//                    finish();
//                }
//            }
//            return true;
//        });
    }

    private void initArguments() {
        Intent i = getIntent();
        Bundle bundle = i.getBundleExtra(BundleKeyConstant.KEY_SHARE_BUNDLE);
        if (bundle != null) {
            mUrl = bundle.getString(BundleKeyConstant.KEY_SHARE_URL);
            mTitle = bundle.getString(BundleKeyConstant.KEY_SHARE_TITLE);
            mDescription = bundle.getString(BundleKeyConstant.KEY_SHARE_DES);
            mBitmap = bundle.getParcelable(BundleKeyConstant.KEY_SHARE_BITMAP);
            mBitmapUrl = bundle.getString(BundleKeyConstant.KEY_SHARE_BITMAP_URL);
            mNodeID = bundle.getInt(BundleKeyConstant.KEY_SHARE_NODE_ID);
            if (mBitmap == null) {
                mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.nav05);
            }
            if (TextUtils.isEmpty(mTitle)) {
                mTitle = getString(R.string.app_name);
            }
            if (TextUtils.isEmpty(mDescription)) {
                mDescription = getString(R.string.app_name);
            }
            if (TextUtils.isEmpty(mBitmapUrl)) {
                mBitmapUrl = mLocalUrl;
            }
        } else {
            finish();
        }


    }

    public List<Map<String, Object>> getData() {
        List<Map<String, Object>> dataList = mDataList;
        for (int i = 0; i < mIconResId.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put(tag[0], mIconResId[i]);
            map.put(tag[1], mIconName[i]);
            dataList.add(map);
        }
        return dataList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_QQ_SHARE || requestCode == Constants.REQUEST_QZONE_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mQQShareListener);
        }

//        SsoHandler ssoHandler = null;
//        if (mSinaAccess != null) {
//            ssoHandler = mSinaAccess.getmSsoHandler();
//        }
//        // SSO 授权回调
//        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
//        if (ssoHandler != null) {
//            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
//        }
    }

    private WeiXinAccess getWeiXinAccess() {
        if (mWeiXinAccess == null) {
            mWeiXinAccess = new WeiXinAccess(AppLib.getContext(), ShareConstant.WEXINAPPID);
        }
        return mWeiXinAccess;
    }

    private void weixinFriendShare() {
        getWeiXinAccess().share2weixin(false, mBitmap, mUrl, mTitle, mDescription);
//		finish();
    }

    private void weixinShare() {
//		mWeiXinAccess = new WeiXinAccess(GjjApp.getInstance(), WEXINAPPID);
        getWeiXinAccess().share2weixin(true, mBitmap, mUrl, mTitle, mDescription);
//		finish();

    }

    private void qqShare() {
        mQQShareListener = new BaseUiListener();
        QQAccess qqAccess = new QQAccess(this, ShareConstant.QQAPPID);
        qqAccess.ShareToQQ(mQQShareListener, mUrl, mTitle, mDescription, mBitmapUrl);
    }

    private void qzoneShare() {
        mQQShareListener = new BaseUiListener();
        QQAccess qqAccess = new QQAccess(this, ShareConstant.QQAPPID);
        qqAccess.shareToQzone(mQQShareListener, mUrl, mTitle, mDescription, mBitmapUrl);
    }

    private void sinaShare() {
        mSinaAccess = new SinaAccess(this, ShareConstant.SINAAPPID);
//        mSinaAccess.initAPI(new WeiboAuthListener() {
//            @Override
//            public void onComplete(Bundle bundle) {
//                Oauth2AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
//                if (mAccessToken.isSessionValid()) {
//                    AccessTokenKeeper.writeAccessToken(ShareActivity.this, mAccessToken);
//                } else {
//
//                }
//                IWeiboShareAPI weiboShareAPI = mSinaAccess.getmWeiboShareAPI();
//                if (weiboShareAPI != null) {
//                    mSinaAccess.shareToWeibo(mUrl, mTitle, mDescription, mBitmap);
//                }
//
//            }
//
//            @Override
//            public void onWeiboException(WeiboException e) {
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//        });
        mSinaAccess.initAPI();
        mSinaAccess.shareToWeibo(mUrl, mTitle, mDescription, mBitmap);
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        switch (baseResponse.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                ToastUtil.shortToast(R.string.share_success);
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                ToastUtil.shortToast(R.string.share_cancel);
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                ToastUtil.shortToast(R.string.share_fail);
                break;
        }
        WXEntryActivity.this.finish();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mIndex = position;
        switch (position) {
            case 0:
                weixinFriendShare();
                break;
            case 1:
                weixinShare();
                break;
//            case 2:
//                qqShare();
//                break;
            case 2:
                qzoneShare();
                break;
            case 3:
                sinaShare();
                break;

        }
    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            ToastUtil.shortToast(R.string.share_success);
            WXEntryActivity.this.finish();
        }

        @Override
        public void onError(UiError e) {
            ToastUtil.shortToast(R.string.share_fail);
            WXEntryActivity.this.finish();
        }

        @Override
        public void onCancel() {
            ToastUtil.shortToast(R.string.share_cancel);
            WXEntryActivity.this.finish();
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if(resp instanceof SendAuth.Resp) {
                    String code = ((SendAuth.Resp) resp).code;
                } else {
                    ToastUtil.shortToast(R.string.share_success);
                }
//                StringBuilder stringBuilder = Util.getThreadSafeStringBuilder();
//                stringBuilder.append("https://api.weixin.qq.com/sns/oauth2/access_token?appid=").append(ShareConstant.WEXINAPPID).append("&secret=").append("")
//                        .append("&code=").append(code).append("&grant_type=authorization_code");
//                String url = stringBuilder.toString();
//                OkHttpUtils.get(url)
//                        .tag(this)
//                        .cacheMode(CacheMode.NO_CACHE)
//                        .execute(new JsonCallback<String>(String.class) {
//                            @Override
//                            public void onError(Call call, Response response, Exception e) {
//                                super.onError(call, response, e);
//                            }
//
//                            @Override
//                            public void onSuccess(String userInfo, Call call, Response response) {
//
//                            }
//                        });
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                ToastUtil.shortToast(R.string.share_cancel);
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                ToastUtil.shortToast(R.string.share_fail);
                break;
            default:
                ToastUtil.shortToast(R.string.share_fail);
                break;
        }
        finish();
    }

    private void goToGetMsg() {

    }

    private void goToShowMsg(ShowMessageFromWX.Req showReq) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mSinaAccess != null) {
            mSinaAccess.getmWeiboShareAPI().handleWeiboResponse(intent, this);
        }
        setIntent(intent);
        if (mWeiXinAccess != null) {
            mWeiXinAccess.getmIWXAPI().handleIntent(intent, this);
        }
    }

}