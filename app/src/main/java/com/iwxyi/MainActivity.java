package com.iwxyi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iwxyi.BillsFragment.BillsFragment;
import com.iwxyi.BillsFragment.BlankDataFragment;
import com.iwxyi.BillsFragment.DummyContent;
import com.iwxyi.RecordActivity.RecordActivity;
import com.iwxyi.Utils.FileUtil;
import com.iwxyi.Utils.SettingsUtil;
import com.iwxyi.Utils.SqlUtil;
import com.iwxyi.Utils.UserInfo;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BlankDataFragment.OnBlankFragmentInteractionListener,
                BillsFragment.OnListFragmentInteractionListener, PlusOneButtonFragment.OnPlusOneButtonFragmentInteractionListener,
                ExportFragment.OnExportFragmentInteractionListener{

    private final int REQUEST_CODE_RECORD = 1;
    private final int REQUEST_CODE_MODIFY = 2;
    private final int REQUEST_CODE_LOGIN  = 3;
    private final int REQUEST_CODE_PERSON = 4;
    private final int RESULT_CODE_RECORD_OK = 101;
    private final int RESULT_CODE_MODIFY_OK = 102;
    private final int RESULT_CODE_LOGIN_OK  = 103;
    private final int RESULT_CODE_PERSON_OK = 104;
    private int columns = 1; // 实现列表多列形式

    private static int currentFragmentIndex = 0;

    private Fragment currentFragment = new Fragment();
    //private BlankDataFragment blankDataFragment = BlankDataFragment.newInstance();
    //private com.iwxyi.BillsFragment.BillsFragment billsFragment = com.iwxyi.BillsFragment.BillsFragment.newInstance(columns);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initData() {
        FileUtil.ensureFolder();

        readUserInfo();

        if (SettingsUtil.getLong(this, "firstStartTime") == 0) {
            long timestamp = System.currentTimeMillis();
            SettingsUtil.setVal(this, "firstStartTime", timestamp);
        }

        SqlUtil.init(getApplicationContext());
    }

    private void readUserInfo() {
        // 修改抽屉头像
        NavigationView navigationView = findViewById(R.id.nav_view);
        View drawView = navigationView.getHeaderView(0);
        ImageView mHeadIv = (ImageView)drawView.findViewById(R.id.iv_avatar);
        TextView mNickTv = (TextView)drawView.findViewById(R.id.tv_nickname);
        TextView mSignTv = (TextView)drawView.findViewById(R.id.tv_signature);

        if (!"".equals(SettingsUtil.getVal(getApplicationContext(), "userID"))) {
            UserInfo.userID = SettingsUtil.getVal(getApplicationContext(), "userID");
            UserInfo.username = SettingsUtil.getVal(getApplicationContext(), "username");
            UserInfo.password = SettingsUtil.getVal(getApplicationContext(), "password");
            UserInfo.nickname = SettingsUtil.getVal(getApplicationContext(), "nickname");
            UserInfo.signature = SettingsUtil.getVal(getApplicationContext(), "signature");
            UserInfo.cellphone = SettingsUtil.getVal(getApplicationContext(), "cellphone");
            UserInfo.logined = true;

            if ("".equals(UserInfo.nickname))
                mNickTv.setText(UserInfo.username);
            else
                mNickTv.setText(UserInfo.nickname);
            if ("".equals(UserInfo.signature))
                mSignTv.setText("点击头像进行同步数据和修改信息");
            else
                mSignTv.setText(UserInfo.signature);
        } else {
            UserInfo.userID = "";
            UserInfo.username = "";
            UserInfo.password = "";
            UserInfo.signature = "";
            UserInfo.logined = false;
            UserInfo.isNew = false;

            mNickTv.setText("未登录");
            mSignTv.setText("你就是个小穷光蛋呐");
        }

    }

    private void initView() {
        // 初始化 工具栏
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 初始化 悬浮按钮
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "抱歉，暂未开发", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                startActivityForResult(new Intent(getApplicationContext(), RecordActivity.class), REQUEST_CODE_RECORD);
            }
        });

        // 初始化 抽屉
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // 抽屉菜单事件
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // 抽屉头像事件
        //View drawView = navigationView.inflateHeaderView(R.layout.nav_header_main); // 用这句会重复头像
        View drawView = navigationView.getHeaderView(0);
        ImageView mHeadIv = (ImageView)drawView.findViewById(R.id.iv_avatar);
        mHeadIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserInfo.logined) { // 用户已经登录，切换到用户信息界面
                    startActivityForResult(new Intent(getApplicationContext(), PersonActivity.class), REQUEST_CODE_PERSON);
                } else {
                    startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), REQUEST_CODE_LOGIN);
                }
            }
        });

        // 抽屉滑动事件
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //抽屉滑动时回调
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //抽屉打开后回调
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                //抽屉关闭后回调
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                //抽屉滑动状态改变时回调
                switch (newState) {
                    case DrawerLayout.STATE_DRAGGING:
                        //拖动状态
                        break;
                    case DrawerLayout.STATE_IDLE:
                        //静止状态
                        break;
                    case DrawerLayout.STATE_SETTLING:
                        //设置状态
                        break;
                    default:
                        break;
                }
            }
        });

        // 初始化碎片
        columns = SettingsUtil.getInt(getApplicationContext(), "columns");
        if (columns < 1) columns = 1; // 读取保存的列数
        /*FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.frameLayout, BlankDataFragment.newInstance());
        ft.add(R.id.frameLayout, com.iwxyi.BillsFragment.BillsFragment.newInstance(columns));
        ft.add(R.id.frameLayout, PlusOneButtonFragment.newInstance("", ""));
        ft.add(R.id.frameLayout, ExportFragment.newInstance("", ""));
        ft.hide(com.iwxyi.BillsFragment.BillsFragment.newInstance(columns));
        ft.hide(PlusOneButtonFragment.newInstance("",""));
        ft.hide(ExportFragment.newInstance("",""));
        ft.commit();*/

        switchFragment(BillsFragment.newInstance(1));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_history) {
            switchFragment(BillsFragment.newInstance(1));
            currentFragmentIndex = 1;
            if (DummyContent.ITEMS.size() == 0) {
                switchFragment(BlankDataFragment.newInstance());
                currentFragmentIndex = 0;
            }
        } else if (id == R.id.nav_balance) {
            currentFragmentIndex = 2;
        } else if (id == R.id.nav_future) {
            currentFragmentIndex = 3;
        } else if (id == R.id.nav_export) {
            switchFragment(ExportFragment.newInstance("", ""));
            currentFragmentIndex = 4;
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void switchFragment(android.support.v4.app.Fragment fragment) {

        /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!fragment.isAdded()) {
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.frameLayout, fragment, fragment.getClass().getName());
        } else {
            transaction.hide(currentFragment)
                    .show(fragment);
        }
        currentFragment = fragment;
        transaction.commitAllowingStateLoss();*/

        // 隐藏现有的Fragment避免显示重叠
        for (android.support.v4.app.Fragment frag : getSupportFragmentManager().getFragments())
            getSupportFragmentManager().beginTransaction().hide(frag).commitAllowingStateLoss();// commit 会导致错误
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment, "history").commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CODE_RECORD_OK) { // 添加账单结束
            //switchFragment(billsFragment);
            if (currentFragmentIndex != 1)
                switchFragment(BillsFragment.newInstance(columns));
        } else if (resultCode == RESULT_CODE_MODIFY_OK) { // 修改账单。与添加唯一不同的是保留滚动位置
            if (currentFragmentIndex != 1)
                switchFragment(BillsFragment.newInstance(columns));
        } else if (resultCode == RESULT_CODE_LOGIN_OK) {
            readUserInfo();
        } else if (resultCode == RESULT_CODE_PERSON_OK) {
            readUserInfo();
        }
    }

    /**
     * 账单历史被单击
     * @param item
     */
    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        String id = item.id;
        Intent intent = new Intent(getApplicationContext(), RecordActivity.class);
        intent.putExtra("billID", id);
        startActivityForResult(intent, REQUEST_CODE_MODIFY);
    }

    @Override
    public void onBlankTvClicked(Uri uri) {
        startActivityForResult(new Intent(getApplicationContext(), RecordActivity.class), REQUEST_CODE_RECORD);
    }

    @Override
    public void onPlusOneFragmentInteraction(Uri uri) {
        Toast.makeText(this, "+1", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onExportFragmentInteraction(Uri uri) {

    }
}
