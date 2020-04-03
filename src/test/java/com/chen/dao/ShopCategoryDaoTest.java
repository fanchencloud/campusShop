package com.chen.dao;

import com.chen.BaseTest;
import com.chen.entity.ShopCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by handsome programmer.
 * User: chen
 * Date: 2019/4/10
 * Time: 17:39
 * Description:
 *
 * @author chen
 */
public class ShopCategoryDaoTest extends BaseTest {

    /**
     * 注入商铺类别数据持久层
     */
    private ShopCategoryDao shopCategoryDao;

    @Test
    public void testQueryList() {
        List<ShopCategory> shopCategories = shopCategoryDao.queryShopCategoryList(null);
        for (ShopCategory shopCategory : shopCategories) {
            System.out.println(shopCategory);
        }
    }

    @Autowired
    public void setShopCategoryDao(ShopCategoryDao shopCategoryDao) {
        this.shopCategoryDao = shopCategoryDao;
    }
}