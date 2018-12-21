package com.iwxyi.RecordActivity;

import com.iwxyi.R;

public class KindBean {
    public String name;
    public int img;
    public boolean choose;

    KindBean(String name) {
        this.name = name;
        img = nameToDrawable(name);
        choose = false;
    }

    private int nameToDrawable(String name) {
        switch (name) {
            case "转账" :
                return R.drawable.kind_1;
            case "运动" :
                return R.drawable.kind_2;
            case "娱乐" :
                return R.drawable.kind_3;
            case "游戏" :
                return R.drawable.kind_4;
            case "饮品" :
                return R.drawable.kind_5;
            case "衣服" :
                return R.drawable.kind_6;
            case "投资" :
                return R.drawable.kind_7;
            case "三餐" :
                return R.drawable.kind_8;
            case "日用品" :
                return R.drawable.kind_9;
            case "美妆" :
                return R.drawable.kind_10;
            case "旅行" :
                return R.drawable.kind_11;
            case "理财" :
                return R.drawable.kind_12;
            case "礼物" :
                return R.drawable.kind_13;
            case "借出" :
                return R.drawable.kind_14;
            case "交通" :
                return R.drawable.kind_15;
            case "奖金" :
                return R.drawable.kind_16;
            case "捡到" :
                return R.drawable.kind_17;
            case "兼职" :
                return R.drawable.kind_18;
            case "话费" :
                return R.drawable.kind_19;
            case "归还" :
                return R.drawable.kind_20;
            case "工资" :
                return R.drawable.kind_21;
            case "房租" :
                return R.drawable.kind_22;
            case "点心" :
                return R.drawable.kind_23;

            default :
                return R.drawable.ic_launcher;
        }
    }
}
