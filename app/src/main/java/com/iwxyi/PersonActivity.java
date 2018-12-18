package com.iwxyi;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iwxyi.Utils.SettingsUtil;
import com.iwxyi.Utils.UserInfo;

import java.util.regex.Pattern;

public class PersonActivity extends AppCompatActivity implements View.OnClickListener {

    private static int RESULT_CODE_PERSON_OK = 104;

    private TextView mNicknameTv;
    private TextView mUsernameTv;
    private TextView mPasswordTv;
    private TextView mSignatureTv;
    private TextView mCellphoneTv;
    private Button mSignoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //TextView tv = (TextView) super.findViewById(R.id.tv_large);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)
                super.findViewById(R.id.toolbar_layout); // 用来设置背景图片的
        if (!"".equals(UserInfo.nickname)) {
            this.setTitle(UserInfo.nickname);
        } else {
            this.setTitle(UserInfo.username);
        }
        collapsingToolbarLayout.setBackgroundResource(R.drawable.bg_orange);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "正在同步，请稍后", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initView();
        readUserInfo();
    }

    private void readUserInfo() {
        mNicknameTv.setText("昵　称：" + UserInfo.nickname);
        mUsernameTv.setText("用户名：" + UserInfo.username);
        mPasswordTv.setText("密　码：" + "********");
        mSignatureTv.setText("签　名：" + UserInfo.signature);
        mCellphoneTv.setText("手机号：" + UserInfo.cellphone);
    }

    private void initView() {
        mNicknameTv = (TextView) findViewById(R.id.tv_nickname);
        mNicknameTv.setOnClickListener(this);
        mUsernameTv = (TextView) findViewById(R.id.tv_username);
        mUsernameTv.setOnClickListener(this);
        mPasswordTv = (TextView) findViewById(R.id.tv_password);
        mPasswordTv.setOnClickListener(this);
        mSignatureTv = (TextView) findViewById(R.id.tv_signature);
        mSignatureTv.setOnClickListener(this);
        mCellphoneTv = (TextView) findViewById(R.id.tv_cellphone);
        mCellphoneTv.setOnClickListener(this);
        mSignoutBtn = (Button) findViewById(R.id.btn_signout);
        mSignoutBtn.setOnClickListener(this);
    }

    private String inputDialog(final String aim, String title, String def) {
        final String[] result = new String[1];
        LayoutInflater factory = LayoutInflater.from(PersonActivity.this);//提示框
        final View view = factory.inflate(R.layout.editbox, null);//这里必须是final的
        final EditText edit=(EditText)view.findViewById(R.id.editText);//获得输入框对象
        edit.setText(def);
        new AlertDialog.Builder(PersonActivity.this)
                .setTitle(title)//提示框标题
                .setView(view)
                .setPositiveButton("确定",//提示框的两个按钮
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                result[0] = edit.getText().toString();
                                onInputDialog(aim, edit.getText().toString());
                            }
                        })
                .setNegativeButton("取消", null)
                .create().show();
        return result[0];
    }

    private String inputDialog2(String title, String def) {final EditText et = new EditText(this);
        et.setText(def);
        final String[] result = {""};
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setIcon(R.drawable.ic_dan)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result[0] = et.getText().toString();
                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
        return result[0];
    }

    private boolean canMatch(String string, String pattern) {
        if ("".equals(string)) return false;
        return Pattern.matches(pattern, string);
    }

    @Override
    public void onClick(View v) {
        String s;
        switch (v.getId()) {
            case R.id.tv_nickname:
                s = inputDialog("nickname", "请修改昵称（支持中文/数字/字母/下划线）", UserInfo.nickname);
                break;
            case R.id.tv_username:
                Toast.makeText(this, "不允许修改用户名", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_password:
                s = inputDialog("old_password", "请输入旧密码", "");
                break;
            case R.id.tv_signature:
                s = inputDialog("signature", "请修改签名（不包含特殊字符）", UserInfo.signature);
                break;
            case R.id.tv_cellphone:
                s = inputDialog("cellphone", "请修改手机号", UserInfo.cellphone);
                break;
            case R.id.btn_signout:
                UserInfo.logined = false;
                UserInfo.userID = "";
                SettingsUtil.setVal(getApplicationContext(), "userID", "");
                Toast.makeText(this, "您已成功退出登录", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CODE_PERSON_OK);
                finish();
                break;
            default:
                break;
        }
    }

    private void onInputDialog(String aim, String s) {
        if (aim.equals("username")) {
            ;
        } else if (aim.equals("nickname")) {
            if (canMatch(s, "^[\\w_@]+$")) {
                mNicknameTv.setText("昵　称：" + s);
                UserInfo.nickname = s;
                SettingsUtil.setVal(getApplicationContext(), "nickname", s);
                setResult(RESULT_CODE_PERSON_OK);
                setTitle(s);
            } else {
                Toast.makeText(this, "请输入正确的格式", Toast.LENGTH_SHORT).show();
            }
        } else if (aim.equals("old_password")) {
            if ("".equals(s)) return;
            if (!s.equals(UserInfo.password)) {
                Toast.makeText(this, "旧密码错误", Toast.LENGTH_SHORT).show();
                return;
            }
            s = inputDialog("password", "请修改密码（支持中文/数字/字母/下划线）", UserInfo.password);
        } else if (aim.equals("password")) {
            if (canMatch(s, "^[\\w_@]+$")) {
                mPasswordTv.setText("密　码：" + s);
                UserInfo.password = s;
                SettingsUtil.setVal(getApplicationContext(), "password", s);
                setResult(RESULT_CODE_PERSON_OK);
            } else if (!"".equals(s)) {
                Toast.makeText(this, "请输入正确的格式", Toast.LENGTH_SHORT).show();
            }
        } else if (aim.equals("signature")) {
            if (canMatch(s, "^[^\n\t']+$")) {
                mSignatureTv.setText("签　名：" + s);
                UserInfo.signature = s;
                SettingsUtil.setVal(getApplicationContext(), "signature", s);
                setResult(RESULT_CODE_PERSON_OK);
            } else if (!"".equals(s)) {
                Toast.makeText(this, "请输入正确的格式", Toast.LENGTH_SHORT).show();
            }
        } else if (aim.equals("cellphone")) {
            if (canMatch(s, "^(\\+\\d{1,3})?[\\d]{11}$")) {
                mCellphoneTv.setText("手机号：" + s);
                UserInfo.cellphone = s;
                SettingsUtil.setVal(getApplicationContext(), "cellphone", s);
                setResult(RESULT_CODE_PERSON_OK);
            } else if (!"".equals(s)) {
                Toast.makeText(this, "请输入正确的格式", Toast.LENGTH_SHORT).show();
            }
        }


    }
}
