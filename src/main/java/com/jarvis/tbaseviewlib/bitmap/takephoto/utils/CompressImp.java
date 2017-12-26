package com.jarvis.tbaseviewlib.bitmap.takephoto.utils;

import com.jarvis.tbaseviewlib.bitmap.BitmapCompress;
import com.jarvis.tbaseviewlib.bitmap.takephoto.model.TResult;
import com.jarvis.tbaseviewlib.model.image.ImageItem;

import java.io.IOException;

/**
 * Created by tansheng on 2017/7/27.
 */

public class CompressImp implements CompressConfig {

    @Override
    public void startCompress(TResult result, CompressCallBackListener listener) {
        //        for (int i = 0; i < result.getImages().size(); i++) {
//            try {
//                BitmapCompress.CompressImageWightAndSize(result.getImages().get(i).getImagePath(),100);
//            } catch (IOException e) {
//                e.printStackTrace();
//                errorCompress();
//            }
//        }

        for (ImageItem iamgeItem:result.getImages()){
            try {
                iamgeItem.setThumbnailPath(BitmapCompress.CompressImageWightAndSize(iamgeItem.getImagePath(),100));
            } catch (IOException e) {
                e.printStackTrace();
                listener.errorCompress(e.toString());
            }
        }
        listener.successCompress(result);
    }
}
