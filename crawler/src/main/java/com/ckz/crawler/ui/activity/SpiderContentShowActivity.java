package com.ckz.crawler.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.ckz.crawler.R;

/**
 * 显示爬虫正式内容页
 */
public class SpiderContentShowActivity extends Activity {
    private WebView webView;
    private ImageView iv_loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spider_content_show);
        Bundle bundle = this.getIntent().getExtras();
        String link = bundle.getString("link");
        webView = (WebView) findViewById(R.id.spider_content);
        iv_loading = (ImageView) findViewById(R.id.web_loading);
        init(link);
    }

    /**
     * 程序内打开网页
     */
    private void init(String link){
        //WebView加载web资源
        webView.loadUrl(link);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        //启用支持javascript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        ///判断页面加载过程
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    // 网页加载完成
                    iv_loading.setVisibility(View.INVISIBLE);
                } else {
                    // 加载中
                    iv_loading.setVisibility(View.VISIBLE);
                }

            }
        });
        //缓存的使用
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        /*不使用缓存：
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        */
    }
}
