package com.iwxyi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {
    private RadioButton mSpendingRb;
    private RadioButton mIncomeRb;
    private RadioButton mBorrowingRb;
    private GridView mKindGv;
    private Spinner mCardSp;
    private EditText mAmountEv;
    private EditText mNoteEv;
    private Button mSubmitBtn;

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
        mSubmitBtn.setOnClickListener(this);

        // 初始化种类
        int a = SettingsUtil.getInt(getApplicationContext(), "recent");
        if (a == 0) {
            mSpendingRb.setChecked(true);
        } else if (a == 1) {
            mIncomeRb.setChecked(true);
        } else if (a == 2) {
            mBorrowingRb.setChecked(true);
        } else {
            mSpendingRb.setChecked(true);
        }

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

        // 读取消费种类的类型
        String kindString = null;
        try {
            kindString = FileUtil.readTextVals("kinds.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if ("".equals(kindString)) {
            kindString = "三餐\n点心\n饮品\n烟酒\n房租\n娱乐\n旅行\n日用品\n运动\n美妆\n交通\n话费\n游戏";
            FileUtil.writeTextVals("kinds.txt", kindString);
        }
        String[] kindType = kindString.split("\n");
        ArrayList<String> kindArray = new ArrayList();
        for (String s : kindType) { // 不知道该怎么转换，就只能用循环一个个遍历啦
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
            default:
                break;
        }
    }
}

