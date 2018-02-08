package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

/**
 * Created by Yuk on 2018/2/6.
 */
public interface IProductService {

    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse setSaleStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVo> managerProductDetail(Integer productId);

    ServerResponse<PageInfo> managerProductList(int pageNum, int pageSize);

    ServerResponse searchProduct(String productName, Integer productId, int pageNum, int pageSize);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerResponse getProductBykeywordAndCategoryId(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);
}
