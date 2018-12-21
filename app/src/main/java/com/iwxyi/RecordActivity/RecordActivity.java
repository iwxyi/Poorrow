package com.iwxyi.RecordActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.iwxyi.BillsFragment.DummyContent;
import com.iwxyi.R;
import com.iwxyi.Utils.DateTimeUtil;
import com.iwxyi.Utils.FileUtil;
import com.iwxyi.Utils.SettingsUtil;

import net.steamcrafted.lineartimepicker.dialog.LinearDatePickerDialog;
import net.steamcrafted.lineartimepicker.dialog.LinearTimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListener {

    private int REQUEST_CODE_RECORD = 1;
    private int REQUEST_CODE_MODIFY = 2;
    private int REQUEST_CODE_MAP = 3;

    private int RESULT_CODE_RECORD_OK = 101;
    private int RESULT_CODE_MODIFY_OK = 102;

    private RadioButton mSpendingRb;
    private RadioButton mIncomeRb;
    private RadioButton mBorrowingRb;
    private GridView mKindGv;
    private Spinner mCardSp;
    private EditText mAmountEv;
    private EditText mNoteEv;
    private Button mSubmitBtn;
    private TextView mDateTv;
    private TextView mTimeTv;
    private TextView mPlaceTv;

    private String businessID = null;
    private String[] kindList;
    private ArrayList<KindBean> kindArray;
    private int kindChoosing;
    private String cardChoosing = "默认";
    private String placeChoosing;

    private int addYear, addMonth, addDate, addHour, addMinute;
    private int tsYear, tsMonth, tsDate, tsHour, tsMinute;
    private Button mDeleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        initView();
        initData();

        initModify();
    }

    private void initView() {
        mSpendingRb = (RadioButton) findViewById(R.id.rb_spending);
        mIncomeRb = (RadioButton) findViewById(R.id.rb_income);
        mBorrowingRb = (RadioButton) findViewById(R.id.rb_borrowing);
        mKindGv = (GridView) findViewById(R.id.gv_kind);
        mCardSp = (Spinner) findViewById(R.id.sp_card);
        mAmountEv = (EditText) findViewById(R.id.ev_amount);
        mNoteEv = (EditText) findViewById(R.id.ev_note);
        mSubmitBtn = (Button) findViewById(R.id.btn_submit);
        mDateTv = (TextView) findViewById(R.id.tv_date);
        mTimeTv = (TextView) findViewById(R.id.tv_time);
        mPlaceTv = (TextView) findViewById(R.id.tv_place);
        mKindGv.setOnItemClickListener(this);
        mDateTv.setOnClickListener(this);
        mTimeTv.setOnClickListener(this);
        mPlaceTv.setOnClickListener(this);
        mSubmitBtn.setOnClickListener(this);
        mDeleteBtn = (Button) findViewById(R.id.btn_delete);
        mDeleteBtn.setOnClickListener(this);
        mDeleteBtn.setVisibility(View.GONE);
        mCardSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                cardChoosing = selected;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initData() {
        kindChoosing = -1;
        cardChoosing = "默认";
        placeChoosing = "";

        // ==== 初始化卡 ====
        String cardString = null;
        cardString = FileUtil.readTextVals("cards.txt");

        if ("".equals(cardString)) {
            cardString = "默认\n现金";
            FileUtil.writeTextVals("cards.txt", cardString);
        }
        String[] cardType = cardString.split("\n");
        ArrayAdapter<String> cardAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cardType);
        cardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item); // 设置下拉框的样式
        mCardSp.setAdapter(cardAdapter);
        cardChoosing = cardType[0];

        // ==== 初始化种类 ====
        kindArray = new ArrayList<>();
        int a = SettingsUtil.getInt(getApplicationContext(), "record_kind");
        emitGVAdapter(a);

        // ==== 初始化时间 ====
        Calendar c = Calendar.getInstance();
        addYear = c.get(Calendar.YEAR);
        addMonth = c.get(Calendar.MONTH) + 1;
        addDate = c.get(Calendar.DAY_OF_MONTH);
        addHour = c.get(Calendar.HOUR_OF_DAY);
        addMinute = c.get(Calendar.MINUTE);
        tsYear = tsMonth = tsDate = tsHour = tsMinute = 0;
        mDateTv.setText("" + DateTimeUtil.dataToString(addYear, addMonth, addDate));
        mTimeTv.setText("" + DateTimeUtil.timeToString(addHour, addMinute));
    }

    private void initModify() {
        // 如果是修改，读取修改的内容
        Intent intent = getIntent();
        businessID = intent.getStringExtra("billID");
        if (!"".equals(businessID)) {
            DummyContent.DummyItem item = DummyContent.ITEM_MAP.get(businessID);
            if (item == null) {
                businessID = "";
                return;
            }
            double amount = item.amount;
            int mode = item.mode;
            String kind = item.kind;
            String source = item.source;
            String note = item.note;
            String card = item.card;
            String place = item.place;
            long timestamp = item.timestamp;
            long addTime = item.addTime;

            if (amount != 0) {
                mAmountEv.setText(amount + "");
            }
            if (mode == 1) {
                mIncomeRb.setChecked(true);
                emitGVAdapter(1);
            } else if (mode == 2) {
                mBorrowingRb.setChecked(true);
                emitGVAdapter(2);
            } else {
                mSpendingRb.setChecked(true);
                emitGVAdapter(0);
            }
            if (!"".equals(kind)) {
                for (int i = 0; i < kindList.length; i++) {
                    if (kind.equals(kindList[i])) {
                        kindChoosing = i;
                        kindArray.get(i).choose = true;
                        MyKindAdapter kindApdapter = new MyKindAdapter(this, kindArray);
                        mKindGv.setAdapter(kindApdapter);
                        break;
                    }
                }
            }
            if (!("".equals(source))) {
                if ("".equals(note)) {
                    note = source;
                } else {
                    note = source + "\n" + note;
                }
            }
            mNoteEv.setText(note);
            if (!"".equals(place)) {
                mPlaceTv.setText(place);
            }
            if (timestamp > 0) {
                tsYear = DateTimeUtil.getYearFromTimestamp(timestamp);
                tsMonth = DateTimeUtil.getMonthFromTimestamp(timestamp);
                tsDate = DateTimeUtil.getDateFromTimestamp(timestamp);
                tsHour = DateTimeUtil.getHourFromTimestamp(timestamp);
                tsMinute = DateTimeUtil.getMinuteFromTimestamp(timestamp);
                mDateTv.setText("" + DateTimeUtil.dataToString(tsYear, tsMonth, tsDate));
                mTimeTv.setText("" + DateTimeUtil.timeToString(tsHour, tsMinute));
            } else if (addTime > 0) {
                addYear = DateTimeUtil.getYearFromTimestamp(addTime);
                addMonth = DateTimeUtil.getMonthFromTimestamp(addTime);
                addDate = DateTimeUtil.getDateFromTimestamp(addTime);
                addHour = DateTimeUtil.getHourFromTimestamp(addTime);
                addMinute = DateTimeUtil.getMinuteFromTimestamp(addTime);
                mDateTv.setText("" + DateTimeUtil.dataToString(addYear, addMonth, addDate));
                mTimeTv.setText("" + DateTimeUtil.timeToString(addHour, addMinute));
            }

            mSubmitBtn.setText("确认修改");
            mDeleteBtn.setVisibility(View.VISIBLE);
        }
    }

    private void emitGVAdapter(int x) {
        String fileName, def;
        if (x == 1) { // 收入
            mIncomeRb.setChecked(true);
            fileName = "kinds_income.txt";
            def = "工资\n理财\n捡到\n礼物";
        } else if (x == 2) { // 借贷
            mBorrowingRb.setChecked(true);
            fileName = "kinds_borrowing.txt";
            def = "借出\n归还\n转账";
        } else /*x == 0*/ { // 支出
            mSpendingRb.setChecked(true);
            fileName = "kinds_spending.txt";
            def = "三餐\n点心\n饮品\n衣服\n房租\n娱乐\n旅行\n日用品\n运动\n美妆\n交通\n话费\n游戏";
        }

        // 读取消费种类的类型
        String kindString = null;
        kindChoosing = -1;
        kindString = FileUtil.readTextVals(fileName);

        // 如果文件不存在，则创建文件，保存内容，并且使用默认的列表
        if ("".equals(kindString)) {
            kindString = def;
            FileUtil.writeTextVals(fileName, kindString);
        }
        kindList = kindString.split("\n");
        // 将字符串数组转换到 KindBean 数组
        kindArray.clear();
        for (String s : kindList) {
            kindArray.add(new KindBean(s));
        }
        MyKindAdapter kindApdapter = new MyKindAdapter(this, kindArray);
        mKindGv.setAdapter(kindApdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit: // 确定添加
                if ("".equals(mAmountEv.getText().toString())) {
                    Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
                    return;
                }

                int mode = 0;
                if (mSpendingRb.isChecked()) {
                    mode = 0;
                } else if (mIncomeRb.isChecked()) {
                    mode = 1;
                } else if (mBorrowingRb.isChecked()) {
                    mode = 2;
                }
                String kind = "";
                if (kindChoosing >= 0) {
                    kind = kindList[kindChoosing];
                }
                String card = cardChoosing;
                String place = "";
                double amount = Double.valueOf(mAmountEv.getText().toString());
                if (mode == 0 && amount > 0) {
                    amount *= -1;
                }
                String source;
                String note = mNoteEv.getText().toString();
                long timestamp = DateTimeUtil.valsToTimestamp(tsYear, tsMonth, tsDate, tsHour, tsMinute, 0);
                long addTime = DateTimeUtil.valsToTimestamp(addYear, addMonth, addDate, addHour, addMinute, 0);
                int linePos = note.indexOf("\n");
                if (linePos > -1) {
                    source = note.substring(0, linePos);
                    note = note.substring(linePos + 1, note.length());
                } else {
                    if ("".equals(note))
                        source = kind;
                    else
                        source = note;
                    note = "";
                }

                if ("".equals(businessID) && !"null".equals(businessID)) {
                    DummyContent.addNew(amount, mode, kind, source, note, card, place, timestamp, addTime);
                    setResult(RESULT_CODE_RECORD_OK);
                } else {
                    DummyContent.moidfyItem(businessID, amount, mode, kind, source, note, card, place, timestamp, addTime);
                    setResult(RESULT_CODE_MODIFY_OK);
                }

                finish();
                break;
            case R.id.rb_spending: // 选择支出
                emitGVAdapter(0);
                SettingsUtil.setVal(getApplicationContext(), "record_kind", 0);
                break;
            case R.id.rb_income: // 选择收入
                emitGVAdapter(1);
                SettingsUtil.setVal(getApplicationContext(), "record_kind", 1);
                break;
            case R.id.rb_borrowing: // 选择借贷
                SettingsUtil.setVal(getApplicationContext(), "record_kind", 2);
                emitGVAdapter(2);
                break;
            case R.id.tv_date:
                boolean data_tutorial = false;
                if (SettingsUtil.getInt(getApplicationContext(), "LinearDatePicker_tutorial") != 0) {
                    data_tutorial = true;
                    SettingsUtil.setVal(getApplicationContext(), "LinearDataPicker__tutorial", 0);
                }
                LinearDatePickerDialog.Builder.with(RecordActivity.this)
                        .setYear(addYear)
                        .setMinYear(2000)
                        .setMaxYear(2030)
                        .setShowTutorial(data_tutorial)
                        .setButtonCallback(new LinearDatePickerDialog.ButtonCallback() {
                            @Override
                            public void onPositive(DialogInterface dialog, int year, int month, int day) {
                                tsYear = year;
                                tsMonth = month;
                                tsDate = day;

                                if (tsHour == 0 && tsMinute == 0) {
                                    tsHour = addHour;
                                    tsMinute = addMinute;
                                }

                                mDateTv.setText("日期：" + DateTimeUtil.dataToString(tsYear, tsMonth, tsDate));
                            }

                            @Override
                            public void onNegative(DialogInterface dialog) {

                            }
                        })
                        .build()
                        .show();

                break;
            case R.id.tv_time:
                boolean time_tutorial = false;
                if (SettingsUtil.getInt(getApplicationContext(), "LinearTimePicker_tutorial") != 0) {
                    time_tutorial = true;
                    SettingsUtil.setVal(getApplicationContext(), "LinearTimePicker__tutorial", 0);
                }
                LinearTimePickerDialog dialog = LinearTimePickerDialog.Builder.with(RecordActivity.this)
                        .setShowTutorial(time_tutorial)
                        .setButtonCallback(new LinearTimePickerDialog.ButtonCallback() {
                            @Override
                            public void onPositive(DialogInterface dialog, int hour, int minutes) {
                                tsHour = hour;
                                tsMinute = minutes;

                                if (tsYear == 0) {
                                    tsYear = addYear;
                                    tsMonth = addMonth;
                                    tsDate = addDate;
                                }

                                mTimeTv.setText("时间：" + DateTimeUtil.timeToString(tsHour, tsMinute));
                            }

                            @Override
                            public void onNegative(DialogInterface dialog) {

                            }
                        })
                        .build();
                dialog.show();

                break;
            case R.id.tv_place:
                //startActivityForResult(new Intent(getApplicationContext(), MapActivity.class), REQUEST_CODE_MAP);
                startActivity(new Intent(getApplicationContext(), MapActivity.class));
                break;
            case R.id.btn_delete:
                if ("".equals(businessID))
                    break;
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("是否删除？")
                        .setContentText("此操作将无法恢复（但可从备份中还原）")
                        .setConfirmText("确认删除")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                DummyContent.removeItem(businessID);
                                finish();
                            }
                        })
                        .show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (kindChoosing == position) { // 重新点击选中的，取消选中
            kindArray.get(position).choose = false;
            kindChoosing = -1;
        } else { // 未选中的，选中
            if (kindChoosing >= 0) {
                kindArray.get(kindChoosing).choose = false;
            }
            kindChoosing = position;
            kindArray.get(position).choose = true;
        }
        MyKindAdapter kindApdapter = new MyKindAdapter(this, kindArray);
        mKindGv.setAdapter(kindApdapter);
    }
}

