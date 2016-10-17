package com.gjj.shop.index;

import java.io.Serializable;

/**
 * Created by Chuck on 2016/9/1.
 */
public class CommentInfo implements Serializable{
    private static final long serialVersionUID = -8455709251524076511L;

    /**
     * content : hhjhhh
     * id : 10
     * uid : 771196826094141440
     * goodsId : 772370913730494465
     * createTime : 1476685873197
     * thumbnail : /res/img/user-avatar.png
     * nickname : 马利
     * star : 5
     * avatar : /res/img/user-avatar.png
     * commentId : 787903775149916160
     * orderId : 780589555752370176
     */

    public String content;
    public String uid;
    public String goodsId;
    public long createTime;
    public String thumbnail;
    public String nickname;
    public int star;
    public String avatar;
    public String commentId;
    public String orderId;

}
