package com.ckz.crawler.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Message;
import android.util.JsonReader;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.toolbox.Volley;
import com.ckz.crawler.R;
import com.ckz.crawler.entity.Article;
import com.ckz.crawler.network.MyStringRequest;
import com.ckz.crawler.ui.activity.SpiderContentShowActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Created by ckz on 2015/11/17.
 * 陈科肇
 */
public class SpiderUtils{
    private List<Article> articles;//文章对象集合
    private Context context;
    private RequestQueue mVolleyQueue;
    private Resources res;
    private  ImageView iv_loading;
    private String content_html = "";

    public String getContent(){
        return content_html;
    }



    /**
     * 下拉-发送请求，获取数据块
     * @param v R.layout.content_view
     * @param url 爬取网址
     * @param mPullRefreshListView 下拉的View对象控件
     * @param context 上下文
     * @param category 爬取文章类别
     */
    public void getPullData(View v,final String url,final PullToRefreshListView mPullRefreshListView, final Context context, final int category){

        if(mVolleyQueue==null){
            mVolleyQueue = Volley.newRequestQueue(context);//请求队列
        }
        this.context = context;
        this.res = context.getResources();
        iv_loading = (ImageView) v.findViewById(R.id.loading);
        iv_loading.setVisibility(View.VISIBLE);

        //使用volley发送StringRequest请求--------------------开始
        MyStringRequest stringRequest = new MyStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response){//请求成功
                byte[] b = response.getBytes(Charset.forName("utf-8"));
                String html = new String(b);
                //System.out.println(html);
                if(category==0){//HuXiu
                    if(url.indexOf("page=")!=-1){
                        try {
                            Gson gson = new Gson();
                            Map<String,String> map= gson.fromJson(html,new TypeToken<Map<String,String>>() {}.getType());
                            html = map.get("data");
                        }catch (JsonSyntaxException ex){
                            throw new JsonSyntaxException(ex);
                        }
                    }
                    articles = new HtmlParse().getHuXiuList(html);
                }else{//Tuicool
                    articles = new HtmlParse().getTuicoolList(html);
                }

                ListView actualListView = mPullRefreshListView.getRefreshableView();

                ItemAdapter mAdapter = new ItemAdapter();

                //Add Sound Event Listener，事件音效
                /*SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(context);
                soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
                soundListener.addSoundEvent(PullToRefreshBase.State.RESET, R.raw.reset_sound);
                soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING, R.raw.refreshing_sound);
                mPullRefreshListView.setOnPullEventListener(soundListener);*/


                // You can also just use setListAdapter(mAdapter) or
                mPullRefreshListView.setAdapter(mAdapter);
                actualListView.setAdapter(mAdapter);//设置适配器
                mAdapter.notifyDataSetChanged();//更新数据
                iv_loading.setVisibility(View.INVISIBLE);
                // Call onRefreshComplete when the list has been refreshed.
                mPullRefreshListView.onRefreshComplete();//完成刷新，关闭刷新动画
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
                String message = "未定义";
                if( error instanceof NetworkError) {
                    message="网络链接异常，请检查网络配置";
                } else if( error instanceof ServerError) {
                    message="服务请求异常";
                } else if( error instanceof AuthFailureError) {
                    message="401 && 403";
                } else if( error instanceof ParseError) {
                    message = "解析错误";
                } else if( error instanceof NoConnectionError) {
                    message="链接异常";
                } else if (error instanceof TimeoutError) {
                    message="请求超时";
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        stringRequest.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36");
        mVolleyQueue.add(stringRequest);//添加到请求队列
        //使用volley发送StringRequest请求--------------------结束
    }


    /**
     * 项适配器
     */
    private class ItemAdapter  extends android.widget.BaseAdapter {


        @Override
        public int getCount() {
            return articles.size();
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
            Article article = articles.get(position);
            View v = View.inflate(context, R.layout.activity_activity_adapter_item, null);
            final ImageView mImageView = (ImageView) v.findViewById(R.id.spider_item_cover);
            mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //加载图片
            String url =article.getCover();
            if(url!=""){
                ImageRequest imgRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        mImageView.setImageBitmap(response);
                    }
                }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mImageView.setImageResource(R.mipmap.ic_launcher);
                    }
                });
                mVolleyQueue.add(imgRequest);
            }else{
                mImageView.setVisibility(View.GONE);//隐藏 并且不占用界面空间
            }
            TextView link = (TextView) v.findViewById(R.id.spider_item_link);
            TextView title = (TextView) v.findViewById(R.id.spider_item_title);
            TextView time = (TextView) v.findViewById(R.id.spider_item_time);
            TextView content = (TextView) v.findViewById(R.id.spider_item_content);
            title.setText(article.getTitle());
            time.setText(article.getTime());
            content.setText(article.getSnapshoot());
            link.setText(article.getHref());
            return v;
        }
    }

    /**
     * 爬虫正文
     * @param url
     * @param context
     */
    public void getSpiderConten(String url, final Context context){
        if(mVolleyQueue==null){
            mVolleyQueue = Volley.newRequestQueue(context);//请求队列
        }
        this.context = context;
        this.res = context.getResources();
        //使用volley发送StringRequest请求--------------------开始
        MyStringRequest stringRequest = new MyStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response){//请求成功
                byte[] b = response.getBytes(Charset.forName("UTF-8"));
                final String html = new String(b);
                content_html = new HtmlParse().getContent(html);
                Message msg  = new Message();
                msg.what= SpiderContentShowActivity.LOAD_SUCCEED;
                SpiderContentShowActivity.MyHandler.sendMessage(msg);
            }
        },new Response.ErrorListener(){//请求失败
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = "未定义";
                if( error instanceof NetworkError) {
                    message="网络链接异常，请检查网络配置";
                } else if( error instanceof ServerError) {
                    message="服务请求异常";
                } else if( error instanceof AuthFailureError) {
                    message="401 && 403";
                } else if( error instanceof ParseError) {
                    message = "解析错误";
                } else if( error instanceof NoConnectionError) {
                    message="链接异常";
                } else if (error instanceof TimeoutError) {
                    message="请求超时";
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        stringRequest.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36");
        mVolleyQueue.add(stringRequest);//添加到请求队列
        //使用volley发送StringRequest请求--------------------结束
    }



}
