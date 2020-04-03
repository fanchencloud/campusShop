package com.chen.dao;

import com.chen.BaseTest;
import com.chen.entity.Area;
import com.chen.entity.PersonInfo;
import com.chen.entity.Shop;
import com.chen.entity.ShopCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by handsome programmer.
 * User: chen
 * Date: 2019/3/12
 * Time: 16:29
 * Description:
 *
 * @author chen
 */
public class ShopDaoTest extends BaseTest {

    /**
     * 注入店铺信息持久化层
     */
    private ShopDao shopDao;

    @Test
    public void testAddShop() {
        Shop shop = new Shop();
        PersonInfo personInfo = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();
        personInfo.setId(12);
        personInfo.setName("测试");
        personInfo.setGender("1");
        personInfo.setPhone("18720878478");
        personInfo.setEmail("1441088980@qq.com");
        personInfo.setHeadPortrait("test");
        personInfo.setEnableStatus(1);
        personInfo.setUserType(2);

        area.setAreaId(3);

        shopCategory.setShopCategoryId(33);

        shop.setOwnerId(personInfo.getId());
        shop.setAreaId(area.getAreaId());
        shop.setShopCategoryId(shopCategory.getShopCategoryId());
        shop.setShopName("尘");
        shop.setShopDescription("欢迎光临！");
        shop.setShopAddress("哈哈");
        shop.setPhone(personInfo.getPhone());
        shop.setShopImg("测试照片");
        shop.setPriority(10);
        shop.setCreateTime(new Date());
        shop.setLastEditTime(new Date());
        shop.setEnableStatus(1);
        shop.setAdvice("没啥建议");

        int effectedNumber = shopDao.addShop(shop);
        assertEquals(1, effectedNumber);
    }

    @Test
    public void testUpdateShop() {
        Shop shop = new Shop();
        shop.setShopId(29);
        shop.setAdvice("修改测试建议");
        shop.setShopDescription("欢迎光临！我是修改!");
        shop.setLastEditTime(new Date());
        int i = shopDao.updateShop(shop);
        assertEquals(1, i);
    }

    @Test
    public void testQueryShop() {
        int shopId = 10;
        Shop shop = shopDao.queryShopByShopId(shopId);
        System.out.println(shop);
    }

    @Test
    public void testPage() {
        Shop shop = new Shop();
        shop.setOwnerId(1012);
        shop.setShopName("测试");
        int number = shopDao.queryShopNumber(shop);
        System.out.println(number);
        List<Shop> shops = shopDao.queryShopList(shop, 1, 3);
        for (Shop s : shops) {
            System.out.println(s);
        }
    }

    @Autowired
    public void setShopDao(ShopDao shopDao) {
        this.shopDao = shopDao;
    }
}