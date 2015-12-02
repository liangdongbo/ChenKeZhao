package com.ckz.demo.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ckz.demo.R;
import com.ckz.demo.utils.LoadImageUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainSubActivity extends AppCompatActivity {

    private List mListItems;//列表数据集合
    private PullToRefreshListView mPullRefreshListView;//下拉刷新控件
    private ItemAdapter mAdapter;//列表适配器
    private String[] mStrings = {"1", "2", "3", "4"};//列表模拟初始数据
    private ListView lvShow = null;//下拉刷新控件，作用：定位listview位置
    private int y = 0;//记录当前liseView位置
    private ImageButton ib_back_icon;
    private LoadImageUtils loadImageUtils;

    RequestQueue requestQueue = null;//volley 的请求队列

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sub);
        requestQueue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        initCurrentActionBar(title);///初始化当前的自定义actionBar
        loadImageUtils = new LoadImageUtils();


        //刷新列表定义
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list_sub);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);//模式设置

        // 上拉加载更多时的提示文本设置
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setLastUpdatedLabel("上拉加载");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("《陈科肇》");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("玩命加载中...");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以加载");
        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        // 添加 一个下拉刷新事件
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // 得到上一次滚动条的位置，让加载后的页面停在上一次的位置，便于用户操作
                y = mListItems.size();
                new GetBottomDataTask().execute();
            }
        });
        lvShow = mPullRefreshListView.getRefreshableView();
        mListItems = new ArrayList<String>();
        mListItems.addAll(Arrays.asList(mStrings));
        mAdapter = new ItemAdapter();
        lvShow.setAdapter(mAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * 初始化当前的自定义actionBar
     */
    private void initCurrentActionBar(String title){
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
        TextView tv_title = (TextView) actionbarLayout.findViewById(R.id.tv_actionbar_title);
        tv_title.setText(title);
        actionBar.setCustomView(actionbarLayout);
        ib_back_icon = (ImageButton) findViewById(R.id.ib_back_icon);
        ib_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /*
     * 上拉加载更多时的事件处理
    */
    private class GetBottomDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            int length = mListItems.size();
            for (int i = length; i < length + 5; i++) {
                mListItems.add(i);
            }
            mAdapter.notifyDataSetChanged();
            // 停止刷新
            mPullRefreshListView.onRefreshComplete();
            // 设置滚动条的位置 -- 加载更多之后，滚动条的位置应该在上一次划到的位置
            lvShow.setSelectionFromTop(y, 0);
            super.onPostExecute(result);
        }
    }


    /**
     * Created by ckz on 2015/12/1.
     * 适配器
     */
    public class ItemAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mListItems.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = View.inflate(MainSubActivity.this, R.layout.adapter_main_sub, null);
            //异步加载图片
            ImageView imageView = (ImageView) v.findViewById(R.id.ms_item_icon);
            loadImageUtils.loadImageByVolley(requestQueue,imageView, "");
            TextView tv_text = (TextView) v.findViewById(R.id.ms_item_text);
            tv_text.setText("哈哈:" + position);
            return v;
        }
    }

}
