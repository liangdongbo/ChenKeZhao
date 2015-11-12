package com.ckz.thought.app;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ckz.thought.R;

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


        TextView tv = (TextView) findViewById(R.id.xx);

        im = (ImageView) findViewById(R.id.ivxx);
        //加载xml动画文件并将其设置到指定的View上

        //透明
        Animation alphaAnimation = AnimationUtils.loadAnimation(this,R.anim.tween_alpha);
        tv.setAnimation(alphaAnimation);

        //动画集合
        /*AnimationSet animationSet = (AnimationSet) AnimationUtils.loadAnimation(this,R.anim.tween_animset);
        im.setAnimation(animationSet);*/


    }

    @Override
    protected void onStart() {
        super.onStart();

        //帧动画
        //第一步将animation-list设置为ImageView的背景

        im.setBackgroundResource(R.drawable.framelist);

        //第二步获取ImagView的背景并将其转换成AnimationDrawable

        /*提示：
        有一点需要强调的是：启动Frame动画的代码animationDrawable.start();不能应用在OnCreate()方法中，
        因为在OnCreate()中AnimationDrawable还没有完全的与ImageView绑定。在OnCreate()中启动动画，只能看到第一张图片*/
        AnimationDrawable animationDrawable = (AnimationDrawable) im.getBackground();
        //第三步开始播放动画

        animationDrawable.start();
    }
}
