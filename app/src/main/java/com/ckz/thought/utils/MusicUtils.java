package com.ckz.thought.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by kaiser on 2015/10/29.
 * 本地音乐播放服务
 */
public class MusicUtils {

    private MediaPlayer mediaPlayer;

    private final String TAG=MusicUtils.class.getSimpleName();


    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }

    /**
     * 开始播放
     * @param resid 音频资源ID
     */
    public void doStart(Context context,Integer resid,boolean isLoop){
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
    public void doRest(){
        mediaPlayer.reset();

    }

    /**
     * 停止播放
     */
    public void doStop(){
        while (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * 暂停播放
     */
    public  void doPause(){
        if (mediaPlayer!=null){
            mediaPlayer.pause();
        }
    }
}
