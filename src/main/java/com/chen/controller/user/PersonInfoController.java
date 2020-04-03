package com.chen.controller.user;

import com.alibaba.fastjson.JSON;
import com.chen.entity.LocalAccount;
import com.chen.entity.PersonInfo;
import com.chen.model.FileContainer;
import com.chen.model.JsonResponse;
import com.chen.service.PersonInfoService;
import com.chen.util.ChenHttpUtils;
import com.chen.util.CommonStrings;
import com.chen.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by handsome programmer.
 * User: chen
 * Date: 2019/4/18
 * Time: 11:43
 * Description: 用户信息控制器
 *
 * @author chen
 */
@Controller
@RequestMapping(value = "/person")
public class PersonInfoController {

    /**
     * 注入个人信息服务层
     */
    private PersonInfoService personInfoService;

    /**
     * 请求修改用户信息
     *
     * @return 页面跳转
     */
    @GetMapping(value = "/modifyUser")
    public String modifyUserInfo() {
        return "user/modifyUserInfo";
    }

    /**
     * 请求修改用户信息
     *
     * @return 页面跳转
     */
    @PostMapping(value = "/modifyUser")
    @ResponseBody
    public JsonResponse modifyUserInfo(HttpServletRequest request) throws UnsupportedEncodingException {
        HashMap<String, String> paramMap = new HashMap<>(2);
        paramMap.put("user", "用户信息");
        paramMap.put("verifyCode", "验证码");
//        paramMap.put("thumbnail", "用户头像");
        Map<String, Object> uploadMessage = ChenHttpUtils.doUploadMessage(request, paramMap);
        String verifyCode = (String) uploadMessage.get("verifyCode");
        if (uploadMessage.get(CommonStrings.ERROR) != null) {
            return (JsonResponse) uploadMessage.get(CommonStrings.ERROR);
        }
        List<Object> list = CommonUtils.validateCode(verifyCode, request);
        if (list.size() > 0) {
            return (JsonResponse) list.get(0);
        }
        // 用户信息
        String userMessage = (String) uploadMessage.get("user");
        PersonInfo personInfo = JSON.parseObject(userMessage, PersonInfo.class);
        // 缩略图
        // 处理详情图片
        List<FileContainer> fileContainerList = null;
        FileContainer headImage = null;
        if (uploadMessage.get(CommonStrings.FILE_LIST) instanceof List) {
            fileContainerList = myCast(uploadMessage.get(CommonStrings.FILE_LIST));
            headImage = fileContainerList.get(0);
        }
        LocalAccount user = (LocalAccount) request.getSession().getAttribute("user");
        personInfo.setId(user.getUserId());
        return personInfoService.updateMessage(personInfo, headImage);
    }


    @SuppressWarnings("unchecked")
    private static <T> T myCast(Object obj) {
        return (T) obj;
    }

    @GetMapping(value = "/deleteHeadImage")
    @ResponseBody
    public JsonResponse deleteHeadImage(HttpServletRequest request) {
        LocalAccount user = (LocalAccount) request.getSession().getAttribute("user");
        personInfoService.deleteHeadImage(user.getUserId());
        return JsonResponse.ok();
    }


    @Autowired
    public void setPersonInfoService(PersonInfoService personInfoService) {
        this.personInfoService = personInfoService;
    }
}
