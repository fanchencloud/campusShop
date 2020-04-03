package com.chen.dao;

import com.chen.BaseTest;
import com.chen.entity.ProductImage;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by handsome programmer.
 * User: chen
 * Date: 2019/3/30
 * Time: 1:13
 * Description:
 *
 * @author chen
 */
public class ProductImageDaoTest extends BaseTest {
    private static Logger logger = Logger.getLogger(AreaDaoTest.class);

    /**
     * 注入持久层
     */
    private ProductImageDao productImageDao;

    @Test
    public void testAddProductImageList() {
        ProductImage productImage1 = new ProductImage();
        productImage1.setImgPath("testPath");
        productImage1.setImgDesc("test Description");
        productImage1.setPriority(3);
        productImage1.setProductId(123);
        productImage1.setCreateTime(new Date());

        ProductImage productImage2 = new ProductImage();
        productImage2.setImgPath("testPath2");
        productImage2.setImgDesc("test Description2");
        productImage2.setPriority(2);
        productImage2.setProductId(123);
        productImage2.setCreateTime(new Date());

        List<ProductImage> productImageList = new ArrayList<>(2);
        productImageList.add(productImage1);
        productImageList.add(productImage2);
        int i = productImageDao.bulkInsertProductImage(productImageList);
        System.out.println(i);
    }

    @Test
    public void testSelect() {
        List<ProductImage> productImages = productImageDao.queryProductImageByProductId(123);
        for (ProductImage p : productImages) {
            System.out.println(p);
        }
    }

    @Test
    public void testDeleteProductImage() {
        int i = productImageDao.deleteProductImageByProductId(123);
        System.out.println(i);
    }

    @Test
    public void testDeleteProductImage2() {
        int i = productImageDao.deleteProductImageByProductImageId(3);
        System.out.println(i);
    }

    @Autowired
    public void setProductImageDao(ProductImageDao productImageDao) {
        this.productImageDao = productImageDao;
    }
}