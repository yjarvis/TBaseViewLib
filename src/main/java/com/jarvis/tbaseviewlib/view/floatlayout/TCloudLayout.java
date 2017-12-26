package com.jarvis.tbaseviewlib.view.floatlayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 标签云: 提供的方法有：
 * <p>1、设置文字颜色、大小
 * <p>2、设置滚动速度 
 * <p>3、设置滚动方向，竖直和横向 
 * <p>4、支持纯文本、纯图片和图文一一对应 
 * <p>5、点击事件的回调
 * <p>6、设置控件之间竖向间距(此方法只有在竖向模式有效) 
 * <p>7、可控制滚动开启与关闭
 * @author tansheng
 */
public class TCloudLayout extends RelativeLayout implements OnClickListener, OnTouchListener {
	private Context context;
	/** 父控件宽高 */
	private int mWidth, mHeight;
	/** 抛物线顶点到零点高度占View总长度的比值 */
	private int scaleArcTopPoint = 3;
	/** 每个Item的第一条数据的顶部X,Y坐标 */
	private int firstTextX, firstTextY;
	/** 整个控件是否已经初始化，true表示已经初始化，false表示未初始化 */
	private boolean isInit = false;
	/** 自动滚动速度 */
	private int speed = 2;
	/** 设置文字最小大小 */
	private float minTextSize = 15;
	/** 设置文字最大大小 */
	private float maxTextSize = 25;
	/** 滚动模式是否是竖向(默认竖向)，ture:表示竖向，false:表示横向 */
	private boolean isVertical = true;
	/** 子控件间距(使用竖向模式时) */
	private int viewMarginVertical = (int) (4.5 * minTextSize);
	/** 子控件间距(使用横向模式时，自动计算中间列中Item的最长宽度) */
	private int viewMarginHorizontal = -1;
	/** 设置文字最小透明度 */
	private float minAlpha = 0.2f;
	/** 设置文字最大透明度 */
	private float maxAlpha = 1f;
	/** 左中右三列View */
	private ArrayList<View> leftOrTopViews, middleViews, rightOrDownViews;
	/** 记录左中右三列View以及其所在位置 */
	private HashMap<Integer, View> leftOrTopViewsMap, middleViewsMap, rightOrDownViewsMap;
	/** 自动滚动控制器 */
	private Timer timer;
	private MyTask myTask;
	/** 是否是从上往下滚动，true:从上向下滚动，false:从下往上滚动 */
	private boolean isMoveUp2Down = true;
	/** 是否是从右往左滚动，true:从右向左滚动，false:从左往右滚动 */
	private boolean isMoveRight2Left = false;

	public TCloudLayout(Context context) {
		super(context);
		init(context);
	}

	public TCloudLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public TCloudLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/** 初始化 */
	@SuppressLint("UseSparseArrays")
	private void init(Context context) {
		this.context = context;
		leftOrTopViews = new ArrayList<View>();
		middleViews = new ArrayList<View>();
		rightOrDownViews = new ArrayList<View>();
		leftOrTopViewsMap = new HashMap<Integer, View>();
		middleViewsMap = new HashMap<Integer, View>();
		rightOrDownViewsMap = new HashMap<Integer, View>();
		timer = new Timer();
		myTask = new MyTask(handler);
	}

