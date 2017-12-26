/*
 * Copyright (C) 2013 www.418log.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jarvis.tbaseviewlib.view.wheelview;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jarvis.tbaseviewlib.widget.PopWindowDataYMD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * 时间计算辅助类
 */
public class AbWheelUtil  {

	/**
	 * 描述：默认的年月日的日期选择器.
	 * 
	 * @param popupWindow
	 *            popupWindow
	 * @param mText
	 *            the m text
	 * @param mWheelViewY
	 *            选择年的轮子
	 * @param mWheelViewM
	 *            选择月的轮子
	 * @param mWheelViewD
	 *            选择日的轮子
	 * @param okBtn
	 *            确定按钮
	 * @param cancelBtn
	 *            取消按钮
	 * @param defaultYear
	 *            默认起始显示的年 1970
	 * @param defaultMonth
	 *            默认起始显示的月 1
	 * @param defaultDay
	 *            默认起始显示的日 1
	 * @param currentYear
	 *            当前显示的年 2014
	 * @param endYearOffset
	 *            结束的年与开始的年的间隔时间 120
	 * @param initStart
	 *            轮子是否初始化默认时间为当前时间（true表示当前时间，false表示使用设置的默认显示时间）
	 */
	public static void initWheelDatePicker(final PopWindowDataYMD  popupWindow, final TextView mText, final AbWheelView mWheelViewY, final AbWheelView mWheelViewM, final AbWheelView mWheelViewD, Button okBtn, Button cancelBtn, int defaultYear, int defaultMonth, int defaultDay, final int currentYear, int endYearOffset, boolean initStart) {

		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };
		// 时间选择可以这样实现
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR) - 1;
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		// int hour = calendar.get(Calendar.HOUR);

		if (initStart) {
			defaultYear = year;
			defaultMonth = month;
			defaultDay = day;
			// defaultHours = hour;
		}
		int endYear = defaultYear + endYearOffset;

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);

		// 设置"年"的显示数据
		mWheelViewY.setAdapter(new AbNumericWheelAdapter(defaultYear-endYearOffset, endYear));
		mWheelViewY.setCyclic(false);// 可循环滚动
		mWheelViewY.setLabel("年"); // 添加文字
		mWheelViewY.setCurrentItem(currentYear - defaultYear+endYearOffset);// 初始化时显示的数据
		mWheelViewY.setValueTextSize(29);
		mWheelViewY.setLabelTextSize(29);
		mWheelViewY.setLabelTextColor(0x80000000);
		// mWheelViewY.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));

		// 月
		mWheelViewM.setAdapter(new AbNumericWheelAdapter(1, 12));
		mWheelViewM.setCyclic(true);
		mWheelViewM.setLabel("月");
		mWheelViewM.setCurrentItem(defaultMonth - 1);
		mWheelViewM.setValueTextSize(29);
		mWheelViewM.setLabelTextSize(29);
		mWheelViewM.setLabelTextColor(0x80000000);
		// mWheelViewM.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));

		// 日
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month + 1))) {
			mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 31));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 30));
		} else {
			// 闰年
			if (isLeapYear(year)) {
				mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 29));
			} else {
				mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 28));
			}
		}

		mWheelViewD.setCyclic(true);
		mWheelViewD.setLabel("日");
		mWheelViewD.setCurrentItem(defaultDay - 1);
		mWheelViewD.setValueTextSize(29);
		mWheelViewD.setLabelTextSize(29);
		mWheelViewD.setLabelTextColor(0x80000000);
		// mWheelViewD.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));

		// mWheelViewX.setAdapter(new AbNumericWheelAdapter(0, 23));
		// mWheelViewX.setCyclic(true);
		// mWheelViewX.setLabel("点");
		// mWheelViewX.setCurrentItem(hour);
		// mWheelViewX.setValueTextSize(32);
		// mWheelViewX.setLabelTextSize(30);
		// mWheelViewX.setLabelTextColor(-2147483648);

		// 添加"年"监听
		AbOnWheelChangedListener wheelListener_year = new AbOnWheelChangedListener() {

			public void onChanged(AbWheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + currentYear;
				int mDIndex = mWheelViewM.getCurrentItem();
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(mWheelViewM.getCurrentItem() + 1))) {
					mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(mWheelViewM.getCurrentItem() + 1))) {
					mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 30));
				} else {
					if (isLeapYear(year_num))
						mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 29));
					else
						mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 28));
				}
				mWheelViewM.setCurrentItem(mDIndex);

			}
		};
		// 添加"月"监听
		AbOnWheelChangedListener wheelListener_month = new AbOnWheelChangedListener() {

			public void onChanged(AbWheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(month_num))) {
					mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(month_num))) {
					mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 30));
				} else {
					int year_num = mWheelViewY.getCurrentItem() + currentYear;
					if (isLeapYear(year_num))
						mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 29));
					else
						mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 28));
				}
				mWheelViewD.setCurrentItem(0);
			}
		};

		mWheelViewY.addChangingListener(wheelListener_year);
		mWheelViewM.addChangingListener(wheelListener_month);

		okBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				int indexYear = mWheelViewY.getCurrentItem();
				String year = mWheelViewY.getAdapter().getItem(indexYear);

				int indexMonth = mWheelViewM.getCurrentItem();
				String month = mWheelViewM.getAdapter().getItem(indexMonth);

				int indexDay = mWheelViewD.getCurrentItem();
				String day = mWheelViewD.getAdapter().getItem(indexDay);
				// 小时
				// int indexHour = mWheelViewX.getCurrentItem();
				// String hours = mWheelViewX.getAdapter().getItem(indexHour);
				// mText.setText(dateTimeFormat(year + "-" + month + "-" + day +
				// "-" + hours));
				mText.setText(dateTimeFormat(year + "-" + month + "-" + day));
			}

		});

		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}

		});

	}

	/**
	 * 描述：默认的月日时分的时间选择器.
	 * 
	 * @param popWindow
	 *            Activity对象
	 * @param mText
	 *            the m text
	 * @param mWheelViewMD
	 *            选择月日的轮子
	 * @param mWheelViewHH
	 *            the m wheel view hh
	 * @param mWheelViewMM
	 *            选择分的轮子
	 * @param okBtn
	 *            确定按钮
	 * @param cancelBtn
	 *            取消按钮
	 * @param defaultYear
	 *            the default year
	 * @param defaultMonth
	 *            the default month
	 * @param defaultDay
	 *            the default day
	 * @param defaultHour
	 *            the default hour
	 * @param defaultMinute
	 *            the default minute
	 * @param initStart
	 *            the init start
	 * @date：2013-7-16 上午10:19:01
	 * @version v1.0
	 */
	public static void initWheelTimePicker(final PopupWindow popWindow, final TextView mText, final AbWheelView mWheelViewMD, final AbWheelView mWheelViewHH, final AbWheelView mWheelViewMM, Button okBtn, Button cancelBtn, int defaultYear, int defaultMonth, int defaultDay, int defaultHour, int defaultMinute, boolean initStart) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);

		if (initStart) {
			defaultYear = year;
			defaultMonth = month;
			defaultDay = day;
			defaultHour = hour;
			defaultMinute = minute;
		}

		String val = dateTimeFormat(defaultYear + "-" + defaultMonth + "-" + defaultDay + " " + defaultHour + ":" + defaultMinute + ":" + second);
		mText.setText(val);
		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		// String[] months_little = { "4", "6", "9", "11" };
		final List<String> list_big = Arrays.asList(months_big);
		// final List<String> list_little = Arrays.asList(months_little);

		final List<String> textDMList = new ArrayList<String>();
		final List<String> textDMDateList = new ArrayList<String>();
		for (int i = 1; i < 13; i++) {
			if (list_big.contains(String.valueOf(i))) {
				for (int j = 1; j < 32; j++) {
					textDMList.add(i + "月" + " " + j + "日");
					textDMDateList.add(defaultYear + "-" + i + "-" + j);
				}
			} else {
				if (i == 2) {
					if (isLeapYear(defaultYear)) {
						for (int j = 1; j < 28; j++) {
							textDMList.add(i + "月" + " " + j + "日");
							textDMDateList.add(defaultYear + "-" + i + "-" + j);
						}
					} else {
						for (int j = 1; j < 29; j++) {
							textDMList.add(i + "月" + " " + j + "日");
							textDMDateList.add(defaultYear + "-" + i + "-" + j);
						}
					}
				} else {
					for (int j = 1; j < 29; j++) {
						textDMList.add(i + "月" + " " + j + "日");
						textDMDateList.add(defaultYear + "-" + i + "-" + j);
					}
				}
			}

		}
		String currentDay = defaultMonth + "月" + " " + defaultDay + "日";
		int currentDayIndex = textDMList.indexOf(currentDay);

		// 月日
		mWheelViewMD.setAdapter(new AbStringWheelAdapter(textDMList, strLength("12月" + " " + "12日")));
		mWheelViewMD.setCyclic(true);
		mWheelViewMD.setLabel("");
		mWheelViewMD.setCurrentItem(currentDayIndex);
		mWheelViewMD.setValueTextSize(32);
		mWheelViewMD.setLabelTextSize(30);
		mWheelViewMD.setLabelTextColor(0x80000000);
		// mWheelViewMD.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));

		// 时
		mWheelViewHH.setAdapter(new AbNumericWheelAdapter(0, 23));
		mWheelViewHH.setCyclic(true);
		mWheelViewHH.setLabel("点");
		mWheelViewHH.setCurrentItem(defaultHour);
		mWheelViewHH.setValueTextSize(32);
		mWheelViewHH.setLabelTextSize(30);
		mWheelViewHH.setLabelTextColor(0x80000000);
		// mWheelViewH.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));

		// 分
		mWheelViewMM.setAdapter(new AbNumericWheelAdapter(0, 59));
		mWheelViewMM.setCyclic(true);
		mWheelViewMM.setLabel("分");
		mWheelViewMM.setCurrentItem(defaultMinute);
		mWheelViewMM.setValueTextSize(32);
		mWheelViewMM.setLabelTextSize(30);
		mWheelViewMM.setLabelTextColor(0x80000000);
		// mWheelViewM.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));

		okBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popWindow.dismiss();
				int index1 = mWheelViewMD.getCurrentItem();
				int index2 = mWheelViewHH.getCurrentItem();
				int index3 = mWheelViewMM.getCurrentItem();

				String dmStr = textDMDateList.get(index1);
				Calendar calendar = Calendar.getInstance();
				int second = calendar.get(Calendar.SECOND);
				String val = dateTimeFormat(dmStr + " " + index2 + ":" + index3 + ":" + second);
				mText.setText(val);
			}

		});

		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popWindow.dismiss();
			}

		});

	}

	/**
	 * 描述：默认的时分的时间选择器.
	 * 
	 * @param popupWindow
	 *            popupWindow
	 * @param mText
	 *            the m text
	 * @param mWheelViewHH
	 *            the m wheel view hh
	 * @param mWheelViewMM
	 *            选择分的轮子
	 * @param okBtn
	 *            确定按钮
	 * @param cancelBtn
	 *            取消按钮
	 * @param defaultHour
	 *            the default hour
	 * @param defaultMinute
	 *            the default minute
	 * @param initStart
	 *            the init start
	 */
	public static void initWheelTimePicker2(final PopWindowDataYMD popupWindow, final TextView mText, final AbWheelView mWheelViewHH, final AbWheelView mWheelViewMM, Button okBtn, Button cancelBtn, int defaultHour, int defaultMinute, boolean initStart) {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		if (initStart) {
			defaultHour = hour;
			defaultMinute = minute;
		}

		String val = dateTimeFormat(defaultHour + ":" + defaultMinute);
		mText.setText(val);

		// 时
		mWheelViewHH.setAdapter(new AbNumericWheelAdapter(0, 23));
		mWheelViewHH.setCyclic(true);
		mWheelViewHH.setLabel("点");
		mWheelViewHH.setCurrentItem(defaultHour);
		mWheelViewHH.setValueTextSize(32);
		mWheelViewHH.setLabelTextSize(30);
		mWheelViewHH.setLabelTextColor(0x80000000);
		// mWheelViewH.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));

		// 分
		mWheelViewMM.setAdapter(new AbNumericWheelAdapter(0, 59));
		mWheelViewMM.setCyclic(true);
		mWheelViewMM.setLabel("分");
		mWheelViewMM.setCurrentItem(defaultMinute);
		mWheelViewMM.setValueTextSize(32);
		mWheelViewMM.setLabelTextSize(30);
		mWheelViewMM.setLabelTextColor(0x80000000);
		// mWheelViewM.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));

		okBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				int index2 = mWheelViewHH.getCurrentItem();
				int index3 = mWheelViewMM.getCurrentItem();
				String val = dateTimeFormat(index2 + ":" + index3);
				mText.setText(val);
			}

		});

		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}

		});

	}

	/**
	 * 描述：默认的年月日时分的日期选择器.
	 * 
	 * @param popupWindow
	 *            Activity对象
	 * @param mText
	 *            the m text
	 * @param mWheelViewY
	 *            选择年的轮子
	 * @param mWheelViewM
	 *            选择月的轮子
	 * @param mWheelViewD
	 *            选择日的轮子
	 * @param mWheelViewHH
	 *            选择时的轮子
	 * @param mWheelViewMM
	 *            选择分的轮子
	 * @param okBtn
	 *            确定按钮
	 * @param cancelBtn
	 *            取消按钮
	 * @param defaultYear
	 *            默认起始显示的年 1970
	 * @param defaultMonth
	 *            默认起始显示的月 1
	 * @param defaultDay
	 *            默认起始显示的日 1
	 * @param defaultHour
	 *            默认起始显示的时 
	 * @param defaultMinute
	 *            默认起始显示的分
	 * @param currentYear
	 *            当前显示的年 2014
	 * @param endYearOffset
	 *            结束的年与开始的年的间隔时间 120
	 * @param initStart
	 *            轮子是否初始化默认时间为当前时间（true表示当前时间，false表示使用设置的默认显示时间）
	 */
	public static void initWheelDatePicker2(final PopWindowDataYMD popupWindow, final TextView mText, final AbWheelView mWheelViewY, final AbWheelView mWheelViewM, final AbWheelView mWheelViewD, final AbWheelView mWheelViewHH, final AbWheelView mWheelViewMM, Button okBtn, Button cancelBtn, int defaultYear, int defaultMonth, int defaultDay, int defaultHour, int defaultMinute, final int currentYear, int endYearOffset, boolean initStart) {
		
		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };
		// 时间选择可以这样实现
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR) ;
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		// int hour = calendar.get(Calendar.HOUR);

		if (initStart) {
			defaultYear = year;
			defaultMonth = month;
			defaultDay = day;
			defaultHour = hour;
			defaultMinute = minute;
			// defaultHours = hour;
		}
		int endYear = defaultYear + endYearOffset;

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);

		// 设置"年"的显示数据
		mWheelViewY.setAdapter(new AbNumericWheelAdapter(defaultYear, endYear));
		mWheelViewY.setCyclic(false);// 可循环滚动
		mWheelViewY.setLabel("年"); // 添加文字
		mWheelViewY.setCurrentItem(currentYear - defaultYear);// 初始化时显示的数据
		mWheelViewY.setValueTextSize(29);
		mWheelViewY.setLabelTextSize(29);
