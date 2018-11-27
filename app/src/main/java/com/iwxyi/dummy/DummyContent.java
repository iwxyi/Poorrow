package com.iwxyi.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position) {
        return new DummyItem(position+"", 100);
    }

    public static class DummyItem {

        public String  id;         // 账单ID（根据时间随机）
        public int     amount;     // 账单金额（+收入，-支出）
        public String  source;     // 账单源头（消费、获取）
        public String  kind;       // 账单种类（吃喝住行、工资、借还等）
        public String  note;       // 账单备注
        public String  card;       // 哪张卡
        public long    timestamp;  // 账单时间戳（以消费时间为准）
        public long    addTime;    // 添加时间戳（以添加时间为准）
        public long    changeTime; // 修改时间戳
        public Boolean reimburse;  // 是否能报销/归还
        public long    remind;     // 定时提醒

        public DummyItem(String id, int amount) {
            this.id = id;
            this.amount = amount;
        }

        public DummyItem(String id, int amount, String source, String kind, String note, String card, long timestamp, long addTime, long changeTime, Boolean reimburse, long remind) {
            this.id = id;
            this.amount = amount;
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
            return xml(id, "ID") + xml(source, "SR") + xml(kind, "KD") + xml(amount+"", "AM")
                    + xml(note, "NT") + xml(card, "CD") + xml(timestamp+"", "TT")
                    + xml(addTime+"", "AT") + xml(changeTime+"", "CT")
                    + xml(reimburse+"", "RB") + xml(remind+"", "RM");
        }

        private String xml(String data, String tag) {
            return "<" + tag + ">" + data + "</" + tag + ">";
        }
    }
}
