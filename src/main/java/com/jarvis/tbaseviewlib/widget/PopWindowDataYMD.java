package com.jarvis.tbaseviewlib.widget;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.jarvis.tbaseviewlib.R;
import com.jarvis.tbaseviewlib.view.wheelview.AbWheelUtil;
import com.jarvis.tbaseviewlib.view.wheelview.AbWheelView;

/**
 * 功能说明：年月日时间选择器弹出层
 * 
 * @author 作者：jarvisT
 * @date 2015-1-26 下午2:32:28
 */
public class PopWindowDataYMD implements OnDismissListener, OnClickListener {
	private PopupWindow popupWindow;

	private TextView viewBg;
	private LinearLayout layout;
	private OnItemClickListener listener;

	public interface OnItemClickListener {
		/** 设置点击确认按钮时监听接口 */
		public void onClickOKPop();
	}

	/**
	 * @author tansheng QQ:717549357
	 * @date 2015-11-30 上午10:22:32
	 * @param context
	 * @param mText
	 * @param listener
	 */
	public PopWindowDataYMD(Context context, TextView mText) {
		View view = LayoutInflater.from(context).inflate(R.layout.popwindow_date_ymd, null);
		layout = (LinearLayout) view.findViewById(R.id.pop_layout);
		viewBg = (TextView) view.findViewById(R.id.pop_bg);
		Button okBtn = (Button) view.findViewById(R.id.wheelview_ok);
		Button cancelBtn = (Button) view.findViewById(R.id.wheelview_cancel);
		AbWheelView wheelViewY = (AbWheelView) view.findViewById(R.id.wheelview1);
		AbWheelView wheelViewM = (AbWheelView) view.findViewById(R.id.wheelview2);
		AbWheelView wheelViewD = (AbWheelView) view.findViewById(R.id.wheelview3);

		wheelViewY.setCenterSelectDrawable(context.getResources().getDrawable(R.drawable.wheel_select));
		wheelViewM.setCenterSelectDrawable(context.getResources().getDrawable(R.drawable.wheel_select));
		wheelViewD.setCenterSelectDrawable(context.getResources().getDrawable(R.drawable.wheel_select));

		// 获取当前年月日
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		// int month = calendar.get(Calendar.MONTH) + 1;
		// int day = calendar.get(Calendar.DATE);


//		okBtn.setOnClickListener(this);
//		cancelBtn.setOnClickListener(this);
		viewBg.setOnClickListener(this);

		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		// 设置popwindow的动画效果
		// popupWindow.setAnimationStyle(R.style.popWindow_anim_style);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.setOnDismissListener(this);// 当popWindow消失时的监听

		AbWheelUtil.initWheelDatePicker(PopWindowDataYMD.this, mText, wheelViewY, wheelViewM, wheelViewD, okBtn, cancelBtn, 0, 0, 0, year, 300, true);
	}

	// 当popWindow消失时响应
	@Override
	public void onDismiss() {
	}

	/** 弹窗显示的位置 */
	public void showAsDropDown(View view) {
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
		setBackgroundBlack(null, 0);

		Animation animationTran = new TranslateAnimation(0, 0, 0, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
		animationTran.setDuration(250);
		Animation animationAlpha = new AlphaAnimation(0, 1);
		animationAlpha.setDuration(250);
		viewBg.startAnimation(animationAlpha);
		layout.startAnimation(animationTran);
	}

	/**
	 * 设置监听
	 * 
	 * @author tansheng QQ:717549357
	 * @date 2015-11-30 上午10:23:56
	 * @param listener
	 */
	public void setOnItemClickListener(OnItemClickListener listener) {
		this.listener = listener;
	}

	/** 控制背景变暗 0变暗 1变亮 */
	private void setBackgroundBlack(View view, int what) {
		if (view == null) {
			return;
		}

		switch (what) {
		case 0:
			view.setVisibility(View.VISIBLE);
			break;
		case 1:
			view.setVisibility(View.GONE);
			break;
		}
	}

	/** 消除弹窗 */
	public void dismiss() {
		Animation animationTran = new TranslateAnimation(0, 0, 0, layout.getHeight());
		animationTran.setDuration(200);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				popupWindow.dismiss();

			}
		}, 200);
		layout.startAnimation(animationTran);
		Animation animationAlpha = new AlphaAnimation(1, 0);
		animationAlpha.setDuration(200);
		viewBg.startAnimation(animationAlpha);

	}

	@Override
	public void onClick(View v) {
//		int id = v.getId();
//		if (id == R.id.wheelview_ok) {
//			if (listener != null) {
//				listener.onClickOKPop();
//			}
//		}else if(id == R.id.wheelview_cancel){
//			if (listener != null) {
//				listener.onClickOKPop();
//			}
//		}
		dismiss();
	}

}
