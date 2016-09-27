package com.gjj.shop.order;

import com.gjj.shop.address.AddressInfo;
import com.gjj.shop.shopping.GoodsInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/14.
 */
public class OrderInfo implements Serializable{
    private static final long serialVersionUID = -1744465433999477993L;



    public long createTime;
    public int status;
    public String shopName;
    public long shopId;
    public String shopLogoThumb;
    public AddressInfo address;
    public long orderId;
    public String shopLogo;

    public List<GoodsInfo> goodsList;
}
