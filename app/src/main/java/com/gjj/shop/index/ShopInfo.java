package com.gjj.shop.index;

import java.io.Serializable;

/**
 * Created by Chuck on 2016/9/12.
 */
public class ShopInfo implements Serializable{
    private static final long serialVersionUID = -3211981110072530493L;


    /**
     * id : 6
     * shopId : 771347008790200300
     * name :  手机店
     * title : 卖手机的
     * details : 就是个卖手机的，咋的
     * logo : /upload/image/20160901/94c41da8b21c6875.jpg
     * logoThumb : /upload/thumb/20160901/94c41da8b21c6875.jpg
     * image : /upload/image/20160901/94c41da8b21c6875.jpg
     * thumbnail : /upload/thumb/20160901/94c41da8b21c6875.jpg
     * contactPhone : 110
     * type : null
     * createTime : 1472738432487
     */

    public long shopId;
    public String name;
    public String title;
    public String details;
    public String logo;
    public String logoThumb;
    public String image;
    public String thumbnail;
    public String contactPhone;
    public long createTime;

}
