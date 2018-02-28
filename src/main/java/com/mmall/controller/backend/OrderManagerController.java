package com.mmall.controller.backend;

import com.mmall.common.ServerResponse;
import com.mmall.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 后台订单管理
 * Created by Yuk on 2018/2/28.
 */
@Controller
@RequestMapping("/backManager/order")
public class OrderManagerController {
    @Autowired
    IOrderService iOrderService;

    /**
     * 后台订单列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "list.do")
    @ResponseBody
    public ServerResponse orderList(@RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
                               @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        return iOrderService.manageList(pageNum,pageSize);
    }

    /**
     * 后台订单详情
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "detail.do")
    @ResponseBody
    public ServerResponse orderDetail(Long orderNo){
        return iOrderService.managerDetail(orderNo);
    }

    /**
     * 后台根据订单号搜索订单
     * @param orderNo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "search.do")
    @ResponseBody
    public ServerResponse orderSearch(Long orderNo,@RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
                                      @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        return iOrderService.managerSearch(orderNo,pageNum,pageSize);
    }

    /**
     * 发货
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "send_goods.do")
    @ResponseBody
    public ServerResponse orderSendGoods(Long orderNo){
        return iOrderService.managerSendGoods(orderNo);
    }
}
