package com.gjj.thirdaccess;

import android.content.Context;
import android.util.Log;

import java.io.*;

/**
 * Title:过家家-项目经理
 * Description:
 * Copyright: Copyright (c) 2016
 * Company: 深圳市过家家
 * version: 1.0.0.0
 * author: jack
 * createDate 2016/2/23
 */
public class AppIdMgr {
    /**
     * 所有的APPID号,格式:QQxxxxxx|WEIXINxxxxxxx|SINAxxxxxx|WEIXINKEYxxxxxx
     */
    private String mAppIds;
    private String QQAPPID = "1104708829";
    private String WEXINAPPID = "wx306491ce9688ceca";
    private String SINAAPPID = "2969236885";

    private Context mContext;

    public AppIdMgr(Context context) {
        this.mContext = context;
        init();
    }

    /**
     * 初始化渠道号
     *
     *
     *
     */
    public void init() {
        InputStream is = null;
        try {
            is = mContext.getAssets().open("thirdaccessid");
            readChannelFile(is);
        } catch (IOException e) {
            Log.e("IOException", e.toString());
        } finally {
            closeCloseable(is);
        }

    }

    public static void closeCloseable(Closeable obj) {
        try {
            // 修复小米MI2的JarFile没有实现Closeable导致崩溃问题
            if (obj instanceof Closeable && obj != null) {
                obj.close();
            }

        } catch (IOException e) {
            Log.w("IOException", e.toString());
        }
    }

    /**
     * 获取渠道号
     *
     * @param in
     * @throws IOException
     */
    public void readChannelFile(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        String readStr = bufferedReader.readLine();
        try {
            mAppIds = AESCrypt.decrypt(AESCrypt.key,readStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getQQAPPID() {
        if (QQAPPID == null)
            if (mAppIds != null) {
                String[] appid_array = mAppIds.split("\\|");

                for (String str : appid_array) {
                    if (str.contains("QQ")) {
                        QQAPPID = str.replace("QQ", "");
                        return QQAPPID;
                    }
                }
            }
        return QQAPPID;
    }

    public String getWEXINAPPID() {
        if (WEXINAPPID == null)
            if (mAppIds != null) {
                String[] appid_array = mAppIds.split("|");
                for (String str : appid_array) {
                    if (str.contains("WEIXIN")) {
                        WEXINAPPID = str.replace("WEIXIN", "");
                        return WEXINAPPID;
                    }
                }
            }
        return WEXINAPPID;
    }

    public String getSINAAPPID() {
        if (SINAAPPID == null)
            if (mAppIds != null) {
                String[] appid_array = mAppIds.split("|");
                for (String str : appid_array) {
                    if (str.contains("SINA")) {
                        SINAAPPID = str.replace("SINA", "");
                        return SINAAPPID;
                    }
                }
            }
        return SINAAPPID;
    }
}
