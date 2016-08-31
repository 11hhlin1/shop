package com.gjj.shop.person;

import java.io.Serializable;

/**
 * Created by Chuck on 2016/8/31.
 */
public class CollectInfo implements Serializable{
    private static final long serialVersionUID = 8657677401839659329L;

    public String title;
    public String msg;
    public String icon;
    public double prePrice;
    public double curPrice;
    public SlideView slideView;
}
