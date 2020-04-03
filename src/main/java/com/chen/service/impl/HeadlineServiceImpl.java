package com.chen.service.impl;

import com.chen.dao.HeadlineDao;
import com.chen.entity.Headline;
import com.chen.service.HeadlineService;
import com.chen.util.PathUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * Created by handsome programmer.
 * User: chen
 * Date: 2019/4/10
 * Time: 20:39
 * Description: 头条信息服务层实现
 *
 * @author chen
 */
@Service
public class HeadlineServiceImpl implements HeadlineService {

    /**
     * 日志记录器
     */
    private static final Logger logger = Logger.getLogger(ShopCategoryServiceImpl.class);

    /**
     * 注入头条信息服务持久层
     */
    private HeadlineDao headlineDao;

    @Override
    public List<Headline> queryHeadlineList(Integer number, Integer status) {
        return headlineDao.queryHeadlineList(number, status);
    }

    @Override
    public File getHeadlineImage(String uuid) {
        Headline headline = headlineDao.queryHeadlineImageByUUID(uuid);
        if (headline == null) {
            logger.error("获取文件出错！uuid = " + uuid);
            return null;
        }
        String headlineImagePath = PathUtils.getImageBasePath() + headline.getPicture();
        headlineImagePath = PathUtils.replaceFileSeparator(headlineImagePath);
        assert headlineImagePath != null;
        return new File(headlineImagePath);
    }

    @Autowired
    public void setHeadlineDao(HeadlineDao headlineDao) {
        this.headlineDao = headlineDao;
    }
}