	/** dip转px */
	private int dipTopx(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 创建一个TextView
	 * 
	 * @author tansheng
	 * @param text
	 *            文字
	 * @param id
	 *            组件id
	 */
	private TextView creatTextView(String text, String id) {
		TextView textView = new TextView(context);
		textView.setId(Integer.valueOf(id));
		textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		textView.setText(text);
		// textView.setTextColor(leftTextColor);
		// textView.setTextSize(minTextSize);
		textView.setGravity(Gravity.CENTER);
		textView.setOnClickListener(this);
		textView.setOnTouchListener(this);
		viewMarginVertical = (int) (4.5 * minTextSize);
		return textView;
	}

	/**
	 * 创建一个带图片和文字的View
	 * 
	 * @author tansheng
	 * @param text
	 *            文字
	 * @param res
	 *            资源路径
	 * @param id
	 *            组件id
	 * @return
	 */
	private LinearLayout creatTextImageView(String text, Bitmap bitmap, String id) {

		LinearLayout ll = new LinearLayout(context);
		ll.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setGravity(Gravity.CENTER);
		ll.setId(Integer.valueOf(id));

		TextView textView = new TextView(context);
		textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		textView.setText(text);
		textView.setTextColor(textColorMiddle);
		textView.setTextSize(minTextSize);
		textView.setGravity(Gravity.CENTER);

		ImageView imageView = new ImageView(context);
		imageView.setLayoutParams(new LayoutParams(dipTopx(context, imageSize), dipTopx(context, imageSize)));
		imageView.setScaleType(ScaleType.CENTER_CROP);
		imageView.setImageBitmap(bitmap);

		ll.addView(imageView);
		ll.addView(textView);

		ll.setOnClickListener(this);
		ll.setOnTouchListener(this);
		viewMarginVertical = (int) (4.5 * minTextSize + 1.5* dipTopx(context, imageSize));
		return ll;
	}

	/**
	 * 创建一个图片的Item
	 * 
	 * @author tansheng
	 * @param bitmap
	 *            资源路径
	 * @param id
	 *            组件id
	 * @return
	 */
	private ImageView creatImageView(Bitmap bitmap, String id) {
		ImageView imageView = new ImageView(context);
		imageView.setId(Integer.valueOf(id));
		imageView.setLayoutParams(new LayoutParams(dipTopx(context, imageSize), dipTopx(context, imageSize)));
		imageView.setScaleType(ScaleType.CENTER_CROP);
		imageView.setImageBitmap(bitmap);
		imageView.setOnClickListener(this);
		imageView.setOnTouchListener(this);
		viewMarginVertical = (int) (1.4 * dipTopx(context, imageSize));
		return imageView;
	}

	// 测量父控件大小(首先执行onMeasure-->onLayout-->dispatchWindowFocusChanged-->onMeasure此时才能拿到父控件的宽高-->onLayout)
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 获得子控件个数
		int count = getChildCount();
		// 测量每个子控件
		for (int i = 0; i < count; i++) {
			View childView = getChildAt(i);
			measureChild(childView, widthMeasureSpec, widthMeasureSpec);
		}

		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
		// System.out.println("onMeasure" + mWidth + "    " + mHeight);
	}

	@Override
	public void dispatchWindowFocusChanged(boolean hasFocus) {
		super.dispatchWindowFocusChanged(hasFocus);
		if (!isInit) {
			// 已经初始化完成，此时可以拿到父控件的宽高
			isInit = true;
			// 获取父控件宽高
			mWidth = getWidth();
			mHeight = getHeight();
			firstTextX = mWidth;
			firstTextY = mHeight;
			// 开始滚动
			startPlay();
		}

		// 丢失焦点时，需要销毁线程
		if (!hasFocus) {
			stopPlay();
		}

	}

