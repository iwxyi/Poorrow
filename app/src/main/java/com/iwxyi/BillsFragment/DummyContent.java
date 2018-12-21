package com.iwxyi.BillsFragment;

import android.util.Log;

import com.iwxyi.Utils.FileUtil;
import com.iwxyi.Utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账单的工厂类
 */
public class DummyContent {

    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();
    public static String _text = "not inited";

    /**
     * 初始化
     */
    static {
        // 如果文件不存在，则初始化文件
        if (!FileUtil.exist("bills.txt")) {
            initStartBills();
        }

        readFromFile();
    }

    /**
     * 从文件 bills.txt 中读取所有的账单（用于更新操作）
     */
    public static void readFromFile() {
        ITEMS.clear();
        ITEM_MAP.clear();

        // 从文件中读取 Bills
        String texts = FileUtil.readTextVals("bills.txt");
        _text = texts;
        Log.i("read bills.txt texts", texts);
        ArrayList<String>bills = StringUtil.getXmls(texts, "BILL");
        for (String b :bills) {
            String id = StringUtil.getXml(b, "ID"); // 必须
            String source = StringUtil.getXml(b, "SR");
            int mode = StringUtil.getXmlInt(b, "MD");
            String kind = StringUtil.getXml(b, "KD");
            double amount = StringUtil.getXmlDouble(b, "AM"); // 必须
            String note = StringUtil.getXml(b, "NT");
            String card = StringUtil.getXml(b, "CD");
            String place = StringUtil.getXml(b, "PL");
            long timestamp = StringUtil.getXmlLong(b, "TT");
            long addTime = StringUtil.getXmlLong(b, "AT");
            long changeTime = StringUtil.getXmlLong(b, "CT");
            boolean reimburse = StringUtil.getXmlBoolean(b, "RB");
            long remind = StringUtil.getXmlLong(b, "RM");

            addItem(createDummyItem(id, amount, mode, kind, source, note, card, place, timestamp, addTime, changeTime, reimburse, remind));
        }
    }

    /**
     * 手动新添加的列表，为其分配 ID
     * @param amount
     * @param mode
     * @param kind
     * @param source
     * @param note
     * @param card
     * @param place
     * @param timestamp
     * @param addTime
     */
    public static void addNew(double amount, int mode, String kind, String source, String note, String card, String place, long timestamp, long addTime) {
        String id = addTime + "_" + ITEMS.size();
        insertNew(id, amount, mode, kind, source, note, card, place, timestamp, addTime, 0, false, 0);
    }

    /**
     * 修改某个选项
     * 修改方式是全部删掉，重新建立
     * @param id
     * @param amount
     * @param mode
     * @param kind
     * @param source
     * @param note
     * @param card
     * @param place
     * @param timestamp
     * @param changeTime
     */
    public static void moidfyItem(String id, double amount, int mode, String kind, String source, String note, String card, String place, long timestamp, long changeTime) {
        DummyItem item = ITEM_MAP.get(id);
        if (item == null) return ;
        long addTime = item.addTime;
        boolean reimburse = item.reimburse;
        long remind = item.remind;
        DummyItem item2 = createDummyItem(id, amount, mode, kind, source, note, card, place, timestamp, addTime, changeTime, reimburse, remind);

        // 修改数组
        for (int i = 0; i < ITEMS.size(); i++) {
            if (ITEMS.get(i).id.equals(id)) {
                ITEMS.set(i, item2);
                ITEM_MAP.remove(id);
                ITEM_MAP.put(id, item2);
            }
        }

        // 修改文件
        String itemText = item2.toString();
        String text = FileUtil.readTextVals("bills.txt");
        int pos = text.indexOf("<ID>" + id);
        if (pos != -1) {
            int left = text.lastIndexOf("<BILL>", pos);
            int right = text.indexOf("</BILL>", pos);
            text = text.substring(0, left) + itemText + text.substring(right+"</BILL>".length());
        } else {
            text = itemText + text;
        }
        FileUtil.writeTextVals("bills.txt", text);
    }

    /**
     * 插入一个新的账单记录
     * @param id
     * @param amount
     * @param mode
     * @param kind
     * @param source
     * @param note
     * @param card
     * @param place
     * @param timestamp
     * @param addTime
     * @param changeTime
     * @param reimburse
     * @param remind
     */
    public static void insertNew(String id, double amount, int mode, String kind, String source, String note, String card, String place, long timestamp, long addTime, long changeTime, Boolean reimburse, long remind) {
        DummyItem item = createDummyItem(id, amount, mode, kind, source, note, card, place, timestamp, addTime, changeTime, reimburse, remind);
        ITEMS.add(0, item);
        ITEM_MAP.put(item.id, item);

        String text = FileUtil.readTextVals("bills.txt");
        text = item.toString() + text;
        FileUtil.writeTextVals("bills.txt", text);
    }

