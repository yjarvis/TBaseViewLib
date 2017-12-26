package com.jarvis.tbaseviewlib.utils;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPoolManager {
	private Context context;
	private SoundPool soundPool;
	/**得到的音频id,通过此id进行音频的播放*/
	private HashMap<String, Integer>	soundHashMap;
	
	private static SoundPoolManager soundPoolManager;
	
	public static SoundPoolManager newInstance(Context context){
		if (soundPoolManager==null) {
			soundPoolManager=new SoundPoolManager(context);
		}
		return soundPoolManager;
	}

	
	
	
	private SoundPoolManager(Context context) {
		this.context = context;
		soundHashMap=new HashMap<String, Integer>();
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	}

	/***
	 * 功能说明：加载音频文件
	 * 
	 * @author 作者：jarvisT
	 * @date 2015-2-28 下午4:51:33
	 * @param resId
	 *            存放在raw文件夹下的音频文件
	 */
	public void initSoundPool(String keyName,int resId) {
		int soundId = soundPool.load(context, resId, 1);
		soundHashMap.put(keyName, soundId);
	}

	/**
	 * 功能说明：播放音频
	 * <p>《注》:虽然内部做了处理，不会使程序崩溃，但是在调用此方法前，请先调用initSoundPool()方法加载音频
	 * @author 作者：jarvisT
	 * @date 2015-2-28 下午4:56:06 
	 * @param loop   循环播放参数(-1：循环播放，0：正常播放，数字n：播放n次)
	 */
	public void playSoundPool(String keyName,int loop) {
		int soundId=soundHashMap.get(keyName);
		if (soundId!=-1) {
			soundPool.play(soundId, 1, 1, 0, loop, 1);
		}
	}

}
