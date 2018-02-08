package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by Yuk on 2018/2/5.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    private static Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 添加商品类别
     * @param categoryName
     * @param parentId
     * @return
     */
    public ServerResponse addCategory(String categoryName,Integer parentId){
        if(parentId == null || StringUtils.isBlank(categoryName)){
            return  ServerResponse.createByErrorMessage("添加品类参数错误");
        }

        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        category.setStatus(true);

        int rowCount = categoryMapper.insert(category);
        if(rowCount > 0){
            return ServerResponse.createBySuccessMessage("添加品类成功");
        }

        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    /**
     * 更新商品
     * @param categoryId
     * @param categoryName
     * @return
     */
    public ServerResponse setCategoryName(Integer categoryId,String categoryName){
        if(categoryId == null || StringUtils.isBlank(categoryName)){
            return  ServerResponse.createByErrorMessage("更新品类参数错误");
        }

        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount > 0){
            return ServerResponse.createBySuccessMessage("修改品类成功");
        }
        return ServerResponse.createByErrorMessage("修改品类失败");
    }

    /**
     * 获取子节点平级商品
     * @param categoryId
     * @return
     */
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId){
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if(CollectionUtils.isEmpty(categoryList)){
            logger.info("未找到当前分类的子分类");
            return ServerResponse.createByErrorMessage("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    /**
     * 递归查询本节点以及孩子节点的id
     * @param categoryId
     * @return
     */
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId){
        Set<Category> categorySet = Sets.newHashSet();
        categorySet = findChildrenCategory(categorySet,categoryId);
        List<Integer> categoryIdList = Lists.newArrayList();
        if(categoryId != null){
            for(Category categoryItem : categorySet){
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    // 递归算法，算出所有子节点
    public Set<Category> findChildrenCategory(Set<Category> categorySet,Integer categoryId){
        // 获取父节点
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category != null){
            // 一定要判断是否为空
            categorySet.add(category);
        }

        // 查找子节点
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category categoryItem :categoryList ){
            findChildrenCategory(categorySet,categoryItem.getId());
        }
        return categorySet;
    }
}
