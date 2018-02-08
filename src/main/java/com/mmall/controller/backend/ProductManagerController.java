package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * Created by Yuk on 2018/2/5.
 */
@Controller
@RequestMapping("/backManager/product/")
public class ProductManagerController {

    @Autowired
    private IProductService iProductService;

    @Autowired
    IFileService iFileService;

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
    @RequestMapping(value = "product_detail.do")
    @ResponseBody
    public ServerResponse getProductDetail(Integer productId){
        return iProductService.managerProductDetail(productId);
    }

    /**
     * 获取商品列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "product__list.do")
    @ResponseBody
    public ServerResponse getProductList(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        return iProductService.managerProductList(pageNum,pageSize);
    }

    /**
     * 根据商品名称搜索商品
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "search_product.do")
    @ResponseBody
    public ServerResponse searchProduct(String productName, Integer productId,
                                        @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        return iProductService.searchProduct(productName,productId,pageNum,pageSize);
    }

    /**
     * springMMV上传
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "upload_file.do")
    @ResponseBody
    public ServerResponse uploadFile(@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request){
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.uploadFile(file,path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix").trim()+targetFileName;

        Map fileMap = Maps.newHashMap();
        fileMap.put("uri",targetFileName);
        fileMap.put("url",url);
        return ServerResponse.createBySuccess(fileMap);
    }

    /**
     * 富文本上传
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = " richText_img_upload.do")
    @ResponseBody
    public Map richTextImgUpload(@RequestParam("upload_file") MultipartFile file,
                                 HttpServletRequest request, HttpServletResponse response){
        // 富文本对于返回值有自己的要求，我们使用simditor,按照simditor要求进行返回
        /*{
            "success":"true/false",
            "msg":"",
            "file_path":""
        }*/
        Map map = Maps.newHashMap();
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.uploadFile(file,path);
        if(StringUtils.isBlank(targetFileName)){
            map.put("success",false);
            map.put("msg","上传失败");
            return map;
        }
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix").trim()+targetFileName;
        map.put("success",true);
        map.put("msg","上传成功");
        map.put("file_path",url);
        response.addHeader("Access-Control-Allow-Headers","X-File-Name");// 规定修改Headeer
        return map;
    }

}
