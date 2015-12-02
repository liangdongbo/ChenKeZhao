package com.ckz.demo.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kaiser on 2015/10/31.
 * 位图处理工具
 */
public class BitmapUtils {


    private static final String TAG = BitmapUtils.class.getSimpleName();

    /**
     * 对图片进行压缩，传入的宽和高，计算出合适的inSampleSize值
     * @param options BitmapFactory.Options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 生成缩略图
     * @param bitmap
     * @param view
     * @return
     */
    public Bitmap compressBitmapFromBitmap(Bitmap bitmap,View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        int width = params.width;
        int height = params.height;
        return ThumbnailUtils.extractThumbnail(bitmap,width,height);
    }


    /**
     * 先解析得到合适的大小，再进行处理显示
     * @param res
     * @param resId
     * @param view 对象，用于取出view的width,height
     * @return
     */
    public Bitmap compressBitmapFromResource(Resources res, int resId,View view) {
        //获取组件布局参数
        ViewGroup.LayoutParams params = view.getLayoutParams();
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, params.width, params.height);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

}
