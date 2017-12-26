package com.jarvis.tbaseviewlib.bitmap.GlideBitmap;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;
import com.jarvis.tbaseviewlib.data.CacheData;

/**
 * 自定义Glide缓存路径
 * 注:必须在AndroidManifest.xml中声明，否则无效
 *  <meta-data
    android:name="com.jarvis.tbaseviewlib.bitmap.GlideBitmap.GlideCachePath"
    android:value="GlideModule" />
 * Created by tansheng on 2016/11/3.
 */
public class GlideCachePath implements GlideModule {
    /**缓存大小*/
    private int cacheSize=150 * 1024 * 1024;
    /**缓存路径*/
    private String cachePath= CacheData.getImagesCache();
    @Override
    public void applyOptions(Context context, GlideBuilder glideBuilder) {
        //设置缓存路径和大小
        glideBuilder.setDiskCache(new DiskLruCacheFactory(cachePath,cacheSize));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public String getCachePath() {
        return cachePath;
    }

    public void setCachePath(String cachePath) {
        this.cachePath = cachePath;
    }
}
