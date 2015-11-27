package com.ckz.crawler.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

import com.ckz.crawler.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/25.
 * 共享工具包
 */
public class ShareUtils{

    private Context context;
    private Resources res;

    public ShareUtils(Context context,Resources res){
        this.context = context;
        this.res = res;
    }


    /**
     * 视频
     */
    public void shareVedio(){
        String type = "video/*";
        String filename = "/myVideo.mp4";
        String mediaPath = Environment.getExternalStorageDirectory() + filename;

        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);
        // Set the MIME type
        share.setType(type);
        // Create the URI from the media
        File media = new File(mediaPath);
        Uri uri = Uri.fromFile(media);
        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);
        // Broadcast the Intent.
        context.startActivity(Intent.createChooser(share, "Share to"));
    }


    /**
     * 分享ImageView组件的图片
     */
    public void shareImageViewModule(ImageView siv){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);

        /*View v = View.inflate(context, R.layout.nav_header_main,null);
        ImageView siv = (ImageView) v.findViewById(R.id.imageView);*/
        Drawable mDrawable = siv.getDrawable();
        Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                mBitmap, "Image Description", null);
        Uri uri = Uri.parse(path);

        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, res.getText(R.string.send_to)));
    }

    /**
     * 分享我个人信息
     */
    public void shareMyInformation(){
        View  v = View.inflate(context,R.layout.nav_header_main,null);
        ImageView siv = (ImageView) v.findViewById(R.id.imageView);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        Drawable mDrawable = siv.getDrawable();
        Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                mBitmap, "Image Description", null);
        Uri uri = Uri.parse(path);
        String text = "陈科肇/n/r18575612426@163.com";
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "陈科肇"));
    }


    /**
     * 发送分享文字
     * @param content
     */
    public void sendText(String content){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, res.getText(R.string.send_to)));
    }


    /**
     * 发送分享一段html文字
     * @param html
     */
    public void sendHtml(String html){
        if(html==null){
            html = "<p>This is the text shared.</p>";
        }
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/html");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(html));
        context.startActivity(Intent.createChooser(sharingIntent,res.getText(R.string.send_to)));
    }


    /**
     *分享文字和单张图片
     * @param text 文字
     * @param imageUri Uri
     */
    public void sharingMultiple(String text,Uri imageUri){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, res.getText(R.string.send_to)));
    }

    /**
     *分享文字和多张图片
     * @param text 文字
     * @param imageUris ArrayList<Uri>
     */
    public void sharingMultiple(String text,ArrayList<Uri> imageUris){
        Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, res.getText(R.string.send_to)));
    }


    /**
     * Returns the URI path to the Bitmap displayed in specified ImageView
     * 返回的URI路径ImageView指定的位图中显示
     * @param imageView
     * @return
     */
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }



    //分享多张图片
    /*ArrayList<Uri> imageUris = new ArrayList<Uri>();
    imageUris.add(imageUri1); // Add your image URIs here
    imageUris.add(imageUri2);

    Intent shareIntent = new Intent();
    shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
    shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
    shareIntent.setType("image*//*");
    startActivity(Intent.createChooser(shareIntent, "Share images to.."));*/


}
