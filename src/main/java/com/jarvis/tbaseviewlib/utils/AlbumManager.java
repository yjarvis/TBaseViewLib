package com.jarvis.tbaseviewlib.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.text.TextUtils;

import com.jarvis.tbaseviewlib.model.image.ImageAlbum;
import com.jarvis.tbaseviewlib.model.image.ImageItem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 查询本机所有图片帮助类（单例模式）
 */
@SuppressWarnings("unused")
public class AlbumManager {
    /**可选择照片的最大数量*/
    private  int maxSelectNumber=6;
    private ContentResolver cr;
    private Context context;
    /**
     * 当有多个选择入口时可设置标记，以便于获得不同的数据
     */
    private  int flag = 1;
    /**
     * 缩略图列表(key:原始图id,value:缩略图路径)
     */
    private LinkedHashMap<String, String> thumbnailList = new LinkedHashMap<String, String>();
    /**
     * 图册列表(key:图册id,value：图册详细数据，包含图册中的所有图片详情)
     */
    private LinkedHashMap<String, ImageAlbum> albumsList = new LinkedHashMap<String, ImageAlbum>();
    /**
     * 本机中所有图册数据
     */
    private  LinkedHashMap<Object, ArrayList<ImageAlbum>> allDataList = new LinkedHashMap<Object, ArrayList<ImageAlbum>>();
    /**
     * 临时缓存已经操作过的图册数据
     */
    private  HashMap<Object, ArrayList<ImageAlbum>> allDataTempList = new HashMap<Object, ArrayList<ImageAlbum>>();
    /**
     * 临时缓存已经操作过的图册数据
     */
    private  HashMap<Object, Collection<ImageItem>> selectImageItemTempList = new HashMap<Object, Collection<ImageItem>>();
    /**
     * 选中的图片的数据(原始路径)
     */
    private  LinkedHashMap<String, ImageItem> selectData = new LinkedHashMap<String, ImageItem>();

    public AlbumManager(Context context) {
        buildAlbums(context);
    }

    /**
     * 获取选择的图片数据 功能说明:
     *
     * @return
     * @author 作者：jarvisT
     * @date 创建日期：2015-10-15 上午2:35:20
     */
    public  ArrayList<String> getSelectData() {
        ArrayList<String> newData = new ArrayList<String>();
        Collection<ImageItem> values = selectData.values();
        for (ImageItem data : values) {
            newData.add(data.imagePath);
        }
        return newData;
    }

    /**
     * 放入选择的图片数据
     *
     * @param key
     * @param data
     * @author tansheng QQ:717549357
     * @date 2015-11-27 下午2:47:09
     */
    public  void putSelectData(String key, ImageItem data) {

        if (data==null){
            data=new ImageItem();
            data.imagePath=key;
            data.thumbnailPath=key;
            data.isSelect=false;
            data.imageId="photo_"+key;
        }

        selectData.put(key, data);
    }

    /**
     * 删除某条选择的图片的数据
     */
    public  void removeSelectData(String key) {
        selectData.get(key).isSelect=false;
        selectData.remove(key);
    }


    /**
     * 获取本机所有图册已经图册中的图片
     */
    public ArrayList<ImageAlbum> buildAlbums(Context context) {
        // 如果已经存在数据就直接返回
        if (allDataList != null && allDataList.get(flag) != null && allDataList.get(flag).size() != 0) {
            return allDataList.get(flag);
        }

        init(context);

        ArrayList<ImageAlbum> imageAlbum = new ArrayList<ImageAlbum>();
        // 获取图册列表
        Iterator<Map.Entry<String, ImageAlbum>> itr = albumsList.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, ImageAlbum> entry = (Map.Entry<String, ImageAlbum>) itr.next();
            // 得到图册列表
            imageAlbum.add(entry.getValue());
        }
        allDataList.put(flag, imageAlbum);

