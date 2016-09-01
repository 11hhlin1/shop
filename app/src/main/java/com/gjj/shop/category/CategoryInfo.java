package com.gjj.shop.category;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chuck on 2016/9/1.
 */
public class CategoryInfo implements Serializable{
    private static final long serialVersionUID = -5951114758016657919L;
    public String cate;
    public List<NextCategoryInfo> categoryInfos;

}
