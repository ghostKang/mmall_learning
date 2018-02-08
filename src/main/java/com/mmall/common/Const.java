package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 定义常量
 * Created by Yuk on 2018/2/4.
 */
public class Const {
    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    /**
     * 内部接口定义角色组
     */
    public interface Role{
        int ROLE_CUSTOMER = 0;// 普通用户
        int ROLE_ADMIN = 1;// 管理员
    }

    /**
     * 商品排序
     */
    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
    }

    public enum ProductStatusEnum{
        ON_SALE(1,"在线状态");
        private Integer code;
        private String value;

        ProductStatusEnum(Integer code,String value){
            this.code = code;
            this.value = value;
        }

        public Integer getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }
}
