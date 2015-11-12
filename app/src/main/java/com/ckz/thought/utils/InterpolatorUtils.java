package com.ckz.thought.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

/**
 * Created by kaiser on 2015/11/13.
 * 陈科肇
 * 自定义动画插入器
 * 可以设置你动画运动的轨迹
 */
public class InterpolatorUtils implements Interpolator {


    private float mCycles;

    /**
     * @param cycles 要重复的周期数
     */
    public InterpolatorUtils(float cycles) {
        mCycles = cycles;
    }

    public InterpolatorUtils(Context context, AttributeSet attrs) {
        TypedArray a =
                context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.CycleInterpolator);

        mCycles = a.getFloat(com.android.internal.R.styleable.CycleInterpolator_cycles, 1.0f);

        a.recycle();
    }

    @Override
    public float getInterpolation(float input) {
        return (float) (Math.sin(2 * mCycles * Math.PI * input));
    }
}
