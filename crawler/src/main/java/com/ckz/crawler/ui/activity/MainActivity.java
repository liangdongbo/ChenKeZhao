package com.ckz.crawler.ui.activity;

import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.ckz.crawler.R;
import com.ckz.crawler.ui.fragment.FindFragment;
import com.ckz.crawler.ui.fragment.HomeFragment;
import com.ckz.crawler.ui.fragment.MyFragment;
import com.ckz.crawler.ui.fragment.TopicFragment;


public class MainActivity extends AppCompatActivity implements FindFragment.OnFragmentInteractionListener {

    //主页面按钮
    private ImageButton btn_main_home;
    private ImageButton btn_main_topic;
    private ImageButton btn_main_find;
    private ImageButton btn_main_my;

    private HomeFragment homeFragment;//首页碎片
    private TopicFragment topicFragment;//话题碎片
    private FindFragment findFragment;//发现碎片
    private MyFragment myFragment;//我的碎片

    private Resources res;//资源对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        setDefaultFragment();

        /*
        //动态管理fragment
        if(findViewById(R.id.main_container)!=null){
            if (savedInstanceState != null) {
                return;
            }

            //创建一个fragment
            MainFragment mainFragment = new MainFragment();
            mainFragment.setArguments(getIntent().getExtras());

            //添加这个mainFragment到'fragment_container'LayoutFragment
            //add这个fragment
            //commit 提交这个fragment到当前的activity
            getSupportFragmentManager().beginTransaction().add(R.id.main_container, mainFragment).commit();


        }*/
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
     * 找到所以的View资源句柄
     */
    private void findAllView(){
        btn_main_home = (ImageButton) findViewById(R.id.btn_main_home);
        btn_main_topic = (ImageButton) findViewById(R.id.btn_main_topic);
        btn_main_find = (ImageButton) findViewById(R.id.btn_main_find);
        btn_main_my = (ImageButton) findViewById(R.id.btn_main_my);
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
     * 按钮单击监听事件处理
     */
    private class BtnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            
        }
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
                    case R.id.btn_main_home ://首页
                        btn_main_home.setBackground(res.getDrawable(R.mipmap.home_tabbar_press));
                        homeFragment = new HomeFragment();
                        transaction.replace(R.id.main_container, homeFragment);//替换当前的碎片
                        break;
                    case R.id.btn_main_topic ://话题
                        btn_main_topic.setBackground(res.getDrawable(R.mipmap.topic_tabbar_press));
                        topicFragment = new TopicFragment();
                        transaction.replace(R.id.main_container, topicFragment);//替换当前的碎片
                        break;
                    case R.id.btn_main_find ://发现
                        btn_main_find.setBackground(res.getDrawable(R.mipmap.discover_tabbar_press));
                        findFragment = new FindFragment();
                        transaction.replace(R.id.main_container,findFragment);//替换当前的碎片
                        break;
                    case R.id.btn_main_my ://我的
                        btn_main_my.setBackground(res.getDrawable(R.mipmap.mine_tabbar_press));
                        myFragment = new MyFragment();
                        transaction.replace(R.id.main_container, myFragment);//替换当前的碎片
                        break;

                }
                transaction.commit();//提交到activity
            }
            
            if(event.getAction()==MotionEvent.ACTION_UP){//弹起
                switch (v.getId()){
                    case R.id.btn_main_home ://首页
                        btn_main_home.setBackground(res.getDrawable(R.mipmap.home_tabbar));
                        break;
                    case R.id.btn_main_topic ://话题
                        btn_main_topic.setBackground(res.getDrawable(R.mipmap.topic_tabbar));
                        break;
                    case R.id.btn_main_find ://发现
                        btn_main_find.setBackground(res.getDrawable(R.mipmap.discover_tabbar));
                        break;
                    case R.id.btn_main_my ://我的
                        btn_main_my.setBackground(res.getDrawable(R.mipmap.mine_tabbar));
                        break;

                }
            }
            return false;
        }
    }





    @Override
    public void onFragmentInteraction(Uri uri) {

    }






}
