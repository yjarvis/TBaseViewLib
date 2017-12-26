package com.jarvis.tbaseviewlib.data;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;


/**
 * 各种文件路径
 */
public class CacheData {
    /**
     * 是否测试模式
     */
    public static boolean isDeBug = true;

    /**
     * 已运行过的所有activity
     */
    public static ArrayList<Activity> activityList = new ArrayList<Activity>();
    private static String imagePath="TBaseView";
    private static String infoPath="TBaseView";
    private static String apkPath="TBaseView";
    /**
     * 图片缓存路径
     */
    private static  String IMAGES_CACHE = Environment.getExternalStorageDirectory().getPath() + "/"+imagePath+"/cache/images/";
    /**
     * 系统错误日志保存路径
     */
    private static  String INFO_PATH = Environment.getExternalStorageDirectory().getPath() + "/"+infoPath+"/info/";
    /**
     * apk安装包保存路径
     */
    private static  String APK_PATH = Environment.getExternalStorageDirectory().getPath() + "/"+apkPath+"/app/";


    /**
     * 缓存在应用下，删除时，缓存也将删除
     *
     * @param context
     * @return
     */
    public String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            //获取sd下/sdcard/Android/data/<application package>/cache这个路径
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            //获取/data/data/<application package>/cache 这个路径
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    /**
     *  创建文件夹
     *  @param pathName 文件夹名称，不传值，则默认以包名创建根目录
     */
    public static void buildCache(Context context,String pathName) {
        if(!TextUtils.isEmpty(pathName)){
            setImagesCache(pathName);
            setInfoPath(pathName);
            setApkPath(pathName);
        }else {
            //不传值，则默认以包名创建根目录
            String packageName=context.getPackageName();
            setImagesCache(packageName);
            setInfoPath(packageName);
            setApkPath(packageName);
        }
        // 新建文件夹
        File file1 = new File(APK_PATH);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        File file2 = new File(IMAGES_CACHE);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        File file3 = new File(INFO_PATH);
        if (!file3.exists()) {
            file3.mkdirs();
        }
    }

    public static void setInfoPath(String infoPath) {
        INFO_PATH = Environment.getExternalStorageDirectory().getPath() + "/"+infoPath+"/info/";
    }

    public static void setImagesCache(String imagesCache) {
        IMAGES_CACHE = Environment.getExternalStorageDirectory().getPath() + "/"+imagesCache+"/cache/images/";
    }

    public static void setApkPath(String apkPath) {
        APK_PATH = Environment.getExternalStorageDirectory().getPath() + "/"+apkPath+"/apk/";
    }

    public static String getImagesCache() {
        File file2 = new File(IMAGES_CACHE);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        return IMAGES_CACHE;
    }

    public static String getInfoPath() {
        File file3 = new File(INFO_PATH);
        if (!file3.exists()) {
            file3.mkdirs();
        }
        return INFO_PATH;
    }

    public static String getApkPath() {
        File file1 = new File(APK_PATH);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        return APK_PATH;
    }
}
