package com.ckz.thought.service.system;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 播放背景音乐
 */
public class BackMusicService extends Service {

    public BackMusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
