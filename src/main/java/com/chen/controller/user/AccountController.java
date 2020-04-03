package com.chen.controller.user;

import com.chen.entity.LocalAccount;
import com.chen.entity.PersonInfo;
import com.chen.model.JsonResponse;
import com.chen.service.LocalAccountService;
import com.chen.service.PersonInfoService;
import com.chen.util.CommonStrings;
import com.chen.util.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * Created by handsome programmer.
 * User: chen
 * Date: 2019/4/16
 * Time: 7:13
 * Description: 用户账号服务控制器
 *
 * @author chen
 */
@Controller
@RequestMapping(value = "/user")
public class AccountController {

    private static final Logger logger = Logger.getLogger(AccountController.class);

    /**
     * 注入用户服务类
     */
    private LocalAccountService localAccountService;

    /**
     * 注入用户信息服务类
     */
    private PersonInfoService personInfoService;

    /**
     * 用户使用用户名密码登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public JsonResponse login(String username, String password, HttpServletRequest request) {
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            LocalAccount user = new LocalAccount();
            user.setUsername(username);
            user.setPassword(password);
            if (localAccountService.login(user)) {
                request.getSession().setAttribute("user", user);
                return JsonResponse.ok("登录成功！");
            }
        }
        return JsonResponse.errorMsg("登录失败，用户名或密码错误！");
    }

    /**
     * 获取用户的头像
     *
     * @param response 服务器响应
     */
    @GetMapping(value = "/getUserImage")
    public void getUserHeadImage(HttpServletRequest request, HttpServletResponse response) {
        LocalAccount user = (LocalAccount) request.getSession().getAttribute("user");
        if (user == null) {
            return;
        }
        // 获取该用户头像的文件流
        File userImage = personInfoService.getUserImage(user.getUserId());
        try {
            CommonUtils.exportPictureData(response, userImage);
        } catch (IOException e) {
            logger.error("输出图片文件失败！失败原因：" + e.getMessage());
        }
    }

    @GetMapping(value = "/isLogin")
    @ResponseBody
    public JsonResponse isLogin(HttpServletRequest request) {
        LocalAccount user = (LocalAccount) request.getSession().getAttribute("user");
        if (user == null) {
            return JsonResponse.errorMsg("你还没登录，请登录！");
        } else {
            return JsonResponse.ok("已登录！");
        }
    }

    /**
     * 请求获取用户信息
     *
     * @param request 数据请求
     * @return 用户信息
     */
    @GetMapping(value = "/getUserInfo")
    @ResponseBody
    public JsonResponse getPersonInfo(HttpServletRequest request) {
        LocalAccount user = (LocalAccount) request.getSession().getAttribute("user");
        if (user == null) {
            return JsonResponse.errorMsg("你还没登录，请登录！");
        }
        PersonInfo personInfo = personInfoService.getPersonInfo(user.getUserId());
        return JsonResponse.ok(personInfo);
    }

    /**
     * 注册用户
     *
     * @param username     用户名
     * @param password     密码
     * @param validateCode 验证码
     * @return 注册结果
     */
    @PostMapping(value = "/register")
    @ResponseBody
    public JsonResponse register(String username, String password, String validateCode, HttpServletRequest request) {
        String cacheCode = (String) request.getSession().getAttribute(CommonStrings.VALIDATE_CODE);
        // 验证码为空
        boolean b = StringUtils.isBlank(validateCode);
        if (b) {
            return JsonResponse.errorMsg("验证码不能为空！");
        }
        // 验证码错误
        if (!(cacheCode.toLowerCase().equals(validateCode.toLowerCase()))) {
            return JsonResponse.errorMsg("验证码错误！");
        }
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return JsonResponse.errorMsg("注册信息有误！");
        }
        LocalAccount user = new LocalAccount();
        user.setUsername(username);
        user.setPassword(password);
        return localAccountService.register(user);
    }

    /**
     * 请求修改用户登录密码
     *
     * @return 页面跳转
     */
    @GetMapping(value = "/modifyPassword")
    public String modifyPassword() {
        return "/user/modifyPassword";
    }

    @PostMapping(value = "/modifyPassword")
    @ResponseBody
    public JsonResponse modifyPassword(String originalPassword, String newPassword, HttpServletRequest request) {
        LocalAccount user = (LocalAccount) request.getSession().getAttribute("user");
        if (localAccountService.modifyPassword(originalPassword, newPassword, user)) {
            return JsonResponse.ok("修改成功！");
        }
        return JsonResponse.errorMsg("修改密码失败！");
    }

    /**
     * 请求退出登录
     *
     * @param request 请求
     * @return 页面跳转
     */
    @GetMapping(value = "/loginOut")
    public String loginOut(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return "redirect:/home";
    }

    @Autowired
    public void setLocalAccountService(LocalAccountService localAccountService) {
        this.localAccountService = localAccountService;
    }

    @Autowired
    public void setPersonInfoService(PersonInfoService personInfoService) {
        this.personInfoService = personInfoService;
    }
}