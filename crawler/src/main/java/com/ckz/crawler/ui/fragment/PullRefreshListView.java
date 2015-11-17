package com.ckz.crawler.ui.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ckz.crawler.R;
import com.ckz.crawler.entity.HuXiu;
import com.ckz.crawler.network.MyStringRequest;
import com.ckz.crawler.utils.HtmlParse;
import com.ckz.crawler.utils.SpiderUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
import com.lynnchurch.horizontalscrollmenu.BaseAdapter;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by win7 on 2015/11/16.
 * 下拉刷新，返回ListView 封装类
 */
public class PullRefreshListView {

    private static final String TAG = PullRefreshListView.class.getSimpleName();
    private Context context;

    //listView下拉刷新
    private LinkedList<String> mListItems;
    private PullToRefreshListView mPullRefreshListView;
    private ArrayAdapter<String> mAdapter;
    private String[] mStrings = { "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
            "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
            "Allgauer Emmentaler", "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
            "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
            "Allgauer Emmentaler" };

    private List<HuXiu> huxius;
    RequestQueue mVolleyQueue;//请求队列


    /**
     * 返回下拉刷新后的ListView对象
     * @param v 要将ListView装载到那个你节点的View对象
     * @param context 上下文
     * @return
     */
    public PullToRefreshListView getRefreshListView(View v, final FragmentActivity context){
        this.context = context;
        mVolleyQueue = Volley.newRequestQueue(context);//请求队列
        //listView下拉刷新
        mPullRefreshListView = (PullToRefreshListView) v.findViewById(R.id.pull_refresh_list);
        // Set a listener to be invoked when the list should be refreshed.监听下拉事件
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(context, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                // Do work to refresh the list here.
                new GetDataTask().execute();
            }
        });
        // Add an end-of-list listener，下拉到列表的最低部时
        mPullRefreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                Toast.makeText(context, "没有啦...", Toast.LENGTH_SHORT).show();
            }
        });
        return mPullRefreshListView;
    }

    /**
     * 下拉，获取数据
     */
    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            return mStrings;
        }

        @Override
        protected void onPostExecute(String[] result) {


            //使用volley发送StringRequest请求--------------------开始
            String url = "http://www.huxiu.com/brief.html";
            MyStringRequest stringRequest = new MyStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response){//请求成功
                    byte[] b = response.getBytes(Charset.forName("utf-8"));
                    String html = new String(b);
                    huxius = new HtmlParse().getHuXiuList(html);
                    if(mListItems==null){
                        mListItems = new LinkedList<String>();
                    }
                    for (HuXiu huxiu :huxius){
                        mListItems.addFirst(huxiu.getTitle());
                    }

                    ListView actualListView = mPullRefreshListView.getRefreshableView();
                    //mListItems.addAll(Arrays.asList(mStrings));
                    //mAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, mListItems);

                    ItemAdapter mAdapter = new ItemAdapter();

                    //Add Sound Event Listener
                    SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(context);
                    soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
                    soundListener.addSoundEvent(PullToRefreshBase.State.RESET, R.raw.reset_sound);
                    soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING, R.raw.refreshing_sound);
                    mPullRefreshListView.setOnPullEventListener(soundListener);


                    // You can also just use setListAdapter(mAdapter) or
                    mPullRefreshListView.setAdapter(mAdapter);
                    actualListView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();


                    // Call onRefreshComplete when the list has been refreshed.
                    mPullRefreshListView.onRefreshComplete();
                }
            },new Response.ErrorListener(){//请求失败
                @Override
                public void onErrorResponse(VolleyError error) {
                    /*
                    TimeoutError -- ConnectionTimeout or SocketTimeout
                    AuthFailureError -- 401 ( UNAUTHORIZED ) && 403 ( FORBIDDEN )
                    ServerError -- 5xx
                    ClientError -- 4xx(Created in this demo for handling all 4xx error which are treated as Client side errors)
                    NetworkError -- No network found
                    ParseError -- Error while converting HTTP Response to JSONObject.
                    */
                    if( error instanceof NetworkError) {
                    } else if( error instanceof ServerError) {
                    } else if( error instanceof AuthFailureError) {
                    } else if( error instanceof ParseError) {
                    } else if( error instanceof NoConnectionError) {
                    } else if (error instanceof TimeoutError) {
                    }
                }
            });
            stringRequest.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            stringRequest.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36");
            mVolleyQueue.add(stringRequest);
            //使用volley发送StringRequest请求--------------------结束
            super.onPostExecute(result);
        }
    }

    /**
     * 项适配器
     */
    private class ItemAdapter  extends android.widget.BaseAdapter {


        @Override
        public int getCount() {
            return huxius.size();
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
            HuXiu huxiu = huxius.get(position);
            View v = View.inflate(context, R.layout.activity_activity_adapter_item, null);
            final ImageView mImageView = (ImageView) v.findViewById(R.id.cover);
            mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //加载图片
            String url =huxiu.getCover();
            ImageRequest imgRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    mImageView.setImageBitmap(response);
                }
            }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mImageView.setImageResource(R.drawable.ic_launcher);
                }
            });
            mVolleyQueue.add(imgRequest);
            TextView title = (TextView) v.findViewById(R.id.title);
            TextView time = (TextView) v.findViewById(R.id.time);
            title.setText(huxiu.getTitle());
            time.setText(huxiu.getTime());
            return v;
        }
    }
}
