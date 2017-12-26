package com.jarvis.tbaseviewlib.bitmap.takephoto.utils;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.jarvis.tbaseviewlib.bitmap.takephoto.Constant;
import com.jarvis.tbaseviewlib.ui.activity.ChoiceImageListActivity;

/**
 * Created by tansheng on 2017/6/30.
 */

public class TIntentUtils {

    /***
     * 跳转到相册
     * @param contextUtils
     * @param limt   图片最大可选择数
     * @param config  配置参数
     * @return
     */
    public static Intent intentToPhoto(TContextUtils contextUtils,int limt,OptionConfig config){
        Intent intent=new Intent(contextUtils.getActivity(), ChoiceImageListActivity.class);
        intent.putExtra(Constant.MAX_SELECTE,limt);
        intent.putExtra(Constant.PHOTO_OPTION_CONFIG,config);
        return intent;
    }


    /**
     * 获取拍照的Intent
     * @return
     */
    public static Intent intentToCamera(Uri outPutUri) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);//将拍取的照片保存到指定URI
        return intent;
    }



}
