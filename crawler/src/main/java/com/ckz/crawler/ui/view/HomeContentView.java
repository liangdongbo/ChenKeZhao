package com.ckz.crawler.ui.view;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ckz.crawler.R;
import com.ckz.crawler.ui.activity.SpiderContentShowActivity;
import com.ckz.crawler.utils.SpiderUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by win7 on 2015/11/16.
 * 首页的内容视图
 */
public class HomeContentView {

    private static final String TAG = HomeContentView.class.getSimpleName();
    private Context context;
    private PullToRefreshListView mPullRefreshListView;//实现刷新的组件
    private SpiderUtils spiderUtils;
    private String url;//爬虫网址
    private int category;
    private LinearLayout fl_content;
    private View content_view;//pageView内容

    /**
     * 创建下拉刷新ListView对象，并赋予相关处理事件
     * @param v 要将ListView装载到那个你节点的View对象
     * @param context 上下文
     * @return PullToRefreshListView
     */
    public LinearLayout getView(View v, final FragmentActivity context,String url, final String tip,int category){
        //初始化参数
        this.context = context;
        this.url = url;
        this.category = category;
        this.content_view = v;

        spiderUtils = new SpiderUtils();
        fl_content = (LinearLayout) v.findViewById(R.id.content_item);
        mPullRefreshListView = (PullToRefreshListView) v.findViewById(R.id.pull_refresh_list);

        // Set a listener to be invoked when the list should be refreshed.监听下拉事件
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                /*String label = DateUtils.formatDateTime(context, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);*/
                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(tip);
                // Do work to refresh the list here.
                new GetDataTask().execute();
            }
        });

        // Add an end-of-list listener，下拉到列表的最低部时
        mPullRefreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                Toast.makeText(context, "哎哟，被你看光了", Toast.LENGTH_SHORT).show();
            }
        });

        //列表项点击事件
        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //View v = View.inflate(context,R.layout.activity_activity_adapter_item,null);
                TextView tv_link = (TextView)view.findViewById(R.id.spider_item_link);
                String link = (String) tv_link.getText();

                //调用WebView的Activity
                Intent intent = new Intent(context,SpiderContentShowActivity.class);
                Bundle bundle  = new Bundle ();
                bundle.putString("link",link);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        return fl_content;
    }

    /**
     * 执行
     */
    private class GetDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //请求并显示数据块
            spiderUtils.getSpiderItem(content_view, url, mPullRefreshListView, context, category);
        }
    }

    /**
     * 执行下拉刷新
     */
    public void doDataTask(){
        new GetDataTask().execute();//默认打开时加载
        mPullRefreshListView.setRefreshing(false);
    }
}
