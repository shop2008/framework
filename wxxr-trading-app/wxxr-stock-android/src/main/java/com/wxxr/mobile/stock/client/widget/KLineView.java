package com.wxxr.mobile.stock.client.widget;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.core.ui.api.IDataChangedListener;
import com.wxxr.mobile.core.ui.api.IObservableListDataProvider;
import com.wxxr.mobile.stock.client.biz.Kline;
import com.wxxr.mobile.stock.client.biz.Stock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;


/**
 * K线自定义控件
 * @author renwenjie
 *
 */
public class KLineView extends SurfaceView implements Callback,IDataChangedListener {

	private SurfaceHolder holder;

	private Stock stock;

	/**
	 * 手指触摸点横坐标
	 */
	private float touchX ;
	
	/**
	 * 白线起始点坐标
	 */
	private float whiteStartX;
	
	
	/**
	 * 用于格式化数据
	 */
	private static DecimalFormat df;
	/**
	 * 画笔工具
	 */
	private Paint mPaint;

	/**
	 * k线类型 0--表示日k, 1--表示周k, 2--表示月k, 其值通过配置文件传递
	 */
	private int klineType;
	/**
	 * 起始点纵度比(自定义的)（点坐标/画板高）
	 */
	private final float hBi = (float) 152 / 220;

	/**
	 * 右上角纵度比(自定义的)（点坐标/画板高）
	 */
	private final float tBi = (float) 20 / 220;

	/**
	 * 柱状图上边缘纵度比(自定义的)（点坐标/画板高）
	 */
	private final float zztBi = (float) 168 / 220;

	/**
	 * 柱状图下底边纵度比(自定义的)（点坐标/画板高）
	 */
	private final float zzbBi = (float) 213 / 220;

	/** 画布的宽 */
	public float cWidth;
	/** 画布的高 */
	public float cHeight;
	/** 分时表左下角X点 */
	public float mStartX;
	/** 分时表左下角Y点 */
	public float mStartY;
	/** 分时表右上角X点 */
	public float mEndX;
	/** 分时表右上角Y点 */
	public float mEndY;
	/** 柱状图顶边Y点 */
	public float zzTopY;
	/** 柱状图底边Y点 */
	public float zzBottomY;
	/** 分时线表的宽 */
	public float fenshiWidth;
	/** 分时线表的高 */
	public float fenshiHeight;

	private boolean isRender = false;
	/**
	 * 起始点宽度比(自定义的)(点坐标/画板宽)
	 */
	private float wBi;

	/**
	 * kLines.size()
	 */
	private int size;

	/**
	 * 要求蜡烛显示的数量
	 */
	private int count = 50;

	/**
	 * 最高成交价
	 */
	private double maxPrice = 0;

	/**
	 * 最低成交价
	 */
	private double minPrice = 20000;

	/**
	 * 成交量最大值
	 */
	private double maxSecuvolume = 0;
	
	private IObservableListDataProvider dataProvider;

	private SurfaceHolder mHolder;

//	private List<Kline> dataProvider;
	public float w;