	// 设置子控件的位置(首先执行onMeasure-->onLayout-->dispatchWindowFocusChanged-->onMeasure此时才能拿到父控件的宽高-->onLayout)
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (isInit) {
			if (isVertical) {
				// 竖向
				// 设置中间Item的位置
				layoutAllForVertical(middleViews, 0);
				int temp = firstTextY;
				// 左边的数据、右边的数据与中间的数据有一定的间距，不在同一条线上
				firstTextY += viewMarginVertical;
				layoutAllForVertical(leftOrTopViews, -1);
				layoutAllForVertical(rightOrDownViews, 1);
				firstTextY = temp;
			} else {
				// 横向
				if (viewMarginHorizontal == -1) {
					// 得到中间item中最长的一个宽度
					for (int i = 0; i < middleViews.size(); i++) {
						if (middleViews.get(i).getMeasuredWidth() > viewMarginHorizontal) {
							viewMarginHorizontal = middleViews.get(i).getMeasuredWidth();
						}
					}
				}
				// 设置中间Item的位置
				layoutAllForHorizontal(middleViews, 0);
				int temp = firstTextX;
				// 左边、右边与中间有一定的间距，不在同一条线上
				firstTextX += 20;
				layoutAllForHorizontal(leftOrTopViews, 1);
				layoutAllForHorizontal(rightOrDownViews, -1);
				firstTextX = temp;
			}
		}
		// System.out.println("onLayout");

	}

	/**
	 * 竖直方向滚动，设置Item控件分别所在位置
	 * 
	 * @author tansheng
	 * @param viewList
	 * @param type
	 *            为-1时往左偏，抛物线开口向右，0的时候走直线，1的时候和-1对称
	 */
	private void layoutAllForVertical(ArrayList<View> viewList, int type) {
		int temp_y = firstTextY;
		for (int i = 0; i < viewList.size(); i++) {
			View temp = viewList.get(i);
			// 根据y值计算x坐标上的偏移量，type为-1时往左偏，抛物线开口向右，0的时候走直线，1的时候和-1对称
			int detaX = type * (int) (-mWidth * 4 / scaleArcTopPoint / Math.pow(mHeight, 2) * Math.pow(mHeight / 2.0 - temp_y, 2) + mWidth / scaleArcTopPoint);
			float scale = (float) (1 - 4 * Math.pow(mHeight / 2.0 - temp_y, 2) / Math.pow(mHeight, 2));
			if (scale < 0) {
				scale = 0;
			}
			float textScale = (float) ((minTextSize + scale * (maxTextSize - minTextSize)) * 1.0 / minTextSize);
			temp.setScaleX(textScale);
			temp.setScaleY(textScale);
			temp.setAlpha(minAlpha + scale * (maxAlpha - minAlpha));
			temp.layout((mWidth - temp.getMeasuredWidth()) / 2 + detaX, temp_y, (mWidth + temp.getMeasuredWidth()) / 2 + detaX, temp_y + temp.getMeasuredHeight());
			temp_y += 2 * viewMarginVertical;
		}
	}

	/**
	 * 横向方向滚动，设置Item控件分别所在位置
	 * 
	 * @author tansheng
	 * @param viewList
	 * @param type
	 *            为-1时往左偏，抛物线开口向右，0的时候走直线，1的时候和-1对称
	 */
	private void layoutAllForHorizontal(ArrayList<View> viewList, int type) {
		int temp_x = firstTextX;
		for (int i = 0; i < viewList.size(); i++) {
			View temp = viewList.get(i);
			// 根据x值计算y坐标上的偏移量，type为-1时往上偏，抛物线开口向下，0的时候走直线，1的时候和-1对称
			int detaY = type * (int) (mHeight / Math.pow(mWidth, 2) * Math.pow(temp_x - mWidth / 10, 2) - mHeight / mWidth * temp_x - mHeight / 6);
			float scale = (float) (1 - 4 * Math.pow(mWidth / 2.0 - temp_x, 2) / Math.pow(mWidth, 2));
			if (scale < 0) {
				scale = 0;
			}
			float textScale = (float) ((minTextSize + scale * (maxTextSize - minTextSize)) * 1.0 / minTextSize);
			temp.setScaleX(textScale);
			temp.setScaleY(textScale);
			temp.setAlpha(minAlpha + scale * (maxAlpha - minAlpha));
			temp.layout(temp_x, detaY + (mHeight - temp.getMeasuredHeight()) / 2, temp_x + temp.getMeasuredWidth(), detaY + (mHeight + temp.getMeasuredHeight()) / 2);
			temp_x += 1.8 * viewMarginHorizontal;
		}

	}

	/**
	 * 向上滚动
	 * 
	 * @author tansheng
	 */
	private void moveUp(int speed) {
		firstTextY -= speed;
		if (firstTextY < -2 * viewMarginVertical) {
			// 将item的第一条数据隐藏，然后将第一条数据放置到尾部，可以循环滚动了
			firstTextY = middleViews.get(1).getTop();
			View tv = middleViews.get(0);
			middleViews.remove(0);
			middleViews.add(tv);

			tv = leftOrTopViews.get(0);
			leftOrTopViews.remove(0);
			leftOrTopViews.add(tv);

			tv = rightOrDownViews.get(0);
			rightOrDownViews.remove(0);
			rightOrDownViews.add(tv);
		}

		// 重绘界面
		requestLayout();
	}

	/**
	 * 向左滚动
	 * 
	 * @author tansheng
	 */
	private void moveLeft(int speed) {
		firstTextX -= speed;
		if (firstTextX < -2 * viewMarginHorizontal) {
			// 将item的第一条数据隐藏，然后将第一条数据放置到尾部，可以循环滚动了
			firstTextX = middleViews.get(1).getLeft();
			View tv = middleViews.get(0);
			middleViews.remove(0);
			middleViews.add(tv);

			tv = leftOrTopViews.get(0);
			leftOrTopViews.remove(0);
			leftOrTopViews.add(tv);

			tv = rightOrDownViews.get(0);
			rightOrDownViews.remove(0);
			rightOrDownViews.add(tv);
		}

		// 重绘界面
		requestLayout();
	}

	/**
	 * 向下滚动
	 * 
	 * @author tansheng
	 */
	private void moveDown(int speed) {
		firstTextY += speed;
		if (firstTextY > viewMarginVertical && middleViews != null && middleViews.size() != 0) {
			// 将item的最后一条数据隐藏，然后将最后一条数据放置到顶部，可以循环滚动了
			firstTextY = -viewMarginVertical;
			View tv = middleViews.get(middleViews.size() - 1);
			middleViews.remove(middleViews.size() - 1);
			middleViews.add(0, tv);

			tv = leftOrTopViews.get(leftOrTopViews.size() - 1);
			leftOrTopViews.remove(leftOrTopViews.size() - 1);
			leftOrTopViews.add(0, tv);

			tv = rightOrDownViews.get(rightOrDownViews.size() - 1);
			rightOrDownViews.remove(rightOrDownViews.size() - 1);
			rightOrDownViews.add(0, tv);

		}

		// 重绘界面
		requestLayout();
	}

	/**
	 * 向右滚动
	 * 
	 * @author tansheng
	 */
	private void moveRight(int speed) {
		firstTextX += speed;
		if (firstTextX > viewMarginHorizontal && middleViews != null && middleViews.size() != 0) {
			// 将item的最后一条数据隐藏，然后将最后一条数据放置到顶部，可以循环滚动了
			firstTextX = (int) (-0.8 * viewMarginHorizontal);
			View tv = middleViews.get(middleViews.size() - 1);
			middleViews.remove(middleViews.size() - 1);
			middleViews.add(0, tv);

			tv = leftOrTopViews.get(leftOrTopViews.size() - 1);
			leftOrTopViews.remove(leftOrTopViews.size() - 1);
			leftOrTopViews.add(0, tv);

			tv = rightOrDownViews.get(rightOrDownViews.size() - 1);
			rightOrDownViews.remove(rightOrDownViews.size() - 1);
			rightOrDownViews.add(0, tv);
		}

		// 重绘界面
		requestLayout();
	}

	/**
	 * 开始滚动
	 * 
	 * @author tansheng
	 */
	public void startPlay() {
		if (myTask != null) {
			myTask.cancel();
			myTask = null;
		}
		myTask = new MyTask(handler);
		timer.schedule(myTask, 0, 20);
	}

	/**
	 * 停止滚动
	 * 
	 * @author tansheng
	 */
	public void stopPlay() {
		if (myTask != null) {
			myTask.cancel();
			myTask = null;
		}
	}

	// 延迟启动播放
	@SuppressLint("HandlerLeak")
	private Handler handlerDelayed = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			startPlay();
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		// 接收到滚动请求
		@Override
		public void handleMessage(Message msg) {
			if (isVertical) {
				// 竖向
				if (isMoveUp2Down) {
					// 往下滚动
					moveDown(speed);
				} else {
					// 往上滚动
					moveUp(speed);
				}
			} else {
				// 横向
				if (isMoveRight2Left) {
					// 往左滚动
					moveLeft(speed);
				} else {
					// 往右滚动
					moveRight(speed);
				}
			}
		}
	};

	private class MyTask extends TimerTask {
		private Handler tempHandler;

		public MyTask(Handler tempHandler) {
			this.tempHandler = tempHandler;
		}

		@Override
		public void run() {
			tempHandler.sendEmptyMessage(0);
		}

	}

	@Override
	public void onClick(View v) {
		// 计算点击的组件中的数据所在位置
		int positionLeftOrTop = -1, positionMiddle = -1, positionRightOrDown = -1;
		for (int i = 0; i < leftOrTopViewsMap.size(); i++) {
			if (leftOrTopViewsMap.get(i).getId() == v.getId()) {
				positionLeftOrTop = i;
			}
		}

		if (positionLeftOrTop == -1) {
			for (int i = 0; i < middleViewsMap.size(); i++) {
				if (middleViewsMap.get(i).getId() == v.getId()) {
					positionMiddle = i;
				}
			}

		}
		if (positionLeftOrTop == -1 && positionMiddle == -1) {
			for (int i = 0; i < rightOrDownViewsMap.size(); i++) {
				if (rightOrDownViewsMap.get(i).getId() == v.getId()) {
					positionRightOrDown = i;
				}
			}

		}

		// TextView的点击事件
		if (listener != null) {
			stopPlay();
			listener.cloudOnClick(v, positionLeftOrTop, positionMiddle, positionRightOrDown);
		}

	}

	/** 按下的X坐标 */
	private float downX;
	/** 按下的Y坐标 */
	private float downY;
	/** 上一次的X坐标 */
	private float lastX;
	/** 上一次的Y坐标 */
	private float lastY;
	/** 计算按下后移动的距离 */
	private double length;

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastX = event.getX();
			lastY = event.getY();
			stopPlay();
			break;
		case MotionEvent.ACTION_MOVE:
			if (isVertical) {
				// 竖向
				if (lastY - y > 0) {
					// 向上滚动
					moveUp((int) (lastY - y));
					isMoveUp2Down = false;
				} else {
					// 向下滚动
					moveDown((int) (y - lastY));
					isMoveUp2Down = true;
				}
			} else {
				// 横向
				if (lastX - x > 0) {
					// 向左滚动
					moveLeft((int) (lastX - x));
					isMoveRight2Left = true;
				} else {
					// 向右边滚动
					moveRight((int) (x - lastX));
					isMoveRight2Left = false;
				}
			}

			lastX = event.getX();
			lastY = event.getY();

			break;
		case MotionEvent.ACTION_UP:
			startPlay();
			break;
		}

		return true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			length = 0;
			downX = event.getX();
			downY = event.getY();
			lastX = event.getX();
			lastY = event.getY();
			stopPlay();
			break;
		case MotionEvent.ACTION_MOVE:
			// 计算按下后移动的距离
			length = Math.sqrt(Math.pow(x - downX, 2) + Math.pow(y - downY, 2));
			if (isVertical) {
				// 竖向
				if (lastY - y > 0) {
					// 向上滚动
					moveUp((int) (lastY - y));
					isMoveUp2Down = false;
				} else {
					// 向下滚动
					moveDown((int) (y - lastY));
					isMoveUp2Down = true;
				}
			} else {
				// 横向
				if (lastX - x > 0) {
					// 向左滚动
					moveLeft((int) (lastX - x));
					isMoveRight2Left = true;
				} else {
					// 向右边滚动
					moveRight((int) (x - lastX));
					isMoveRight2Left = false;
				}
			}

			lastX = event.getX();
			lastY = event.getY();

			break;
		case MotionEvent.ACTION_UP:
			// 如果移动距离小于10像素，那么就触发点击事件
			if (length < 10) {
				// 点击事件
				v.performClick();
				// 点击事件,延迟2秒后再次自动滚动
				handlerDelayed.sendEmptyMessageDelayed(0, 2000);
			} else {
				startPlay();
			}

			break;
		}

		return true;
	}

	// -----------------------------------------以下是此类提供调用的方法------------------------------------------------------------------------
	/** 左边TextView的字体颜色 */
	private int textColorLeftOrTop = Color.BLACK;
	/** 中边TextView的字体颜色 */
	private int textColorMiddle = Color.BLACK;
	/** 有边TextView的字体颜色 */
	private int textColorRightOrDown = Color.BLACK;
	/** 左边TextView的字体大小 */
	private float textSizeLeftOrTop = 15;
	/** 中边TextView的字体大小 */
	private float textSizeMiddle = 15;
	/** 有边TextView的字体大小 */
	private float textSizeRightOrDown = 15;
	/** 图片的显示大小,单位dp */
	private int imageSize = 50;
	/** 点击事件的回调监听 */
	private OnClickCloudLayoutListner listener;

	public interface OnClickCloudLayoutListner {
		/** 点击事件 */
		public void cloudOnClick(View v, int positonLeftOrTop, int positonMiddle, int positonRightOrDown);
	}

	public void setOnClickCloudLayoutListner(OnClickCloudLayoutListner listener) {
		this.listener = listener;
	}

	/** 设置滚动速度 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/** 设置子控件之间竖向的间距（竖向模式时有效） */
	public void setViewMargin(int viewMargin) {
		this.viewMarginVertical = viewMargin;
	}

	/** 设置滚动放向，true:竖向，false:横向 */
	public void setVertical(boolean isVertical) {
		this.isVertical = isVertical;
	}

	/** 统一设置全部数据的字体颜色 */
	public void setTextColorAll(int textColor) {
		this.textColorLeftOrTop = textColor;
		this.textColorMiddle = textColor;
		this.textColorRightOrDown = textColor;
	}

	/** 统一设置全部数据的字体大小 */
	public void setTextSizeAll(int size) {
		this.textSizeLeftOrTop = size;
		this.textSizeMiddle = size;
		this.textSizeRightOrDown = size;
	}

	/** 设置图片的大小，宽高相同，单位dp */
	public void setImageSize(int size) {
		this.imageSize = size;
	}

	public void setTextColorLeftOrTop(int textColorLeftOrTop) {
		this.textColorLeftOrTop = textColorLeftOrTop;
	}

	public void setTextColorMiddle(int textColorMiddle) {
		this.textColorMiddle = textColorMiddle;
	}

	public void setTextColorRightOrDown(int textColorRightOrDown) {
		this.textColorRightOrDown = textColorRightOrDown;
	}

	public void setTextSizeLeftOrTop(float textSizeLeftOrTop) {
		this.textSizeLeftOrTop = textSizeLeftOrTop;
	}

	public void setTextSizeRightOrDown(float textSizeRightOrDown) {
		this.textSizeRightOrDown = textSizeRightOrDown;
	}

	public void setTextSizeMiddle(float textSizeMiddle) {
		this.textSizeMiddle = textSizeMiddle;
	}

	/**
	 * 设置一条左边的文本显示
	 * 
	 * @param text
	 *            显示的文本
	 * @param position
	 *            组件id，一定要按照数据位置传递(0,1,2,3...n),如果id不按此顺序，则拿不到点击事件的position
	 */
	public void setTextLeftOrTop(String text, int position) {
		TextView tv = creatTextView(text, "-123456" + position);
		tv.setTextColor(textColorLeftOrTop);
		tv.setTextSize(textSizeLeftOrTop);
		leftOrTopViews.add(tv);
		leftOrTopViewsMap.put(position, tv);
		addView(tv);

	}

	/**
	 * 设置一条中间的文本显示
	 * 
	 * @param text
	 *            显示的文本
	 * @param position
	 *            组件id，一定要按照数据位置传递(0,1,2,3...n),如果id不按此顺序，则拿不到点击事件的position
	 */
	public void setTextMiddle(String text, int position) {
		TextView tv = creatTextView(text, "" + position);
		tv.setTextColor(textColorMiddle);
		tv.setTextSize(textSizeMiddle);
		middleViews.add(tv);
		middleViewsMap.put(position, tv);
		addView(tv);
	}

	/**
	 * 设置一条右边的文本显示
	 * 
	 * @param text
	 *            显示的文本
	 * @param position
	 *            组件id，一定要按照数据位置传递(0,1,2,3...n),如果id不按此顺序，则拿不到点击事件的position
	 */
	public void setTextRightOrDown(String text, int position) {
		TextView tv = creatTextView(text, "123456" + position);
		tv.setTextColor(textColorRightOrDown);
		tv.setTextSize(textSizeRightOrDown);
		rightOrDownViews.add(tv);
		rightOrDownViewsMap.put(position, tv);
		addView(tv);
	}

	/**
	 * 设置左边一列Item文字
	 * 
	 * @param list
	 *            需要显示的文本信息
	 */
	public void setTextLeftOrTop(ArrayList<String> list) {
		for (int i = 0; i < list.size(); i++) {
			TextView tv = creatTextView(list.get(i), "-123456" + i);
			tv.setTextColor(textColorLeftOrTop);
			tv.setTextSize(textSizeLeftOrTop);
			leftOrTopViews.add(tv);
			leftOrTopViewsMap.put(i, tv);
			addView(tv);
		}

	}

	/**
	 * 设置中间一列Item文字
	 * 
	 * @param list
	 *            需要显示的文本信息
	 */
	public void setTextMiddle(ArrayList<String> list) {

		for (int i = 0; i < list.size(); i++) {
			TextView tv = creatTextView(list.get(i), "" + i);
			tv.setTextColor(textColorMiddle);
			tv.setTextSize(textSizeMiddle);
			middleViews.add(tv);
			middleViewsMap.put(i, tv);
			addView(tv);
		}
	}

	/**
	 * 设置右边一列Item文字
	 * 
	 * @param list
	 *            需要显示的文本信息
	 */
	public void setTextRightOrDown(ArrayList<String> list) {
		for (int i = 0; i < list.size(); i++) {
			TextView tv = creatTextView(list.get(i), "123456" + i);
			tv.setTextColor(textColorRightOrDown);
			tv.setTextSize(textSizeRightOrDown);
			rightOrDownViews.add(tv);
			rightOrDownViewsMap.put(i, tv);
			addView(tv);
		}
	}

	/**
	 * 设置一条左边的图片显示
	 * 
	 * @param bitmap
	 *            图片资源
	 * @param position
	 *            组件id，一定要按照数据位置传递(0,1,2,3...n),如果id不按此顺序，则拿不到点击事件的position
	 */
	public void setImageLeftOrTop(Bitmap bitmap, int position) {
		ImageView imageView = creatImageView(bitmap, "-123456" + position);
		leftOrTopViews.add(imageView);
		leftOrTopViewsMap.put(position, imageView);
		addView(imageView);
	}

	/**
	 * 设置一条中间的图片显示
	 * 
	 * @param bitmap
	 *            图片资源
	 * @param position
	 *            组件id，一定要按照数据位置传递(0,1,2,3...n),如果id不按此顺序，则拿不到点击事件的position
	 */
	public void setImageMiddle(Bitmap bitmap, int position) {
		ImageView imageView = creatImageView(bitmap, "" + position);
		middleViews.add(imageView);
		middleViewsMap.put(position, imageView);
		addView(imageView);
	}

	/**
	 * 设置一条右边的图片显示
	 * 
	 * @param bitmap
	 *            图片资源
	 * @param position
	 *            组件id，一定要按照数据位置传递(0,1,2,3...n),如果id不按此顺序，则拿不到点击事件的position
	 */
	public void setImageRightOrDown(Bitmap bitmap, int position) {
		ImageView imageView = creatImageView(bitmap, "123456" + position);
		rightOrDownViews.add(imageView);
		rightOrDownViewsMap.put(position, imageView);
		addView(imageView);
	}

	/**
	 * 设置左边图片显示
	 * 
	 * @param list
	 *            图片数据
	 */
	public void setImageLeftOrTop(ArrayList<Bitmap> list) {
		for (int i = 0; i < list.size(); i++) {
			ImageView imageView = creatImageView(list.get(i), "-123456" + i);
			leftOrTopViews.add(imageView);
			leftOrTopViewsMap.put(i, imageView);
			addView(imageView);
		}
	}

	/**
	 * 设置中间图片显示
	 * 
	 * @param list
	 *            图片数据
	 */
	public void setImageMiddle(ArrayList<Bitmap> list) {
		for (int i = 0; i < list.size(); i++) {
			ImageView imageView = creatImageView(list.get(i), "" + i);
			middleViews.add(imageView);
			middleViewsMap.put(i, imageView);
			addView(imageView);
		}
	}

	/**
	 * 设置右边图片显示
	 * 
	 * @param list
	 *            图片数据
	 */
	public void setImageRightOrDown(ArrayList<Bitmap> list) {
		for (int i = 0; i < list.size(); i++) {
			ImageView imageView = creatImageView(list.get(i), "123456" + i);
			rightOrDownViews.add(imageView);
			rightOrDownViewsMap.put(i, imageView);
			addView(imageView);
		}
	}

	/**
	 * 设置左边一列Item文字
	 * 
	 * @param list
	 *            文字信息
	 * @param src
	 *            图片数据
	 */
	public void setTextImageLeftOrTop(ArrayList<String> list, ArrayList<Bitmap> src) {
		for (int i = 0; i < list.size(); i++) {
			LinearLayout tv = creatTextImageView(list.get(i), src.get(i), "-123456" + i);
			leftOrTopViews.add(tv);
			leftOrTopViewsMap.put(i, tv);
			addView(tv);
		}

	}

	/**
	 * 设置中间一列Item文字
	 * 
	 * @param list
	 *            文字信息
	 * @param src
	 *            图片数据
	 */
	public void setTextImageMiddle(ArrayList<String> list, ArrayList<Bitmap> src) {

		for (int i = 0; i < list.size(); i++) {
			LinearLayout tv = creatTextImageView(list.get(i), src.get(i), "" + i);
			middleViews.add(tv);
			middleViewsMap.put(i, tv);
			addView(tv);
		}
	}

	/**
	 * 设置右边一列Item文字 和图片
	 * 
	 * @param list
	 *            文字信息
	 * @param src
	 *            图片数据
	 */
	public void setTextImageRightOrDown(ArrayList<String> list, ArrayList<Bitmap> src) {
		for (int i = 0; i < list.size(); i++) {
			LinearLayout tv = creatTextImageView(list.get(i), src.get(i), "123456" + i);
			rightOrDownViews.add(tv);
			rightOrDownViewsMap.put(i, tv);
			addView(tv);
		}
	}

	public void clear() {
		rightOrDownViews.clear();
		middleViews.clear();
		leftOrTopViews.clear();
		leftOrTopViewsMap.clear();
		middleViewsMap.clear();
		rightOrDownViewsMap.clear();
	}

	// -----------------------------------------以上是此类提供调用的方法------------------------------------------------------------------------

}
