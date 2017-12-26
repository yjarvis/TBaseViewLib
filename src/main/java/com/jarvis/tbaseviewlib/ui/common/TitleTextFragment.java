package com.jarvis.tbaseviewlib.ui.common;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jarvis.tbaseviewlib.R;

/**
 * 常用全文本显示的Title标题布局 功能说明 </p>作者: TS 创建日期：2014-10-13下午1:36:34 示例：
 */
public class TitleTextFragment extends TFragment {
	/** 左边的文本 */
	private TextView leftText;
	/** 中间的文本 */
	private TextView middleText;
	/** 右边的文本 */
	public TextView rightText;
	/** 左边文本内容 */
	private String leftContent = "";
	/** 中边文本内容 */
	private String middleContent = "";
	/** 右边文本内容 */
	private String rightContent = "";

	/** 控制title的背景颜色 */
	private String bgColor = "";
	/** 控制title的背景颜色的组件 */
	private RelativeLayout title_fragment_bg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			leftContent = bundle.getString("leftContent");
			middleContent = bundle.getString("middleContent");
			rightContent = bundle.getString("rightContent");
			bgColor = bundle.getString("bgColor");
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.title_text_fra_layout, container, false);

		initView(view);
		return view;
	}

	/**
	 * 
	 * 功能说明：常用的纯文本Title布局 </p>作者：ts 创建日期:2014-10-13 参数：
	 * 
	 * @param leftContent
	 *            左边的文本内容
	 * @param middleContent
	 *            中间的文本内容
	 * @param rightContent
	 *            右边的文本内容
	 * @param bgColor
	 *            背景颜色
	 * @return fragment 返回fragment
	 */
	public TitleTextFragment newInstance(String leftContent, String middleContent, String rightContent, String bgColor) {
		TitleTextFragment fragment = new TitleTextFragment();
		Bundle bundle = new Bundle();
		bundle.putString("leftContent", leftContent);
		bundle.putString("middleContent", middleContent);
		bundle.putString("rightContent", rightContent);
		bundle.putString("bgColor", bgColor);
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public void showFragment(Fragment fragment) {

	}

	@Override
	public void initView(View view) {
		leftText = (TextView) view.findViewById(R.id.title_left_show);
		middleText = (TextView) view.findViewById(R.id.title_middle_show);
		rightText = (TextView) view.findViewById(R.id.title_right_show);
		title_fragment_bg = (RelativeLayout) view.findViewById(R.id.title_fragment_bg);

		leftText.setOnClickListener(this);
		middleText.setOnClickListener(this);
		rightText.setOnClickListener(this);

		if (!TextUtils.isEmpty(leftContent)) {
			leftText.setText(leftContent);
		}
		if (!TextUtils.isEmpty(middleContent)) {
			middleText.setText(middleContent);
		}
		if (!TextUtils.isEmpty(rightContent)) {
			rightText.setText(rightContent);
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
		if (listener != null) {
			if (v == leftText) {
				listener.leftOnClik();
			} else if (v == middleText) {
				listener.middleOnClik();
			} else if (v == rightText) {
				listener.rightOnClik(rightText.getText().toString());
			}
		}

	}

	private RightOnClikListener listener;

	public interface RightOnClikListener {
		/** title左边的点击事件回调 */
		public void leftOnClik();

		/** title中间边的点击事件回调 */
		public void middleOnClik();

		/** title右边的点击事件回调 */
		public void rightOnClik(String str);
	}

	/**
	 * 功能说明：Title右边文字的点击监听 作者：ts 创建日期:2014-11-14 参数：
	 * 
	 * @param listener
	 */
	public void setTitleOnClikListener(RightOnClikListener listener) {
		this.listener = listener;
	}
}