	public KLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public KLineView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public KLineView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		holder = getHolder();
		holder.addCallback(this);
		mPaint = new Paint();
		df = new DecimalFormat("0.00");
		isRender = true;
	}

	
	public void setStock(Stock stock) {
		this.stock = stock;
	}
	
	

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		this.mHolder = holder;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		this.mHolder = null;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		touchX = event.getX();
		return true;
	}
	

	public void updateCanvas() {
		Canvas c = null;
		try {
			c = mHolder.lockCanvas(null);
			/**
			 *  指定背景
			 */
			if (c != null) {
				c.drawColor(Color.BLACK);
				/**
				 * 画图
				 */
				doDraw(c, klineType);
			}
		} finally {
			if (c != null)
				mHolder.unlockCanvasAndPost(c);
		}
	}

	private void doDraw(Canvas c, int klineType) {

		setData(c);
		drawBgProgram(c);
		if (dataProvider != null) {
			drawKline(c);
		}

	}

	public void setData(Canvas c) {
		cWidth = c.getWidth();
		cHeight = c.getHeight();
		wBi = (float) 65 / cWidth;
		mStartX = cWidth * wBi;
		mStartY = cHeight * hBi;
		mEndX = cWidth - 1;
		mEndY = cHeight * tBi;
		zzTopY = cHeight * zztBi;
		zzBottomY = cHeight * zzbBi;
		fenshiWidth = mEndX - mStartX - 2;
		fenshiHeight = mStartY - mEndY;
	}

	private void setData2() {
		if (dataProvider != null && dataProvider.getItemCounts() > 0) {
			size = dataProvider.getItemCounts();
			count = size <= 50 ? size : 50;// 缺省最多取50个蜡烛图

			double temp;
			for (int i = 0; i < count; i++) {
				temp = Double.parseDouble(((Kline)dataProvider.getItem(i)).high);
				if (maxPrice < temp)
					maxPrice = temp;
				temp = Double.parseDouble(((Kline)dataProvider.getItem(i)).low);
				if (minPrice > temp)
					minPrice = temp;
				temp = Double.parseDouble(((Kline)dataProvider.getItem(i)).secuvolume);
				if (maxSecuvolume < temp)
					maxSecuvolume = temp;
			}
		}
	}
	
	/**
	 * 刷新界面
	 */
	public void notifyDataSetChanged() {
			updateCanvas();
	}

	public void drawKline(Canvas canvas) {
		// 在背景表格上画数据
		
		
		if (dataProvider == null) {
			return;
		}
		if (dataProvider.getItemCounts() == 0) {
			return;
		}

		setData2();
		/** 画各项指标从这里开始************************************************ */

		/** 清空画笔样式 */
		mPaint.setPathEffect(null);
		canvas.save();

		/** 设置表格左边的股票价格文字 */
		if (cWidth >= 570) {
			mPaint.setTextSize(10);
		} else if (cWidth >= 450) {
			mPaint.setTextSize(8);
		} else if (cWidth >= 330) {
			mPaint.setTextSize(6);
		} else {
			mPaint.setTextSize(4);
		}
		mPaint.setTextAlign(Paint.Align.RIGHT);
		mPaint.setColor(Color.WHITE);

		/** 计算左边7个值 */
		double price1 = maxPrice;
		double price2 = (maxPrice - minPrice) * 5 / 6 + minPrice;
		double price3 = (maxPrice - minPrice) * 2 / 3 + minPrice;
		double price4 = (maxPrice - minPrice) / 2 + minPrice;
		double price5 = (maxPrice - minPrice) / 3 + minPrice;
		double price6 = (maxPrice - minPrice) / 6 + minPrice;
		double price7 = minPrice;

		canvas.drawText(formatDouble(price1), mStartX - 3, mEndY + 3, mPaint);
		canvas.drawText(formatDouble(price2), mStartX - 3, mEndY + 3
				+ (fenshiHeight / 6), mPaint);
		canvas.drawText(formatDouble(price3), mStartX - 3, mEndY + 3
				+ fenshiHeight / 3, mPaint);
		canvas.drawText(formatDouble(price4), mStartX - 3, mEndY + 3
				+ fenshiHeight / 2, mPaint);
		canvas.drawText(formatDouble(price5), mStartX - 3, mEndY + 3
				+ (fenshiHeight / 6) * 4, mPaint);
		canvas.drawText(formatDouble(price6), mStartX - 3, mEndY + 3
				+ (fenshiHeight / 6) * 5, mPaint);
		canvas.drawText(formatDouble(price7), mStartX - 3, mStartY - 1, mPaint);

		/** 设置表格底边时间文字 */
		if (cWidth >= 570) {
			mPaint.setTextSize(10);
		} else if (cWidth >= 450) {
			mPaint.setTextSize(8);
		} else if (cWidth >= 330) {
			mPaint.setTextSize(6);
		} else {
			mPaint.setTextSize(4);
		}
		mPaint.setTextAlign(Paint.Align.CENTER);
		mPaint.setColor(Color.WHITE);
		canvas.drawText(((Kline)dataProvider.getItem(0)).date, mStartX + 31, zzTopY - 1, mPaint);
		canvas.drawText(((Kline)dataProvider.getItem(count / 2)).date, mStartX + fenshiWidth / 2,
				zzTopY - 1, mPaint);
		canvas.drawText(((Kline)dataProvider.getItem(count - 1)).date, mEndX - 31, zzTopY - 1,
				mPaint);

		/** 画成交量柱状图的值 */
		if (cWidth >= 570) {
			mPaint.setTextSize(10);
		} else if (cWidth >= 450) {
			mPaint.setTextSize(8);
		} else if (cWidth >= 330) {
			mPaint.setTextSize(6);
		} else {
			mPaint.setTextSize(4);
		}
		/** 成交量（万） */
		mPaint.setTextAlign(Paint.Align.RIGHT);
		canvas.drawText(formatNum(maxSecuvolume / 100), mStartX - 3,
				zzTopY + 10, mPaint);
		canvas.drawText(formatNum(maxSecuvolume / 200), mStartX - 3, zzTopY
				+ (zzBottomY - zzTopY) / 2 + 8, mPaint);
		/** 单位（手） */
		canvas.drawText("单位:手", mStartX - 3, zzBottomY + 4, mPaint);

		/** 蜡烛图画法 */
		/** 清空画笔样式 */
		mPaint.setPathEffect(null);// 轨道效果
		canvas.save();
		float left = mStartX, top, right, bottom;
		float startX, startY, stopY;
		mPaint.setStyle(Paint.Style.FILL);

		w = fenshiWidth / (count * 3);

		
		/**
		 * 画每个柱状图的白线
		 */
		drawWhiteLine(canvas, mStartX, mEndX, mEndY, zzBottomY, count, w);
	
		for (int i = 0; i < count; i++) {

			double open = Double.parseDouble(((Kline)dataProvider.getItem(i)).open);
			double high = Double.parseDouble(((Kline)dataProvider.getItem(i)).high);
			double low = Double.parseDouble(((Kline)dataProvider.getItem(i)).low);
			double newprice = Double.parseDouble(((Kline)dataProvider.getItem(i)).newprice);
			double secuvolume = Double.parseDouble(((Kline)dataProvider.getItem(i)).secuvolume);

			if (open > newprice) {
				mPaint.setColor(Color.parseColor("#3C7F00"));
				top = (float) (mStartY - (open - minPrice) * fenshiHeight
						/ (maxPrice - minPrice));
				right = left + w * 2;
				bottom = (float) (mStartY - (newprice - minPrice)
						* fenshiHeight / (maxPrice - minPrice));
			} else {
				mPaint.setColor(Color.parseColor("#BA2514"));
				top = (float) (mStartY - (newprice - minPrice) * fenshiHeight
						/ (maxPrice - minPrice));
				right = left + w * 2;
				bottom = (float) (mStartY - (open - minPrice) * fenshiHeight
						/ (maxPrice - minPrice));
			}

			int a = (int) top;
			int b = (int) bottom;
			// 画蜡烛
			if (open == newprice || a == b) {
				canvas.drawLine(left, top, right, bottom, mPaint);
			} else {
				canvas.drawRect(left, top, right, bottom, mPaint);
			}

			// 画柱状图
			if (secuvolume != 0) {
				
				top = (float) (zzBottomY - secuvolume * (zzBottomY - zzTopY)
						/ maxSecuvolume + 1);
				float deltaY = 0;
				if (top > zzBottomY) {
					deltaY = top - zzBottomY;
					top = top - 2* deltaY;
				}
				
				/**
				 * 柱状图高度
				 */
				float absDisY = zzBottomY - top;
				/**
				 * 底部边框高度
				 */
				float absDisF = zzBottomY - zzTopY;
				

				if (absDisY > absDisF) {
					top = top + (absDisY - absDisF)+2;
				}
				canvas.drawRect(left, top, right, zzBottomY, mPaint);
			}
			startX = left + (right - left) / 2;
			startY = (float) (mStartY - (high - minPrice) * fenshiHeight
					/ (maxPrice - minPrice));
			stopY = (float) (mStartY - (low - minPrice) * fenshiHeight
					/ (maxPrice - minPrice));
			// 画中线
			canvas.drawLine(startX, startY, startX, stopY, mPaint);

			left += w * 3;

		}
		canvas.restore();
	}


	private void drawWhiteLine(Canvas canvas, float left, float right,
			float top, float bottom, int size, float w) {
		mPaint.setColor(Color.WHITE);
		int whiteLineCount = (int) ((touchX - left) / w /3);
		if (touchX >= left && touchX <= right && whiteLineCount < size) {
			whiteStartX = left + w + whiteLineCount * 3 * w;
			canvas.drawLine(whiteStartX, top - 6, whiteStartX, bottom, mPaint);
		}
	}

	public void setKlineData(ArrayList<Kline> data) {

	}

	public void drawBgProgram(Canvas canvas) {
		/** 设置虚线样式 */
		mPaint.setColor(Color.RED);
		mPaint.setStyle(Paint.Style.STROKE);
		// mPaint.setColor(Color.DKGRAY);
		// Path path = new Path();
		// path.moveTo(0, 10);
		// path.lineTo(480, 10);
		PathEffect effects = new DashPathEffect(new float[] { 2, 2, 2, 2 }, 1);
		mPaint.setPathEffect(effects);
		// canvas.drawPath(path, mPaint);

		/** 横虚线 */

		// if (!Tools.isPortrait(context))
		// {
		// 分时的
		for (int i = 0; i < 7; i++) {
			canvas.drawLine(mStartX, mStartY - i * (fenshiHeight / 6), mEndX,
					mStartY - i * (fenshiHeight / 6), mPaint);
		}
		// 柱状图的一条横虚线
		canvas.drawLine(mStartX, zzTopY + (zzBottomY - zzTopY) / 2, mEndX,
				zzTopY + (zzBottomY - zzTopY) / 2, mPaint);

		/** 纵虚线 */
		for (int j = 1; j < 4; j++) {
			float x = mStartX + j * (fenshiWidth / 4);
			// 分时的
			canvas.drawLine(x, mStartY + 6, x, mEndY - 6, mPaint);
			// 柱状图的
			canvas.drawLine(x, zzTopY, x, zzBottomY, mPaint);
		}
		// }

		/** 清空画笔样式 */
		mPaint.setPathEffect(null);

		/** 外围边框 */
		// mPaint.setColor(Color.DKGRAY);
		mPaint.setColor(Color.RED);
		canvas.drawLine(mStartX, mEndY - 6, mEndX, mEndY - 6, mPaint);// 分时上方横线
		canvas.drawLine(mStartX, mStartY + 6, mEndX, mStartY + 6, mPaint);// 分时下方横线
		canvas.drawLine(mStartX, mStartY + 6, mStartX, mEndY - 6, mPaint);// 分时左纵线
		canvas.drawLine(mEndX, mStartY + 6, mEndX, mEndY - 6, mPaint);// 分时右纵线
		canvas.drawLine(mStartX, zzTopY, mEndX, zzTopY, mPaint);// 柱状图上方横线
		canvas.drawLine(mStartX, zzBottomY, mEndX, zzBottomY, mPaint);// 柱状图底边横线
		canvas.drawLine(mStartX, zzBottomY, mStartX, zzTopY, mPaint);// 柱状图左纵线
		canvas.drawLine(mEndX, zzBottomY, mEndX, zzTopY, mPaint);// 柱状图右纵线
		canvas.save();
	}

	private String formatDouble(double value) {

		if (stock.code.substring(0, 1).equals("9")) {
			return formatDouble(round(value, 3), 3);
		} else {
			return df.format(round(value, 2));
		}
	}

	private String formatNum(double value) {
		// 成交量纵坐标小于1万，直接显示，如9453；
		// 大于1万，加万，保留1位小数，如1140.5万；
		// 大于1亿，加亿，保留1位小数，如1.5亿。
		if (value >= 100000000000d) {// 大于等于1千亿
			return round(value / 100000000, 1) + "亿";// 6,666亿
		} else if (value >= 100000000) {// 大于等于1亿
			return round((value / 100000000), 1) + "亿";// 666.6亿
		} else if (value > 10000000 && value < 100000000) {// 大于1千万小于1亿
			return round(value / 10000, 1) + "万";// 6666万
		} else if (value >= 10000) {// 大于等于一万
			return round((value / 10000), 1) + "万";// 666.6万
		} else {// 小于一万
			return String.valueOf(round(value, 1));// 6,666
		}
	}
	
	
	/**
	 * FIXME 提供小数位四舍五入处理。如果不是科学计算，任何商业计算都应该使用BigDecimal而不是double，因为double类型不精确。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后“最多”保留几位
	 * @return 四舍五入后的结果
	 */
	private double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * FIXME “#”是可以为空，“0”是不够添0占位，“，”是分隔符;例如“#，##0.0#”
	 * 
	 * @param v
	 *            需要格式化的数字
	 * @param scale
	 *            小数点后“至少”保留几位
	 * @return
	 */
	public String formatDouble(double v, int scale) {
		String temp = "0.";
		for (int i = 0; i < scale; i++) {
			temp += "0";
		}
		return new java.text.DecimalFormat(temp).format(v);
	}

	/**
	 * FIXME 小数转化为百分数,保留2位小数(有四舍五入)
	 * 
	 * @param -0.12345
	 * @return -12.35%
	 */
	public String formatToPercent(double value) {
		Double ret = null;
		value = value * 100;
		int precision = 2;
		try {
			double factor = Math.pow(10, precision);
			ret = new Double(Math.floor(value * factor + 0.5) / factor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String tmp = String.valueOf(ret);
		if (tmp.substring(tmp.indexOf('.') + 1).length() < 2) {
			tmp = tmp + "0";
		}
		return tmp + "%";
	}

	/**
	 * @return the dataProvider
	 */
	public IObservableListDataProvider getDataProvider() {
		return dataProvider;
	}

	/**
	 * @param dataProvider the dataProvider to set
	 */
	public void setDataProvider(IObservableListDataProvider dataProvider) {
		IObservableListDataProvider oldProv = this.dataProvider;
		this.dataProvider = dataProvider;
		if(this.dataProvider != null){
			this.dataProvider.registerDataChangedListener(this);
		}else if(oldProv != null){
			oldProv.unregisterDataChangedListener(this);
		}
	}

	@Override
	public void dataItemChanged() {
		updateCanvas();
	}

	@Override
	public void dataSetChanged() {
		updateCanvas();
	}

}
