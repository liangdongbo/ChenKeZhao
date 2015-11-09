package com.ckz.thought.app;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ckz.thought.R;
import com.ckz.thought.utils.BitmapUtils;
import com.ckz.thought.utils.TimerUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kaiser on 2015/10/27.
 * 菜单二
 * 升级你的脑子记忆力
 */
public class MemoryActivity extends AppCompatActivity {

    private GridView gridView;
    private List<Map<String, Object>> resource;//gridview适配器数据源
    private BitmapUtils bitmapUtils;//位图处理工具类
    private List<Map<String, Object>> bitmaps;//获取0~9数字的位图数组
    private int count = 0;//设置显示多少个数字
    private Long result;//正确结果
    private Long recordResult;//记录结果
    //九宫格按钮颜色数组
    int[] btnColors;
    //提醒任务
    private TimerUtils timerUtils;//记录触发时间
    private TimerUtils timerTime;//记录秒
    private int setTimeout = 3;
    private final int TIMEOUT = 1;//超时处理
    private final int LOOP=2;//循环处理
    private int second  = 1;//记录秒


    private ImageView[] btns;//记录按钮集合
    private int[] btnNum;//按钮对应的数字

    private Button btn_memory_go;
    private Button btn_memory_end;

    private Resources res;

    //消息处理机制
    private final Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIMEOUT://游戏计时开始
                    refreshNumbers(bitmaps.size());
                    timerUtils.clearTimeout();
                    break;
                case LOOP ://循环处理
                    btn_memory_end.setText("计时中..."+second);
                    if(second==3){
                        timerTime.clearTimeout();
                        btn_memory_end.setText("请按顺序输入数字...");
                        btn_memory_end.setClickable(false);
                        //注册按钮事件
                        int length = btns.length;
                        for(int i=0;i<length;i++){
                            btns[i].setClickable(true);//按钮事件
                            btns[i].setOnTouchListener(new BtnTouchListener());
                        }
                    }
                    second++;
                    break;
            }
        }
    };


    /**
     * 触摸事件
     */
    private class BtnTouchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(event.getAction()==MotionEvent.ACTION_DOWN){//按下
                int length = btns.length;
                for(int i=0;i<length;i++){
                    if(v==btns[i]){
                        Toast.makeText(MemoryActivity.this, "代表数字："+btnNum[i], Toast.LENGTH_SHORT).show();
                    }
                }
            }
            if(event.getAction()==MotionEvent.ACTION_UP){//弹起

            }
            return false;
        }
    }
    /**
     * 单击监听事件
     */
    private class BtnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_memory_go :
                    //开始游戏
                    timerUtils.setTimeout(setTimeout);
                    refreshNumbers(3);
                    //记录秒
                    timerTime.setTimeout(1);
                    //改变按钮属性
                    btn_memory_go.setId(R.id.btn_memory_end);
                    btn_memory_end = (Button) findViewById(R.id.btn_memory_end);
                    break;
                case R.id.btn_memory_end :
                    //结束游戏
                    timerUtils.clearTimeout();
                    timerTime.clearTimeout();
                    second=1;
                    //重新布置数字显示
                    refreshNumbers(bitmaps.size());
                    //改变按钮属性
                    btn_memory_go.setText("开始");
                    btn_memory_go.setId(R.id.btn_memory_go);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);
        gridView = (GridView) findViewById(R.id.gridView);
        bitmapUtils = new BitmapUtils();
        res = getResources();
        //九宫格按钮颜色数组
        btnColors = new int[]{
                res.getColor(R.color.btn_go_one),
                res.getColor(R.color.btn_go_two),
                res.getColor(R.color.btn_go_three),
                res.getColor(R.color.btn_go_four),
                res.getColor(R.color.btn_go_five),
                res.getColor(R.color.btn_go_six),
                res.getColor(R.color.btn_go_seven),
                res.getColor(R.color.btn_go_eight),
                res.getColor(R.color.btn_go_nine),
                res.getColor(R.color.btn_go_zero)
        };
        bitmaps = bitmapUtils.getSquaredUpNum(this, R.drawable.app_go_number,btnColors);

        btn_memory_go = (Button) findViewById(R.id.btn_memory_go);

        btn_memory_go.setOnClickListener(new BtnClickListener());


        //初始化消息处理机制
        timerUtils = new TimerUtils(myHandler);
        timerUtils.setSequence(TIMEOUT);
        //记录秒
        timerTime = new TimerUtils(myHandler);
        timerTime.setSequence(LOOP);

        btns = new ImageView[bitmaps.size()];
        btnNum = new int[bitmaps.size()];
    }

    @Override
    protected void onStart() {
        super.onStart();
        //显示数字
        refreshNumbers(bitmaps.size());
    }

    /**
     * 重新加载布局数字显示
     */
    private void refreshNumbers(int n) {
        count = n;
        //随机排序按钮图片资源List
        Collections.shuffle(bitmaps);
        MyAdapter myAdapter = new MyAdapter();
        gridView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }


    /**
     * 自定定义的适配器
     */
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return count;
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
            //适配器
            View view = View.inflate(getApplicationContext(), R.layout.activity_memory_item, null);
            ImageView iv = (ImageView) view.findViewById(R.id.imageView);
            btns[position] = iv;
            btnNum[position] = (int) bitmaps.get(position).get("number");
            iv.setImageBitmap((Bitmap) bitmaps.get(position).get("bitmap"));
            return view;
        }
    }
}
