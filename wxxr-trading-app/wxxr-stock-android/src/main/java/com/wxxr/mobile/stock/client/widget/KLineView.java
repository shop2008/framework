package com.wxxr.mobile.stock.client.widget;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.View;

import com.wxxr.mobile.core.ui.api.IDataChangedListener;
import com.wxxr.mobile.core.ui.api.IObservableListDataProvider;
import com.wxxr.mobile.stock.app.bean.StockLineBean;
import com.wxxr.mobile.stock.client.biz.Kline;
import com.wxxr.mobile.stock.client.utils.Utils;

/**
 * K线自定义控件
 * 
 * @author renwenjie
 * 
 */
public class KLineView extends View implements IDataChangedListener {

	/**
	 * 手指触摸点横坐标
	 */
	private float touchX;

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
	 * K线起始点纵度比(自定义的)（点坐标/画板高）
	 */
	private final float hBi = (float) 152 / 220;

	/**
	 * K线右上角纵度比(自定义的)（点坐标/画板高）
	 */
	private final float tBi = (float) 12 / 220;

	/**
	 * 柱状图上边缘纵度比(自定义的)（点坐标/画板高）
	 */
	private final float zztBi = (float) 172 / 220;

	/**
	 * 柱状图下底边纵度比(自定义的)（点坐标/画板高）
	 */
	private final float zzbBi = (float) 215 / 220;

	/** 画布的宽 */
	public float cWidth;
	/** 画布的高 */
	public float cHeight;
	/** K线左下角X点 */
	public float mStartX;
	/** K线左下角Y点 */
	public float mStartY;
	/** K线右上角X点 */
	public float mEndX;
	/** K线右上角Y点 */
	public float mEndY;
	/** 柱状图顶边Y点 */
	public float zzTopY;
	/** 柱状图底边Y点 */
	public float zzBottomY;
	/** K线表的宽 */
	public float fenshiWidth;
	/** K线表的高 */
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
	private float maxPrice = 0;

	/**
	 * 最低成交价
	 */
	private float minPrice = 20000;

	/**
	 * 成交量最大值
	 */
	private float maxSecuvolume = 0;

