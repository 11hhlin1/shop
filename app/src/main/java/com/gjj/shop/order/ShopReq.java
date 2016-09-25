package com.gjj.shop.order;

import java.util.List;

/**
 * Created by user on 16/9/24.
 */
public class ShopReq {
    public String shopId;
    public long addressId;
    public List<OrderGoodReq> goodsList;
}
