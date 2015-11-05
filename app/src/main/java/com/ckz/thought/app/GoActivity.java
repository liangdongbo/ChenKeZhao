package com.ckz.thought.app;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ckz.thought.R;
import com.ckz.thought.service.LocalhostMusicService;
import com.ckz.thought.utils.BitmapUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by kaiser on 2015/10/26.
 * 菜单一
 * 算法基础，对于算术公式的默算能力提升
 */
public class GoActivity extends AppCompatActivity{

    private static final String TAG = GoActivity.class.getSimpleName();
    private static int[][] btns;//九宫格按钮资源ID集合
    private static int[][] drawId;//按钮资源ID
    private static int[][] drawId_1;
    private static int[][] btnColors;//按钮颜色集合
    private static int temp = 0;//记录九宫格按钮按下的位置
    private static ImageButton btn;
    private static ImageView imageView;
    private static TextView tvFormula;//显示公式的文本框
    private static Bitmap bitmap;//九宫格数字位图

    //随机公式创建
    private static String[] operations=new String[]{"+","-","×"};
    private static int numberOne = 0;
    private static int numberTwo = 0;
    private static int result = 0;
    private static int numMax = 20;
    private static List<Integer> btnClickResult;
    //private static int resultLength=5;//保存记录结果的最大长度
    private static TextView recordInput;//记录输入的结果
    private static TextView tvShowHelp;//显示结果帮助


    public  GoActivity(){
        super();
    }

