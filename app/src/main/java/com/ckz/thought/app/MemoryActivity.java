package com.ckz.thought.app;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ckz.thought.R;
import com.ckz.thought.utils.BitmapUtils;
import com.ckz.thought.utils.MusicUtils;
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

    private static final String TAG = MemoryActivity.class.getSimpleName();
    private GridView gridView;
    private List<Map<String, Object>> resource;//gridview适配器数据源
    private BitmapUtils bitmapUtils;//位图处理工具类
    private List<Map<String, Object>> bitmaps;//获取0~9数字的位图数组
    private int count = 0;
    private int setCount = 4;//设置显示多少个数字
    //九宫格按钮颜色数组
    int[] btnColors;
    //提醒任务
    private TimerUtils timerUtils;//记录触发时间
    private TimerUtils timerTime;//记录秒
    private int setTimeout = 3;
    private final int TIMEOUT = 1;//超时处理
    private final int LOOP=2;//循环处理
    private final int BTNLISTENER=3;//是否完全获取按钮集合
    private final int GETRESULT=4;//是否完全装载结果
    private int second  = 1;//记录秒


    private ImageView[] btns;//记录按钮集合
    private int[] btnNum;//按钮对应的数字

    private Button btn_memory_go;
    private Button btn_memory_end;

    private Resources res;


    //记录操作
    private String recordResult="";//记录数字
    private int[] resultArray;//正确结果
    private String result="";//获取正确结果
    private boolean flag;//标记是否为结果


    private int score =0;//分数记录
    private int oCount = 0;//操作次数
    private int oTimeout = 0;//超时记录
    private TextView app_memory_score;//获取分数记录控件
    private TextView app_memory_count;//获取操作次数控件
    private TextView app_memory_timeOut;//获取超时控件
    private TextView app_memory_recordInput;//获取显示结果控件

    private MusicUtils musicUtils;

    //消息处理机制
    private final Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIMEOUT://超时，记录数据
                    musicUtils.gameOverMusic(MemoryActivity.this);
                    oTimeout++;//超时
                    score--;//减分
                    oCount++;//次数
                    refurbishRecord();
                    timerUtils.clearTimeout();
                    Toast.makeText(MemoryActivity.this, "超时了("+setTimeout+"秒)  ◎＿◎", Toast.LENGTH_SHORT).show();
                    //重新开始
                    //洗牌
                    refreshNumbers(setCount, true);
                    //计时
                    timerTime.setTimeout(0, second, LOOP);
                    break;
                case LOOP ://循环处理
                    int s = 3;
                    btn_memory_end.setClickable(false);
                    if(second>s){
                        second = second-s;
                    }
                    btn_memory_end.setText("开始倒计时："+second);
                    if (second==s){
                        refreshNumbers(bitmaps.size(), false);
                        timerTime.clearTimeout();
                        recordResult="";
                        btn_memory_end.setText("请按顺序输入数字...");
                        /*gridView.setOnItemClickListener(new ItemClickListener());*/
                    }
                    second++;
                    break;
                case BTNLISTENER://按钮已经获取，可以监听
                    //注册按钮事件
                    int length = btns.length;
                    for(int i=0;i<length;i++){
                        if(btns[i]!=null){
                            btns[i].setClickable(true);//按钮事件
                            btns[i].setOnTouchListener(new BtnTouchListener());
                        }
                    }
                    break;
                case GETRESULT:
                    //获取正确结果
                    int l = resultArray.length;
                    for(int j=0;j<l;j++){
                        result+=resultArray[j];
                    }
                    break;
            }
        }
    };

    /**
     * gridView项监听事件
     */
    private class  ItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }

    /**
     * 触摸事件
     */
    private class BtnTouchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(event.getAction()==MotionEvent.ACTION_DOWN){//按下
                if(v.getId()==R.id.app_memory_recordInput){//显示结果
                    app_memory_recordInput.setText(result);
                }else{//按钮事件
                    if(timerUtils.getTimer()==null){
                        timerUtils.setTimeout(setTimeout,TIMEOUT);//记录超时
                    }
                    musicUtils.gameBtnMusic(MemoryActivity.this);
                    int length = btns.length;
                    for(int i=0;i<length;i++){
                        if(v.equals(btns[i])){
                            recordResult+=(long) btnNum[i];
                            //是否一致
                            Toast.makeText(MemoryActivity.this, result +"=="+ recordResult, Toast.LENGTH_SHORT).show();
                            boolean b= false;//是否结束
                            if(recordResult.equals(result)){
                                b = true;
                                //通关音效
                                musicUtils.gameNextMusic(MemoryActivity.this);
                                score++;//加分

                            }
                            if(Long.parseLong(recordResult)>Long.parseLong(result)){
                                b= true;
                                //失败
                                musicUtils.gameOverMusic(MemoryActivity.this);
                                Toast.makeText(MemoryActivity.this, "回家练练吧  ◎＿◎", Toast.LENGTH_SHORT).show();
                                score--;//减分
                            }
                            if(b){
                                timerUtils.clearTimeout();//取消记录超时
                                oCount++;
                                //重新开始
                                //洗牌
                                refreshNumbers(setCount, true);
                                //计时
                                timerTime.setTimeout(0,second,LOOP);
                                refurbishRecord();
                            }
                        }
                    }
                }

            }
            if(event.getAction()==MotionEvent.ACTION_UP){//弹起
                if(v.getId()==R.id.app_memory_recordInput){//显示结果
                    app_memory_recordInput.setText("");
                }else{

                }
            }
            return false;
        }
    }

    /**
     * 刷新记录数值
     */
    private void  refurbishRecord(){
        app_memory_count.setText("次数：" + oCount);
        app_memory_score.setText("分数："+score);
        app_memory_timeOut.setText("超时："+oTimeout);
    }
    /**
     * 单击监听事件
     */
    private class BtnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_memory_go :
                    btn_memory_go.setText("");
                    //开始游戏
                    timerTime.setTimeout(0,second,LOOP);
                    refreshNumbers(setCount, true);
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
                    refreshNumbers(bitmaps.size(),false);
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
        //记录秒
        timerTime = new TimerUtils(myHandler);

        btnNum = new int[bitmaps.size()];


        musicUtils = new MusicUtils();

        //初始化记录控件
        app_memory_score = (TextView) findViewById(R.id.app_memory_score);
        app_memory_count = (TextView) findViewById(R.id.app_memory_count);
        app_memory_timeOut = (TextView) findViewById(R.id.app_memory_timeOut);
        app_memory_recordInput = (TextView) findViewById(R.id.app_memory_recordInput);
        //按下，显示结果，提起，隐藏
        app_memory_recordInput.setOnTouchListener(new BtnTouchListener());
    }

    @Override
    protected void onStart() {
        super.onStart();
        //显示数字
        refreshNumbers(bitmaps.size(),false);
    }

    /**
     * 重新加载布局数字显示
     */
    private void refreshNumbers(int n,boolean b) {
        flag = b;
        count = n;
        if(flag){
            resultArray = new int[count];
        }
        if(!flag){
            btns = new ImageView[bitmaps.size()];
        }
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
            int num = (int) bitmaps.get(position).get("number");
            //适配器
            View view = View.inflate(getApplicationContext(), R.layout.activity_memory_item, null);
            ImageView iv = (ImageView) view.findViewById(R.id.imageView);
            if(flag){
                resultArray[position] = num;
                if(position==(getCount()-1)){//触发处理获取正确结果
                    result="";
                    Message mes = new Message();
                    mes.what = GETRESULT;
                    myHandler.sendMessage(mes);
                }
            }else{
                btns[position] = iv;
                btnNum[position] = num;
                if(position==(getCount()-1)){//触发处理按钮监听事件
                    Message mes = new Message();
                    mes.what = BTNLISTENER;
                    myHandler.sendMessage(mes);
                }
            }
            iv.setImageBitmap((Bitmap) bitmaps.get(position).get("bitmap"));
            return view;
        }
    }
}
