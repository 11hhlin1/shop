/*
    Fmms Android Client, AppRegister
    Copyright (c) 2015 Fmms Tech Company Limited
*/

package com.gjj.shop.wxapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * [A brief description]
 * 
 * @author huxinwu
 * @version 1.0
 * @date 2015-8-4
 * 
 **/
public class AppRegister extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		final IWXAPI api = WXAPIFactory.createWXAPI(context, null);
		api.registerApp(ShareConstant.WEXINAPPID);
	}

}
