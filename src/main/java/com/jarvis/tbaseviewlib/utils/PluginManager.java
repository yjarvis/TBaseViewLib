package com.jarvis.tbaseviewlib.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.jarvis.tbaseviewlib.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dalvik.system.PathClassLoader;

/**
 * 插件管理器 
 * 步骤： 
 * 1、在主App的mainfest中配置 android:sharedUserId="包名+id"
 * 2、在插件App中的mainfest中配置与主App相同的shareUserId
 * 3、实例化PluginManager.newInstance(context)
 * 4、通过findPluginResource(class)或者findPluginResource(PackageInfo,class)获得插件中的资源
 * 5、通过getPluginDrawable(String)得到插件中某个指定的资源
 * 
 * @author tansheng
 */
public class PluginManager {
	private Context mContext;
	private Context pluginContext = null;
	private static PluginManager instance = null;
	/** 已安装的插件列表 */
	private List<PackageInfo> pluginAppList;
	/** 插件中的资源数据 */
	private HashMap<String, Integer> pluginDataMap;

	public Context getPluginContext() {
		return pluginContext;
	}

	public List<PackageInfo> getPluginAppList() {
		return pluginAppList;
	}

	public HashMap<String, Integer> getPluginDataMap() {
		return pluginDataMap;
	}

	public static PluginManager newInstance(Context context) {
		if (instance == null) {
			instance = new PluginManager(context);
		}
		return instance;
	}

	private PluginManager(Context context) {
		this.mContext = context;
		pluginAppList = new ArrayList<PackageInfo>();
		findPluginApk();
	}

