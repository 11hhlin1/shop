package com.gjj.shop.index;

import com.gjj.shop.index.foreign.CategoryData;
import com.gjj.shop.model.ProductInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chuck on 2016/9/13.
 */
public class IndexData implements Serializable{
    private static final long serialVersionUID = -2225518971536783369L;

    /**
     * goodsId : 772370913730494500
     * logo : /upload/image/20160904/125d5a9179da0998.png
     * logoThumb : /upload/thumb/20160904/125d5a9179da0998.png
     */

    public List<BannerBean> banner;
    /**
     * id : 7
     * goodsId : 772370913730494500
     * shopId : 771347008790200300
     * name : 香奈儿包
     * title : 归档大气上档次的法国巴黎香奈儿包包，女性朋友装X的必备用品，快快入手吧
     * logo : /upload/image/20160904/125d5a9179da0998.png
     * logoThumb : /upload/thumb/20160904/125d5a9179da0998.png
     * imageList : ["/upload/image/20160904/5b3f571da249ab80.png","/upload/image/20160904/20b2a38128cb2b2c.png","/upload/image/20160904/830f14a983734609.png","/upload/image/20160904/f1251d81a9331524.png"]
     * thumbList : ["/upload/thumb/20160904/5b3f571da249ab80.png","/upload/thumb/20160904/20b2a38128cb2b2c.png","/upload/thumb/20160904/830f14a983734609.png","/upload/thumb/20160904/f1251d81a9331524.png"]
     * curPrice : 9000
     * prePrice : 8900
     * tags : {"颜色":["红色","黑色","黄色","白色","绿色"]}
     * details : 描述：归档大气上档次的法国巴黎香奈儿包包，女性朋友装X的必备用品，快快入手吧，归档大气上档次的法国巴黎香奈儿包包，女性朋友装X的必备用品，快快入手吧
     * type : null
     * status : null
     * createTime : 1472982550448
     */

    public List<ProductInfo> promotion;
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
    public List<ActivityInfo> tags;
    public List<ShopInfo> shop;

    public static class BannerBean {
        public long goodsId;
        public String logo;
        public String logoThumb;
        public int sortId;

    }


}
