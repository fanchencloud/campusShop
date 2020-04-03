package com.chen.dao;

import com.chen.entity.Area;

import java.util.List;

/**
 * Created by handsome programmer.
 * User: chen
 * Date: 2019/3/12
 * Time: 0:27
 * Description: 区域数据持久化操作
 *
 * @author chen
 */
public interface AreaDao {
    /**
     * 查询所有的区域信息
     *
     * @return 区域列表
     */
    List<Area> queryAllArea();

    List<Area> getRegisterAreaList();
}
