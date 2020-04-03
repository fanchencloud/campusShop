package com.chen.service;

import com.chen.entity.LocalAccount;
import com.chen.model.JsonResponse;

/**
 * Created by handsome programmer.
 * User: chen
 * Date: 2019/4/16
 * Time: 7:33
 * Description: 本地账号服务接口
 *
 * @author chen
 */
public interface LocalAccountService {

    /**
     * 用户登录
     *
     * @param localAccount 用户信息
     * @return 登录结果
     */
    boolean login(LocalAccount localAccount);

    /**
     * 用户注册
     *
     * @param localAccount 用户信息
     * @return 注册结果
     */
    JsonResponse register(LocalAccount localAccount);

    /**
     * 修改用户登录面
     *
     * @param originalPassword 原始密码
     * @param newPassword      新密码
     * @param user             用户信息
     * @return 修改结果
     */
    boolean modifyPassword(String originalPassword, String newPassword, LocalAccount user);
}
