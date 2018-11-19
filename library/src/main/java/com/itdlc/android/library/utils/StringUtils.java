package com.itdlc.android.library.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by felear on 2016/11/17.
 * email:985979637@qq.com
 */
public class StringUtils {

    public static String getObjDataTime(String pattern, long time) {
        return new SimpleDateFormat(pattern).format(new Date(time));
    }

    public static String getCurrentRefreshTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String getDataVByLongTime(long time) {
        return new SimpleDateFormat("yy/MM/dd HH:mm:ss").format(new Date(time));
    }

    public static String getDataHByLongTime(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
    }

    public static String getDataByLongTime(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(time));
    }

    public static String getShortDataByLongTime(long time) {
        return new SimpleDateFormat("yyyy-M-d HH:mm").format(new Date(time));
    }

    public static String getShortHourTime(long time) {
        return new SimpleDateFormat("HH:mm").format(new Date(time));
    }
    public static String getProfitDataHByLongTime(long time) {
        return new SimpleDateFormat("yyyy-MM-dd\nHH:mm:ss").format(new Date(time));
    }

    public static String getDayByLongTime(long time) {

        return new SimpleDateFormat("yyyy/MM/dd").format(new Date(time));
    }

    public static String getChinaDayByLongTime(long time) {

        return new SimpleDateFormat("yyyy年MM月dd日").format(new Date(time));
    }

    public static String dateToStamp(String pattern,String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }
    public static String getCurWeek() {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static String getJson(List<HashMap<String, String>> list) {

        String strJson = "[";

        for (int i = 0; i < list.size(); i++) {
            strJson += "{";
            HashMap<String, String> map = list.get(i);
            for (String key : map.keySet()) {
                strJson += "\"" + key + "\":\"" + map.get(key) + "\",";
            }
            strJson = strJson.substring(0, strJson.length() - 1) + "},";
        }

        strJson = strJson.substring(0, strJson.length() - 1) + "]";
        return strJson;
    }

    public static boolean checkNameChese(String name) {
        boolean res = true;
        char[] cTemp = name.toCharArray();

        for (int i = 0; i < name.length(); ++i) {
            if (!isChinese(cTemp[i])) {
                res = false;
                break;
            }
        }

        return res;
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    public static boolean isContainsChinese(String str) {
        if (str == null) {
            return false;
        } else {
            String regEx = "[一-龥]";
            Pattern pat = Pattern.compile(regEx);
            Matcher matcher = pat.matcher(str);
            boolean flg = false;
            if (matcher.find()) {
                flg = true;
            }

            return flg;
        }
    }

    public static boolean isNormName(String name) {
        Pattern p = Pattern.compile("^([一-龥a-zA-Z0-9@_]+)$");
        Matcher m = p.matcher(name);
        return m.matches();
    }

    // 判断是否为合法的用户名
    public static boolean checkName(String name) {
        if (name == null) {
            return false;
        }
        return isMobileNumber(name) || checkEmail(name);
    }

    public static String chinaToUnicode(String str) {
        String result = "";

        for (int i = 0; i < str.length(); ++i) {
            char chr1 = str.charAt(i);
            if (chr1 >= 19968 && chr1 <= 171941) {
                result = result + "\\u" + Integer.toHexString(chr1);
            } else {
                result = result + str.charAt(i);
            }
        }

        return result;
    }

    //判断手机号
    public static boolean isMobileNumber(String mobileNumber) {
        if (mobileNumber == null) {
            return false;
        }
        //        Pattern p = Pattern.compile("^1\\d{10}$");
        Pattern p = Pattern.compile("^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$");
        Matcher m = p.matcher(mobileNumber);
        return m.matches();
    }

    public static String maskMobileNumber(String mobileNumber) {
        if (mobileNumber == null || mobileNumber.length() != 11) {
            return mobileNumber;
        }
        return mobileNumber.substring(0, 3) + "****" + mobileNumber.substring(3 + 4);
    }

    public static boolean checkEmail(String email) {
        boolean flag = false;

        try {
            String e = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(e);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception var5) {
            flag = false;
        }

        return flag;
    }

    //判断座机号码
    public static boolean isPhoneNumber(String phoneNumber) {
        String regexp = "^(0[0-9]{2,3}\\-)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean isIdentityId(String id) {
        String regexp = "^(\\d{6})(19|20)(\\d{2})(1[0-2]|0[1-9])(0[1-9]|[1-2][0-9]|3[0-1])(\\d{3})(\\d|X|x)$";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(id);
        return matcher.matches();
    }

    public static boolean isMobilePhoneNumber(String phoneNumber) {
        boolean result = isMobileNumber(phoneNumber) || isPhoneNumber(phoneNumber);
        return result;
    }

    public static String trim(String str) {
        return str.trim().replaceAll(" ", "");
    }

    //double转整形，如果小数点后面全是0，就取整
    public static String double2int(double d) {
        String s = String.valueOf(d);
        int len = s.length();
        int dot = -1;
        boolean flag = true;
        for (int i = 0; i < len - 1; i++) {
            if (s.charAt(i) == '.') {
                dot = i;
                break;
            }
        }
        for (int j = len - 1; j > dot; j--) {
            if (s.charAt(j) != '0') {
                flag = false;
                break;
            }
        }
        if (flag == true) {
            return s.substring(0, dot);
        } else {
            return s;
        }
    }

    //float转整形，如果小数点后面全是0，就取整
    public static String float2int(float f) {
        String s = String.valueOf(f);
        int len = s.length();
        int dot = -1;
        boolean flag = true;
        for (int i = 0; i < len - 1; i++) {
            if (s.charAt(i) == '.') {
                dot = i;
                break;
            }
        }
        for (int j = len - 1; j > dot; j--) {
            if (s.charAt(j) != '0') {
                flag = false;
                break;
            }
        }
        if (flag == true) {
            return s.substring(0, dot);
        } else {
            return s;
        }
    }

    private static DecimalFormat mMoneyFormat = new java.text.DecimalFormat("######0.00");

    public static String fitMoney(double d) {
        return mMoneyFormat.format(d);
    }

    // 数组转字符串
    public static String arrayToString(List<String> list, String join) {

        if (list == null) {
            return "";
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                result.append(list.get(i));
            } else {
                result.append(join).append(list.get(i));
            }
        }

        return result.toString();
    }

    // 字符串转
    public static List<String> stringToArray(String src, String join) {

        String[] split = src.split(join);
        ArrayList<String> lst = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            lst.add(split[i]);
        }
        return lst;
    }
}
