package com.ckz.thought.utils;

import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by win7 on 2015/11/9.
 * 定时器工具类
 */
public class TimerUtils {
    private Timer timer;

    //消息处理机制
    private Handler myHandler;

    //消息机制的序列号设置
    private int sequence;


    private TimerUtils(){
        super();
    }

    /**
     * 构造函数初始化 Handler 消息处理机制
     * @param handler 消息处理机制对象
     */
    public TimerUtils(Handler handler){
        myHandler = handler;
    }

    /**
     * 消息机制序列号的设置
     * @param number
     */
    public void setSequence(int number){
        sequence = number;
    }


    /**
     * 提醒任务
     */
    private class RemindTask extends TimerTask {
        @Override
        public void run() {
            Message message = new Message();
            message.what = sequence;
            myHandler.sendMessage(message);
        }
    }

    /**
     * 设置计时，秒为单位
     *
     * @param seconds
     */
    public void setTimeout(int seconds) {
        timer = new Timer();
        //第一次多久触发，之后每次多久触发
        timer.schedule(new RemindTask(), seconds * 1000,seconds * 1000);
    }

    /**
     * 取消计时器任务
     */
    public void clearTimeout() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }
}
