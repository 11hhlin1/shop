package com.gjj.shop.net;

/**
 * Created by user on 16/7/16.
 */
public class ApiConstants {
    public static final String host = "http://120.24.69.203:8080";
//    public static final String host = "http://192.168.1.110:8080";

    public static final String LOGIN = host + "/user/login";
    public static final String THIRD_LOGIN = host + "/user/thirdLogin";
    public static final String REGISTER = host+ "/user/register";
    public static final String GET_SMS_CODE = host+ "/common/sendPhoneCode";
    public static final String CHANGE_PSW = host+ "/user/updatePassword";
    public static final String GET_USER_INFO = host+ "/user/info";
    public static final String FIND_PSW = host+ "/user/findPassword";
    public static final String LOGOUT = host+ "/user/logout";
    public static final String COMMUNITY_PUBLISH = host+ "/community/publish";
    public static final String COMMUNITY_LIST = host+ "/community/infoList";
    public static final String SORT_LEFT_LIST = host+ "/sort/infoList";
    public static final String UPLOAD_IMAGE = host+ "/service/upload/image";
    public static final String UPDATE_USER_INFO = host+ "/user/updateInfo";
    public static final String PRODUCT_LIST = host+ "/goods/infoList";
    public static final String ADD_CART = host+ "/cart/add";
    public static final String DELETE_CART = host+ "/cart/deleteList";
    public static final String CART_LIST = host+ "/cart/infoList";
    public static final String CREATE_ORDER = host+ "/order/create";
    public static final String CANCEL_ORDER = host+ "/order/cancel";
    public static final String RECEIVE_ORDER = host+ "/order/receive";
    public static final String ORDER_LIST = host+ "/order/infoList";
    public static final String PRODUCT_INFO = host+ "/goods/info";
    public static final String SHOP_INFO = host+ "/shop/info";
    public static final String ADD_ADDRESS = host+ "/user/address/add";
    public static final String GET_ADDRESS_LIST = host+ "/user/address/infoList";
    public static final String DELETE_ADDRESS = host+ "/user/address/delete";
    public static final String UPDATE_ADDRESS = host+ "/user/address/update";
    public static final String HOME_PAGE = host+ "/page/home";
    public static final String COLLECT_LIST = host+ "/collect/infoList";
    public static final String COLLECT_GOOD = host+ "/collect/edit";
    public static final String ADD_FEEDBACK = host+ "/feedback/add";
    public static final String PAY_BY_WEIXIN = host+ "/pay/wx/appOrder";
    public static final String PAY_BY_ALIPAY = host+ "/pay/alipay/signatures";
    public static final String COMMENT_PRODUCT = host+ "/comment/publish";
    public static final String COMMENT_INFOLIST = host+ "/comment/infoList";

}
