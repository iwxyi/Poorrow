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
        mPasswordTv.setText("密　码：" + UserInfo.password);
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

    private String inputDialog(String title, String def) {
        ;
        return "";
    }

    private String inputDialog3(String title, String def) {
        final String[] result = {""};
        final EditDialog editDialog = new EditDialog(this);
        editDialog.setTitle(title);
        editDialog.setYesOnclickListener("确定", new EditDialog.onYesOnclickListener() {
            @Override
            public void onYesClick(String phone) {
                result[0] = phone;
                    editDialog.dismiss();
                    //让软键盘隐藏
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mCellphoneTv.getApplicationWindowToken(), 0);

            }
        });
        editDialog.setNoOnclickListener("取消", new EditDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                editDialog.dismiss();
            }
        });
        editDialog.show();
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
                s = inputDialog("请修改昵称（支持中文/数字/字母/下划线）", UserInfo.nickname);
                if (canMatch(s, "^[\\w_@]+$")) {
                    UserInfo.nickname = s;
                    SettingsUtil.setVal(getApplicationContext(), "nickname", s);
                } else {
                    Toast.makeText(this, "请输入正确的格式", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_username:
                Toast.makeText(this, "不允许修改用户名", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_password:
                s = inputDialog("请输入旧密码", "");
                if (!s.equals(UserInfo.password)) {
                    Toast.makeText(this, "旧密码错误", Toast.LENGTH_SHORT).show();
                    break;
                }
                s = inputDialog("请修改密码（支持中文/数字/字母/下划线）", UserInfo.password);
                if (canMatch(s, "^[\\w_@]+$")) {
                    UserInfo.password = s;
                    SettingsUtil.setVal(getApplicationContext(), "password", s);
                } else if (!"".equals(s)) {
                    Toast.makeText(this, "请输入正确的格式", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_signature:
                s = inputDialog("请修改签名（不包含特殊字符）", UserInfo.signature);
                if (canMatch(s, "^[\n\t']+$")) {
                    UserInfo.signature = s;
                    SettingsUtil.setVal(getApplicationContext(), "signature", s);
                } else if (!"".equals(s)) {
                    Toast.makeText(this, "请输入正确的格式", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_cellphone:
                s = inputDialog("请修改手机号", UserInfo.cellphone);
                if (canMatch(s, "^(\\+\\d{1,3})?[\\d]{11}$")) {
                    UserInfo.cellphone = s;
                    SettingsUtil.setVal(getApplicationContext(), "cellphone", s);
                } else if (!"".equals(s)) {
                    Toast.makeText(this, "请输入正确的格式", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_signout:// TODO 18/12/18
                UserInfo.logined = false;
                UserInfo.userID = "";
                SettingsUtil.setVal(getApplicationContext(), "userID", "");
                Toast.makeText(this, "您已成功退出登录", Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                break;
        }
    }
}
