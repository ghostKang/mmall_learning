package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Yuk on 2018/2/9.
 */
@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    ICartService iCartService;

    /**
     * 查询购物车
     * @param session
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<CartVo> list(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return iCartService.list(user.getId());
    }

    /**
     * 添加到购物车
     * @param session
     * @param productId
     * @param count
     * @return
     */
    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse<CartVo> add(HttpSession session, Integer productId, Integer count){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return iCartService.add(user.getId(),productId,count);
    }

    /**
     * 更新购物车
     * @param session
     * @param productId
     * @param count
     * @return
     */
    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse<CartVo> update(HttpSession session, Integer productId, Integer count){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return iCartService.update(user.getId(),productId,count);
    }

    /**
     * 购物车中删除商品
     * @param session
     * @param productIds
     * @return
     */
    @RequestMapping("delete.do")
    @ResponseBody
    public ServerResponse<CartVo> delete(HttpSession session, String productIds){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return iCartService.delete(user.getId(),productIds);
    }

    /**
     * 全选
     * @param session
     * @return
     */
    @RequestMapping("checkAll.do")
    @ResponseBody
    public ServerResponse<CartVo> checkAll(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return iCartService.checkOrUnCheck(user.getId(),Const.Cart.CHECK,null);
    }

    /**
     * 全反选
     * @param session
     * @return
     */
    @RequestMapping("unCheckAll.do")
    @ResponseBody
    public ServerResponse<CartVo> unCheckAll(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return iCartService.checkOrUnCheck(user.getId(),Const.Cart.UN_CHECK,null);
    }

    /**
     * 单独选
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping("check.do")
    @ResponseBody
    public ServerResponse<CartVo> unCheck(HttpSession session,Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return iCartService.checkOrUnCheck(user.getId(),Const.Cart.CHECK,productId);
    }

    /**
     * 单独反选
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping("unCheck.do")
    @ResponseBody
    public ServerResponse<CartVo> check(HttpSession session,Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return iCartService.checkOrUnCheck(user.getId(),Const.Cart.UN_CHECK,productId);
    }

    /**
     * 查询当前用户的购物车里里面的商品数量
     * @param session
     * @return
     */
    @RequestMapping("getProductCount.do")
    @ResponseBody
    public ServerResponse<Integer> getProductCount(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return iCartService.getCartProductCount(user.getId());
    }
}
