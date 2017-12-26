package com.jarvis.tbaseviewlib.view.floatlayout;


import android.content.Context;
import android.graphics.PixelFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.jarvis.tbaseviewlib.R;
/**
 * 悬浮窗控件
 * @author tansheng  QQ:717549357
 * @date 2015-11-30 上午11:19:55
 */
public class FloatWidget {

	private FloatViewOnClickListner mOnClickListener;
	private FloatViewOnDragListner mOnDragListener;
	private View myLayout;

	private WindowManager wm;
	private LayoutParams params;
	private Context context;
	/** 按下时的Y坐标 */
	private float DownY;
	/** 按下时的X坐标 */
	private float DownX;
	/** 悬浮窗所在位置的X坐标 */
	private float paramX;
	/** 悬浮窗所在位置的Y坐标 */
	private float paramY;
	
	/** X轴移动的长度 */
	private float dx;
	/** Y轴移动的长度 */
	private float dy;

	/** 悬浮窗布局的宽 */
	private int layoutWidth;
	/** 悬浮窗布局的高 */
	private int layoutHeight;
	/** 悬浮窗动画（在style文件中配置的
	 *  <style name="floatview_anim" parent="android:Animation">
     *  <item name="android:windowEnterAnimation">@anim/activity_anim_in</item>
     *  <item name="android:windowExitAnimation">@anim/activity_anim_out</item>
	 */
	private int animationStyle=-1;

	
	
	
	public interface FloatViewOnClickListner {
		/** 点击时的监听 */
		public void onClick(View v);
	}

	public interface FloatViewOnDragListner {
		/** 拖动时监听,返回悬浮窗x,y坐标 */
		public void onDrag(int paramsX, int paramsY);
	}

	public FloatWidget(Context context) {
		this.context = context;
	}

	/**
	 * 功能说明：创建悬浮窗，导入的是xml的布局，而不是某一个组件的id
	 * @author 作者：jarvisT
	 * @date 2015-2-12 上午9:50:48 
	 * @param view   悬浮窗的布局xml
	 */
	public void creatFloatView(View view) {
		if (wm == null) {
			wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		}
		if (params == null) {
			params = new LayoutParams();
		}
		// 设置window type
		/*
		 * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
		 * 即拉下通知栏不可见
		 */
		params.type = LayoutParams.TYPE_PHONE;

		params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明(RGBA_8888)
		// 设置Window flag

		/*
		 * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
		 * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
		 * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
		 */
		params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;

		// 设置悬浮窗的长得宽
		params.width = LayoutParams.MATCH_PARENT;
		params.height = LayoutParams.WRAP_CONTENT;
		//动画效果
		if (animationStyle==-1) {
			params.windowAnimations=R.style.floatview_anim;
		}else {
			params.windowAnimations=animationStyle;
		}
		// 设置悬浮窗的位置
		params.x = 0;
		params.y = 0;
		this.myLayout = view;

		wm.addView(view, params);

	}

	/**
	 * 设置是否可以拖动
	 * 
	 * @param isDrag
	 *            是否可以拖动此布局，true可以拖动，并能执行点击事件，false不可以拖动，只执行点击事件
	 */
	public void setIsDrag(boolean isDrag) {
		if (isDrag) {
			// 执行触摸事件
			myLayout.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						// 获取相对屏幕的坐标，即以屏幕左上角为原点
						DownX = event.getRawX();
						DownY = event.getRawY();
						// 得到当前悬浮窗的位置
						paramX = params.x;
						paramY = params.y;

						layoutWidth = myLayout.getWidth();
						layoutHeight = myLayout.getHeight();

						break;
					case MotionEvent.ACTION_MOVE:
						// 计算移动的位置
						dx = event.getRawX() - DownX;
						dy = event.getRawY() - DownY;

						// 将移动的位置+之前的悬浮窗位置，得到移动后的悬浮窗位置
							params.x = (int) (paramX + dx);
							params.y = (int) (paramY + dy);
							if (mOnDragListener != null) {
								mOnDragListener.onDrag(params.x, params.y);
							}
							// 更新悬浮窗
							wm.updateViewLayout(myLayout, params);
						break;
					case MotionEvent.ACTION_UP: // 如果移动距离不超过5像素，那么则执行OnClick事件
						if (dx <= 10 && dx >= 0 && dy <= 10 && dy >= 0) {
							if (mOnClickListener != null) {
								// 执行点击事件
								mOnClickListener.onClick(v);
							}
						}

						dx = 0;
						dy = 0;
						break;
					}
					return true;
				}
			});
		}
	}

	/** 设置点击监听 */
	public void setOnClickListener(FloatViewOnClickListner listner) {
		this.mOnClickListener = listner;
		// 执行点击事件
		myLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mOnClickListener != null) {
					// 执行点击事件
					mOnClickListener.onClick(v);
				}
			}
		});
	}

	/** 设置拖动时监听 */
	public void setOnDragListner(FloatViewOnDragListner listner) {
		this.mOnDragListener = listner;
	}

	

	/** 删除悬浮窗 */
	public void removeFloatView() {
		if (wm == null) {
			return;
		}
		wm.removeView(myLayout);
	}

	/** 设置悬浮窗的位置 */
	public void setPositionFloatView(int x, int y) {
		if (params == null || myLayout == null) {
			return;
		}
		params.x = x;
		params.y = y;
		wm.updateViewLayout(myLayout, params);
	}

	/** 更新悬浮窗状态 */
	public void updataFloatView() {
		if (wm == null || params == null) {
			return;
		}
		wm.updateViewLayout(myLayout, params);
	}

	/** 获取当前悬浮窗的X坐标 */
	public int getFloatX() {
		return (int) paramX;
	}

	/** 获取当前悬浮窗的Y坐标 */
	public int getFloatY() {
		return (int) paramY;
	}
	/**获取悬浮窗宽*/
	public int getLayoutWidth() {
		return layoutWidth;

	}

	/**获取悬浮窗高*/
	public int getLayoutHeight() {
		return layoutHeight;
	}
	
	
	public void setAnimation(int style){
		animationStyle=style;
	}
}