    public static void addNew(String id, double amount, int mode, String kind, String source, String note, String card, String place, long timestamp, long addTime, long changeTime, Boolean reimburse, long remind) {
        DummyItem item = createDummyItem(id, amount, mode, kind, source, note, card, place, timestamp, addTime, changeTime, reimburse, remind);
        addItem(item);

        String text = FileUtil.readTextVals("bills.txt");
        text = item.toString() + text;
        FileUtil.writeTextVals("bills.txt", text);
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * 类似工厂模式添加 Dummy
     */
    private static DummyItem createDummyItem(String id, double amount, int mode, String kind, String source, String note, String card, String place, long timestamp, long addTime, long changeTime, Boolean reimburse, long remind) {
        return new DummyItem(id, amount, mode, kind, source, note, card, place, timestamp, addTime, changeTime, reimburse, remind);
    }

    public static void removeItem(String businessID) {
        DummyItem item = ITEM_MAP.get(businessID);
        if (item == null) return ;
        ITEM_MAP.remove(item);
        ITEMS.remove(item);
        String s = FileUtil.readTextVals("bills.txt");
        int pos = s.indexOf("<ID>" + businessID + "</ID>");
        if (pos == -1) return ;
        int left = s.lastIndexOf("<BILL>", pos);
        int right = s.indexOf("</BILL>", pos);
        if (left == -1 || right == -1) return ;
        s = s.substring(0, left) + s.substring(right+"</BILL>".length());
        FileUtil.writeTextVals("bills.txt", s);
    }

    public static class DummyItem {

        public String  id;         // 账单ID（根据时间随机）
        public double  amount;     // 账单金额（+收入，-支出）
        public int     mode;       // 账单类型（支出、收入、借贷）
        public String  kind;       // 账单种类（吃喝住行、工资、借还等）
        public String  source;     // 账单源头（消费、获取）
        public String  note;       // 账单备注
        public String  card;       // 哪张卡
        public String  place;      // 消费地点
        public long    timestamp;  // 账单时间戳（以消费时间为准）
        public long    addTime;    // 添加时间戳（以添加时间为准）
        public long    changeTime; // 修改时间戳
        public Boolean reimburse;  // 是否能报销/归还
        public long    remind;     // 提醒时间戳

        public DummyItem(String id, int amount) {
            this.id = id;
            this.amount = amount;
        }

        public DummyItem(String id, double amount, int mode, String kind, String source, String note, String card, String place, long timestamp, long addTime, long changeTime, Boolean reimburse, long remind) {
            this.id = id;
            this.amount = amount;
            this.mode = mode;
            this.source = source;
            this.kind = kind;
            this.note = note;
            this.card = card;
            this.place = place;
            this.timestamp = timestamp;
            this.addTime = addTime;
            this.changeTime = changeTime;
            this.reimburse = reimburse;
            this.remind = remind;
        }

        @Override
        public String toString() {
            return StringUtil.toXml(
                    StringUtil.toXml(id, "ID")
                    + StringUtil.toXml(mode+"", "MD")
                    + StringUtil.toXml(kind, "KD")
                    + StringUtil.toXml(source, "SR")
                    + StringUtil.toXml(amount, "AM")
                    + StringUtil.toXml(note, "NT")
                    + StringUtil.toXml(card, "CD")
                    + StringUtil.toXml(place, "PL")
                    + StringUtil.toXml(timestamp, "TT")
                    + StringUtil.toXml(addTime, "AT")
                    + StringUtil.toXml(changeTime, "CT")
                    + StringUtil.toXml(reimburse, "RB")
                    + StringUtil.toXml(remind, "RM")
            , "BILL");
        }
    }

    private static void initStartBills() {
        String text = "<BILL><ID>01</ID><SR>欢迎来到 穷光蛋的世界</SR><AM>1.00</AM><NT>你知道，自己只是个穷光蛋，一贫如洗</NT></BILL>";
        text += "<BILL><ID>02</ID><SR>其实啊，你很幸运</SR><AM>2.00</AM><NT>真的很幸运，能和本开发者一同品味着贫穷，品味着无力</NT></BILL>";
        text += "<BILL><ID>03</ID><SR>下一世</SR><AM>3.00</AM><NT>心愿 · 下一世，我们必将，生活在无忧无虑的富饶世界！</NT></BILL>";
        FileUtil.writeTextVals("bills.txt", text);
        Log.i("initStartBills", text);
    }
}
