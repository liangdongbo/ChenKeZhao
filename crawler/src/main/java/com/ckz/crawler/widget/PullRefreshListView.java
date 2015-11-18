package com.ckz.crawler.widget;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ckz.crawler.R;
import com.ckz.crawler.ui.activity.SpiderContentShowActivity;
import com.ckz.crawler.utils.SpiderUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.logging.LogRecord;

/**
 * Created by win7 on 2015/11/16.
 * ListView下拉刷新
 */
public class PullRefreshListView {

    private static final String TAG = PullRefreshListView.class.getSimpleName();
    private Context context;
    private PullToRefreshListView mPullRefreshListView;
    private RequestQueue mVolleyQueue;//请求队列
    private SpiderUtils spiderUtils;
    private String url;//爬虫网址
    private int category;
    private FrameLayout fl_content;
    private View content_view;//pageView内容

    /**
     * 创建下拉刷新ListView对象，并赋予相关处理事件
     * @param v 要将ListView装载到那个你节点的View对象
     * @param context 上下文
     * @return PullToRefreshListView
     */
    public FrameLayout getRefreshListView(View v, final FragmentActivity context,String url, final String tip,int category){
        //初始化参数
        this.context = context;
        this.url = url;
        this.category = category;
        this.content_view = v;
        mVolleyQueue = Volley.newRequestQueue(context);//请求队列
        spiderUtils = new SpiderUtils();
        fl_content = (FrameLayout) v.findViewById(R.id.content_item);
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
        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//列表点击事件
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
    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            String[] mString = new String[]{""};
            return mString;
        }

        @Override
        protected void onPostExecute(String[] result) {
            //请求并显示数据块
            spiderUtils.getSpiderItem(content_view,url,mPullRefreshListView,mVolleyQueue,context,category);
            super.onPostExecute(result);
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
