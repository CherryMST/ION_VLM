package com.nokia.vlm.utils;

import java.util.regex.Pattern;

/**
 * 正则
 */

public class ValidatorUtils {

    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "1[3|5|7|8|][0-9]{9}";

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }


}
