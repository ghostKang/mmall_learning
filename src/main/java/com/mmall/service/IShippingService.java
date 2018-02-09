package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

/**
 * Created by Yuk on 2018/2/9.
 */
public interface IShippingService {

    ServerResponse add(Shipping shipping);

    ServerResponse delete(Integer userId,Integer shippingId);

    ServerResponse update(Shipping shipping);

    ServerResponse select(Integer userId,Integer shippingId);

    ServerResponse list(Integer userId,int pageNum,int pageSize);
}
