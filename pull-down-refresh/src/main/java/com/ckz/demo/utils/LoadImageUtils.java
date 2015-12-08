package com.ckz.demo.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.ckz.demo.R;

/**
 * Created by ckz on 2015/12/2.
 * 图片加载工具包
 */
public class LoadImageUtils {
    /**
     * 利用Volley异步加载图片
     * 注意方法参数:
     * getImageListener(ImageView view, int defaultImageResId, int errorImageResId)
     * 第一个参数:显示图片的ImageView
     * 第二个参数:默认显示的图片资源
     * 第三个参数:加载错误时显示的图片资源
     */
    public void loadImageByVolley(RequestQueue requestQueue,ImageView mImageView, String imageUrl) {
        imageUrl = "http://avatar.csdn.net/6/6/D/1_lfdfhl.jpg";
        final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(20);
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public void putBitmap(String key, Bitmap value) {
                lruCache.put(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {
                return lruCache.get(key);
            }
        };
        ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(mImageView, R.drawable.ic_launcher, R.drawable.abc_ic_clear_mtrl_alpha);
        imageLoader.get(imageUrl, listener);
    }
}
