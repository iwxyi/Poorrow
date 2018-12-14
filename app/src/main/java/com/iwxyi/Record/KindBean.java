package com.iwxyi.Record;

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
        return R.drawable.ic_card;
    }
}
