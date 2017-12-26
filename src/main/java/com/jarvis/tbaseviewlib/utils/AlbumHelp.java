package com.jarvis.tbaseviewlib.utils;

import android.content.Context;

/**
 * 此图片辅助类继承图片管理工具类，并实现单列模式，如果有多个入口进入图片选择，则只需按本类创建多个此类即可
 * 作者: tansheng on 2016/1/21.
 * QQ:717549357
 */
public class AlbumHelp extends AlbumManager {

    private AlbumHelp instance;

    public AlbumHelp getInstance(Context c){
        if (null==instance){
            instance=new AlbumHelp(c);
        }
        return  instance;
    }

    private AlbumHelp(Context context) {
        super(context);
    }
}
