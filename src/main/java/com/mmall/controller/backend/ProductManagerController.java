package com.mmall.controller.backend;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by Yuk on 2018/2/5.
 */
@Controller
@RequestMapping("/backManager/product/")
public class ProductManagerController {

    @Autowired
    private IProductService iProductService;

    /**
     * 保存或更新产品
     * @param product
     * @return
     */
    @RequestMapping(value = "save_product.do")
    @ResponseBody
    public ServerResponse saveProduct(Product product){
        return iProductService.saveOrUpdateProduct(product);
    }

    /**
     * 修改产品销售状态
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping(value = "set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(Integer productId, Integer status){
        return iProductService.setSaleStatus(productId,status);
    }

    /**
     * 获取产品详情
     * @param productId
     * @return
     */
    @RequestMapping(value = "detail.do")
    @ResponseBody
    public ServerResponse getDetail(Integer productId){
        return iProductService.managerProductDetail(productId);
    }

    /**
     * 获取商品列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "list.do")
    @ResponseBody
    public ServerResponse getProductList(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        return iProductService.getProductList(pageNum,pageSize);
    }
}
