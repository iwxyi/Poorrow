package com.iwxyi;

import java.util.ArrayList;

public class StringUtil {
    public static ArrayList<String> getXmls(String str, String tag) {
        ArrayList<String> result = new ArrayList<>();
        String startTag = "<" + tag + ">";
        String endTag = "</" + tag + ">";
        int left = 0, right = 0;
        while (right != -1) {
            left = str.indexOf(startTag, right);
            if (left == -1) break;
            left += startTag.length();
            right = str.indexOf(endTag, left);
            if (right == -1) break;
            String mid = str.substring(left, right);
            result.add(mid);
        }
        return result;
    }

    public static String getXml(String str, String tag) {
        String result = "";
        String startTag = "<" + tag + ">";
        String endTag = "</" + tag + ">";
        int left = str.indexOf(startTag);
        if (left == -1) return result;
        left += startTag.length();
        int right = str.indexOf(endTag, left);
        if (right == -1) return result;
        result = str.substring(left, right);
        return result;
    }

    public static int getXmlInt(String str, String tag) {
        String s = getXml(str, tag);
        if ("".equals(s)) return 0;
        return Integer.parseInt(s);
    }

    public static long getXmlLong(String str, String tag) {
        String s = getXml(str, tag);
        if ("".equals(s)) return 0;
        return Long.parseLong(s);
    }

    public static boolean getXmlBoolean(String str, String tag) {
        String s = getXml(str, tag);
        if ("".equals(s)) return false;
        return "1".equals(s);
    }

    public static double getXmlDouble(String str, String tag) {
        String s = getXml(str, tag);
        if ("".equals(s)) return 0.0;
        return Double.parseDouble(s);
    }

    public static String toXml(String str, String tag) {
        return "<" + tag + ">" + str + "</" + tag + ">";
    }

    public static String toXml(int val, String tag) {
        return "<" + tag + ">" + val + "</" + tag + ">";
    }

    public static String toXml(long val, String tag) {
        return "<" + tag + ">" + val + "</" + tag + ">";
    }

    public static String toXml(boolean val, String tag) {
        return "<" + tag + ">" + (val?1:0) + "</" + tag + ">";
    }

    public static String toXml(double val, String tag) {
        return "<" + tag + ">" + val + "</" + tag + ">";
    }
}
