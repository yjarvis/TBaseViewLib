package com.jarvis.tbaseviewlib.ui.common;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jarvis.tbaseviewlib.R;

/**
 * 常用带返回按钮的Title标题布局 功能说明 </p>作者: TS 创建日期：2014-10-13下午1:36:34 示例：
 */
@SuppressWarnings("unused")
public class TitleHomeFragment extends TFragment implements OnClickListener {
	/** 左边的按钮 */
	private TextView titleLeft;
	/** 右边的按钮 */
	private TextView titleRight;
	/** 标题文本 */
	private ImageView titleMiddle;
	/** 标题内容 */
	private String strLeft = "";
	private String strMiddle = "";
	private String strRight = "";
	/** 控制title的背景颜色 */
	private String bgColor = "";
	/** 控制title的背景颜色的组件 */
	private LinearLayout title_fragment_bg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			strLeft = args.getString("strLeft");
			strMiddle = args.getString("strContent");
			strRight = args.getString("strRight");
			bgColor = args.getString("bgColor");
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.title_home_fra, container, false);

		initView(view);
		return view;
	}

	/**
	 * 功能说明：初始化title的数据
	 * <p >
	 * 作者：ts 创建日期:2014-10-29 参数：
	 * 
	 * @param strContent
	 *            标题文字
	 * @param bgColor
	 *            title的背景颜色（例如：为""则默认）
	 * @return 示例：
	 */
	public TitleHomeFragment newInstance(String strContent, String bgColor) {
		TitleHomeFragment fragment = new TitleHomeFragment();
		Bundle args = new Bundle();
		args.putString("strContent", strContent);
		args.putString("bgColor", bgColor);
		fragment.setArguments(args);

		return fragment;
	}
	/**
	 * 功能说明：初始化title的数据
	 * <p >
	 * 作者：ts 创建日期:2014-10-29 参数：
	 * 
	 * @param titleContent
	 *            标题文字
	 * @param bgColor
	 *            title的背景颜色（例如：为""则默认）
	 * @return 示例：
	 */
	public TitleHomeFragment newInstance(String strLeft,String strContent,String strRight, String bgColor) {
		TitleHomeFragment fragment = new TitleHomeFragment();
		Bundle args = new Bundle();
		args.putString("strLeft", strLeft);
		args.putString("strContent", strContent);
		args.putString("strRight", strRight);
		args.putString("bgColor", bgColor);
		fragment.setArguments(args);
		
		return fragment;
	}


	@Override
	public void showFragment(Fragment fragment) {

	}

	@Override
	public void initView(View view) {
		titleLeft = (TextView) view.findViewById(R.id.title_left);
		titleMiddle = (ImageView) view.findViewById(R.id.title_middle);
		titleRight = (TextView) view.findViewById(R.id.title_right);
		title_fragment_bg = (LinearLayout) view.findViewById(R.id.title_fragment_bg);

		titleLeft.setOnClickListener(this);
		titleMiddle.setOnClickListener(this);
		titleRight.setOnClickListener(this);
		// 设置标题
		if (!TextUtils.isEmpty(strLeft)) {
			titleLeft.setText(strLeft);
		}
		// 设置标题
//		if (!TextUtils.isEmpty(strMiddle)) {
//			titleMiddle.setText(strMiddle);
//		}
		// 设置标题
		if (!TextUtils.isEmpty(strRight)) {
			titleRight.setText(strRight);
		}
		// 设置title的背景颜色
		if (!TextUtils.isEmpty(bgColor)) {
			title_fragment_bg.setBackgroundColor(Color.parseColor(bgColor));
		}
		
		

	}

	@Override
	public void setData() {

	}

	@Override
	public void requestData(boolean isShow) {

	}

	@Override
	public void onClick(View v) {
		
	}
}
