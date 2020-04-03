package com.chen.service;

import com.chen.BaseTest;
import com.chen.entity.Area;
import com.chen.entity.PersonInfo;
import com.chen.entity.Shop;
import com.chen.entity.ShopCategory;
import com.chen.model.FileContainer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by handsome programmer.
 * User: chen
 * Date: 2019/3/13
 * Time: 3:30
 * Description:
 *
 * @author chen
 */
public class ShopServiceTest extends BaseTest {

    /**
     * 注入店铺服务类
     */
    private ShopService shopService;

    @Test
    public void testAddShop() throws IOException {
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
        shop.setShopName("测试店铺添加服务");
        shop.setShopDescription("欢迎光临！");
        shop.setShopAddress("哈哈");
        shop.setPhone(personInfo.getPhone());
        shop.setShopImg("测试照片");
        shop.setPriority(10);
        shop.setCreateTime(new Date());
        shop.setLastEditTime(new Date());
        shop.setEnableStatus(1);
        shop.setAdvice("没啥建议");


        File file = new File("E:\\test\\girl.jpg");
        FileInputStream input = new FileInputStream(file);
        FileContainer fileContainer = new FileContainer();
        fileContainer.setFileName(file.getName());
        fileContainer.setFileInputStream(input);


        shopService.addShop(shop, fileContainer);
    }

    @Test
    public void updateShop() throws FileNotFoundException {
        Shop shop = new Shop();
        shop.setShopId(10);
        shop.setShopDescription("Test modify, haha!");
        File file = new File("F:\\picture\\wuman.png");
        FileInputStream input = new FileInputStream(file);
        FileContainer fileContainer = new FileContainer();
        fileContainer.setFileName(file.getName());
        fileContainer.setFileInputStream(input);

        shopService.modifyShop(shop, fileContainer);
    }

    @Test
    public void queryList() {
        List<Shop> shopList = shopService.getShopList(1012);
        for (Shop s : shopList) {
            System.out.println(s);
        }
    }

    @Autowired
    public void setShopService(ShopService shopService) {
        this.shopService = shopService;
    }
}