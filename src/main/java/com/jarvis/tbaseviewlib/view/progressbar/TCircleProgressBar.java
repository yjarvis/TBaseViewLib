package com.jarvis.tbaseviewlib.view.progressbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.jarvis.tbaseviewlib.utils.TUtils;

/**
 * 圆形进度条 功能说明:
 * @author 作者：jarvisT
 * @date 创建日期：2015-5-18 下午9:01:08
 */
@SuppressLint("DrawAllocation")
public class TCircleProgressBar extends View {

	private Context context;
	/** 组件宽度(也是绘制的最长宽) */
	private int width = 0;
	/** 组件高度(也是绘制的最长高度) */
	private int height = 0;
	/** 绘制的进度 */
	private int progress = 0;
	/** 大弧形的paint */
	private Paint paint = new Paint();
	/** 文字的paint */
	private Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
	/** 大弧形的背景的paint */
	private Paint paintbg = new Paint();
	/** 绘制圆圈线的宽度 */
	private int strokeWidth = 10;
	/** 绘制文字的大小 */
	private int textSize = 20;
	/** 大弧形 */
	private RectF rectF;
	/** 绘制大弧形的颜色 */
	private int color = Color.RED;
	/** 绘制停止的最大角度 */
	private int stopNumber = 360;
	/** 圆圈的边距 */
	private int offDistance = 25;
	/** 文字停止的加载进度 */
	private int stopProgresstStr = 100;
	/** 文字加载进度 */
	private float progressStr = 0;

	public TCircleProgressBar(Context context) {
		super(context);
		initView(context);
	}

	public TCircleProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public TCircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	private void initView(Context context) {
		width = getWidth();
		height = getHeight();
		this.context = context;

		// 大弧线颜色
		paint.setColor(color);
		paint.setStrokeWidth(strokeWidth);
		// 大弧线设置实心
		paint.setStyle(Paint.Style.STROKE);
		// 抗锯齿
		paint.setAntiAlias(true);
		//设置圆角线条
		paint.setStrokeCap(Cap.ROUND); 
		//文字的设置
		paintText.setTextSize(TUtils.dipTopx(context, textSize));
		paintText.setColor(color);
		// 文字的x坐标以中心为准
		paintText.setTextAlign(Paint.Align.CENTER);

		// 圆圈背景颜色
		paintbg.setColor(Color.parseColor("#F0F0F0"));
		paintbg.setStrokeWidth(strokeWidth);
		// 圆圈背景设置实心
		paintbg.setStyle(Paint.Style.STROKE);
		// 抗锯齿
		paintbg.setAntiAlias(true);

		setStopNumber(100, 0);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 绘制背景
		canvas.drawColor(Color.WHITE);
		// 每次绘制4像素
		progress += 3;
		progressStr+=0.8;
		if (progress >= stopNumber) {
			progress = stopNumber;
		}
		if (progressStr>=stopProgresstStr) {
			progressStr=stopProgresstStr;
		}
		// 圆的背景
		canvas.drawCircle(width / 2, height / 2, width / 2 - offDistance, paintbg);
		// 大弧形
		canvas.drawArc(rectF, 270, 0 + progress, false, paint);
		//绘制文字加载进度
		canvas.drawText((int)progressStr+"%", width / 2, height / 2 + TUtils.dipTopx(context, textSize) / 2 - TUtils.dipTopx(context, 4), paintText);
		// System.out.println("宽：" + width + "    高：" + height);
		// 重绘
		this.postInvalidate();
	}

	/**
	 * 设置停止的位置
	 * 
	 * @param allNumber
	 *            总数
	 * @param stopNumber2
	 *            已经用过的数量
	 */
	public void setStopNumber(int allNumber, int stopNumber2) {
		stopNumber = (int) ((stopNumber2 * 1.0 / allNumber) * stopNumber);

		int temp = 0;
		if ((int) ((stopNumber2 * 1.0 / allNumber) * 100) <1) {
//			temp =(int) (stopNumber2  % allNumber);
//			textStr="0."+temp+"%";
//			return;
			temp=0;
		} else {
			temp = (int) ((stopNumber2 * 1.0 / allNumber) * 100);
		}
		stopProgresstStr = temp;
	}

	/**
	 * 设置绘制弧形的颜色
	 * 
	 * @param color
	 */
	public void setColor(int color) {
		this.color = color;
		// 设置大弧形颜色
		if (color != 0) {
			paint.setColor(color);
		}
	}

	/**
	 * 开始动画 功能说明:
	 * 
	 * @author 作者：jarvisT
	 * @date 创建日期：2015-5-18 下午8:28:55
	 */
	public void start() {
		progress = 0;
	}

	/**
	 * 停止动画 功能说明:
	 * @author 作者：jarvisT
	 * @date 创建日期：2015-5-18 下午8:28:55
	 */
	public void stop() {
		progress = stopNumber;
	}

	/**
	 * 设置圆圈的粗细
	 * @param strokeWidth
	 */
	public void setStrokeWidth(int strokeWidth){
		this.strokeWidth=strokeWidth;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);

		// 大弧形的范围
		rectF = new RectF(offDistance, offDistance, width - offDistance, height - offDistance);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
