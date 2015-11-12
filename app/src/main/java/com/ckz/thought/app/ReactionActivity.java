package com.ckz.thought.app;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reaction);


        TextView tv = (TextView) findViewById(R.id.xx);

        ImageView im = (ImageView) findViewById(R.id.ivxx);
        //加载xml动画文件并将其设置到指定的View上

        //透明
        Animation alphaAnimation = AnimationUtils.loadAnimation(this,R.anim.tween_alpha);
        tv.setAnimation(alphaAnimation);

        //动画集合
        AnimationSet animationSet = (AnimationSet) AnimationUtils.loadAnimation(this,R.anim.tween_animset);
        im.setAnimation(animationSet);
    }

}
