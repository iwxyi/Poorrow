package com.iwxyi;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.iwxyi.Fragments.BillsList.BillsFragment;
import com.iwxyi.Fragments.BillsList.DummyContent;
import com.iwxyi.Fragments.BlankDataFragment;
import com.iwxyi.Record.RecordActivity;
import com.iwxyi.Utils.FileUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BlankDataFragment.OnFragmentInteractionListener,
                BillsFragment.OnListFragmentInteractionListener{

    private final int REQUEST_CODE_RECORD = 1;
    private final int REQUEST_CODE_MODIFY = 2;
    private final int RESULT_CODE_RECORD_OK = 101;
    private final int RESULT_CODE_MODIFY_OK = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        FileUtil.ensureFolder();
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

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // 抽屉响应事件
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
//        switchFragment(BlankDataFragment.newInstance());
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
            if (DummyContent.ITEMS.size() == 0)
                switchFragment(BlankDataFragment.newInstance());
        } else if (id == R.id.nav_balance) {

        } else if (id == R.id.nav_future) {

        } else if (id == R.id.nav_export) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void switchFragment(android.support.v4.app.Fragment fragment) {
        // 隐藏现有的Fragment避免显示重叠
        for (android.support.v4.app.Fragment frag : getSupportFragmentManager().getFragments())
            getSupportFragmentManager().beginTransaction().hide(frag).commitAllowingStateLoss();// commit 会导致错误
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment, "history").commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CODE_RECORD_OK) { // 添加账单结束
            switchFragment(BillsFragment.newInstance(1));
        } else if (resultCode == RESULT_CODE_MODIFY_OK) { // 修改账单。与添加唯一不同的是保留滚动位置
            switchFragment(BillsFragment.newInstance(1));
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
    public void onTvClicked(Uri uri) {
        startActivityForResult(new Intent(getApplicationContext(), RecordActivity.class), REQUEST_CODE_RECORD);
    }
}
