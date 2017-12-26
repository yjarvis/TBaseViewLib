package com.jarvis.tbaseviewlib.bitmap.takephoto;

import com.jarvis.tbaseviewlib.model.image.ImageItem;

import java.util.ArrayList;

/**
 * Created by tansheng on 2017/6/23.
 */

public class Constant {
    /**
     * 选择相册后返回标记
     */
    public static final int CODE_CHOICE_PHOTO = 1911;
    public static final String CODE_RESULT_PHOTO="choicePhoto";
    /**
     * 选择拍照后返回标记
     */
    public static final int CODE_CHOICE_CAMERA = 1912;
    /**
     * 可选图片的最大值
     */
    public static String MAX_SELECTE="max_select_image";


    /**
     * 配置参数传递key
     */
    public static String PHOTO_OPTION_CONFIG="photo_config";


    /**
     * 上一次选择的图片数据集合(不包括拍照的图片)
     */
    private static ArrayList<ImageItem> lastSelectList;
    public static ArrayList<ImageItem> getLastSelectList() {
        return lastSelectList;
    }
    public static void setLastSelectList(ArrayList<ImageItem> lastSelectList) {
        Constant.lastSelectList = lastSelectList;
    }
}
