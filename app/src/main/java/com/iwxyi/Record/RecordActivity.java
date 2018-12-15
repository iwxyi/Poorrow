package com.iwxyi.Record;

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

import com.iwxyi.Fragments.BillsList.DummyContent;
import com.iwxyi.R;
import com.iwxyi.Utils.DateTimeUtil;
import com.iwxyi.Utils.FileUtil;
import com.iwxyi.Utils.SettingsUtil;

import net.steamcrafted.lineartimepicker.dialog.LinearDatePickerDialog;
import net.steamcrafted.lineartimepicker.dialog.LinearTimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListener {

    private int REQUEST_CODE_MAP = 10;
    private int RESULT_CODE_OK = 1;

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

    private String[] kindList;
    private ArrayList<KindBean> kindArray;
    private int kindChoosing;
    private String cardChoosing = "默认";
    private String placeChoosing;

    private int addYear, addMonth, addDate, addHour, addMinute;
    private int tsYear, tsMonth, tsDate, tsHour, tsMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        initView();
        initData();
    }

    /**
     * 这个是每次切换都会产生的
     * 因为每次添加都要重新修改时间
     */
    @Override
    protected void onStart() {
        initData();

        Calendar c = Calendar.getInstance();
        addYear = c.get(Calendar.YEAR);
        addMonth = c.get(Calendar.MONTH) + 1;
        addDate = c.get(Calendar.DAY_OF_MONTH);
        addHour = c.get(Calendar.HOUR_OF_DAY);
        addMinute = c.get(Calendar.MINUTE);

        tsYear = tsMonth = tsDate = tsHour = tsMinute = 0;
        /*
        tsYear = addYear;
        tsMonth = addMonth;
        tsDate = addDate;
        tsHour = addHour;
        tsMinute = addMinute;
         */

        mDateTv.setText("日期：" + DateTimeUtil.dataToString(addYear, addMonth, addDate));
        mTimeTv.setText("时间：" + DateTimeUtil.timeToString(addHour, addMinute));

        super.onStart();
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
                String source;
                String note = mNoteEv.getText().toString();
                long timestamp = DateTimeUtil.valsToTimestamp(tsYear, tsMonth, tsDate, tsHour, tsMinute, 0);
                long addTime = DateTimeUtil.valsToTimestamp(addYear, addMonth, addDate, addHour, addMinute, 0);
                int linePos = note.indexOf("\n");
                if (linePos > -1) {
                    source = note.substring(0, linePos);
                    note = note.substring(linePos+1, note.length());
                } else {
                    if ("".equals(note))
                        source = kind;
                    else
                        source = note;
                    note = "";
                }

                // 保存到 Bundle，原 Activity 便于读取
                Intent intent = new Intent();
                intent.putExtra("record_mode", mode);
                intent.putExtra("record_kind", kind);
                intent.putExtra("record_card", card);
                intent.putExtra("record_amount", amount);
                intent.putExtra("record_note", note);
                intent.putExtra("record_place", place);
                intent.putExtra("record_timestamp", timestamp);
                intent.putExtra("record_addTime", addTime);

                DummyContent.addNew(amount, mode, kind, source, note, card, place, timestamp, addTime);

                setResult(RESULT_CODE_OK, intent);
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

                                mTimeTv.setText("时间：" + DateTimeUtil.timeToString(tsHour, tsMinute));
                            }

                            @Override
                            public void onNegative(DialogInterface dialog) {

                            }
                        })
                        .build();
                dialog.show();

                break;
            case R.id.tv_place :
                //startActivityForResult(new Intent(getApplicationContext(), MapActivity.class), REQUEST_CODE_MAP);
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

