package com.chen.service.impl;

import com.chen.dao.ProductCategoryDao;
import com.chen.entity.ProductCategory;
import com.chen.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by handsome programmer.
 * User: chen
 * Date: 2019/3/28
 * Time: 14:48
 * Description:
 * h
 *
 * @author chen
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    /**
     * 注入店铺商品类别持久层
     */
    private ProductCategoryDao productCategoryDao;

    @Override
    public List<ProductCategory> queryProductCategoryListByShopId(int shopId) {
        List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryListByShopId(shopId);
        for (ProductCategory productCategory : productCategoryList) {
            productCategory.setShopId(null);
        }
        return productCategoryList;
    }

    @Override
    public boolean addProductCategory(ProductCategory productCategory) {
        return productCategoryDao.addProductCategory(productCategory) != 0;
    }

    @Override
    public void deleteProductCategory(int productCategoryId) {
        productCategoryDao.deleteProductCategory(productCategoryId);
    }

    @Override
    public boolean modifyProductCategory(ProductCategory productCategory) {
        productCategory.setLastEditTime(new Date());
        return productCategoryDao.modifyProductCategory(productCategory) != 0;
    }

    @Override
    public ProductCategory queryProductCategoryByProductCategoryId(int productCategoryId) {
        return productCategoryDao.queryProductCategoryByProductCategoryId(productCategoryId);
    }

    @Autowired
    public void setProductCategoryDao(ProductCategoryDao productCategoryDao) {
        this.productCategoryDao = productCategoryDao;
    }
}
