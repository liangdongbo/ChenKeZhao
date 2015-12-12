package com.ckz.crawler.ui.activity;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.ckz.crawler.R;
import com.ckz.crawler.utils.SpiderUtils;

/**
 * 显示爬虫正式内容页
 */
public class SpiderContentShowActivity extends Activity {
    public static final int LOAD_SUCCEED = 1;
    private String link;
    private static WebView webView;
    private static ImageView iv_loading;
    private static SpiderUtils su;
    public static Handler MyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case LOAD_SUCCEED :
                    //显示html数据
                    String spider_html = su.getContent();
                    if(spider_html.trim().length()==0||spider_html==null){
                        spider_html="找不到内容...";
                    }
                    webView.loadDataWithBaseURL(null, spider_html, "text/html","UTF-8", null);//解决中文乱码
                    //webView.loadData("<html><body>啥啥<body></html>", "text/html", "UTF-8");
                    iv_loading.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spider_content_show);
        Bundle bundle = this.getIntent().getExtras();
        link = bundle.getString("link");
        webView = (WebView) findViewById(R.id.spider_content);
        WebSettings webSettings= webView.getSettings(); // webView: 类WebView的实例
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        /*webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);*/
        iv_loading = (ImageView) findViewById(R.id.web_loading);
        iv_loading.setVisibility(View.VISIBLE);
        su = new SpiderUtils();
        su.getSpiderConten(link, this);
    }

}
