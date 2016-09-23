package com.gjj.shop.person;

import com.gjj.shop.model.ProductInfo;

import java.io.Serializable;

/**
 * Created by Chuck on 2016/8/31.
 */
public class CollectInfo implements Serializable{
    private static final long serialVersionUID = 8657677401839659329L;

    public ProductInfo product;
    public SlideView slideView;
}
