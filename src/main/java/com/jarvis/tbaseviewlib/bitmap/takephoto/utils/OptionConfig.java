package com.jarvis.tbaseviewlib.bitmap.takephoto.utils;

import java.io.Serializable;

/**
 * Created by tansheng on 2017/6/29.
 */

public class OptionConfig implements Serializable{
    /**
     * 是否缓存已经选过的图片，以便第二次打开时显示已选择
     */
    private boolean haveCache =true;

    public boolean isHaveCache() {
        return haveCache;
    }

    public OptionConfig setHaveCache(boolean haveCache) {
        this.haveCache = haveCache;
        return this;
    }
}