	private IObservableListDataProvider dataProvider;

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
		mPaint = new Paint();
		df = new DecimalFormat("0.00");
		isRender = true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		doDraw(canvas, klineType);
	}

	public void updateCanvas() {
		postInvalidate();
	}

	private void doDraw(Canvas c, int klineType) {
		setBgData(c);
		drawBgProgram(c);
		setKlineData();
		drawKline(c);
	}

	public void setBgData(Canvas c) {
		cWidth = this.getWidth(); // 画布宽
		cHeight = this.getHeight(); // 画布高
		wBi = (float) 65 / cWidth; // 从逻辑的65开始
		mStartX = cWidth * wBi; // 算实际画布开始位置
		mStartY = cHeight * hBi;
		mEndX = cWidth - 1;
		mEndY = cHeight * tBi;
		zzTopY = cHeight * zztBi;
		zzBottomY = cHeight * zzbBi;
		fenshiWidth = mEndX - mStartX - 2;
		fenshiHeight = mStartY - mEndY;
	}

	private void setKlineData() {
		if (dataProvider != null && dataProvider.getItemCounts() > 0) {
			size = dataProvider.getItemCounts();
			count = size <= 50 ? size : 50;// 缺省最多取50个蜡烛图
			maxPrice = Float.parseFloat(String
					.valueOf(((StockLineBean) dataProvider.getItem(0))
							.getHigh()));
			minPrice = Float
					.parseFloat(String.valueOf(((StockLineBean) dataProvider
							.getItem(0)).getLow()));
			maxSecuvolume = Float.parseFloat(String
					.valueOf(((StockLineBean) dataProvider.getItem(0))
							.getSecuvolume()));
			float temp;
			for (int i = 0; i < count; i++) {
				temp = Float.parseFloat(String
						.valueOf(((StockLineBean) dataProvider.getItem(i))
								.getHigh()));
				if (maxPrice < temp)
					maxPrice = temp;
				temp = Float.parseFloat(String
						.valueOf(((StockLineBean) dataProvider.getItem(i))
								.getLow()));
				if (minPrice > temp)
					minPrice = temp;
				temp = Float.parseFloat(String
						.valueOf(((StockLineBean) dataProvider.getItem(i))
								.getSecuvolume()));
				if (maxSecuvolume < temp)
					maxSecuvolume = temp;
			}
		}
	}

	//
	// /**
	// * 刷新界面
	// */
	// public void notifyDataSetChanged() {
	// updateCanvas();
	// }

	void setPaintTextSize(Paint p) {
		if (cWidth >= 1000) {
			mPaint.setTextSize(18);
		} else if (cWidth >= 700) {
			mPaint.setTextSize(16);
		} else if (cWidth >= 570) {
			mPaint.setTextSize(14);
		} else if (cWidth >= 450) {
			mPaint.setTextSize(10);
		} else if (cWidth >= 330) {
			mPaint.setTextSize(10);
		} else {
			mPaint.setTextSize(10);
		}
	}

	public void drawKline(Canvas canvas) {
		// 在背景表格上画数据
		if (dataProvider == null) {
			return;
		}
		if (dataProvider.getItemCounts() == 0) {
			return;
		}
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setAntiAlias(true);
		/** 设置表格左边的股票价格文字 */
		setPaintTextSize(mPaint);
		mPaint.setTextAlign(Paint.Align.RIGHT);
		mPaint.setColor(Color.parseColor("#C43F32"));

		/** 计算左边7个值 */
		float price1 = maxPrice;
		float price2 = (maxPrice - minPrice) * (K_LINE_LABEL_Y_NUM - 1)
				/ K_LINE_LABEL_Y_NUM + minPrice;
		float price3 = (maxPrice - minPrice) * (K_LINE_LABEL_Y_NUM - 2)
				/ K_LINE_LABEL_Y_NUM + minPrice;
		float price4 = (maxPrice - minPrice) * (K_LINE_LABEL_Y_NUM - 3)
				/ K_LINE_LABEL_Y_NUM + minPrice;
		float price5 = minPrice;

		canvas.drawText(formatDouble(price1 / 1000), mStartX - 6, mEndY + 10,
				mPaint);
		canvas.drawText(formatDouble(price2 / 1000), mStartX - 6, mEndY + 10
				+ (fenshiHeight / K_LINE_LABEL_Y_NUM), mPaint);
		mPaint.setColor(Color.WHITE);
		canvas.drawText(formatDouble(price3 / 1000), mStartX - 6, mEndY + 6
				+ fenshiHeight / (K_LINE_LABEL_Y_NUM - 2), mPaint);
		mPaint.setColor(Color.parseColor("#4A9143"));
		canvas.drawText(formatDouble(price4 / 1000), mStartX - 6, mEndY
				+ fenshiHeight * (K_LINE_LABEL_Y_NUM - 1)
				/ (K_LINE_LABEL_Y_NUM), mPaint);
		canvas.drawText(formatDouble(price5 / 1000), mStartX - 6, mStartY,
				mPaint);

		/** 设置表格底边时间文字 */
		setPaintTextSize(mPaint);
		mPaint.setTextAlign(Paint.Align.CENTER);
		mPaint.setColor(Color.WHITE);
		canvas.drawText(
				((StockLineBean) dataProvider.getItem(count * 4 / 5)).getDate(),
				mStartX + (fenshiWidth / 5), mStartY + TOP_BOTTOM_SPACE_HEIGHT
						+ (zzTopY - mStartY) / 2, mPaint);
		canvas.drawText(
				((StockLineBean) dataProvider.getItem(count * 3 / 5)).getDate(),
				mStartX + (fenshiWidth * 2 / 5), mStartY
						+ TOP_BOTTOM_SPACE_HEIGHT + (zzTopY - mStartY) / 2,
				mPaint);
		canvas.drawText(
				((StockLineBean) dataProvider.getItem(count * 2 / 5)).getDate(),
				mStartX + (fenshiWidth * 3 / 5), mStartY
						+ TOP_BOTTOM_SPACE_HEIGHT + (zzTopY - mStartY) / 2,
				mPaint);
		canvas.drawText(
				((StockLineBean) dataProvider.getItem(count / 5)).getDate(),
				mStartX + (fenshiWidth * 4 / 5), mStartY
						+ TOP_BOTTOM_SPACE_HEIGHT + (zzTopY - mStartY) / 2,
				mPaint);

		/** 画成交量柱状图的值 */
		setPaintTextSize(mPaint);
		/** 成交量（万） */
		mPaint.setTextAlign(Paint.Align.RIGHT);
		canvas.drawText(formatNum(maxSecuvolume / 100), mStartX - 6,
				zzTopY + 10, mPaint);
		canvas.drawText(formatNum(maxSecuvolume / 200), mStartX - 6, zzTopY
				+ (zzBottomY - zzTopY) / 2 + 8, mPaint);
		mPaint.setTextSize(16);
		/** 单位（手） */
		canvas.drawText("单位:手", mStartX - 6, zzBottomY + 4, mPaint);

		/** 蜡烛图画法 */
		canvas.save();
		float left = mStartX, top, right, bottom;
		float startX, startY, stopY;

		w = 1f;// fenshiWidth / (count * K_LINE_SPACE);

		/**
		 * 画每个柱状图的白线
		 */
		drawWhiteLine(canvas, mStartX, mEndX, mEndY, zzBottomY, count, w);

		for (int i = count - 1, j = 0; i >= 0; i--, j++) {
			float open = Float.parseFloat(String
					.valueOf(((StockLineBean) dataProvider.getItem(i))
							.getOpen()));
			float high = Float.parseFloat(String
					.valueOf(((StockLineBean) dataProvider.getItem(i))
							.getHigh()));
			float low = Float
					.parseFloat(String.valueOf(((StockLineBean) dataProvider
							.getItem(i)).getLow()));
			float newprice = Float.parseFloat(String
					.valueOf(((StockLineBean) dataProvider.getItem(i))
							.getPrice()));
			float secuvolume = Float.parseFloat(String
					.valueOf(((StockLineBean) dataProvider.getItem(i))
							.getSecuvolume()));
			// Log.d("",
			// "open:"+high+" low:"+low+" newprice:"+newprice+" secuvolume:"+secuvolume);
			if (open >= newprice) {
				mPaint.setColor(Color.parseColor("#329900"));
				top = (float) (mStartY - (open - minPrice) * fenshiHeight
						/ (maxPrice - minPrice));
				right = left + fenshiWidth / count - w;
				bottom = (float) (mStartY - (newprice - minPrice)
						* fenshiHeight / (maxPrice - minPrice));
			} else {
				mPaint.setColor(Color.parseColor("#E92C0C"));
				top = (float) (mStartY - (newprice - minPrice) * fenshiHeight
						/ (maxPrice - minPrice));
				right = left + fenshiWidth / count - w;
				bottom = (float) (mStartY - (open - minPrice) * fenshiHeight
						/ (maxPrice - minPrice));
			}

			int a = (int) top;
			int b = (int) bottom;

			// 画蜡烛
			if (open == newprice && high == low) {
				mPaint.setColor(Color.WHITE);
				mPaint.setStrokeWidth(2);
				canvas.drawLine(left, top, right, bottom, mPaint);
			} else if (open == newprice || a == b) {
				mPaint.setStrokeWidth(2);
				canvas.drawLine(left, top, right, bottom, mPaint);
			} else {
				canvas.drawRect(left, top, right, bottom, mPaint);
			}
			mPaint.setStrokeWidth(1);
			if (open >= newprice) {
				mPaint.setColor(Color.parseColor("#329900"));
			} else {
				mPaint.setColor(Color.parseColor("#E92C0C"));
			}
			// 画柱状图
			if (secuvolume != 0) {
				top = (float) (zzBottomY - secuvolume * (zzBottomY - zzTopY)
						/ maxSecuvolume + 1);
				float deltaY = 0;
				if (top > zzBottomY) {
					deltaY = top - zzBottomY;
					top = top - 2 * deltaY;
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
					top = top + (absDisY - absDisF) + 2;
				}
				canvas.drawRect(left, top, right, zzBottomY, mPaint);
			}
			startX = left + (right - left) / 2;
			startY = (float) (mStartY - (high - minPrice) * fenshiHeight
					/ (maxPrice - minPrice));
			stopY = (float) (mStartY - (low - minPrice) * fenshiHeight
					/ (maxPrice - minPrice));
			// 画中线
			mPaint.setStrokeWidth(2);
			canvas.drawLine(startX, startY, startX, stopY, mPaint);

			left += fenshiWidth / count;

		}
		canvas.restore();
	}

	private void drawWhiteLine(Canvas canvas, float left, float right,
			float top, float bottom, int size, float w) {
		mPaint.setColor(Color.WHITE);
		int whiteLineCount = (int) ((touchX - left) / w / 3);
		if (touchX >= left && touchX <= right && whiteLineCount < size) {
			whiteStartX = left + w + whiteLineCount * 3 * w;
			canvas.drawLine(whiteStartX, top - 6, whiteStartX, bottom, mPaint);
		}
	}

	public void setKlineData(ArrayList<Kline> data) {

	}

	private final static int K_LINE_LABEL_X_NUM = 5;
	private final static int K_LINE_LABEL_Y_NUM = 4;
	private final static int TOP_BOTTOM_SPACE_HEIGHT = 8;

	public void drawBgProgram(Canvas canvas) {
		/** 设置虚线样式 */
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG));
		mPaint.setColor(Color.parseColor("#535353"));
		mPaint.setStyle(Paint.Style.STROKE);

		/** 横虚线 */
		// K线的
		for (int i = 0; i < K_LINE_LABEL_X_NUM + 1; i++) {
			canvas.drawLine(mStartX, mStartY - i
					* (fenshiHeight / K_LINE_LABEL_Y_NUM), mEndX, mStartY - i
					* (fenshiHeight / K_LINE_LABEL_Y_NUM), mPaint);
		}
		// 柱状图的一条横虚线
		canvas.drawLine(mStartX, zzTopY + (zzBottomY - zzTopY) / 2, mEndX,
				zzTopY + (zzBottomY - zzTopY) / 2, mPaint);

		/** 纵虚线 */
		for (int j = 1; j < K_LINE_LABEL_Y_NUM + 1; j++) {
			float x = mStartX + j * (fenshiWidth / K_LINE_LABEL_X_NUM);
			// K线的
			canvas.drawLine(x, mStartY + TOP_BOTTOM_SPACE_HEIGHT, x, mEndY
					- TOP_BOTTOM_SPACE_HEIGHT, mPaint);
			// 柱状图的
			canvas.drawLine(x, zzTopY, x, zzBottomY, mPaint);
		}

		/** 外围边框 */
		canvas.drawLine(mStartX, mEndY - TOP_BOTTOM_SPACE_HEIGHT, mEndX, mEndY
				- TOP_BOTTOM_SPACE_HEIGHT, mPaint);// 分时上方横线
		canvas.drawLine(mStartX, mStartY + TOP_BOTTOM_SPACE_HEIGHT, mEndX,
				mStartY + TOP_BOTTOM_SPACE_HEIGHT, mPaint);// 分时下方横线
		canvas.drawLine(mStartX, mStartY + TOP_BOTTOM_SPACE_HEIGHT, mStartX,
				mEndY - TOP_BOTTOM_SPACE_HEIGHT, mPaint);// 分时左纵线
		canvas.drawLine(mEndX, mStartY + TOP_BOTTOM_SPACE_HEIGHT, mEndX, mEndY
				- TOP_BOTTOM_SPACE_HEIGHT, mPaint);// 分时右纵线
		canvas.drawLine(mStartX, zzTopY, mEndX, zzTopY, mPaint);// 柱状图上方横线
		canvas.drawLine(mStartX, zzBottomY, mEndX, zzBottomY, mPaint);// 柱状图底边横线
		canvas.drawLine(mStartX, zzBottomY, mStartX, zzTopY, mPaint);// 柱状图左纵线
		canvas.drawLine(mEndX, zzBottomY, mEndX, zzTopY, mPaint);// 柱状图右纵线
		canvas.save();
	}

	private String formatDouble(float value) {
		return df.format(Utils.roundHalfUp(value, 2));
	}

	private String formatNum(float value) {
		// 成交量纵坐标小于1万，直接显示，如9453；
		// 大于1万，加万，保留1位小数，如1140.5万；
		// 大于1亿，加亿，保留1位小数，如1.5亿。
		if (value >= 100000000000d) {// 大于等于1千亿
			return Utils.roundHalfUp(value / 100000000, 1) + "亿";// 6,666亿
		} else if (value >= 100000000) {// 大于等于1亿
			return Utils.roundHalfUp((value / 100000000), 1) + "亿";// 666.6亿
		} else if (value > 10000000 && value < 100000000) {// 大于1千万小于1亿
			return Utils.roundHalfUp(value / 10000, 1) + "万";// 6666万
		} else if (value >= 10000) {// 大于等于一万
			return Utils.roundHalfUp((value / 10000), 1) + "万";// 666.6万
		} else {// 小于一万
			return String.valueOf(Utils.roundHalfUp(value, 1));// 6,666
		}
	}

	/**
	 * @return the dataProvider
	 */
	public IObservableListDataProvider getDataProvider() {
		return dataProvider;
	}

	/**
	 * @param dataProvider
	 *            the dataProvider to set
	 */
	public void setDataProvider(IObservableListDataProvider dataProvider) {
		IObservableListDataProvider oldProv = this.dataProvider;
		this.dataProvider = dataProvider;
		if (this.dataProvider != null) {
			this.dataProvider.registerDataChangedListener(this);
		} else if (oldProv != null) {
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
