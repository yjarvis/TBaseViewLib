package com.jarvis.tbaseviewlib.bitmap.takephoto.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import com.jarvis.tbaseviewlib.model.image.ImageAlbum;
import com.jarvis.tbaseviewlib.model.image.ImageItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 获取本机图片辅助工具类
 * Created by tansheng on 2017/6/23.
 */

public class TAlbumUtils {


    private static TAlbumUtils instance;
    private ContentResolver cr;
    private Context context;
    /**
     * 是否经创建了相册数据
     */
    private boolean isBuild=false;
    /**
     * 相册集数据
     */
//    private LinkedHashMap<String, TImageAlbum> albumsList;
    private ArrayList<ImageAlbum> albumsList = new ArrayList<>();
    /**
     * 相册中的图片缩略图(key:原图id,value:缩略图路径)
     */
    private HashMap<Integer, String> thumbnailList = new HashMap<>();

    public static TAlbumUtils of(Context context) {
        if (instance == null) {
            synchronized (TAlbumUtils.class) {
                if (instance == null) {
                    instance = new TAlbumUtils(context);
                }
            }
        }
        return instance;
    }

    private TAlbumUtils(Context context) {
        if (context != null) {
            init(context);
            //获取本地图片相册集
        }
    }

    private void init(Context context) {
        this.context = context;
        cr = context.getContentResolver();
    }

    /**
     * 获取相册集数据
     */
    private void getAlbumData() {
        if (isBuild){
            handler.sendEmptyMessage(0);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取图片缩略图数据
                getThumbnailData();
                // 构造相册索引
                String columns[] = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.PICASA_ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.TITLE, MediaStore.Images.Media.SIZE, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
                // 得到一个游标
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
                int lastPosition = cur.getCount() - 1;
                if (cur.moveToLast()) {
                    // 获取指定列的索引
                    int photoIDIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                    int photoPathIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    int photoNameIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                    int photoTitleIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                    int photoSizeIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
                    int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                    int bucketIdIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
                    int picasaIdIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.PICASA_ID);
                    // 获取图片总数
                    int totalNum = cur.getCount();
                    // 图片id
                    int _id;
                    // 图片名称
                    String name;
                    // 图片路径
                    String path;
                    // 图片title
                    String title;
                    // 图片大小
                    String size;
                    // 图册名
                    String albumName;
                    // 图册id
                    String bucketId;
                    String picasaId;

                    do {
                        // 图片id
                        _id = cur.getInt(photoIDIndex);
                        // 图片名称
                        name = cur.getString(photoNameIndex);
                        // 图片路径
                        path = cur.getString(photoPathIndex);
                        // 图片title
                        title = cur.getString(photoTitleIndex);
                        // 图片大小
                        size = cur.getString(photoSizeIndex);
                        // 图册名
                        albumName = cur.getString(bucketDisplayNameIndex);
                        // 图册id
                        bucketId = cur.getString(bucketIdIndex);
                        picasaId = cur.getString(picasaIdIndex);

                        // 判断是否已经存在此图册，如果存在，则直接将此图册中的图片加入
//                TImageAlbum album = albumsList.get(bucketId);
                        boolean isHave = false;
                        ImageAlbum album = null;
                        for (int i = 0; i < albumsList.size(); i++) {
                            if (albumsList.get(i).getId().equals(bucketId)) {
                                isHave = true;
                                album = albumsList.get(i);
                                break;
                            }
                        }

                        if (!isHave) {
                            // 如果不存在图册，则先创建图册
                            album = new ImageAlbum();
                            // 图册中的图片列表
                            album.imageList = new ArrayList<>();
                            // 图册名
                            album.setAlbumName(albumName);
                            //图册id
                            album.setId(bucketId);
                            // 加入此图册
                            albumsList.add(album);
                        }
                        // 获取每个图片的详细数据
                        ImageItem imageItem = new ImageItem();
                        // 图片id
                        imageItem.setImageId(String.valueOf(_id));
                        // 原始图片路径
                        imageItem.setImagePath(path);
                        // 缩略图路径
                        imageItem.setThumbnailPath(thumbnailList.get(_id));
                        // 添加此图片到图册中
                        album.imageList.add(imageItem);
                        //当前图册中的图片数量+1
                        album.count++;
                        lastPosition -= 1;
                    } while (cur.moveToPosition(lastPosition));
                }
                isBuild=true;
                handler.sendEmptyMessage(0);
            }
        }).start();
    }


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (listener != null) {
                listener.onAlbumData(albumsList);
            }
        }
    };

    /**
     * 获取图片缩略图
     */
    private void getThumbnailData() {
        String[] projection = {MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID, MediaStore.Images.Thumbnails.DATA};
        Cursor cur = cr.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
        // 上一次的查询的位置
        int lastPosition = cur.getCount() - 1;
        // 缩略图与原图关联的id
        int image_idColumn;
        // 缩略图的路径
        int dataColumn;
        // 倒序查询，保证最新的照片在最前面
        if (cur.moveToLast()) {
//            int _id;// 缩略图id
//            int image_id;// 缩略图与原图关联的id
//            String image_path;// 缩略图的路径

//            int _idColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails._ID);
            image_idColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
            dataColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.DATA);

            do {
                // 获取对应的数据
//                _id = cur.getInt(_idColumn);
//                image_id = cur.getInt(image_idColumn);
//                image_path = cur.getString(dataColumn);
                thumbnailList.put(cur.getInt(image_idColumn), cur.getString(dataColumn));
                lastPosition -= 1;
            } while (cur.moveToPosition(lastPosition));
        }
    }

    private OnAlbumDataListener listener;

    public interface OnAlbumDataListener {
        void onAlbumData(ArrayList<ImageAlbum> data);
    }
    public TAlbumUtils setOnAlbumDataListener(OnAlbumDataListener listener) {
        this.listener = listener;
        return instance;
    }


    public TAlbumUtils build(){
        getAlbumData();
        return instance;
    }

    /**
     * 获取本机中的全部相册数据
     * @return
     */
    public ArrayList<ImageAlbum> getAllData(){
        return albumsList;
    }
}
