package com.ckz.thought.app;

import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ckz.thought.R;
import com.ckz.thought.entity.AnimationView;

/**
 * Created by kaiser on 2015/10/27.
 * 菜单三
 * 提升你脑子的记忆力
 */
public class ReactionActivity extends AppCompatActivity{

    private ImageView im;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction);

        ImageView imgShow = (ImageView) findViewById(R.id.ivxx);

        //属性动画
        ObjectAnimator animator=ObjectAnimator.ofInt(new AnimationView(imgShow),"width", 10);
        animator.setDuration(3000);//设置动画持续的时间
        animator.setRepeatCount(10);//设置动画重复的次数
        animator.start();//开启动画

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
