package com.ckz.crawler.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.ckz.crawler.R;
import com.ckz.crawler.ui.fragment.MainFragment;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


        }
    }
}
