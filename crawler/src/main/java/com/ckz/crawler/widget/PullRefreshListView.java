package com.ckz.crawler.widget;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ckz.crawler.R;
import com.ckz.crawler.utils.SpiderUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

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


    /**
     * 创建下拉刷新ListView对象，并赋予相关处理事件
     * @param v 要将ListView装载到那个你节点的View对象
     * @param context 上下文
     * @return PullToRefreshListView
     */
    public PullToRefreshListView getRefreshListView(View v, final FragmentActivity context,String url, final String tip,int category){
        //初始化参数
        this.context = context;
        this.url = url;
        this.category = category;
        mVolleyQueue = Volley.newRequestQueue(context);//请求队列
        spiderUtils = new SpiderUtils();

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
        return mPullRefreshListView;
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
            spiderUtils.getSpiderItem(url,mPullRefreshListView,mVolleyQueue,context,category);
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
