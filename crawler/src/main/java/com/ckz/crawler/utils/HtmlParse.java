package com.ckz.crawler.utils;

import com.ckz.crawler.entity.HuXiu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by win7 on 2015/11/17.
 * HTML解析工具包
 */
public class HtmlParse {

    /**
     * 虎嗅，获取列表数据模块
     * @param html
     * @return
     */
    public List<HuXiu> getHuXiuList(String html){
        List<HuXiu> results = new ArrayList<HuXiu>();
        Document doc = Jsoup.parse(html);
        Elements items =  doc.select("div[class=mod-b mod-art]");          //获取全部模块内容元素
        for(Element item:items){
            //获取每一个模块
            //标题
            Element title = item.getElementsByClass("mob-ctt").first();
            Element h3 = title.getElementsByTag("h3").first();   //推荐内容标题元素
            Element a = h3.getElementsByTag("a").first();  //推荐内容的Url超链接元素
            String href = "http://www.huxiu.com"+a.attr("href");                        //内容url
            //发布时间
            Element time = title.getElementsByClass("time").first();
            //头像信息
            Element head = item.getElementsByClass("mod-thumb").first();
            String cover  = head.getElementsByTag("img").first().attr("data-original").toString();

            //快照内容
            Element content  = title.getElementsByClass("mob-sub").first();
            results.add(new HuXiu(a.text(),href,content.text(),cover,time.text()));
        }
        return results;
    }
}
