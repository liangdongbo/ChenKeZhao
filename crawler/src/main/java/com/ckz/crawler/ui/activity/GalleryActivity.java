package com.ckz.crawler.ui.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ckz.crawler.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 图库显示
 */
public class GalleryActivity extends AppCompatActivity {

    private GalleryStaggeredAdapter mGalleryStaggeredAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        initCurrentActionBar();
        //获取句柄
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv_gallery_view);

        /*
        设置布局管理器：
        LinearLayoutManager 现行管理器，支持横向、纵向。
        GridLayoutManager 网格布局管理器
        StaggeredGridLayoutManager 瀑布就式布局管理器*/
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,
                StaggeredGridLayoutManager.VERTICAL));
        //设置adapter
        mGalleryStaggeredAdapter =new GalleryStaggeredAdapter(GalleryActivity.this);
        mRecyclerView.setAdapter(mGalleryStaggeredAdapter);
        //设置Item动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        initEvent();
    }

    /**
     * 初始化当前的自定义actionBar
     */
    private void initCurrentActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);//消除v7 actionbar阴影
        // 返回箭头（默认不显示）
        actionBar.setDisplayHomeAsUpEnabled(false);
        // 左侧图标点击事件使能
        actionBar.setHomeButtonEnabled(true);
        // 使左上角图标(系统)是否显示
        actionBar.setDisplayShowHomeEnabled(false);
        // 显示标题
        actionBar.setDisplayShowTitleEnabled(false);
        //显示自定义视图
        actionBar.setDisplayShowCustomEnabled(true);
        View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.actionbar_layout, null);
        actionBar.setCustomView(actionbarLayout);

        ImageButton ib_back_icon = (ImageButton) findViewById(R.id.ib_back_icon);
        ib_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initEvent() {
        mGalleryStaggeredAdapter.setOnItemClickLitener(new GalleryStaggeredAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(GalleryActivity.this,position + " click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //Toast.makeText(GalleryActivity.this,position + " long click", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
