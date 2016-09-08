package com.gjj.shop.address;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 16/9/8.
 */
public class AddressListInfo implements Serializable{
    public long defaultAddressId;
    public List<AddressInfo> addressList;
}
