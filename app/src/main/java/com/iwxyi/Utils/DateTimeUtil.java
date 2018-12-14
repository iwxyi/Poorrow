package com.iwxyi.Utils;

public class DateTimeUtil {
    public static String DataToString(int year, int month, int date) {
        String rst = year + "/";
        if (month < 10)
            rst += "0";
        rst += month + "/";
        if (date < 10)
            rst += "0";
        rst += date;
        return rst;
    }

    public static String TimeToString(int hour, int minute) {
        String rst = "";
        if (hour < 10)
            rst += "0";
        rst += hour + ":";
        if (minute < 10)
            rst += "0";
        rst += minute;
        return rst;
    }
}
