package com.chen.service.impl;

import com.chen.dao.LocalAccountDao;
import com.chen.dao.PersonInfoDao;
import com.chen.entity.LocalAccount;
import com.chen.entity.PersonInfo;
import com.chen.model.JsonResponse;
import com.chen.service.LocalAccountService;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Date;

/**
 * Created by handsome programmer.
 * User: chen
 * Date: 2019/4/16
 * Time: 7:35
 * Description: 本地账号服务实现类
 *
 * @author chen
 */
@Service
public class LocalAccountServiceImpl implements LocalAccountService {

    private static final Logger logger = Logger.getLogger(LocalAccountServiceImpl.class);

    /**
     * 注入本地账户数据持久层
     */
    private LocalAccountDao localAccountDao;

    /**
     * 注入用户信息数据持久层
     */
    private PersonInfoDao personInfoDao;

    private DataSourceTransactionManager dataSourceTransactionManager;

    @Override
    public boolean login(LocalAccount localAccount) {
        // 加密数据
        String username = localAccount.getUsername();
        String password = Md5Crypt.apr1Crypt(username, localAccount.getPassword());
        LocalAccount record = localAccountDao.getRecordByUsernameAndPassword(username, password);
        if (record != null) {
            // 用户名密码正确
            localAccount.setPassword(null);
            localAccount.setId(record.getId());
            localAccount.setUserId(record.getUserId());
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public JsonResponse register(LocalAccount localAccount) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setName("register");
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = dataSourceTransactionManager.getTransaction(defaultTransactionDefinition);
        JsonResponse result = null;
        // 查找用户名是否已被占用
        boolean flag = localAccountDao.isExist(localAccount.getUsername());
        try {
            if (flag) {
                result = JsonResponse.errorMsg("用户名已被使用！");
                logger.error("用户名已被使用！");
                return result;
            }
            // 设置本地账号的默认属性
            localAccount.setCreateTime(new Date());
            localAccount.setLastEditTime(localAccount.getCreateTime());
            PersonInfo personInfo = new PersonInfo();
            personInfo.setCreateTime(new Date());
            personInfo.setLastEditTime(personInfo.getCreateTime());
            // 默认为账号可用状态，默认等级为顾客
            personInfo.setEnableStatus(1);
            personInfo.setUserType(1);
            // 将用户信息持久化到数据库中
            int number = personInfoDao.addPersonInfo(personInfo);
            if (number <= 0) {
                logger.error("添加用户信息失败！");
                throw new RuntimeException("添加用户信息失败！");
            }
            // 将本地账号持久化到数据库
            localAccount.setUserId(personInfo.getId());
            localAccount.setPassword(Md5Crypt.apr1Crypt(localAccount.getUsername(), localAccount.getPassword()));
            number = localAccountDao.addLocalAccount(localAccount);
            if (number <= 0) {
                logger.error("添加本地账号信息失败！");
                throw new RuntimeException("添加本地账号信息失败！");
            }
            result = JsonResponse.ok("注册成功！");
        } catch (RuntimeException e) {
            dataSourceTransactionManager.rollback(status);
            result = JsonResponse.errorMsg("注册失败");
        }
        return result;
    }

    @Override
    public boolean modifyPassword(String originalPassword, String newPassword, LocalAccount localAccount) {
        String password = Md5Crypt.apr1Crypt(localAccount.getUsername(), originalPassword);
        String temp = Md5Crypt.apr1Crypt("admin", "123456");
        LocalAccount record = localAccountDao.getRecordByUsernameAndPassword(localAccount.getUsername(), password);
        if (record == null) {
            return false;
        }
        record.setPassword(Md5Crypt.apr1Crypt(localAccount.getUsername(), newPassword));
        record.setLastEditTime(new Date());
        int i = localAccountDao.updateLocalAccount(record);
        return i != 0;
    }

    @Autowired
    public void setLocalAccountDao(LocalAccountDao localAccountDao) {
        this.localAccountDao = localAccountDao;
    }


    @Autowired
    public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
        this.dataSourceTransactionManager = dataSourceTransactionManager;
    }

    @Autowired
    public void setPersonInfoDao(PersonInfoDao personInfoDao) {
        this.personInfoDao = personInfoDao;
    }
}