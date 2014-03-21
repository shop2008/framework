package com.wxxr.mobile.stock.client.biz;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PathEffect;
import android.view.MotionEvent;

public class StockBaseState implements IStockBaseState {
	public static final int STOCK_RED = Color.parseColor("#BA2514"); //红色
	public static final int STOCK_GREEN = Color.parseColor("#3C7F00"); // 绿色
	public static final int STOCK_YELLOW = Color.parseColor("#FFE400");//黄色
	public static final int STOCK_WHITE = Color.parseColor("#FFFFFF"); // 白色
	public static final int STOCK_BLACK = Color.parseColor("#000000"); //黑色
	public static final int STOCK_FONT = Color.parseColor("#b8b7bf"); //灰色
	public static final int STOCK_BLUE = Color.parseColor("#0966be"); //蓝色
	
	public static final int GRAY_HQ = Color.parseColor("#595b5d");
	public static final int BLACK_FONT = Color.parseColor("#000000");
	private Paint p;
	private Paint mPaint;
	private float wBi;// 起始点宽度比(自定义的)(点坐标/画板宽)
	private final float hBi = (float) 152 / 220;// 起始点纵度比(自定义的)（点坐标/画板高）
	private final float tBi = (float) 20 / 220;// 右上角纵度比(自定义的)（点坐标/画板高）
	private final float zztBi = (float) 168 / 220;// 柱状图上边缘纵度比(自定义的)（点坐标/画板高）
	private final float zzbBi = (float) 213 / 220;// 柱状图下底边纵度比(自定义的)（点坐标/画板高）
	
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
	public int isFiveDay = 4;
	private int strokeColor = STOCK_RED;
	private int borderColor = STOCK_RED;
	public StockBaseState(){
		p = new Paint();
		mPaint = new Paint();
	}
	private void setData(Canvas canvas)
	{
		cWidth = canvas.getWidth();
		cHeight = canvas.getHeight();
		wBi = (float) 68 / cWidth;
		mStartX = cWidth * wBi;
		mStartY = cHeight * hBi;
		mEndX = cWidth - mStartX;
		mEndY = cHeight * tBi;
		zzTopY = cHeight * zztBi;
		zzBottomY = cHeight * zzbBi;
		fenshiWidth = mEndX - mStartX;
		fenshiHeight = mStartY - mEndY;
	}	
	
	@Override
	public void doDraw(Canvas canvas) {
		setData(canvas);
		// 画布和画笔的抗锯齿处理 */
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));		
		/** 画边框************************************************************* */
		/** 设置虚线样式 */
		p.setColor(Color.RED);
		p.setStyle(Paint.Style.STROKE);
		PathEffect effects = new DashPathEffect(new float[]{ 2, 2, 2, 2 }, 1);
		p.setPathEffect(effects);
			/** 横虚线 */
			for (int i = 0; i < 7; i++)
			{
				canvas.drawLine(mStartX, mStartY - i * (fenshiHeight / 6), mEndX, mStartY - i * (fenshiHeight / 6), p);
			}
			// 柱状图的一条横虚线
			canvas.drawLine(mStartX, zzTopY + (zzBottomY - zzTopY) / 2, mEndX, zzTopY + (zzBottomY - zzTopY) / 2, p);
			/** 纵虚线 */
			for (int j = 1; j < isFiveDay; j++)
			{
				float x = mStartX + j * (fenshiWidth / isFiveDay);
				// 分时的
				canvas.drawLine(x, mStartY + 6, x, mEndY - 6, p);
				// 柱状图的
				canvas.drawLine(x, zzTopY, x, zzBottomY, p);
			}
		/** 清空画笔样式 */
		p.setPathEffect(null);
		/** 外围边框 */
		p.setColor(Color.RED);
		canvas.drawLine(mStartX, mEndY - 6, mEndX, mEndY - 6, p);// 分时上方横线
		canvas.drawLine(mStartX, mStartY + 6, mEndX, mStartY + 6, p);// 分时下方横线
		canvas.drawLine(mStartX, mStartY + 6, mStartX, mEndY - 6, p);// 分时左纵线
		canvas.drawLine(mEndX, mStartY + 6, mEndX, mEndY - 6, p);// 分时右纵线

		canvas.drawLine(mStartX, zzTopY, mEndX, zzTopY, p);// 柱状图上方横线
		canvas.drawLine(mStartX, zzBottomY, mEndX, zzBottomY, p);// 柱状图底边横线
		canvas.drawLine(mStartX, zzBottomY, mStartX, zzTopY, p);// 柱状图左纵线
		canvas.drawLine(mEndX, zzBottomY, mEndX, zzTopY, p);// 柱状图右纵线
		canvas.save();		
	}		
	
	@Override
	public void doTouch(MotionEvent event) {
		
	}
	@Override
	public void setFiveDay(int num) {
		if(num>0 && num==5){
			isFiveDay = num;
		}
	}
	@Override
	public void setBorderColor(int sbColor) {
		if(sbColor > 0){
			borderColor = sbColor;
		}
	}
	@Override
	public void setStrokeColor(int xbColor) {
		if(xbColor > 0){
			strokeColor = xbColor;
		}
	}
	@Override
	public void setStockUpColor(int upColor) {
	}
	@Override
	public void setStockDownColor(int downColor) {
	}
	@Override
	public void setStockAverageLineColor(int averagColor) {
		
	}
	@Override
	public void setStockCloseColor(int closeColor) {
	}
}
