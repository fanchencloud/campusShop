package com.chen.service.impl;

import com.chen.dao.AreaDao;
import com.chen.entity.Area;
import com.chen.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by handsome programmer.
 * User: chen
 * Date: 2019/3/12
 * Time: 3:49
 * Description:
 *
 * @author chen
 */
@Service
public class AreaServiceImpl implements AreaService {

    /**
     * 注入区域信息持久化单元
     */
    private AreaDao areaDao;

    @Override
    public List<Area> getAreaList() {
        List<Area> areas = areaDao.queryAllArea();
        for (Area area : areas) {
            area.setCreateTime(null);
            area.setLastEditTime(null);
        }
        return areas;
    }

    @Override
    public List<Area> getRegisterAreaList() {
        return areaDao.getRegisterAreaList();
    }

    @Autowired
    public void setAreaDao(AreaDao areaDao) {
        this.areaDao = areaDao;
    }
}
