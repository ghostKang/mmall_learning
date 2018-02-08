package com.mmall.controller.portal;

import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Yuk on 2018/2/8.
 */
@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    IProductService iProductService;

    /**
     * 前台获取产品详情
     * @param productId
     * @return
     */
    @RequestMapping("get_product_detail.do")
    @ResponseBody
    public ServerResponse getProductDetail(Integer productId){
        return iProductService.getProductDetail(productId);
    }

    @RequestMapping("get_by_keyword.do")
    @ResponseBody
    public ServerResponse getProductBykeywordAndCategoryId(@RequestParam(value = "keyword",required = false) String keyword,
                                                           @RequestParam(value = "categoryId",required = false)Integer categoryId,
                                                           @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
                                                           @RequestParam(value = "pageSize",defaultValue = "10")int pageSize,
                                                           @RequestParam(value = "orderBy",defaultValue = "") String orderBy){
        return iProductService.getProductBykeywordAndCategoryId(keyword,categoryId,pageNum,pageSize,orderBy);
    }
}
