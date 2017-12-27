package com.qx.framelib.utlis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {

    public static boolean isEmpty(String str) {
        if (str != null && !"null".equals(str) && str.trim().length() > 0) {
            return false;
        }
        return true;
    }
    public static boolean isMobileNO(String mobiles) {
//		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(14[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static String toString(String value) {
        if (isEmpty(value)) {
            return "";
        } else {
            return value;
        }
    }
    /**
     * 是否是有效价格
     *
     * @param price
     * @return
     */
    public static boolean isPriceStr(String price) {
        if (!isEmpty(price)) {

//            if (price.contains(".")) {//不能输入小数
//                return false;
//            }
            int num = appearNumber(price, ".");//如果价格里面出现多个小数点
            if (num > 1) {
                return false;
            }
            String first = price.substring(0, 1);
            if (".".equals(first)) {
                return false;
            }
//            if(price.contains(".")) {
//                int dot = price.indexOf(".");
//                String dotStr = price.substring(dot, price.length());
//                Log.e("text", dotStr);
//                if (dotStr.length() > 3) {//只能输入到小数点后两位  10.55
//                    return false;
//                }
//
//            }

            if (Double.parseDouble(price) > 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取指定字符串出现的次数
     *
     * @param srcText  源字符串
     * @param findText 要查找的字符串
     * @return
     */
    public static int appearNumber(String srcText, String findText) {
        int count = 0;
        int index = 0;
        while ((index = srcText.indexOf(findText, index)) != -1) {
            index = index + findText.length();
            count++;
        }
        return count;
    }



    /**
     * 过滤字符串的空格
     */
    public static String replaceBlank(String str) {
        if (null == str) {
            return null;
        }
        String dest = "";
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(str);
        dest = m.replaceAll("");
        return dest;
    }


    /**
     * 判断两个字符串是否相等。如果两个都为null也相等
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean isStringEqual(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 != null) {
            return str1.equals(str2);
        }
        //then str1=null，str2 not null
        return false;
    }

    /**
     * 字符串转换成long值，抛异常则返回0
     *
     * @param string
     * @return
     */
    public static long parseLongValue(String string) {
        try {
            return Long.parseLong(string);
        } catch (Exception ex) {
        }
        return 0;
    }

    /**
     * 字符串转换成int值，抛异常返回0
     *
     * @param string
     * @return
     */
    public static int parseIntValue(String string) {
        return parseIntValue(string, 0);
    }

    public static int parseIntValue(String string, int defaultVal) {
        try {
            return Integer.parseInt(string);
        } catch (Exception ex) {
        }
        return defaultVal;
    }


    /**
     * 去掉每行首尾半角空格，以及每行首部的全角空格
     */
    public static String trimMultiPrefixSpace(String text) {
        if (text == null) {
            return text;
        }
        String[] lines = text.split("\n");
        String result = "";
        for (String line : lines) {
            if (line != null) {
                line = line.trim();
                int index = 0;
                for (; index < line.length(); index++) {
                    if (line.charAt(index) != '　') {
                        break;
                    }
                }
                if (index < line.length()) {
                    line = line.substring(index);
                }
                result = result + line + "\n";
            }
        }
        return result;
    }

    /**
     * 截取小数点后面的数字
     *
     * @param text
     * @return
     */
    public static String getDotBehind(String text) {

        if (TextUtil.isEmpty(text)) {
            return null;
        }
        if (!text.contains(".")) {
            return null;
        }

        StringBuffer sb = new StringBuffer(text);
        int index = text.indexOf(".")+1;
        String str = sb.delete(0, index).toString();
        return str;
    }

    public static String getDotFront(String text) {

        if (TextUtil.isEmpty(text)) {
            return null;
        }
        if (!text.contains(".")) {
            return text;
        }
        StringBuffer sb = new StringBuffer(text);
        int index = text.indexOf(".");
        String str = sb.delete(index, text.length()).toString();
        return str;
    }


}
