package com.ckz.crawler.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ckz.crawler.R;
import com.ckz.crawler.widget.PullRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lynnchurch.horizontalscrollmenu.BaseAdapter;
import com.lynnchurch.horizontalscrollmenu.HorizontalScrollMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * create an instance of this fragment.
 *
 * 首页模块碎片
 */
public class HomeFragment extends Fragment {

    //导航菜单
    private HorizontalScrollMenu hsm_container;
    private View view ;//fragment_home View对象
    private List<PullRefreshListView> mPullRefreshListViews;
    private boolean flag = false;//ListView是否已经加载完成


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        return view;
    }

    public void initView() {
        hsm_container = (HorizontalScrollMenu) view.findViewById(R.id.hsm_container);
        hsm_container.setSwiped(true);
        hsm_container.setAdapter(new MenuAdapter());
    }

    private class MenuAdapter extends BaseAdapter {
        String[] names = new String[]
                {"虎嗅-说正经","虎嗅-弄创潮","虎嗅-早晚见","虎嗅-耍腔调","推酷-SEO","推酷-交互设置","推酷-创业", "推酷-O2O", "推酷-软件构架", "推酷-产品设计"};

        String[] urls = new String[]{
                "http://www.huxiu.com/business.html",
                "http://www.huxiu.com/startups.html",
                "http://www.huxiu.com/brief.html",
                "http://www.huxiu.com/lifestyle.html",
                "http://www.tuicool.com/topics/10450014",
                "http://www.tuicool.com/topics/10500001",
                "http://www.tuicool.com/topics/10000024",
                "http://www.tuicool.com/topics/10050185",
                "http://www.tuicool.com/topics/11000148",
                "http://www.tuicool.com/topics/10500004"
        };
        String[] tips = new String[]{
                "明星公司、人物与产业趋势",
                "关于创业的一切",
                "早晚报、一周热文、嗅评",
                "生活方式、文化思潮与八卦",
                "http://www.tuicool.com/topics/10450014",
                "http://www.tuicool.com/topics/10500001",
                "http://www.tuicool.com/topics/10000024",
                "http://www.tuicool.com/topics/10050185",
                "http://www.tuicool.com/topics/11000148",
                "http://www.tuicool.com/topics/10500004"
        };
        int category = 0;//0表示虎嗅、1表示推酷
        @Override
        public List<String> getMenuItems() {
            // TODO Auto-generated method stub
            return Arrays.asList(names);
        }

        @Override
        public List<View> getContentViews() {
            // TODO Auto-generated method stub
            List<View> views = new ArrayList<View>();
            if(mPullRefreshListViews==null){
                mPullRefreshListViews = new ArrayList<PullRefreshListView>();
            }
            int length = names.length;
            for (int i=0;i<length;i++) {
                View v = LayoutInflater.from(getActivity()).inflate(R.layout.content_view, null);
                PullRefreshListView mPullRefreshListView = new PullRefreshListView();
                if(i<4){
                    category=0;
                }else{
                    category=1;
                }
                //PullToRefreshListView listView = mPullRefreshListView.getRefreshListView(v, getActivity(),urls[i],tips[i],category);
                FrameLayout frameLayout = mPullRefreshListView.getRefreshListView(v, getActivity(),urls[i],tips[i],category);
                //添加到ListView集合
                views.add(frameLayout);
                mPullRefreshListViews.add(mPullRefreshListView);
                /*
                TextView tv = (TextView) v.findViewById(R.id.tv_content);
                tv.setText(str);
                views.add(v);*/
            }
            mPullRefreshListViews.get(0).doDataTask();
            flag = true;
            return views;
        }

        /**
         * 页面发生改变时，调用
         * @param position
         * @param visitStatus
         */
        @Override
        public void onPageChanged(int position, boolean visitStatus) {
            // TODO Auto-generated method stub
            /*Toast.makeText(getActivity(),
                    "内容页：" + (position + 1) + " 访问状态：" + visitStatus,
                    Toast.LENGTH_SHORT).show();*/

            if(flag){
                mPullRefreshListViews.get(position).doDataTask();
            }

        }

    }





}
