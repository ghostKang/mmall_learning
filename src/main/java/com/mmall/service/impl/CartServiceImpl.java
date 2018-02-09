package com.mmall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Yuk on 2018/2/9.
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService {

    @Autowired
    CartMapper cartMapper;

    @Autowired
    ProductMapper productMapper;

    /**
     * 添加到购物车
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    public ServerResponse<CartVo> add(Integer userId,Integer productId,Integer count){
        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIDProductId(userId,productId);
        if(cart == null){
            // 要添加的商品不在购物车中
            Cart cart1 = new Cart();
            cart1.setProductId(productId);
            cart1.setChecked(Const.Cart.CHECK);
            cart1.setUserId(userId);
            cart1.setQuantity(count);
            int insertCount = cartMapper.insert(cart1);
            if(insertCount > 0){
                return ServerResponse.createBySuccessMessage("添加商品成功");
            }
            return ServerResponse.createByErrorMessage("添加商品失败");
        }else{
            // 要添加的商品已存在购物车中，直接修改数量
            count = cart.getQuantity()+count;
            cart.setQuantity(count);
            int updateCount = cartMapper.updateByPrimaryKeySelective(cart);
            if(updateCount <= 0){
                return ServerResponse.createByErrorMessage("修改商品数量失败");
            }
        }
        return this.list(userId);
    }

    /**
     * 更新购物车
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    public ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count){
        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIDProductId(userId,productId);
        if(cart != null){
            cart.setQuantity(count);
            int updateCount = cartMapper.updateByPrimaryKeySelective(cart);
            if(updateCount <= 0){
                return ServerResponse.createByErrorMessage("修改商品数量失败");
            }
        }
        return this.list(userId);
    }

    /**
     * 购物车中删除商品
     * @param userId
     * @param productIds
     * @return
     */
    public ServerResponse<CartVo> delete(Integer userId,String productIds){
        List<String> productIdList = Splitter.on(",").splitToList(productIds);// 字符串转集合
        if(CollectionUtils.isEmpty(productIdList)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteByUserIdProductIds(userId,productIdList);
        return this.list(userId);
    }

    /**
     * 查询购物车
     * @param userId
     * @return
     */
    public ServerResponse<CartVo> list(Integer userId) {
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    /**
     * 全选与反选
     * @param userId
     * @return
     */
    public ServerResponse<CartVo> checkOrUnCheck(Integer userId,Integer checked,Integer productId) {
        if(checked == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.updateCheckOrUnCheck(userId,checked,productId);
        return this.list(userId);
    }

    /**
     * 购物车商品数量
     * @param userId
     * @return
     */
    public ServerResponse<Integer> getCartProductCount(Integer userId){
        return ServerResponse.createBySuccess(cartMapper.selectCartProductCountByUserId(userId));
    }

    /**
     * 获取购物车列表
     * @param userId
     * @return
     */
    private CartVo getCartVoLimit(Integer userId){
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);// 获取购物车

        List<CartProductVo> cartProductVoList = Lists.newArrayList();

        BigDecimal cartTotalPrice = new BigDecimal("0");

        if(CollectionUtils.isNotEmpty(cartList)){
            // 购物车不为空
            for(Cart cartItem : cartList){
                // 遍历购物车信息
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(userId);
                cartProductVo.setProductId(cartItem.getProductId());

                // 获取商品信息
                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if(product != null){
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());
                    //判断库存
                    int buyLimitCount = 0;
                    if(product.getStock()>=cartItem.getQuantity()){
                        // 库存充足
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    }else{
                        // 超出库存
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAILD);
                        buyLimitCount = product.getStock();
                        Cart cart = new Cart();
                        cart.setId(cartItem.getId());
                        cart.setQuantity(buyLimitCount);
                        // 更新为有效库存
                        cartMapper.updateByPrimaryKeySelective(cart);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    // 计算商品总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),buyLimitCount));
                    cartProductVo.setProductChecked(cartItem.getChecked());
                }
                if(cartItem.getChecked() == Const.Cart.CHECK){
                    // 如果当前商品已勾选，将总价更新到购物车总价中
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return cartVo;
    }

    /**
     * 判断购物车中的商品是否全选
     * @param userId
     * @return 全选：true
     */
    private boolean getAllCheckedStatus(Integer userId){
        if(userId == null){
            return false;
        }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
    }

}