//		mWheelViewY.setLabelTextColor(0x80000000);
//		mWheelViewY.setValueTextColor(0x80000000);
		// mWheelViewY.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));

		// 月
		mWheelViewM.setAdapter(new AbNumericWheelAdapter(1, 12));
		mWheelViewM.setCyclic(true);
		mWheelViewM.setLabel("月");
		mWheelViewM.setCurrentItem(defaultMonth - 1);
		mWheelViewM.setValueTextSize(29);
		mWheelViewM.setLabelTextSize(29);
//		mWheelViewM.setLabelTextColor(0x80000000);
		// mWheelViewM.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));

		// 日
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month + 1))) {
			mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 31));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 30));
		} else {
			// 闰年
			if (isLeapYear(year)) {
				mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 29));
			} else {
				mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 28));
			}
		}

		mWheelViewD.setCyclic(true);
		mWheelViewD.setLabel("日");
		mWheelViewD.setCurrentItem(defaultDay - 1);
		mWheelViewD.setValueTextSize(29);
		mWheelViewD.setLabelTextSize(29);
//		mWheelViewD.setLabelTextColor(0x80000000);
		// mWheelViewD.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));

		// 时
		mWheelViewHH.setAdapter(new AbNumericWheelAdapter(0, 23));
		mWheelViewHH.setCyclic(true);
		mWheelViewHH.setCurrentItem(defaultHour);
		mWheelViewHH.setValueTextSize(29);
		mWheelViewHH.setLabelTextSize(30);
