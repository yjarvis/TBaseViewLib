package com.jarvis.tbaseviewlib.bitmap;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.jarvis.tbaseviewlib.data.CacheData;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 图片压缩辅助类
 *
 * @author tansheng QQ:717549357
 * @date 2015-11-26 下午4:26:10
 */
public class BitmapCompress {

    /* 方法一：尺寸压缩 */

    /**
     * 按尺寸压缩图片
     *
     * @param filePath 图片原始路径
     * @return 返回压缩后的Bitmap
     * @throws IOException
     * @author tansheng QQ:717549357
     * @date 2015-11-26 下午4:48:24
     */
    public static String CompressImageSize(String filePath) throws IOException {
        return CompressImageSize(filePath, 0, 0);
    }

    /**
     * 按尺寸压缩图片
     *
     * @param filePath 图片原始路径
     * @param maxWidth    压缩后的图片宽
     * @param maxHeigth   压缩后的图片高
     * @return 返回压缩后的Bitmap
     * @throws IOException
     * @author tansheng QQ:717549357
     * @date 2015-11-26 下午4:48:24
     */
    public static String CompressImageSize(String filePath, int maxWidth, int maxHeigth) throws IOException {
        if (maxWidth <= 0) {
            maxWidth = 500;
        }
        if (maxHeigth <= 0) {
            maxHeigth = 800;
        }


        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(filePath)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 不加载到内存中，此时bitmap返回null，但可以拿到此时图片的信息
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        File file = new File(filePath);
        if (options.outWidth < maxWidth || options.outHeight < maxHeigth) {
            // 如果图片宽高小于设置的压缩值，那么就不压缩图片
            in = new BufferedInputStream(new FileInputStream(file));
            // 加载到内存中
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(in, null, options);
        } else {
            while (true) {
                // 图片宽高等比例缩放
                if ((options.outWidth >> i <= maxWidth) || (options.outHeight >> i <= maxHeigth)) {
                    in = new BufferedInputStream(new FileInputStream(file));
                    // 计算压缩度
                    options.inSampleSize = (int) Math.pow(2.0D, i);
                    // 加载到内存中
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeStream(in, null, options);
                    break;
                }
                i += 1;
            }
        }
        // 压缩成功后，保存压缩后的图片
        saveBitmap(bitmap, CacheData.getImagesCache(), file.getName());
        return CacheData.getImagesCache() + file.getName();
    }




	/* 方法二：质量压缩 */

