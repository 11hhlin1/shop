package com.gjj.shop.net;

/**
 * Created by user on 16/8/14.
 */
public class UrlUtil {
    public static String getHttpUrl(String imageUrl){
        return ApiConstants.host + imageUrl;
    }
}
