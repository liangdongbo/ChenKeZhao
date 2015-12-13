package com.ckz.crawler.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ckz.crawler.R;
import com.ckz.crawler.ui.view.HomeContentView;

/**
 * create an instance of this fragment.
 *
 * 首页模块碎片
 */
public class HomeFragment extends Fragment {

    private TabLayout mTabLayout;//选项卡
    private ViewPager mViewPager;//选项切换页
    private HomeContentView mHomeContentView;//首页内容视图
    private FloatingActionButton fab_snackbar_tip;//浮动按钮

    private View view ;//fragment_home View对象


    /**
     * 分页适配器
     */
    private PagerAdapter mPagerAdapter = new PagerAdapter() {

        String[] titles = new String[]
                {"虎嗅-说正经","虎嗅-弄创潮","虎嗅-早晚见","虎嗅-耍腔调","推酷-SEO","推酷-交互设置","推酷-创业", "推酷-O2O", "推酷-软件构架", "推酷-产品设计"};

        //获取数据的url
        String[] xh_urls = new String[]{
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
        String[] urls = new String[]{
                "http://www.huxiu.com/v2_action/article_list?huxiu_hash_code=b40645f27fce6ac837ba7837db8e63ad&catid=1&page=2",
                "http://www.huxiu.com/v2_action/article_list?huxiu_hash_code=b40645f27fce6ac837ba7837db8e63ad&catid=2&page=2",
                "http://www.huxiu.com/v2_action/article_list?huxiu_hash_code=b40645f27fce6ac837ba7837db8e63ad&catid=3&page=2",
                "http://www.huxiu.com/v2_action/article_list?huxiu_hash_code=b40645f27fce6ac837ba7837db8e63ad&catid=4&page=2",
                "http://www.tuicool.com/topics/10450014?st=0&lang=1&pn=1",
                "http://www.tuicool.com/topics/10500001?st=0&lang=1&pn=1",
                "http://www.tuicool.com/topics/10000024?st=0&lang=1&pn=1",
                "http://www.tuicool.com/topics/10050185?st=0&lang=1&pn=1",
                "http://www.tuicool.com/topics/11000148?st=0&lang=1&pn=1",
                "http://www.tuicool.com/topics/10500004?st=0&lang=1&pn=1"
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



        //开始调用显示页面
        @Override
        public void startUpdate(ViewGroup container) {
            super.startUpdate(container);
        }

        //设置当前位置上的ViewPager的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        //返回可用视图的数量
        @Override
        public int getCount() {
            return titles.length;
        }

        //根据指定的位置创建一个 item
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.content_view, null);
            fab_snackbar_tip = (FloatingActionButton) v.findViewById(R.id.fab_snackbar_tip);
            mHomeContentView = new HomeContentView();
            if(position<4){
                category=0;
            }else{
                category=1;
            }
            LinearLayout linearLayout = mHomeContentView.getView(v,fab_snackbar_tip, getActivity(), urls[position],xh_urls[position],tips[position], category);
            mHomeContentView.doDataTask();
            ((ViewPager) container).addView(linearLayout);
            return linearLayout;
        }

        //移除给定位置的页面
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        //决定是否与一个特定的页面视图返回的关键对象
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home, container, false);
        mTabLayout = (TabLayout) view.findViewById(R.id.tl_home_tabs);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_home_viewpager);

        //第一步
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //第二步
        mTabLayout.setTabsFromPagerAdapter(mPagerAdapter);
        //第三步
        final TabLayout.TabLayoutOnPageChangeListener listener = new TabLayout.TabLayoutOnPageChangeListener(mTabLayout);
        mViewPager.addOnPageChangeListener(listener);
        mViewPager.setAdapter(mPagerAdapter);
        return view;
    }

}
