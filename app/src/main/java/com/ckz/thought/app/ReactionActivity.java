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

        ImageView iv = (ImageView) findViewById(R.id.ivxx);

        Animation translateAnimation = AnimationUtils.loadAnimation(this,R.anim.translate);
        iv.setAnimation(translateAnimation);



    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
