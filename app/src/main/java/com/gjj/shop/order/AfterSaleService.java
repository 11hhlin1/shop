package com.gjj.shop.order;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/22.
 */
public class AfterSaleService implements Serializable{

    private static final long serialVersionUID = 3308741244651787183L;
    /**
     * title : 退货退款
     * content : 哈哈健健康康
     * status : 0
     */

    private String title;
    private String content;
    private int status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
