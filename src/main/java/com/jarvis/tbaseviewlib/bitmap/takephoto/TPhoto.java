package com.jarvis.tbaseviewlib.bitmap.takephoto;

import android.content.Intent;

import com.jarvis.tbaseviewlib.bitmap.takephoto.model.TResult;
import com.jarvis.tbaseviewlib.bitmap.takephoto.utils.CompressConfig;
import com.jarvis.tbaseviewlib.bitmap.takephoto.utils.OptionConfig;

import java.io.File;

/**
 * 实现
 *
 * Created by tansheng on 2017/6/22.
 */

public interface TPhoto {
    /**
     * 多选图片
     * @param limt  多选数量
     */
    void onPickMuliple(int limt);

    /**
     * 从相册中选择图片
     */
    void onPickGallery();

    /**
     * 从照相机中选择图片
     */
    void onPickCamera(File file);

    /**
     * 处理拍照或从相册选择的图片或裁剪的结果
     * @param requestCode
     * @param resultCode
     * @param data
     */
    void onActivityResult(int requestCode, int resultCode, Intent data);

    /**
     * 设置TPhoto的配置
     * @param config
     */
    void setOptionConfig(OptionConfig config);

    /**
     * 设置压缩配置
     */
    void setCompressConfig(CompressConfig compress);

    /**
     * 选择照片后的回调
     */
     interface TPhotoResultCallBack{
        void onSuccess(TResult result);
        void onFail();
        void onCancle();
    }
}
