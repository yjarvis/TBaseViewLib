package com.jarvis.tbaseviewlib.bitmap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.jarvis.tbaseviewlib.R;
import com.jarvis.tbaseviewlib.data.CacheData;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;

import java.io.File;

/**
 * 功能说明:图片下载类
 * 
 * @author 作者：jarvisT
 */
public class BitmapHelp {

	private BitmapUtils bitmapUtils;
	private Context mContext;

	public BitmapHelp(Context context) {
		File file = new File(CacheData.getImagesCache());
		if (!file.exists()) {
			file.mkdirs();
		}
		mContext = context;
		// 初始化图片下载工具
		bitmapUtils = new BitmapUtils(context, CacheData.getImagesCache());
	}

	public BitmapHelp(String cachePath, Context context) {
		File file = new File(cachePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		mContext = context;
		// 初始化图片下载工具
		bitmapUtils = new BitmapUtils(context, cachePath);
	}

	/**
	 * 设置缓存地址
	 * 
	 * @author tansheng QQ:717549357
	 * @date 2015-11-27 下午3:51:05
	 * @param cachePath
	 */
	public void setCachePath(String cachePath) {
		if (bitmapUtils != null) {
			bitmapUtils = null;
		}
		bitmapUtils = new BitmapUtils(mContext, cachePath);
	}

	/**
	 * 带动画的下载方法
	 * 
	 * @author tansheng QQ:717549357
	 * @date 2015-11-27 下午3:45:23
	 * @param imageview
	 *            显示的ImageView
	 * @param url
	 *            下载连接
	 * @param isAnimation
	 *            是否使用动画
	 */
	public void display(ImageView imageview, String url, boolean isAnimation) {
		BitmapDisplayConfig bitmapDisplayConfig = new BitmapDisplayConfig();
		Drawable drawable = mContext.getResources().getDrawable(R.drawable.empty_photo);
		bitmapDisplayConfig.setLoadFailedDrawable(drawable);
		bitmapDisplayConfig.setLoadingDrawable(drawable);
		if (isAnimation) {
			Animation animationAlpha = new AlphaAnimation(0.3f, 1);
			animationAlpha.setDuration(250);
			bitmapDisplayConfig.setAnimation(animationAlpha);
		}
		if (null == bitmapUtils) {
			bitmapUtils = new BitmapUtils(mContext, CacheData.getImagesCache());
		}
		bitmapUtils.display(imageview, url, bitmapDisplayConfig);
	}

	/**
	 * 图片下载
	 * 
	 * @author tansheng QQ:717549357
	 * @date 2015-11-27 下午3:45:23
	 * @param imageview
	 *            显示的ImageView
	 * @param url
	 *            下载连接
	 */
	public void display(ImageView imageview, String url, boolean isAnimation, boolean isDefault) {
		BitmapDisplayConfig bitmapDisplayConfig = new BitmapDisplayConfig();
		if (isDefault){
			Drawable drawable = mContext.getResources().getDrawable(R.drawable.empty_photo);
			bitmapDisplayConfig.setLoadFailedDrawable(drawable);
			bitmapDisplayConfig.setLoadingDrawable(drawable);
		}
		if (isAnimation) {
			Animation animationAlpha = new AlphaAnimation(0.3f, 1);
			animationAlpha.setDuration(250);
			bitmapDisplayConfig.setAnimation(animationAlpha);
		}
		if (null == bitmapUtils) {
			bitmapUtils = new BitmapUtils(mContext, CacheData.getImagesCache());
		}
		bitmapUtils.display(imageview, url, bitmapDisplayConfig);
	}
	/**
	 * 图片下载
	 *
	 * @author tansheng QQ:717549357
	 * @date 2015-11-27 下午3:45:23
	 * @param imageview
	 *            显示的ImageView
	 * @param url
	 *            下载连接
	 */
	public void display(ImageView imageview, String url) {
		BitmapDisplayConfig bitmapDisplayConfig = new BitmapDisplayConfig();
		Drawable drawable = mContext.getResources().getDrawable(R.drawable.empty_photo);
		bitmapDisplayConfig.setLoadFailedDrawable(drawable);
		bitmapDisplayConfig.setLoadingDrawable(drawable);
		if (null == bitmapUtils) {
			bitmapUtils = new BitmapUtils(mContext, CacheData.getImagesCache());
		}
		bitmapUtils.display(imageview, url, bitmapDisplayConfig);
	}

	/**
	 * 自定义图片下载回调
	 * 
	 * @author tansheng QQ:717549357
	 * @date 2015-11-27 下午3:46:17
	 * @param imageview
	 *            显示的ImageView
	 * @param url
	 *            下载连接
	 * @param bitmapLoadCallBack
	 *            下载回调
	 */
	public void display(ImageView imageview, String url, BitmapLoadCallBack<ImageView> bitmapLoadCallBack) {
		if (null == bitmapUtils) {
			bitmapUtils = new BitmapUtils(mContext, CacheData.getImagesCache());
		}
		bitmapUtils.display(imageview, url, bitmapLoadCallBack);
	}

}
