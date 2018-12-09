package com.iwxyi;

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
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListener {

    private RadioButton mSpendingRb;
    private RadioButton mIncomeRb;
    private RadioButton mBorrowingRb;
    private GridView mKindGv;
    private Spinner mCardSp;
    private EditText mAmountEv;
    private EditText mNoteEv;
    private Button mSubmitBtn;

    private String[] kindList;
    private int kindChoosing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        initView();
        initData();
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
        mKindGv.setOnItemClickListener(this);
        mSubmitBtn.setOnClickListener(this);

        // 初始化种类
        int a = SettingsUtil.getInt(getApplicationContext(), "record_kind");
        emitGVAdapter(a);
    }

    private void initData() {
        // 读取卡的列表
        String cardString = null;
        try {
            cardString = FileUtil.readTextVals("cards.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if ("".equals(cardString)) {
            cardString = "默认\n现金";
            FileUtil.writeTextVals("cards.txt", cardString);
        }
        String[] cardType = cardString.split("\n");
        ArrayAdapter<String> cardAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cardType);
        cardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item); // 设置下拉框的样式
        mCardSp.setAdapter(cardAdapter);
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
//        Toast.makeText(this, def, Toast.LENGTH_SHORT).show();
        // 读取消费种类的类型
        String kindString = null;
        kindChoosing = -1;
        try {
            kindString = FileUtil.readTextVals(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if ("".equals(kindString)) {
            kindString = def;
            FileUtil.writeTextVals(fileName, kindString);
        }
        kindList = kindString.split("\n");
        ArrayList<String> kindArray = new ArrayList();
        for (String s : kindList) { // 不知道该怎么转换，就只能用循环一个个遍历啦
            kindArray.add(s);
        }
        MyKindApdapter kindApdapter = new MyKindApdapter(this, kindArray);
        mKindGv.setAdapter(kindApdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if ("".equals(mAmountEv.getText().toString())) {
                    Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.rb_spending :
                emitGVAdapter(0);
                SettingsUtil.setVal(getApplicationContext(), "record_kind", 0);
                break;
            case R.id.rb_income :
                emitGVAdapter(1);
                SettingsUtil.setVal(getApplicationContext(), "record_kind", 1);
                break;
            case R.id.rb_borrowing :
                SettingsUtil.setVal(getApplicationContext(), "record_kind", 2);
                emitGVAdapter(2);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}

