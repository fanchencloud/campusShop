package com.chen;

import com.alibaba.fastjson.JSON;
import com.chen.util.CommonUtils;

/**
 * Created by handsome programmer.
 * User: chen
 * Date: 2019/3/24
 * Time: 3:54
 * Description:
 *
 * @author chen
 */
public class JsonTest {
    public static void main(String[] args) {
        for (int i = 0; i < 4; i++) {
            System.out.println(CommonUtils.getUUID());
        }
    }
}
