package com.ckz.thought.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.ckz.thought.R;

/**
 * Created by kaiser on 2015/10/31.
 * 位图处理工具
 */
public class BitmapUtils {


    public static Bitmap xx(){
        /*updateBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        canvas = new Canvas(updateBitmap);
        paint = new Paint();
        final ColorMatrix cm = new ColorMatrix();
        //设置颜色5*4矩阵，范围0.0f~2.0f，1为保持原图的RGBA值
        cm.set(new float[] {
                2.0f, 0, 0, 0, 0,// R红色值，第一位
                0, 1, 0, 0, 0,// G绿色值，第二位
                0, 0, 1, 0, 0,// B蓝色值，第三位
                0, 0, 0, 1, 0 // A透明度，第四位
        });
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        final Matrix matrix = new Matrix();
        canvas.drawBitmap(bitmap, matrix, paint);
        imageView.setImageBitmap(updateBitmap);*/
        return null;
    }

    /**
     * 设置指定像素区域的颜色
     * 只有透明和白色的像素点是不修改的
     * @param bitmap 位图
     * @param startX 开始X轴的像素
     * @param startY 开始Y轴的像素
     * @param endX 结束X轴的像素
     * @param endY 结束Y轴的像素
     * @param setColor xml定义的颜色资源ID
     * @return 修改后的Bitmap
     */
    public static Bitmap setBitmapPixel(Bitmap bitmap,int startX,int startY,int endX,int endY,int setColor){
        //没必要每次都循环图片中的所有点，因为这样会比较耗时。
        int loopCount = startY * startX;
        //填充颜色的起始点 开始 到 终点
        for(int i=startX;i<endX;i++){
            for(int j=startY;j<endY;j++){
                //在这说明一下 如果color 是全透明 或者全黑 返回值为 0
                //getPixel()不带透明通道 getPixel32()才带透明部分 所以全透明是0x00000000
                //而不透明黑色是0xFF000000 如果不计算透明部分就都是0了
                int color = bitmap.getPixel(i,j);
                if (color != 0 && color != Color.WHITE) {
                    //int temp = Color.argb(argb_[0],argb_[1],argb_[2],argb_[3]);
                    //bitmap.setPixel(i, j, Color.BLACK);
                    bitmap.setPixel(i,j,setColor);
                }
                //还原图片颜色
                /*bitmap.setPixel(i,j,arrayColor[loopCount]);
                loopCount++;*/
            }
        }

        return bitmap;
    }

    /**
     * 获取位图的像素点
     * @param resources
     * @param resid
     * @return
     */
    public static int[] getBitmapPixel(Resources resources,int resid){
        Bitmap bitmap = BitmapUtils.getMutableBitmap(resources, resid);
        //处理图片的第个像素点
        int bitmapWidth = 0;
        int bitmapHeight = 0;
        int arrayColor[] = null;

        bitmapWidth = bitmap.getWidth();
        bitmapHeight = bitmap.getHeight();
        arrayColor = new int[bitmapHeight*bitmapWidth];
        //循环每一个像素点
        int count = 0;
        for(int i=0;i<bitmapWidth;i++){
            for(int j=0;j<bitmapHeight;j++){
                //获得Bitmap 图片中每一个点的color颜色值
                int color = bitmap.getPixel(i,j);
                //将颜色值存到数组里，方便修改
                arrayColor[count]=color;

                //如果你想做的更细致的话 可以把颜色值的R G B 拿到做响应的处理
                /*
                int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);
                */
                count++;
            }
        }
        return arrayColor;
    }

    /**
     * 九宫格图片截取
     * @param context 上下文
     * @param resid 九宫格图片ID
     * @return
     */
    public static Bitmap[] getSquaredUpNum(Context context,int resid){
        Bitmap resource = BitmapFactory.decodeResource(context.getResources(), resid);
        Bitmap one = Bitmap.createBitmap(resource, 0, 0, 90, 120);
        Bitmap two = Bitmap.createBitmap(resource, 90, 0, 207, 120);
        Bitmap three = Bitmap.createBitmap(resource, 207, 0, 326, 120);
        Bitmap four = Bitmap.createBitmap(resource, 326, 0, 438, 120);
        Bitmap five = Bitmap.createBitmap(resource, 438, 0, 540, 120);
        Bitmap six = Bitmap.createBitmap(resource, 0, 120, 90, 120);
        Bitmap seven = Bitmap.createBitmap(resource, 90, 120, 207, 120);
        Bitmap eight = Bitmap.createBitmap(resource, 207, 120, 326, 120);
        Bitmap nine = Bitmap.createBitmap(resource, 326, 120, 438, 120);
        Bitmap zero = Bitmap.createBitmap(resource, 438, 120, 540, 120);

        //封装成数组
        Bitmap[] bitmaps = new Bitmap[]{
                one,two,three,four,five,six,seven,eight,nine,zero
        };
        return bitmaps;
    }


    /**
     * 你正在试图修改的不可变的位图的像素为单位）。
     * 您不能修改的不可变的位图的像素。如果您尝试它将抛出 IllegalStateException。
     * 使用这个方法从资源获取可变位图
     * @param resources
     * @param resId
     * @return
     */
    public static Bitmap getMutableBitmap(Resources resources,int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        return BitmapFactory.decodeResource(resources, resId, options);
    }
}
