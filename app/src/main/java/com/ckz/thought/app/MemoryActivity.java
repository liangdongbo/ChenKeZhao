package com.ckz.thought.app;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.ckz.thought.R;
import com.ckz.thought.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kaiser on 2015/10/27.
 * 菜单二
 * 升级你的脑子记忆力
 */
public class MemoryActivity extends AppCompatActivity {

    private GridView gridView;
    private List<Map<String,Object>> resource;//gridview适配器数据源
    private BitmapUtils bitmapUtils;//位图处理工具类

    private Bitmap[] bitmaps;

    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);
        gridView = (GridView) findViewById(R.id.gridView);

        bitmapUtils = new BitmapUtils();
        bitmaps = bitmapUtils.getSquaredUpNum(this,R.drawable.app_go_number);

        res = getResources();



    }

    @Override
    protected void onStart() {
        super.onStart();

        MyAdapter myAdapter = new MyAdapter();
        gridView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }


    /**
     * 自定定义的适配器
     */
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return bitmaps.length;
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
            View view = View.inflate(getApplicationContext(),R.layout.activity_memory_item,null);
            ImageView iv = (ImageView) view.findViewById(R.id.imageView);
            iv.setImageBitmap(bitmaps[position]);
            return view;
        }
    }
}
