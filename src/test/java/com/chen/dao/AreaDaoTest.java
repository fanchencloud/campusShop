package com.chen.dao;

import com.chen.BaseTest;
import com.chen.entity.Area;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by handsome programmer.
 * User: chen
 * Date: 2019/3/12
 * Time: 2:18
 * Description:
 *
 * @author chen
 */
public class AreaDaoTest extends BaseTest {

    private static Logger logger = Logger.getLogger(AreaDaoTest.class);

    private AreaDao areaDao;

    @Test
    public void testQueryAllArea() {
        List<Area> areas = areaDao.queryAllArea();
        for (Area area : areas) {
            System.out.println(area);
        }
        logger.info("这是 info 日志测试");
        logger.debug("这是 debug 日志测试");
        logger.trace("这是 trace 日志测试");
        logger.error("这是 error 日志测试");
        logger.warn("这是 warn 日志测试");
    }

    @Autowired
    public void setAreaDao(AreaDao areaDao) {
        this.areaDao = areaDao;
    }
}
