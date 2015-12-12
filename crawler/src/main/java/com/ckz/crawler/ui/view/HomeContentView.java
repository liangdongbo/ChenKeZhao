package com.ckz.crawler.ui.view;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ckz.crawler.R;
import com.ckz.crawler.ui.activity.SpiderContentShowActivity;
import com.ckz.crawler.utils.NumberToDrawable;
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
    private PullToRefreshListView mPullRefreshListView;//实现下拉刷新的组件
    private SpiderUtils spiderUtils;//爬虫工具包
    private String url;//爬虫网址
    private String xh_url;
    private int category;
    private LinearLayout fl_content;
    private View content_view;//pageView内容
    private int pageNo=0;//页码
    private FloatingActionButton fab;//浮动页码

    /**
     * 创建下拉刷新ListView对象，并赋予相关处理事件
     * @param v 要将ListView装载到那个你节点的View对象
     * @param context 上下文
     * @return PullToRefreshListView
     */
    public LinearLayout getView(View v,FloatingActionButton fab, final FragmentActivity context,String url,String xh_url, final String tip,int category){
        String tempUrl = url;
        //初始化参数
        this.context = context;
        this.url = url;
        this.xh_url = xh_url;
        this.category = category;
        this.content_view = v;
        this.fab = fab;

        spiderUtils = new SpiderUtils();
        fl_content = (LinearLayout) v.findViewById(R.id.content_item);



        //定义刷新控件
        mPullRefreshListView = (PullToRefreshListView) v.findViewById(R.id.pull_refresh_list);
        /*
         * Mode.BOTH：同时支持上拉下拉
         * Mode.PULL_FROM_START：只支持下拉Pulling Down
         * Mode.PULL_FROM_END：只支持上拉Pulling Up
         */
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

        // 下拉加载更多时的提示文本设置
        mPullRefreshListView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel("下拉刷新");
        mPullRefreshListView.getLoadingLayoutProxy(true, false).setPullLabel(tip);
        mPullRefreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel("玩命刷新中...");
        mPullRefreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel("放开以刷新");
        // 上拉加载更多时的提示文本设置
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setLastUpdatedLabel("上拉加载");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("《陈科肇》");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("玩命加载中...");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以加载");


        // Set a listener to be invoked when the list should be refreshed.监听上拉、下拉事件
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            //下拉
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                /*String label = DateUtils.formatDateTime(context, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);*/
                // Update the LastUpdatedLabel
                //refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(tip);
                // Do work to refresh the list here.
                new PullDownGetDataTask().execute();
            }
            //上拉
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(tip);
                new PullUpGetDataTask().execute();
            }
        });

        // Add an end-of-list listener，下拉到列表的最低部时
        mPullRefreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                //Toast.makeText(context, "哎哟，被你看光了", Toast.LENGTH_SHORT).show();
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
     * 下拉-异步任务执行，获取数据
     */
    private class PullDownGetDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //请求并显示数据块
            pageNo--;
            if(pageNo<0){
                pageNo=0;
            }
            fab.setImageDrawable(new NumberToDrawable(context).getDrawable(pageNo+1));

            String tempUrl = "";
            if(category==0 && pageNo==0){
                tempUrl = xh_url;
            }else {
                if(url.indexOf("pn=")!=-1){
                    tempUrl = url.substring(0,url.lastIndexOf("=")+1)+pageNo;
                }
                if(url.indexOf("page=")!=-1){
                    tempUrl = url.substring(0,url.lastIndexOf("=")+1) + (pageNo+1);

                }
            }
            spiderUtils.getPullData(content_view, tempUrl, mPullRefreshListView, context, category);
        }
    }

    /**
     * 上拉-异步任务执行，获取数据
     */
    private class PullUpGetDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //请求并显示数据块
            pageNo++;
            fab.setImageDrawable(new NumberToDrawable(context).getDrawable(pageNo+1));

            String tempUrl = "";
            if(category==0 && pageNo==0){
                tempUrl = xh_url;
            }else {
                if(url.indexOf("pn=")!=-1){
                    tempUrl = url.substring(0,url.lastIndexOf("=")+1)+pageNo;
                }
                if(url.indexOf("page=")!=-1){
                    tempUrl = url.substring(0,url.lastIndexOf("=")+1) + (pageNo+1);

                }
            }
            spiderUtils.getPullData(content_view, tempUrl, mPullRefreshListView, context, category);
        }
    }

    /**
     * 执行下拉刷新
     */
    public void doDataTask(){
        new PullDownGetDataTask().execute();//默认打开时加载
        mPullRefreshListView.setRefreshing(false);
    }
}