    /**
     * 质量+尺寸压缩图片，并保存到指定位置
     *
     * @param filePath 图片原始路径
     * @param maxKb    压缩后的图片最大占用内存(单位kb)
     * @return 返回压缩后图片的路径
     * @throws IOException
     */
    public static String CompressImageWight(String filePath, int maxKb) throws IOException {
        File file = new File(filePath);
        if (file.length()/1024 <= maxKb) {
            return filePath;
        }

        //如果图片内存大小大于可使用内存大小的一半，就先返回失败
        if (getBitmapMemory(filePath) > Runtime.getRuntime().maxMemory()/2) {
            return "";
        }
        if (maxKb<10) {
            maxKb=30;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 开始读入图片,加载到内存中，此时才能拿到真实的图片，如果把options.inJustDecodeBounds 设true则只能获得图片的一些基础信息
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeFile(filePath, options);// 此时返回bm
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        int quality = 70;
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > maxKb) {
            baos.reset();// 重置baos即清空baos
            quality -= 10;// 每次都减少10
            if (quality < 50) {
                bitmap = CompressImageSizeShow(filePath,960,1280);
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                break;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
//		ByteArrayInputStream in = new ByteArrayInputStream(baos.toByteArray());
//		Bitmap newBitmap = BitmapFactory.decodeStream(in, null, null);
        // 压缩成功后，保存压缩后的图片
        saveBitmap(baos, CacheData.getImagesCache(), file.getName());
        baos.close();
        // 回收图片，释放内存
        bitmap.recycle();
        bitmap=null;
        return CacheData.getImagesCache() + file.getName();
    }

	

	/* 方法三：质量+尺寸混合压缩 */
    /**
     * 质量+尺寸压缩图片，并保存到指定位置
     *
     * @param filePath 图片原始路径
     * @param maxKb    压缩后的图片最大占用内存(单位kb)
     * @return 返回压缩后图片的路径
     * @throws IOException
     */
    public static String CompressImageWightAndSize(String filePath,  int maxKb) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length()/1024;// 图片文件大小
        long size = getBitmapMemory(filePath);// 图片的占用内存大小
        Bitmap bitmap = null;
        //如果图片内存大小大于可使用内存大小的一半，就先压缩尺寸
        if (size > Runtime.getRuntime().maxMemory()/2) {
            bitmap = CompressImageSizeShow(filePath,600,1080);
        } else if(fileSize>maxKb){
            BitmapFactory.Options options = new BitmapFactory.Options();
            // 开始读入图片,加载到内存中，此时才能拿到真实的图片，如果把options.inJustDecodeBounds 设true则只能获得图片的一些基础信息
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            bitmap = BitmapFactory.decodeFile(filePath, options);// 此时返回bm
        }else if(fileSize<=maxKb ){
            //小于maxKb则不进行压缩
            return filePath;
        }
        if (maxKb<10) {
            maxKb=30;
        }

        //压缩图片
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        int quality = 80;
        // 循环判断如果压缩后图片是否大于maxKb,大于继续压缩
        while (baos.toByteArray().length / 1024 > maxKb) {
            baos.reset();// 重置baos
            quality -= 10;// 每次都减少10
            if (quality< 50){
                bitmap = CompressImageSizeShow(filePath,500,960);
                baos = new ByteArrayOutputStream();
                quality=70;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);// 这里压缩quality%，把压缩后的数据存放到baos中
        }
//		ByteArrayInputStream in = new ByteArrayInputStream(baos.toByteArray());
//		Bitmap newBitmap = BitmapFactory.decodeStream(in, null, null);
        // 压缩成功后，保存压缩后的图片
        saveBitmap(baos, CacheData.getImagesCache(), file.getName());
        baos.close();
        // 回收图片，释放内存
        bitmap.recycle();
        bitmap=null;
        System.gc();
        return CacheData.getImagesCache() + file.getName();
    }


//------------------------------------------------end--------------------------------------------------------------------------


    /**
     * 压缩图片并返回压缩后的路径，此方法优化压缩速度，对已经压缩过的图片不进行再次压缩
     *
     * @param picList    需要压缩的图片路径集合
     * @param uploadList 压缩后的图片路径集合
     * @return 返回压缩后的图片路径集合
     */
    public static ArrayList<String> CompressImageSizeList(ArrayList<String> picList, ArrayList<String> uploadList,int maxKb) {
        if (picList.size() == 0) {
            return picList;
        }
        boolean isZip = true;
        //临时保存压缩后的图片路径
        ArrayList<String> tmpList = new ArrayList<String>();
        for (int i = 0; i < picList.size(); i++) {
            for (int j = 0; j < uploadList.size(); j++) {
                //判断是否需要压缩
                if (picList.get(i).substring(picList.get(i).lastIndexOf(File.separator) + 1).
                        equals(uploadList.get(j).substring(uploadList.get(j).lastIndexOf(File.separator) + 1))) {
                    isZip = false;
                    tmpList.add(uploadList.get(i));
                }
            }
            String tmpPath = "";
            if (isZip) {
                try {
                    tmpPath = BitmapCompress.CompressImageWightAndSize(picList.get(i), maxKb);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tmpList.add(tmpPath);
            }
            isZip = true;
        }
        if (tmpList.size() > 0) {
            uploadList.clear();
            uploadList.addAll(tmpList);
        }

        return uploadList;
    }


    /**
     * 按尺寸压缩图片,此方法未保存压缩后的图片,只用于展示
     *
     * @param path 图片原始路径
     * @return 返回压缩后的Bitmap
     * @throws IOException
     * @author tansheng QQ:717549357
     * @date 2015-11-26 下午4:48:24
     */
    public static Bitmap CompressImageSizeShow(String path) throws IOException {
        return CompressImageSizeShow(path, 0, 0);
    }


    /**
     * 按尺寸压缩图片,此方法未保存压缩后的图片
     *
     * @param path   图片原始路径
     * @param width  压缩后的尺寸宽
     * @param height 压缩后的尺寸高
     * @return 返回压缩后的Bitmap
     * @throws IOException
     * @author tansheng QQ:717549357
     * @date 2015-11-26 下午4:48:24
     */
    public static Bitmap CompressImageSizeShow(String path, int width, int height) throws IOException {
        if (width <= 0) {
            width = 600;
        }
        if (height <= 0) {
            height = 1080;
        }
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 不加载到内存中，此时bitmap返回null，但可以拿到此时图片的信息
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        File file = new File(path);
        if (options.outWidth < width || options.outHeight < height) {
            // 如果图片宽高小于设置的压缩值，那么就不压缩图片
            in = new BufferedInputStream(new FileInputStream(file));
            // 加载到内存中
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(in, null, options);
        } else {
            while (true) {
                // 图片宽高等比例缩放
                if ((options.outWidth >> i <= width) || (options.outHeight >> i <= height)) {
                    in = new BufferedInputStream(new FileInputStream(file));
                    // 计算压缩度
                    options.inSampleSize = (int) Math.pow(2.0D, i);
                    // 加载到内存中
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeStream(in, null, options);
                    break;
                }
                i += 1;
            }
        }
        return bitmap;
    }

    /**
     * 保存图片
     *
     * @param bitmap       需要保存的原始图片
     * @param savePath     保存的位置
     * @param saveFileName 保存的图片名称
     * @return 返回图片保存位置
     * @author tansheng QQ:717549357
     * @date 2015-11-26 下午4:45:44
     */
    public static String saveBitmap(Bitmap bitmap, String savePath, String saveFileName) {
        // 图片文件名称
        if (TextUtils.isEmpty(saveFileName)) {
            saveFileName = getFormatTimeByCustom(System.currentTimeMillis(), "yyyyMMdd_HHmmss") + ".jpg";
        }
        File file = new File(savePath, saveFileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    /**
     * 保存图片
     *
     * @param baos         需要保存的图片流
     * @param savePath     保存的位置
     * @param saveFileName 保存的图片名称
     * @return 返回图片保存位置
     * @author tansheng QQ:717549357
     * @date 2015-11-26 下午4:45:44
     */
    public static String saveBitmap(ByteArrayOutputStream baos, String savePath, String saveFileName) {
        // 图片文件名称
        if (TextUtils.isEmpty(saveFileName)) {
            saveFileName = getFormatTimeByCustom(System.currentTimeMillis(), "yyyyMMdd_HHmmss") + ".jpg";
        }
        File file = new File(savePath, saveFileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(baos.toByteArray());
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }


    /**
     * 获得图片加入到内存中时所需占用的内存大小
     * @param filePath  图片文件路径
     * @return  返回占用内存大小
     */
    public static long getBitmapMemory(String filePath){
        long memory=0;
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 设true不加载到内存中，只能获得图片的一些基础信息
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);// 此时返回bm
        int a=1;
        //不同类型的图片单位像素所占用的字节数
        if (options.inPreferredConfig==Bitmap.Config.ALPHA_8){
            a=1;
        }
        if (options.inPreferredConfig==Bitmap.Config.RGB_565){
            a=2;
        }
        if (options.inPreferredConfig==Bitmap.Config.ARGB_4444){
            a=2;
        }
        if (options.inPreferredConfig==Bitmap.Config.ARGB_8888){
            a=4;
        }

        memory=options.outWidth*options.outHeight*a;

        return memory;
    }


    /**
     * 功能说明： 简单的时间格式
     * <p/>
     * 作者: jarvisT
     *
     * @param time   需要格式化的时间
     * @param format 可为null或者“” 默认格式化的模版（"MM-dd HH:mm"）
     * @return 返回模版化后的时间
     */
    @SuppressLint("SimpleDateFormat")
    private static String getFormatTimeByCustom(long time, String format) {
        if (0 == time) {
            return "";
        }
        SimpleDateFormat mDateFormat = null;
        if (!TextUtils.isEmpty(format)) {
            mDateFormat = new SimpleDateFormat(format);
        } else {
            mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        }
        return mDateFormat.format(new Date(time));

    }

//	/**
//	 * 判断文件是否存在
//	 * 
//	 * @param path
//	 *            文件路径
//	 * @return false:不存在，true:存在
//	 */
//	private static boolean isHaveFile(String path) {
//		if (TextUtils.isEmpty(path)) {
//			return false;
//		}
//
//		File file = new File(path);
//
//		return file.exists();
//
//	}
}
