package com.iwxyi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public class RecordActivity extends AppCompatActivity {
    private RadioButton mSpendingCb;
    private RadioButton mIncomeCb;
    private RadioButton mBorrowingCb;
    private RecyclerView mKindRv;
    private Spinner mCardSp;
    private EditText mAmountEv;
    private EditText mNoteEv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        initView();
        initData();
    }

    private void initView() {
        mSpendingCb = (RadioButton) findViewById(R.id.cb_spending);
        mIncomeCb = (RadioButton) findViewById(R.id.cb_income);
        mBorrowingCb = (RadioButton) findViewById(R.id.cb_borrowing);
        mKindRv = (RecyclerView) findViewById(R.id.rv_kind);
        mCardSp = (Spinner) findViewById(R.id.sp_card);
        mAmountEv = (EditText) findViewById(R.id.ev_amount);
        mNoteEv = (EditText) findViewById(R.id.ev_note);

        mSpendingCb.setChecked(true);
    }

    private void initData() {
        String[] cardType = new String[]{"默认", "现金", "银行卡"};
        ArrayAdapter<String> cardAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cardType);
        cardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item); // 设置下拉框的样式
        mCardSp.setAdapter(cardAdapter);
    }
}

