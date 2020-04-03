package com.chen.service.impl;

import com.chen.dao.PersonInfoDao;
import com.chen.entity.PersonInfo;
import com.chen.model.FileContainer;
import com.chen.model.JsonResponse;
import com.chen.service.PersonInfoService;
import com.chen.util.ImageUtils;
import com.chen.util.PathUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.File;
import java.io.IOException;

/**
 * Created by handsome programmer.
 * User: chen
 * Date: 2019/4/16
 * Time: 9:17
 * Description: 用户信息服务实现类
 *
 * @author chen
 */
@Service
public class PersonInfoServiceImpl implements PersonInfoService {

    private static final Logger logger = Logger.getLogger(PersonInfoServiceImpl.class);

    private DataSourceTransactionManager dataSourceTransactionManager;

    /**
     * 注入用户信息持久层
     */
    private PersonInfoDao personInfoDao;

    @Override
    public PersonInfo getPersonInfo(int id) {
        return personInfoDao.queryById(id);
    }

    @Override
    public File getUserImage(Integer userId) {
        PersonInfo personInfo = personInfoDao.queryById(userId);
        String s = PathUtils.getImageBasePath() + personInfo.getHeadPortrait();
        s = PathUtils.replaceFileSeparator(s);
        assert s != null;
        return new File(s);
    }

    @Override
    public void deleteHeadImage(int userId) {
        // 查询信息
        PersonInfo personInfo = personInfoDao.queryById(userId);
        // 删除数据库的信息
        personInfoDao.deletePersonHeadImage(userId);
        // 删除文件 图片路径根路径 + /upload/user/1111111.jpg
        String picturePath = PathUtils.getImageBasePath() + personInfo.getHeadPortrait();
        File f = new File(picturePath);
        if (f.exists()) {
            boolean delete = f.delete();
        }
    }

    @Override
    @Transactional
    public JsonResponse updateMessage(PersonInfo personInfo, FileContainer headImage) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setName("updateMessage");
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = dataSourceTransactionManager.getTransaction(defaultTransactionDefinition);
        JsonResponse jsonResponse = null;
        boolean flag = true;
        try {
            // 判断店铺商品是否是空值
            if (personInfo == null || personInfo.getId() == null) {
                throw new RuntimeException("用户信息有误！");
            }

            // 修改用户信息
            int number = personInfoDao.updateUserMessage(personInfo);
            if (number <= 0) {
                throw new RuntimeException("修改用户信息失败！");
            }
            // 用户头像不为空则修改用户头像图
            if (headImage != null) {
                saveHeadImage(personInfo, headImage);
                // 更新用户的头像信息
                PersonInfo p = new PersonInfo();
                p.setId(personInfo.getId());
                p.setHeadPortrait(personInfo.getHeadPortrait());
                number = personInfoDao.updateUserMessage(p);
                if (number <= 0) {
                    logger.error("处理商品缩略图信息出错！用户id：" + personInfo.getId());
                    throw new RuntimeException("添加用户头像！");
                }
            }
        } catch (Exception e) {
            dataSourceTransactionManager.rollback(status);
            flag = false;
            jsonResponse = JsonResponse.errorMsg(e.getMessage());
        }
        if (flag) {
            jsonResponse = JsonResponse.ok("修改用户信息成功！");
        }
        return jsonResponse;
    }

    private void saveHeadImage(PersonInfo personInfo, FileContainer headImage) throws IOException {
        // 获取shop图片目录的相对路径
        String dest = PathUtils.getUserImagePath(personInfo.getId());
        // 获取图片文件的路径
        String shopImageAddress = ImageUtils.generateThumbnail(headImage.getFileInputStream(), dest, headImage.getFileName());
        // 设置用户头像图片路径
        personInfo.setHeadPortrait(shopImageAddress);
    }

    @Autowired
    public void setPersonInfoDao(PersonInfoDao personInfoDao) {
        this.personInfoDao = personInfoDao;
    }

    @Autowired
    public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
        this.dataSourceTransactionManager = dataSourceTransactionManager;
    }
}
