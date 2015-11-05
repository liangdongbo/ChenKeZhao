package com.ckz.thought.service;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.ckz.thought.app.GoActivity;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by kaiser on 2015/10/29.
 * 本地音乐播放服务
 * 单例模式
 */
public class LocalhostMusicService{

    public static MediaPlayer mediaPlayer;

    private static final String TAG="LocalhostMusicService";

    /**
     * 单例化，不允许外面new对象
     */
    private static LocalhostMusicService musicService = new LocalhostMusicService();

    /**
     * 提供外部访问接口
     * @return
     */
    public static LocalhostMusicService getMusicService(){
        return musicService;
    }

    /**
     * 开始播放
     * @param resid 音频资源ID
     */
    public static void doStart(Context context,Integer resid,boolean isLoop){
        try {
            //初始化音频资源
            doStop();
            mediaPlayer = MediaPlayer.create(context,resid);
            mediaPlayer.setLooping(isLoop);
            mediaPlayer.start();

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.i(TAG, "按钮声音播放失败..." + mediaPlayer);
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置播放
     */
    public static void doRest(){
        mediaPlayer.reset();

    }

    /**
     * 停止播放
     */
    public static void doStop(){
        while (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
