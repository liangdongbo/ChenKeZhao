package com.ckz.crawler.utils;

import com.ckz.crawler.entity.Article;

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
    public List<Article> getHuXiuList(String html){
        List<Article> results = new ArrayList<Article>();
        Document doc = Jsoup.parse(html);
        Elements items =  doc.select("div[class=mod-b mod-art]");          //获取全部模块内容元素
        for(Element item:items){
            //获取每一个模块
            //id
            String id = item.attr("data-aid");
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
            results.add(new Article(id,a.text(),href,content.text(),cover,time.text()));
        }
        return results;
    }

    /**
     * 推酷，获取文章项列表
     * @return
     */
    public List<Article> getTuicoolList(String html){
        List<Article> results = new ArrayList<Article>();
        Document doc = Jsoup.parse(html);
        Elements items =  doc.select("div[class=single_fake]");          //获取全部模块内容元素
        for(Element item:items){
            String cover_path="";
            String title_str="";
            String time_str="";
            String href_str="";
            String content_str="";
            //获取每一个模块
            //id
            String id = item.attr("data-id");
            //标题
            Element a = item.getElementsByClass("article-list-title").first();
            if(a!=null){
                href_str = "http://www.tuicool.com"+a.attr("href");//内容url
                title_str=a.text();
            }
            //发布时间
            Element time = item.select("div[class=tip meta-tip]").first();
            if(time!=null){
                time_str=time.getElementsByTag("span").get(1).text();
            }
            //封面信息
            Element head = item.getElementsByClass("article_thumb").first();
            if(head!=null){
                Element cover  = head.getElementsByTag("img").first();
                cover_path=cover.attr("src");
            }

            //快照内容
            Element content  = item.getElementsByClass("article_cut").first();
            if(content!=null){
                content_str=content.text();
            }
            results.add(new Article(id,title_str,href_str,content_str,cover_path,time_str));
        }
        return results;
    }

    /**
     * 根据html，获取虎嗅或推酷正文内容
     * @param html
     * @return
     */
    public String getContent(String html){
        Document doc = Jsoup.parse(html);
        String str="";
        Elements content1 =  doc.select("div[class=article-wrap]");//获取虎嗅正文元素
        Elements content2 = doc.getElementsByClass("contant");//获取推酷正文元素
        if (content1.size()!=0){
            str=content1.html();
        }else if (content2.size()!=0){
            str = content2.html();
        }
        return str;
    }
}
