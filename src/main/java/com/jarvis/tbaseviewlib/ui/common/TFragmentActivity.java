package com.jarvis.tbaseviewlib.ui.common;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.google.gson.Gson;
import com.jarvis.tbaseviewlib.R;
import com.jarvis.tbaseviewlib.bitmap.BitmapHelp;
import com.jarvis.tbaseviewlib.constrans.Constrans;
import com.jarvis.tbaseviewlib.data.CacheData;
import com.jarvis.tbaseviewlib.http.GsonHelp;
import com.jarvis.tbaseviewlib.utils.CrashHandler;
import com.jarvis.tbaseviewlib.utils.StatusBarCompat;

/**
 * 
 * 功能说明   基类的FragmentActivity
 * 作者: ts
 * 创建日期：2014-10-13下午2:20:20
 * 示例：
 */
public abstract class TFragmentActivity extends FragmentActivity implements View.OnClickListener {
	
	public TitleBackFragment titleBackFragment;
	public Context context;
	public Resources resources;
    public Intent intent=null;
    public Gson gson;
    public BitmapHelp bitmapHelp;
	/**错误日志收集*/
	private CrashHandler crashHandler;
    /**是否是第一次进入Activity,true表示是第一次，false表示不是第一次进入，则会调用Resume()方法*/
    public boolean isFirstCreate=true;
    /**标题*/
    public String titleName;


	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		context=this;
		resources=getResources();
		//保存运行过的Acyivity，当退出时全体finish
        CacheData.activityList.add(this);
        //获取网络工具类
        gson=GsonHelp.newInstance();
        bitmapHelp=new BitmapHelp(context);
        isFirstCreate=true;
        //获取标题
    	titleName = getIntent().getStringExtra(Constrans.FLAG_TITLE);
	}

	/**
	 * 设置抓取错误日志
	 */
	public void openCrashErrData(CrashHandler.CrashHandlerListener listener){
		//抓取错误异常日志
		crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);
		crashHandler.setCrashHandlerListener(listener);
	}


	/**
     * 设置沉浸式效果
     * 添加沉浸式时，需要使用NoActionBar的主题，并且新增values-v19文件夹，并添加styles 
     * <style name="AppTheme"parent="@style/AppBaseTheme"> 
     * <item name="android:windowTranslucentStatus">true</item> 
     * </style>
     * 
     * 最后，在需要实现沉浸式的布局中的根节点上添加
     * android:fitsSystemWindows="true"
     * 注:此方法一定要在setContentView之后执行
     * @author tansheng  QQ:717549357
     * @date 2015-11-30 下午4:50:16 
     * @param isStatusBar   true:使用沉浸式效果，false不使用沉浸式效果
     */
    public void setStatusBarCompat(boolean isStatusBar){
    	if (isStatusBar) {
    		StatusBarCompat.compat(this, resources.getColor(R.color.titleBg));
		}
    }
    /**
     * 设置沉浸式效果
     * 添加沉浸式时，需要使用NoActionBar的主题，并且新增values-v19文件夹，并添加styles 
     * <style name="AppTheme"parent="@style/AppBaseTheme"> 
     * <item name="android:windowTranslucentStatus">true</item> 
     * </style>
     *
     * 最后，在需要实现沉浸式的布局中的根节点上添加
     * android:fitsSystemWindows="true"
     * 注:此方法一定要在setContentView之后执行
     * @author tansheng  QQ:717549357
     * @date 2015-11-30 下午4:50:16 
     * @param isStatusBar   true:使用沉浸式效果，false不使用沉浸式效果
     * @param color   沉浸式效果颜色
     */
    public void setStatusBarCompat(boolean isStatusBar,int color){
    	if (isStatusBar) {
    		StatusBarCompat.compat(this, color);
    	}
    }
    
    /**
     * 功能说明:初始化组件
     *
     * @author 作者：jarvisT
     * @date 创建日期：2015-1-22 下午11:59:03
     */
    public void initView(boolean isStatusBar){
		setStatusBarCompat(isStatusBar);
	}

    /**
     * 功能说明:为各个组件设置数据
     *
     * @author 作者：jarvisT
     * @date 创建日期：2015-1-22 下午11:59:03
     */
    public abstract void setData();

    /**
     * 功能说明:请求网络数据
     * @param isShow 是否显示正在加载的弹窗
     * @author 作者：jarvisT
     * @date 创建日期：2015-1-22 下午11:59:03
     */
    public abstract void requestData(boolean isShow);


    /**
     * 
     * 功能说明：加入titlefragment到布局中
     * </p>作者：ts
     * 创建日期:2014-10-13
     * 参数：
     * @param fragment   需要加入的fragment
     */
    final public void addTitleFragment(Fragment fragment) {
    	FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
    	ft.replace(R.id.title_fragment_content, fragment);
    	ft.commitAllowingStateLoss();
    }
	/**
	 * 
	 * 功能说明：加入某个fragment到布局中
	 * </p>作者：ts
	 * 创建日期:2014-10-13
	 * 参数：
	 * @param fragment   需要加入的fragment
	 * @param view_id    加入的控件id
	 */
	final public void addFragment(Fragment fragment,int view_id) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		ft.add(view_id, fragment);
		ft.commitAllowingStateLoss();
	}
	/**
	 * 
	 * 功能说明：加入某个fragment到布局中
	 * </p>作者：ts
	 * 创建日期:2014-10-13
	 * 参数：
	 * @param fragment   需要加入的fragment
	 * @param view_id    加入的控件id
	 */
	final public void addReplaceFragment(Fragment fragment,int view_id) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		ft.replace(view_id, fragment);
		ft.commitAllowingStateLoss();
	}

	/**
	 * 
	 * 功能说明：显示添加的fragment
	 * </p>作者：ts
	 * 创建日期:2014-10-13
	 * 参数：
	 * @param fragment  需要显示的fragment
	 */
	public abstract void showFragment(Fragment fragment);
	
	/**
	 * 
	 * 功能说明：删除某一个fragment
	 * </p>作者：ts
	 * 创建日期:2014-10-13
	 * 参数：
	 * @param fragment  传递需要被删除的fragment
	 */
	final public void removeFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		ft.remove(fragment);
		ft.commitAllowingStateLoss();
	}


	@Override
	public void onClick(View arg0) {
		
	}


	@Override
	protected void onResume() {
		super.onResume();
		isFirstCreate=false;
	}

	
//	private  AnimKeyBackListener  animListener;
//	public interface AnimKeyBackListener{
//		public void animKeyBack();
//	}
//	public void setAnimKeyBack(AnimKeyBackListener  animListener){
//		this.animListener=animListener;
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (animListener!=null) {
//			animListener.animKeyBack();
//		}
//		
//		return super.onKeyDown(keyCode, event);
//	}
//	
	
	
	
}
