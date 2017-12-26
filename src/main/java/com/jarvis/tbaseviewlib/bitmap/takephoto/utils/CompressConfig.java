package com.jarvis.tbaseviewlib.bitmap.takephoto.utils;

import com.jarvis.tbaseviewlib.bitmap.takephoto.model.TResult;

/**
 * Created by tansheng on 2017/7/27.
 */

public interface CompressConfig {
    void startCompress(TResult result,CompressCallBackListener listener);

    interface CompressCallBackListener{
        void cancleCompress(String e);
        void errorCompress(String e);
        void successCompress(TResult result);
    }
}
