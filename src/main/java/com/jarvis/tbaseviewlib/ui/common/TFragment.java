package com.jarvis.tbaseviewlib.ui.common;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.google.gson.Gson;
import com.jarvis.tbaseviewlib.R;
import com.jarvis.tbaseviewlib.bitmap.BitmapHelp;
import com.jarvis.tbaseviewlib.http.GsonHelp;

public abstract class TFragment extends Fragment implements View.OnClickListener {

	public Context context;
	public Resources resources;
	public Intent intent;
	public View view;
	public Gson gson;
	public BitmapHelp bitmapHelp;
	public FragmentCallBack listenerFragment;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		resources = getActivity().getResources();
		gson = GsonHelp.newInstance();
		bitmapHelp =new BitmapHelp(context);
	}
	
	public interface FragmentCallBack{
		/**
		 * 点Fragment在FragmentActivity响应的回调函数
		 *@author tansheng  QQ:717549357
		 * @date 2015-11-18 下午2:08:55 
		 * @param v   点击的控件
		 * @param fragmentFlag   操作的fragment的标记
		 * @param number
		 */
		public void onFragmentClick(View v, int fragmentFlag, int number);
	}

	public void setFragmentCallBackListener(FragmentCallBack listener){
		this.listenerFragment=listener;
	}
	
	/**
	 * 功能说明:初始化组件
	 * 
	 * @author 作者：jarvisT
	 * @date 创建日期：2015-1-22 下午11:59:03
	 */
	public abstract void initView(View view);

	/**
	 * 功能说明:为各个组件设置数据
	 * 
	 * @author 作者：jarvisT
	 * @date 创建日期：2015-1-22 下午11:59:03
	 */
	public abstract void setData();

	/**
	 * 功能说明:请求网络数据
	 * 
	 * @param isShow
	 *            是否显示正在加载的弹窗
	 * @author 作者：jarvisT
	 * @date 创建日期：2015-1-22 下午11:59:03
	 */
	public abstract void requestData(boolean isShow);

	/**
	 * 
	 * 功能说明：加入titlefragment到布局中 </p>作者：ts 创建日期:2014-10-13 参数：
	 * 
	 * @param fragment
	 *            需要加入的fragment
	 */
	final public void addTitleFragment(Fragment fragment) {
		FragmentTransaction ft = this.getChildFragmentManager().beginTransaction();
		ft.replace(R.id.title_fragment_content, fragment);
		ft.commitAllowingStateLoss();
	}

	/**
	 * 功能说明：加入某个fragment到布局中 </p>作者：ts 创建日期:2014-10-13 参数：
	 * 
	 * @param fragment
	 *            需要加入的fragment
	 * @param view_id
	 *            加入的控件id
	 */
	final public void addFragment(Fragment fragment, int view_id) {
		FragmentTransaction ft = this.getChildFragmentManager().beginTransaction();
		ft.add(view_id, fragment);
		ft.commitAllowingStateLoss();
	}

	/**
	 * 
	 * 功能说明：加入某个fragment到布局中 </p>作者：ts 创建日期:2014-10-13 参数：
	 * 
	 * @param fragment
	 *            需要加入的fragment
	 * @param view_id
	 *            加入的控件id
	 */
	final public void addReplaceFragment(Fragment fragment, int view_id) {
		FragmentTransaction ft = this.getChildFragmentManager().beginTransaction();
		ft.replace(view_id, fragment);
		ft.commitAllowingStateLoss();
	}

	/**
	 * 
	 * 功能说明：显示添加的fragment </p>作者：ts 创建日期:2014-10-13 参数：
	 * 
	 * @param fragment
	 *            需要显示的fragment
	 */
	abstract public void showFragment(Fragment fragment);

	/**
	 * 
	 * 功能说明：删除某一个fragment </p>作者：ts 创建日期:2014-10-13 参数：
	 * 
	 * @param fragment
	 *            传递需要被删除的fragment
	 */
	final public void removeFragment(Fragment fragment) {
		FragmentTransaction ft = this.getChildFragmentManager().beginTransaction();
		ft.remove(fragment);
		ft.commitAllowingStateLoss();
	}
	/**
	 * 刷新数据
	 *@author tansheng  QQ:717549357
	 * @date 2015-11-18 下午2:48:56
	 */
	public void refreshData(){
		
	}

}
