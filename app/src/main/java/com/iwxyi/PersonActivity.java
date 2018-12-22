package com.iwxyi;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iwxyi.BillsFragment.DummyContent;
import com.iwxyi.Utils.FileUtil;
import com.iwxyi.Utils.Global;
import com.iwxyi.Utils.SettingsUtil;
import com.iwxyi.Utils.StreamUtil;
import com.iwxyi.Utils.StringUtil;
import com.iwxyi.Utils.UserInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PersonActivity extends AppCompatActivity implements View.OnClickListener {

    private final int RESULT_CODE_PERSON_OK = 104;
    private final int SYNC_RESULT = 1;

    private TextView mNicknameTv;
    private TextView mUsernameTv;
    private TextView mPasswordTv;
    private TextView mSignatureTv;
    private TextView mCellphoneTv;
    private Button mSignoutBtn;
    private TextView mUsedDayTv;
    private TextView mRecordCountTv;
    private FloatingActionButton mFab;

    /**
     * 创建完毕
     * @param savedInstanceState
     */
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
                syncAll();
            }
        });

        initView();
        readUserInfo();
    }

    /**
     * 读取用户设置
     */
    private void readUserInfo() {
        mNicknameTv.setText("昵　称：" + UserInfo.nickname);
        mUsernameTv.setText("用户名：" + UserInfo.username);
        mPasswordTv.setText("密　码：" + "********");
        mSignatureTv.setText("签　名：" + UserInfo.signature);
        mCellphoneTv.setText("手机号：" + UserInfo.cellphone);

        int day = 1;
        long first = SettingsUtil.getLong(this, "firstStartTime");
        if (first > 0) {
            long t = System.currentTimeMillis();
            long delta = t - first;
            double res = delta / 1000 / 60 / 60 / 24;
            day = (int) (res + 1);
        }
        mUsedDayTv.setText("您已经使用了 " + day + "天");

        mRecordCountTv.setText("共有账单记录 " + DummyContent.ITEMS.size() + " 条");
    }

    /**
     * 初始化控件
     */
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
        mUsedDayTv = (TextView) findViewById(R.id.tv_usedDay);
        mUsedDayTv.setOnClickListener(this);
        mRecordCountTv = (TextView) findViewById(R.id.tv_recordCount);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);
    }

    /**
     * 弹出输入框
     * @param aim 目标
     * @param title 标题
     * @param def 默认值
     * @return 输入的字符串
     */
    private String inputDialog(final String aim, String title, String def) {
        final String[] result = new String[1];
        LayoutInflater factory = LayoutInflater.from(PersonActivity.this);//提示框
        final View view = factory.inflate(R.layout.editbox, null);//这里必须是final的
        final EditText edit = (EditText) view.findViewById(R.id.editText);//获得输入框对象
        edit.setText(def);
        new AlertDialog.Builder(PersonActivity.this)
                .setTitle(title)//提示框标题
                .setView(view)
                .setPositiveButton("确定",//提示框的两个按钮
                        new DialogInterface.OnClickListener() {
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

    /**
     * 淡出输入框2（没用了）
     * @param title  标题
     * @param def 默认值
     * @return 输入的字符串
     */
    private String inputDialog2(String title, String def) {
        final EditText et = new EditText(this);
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

    /**
     * 能否匹配正则表达式，用来判断输入的结构是否正确
     * @param string  字符串
     * @param pattern 正则表达式格式
     * @return 能否匹配
     */
    private boolean canMatch(String string, String pattern) {
        if ("".equals(string)) return false;
        return Pattern.matches(pattern, string);
    }

    /**
     * 控件单击事件
     * @param v
     */
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
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("是否退出？")
                        .setContentText("您将退出当前账号，在下次登录之前无法从云端恢复（数据库备份不受影响）")
                        .setConfirmText("确认退出")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                UserInfo.logined = false;
                                UserInfo.userID = "";
                                SettingsUtil.setVal(getApplicationContext(), "userID", "");
                                Toast.makeText(getApplicationContext(), "您已成功退出登录", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_CODE_PERSON_OK);
                                finish();
                            }
                        })
                        .show();
                break;
            case R.id.fab:
                syncAll();
                break;
            default:
                break;
        }
    }

    /**
     * 输入框结束事件
     * @param aim 目标
     * @param s   字符串
     */
    private void onInputDialog(String aim, String s) {
        if (aim.equals("nickname")) {
            if (canMatch(s, "^[\\w_@]+$")) {
                mNicknameTv.setText("昵　称：" + s);
                UserInfo.nickname = s;
                SettingsUtil.setVal(getApplicationContext(), "nickname", s);
                setResult(RESULT_CODE_PERSON_OK);
                this.setTitle(s);
                updateContent("nickname", s);
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
                updateContent("password", s);
            } else if (!"".equals(s)) {
                Toast.makeText(this, "请输入正确的格式", Toast.LENGTH_SHORT).show();
            }
        } else if (aim.equals("signature")) {
            if (canMatch(s, "^[^\n\t']+$")) {
                mSignatureTv.setText("签　名：" + s);
                UserInfo.signature = s;
                SettingsUtil.setVal(getApplicationContext(), "signature", s);
                setResult(RESULT_CODE_PERSON_OK);
                updateContent("signature", s);
            } else if (!"".equals(s)) {
                Toast.makeText(this, "请输入正确的格式", Toast.LENGTH_SHORT).show();
            }
        } else if (aim.equals("cellphone")) {
            if (canMatch(s, "^(\\+\\d{1,3})?[\\d]{11}$")) {
                mCellphoneTv.setText("手机号：" + s);
                UserInfo.cellphone = s;
                SettingsUtil.setVal(getApplicationContext(), "cellphone", s);
                setResult(RESULT_CODE_PERSON_OK);
                updateContent("cellphone", s);
            } else if (!"".equals(s)) {
                Toast.makeText(this, "请输入正确的格式", Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * 多线程返回处理函数
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == SYNC_RESULT) {
                String s = (String) msg.obj;
                //Toast.makeText(PersonActivity.this, reason, Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(R.id.fab), s, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    };

    /**
     * 上传用户信息
     * @param field 文件名
     * @param val   数值
     */
    private void updateContent(final String field, final String val) {
        new Thread() {
            public void run() {
                String path = Global.URL_DOMAIN + "uploadcontent.php";
                path += "?userID=" + UserInfo.userID + "&password=" + UserInfo.password;
                path += "&" + field + "=" + val;
                URL url = null;
                try {
                    url = new URL(path);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setConnectTimeout(5000);
                    int code = urlConnection.getResponseCode();
                    if (code == 200) {
                        InputStream in = urlConnection.getInputStream();
                        String content = StreamUtil.readStream(in);
                        Message msg = Message.obtain();
                        msg.what = SYNC_RESULT;
                        if (!"OK".equals(StringUtil.getXml(content, "STATE"))) {
                            msg.obj = StringUtil.getXml(content, "REASON");
                        } else {
                            msg.obj = "上传成功";
                        }
                        handler.sendMessage(msg);
                    } else {
                        sleep(4000);
                        Message msg = Message.obtain();
                        msg.what = SYNC_RESULT;
                        msg.obj = "上传结束";
                        handler.sendMessage(msg);
                    }
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 同步所有内容（仅上传）
     */
    private void syncAll() {
        new Thread() {
            public void run() {
                String path = Global.URL_DOMAIN + "uploadcontent.php";
                path += "?userID=" + UserInfo.userID + "&password=" + UserInfo.password;

                path += "&bills=" + URLEncoder.encode(FileUtil.readTextVals("bills.txt"));
                path += "&cards=" + URLEncoder.encode(FileUtil.readTextVals("cards.txt"));
                path += "&kinds_spending=" + URLEncoder.encode(FileUtil.readTextVals("kinds_spending.txt"));
                path += "&kinds_income=" + URLEncoder.encode(FileUtil.readTextVals("kinds_income.txt"));
                path += "&kinds_borrowing=" + URLEncoder.encode(FileUtil.readTextVals("kinds_borrowing.txt"));
                Log.i("====sync", path);
                URL url = null;
                try {
                    url = new URL(path);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setConnectTimeout(5000);
                    int code = urlConnection.getResponseCode();
                    if (code == 200) {
                        InputStream in = urlConnection.getInputStream();
                        String content = StreamUtil.readStream(in);
                        Message msg = Message.obtain();
                        msg.what = SYNC_RESULT;
                        if (!"OK".equals(StringUtil.getXml(content, "STATE"))) {
                            msg.obj = StringUtil.getXml(content, "REASON");
                        } else {
                            msg.obj = "上传成功";
                        }
                        handler.sendMessage(msg);
                    }
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