//		mWheelViewHH.setLabelTextColor(0x80000000);
		// mWheelViewH.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));

		// 分
		mWheelViewMM.setAdapter(new AbNumericWheelAdapter(0, 59));
		mWheelViewMM.setCyclic(true);
		mWheelViewMM.setCurrentItem(defaultMinute);
		mWheelViewMM.setValueTextSize(29);
		mWheelViewMM.setLabelTextSize(30);
//		mWheelViewMM.setLabelTextColor(0x80000000);
		// mWheelViewM.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));

		// 添加"年"监听
		AbOnWheelChangedListener wheelListener_year = new AbOnWheelChangedListener() {

			public void onChanged(AbWheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + currentYear;
				int mDIndex = mWheelViewM.getCurrentItem();
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(mWheelViewM.getCurrentItem() + 1))) {
					mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(mWheelViewM.getCurrentItem() + 1))) {
					mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 30));
				} else {
					if (isLeapYear(year_num))
						mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 29));
					else
						mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 28));
				}
				mWheelViewM.setCurrentItem(mDIndex);

			}
		};
		// 添加"月"监听
		AbOnWheelChangedListener wheelListener_month = new AbOnWheelChangedListener() {

			public void onChanged(AbWheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(month_num))) {
					mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(month_num))) {
					mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 30));
				} else {
					int year_num = mWheelViewY.getCurrentItem() + currentYear;
					if (isLeapYear(year_num))
						mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 29));
					else
						mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 28));
				}
				mWheelViewD.setCurrentItem(0);
			}
		};

		mWheelViewY.addChangingListener(wheelListener_year);
		mWheelViewM.addChangingListener(wheelListener_month);

		okBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				int indexYear = mWheelViewY.getCurrentItem();
				String year = mWheelViewY.getAdapter().getItem(indexYear);

				int indexMonth = mWheelViewM.getCurrentItem();
				String month = mWheelViewM.getAdapter().getItem(indexMonth);

				int indexDay = mWheelViewD.getCurrentItem();
				String day = mWheelViewD.getAdapter().getItem(indexDay);
				int indexHour = mWheelViewHH.getCurrentItem();
				String hour = mWheelViewHH.getAdapter().getItem(indexHour);
				int indexMinute = mWheelViewMM.getCurrentItem();
				String minute = mWheelViewMM.getAdapter().getItem(indexMinute);
				// 小时
				// int indexHour = mWheelViewX.getCurrentItem();
				// String hours = mWheelViewX.getAdapter().getItem(indexHour);
				// mText.setText(dateTimeFormat(year + "-" + month + "-" + day +
				// "-" + hours));
				mText.setText(dateTimeFormat(year + "-" + month + "-" + day + " "+hour+":"+minute));
			}

		});

		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}

		});

	}

	/**
	 * 描述：不足2个字符的在前面补“0”.
	 * 
	 * @param str
	 *            指定的字符串
	 * @return 至少2个字符的字符串
	 */
	private static String strFormat2(String str) {
		try {
			if (str.length() <= 1) {
				str = "0" + str;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 描述：判断一个字符串是否为null或空值.
	 * 
	 * @param str
	 *            指定的字符串
	 * @return true or false
	 */
	private static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * 描述：标准化日期时间类型的数据，不足两位的补0.
	 * 
	 * @param dateTime
	 *            预格式的时间字符串，如:2012-3-2 12:2:20
	 * @return String 格式化好的时间字符串，如:2012-03-20 12:02:20
	 */
	private static String dateTimeFormat(String dateTime) {
		StringBuilder sb = new StringBuilder();
		try {
			if (isEmpty(dateTime)) {
				return null;
			}
			String[] dateAndTime = dateTime.split(" ");
			if (dateAndTime.length > 0) {
				for (String str : dateAndTime) {
					if (str.indexOf("-") != -1) {
						String[] date = str.split("-");
						for (int i = 0; i < date.length; i++) {
							String str1 = date[i];
							sb.append(strFormat2(str1));
							if (i < date.length - 1) {
								sb.append("-");
							}
						}
					} else if (str.indexOf(":") != -1) {
						sb.append(" ");
						String[] date = str.split(":");
						for (int i = 0; i < date.length; i++) {
							String str1 = date[i];
							sb.append(strFormat2(str1));
							if (i < date.length - 1) {
								sb.append(":");
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return sb.toString();
	}

	/**
	 * 描述：获取字符串的长度.
	 * 
	 * @param str
	 *            指定的字符串
	 * @return 字符串的长度（中文字符计2个）
	 */
	private static int strLength(String str) {
		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		if (!isEmpty(str)) {
			// 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
			for (int i = 0; i < str.length(); i++) {
				// 获取一个字符
				String temp = str.substring(i, i + 1);
				// 判断是否为中文字符
				if (temp.matches(chinese)) {
					// 中文字符长度为2
					valueLength += 2;
				} else {
					// 其他字符长度为1
					valueLength += 1;
				}
			}
		}
		return valueLength;
	}

	/**
	 * 描述：判断是否是闰年()
	 * <p>
	 * (year能被4整除 并且 不能被100整除) 或者 year能被400整除,则该年为闰年.
	 * 
	 * @param year
	 *            年代（如2012）
	 * @return boolean 是否为闰年
	 */
	private static boolean isLeapYear(int year) {
		if ((year % 4 == 0 && year % 400 != 0) || year % 400 == 0) {
			return true;
		} else {
			return false;
		}
	}

}
