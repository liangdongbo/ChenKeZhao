package com.ckz.crawler.ui.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ckz.crawler.R;
import com.ckz.crawler.ui.fragment.FindFragment;
import com.ckz.crawler.ui.fragment.HomeFragment;
import com.ckz.crawler.ui.fragment.MyFragment;
import com.ckz.crawler.ui.fragment.TopicFragment;
import com.ckz.crawler.utils.ShareUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.SimpleFormatter;


public class MainActivity extends AppCompatActivity implements FindFragment.OnFragmentInteractionListener,NavigationView.OnNavigationItemSelectedListener{

    private static final int REQUEST_CODE_CAMERA = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    //主页面按钮
    private LinearLayout btn_main_home;
    private LinearLayout btn_main_topic;
    private LinearLayout btn_main_find;
    private LinearLayout btn_main_my;
    //按钮图标
    private ImageView icon_btn_main_home;
    private ImageView icon_btn_main_topic;
    private ImageView icon_btn_main_find;
    private ImageView icon_btn_main_my;

    private HomeFragment homeFragment;//首页碎片
    private TopicFragment topicFragment;//话题碎片
    private FindFragment findFragment;//发现碎片
    private MyFragment myFragment;//我的碎片

    private Resources res;//资源对象

    private DrawerLayout drawer;//抽屉布局

    private ShareUtils shareUtils;//分享工具包

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setDefaultFragment();
        initDrawer();
        shareUtils = new ShareUtils(MainActivity.this,getResources());
    }

    /**
     * 设置默认的Fragment
     */
    private void setDefaultFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();//开启事务
        homeFragment = new HomeFragment();
        transaction.replace(R.id.main_container, homeFragment);//替换当前的碎片
        transaction.commit();//提交到activity
        icon_btn_main_home.setBackground(res.getDrawable(R.mipmap.home_tabbar_press));

    }

    /**
     * 初始化
     */
    private void init(){
        res = getResources();
        findAllView();//查找资源
        registerAllBtnListener();//按钮资源事件注册
    }

    /**
     * 初始化抽屉
     */
    private void initDrawer(){
        /*获取抽屉布局句柄*/
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        /*工具条*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*抽屉布局的开关句柄*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        /*导航视图*/
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * 找到所以的View资源句柄
     */
    private void findAllView(){
        btn_main_home = (LinearLayout) findViewById(R.id.btn_main_home);
        btn_main_topic = (LinearLayout) findViewById(R.id.btn_main_topic);
        btn_main_find = (LinearLayout) findViewById(R.id.btn_main_find);
        btn_main_my = (LinearLayout) findViewById(R.id.btn_main_my);
        icon_btn_main_home = (ImageView) findViewById(R.id.icon_btn_main_home);
        icon_btn_main_topic = (ImageView) findViewById(R.id.icon_btn_main_topic);
        icon_btn_main_find = (ImageView) findViewById(R.id.icon_btn_main_find);
        icon_btn_main_my = (ImageView) findViewById(R.id.icon_btn_main_my);
    }

    /**
     * 注册所有的按钮监听事件
     */
    private void registerAllBtnListener(){
        btn_main_home.setOnTouchListener(new BtnTouchListener());
        btn_main_topic.setOnTouchListener(new BtnTouchListener());
        btn_main_find.setOnTouchListener(new BtnTouchListener());
        btn_main_my.setOnTouchListener(new BtnTouchListener());
    }

    /**
     * 按钮的触摸事件
     */
    private class BtnTouchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            
            if(event.getAction()==MotionEvent.ACTION_DOWN){//按下
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();//开启事务
                switch (v.getId()){
                    case R.id.btn_main_home://首页
                        icon_btn_main_home.setBackground(res.getDrawable(R.mipmap.home_tabbar_press));
                        homeFragment = new HomeFragment();
                        transaction.replace(R.id.main_container, homeFragment);//替换当前的碎片
                        break;
                    case R.id.btn_main_topic ://话题
                        icon_btn_main_topic.setBackground(res.getDrawable(R.mipmap.topic_tabbar_press));
                        topicFragment = new TopicFragment();
                        transaction.replace(R.id.main_container, topicFragment);//替换当前的碎片
                        break;
                    case R.id.btn_main_find ://发现
                        icon_btn_main_find.setBackground(res.getDrawable(R.mipmap.discover_tabbar_press));
                        findFragment = new FindFragment();
                        transaction.replace(R.id.main_container,findFragment);//替换当前的碎片
                        break;
                    case R.id.btn_main_my ://我的
                        icon_btn_main_my.setBackground(res.getDrawable(R.mipmap.mine_tabbar_press));
                        myFragment = new MyFragment();
                        transaction.replace(R.id.main_container, myFragment);//替换当前的碎片
                        break;

                }
                transaction.commit();//提交到activity
            }
            
            if(event.getAction()==MotionEvent.ACTION_UP){//弹起
                icon_btn_main_home.setBackground(res.getDrawable(R.mipmap.home_tabbar));
                icon_btn_main_topic.setBackground(res.getDrawable(R.mipmap.topic_tabbar));
                icon_btn_main_find.setBackground(res.getDrawable(R.mipmap.discover_tabbar));
                icon_btn_main_my.setBackground(res.getDrawable(R.mipmap.mine_tabbar));
                switch (v.getId()){
                    case R.id.btn_main_home ://首页
                        icon_btn_main_home.setBackground(res.getDrawable(R.mipmap.home_tabbar_press));
                        break;
                    case R.id.btn_main_topic ://话题
                        icon_btn_main_topic.setBackground(res.getDrawable(R.mipmap.topic_tabbar_press));
                        break;
                    case R.id.btn_main_find ://发现
                        icon_btn_main_find.setBackground(res.getDrawable(R.mipmap.discover_tabbar_press));
                        break;
                    case R.id.btn_main_my ://我的
                        icon_btn_main_my.setBackground(res.getDrawable(R.mipmap.mine_tabbar_press));
                        break;

                }
            }
            return false;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * 压背景的时候，处理事件
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 导航视图的菜单项被选时，处理相关事件
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {//相机
            // Handle the camera action
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent, REQUEST_CODE_CAMERA);
        } else if (id == R.id.nav_gallery) {//图库
            Intent intent = new Intent(MainActivity.this,GalleryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {//视频

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {//分享
            //分享ImageView组件单张图片
          /*  View  v = View.inflate(this,R.layout.nav_header_main,null);
            ImageView siv = (ImageView) v.findViewById(R.id.imageView);
            shareUtils.shareImageViewModule(siv);*/
            shareUtils.shareMyInformation();
        } else if (id == R.id.nav_send) {//发送文字
            shareUtils.sendText("陈科肇\n18575612426@163.com");
        }
        //关闭抽屉
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.i("TestFile",
                        "SD card is not avaiable/writeable right now.");
                return;
            }
            String name = new DateFormat().format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

            FileOutputStream b = null;

            String path = Environment.getExternalStorageDirectory().getPath()+"/chenkezhao/";
            File file=new File(path);
            if(!file.exists()){
                file.mkdir();
            }
            File f = new File(file,name);
            try {
                b = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //广播，通知图库刷新指定图片
            Uri localUri = Uri.fromFile(f);
            Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
            sendBroadcast(localIntent);

            //提示消息
            final Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(R.id.main_view), "照片保存成功", Snackbar.LENGTH_LONG);
            snackbar.setAction("关闭", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();

        }
    }
}
