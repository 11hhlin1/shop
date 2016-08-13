package com.gjj.shop.net;

/**
 * Created by user on 16/7/16.
 */
public class ApiConstants {
    public static final String host = "http://120.24.69.203:8080/";
    public static final String LOGIN = host + "user/login";
    public static final String REGISTER = host+ "user/register";
    public static final String GET_SMS_CODE = host+ "user/getsmscode";
    public static final String GET_USER_INFO = host+ "user/info";
    public static final String FIND_PSW = host+ "user/findPassword";
    public static final String COMMUNITY_PUBLISH = host+ "community/publish";
    public static final String COMMUNITY_LIST = host+ "community/infoList";
    public static final String UPLOAD_IMAGE = host+ "service/upload/image";
}
