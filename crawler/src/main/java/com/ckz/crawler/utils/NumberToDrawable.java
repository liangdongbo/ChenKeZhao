package com.ckz.crawler.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.ckz.crawler.R;

/**
 * Created by ckz on 2015/12/12.
 * 陈科肇
 * 数字转化为图片对象
 */
public class NumberToDrawable {

    private Context context;

    public NumberToDrawable(Context context){
        this.context=context;
    }

    /** 功能简述:画出小红点
     *  功能详细描述:
     *  注意:
     * @param number 数字
     */
    public Drawable getDrawable(int number) {
        Drawable mCounterDrawable = null;
        Bitmap bitmapDrawable = null;

        if (mCounterDrawable == null) {
            // 初始化画布
            mCounterDrawable = new BitmapDrawable(context.getResources(), Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888) );
            Bitmap bitmapDrawables = ((BitmapDrawable) mCounterDrawable).getBitmap();
            int bitmapX = bitmapDrawables.getWidth();
            int bitmapY = bitmapDrawables.getHeight();
            bitmapDrawable = Bitmap.createBitmap(bitmapX, bitmapY, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmapDrawable);

            // 拷贝图片
            Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setDither(true);// 防抖动
            mPaint.setFilterBitmap(true);// 用来对Bitmap进行滤波处理，这样，当你选择Drawable时，会有抗锯
            Rect src = new Rect(0, 0, bitmapX, bitmapX);
            Rect dst = new Rect(0, 0, bitmapX, bitmapX);
            canvas.drawBitmap(((BitmapDrawable) mCounterDrawable).getBitmap(), src, dst, mPaint);

            //            canvas.drawBitmap(bitmapDrawable, bitmapX, bitmapY, mPaint);
            //画数字
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setTypeface(Typeface.DEFAULT_BOLD);
            mPaint.setColor(Color.WHITE);
            mPaint.setTextSize(100);
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();

            canvas.drawText(String.valueOf(number), 56, 56 + (fontMetrics.bottom - fontMetrics.top) / 4, mPaint);
            canvas.save();
        }
        return new BitmapDrawable(context.getResources(), bitmapDrawable);
    }
}
