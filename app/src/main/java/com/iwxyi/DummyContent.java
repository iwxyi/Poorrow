package com.iwxyi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DummyContent {


    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();
    private static final int COUNT = 25;

    static {
        /*for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }*/
        String texts = FileUtil.readTextVals("bills.txt");
        ArrayList<String>bills = StringUtil.getXmls(texts, "BILL");
        for (String b :bills) {
            String id = StringUtil.getXml(b, "ID"); // 必须
            String source = StringUtil.getXml(b, "SR");
            int mode = StringUtil.getXmlInt(b, "MD");
            String kind = StringUtil.getXml(b, "KD");
            double amount = StringUtil.getXmlDouble(b, "AM"); // 必须
            String note = StringUtil.getXml(b, "NT");
            String card = StringUtil.getXml(b, "CD");
            long timestamp = StringUtil.getXmlLong(b, "TT");
            long addTime = StringUtil.getXmlLong(b, "AT");
            long changeTime = StringUtil.getXmlLong(b, "CT");
            boolean reimburse = StringUtil.getXmlBoolean(b, "RB");
            long remind = StringUtil.getXmlLong(b, "RM");

            addItem(createDummyItem(id, amount, mode, source, kind, note, card, timestamp, addTime, changeTime, reimburse, remind));
        }
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * 类似工厂模式添加 Dummy
     */
    private static DummyItem createDummyItem(String id, double amount, int mode, String source, String kind, String note, String card, long timestamp, long addTime, long changeTime, Boolean reimburse, long remind) {
        return new DummyItem(id, amount, mode, source, kind, note, card, timestamp, addTime, changeTime, reimburse, remind);
    }

    public static class DummyItem {

        public String  id;         // 账单ID（根据时间随机）
        public double  amount;     // 账单金额（+收入，-支出）
        public int     mode;       // 账单类型（支出、收入、借贷）
        public String  source;     // 账单源头（消费、获取）
        public String  kind;       // 账单种类（吃喝住行、工资、借还等）
        public String  note;       // 账单备注
        public String  card;       // 哪张卡
        public long    timestamp;  // 账单时间戳（以消费时间为准）
        public long    addTime;    // 添加时间戳（以添加时间为准）
        public long    changeTime; // 修改时间戳
        public Boolean reimburse;  // 是否能报销/归还
        public long    remind;     // 提醒时间戳

        public DummyItem(String id, int amount) {
            this.id = id;
            this.amount = amount;
        }

        public DummyItem(String id, double amount, int mode, String source, String kind, String note, String card, long timestamp, long addTime, long changeTime, Boolean reimburse, long remind) {
            this.id = id;
            this.amount = amount;
            this.mode = mode;
            this.source = source;
            this.kind = kind;
            this.note = note;
            this.card = card;
            this.timestamp = timestamp;
            this.addTime = addTime;
            this.changeTime = changeTime;
            this.reimburse = reimburse;
            this.remind = remind;
        }

        @Override
        public String toString() {
            return StringUtil.toXml(id, "ID")
                    + StringUtil.toXml(source, "SR")
                    + StringUtil.toXml(mode+"", "MD")
                    + StringUtil.toXml(kind, "KD")
                    + StringUtil.toXml(amount, "AM")
                    + StringUtil.toXml(note, "NT")
                    + StringUtil.toXml(card, "CD")
                    + StringUtil.toXml(timestamp, "TT")
                    + StringUtil.toXml(addTime, "AT")
                    + StringUtil.toXml(changeTime, "CT")
                    + StringUtil.toXml(reimburse, "RB")
                    + StringUtil.toXml(remind, "RM");
        }
    }
}
