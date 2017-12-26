package com.jarvis.tbaseviewlib.bitmap.GlideBitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jarvis.tbaseviewlib.R;
import com.jarvis.tbaseviewlib.data.CacheData;
import com.jarvis.tbaseviewlib.utils.TUtils;

/**
 * 基于glide封装的图片加载类
 * Created by tansheng on 2016/11/3.
 */
public class GlideHelp {
    private Context context;

    public GlideHelp(Context context) {
        this.context = context;
    }

    /**
     * 设置渐变的图片加载
     *
     * @param url
     * @param imageView
     */
    public void display(String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .crossFade()
                .thumbnail(0.8f)
                .centerCrop()
                .error(R.drawable.empty_photo)
                .placeholder(R.drawable.empty_photo)
                .into(imageView);
    }

    /**
     * 设置渐变的图片加载
     *
     * @param url
     * @param imageView
     * @param errImg    下载失败的占位图
     * @param placeImg  下载过程中的占位图
     */
    public void display(String url, ImageView imageView, int errImg, int placeImg) {
        Glide.with(context)
                .load(url)
                .crossFade()
                .thumbnail(0.8f)
                .centerCrop()
                .error(errImg)
                .placeholder(placeImg)
                .into(imageView);
    }

    /**
     * 设置渐变的图片加载
     *
     * @param url
     * @param imageView
     * @param width     加载指定宽
     * @param height    加载指定高
     * @param placeImg  下载过程中的占位图
     */
    public void display(String url, ImageView imageView, int width, int height, int placeImg) {
        Glide.with(context)
                .load(url)
                .crossFade()
                .thumbnail(0.8f)
                .override(width, height)
                .centerCrop()
                .error(R.drawable.empty_photo)
                .placeholder(placeImg)
                .into(imageView);
    }

    /**
     * 设置渐变的图片加载
     *
     * @param url
     * @param imageView
     * @param diskCacheStrategy 缓存方式
     *                          DiskCacheStrategy.NONE 什么都不缓存，就像刚讨论的那样
     *                          DiskCacheStrategy.SOURCE 仅仅只缓存原来的全分辨率的图像
     *                          DiskCacheStrategy.RESULT 仅仅缓存最终的图像，即，降低分辨率后的（或者是转换后的）
     *                          DiskCacheStrategy.ALL 缓存所有版本的图像（默认行为）
     */
    public void display(String url, ImageView imageView, DiskCacheStrategy diskCacheStrategy) {
        Glide.with(context)
                .load(url)
                .crossFade()
                .thumbnail(0.8f)
                .centerCrop()
                .error(R.drawable.empty_photo)
                .placeholder(R.drawable.empty_photo)
                .diskCacheStrategy(diskCacheStrategy)
                .into(imageView);
    }

   private Bitmap bitmap=null;

    /**
     * 得到下载的图片的Bitmap
     * @param url
     * @param imageView
     */
    public Bitmap display(String url, final ImageView imageView, BaseTarget baseTarget) {
        bitmap=null;
        if (baseTarget == null) {
            SimpleTarget simpleTarget = new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bmp, GlideAnimation<? super Bitmap> glideAnimation) {
                    if(null!=imageView){
                        imageView.setImageBitmap(bmp);
                        bitmap = bmp;
                    }
                }
            };
            Glide.with(context)
                    .load(url)
                    .crossFade()
                    .thumbnail(0.8f)
                    .centerCrop()
                    .error(R.drawable.empty_photo)
                    .placeholder(R.drawable.empty_photo)
                    .into(simpleTarget);
        }else {
            Glide.with(context)
                    .load(url)
                    .crossFade()
                    .thumbnail(0.8f)
                    .centerCrop()
                    .error(R.drawable.empty_photo)
                    .placeholder(R.drawable.empty_photo)
                    .into(baseTarget);
        }

        return bitmap;
    }

    /**
     * 清空缓存
     */
    public void clearCache(){
        try {
            //如果是主线程就创建一个子线程去清除
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到已经缓存的大小
     * @param path 得到指定路径的缓存大小，默认为图片缓存路径
     * @return
     */
    public String getCacheSize(String path){
        try {
            if (TextUtils.isEmpty(path)){
                return TUtils.getFormatSize(TUtils.getCacheSize(CacheData.getImagesCache()));
            }else {
                return TUtils.getFormatSize(TUtils.getCacheSize(path));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
