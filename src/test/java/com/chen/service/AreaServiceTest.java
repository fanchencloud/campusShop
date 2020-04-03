package com.chen.service;

import com.chen.BaseTest;
import com.chen.entity.Area;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by handsome programmer.
 * User: chen
 * Date: 2019/3/12
 * Time: 3:51
 * Description:
 *
 * @author chen
 */
public class AreaServiceTest extends BaseTest {

    /**
     * 注入区域信息服务
     */
    private AreaService areaService;

    @Test
    public void testQueryAreaList() {
        List<Area> areaList = areaService.getAreaList();
        for (Area area : areaList) {
            System.out.println(area);
        }
    }

    @Autowired
    public void setAreaService(AreaService areaService) {
        this.areaService = areaService;
    }
}