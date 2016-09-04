package com.gjj.shop.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Chuck on 2016/7/25.
 */
public class ProductInfo implements Serializable{

    private static final long serialVersionUID = -6208697943437028908L;
    /**
     * goodsId : 769802415720366080
     * prePrice : 34
     * thumbList : ["/upload/thumb/20160827/7df6db458750ce86.jpg","/upload/thumb/20160827/a38cfab3c3b4d9e6.jpg","/upload/thumb/20160827/33b5e7c3ff0271ed.jpg","/upload/thumb/20160827/7f0c22baa5799f84.jpg","/upload/thumb/20160827/a7a2f0484dd363a8.jpg"]
     * marks : {"颜色":["红色"，"黑色"]}
     * title : 官方回复电话
     * type :
     * createTime : 1472370172805
     * logoThumb : /upload/thumb/20160828/80a2f7a35ebec9b9.png
     * name : 换个
     * logo : /upload/image/20160828/80a2f7a35ebec9b9.png
     * details : 1
     * curPrice : 454
     * id : 2
     * shopId : 769523828060586000
     * imageList : ["/upload/image/20160827/7df6db458750ce86.jpg","/upload/image/20160827/a38cfab3c3b4d9e6.jpg","/upload/image/20160827/33b5e7c3ff0271ed.jpg","/upload/image/20160827/7f0c22baa5799f84.jpg","/upload/image/20160827/a7a2f0484dd363a8.jpg"]
     * status :
     * shopName : 黑店
     * shopLogo : /upload/image/20160827/8a0e81850500c2c9.jpg
     * shopThumb : /upload/thumb/20160827/8a0e81850500c2c9.jpg
     */

    public String goodsId;
    public double prePrice;
    public List<String> thumbList;
    public String tags;
    public String title;
    public String type;
    public String contactPhone;
    public long createTime;
    public String logoThumb;
    public String name;
    public String logo;
    public String details;
    public double curPrice;
    public int id;
    public String shopId;
    public List<String> imageList;
    public String status;
    public String shopName;
    public String shopLogo;
    public String shopThumb;


}
