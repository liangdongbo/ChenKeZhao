package com.ckz.thought.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.ckz.thought.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);
        gridView = (GridView) findViewById(R.id.gridView);

        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < 100; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("imageView", R.drawable.btn_green);
            map.put("text", "哈哈" + i);
            data.add(map);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, R.layout.activity_memory_item,
                new String[]{"imageView", "text"}, new int[]{R.id.imageView, R.id.text});

        gridView.setAdapter(simpleAdapter);




    }
}
