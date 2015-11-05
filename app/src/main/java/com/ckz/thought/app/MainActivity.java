package com.ckz.thought.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ckz.thought.R;
import com.ckz.thought.service.LocalhostMusicService;
import com.ckz.thought.service.system.BackMusicService;

/**
 * 程序的主页面
 * 菜单功能跳转页
 */
public class MainActivity extends AppCompatActivity {

    private static Intent backMusic = null;

    private static final String TAG = "MainActivity";
    /**
     * 自定义单击事件监听类
     * 内部类
     */
    private class MainClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.app_main_menu_1:
                    Toast.makeText(MainActivity.this,"你单击了 GO... 按钮",Toast.LENGTH_LONG).show();
                    Intent goIntent = new Intent(MainActivity.this,GoActivity.class);
                    startActivity(goIntent);
                    break;
                case R.id.app_main_menu_2:
                    Toast.makeText(MainActivity.this, "你单击了 敢吗？ 按钮", Toast.LENGTH_SHORT).show();
                    Intent memoryIntent = new Intent(MainActivity.this,MemoryActivity.class);
                    startActivity(memoryIntent);
                    break;
                case R.id.app_main_menu_3:
                    Toast.makeText(MainActivity.this, "你单击了 无视你 按钮", Toast.LENGTH_SHORT).show();
                    Intent reactionIntent = new Intent(MainActivity.this,ReactionActivity.class);
                    startActivity(reactionIntent);
                    break;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate...");

        //按钮单击事件监听
        Button btn_menu1 = (Button) findViewById(R.id.app_main_menu_1);
        LinearLayout btn_menu2 = (LinearLayout) findViewById(R.id.app_main_menu_2);
        Button btn_menu3 = (Button) findViewById(R.id.app_main_menu_3);
        btn_menu1.setOnClickListener(new MainClickListener());
        btn_menu2.setOnClickListener(new MainClickListener());
        btn_menu3.setOnClickListener(new MainClickListener());

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart...");
        LocalhostMusicService.doStart(MainActivity.this, R.raw.back_music, true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop...");
        new Thread(){
            @Override
            public void run() {
                if(LocalhostMusicService.mediaPlayer!=null){
                    if(!LocalhostMusicService.mediaPlayer.isLooping()){
                        while (LocalhostMusicService.mediaPlayer.isPlaying()){
                        }
                        LocalhostMusicService.doStop();
                    }else{
                        LocalhostMusicService.doStop();
                    }
                }
            }
        }.start();
    }
}