        return allDataList.get(flag);
    }


    /**
     * 删除所有数据
     *
     * @author 作者：jarvisT
     * @date 创建日期：2015-7-31 上午12:32:05
     */
    public void removeAllData() {

        if (selectData != null && selectData.size() != 0) {
            selectData.clear();
        }
        if (allDataList != null && allDataList.size() != 0) {
            allDataList.clear();
        }
        if (allDataTempList != null && allDataTempList.size() != 0) {
            allDataTempList.clear();
        }
        flag = 1;

    }

    /**
     * 删除所有操作后的数据,在用户放弃选择图片后使用
     *
     * @author 作者：jarvisT
     * @date 创建日期：2015-7-31 上午12:32:05
     */
    public  void removeSelectDataAll() {
        if (selectData != null && selectData.size() != 0) {
            selectData.clear();
        }
    }


    /**
     * 判断文件是否存在
     *
     * @param path 文件路径
     * @return false不存在，true存在
     * @author tansheng QQ:717549357
     */
    public  boolean isHaveFile(String path) {

        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        return file.exists();
    }

    public  int getMaxSelectNumber() {
        return maxSelectNumber;
    }

    public  void setMaxSelectNumber(int maxSelectNumber) {
        maxSelectNumber = maxSelectNumber;
    }

    //------------------------------------------------以上为此类提供的操作方法-----------------------------------------------------------------------

    /**
     * 初始化,此时获取本机的所有图片
     *
     * @param context
     */
    private void init(Context context) {
        if (this.context == null) {
            this.context = context;
            cr = context.getContentResolver();
            // 获取图册已经图册中的图片
            getAlbumData();
        }
    }

    /**
     * 获取缩略图数据
     */

    private void getThumbnailData() {
        String[] projection = {Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA};
        Cursor cur = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
        // 上一次的查询的位置
        int lastPosition = cur.getCount() - 1;
        // 倒序查询，保证最新的照片在最前面
        if (cur.moveToLast()) {
            int _id;// 缩略图id
            int image_id;// 缩略图与原图关联的id
            String image_path;// 缩略图的路径

            int _idColumn = cur.getColumnIndex(Thumbnails._ID);
            int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
            int dataColumn = cur.getColumnIndex(Thumbnails.DATA);

            do {
                // 获取对应的数据
                _id = cur.getInt(_idColumn);
                image_id = cur.getInt(image_idColumn);
                image_path = cur.getString(dataColumn);
                thumbnailList.put(String.valueOf(image_id), image_path);
                lastPosition -= 1;
            } while (cur.moveToPosition(lastPosition));
        }
    }

    /**
     * 获取图册，并加入原始图片和缩略图的数据
     */
    private void getAlbumData() {

        // 构造缩略图索引
        getThumbnailData();
        // 构造相册索引
        String columns[] = new String[]{Media._ID, Media.BUCKET_ID, Media.PICASA_ID, Media.DATA, Media.DISPLAY_NAME, Media.TITLE, Media.SIZE, Media.BUCKET_DISPLAY_NAME};
        // 得到一个游标
        Cursor cur = cr.query(Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
        int lastPosition = cur.getCount() - 1;
        if (cur.moveToLast()) {
            // 获取指定列的索引
            int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
            int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
            int photoNameIndex = cur.getColumnIndexOrThrow(Media.DISPLAY_NAME);
            int photoTitleIndex = cur.getColumnIndexOrThrow(Media.TITLE);
            int photoSizeIndex = cur.getColumnIndexOrThrow(Media.SIZE);
            int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
            int bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID);
            int picasaIdIndex = cur.getColumnIndexOrThrow(Media.PICASA_ID);
            // 获取图片总数
            int totalNum = cur.getCount();

            do {
                // 图片id
                String _id = cur.getString(photoIDIndex);
                // 图片名称
                String name = cur.getString(photoNameIndex);
                // 图片路径
                String path = cur.getString(photoPathIndex);
                // 图片title
                String title = cur.getString(photoTitleIndex);
                // 图片大小
                String size = cur.getString(photoSizeIndex);
                // 图册名
                String albumName = cur.getString(bucketDisplayNameIndex);
                // 图册id
                String bucketId = cur.getString(bucketIdIndex);
                String picasaId = cur.getString(picasaIdIndex);

                // 判断是否已经存在此图册，如果存在，则直接将此图册中的图片加入
                ImageAlbum album = albumsList.get(bucketId);
                if (album == null) {
                    // 如果不存在图册，则先创建图册
                    album = new ImageAlbum();
                    // 图册中的图片列表
                    album.imageList = new ArrayList<ImageItem>();
                    // 图册名
                    album.albumName = albumName;
                    // 加入此图册
                    albumsList.put(bucketId, album);
                }
                // 获取每个图片的详细数据
                ImageItem imageItem = new ImageItem();
                // 图片id
                imageItem.imageId = _id;
                // 原始图片路径
                imageItem.imagePath = path;
                // 缩略图路径
                imageItem.thumbnailPath = thumbnailList.get(_id);
                // 添加此图片到图册中
                album.imageList.add(imageItem);
                album.count++;
                lastPosition -= 1;
            } while (cur.moveToPosition(lastPosition));
        }
    }


    /**
     * 拍照图片的路径
     */
    private  String fPath = "";

    /**
     * 调用拍照 功能说明:
     *
     * @param activity
     * @param imgPath     拍照生成的图片存放的文件夹名称
     * @param requestCode 返回标记
     * @author 作者：jarvisT
     * @date 创建日期：2015-10-15 上午12:50:49
     */
    public  Intent choiceCarmera(Activity activity, String imgPath, int requestCode, boolean isFragment, int flag) {
        // 图片文件名称
        String photoFileName = getFormatTimeByCustom(System.currentTimeMillis(), "yyyyMMdd_HHmmss");
        // 调用相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        flag = flag;
        // 判断SD卡是否可用
        if ((Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))) {
            File file = new File(imgPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            // 创建一个文件
            File tempFile = new File(imgPath + photoFileName + ".jpg");
            // 得到创建的文件的uri地址
            Uri uri = Uri.fromFile(tempFile);

            // 得到图片的路径
            fPath = tempFile.getAbsolutePath();

            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        }
        if (isFragment) {
            // Fragment中调用的情况下，需要在Fragment中调用startActivityForResult(intent,
            // requestCode);
            intent.putExtra("flag", flag);

            activity.setResult(flag, intent);
            return intent;
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
        return null;
    }

    /**
     * 获取拍照的图片路径 功能说明:
     *
     * @return
     * @author 作者：jarvisT
     * @date 创建日期：2015-10-15 上午3:04:39
     */
    public  String getPath() {
        if (TextUtils.isEmpty(fPath)) {
            return "";
        }
        return fPath;
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
    private  String getFormatTimeByCustom(long time, String format) {
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

}
