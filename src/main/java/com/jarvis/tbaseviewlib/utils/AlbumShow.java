package com.jarvis.tbaseviewlib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ImageView;

import com.jarvis.tbaseviewlib.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * 选择使用原始图片或者缩略图显示
 *
 * @author tansheng
 */
public class AlbumShow {
    private Handler handler = new Handler();
    /**
     * 记录已经存在的图片缓存
     */
    private HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();

    /**
     * 添加图片到软引用中
     */
    private void put(String path, Bitmap bmp) {
        if (!TextUtils.isEmpty(path) && bmp != null) {
            imageCache.put(path, new SoftReference<Bitmap>(bmp));
        }
    }

    /**
     * 显示缩略图或者压缩后的原始图
     */
    public void disPlayBitmap(final Context context, final ImageView imageView, final String sourcePath) {
        this.disPlayBitmap(context,imageView,null,sourcePath);
    }

    /**
     * 显示缩略图或者压缩后的原始图
     */
    public void disPlayBitmap(final Context context, final ImageView imageView, final String thumbPath, final String sourcePath) {
        if (TextUtils.isEmpty(thumbPath) && TextUtils.isEmpty(sourcePath)) {
            // 判断是否存在路径
            return;
        }
        final String path;

        if (!TextUtils.isEmpty(thumbPath)) {
            // 缩略图路径
            path = thumbPath;
        } else {
            // 原始图路径
            path = sourcePath;
        }

        // 如果软引用中保存有此图片，则进行返回
        if (imageCache.containsKey(path)) {
            // SoftReference是软引用，得到软引用后使用get()方法返回bitmap对象的强引用,此时,bitmap有可能被回收了，所以需要判空，若为null则重新添加到引用中
            Bitmap bitmap = imageCache.get(path).get();
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                return;
            }
        }

        imageView.setImageBitmap(null);

        // 如果软引用中不存在,则通过路径，去加载一张图片进去
        new Thread(new Runnable() {
            Bitmap bitmap;

            @Override
            public void run() {
                try {
                    if (!TextUtils.isEmpty(thumbPath)) {
                        // 缩略图路径
                        bitmap = BitmapFactory.decodeFile(thumbPath);
                        if (bitmap == null) {
                            // 原始图路径
                            bitmap = revitionImageSize(sourcePath);
                        }
                    } else {
                        // 原始图路径
                        bitmap = revitionImageSize(sourcePath);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (bitmap == null) {
                    // 如果此时还没有图片，就加载一张默认图
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_photo);
                }
                // 加入到软引用中
                put(path, bitmap);
                // 更新UI
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });

            }
        }).start();

    }

    /**
     * 压缩图片大小用于显示，防止OOM
     */
    private Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 不加载到内存中，此时bitmap返回null，但可以拿到此时图片的信息
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            // 图片宽高等比例缩放
            if ((options.outWidth >> i <= 200) && (options.outHeight >> i <= 300)) {
                in = new BufferedInputStream(new FileInputStream(new File(path)));
                // 计算压缩度
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inPreferredConfig= Bitmap.Config.RGB_565;
                options.inPurgeable = true;
                options.inInputShareable = true;
                // 加载到内存中
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

}
