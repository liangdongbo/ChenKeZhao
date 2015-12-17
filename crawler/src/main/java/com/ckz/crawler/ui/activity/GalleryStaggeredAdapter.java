package com.ckz.crawler.ui.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import com.ckz.crawler.R;
import com.ckz.crawler.utils.BitmapUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class GalleryStaggeredAdapter extends
        RecyclerView.Adapter<GalleryStaggeredAdapter.MyViewHolder> {

    private File baseFile = new File(Environment.getExternalStorageDirectory().getPath()+"/chenkezhao/");
    private List<String> paths;
    private LayoutInflater mInflater;
    private List<Integer> mHeights;
    private BitmapUtils bitmapUtils;
    private Context context;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public GalleryStaggeredAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        bitmapUtils = new BitmapUtils();
        paths = imagePath(baseFile);

        mHeights = new ArrayList<Integer>();
        for (int i = 0; i < paths.size(); i++) {
            mHeights.add((int) (200 + Math.random() * 300));
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(mInflater.inflate(
                R.layout.item_gallery, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        LayoutParams lp = holder.imageView.getLayoutParams();
        lp.height = mHeights.get(position);
        holder.imageView.setLayoutParams(lp);
        try {
            holder.imageView.setImageBitmap(buildThum(holder.imageView, position));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                    View layout = View.inflate(context,R.layout.dialog_gallery,null);
                    ImageView imageView = (ImageView) layout.findViewById(R.id.gallery_select_item);
                    Bitmap bitmap = bitmapUtils.compressBitmapFromFile(paths.get(pos), imageView);
                    imageView.setImageBitmap(bitmap);
                    new AlertDialog.Builder(context).setTitle("图片展示").setView(layout)
                           // .setPositiveButton("确定", null)
                            .setNegativeButton("关闭", null).show();

                }
            });

            holder.itemView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    //提示消息
                    final Snackbar snackbar = Snackbar.make(holder.itemView, "确定要删除？", Snackbar.LENGTH_LONG);
                    snackbar.setAction("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            paths.remove(position);
                            notifyItemRemoved(position);
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    class MyViewHolder extends ViewHolder {

        ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.id_num);

        }
    }

    /**
     * 获取图片地址列表
     * @param file
     * @return
     */
    private static ArrayList<String> imagePath(File file) {
        ArrayList<String> list = new ArrayList<String>();
        File[] files = file.listFiles();
        for (File f : files) {
            list.add(f.getAbsolutePath());
        }
        Collections.sort(list);
        return list;
    }


    /**
     * 读取sdcard文件夹中的图片，并生成略缩图
     * @return
     * @throws FileNotFoundException
     */
    private Bitmap buildThum(ImageView imageView,int position) throws FileNotFoundException {
        // 使用TreeMap，排序问题就不需要纠结了
        Bitmap bitmap = null;
        if (baseFile != null && baseFile.exists()) {
            if (!paths.isEmpty()) {
                bitmap = bitmapUtils.compressBitmapFromFile(paths.get(position), imageView);
            }
        }
        return bitmap;
    }


}