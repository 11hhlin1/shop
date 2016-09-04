package com.gjj.shop.shopping;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/7/30.
 */
public class ShopInfo implements Serializable {
    private static final long serialVersionUID = 7521979946290122221L;

    public String shopId;
    public String shopName;
    public String shopImage;
    public String shopThumb;
    public List<GoodsInfo> goodsList;
}