    /**
     * 监听按钮的单击事件
     */
    private class MyBtnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tvFormula :
                    //game yes背景音乐
                    LocalhostMusicService.doStart(GoActivity.this,R.raw.game_yes,false);
                    //创建运算公式
                    String formula = createFormula();
                    tvFormula.setText(formula);
                    //单击按钮的结果
                    btnClickResult=null;//设置为null，让垃圾回收机制回收
                    //清空记录
                    recordInput.setText("");
                    Toast.makeText(GoActivity.this, "清空了记录的结果！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 按钮的监听按下和弹起事件
     */
    private class MyBtnTouchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            //按下
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                switch (v.getId()){
                    case R.id.tvFormula ://显示公式文本框事件
                        tvFormula.setBackground(null);
                        break;
                    case R.id.tvShowHelp ://显示结果帮助事件
                        String tempResult = String.valueOf(result);
                        if(tempResult.length()>2){
                            tempResult = "...";
                        }
                        tvShowHelp.setText(tempResult);
                        tvShowHelp.setBackground(null);
                        //?背景音乐
                        LocalhostMusicService.doStart(GoActivity.this, R.raw.question, false);
                        break;
                    default://九宫格按钮事件
                        //背景音乐
                        LocalhostMusicService.doStart(GoActivity.this,R.raw.btn_click,false);
                        int id = v.getId();
                        int length = btns.length;
                        for(int i=0;i<length;i++){
                            btn = (ImageButton) findViewById(btns[i][1]);
                            if(btn.getId()==id){
                                //对应事件
                                temp=i;
                                btn.setBackground(getResources().getDrawable(drawId_1[i][1]));
                                //找到对应的数字
                                int num = drawId[i][0];
                                int flag = 0;
                                int btnsLength = btnColors.length;
                                for(int j=0;j<btnsLength;j++){
                                    if(num==btnColors[j][0]){
                                        //记录下对应的数字
                                        flag = j;
                                        break;
                                    }
                                }
                                //Toast.makeText(GoActivity.this, "当前按钮代表数字为："+(flag+1), Toast.LENGTH_SHORT).show();
                                //保存单击的数字
                                if(btnClickResult==null){
                                    btnClickResult = new ArrayList<Integer>();
                                }
                                btnClickResult.add(flag+1);
                                //判断答案与结果是否一致
                                int result_l = btnClickResult.size();
                                String resultStr = "";
                                for(int r=0;r<result_l;r++){
                                    resultStr+=btnClickResult.get(r);
                                    recordInput.setText("输入记录："+resultStr);
                                }
                                boolean rFlag = false;//是否洗牌标记
                                String message ="";
                                if(result==Integer.parseInt(resultStr)){
                                    //game over背景音乐
                                    LocalhostMusicService.doStart(GoActivity.this,R.raw.game_yes,false);
                                    //答案与结果一致
                                    rFlag = true;
                                    message = "Good，不错不错。";
                                }else if(result_l>String.valueOf(result).length() || Integer.parseInt(resultStr)>result){
                                    //game over背景音乐
                                    LocalhostMusicService.doStart(GoActivity.this,R.raw.game_over,false);
                                    //答案不一致，并记录值超出正确结果长度
                                    rFlag = true;
                                    message = "答案错误，且结果记录值已经大于正确结果值！";
                                }
                                if(rFlag){
                                    Toast.makeText(GoActivity.this, message, Toast.LENGTH_SHORT).show();
                                    //洗牌
                                    shuffleAll();
                                    //创建公式
                                    String formula = createFormula();
                                    tvFormula.setText(formula);
                                    btnClickResult=null;//设置为null，让垃圾回收机制回收
                                    //清空记录
                                    recordInput.setText("");
                                    //清空帮助
                                    tvShowHelp.setText("");
                                    tvShowHelp.setBackground(getResources().getDrawable(R.drawable.question));
                                }
                                break;
                            }
                        }
                        break;
                }
            }
            //弹起
            if(event.getAction() == MotionEvent.ACTION_UP){
                switch (v.getId()) {
                    case R.id.tvFormula://显示公式文本框事件
                        tvFormula.setBackground(getResources().getDrawable(R.drawable.tv_dialog));
                        tvShowHelp.setText("");
                        tvShowHelp.setBackground(getResources().getDrawable(R.drawable.question));
                        break;
                    case R.id.tvShowHelp://显示结果帮助事件
                        tvShowHelp.setText("");
                        tvShowHelp.setBackground(getResources().getDrawable(R.drawable.question));
                        break;
                    default://九宫格按钮事件
                        btn.setBackground(getResources().getDrawable(drawId[temp][1]));
                        break;
                }
            }
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go);
        //背景音乐
        LocalhostMusicService.doStart(GoActivity.this, R.raw.back_go_start, false);

        //初始化资源
        tvShowHelp = (TextView) findViewById(R.id.tvShowHelp);
        recordInput = (TextView) findViewById(R.id.recordInput);
        imageView = (ImageView) findViewById(R.id.textNumber);
        tvFormula  = (TextView) findViewById(R.id.tvFormula);
        //获取按钮对应的图片资源ID
        drawId =new int[][]{
                {1,R.drawable.btn_gray},
                {2,R.drawable.btn_red},
                {3,R.drawable.btn_green},
                {4,R.drawable.btn_khaki},
                {5,R.drawable.btn_violet},
                {6,R.drawable.btn_blue_green},
                {7,R.drawable.btn_white},
                {8,R.drawable.btn_yellow},
                {9,R.drawable.btn_watchet}
        };
        drawId_1 =new int[][]{
                {1,R.drawable.btn_gray_1},
                {2,R.drawable.btn_red_1},
                {3,R.drawable.btn_green_1},
                {4,R.drawable.btn_khaki_1},
                {5,R.drawable.btn_violet_1},
                {6,R.drawable.btn_blue_green_1},
                {7,R.drawable.btn_white_1},
                {8,R.drawable.btn_yellow_1},
                {9,R.drawable.btn_watchet_1}
        };
        //获取按钮对应的资源ID
        btns = new int[][]{
                {1,R.id.btnI_1},
                {2,R.id.btnI_2},
                {3,R.id.btnI_3},
                {4,R.id.btnI_4},
                {5,R.id.btnI_5},
                {6,R.id.btnI_6},
                {7,R.id.btnI_7},
                {8,R.id.btnI_8},
                {9,R.id.btnI_9}
        };

        //app_go_number 1620x720
        bitmap = BitmapUtils.getMutableBitmap(getResources(),R.drawable.app_go_number);
        //九宫格按钮颜色数组
        btnColors = new int[][]{
                {1,getResources().getColor(R.color.btn_go_one)},
                {2,getResources().getColor(R.color.btn_go_two)},
                {3,getResources().getColor(R.color.btn_go_three)},
                {4,getResources().getColor(R.color.btn_go_four)},
                {5,getResources().getColor(R.color.btn_go_five)},
                {6,getResources().getColor(R.color.btn_go_six)},
                {7,getResources().getColor(R.color.btn_go_seven)},
                {8,getResources().getColor(R.color.btn_go_eight)},
                {9,getResources().getColor(R.color.btn_go_nine)}/*,
                {0,getResources().getColor(R.color.btn_go_zero)}*/
        };

        //注册公式文本框事件
        tvFormula.setOnClickListener(new MyBtnClickListener());
        tvFormula.setOnTouchListener(new MyBtnTouchListener());
        //帮助help事件注册，XML添加android:clickable="true"
        tvShowHelp.setOnTouchListener(new MyBtnTouchListener());


        //注册按钮监听事件
        int length = btns.length;
        for(int i=0;i<length;i++){
            findViewById(btns[i][1]).setOnTouchListener(new MyBtnTouchListener());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        shuffleAll();
        //创建运算公式
        String formula = createFormula();
        tvFormula.setText(formula);
    }

    /**
     * 洗牌所有，数字颜色及按钮
     */
    private void shuffleAll(){
        //打乱颜色及显示
        shuffleNum(btnColors, bitmap, imageView);
        //打乱按钮显示顺序
        shuffleBtn(btns, drawId);
    }

    /**
     * 打乱按钮显示顺序
     * @param btns  九宫格按钮资源ID集合
     * @param drawId 按钮图片资源ID集合
     */
    private void shuffleBtn(int[][] btns,int[][] drawId){

        //随机排序按钮图片资源List
        List<int[]> list = Arrays.asList(drawId);
        Collections.shuffle(list);
        //同步drawId_1
        int size = list.size();
        int length_ = drawId_1.length;
        for(int i=0;i<size;i++){
            int temp = list.get(i)[0];
            for(int j=i;j<length_;j++){
                if(temp==drawId_1[j][0]){
                    int[] temp2 = drawId_1[i];
                    drawId_1[i]=drawId_1[j];
                    drawId_1[j]=temp2;
                    break;
                }
            }
        }

        int length = btns.length;
        for(int i=0;i<length;i++){
            View btn = findViewById(btns[i][1]);
            btn.setBackground(getResources().getDrawable(list.get(i)[1]));
        }
    }

    /**
     * 打乱数字颜色显示顺序
     * @param btnColors 定义好的按钮颜色数组
     * @param bitmap 要处理的图片对象
     * @param imageView 要显示处理后的图片的ImageView
     */
    private void shuffleNum(int[][] btnColors,Bitmap bitmap,ImageView imageView){

        //打乱数字颜色显示顺序
        List<int[]> list = Arrays.asList(btnColors);
        Collections.shuffle(list);

        //显示颜色
        int bitmapW = bitmap.getWidth();
        int bitmapH = bitmap.getHeight();
        int xW = bitmapW/5;
        int yH = bitmapH/2;
        for(int i=0;i<9;i++){
            int n = 0;
            if(i>4){
                n=1;
            }
            int j = i;
            if(i>=5){
                j=i-5;
            }
            bitmap=BitmapUtils.setBitmapPixel(
                    bitmap,
                    xW*j,
                    yH*n,
                    xW*(j+1),
                    yH*(n+1),
                    btnColors[i][1]
            );
        }
        imageView.setImageBitmap(bitmap);
    }

    /**
     * 创建基本的运算公式，并保存运算的结果
     * @return
     */
    private String createFormula(){
        //打乱操作符排序，并取出第一个操作符
        List<String> list = Arrays.asList(operations);
        Collections.shuffle(list);
        String operation =list.get(0);
        numberOne = randomInteger(numMax);
        numberTwo = randomInteger(numMax);
        if(numberOne<numberTwo && "-".equals(operation)){
            int temp =numberOne;
            numberOne = numberTwo;
            numberTwo = temp;
        }
        String str = numberOne+operation+numberTwo;
        switch (operation){
            case "+" :
                result = numberOne+numberTwo;
                break;
            case "-" :
                result = numberOne-numberTwo;
                break;
            case "×":
                result = numberOne*numberTwo;
                break;
        }
        return str;
    }

    /**
     * 返回一个指定最大数内的一个随机整数
     * @param max
     * @return
     */
    private int randomInteger(int max){
        return new Random().nextInt(max);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
