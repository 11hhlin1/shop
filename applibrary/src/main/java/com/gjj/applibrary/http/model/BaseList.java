package com.gjj.applibrary.http.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/14.
 */
public class BaseList<T> implements Serializable{
    private static final long serialVersionUID = -3716323801410715310L;
    //    public Class<T> clazz;
//    public BaseList(Class<T> clazz) {
//        this.clazz = clazz;
//    }
    public List<T> list;
}
