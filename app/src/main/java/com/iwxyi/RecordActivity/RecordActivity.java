package com.iwxyi.RecordActivity;

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
import android.widget.Toast;

import com.iwxyi.Utils.FileUtil;
import com.iwxyi.R;
import com.iwxyi.Utils.SettingsUtil;

import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListener {

    private int RECULT_CODE_OK = 1;

    private RadioButton mSpendingRb;
    private RadioButton mIncomeRb;
    private RadioButton mBorrowingRb;
    private GridView mKindGv;
    private Spinner mCardSp;
    private EditText mAmountEv;
    private EditText mNoteEv;
    private Button mSubmitBtn;

    private String[] kindList;
    private ArrayList<KindBean> kindArray;
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
    }

    private void initData() {
        // 初始化卡
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

        // 初始化种类
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
            case R.id.btn_submit:
                if ("".equals(mAmountEv.getText().toString())) {
                    Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
                    return ;
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
                String card = mCardSp.toString();
                double amount = Double.valueOf(mAmountEv.getText().toString());
                String note = mNoteEv.getText().toString();
                long timestamp = 0;

                // 保存到 Bundle
                Intent intent = new Intent();
                intent.putExtra("record_mode", mode);
                intent.putExtra("record_kind", kind);
                intent.putExtra("record_card", card);
                intent.putExtra("record_amount", amount);
                intent.putExtra("record_note", note);
                intent.putExtra("record_timestamp", timestamp);

                setResult(RECULT_CODE_OK, intent);
                finish();
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