	/**
	 * 查找本机安装的插件
	 * 
	 * @author tansheng
	 */
	private void findPluginApk() {
		PackageManager packageManager = mContext.getPackageManager();
		// 获取本机的包名
		String localPackName = mContext.getPackageName();
		// 获取本机的信息
		PackageInfo info = null;
		// 获取本机的shareUserId
		String localUserId = null;
		try {
			info = packageManager.getPackageInfo(localPackName, 0);
			// 获取本机的shareUserId
			localUserId = info.sharedUserId;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		// 获取本机安装的Apk信息
		List<PackageInfo> installAppList = packageManager.getInstalledPackages(0);
		// 获得安装的插件
		for (PackageInfo packageInfo : installAppList) {
			// 如果安装的app的包名和本机不同，并且他们的shareUserId相同，则是此app的插件
			if (packageInfo.sharedUserId != null && !packageInfo.packageName.equals(localPackName) && packageInfo.sharedUserId.equals(localUserId)) {
				pluginAppList.add(packageInfo);
			}
		}
	}

	/**
	 *

	 */
	/**
	 * 获取所有的插件中的资源数据
	 * @param cls
	 * @author tansheng
	 * @return
	 */
	public ArrayList<?> findPluginResource(Class<?> cls) {
		// 暂无插件资源
		if (pluginAppList == null || pluginAppList.size() == 0) {
			Toast.makeText(mContext, "暂未下载插件，请先下载插件", Toast.LENGTH_SHORT).show();
			return null;
		}
		ArrayList<Object> objList = new ArrayList<Object>();

		if (pluginDataMap != null) {
			pluginDataMap.clear();
			pluginDataMap = null;
		}

		pluginDataMap = new HashMap<String, Integer>();
		Object obj = null;
		for (PackageInfo info : pluginAppList) {
			// 首先获取插件程序的上下文
			try {
				pluginContext = mContext.createPackageContext(info.packageName, Context.CONTEXT_IGNORE_SECURITY);
				// 类加载器
				ClassLoader classLoader = new PathClassLoader(pluginContext.getPackageResourcePath(), null, mContext.getClassLoader());
				Class<?> forName = null;
				// 反射获得资源类
				forName = Class.forName(info.packageName + ".R$drawable", true, classLoader);
				/*
				 * public Field getDeclaredField(String name) 获取任意指定名字的成员 public
				 * Field[] getDeclaredFields() 获取所有的成员变量 public
				 * FieldgetField(String name) 获取任意public成员变量 public Field[]
				 * getFields()获取所有的public成员变量
				 */
				// 获取插件中的资源
				Field[] fields = forName.getFields();
				Field[] fieldsBean = cls.getDeclaredFields();
				// 获得bean类对象
				obj = cls.newInstance();
				for (Field field_plugin : fields) {
					// 获得插件中的资源名称和值
					String name_plugin = field_plugin.getName();
					int id_plugin = field_plugin.getInt(R.drawable.class);
					for (Field field2_bean : fieldsBean) {
						// 获得bean类中的对象名
						String name_bean = field2_bean.getName();
						// 如果插件中的资源名称与协议的名称一致，则加入到资源数据中
						if (name_plugin.equals(name_bean)) {
							// 为bean类对象赋值
							setter(obj, name_bean, name_bean, field2_bean.getType());
							pluginDataMap.put(name_plugin, id_plugin);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			objList.add(obj);
		}
		return objList;
	}

	/**
	 * 获取插件中的资源数据
	 * 
	 * @author tansheng
	 */
	public Object findPluginResource(PackageInfo info, Class<?> cls) {
		if (pluginDataMap != null) {
			pluginDataMap.clear();
			pluginDataMap = null;
		}
		pluginDataMap = new HashMap<String, Integer>();

		Object obj = null;
		try {
			// 首先获取插件程序的上下文
			pluginContext = mContext.createPackageContext(info.packageName, Context.CONTEXT_IGNORE_SECURITY);
			// 类加载器
			ClassLoader classLoader = new PathClassLoader(pluginContext.getPackageResourcePath(), null, mContext.getClassLoader());
			Class<?> forName = null;
			// 反射获得资源类
			forName = Class.forName(info.packageName + ".R$drawable", true, classLoader);
			/*
			 * public Field getDeclaredField(String name) 获取任意指定名字的成员 
			 * public Field[] getDeclaredFields() 获取所有的成员变量 
			 * public FieldgetField(Stringname) 获取任意public成员变量 
			 * public Field[] getFields()获取所有的public成员变量
			 */
			// 获取插件中的属性
			Field[] fields = forName.getFields();
			// 获得bean类的属性
			Field[] fieldsBean = cls.getDeclaredFields();
			// 获得bean类对象
			obj = cls.newInstance();
			for (Field field_plugin : fields) {
				// 获得插件中的属性名称和值
				String name_plugin = field_plugin.getName();
				int id_plugin = field_plugin.getInt(R.drawable.class);
				for (Field field2_bean : fieldsBean) {
					// 获得bean类中的对象名
					String name_bean = field2_bean.getName();
					// 如果插件中的资源名称与协议的名称一致，则加入到资源数据中
					if (name_plugin.equals(name_bean)) {
						// 为bean类对象赋值
						setter(obj, name_bean, name_bean, field2_bean.getType());
						pluginDataMap.put(name_plugin, id_plugin);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return obj;
	}

	/**
	 * 获取插件资源中的drawable,用于设置背景
	 * 
	 * @author tansheng
	 */
	public Drawable getPluginDrawable(String key) {

		int id = pluginDataMap.get(key);
		Drawable drawable = pluginContext.getResources().getDrawable(id);

		return drawable;
	}

	/**
	 * @param obj
	 *            操作的对象
	 * @param att
	 *            操作的属性
	 * */
	@SuppressWarnings("unused")
	private void getter(Object obj, String att) {
		try {
			Method method = obj.getClass().getMethod("get" + att);
			System.out.println(method.invoke(obj));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param obj
	 *            操作的对象
	 * @param att
	 *            操作的属性
	 * @param value
	 *            设置的值
	 * @param type
	 *            参数的属性
	 * */
	@SuppressLint("DefaultLocale")
	private void setter(Object obj, String att, Object value, Class<?> type) {
		// 方法名
		String attName = att.substring(0, 1).toUpperCase() + att.substring(1);
		try {
			Method method = obj.getClass().getMethod("set" + attName, type);
			method.invoke(obj, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
