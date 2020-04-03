package com.chen.dao;

import com.chen.BaseTest;
import com.chen.entity.Headline;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by handsome programmer.
 * User: chen
 * Date: 2019/4/10
 * Time: 20:18
 * Description:
 *
 * @author chen
 */
public class HeadlineDaoTest extends BaseTest {

    /**
     * 注入头条数据持久层
     */
    private HeadlineDao headlineDao;

    @Test
    public void testQueryHeadLine() {
        List<Headline> headlines = headlineDao.queryHeadlineList(2, null);
        for (Headline h :
                headlines) {
            System.out.println(h);
        }
    }

    @Autowired
    public void setHeadlineDao(HeadlineDao headlineDao) {
        this.headlineDao = headlineDao;
    }
}