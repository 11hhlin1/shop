package com.gjj.shop.index;

import java.io.Serializable;

/**
 * Created by Chuck on 2016/9/12.
 */
public class ShopInfo implements Serializable{
    private static final long serialVersionUID = -3211981110072530493L;

    /**
     * id : 5
     * shopId : 771231952710664200
     * name : 海绵包包
     * title : 打算打算打算
     * details : 大师的撒的
     * thumbnail : /upload/thumb/20160901/39a83a40f01c9f98.png
     * image : /upload/image/20160901/39a83a40f01c9f98.png
     * contactPhone : 4005882935
     * type : null
     * createTime : 1472711000980
     */

    public long shopId;
    public String name;
    public String title;
    public String details;
    public String thumbnail;
    public String image;
    public String contactPhone;
    public long createTime;


}
