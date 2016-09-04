package com.gjj.shop.net;

/**
 * Created by user on 16/7/16.
 */
public class ApiConstants {
    public static final String host = "http://120.24.69.203:8080";
    public static final String LOGIN = host + "/user/login";
    public static final String REGISTER = host+ "/user/register";
    public static final String GET_SMS_CODE = host+ "/common/sendPhoneCode";
    public static final String CHANGE_PSW = host+ "/user/updatePassword";
    public static final String GET_USER_INFO = host+ "/user/info";
    public static final String FIND_PSW = host+ "/user/findPassword";
    public static final String LOGOUT = host+ "/user/logout";
    public static final String COMMUNITY_PUBLISH = host+ "/community/publish";
    public static final String CART_LIST = host+ "/cart/infoList";
    public static final String COMMUNITY_LIST = host+ "/community/infoList";
    public static final String UPLOAD_IMAGE = host+ "/service/upload/image";
    public static final String UPDATE_USER_INFO = host+ "/user/updateInfo";
    public static final String PRODUCT_LIST = host+ "/goods/infoList";
    public static final String ADD_CART = host+ "/cart/add";
    public static final String PRODUCT_INFO = host+ "/goods/info";
    public static final String SHOP_INFO = host+ "/shop/info";
}
