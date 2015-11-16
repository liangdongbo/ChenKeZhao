package com.ckz.crawler.ui.fragment;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ckz.crawler.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
import com.lynnchurch.horizontalscrollmenu.BaseAdapter;
import com.lynnchurch.horizontalscrollmenu.HorizontalScrollMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * create an instance of this fragment.
 *
 * 首页模块碎片
 */
public class HomeFragment extends Fragment {

    //导航菜单
    private HorizontalScrollMenu hsm_container;
    View view ;//fragment_home View对象

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

    class MenuAdapter extends BaseAdapter {
        String[] names = new String[]
                {"菜单一", "菜单二", "菜单三", "菜单四", "菜单五", "菜单六", "菜单七"};

        @Override
        public List<String> getMenuItems() {
            // TODO Auto-generated method stub
            return Arrays.asList(names);
        }

        @Override
        public List<View> getContentViews() {
            // TODO Auto-generated method stub
            List<View> views = new ArrayList<View>();
            for (String str : names) {
                View v = LayoutInflater.from(getActivity()).inflate(R.layout.content_view, null);
                PullToRefreshListView listView = new PullRefreshListView().getRefreshListView(v, getActivity());
                //添加到ListView集合
                views.add(listView);
                /*
                TextView tv = (TextView) v.findViewById(R.id.tv_content);
                tv.setText(str);
                views.add(v);*/
            }
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
        }

    }





}
