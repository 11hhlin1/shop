package com.gjj.shop.shopping;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chuck on 2016/8/3.
 */
public class ShopAdapterInfo implements Serializable{
    public boolean isSel;
    public String shopId;
    public String shopName;
    public String shopImage;
    public String shopThumb;
    public List<GoodsAdapterInfo> goodsList;
}
