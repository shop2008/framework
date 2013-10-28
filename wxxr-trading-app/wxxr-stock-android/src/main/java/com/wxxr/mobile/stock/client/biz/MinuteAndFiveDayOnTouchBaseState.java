package com.wxxr.mobile.stock.client.biz;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

public class MinuteAndFiveDayOnTouchBaseState implements IStockTouchBaseState {

	private Context mContext;
	private MinuteLine minuteline;
	private Activity mView;
	private ArrayList<MinuteLine> arrMinuteLines;
	private double mClose;
	private float mX;
	private Paint mPaint = null;
	private boolean nFiveDay=false;
	private int count;
	private ArrayList<Perminute> perminutes;
	public MinuteAndFiveDayOnTouchBaseState(Context context, MinuteLine minuteLine){
		this.mContext = context;
		this.minuteline = minuteLine;
		mPaint = new Paint();
		mPaint.setTextSize(15);
		this.mClose = minuteLine.close;
	}
	
	public MinuteAndFiveDayOnTouchBaseState(Context context, ArrayList<MinuteLine> mLines,boolean isFiveDay){
		this.mContext = context;
		this.arrMinuteLines = mLines;
		this.nFiveDay = isFiveDay;
		mPaint = new Paint();
		if(arrMinuteLines!=null){
			this.mClose = arrMinuteLines.get(mLines.size()-1).close;
		}
		perminutes = new ArrayList<Perminute>();
		if(arrMinuteLines!=null && arrMinuteLines.size()>0){
			for (int i = arrMinuteLines.size()-1; i >= 0; i--) {
				MinuteLine mLine = arrMinuteLines.get(i);
				for (int j = 0, m = mLine.perminutes.size(); j < m; j++) {
					perminutes.add(mLine.perminutes.get(j));
				}
			}			
		}
	}
	/**
	 * 设置5日股票图
	 * */
	
	@Override
	public void doTouch(MotionEvent event) {
		mX = event.getX();
	}

	
	@Override
	public void doDraw(Canvas canvas, float left, float right, float top,
			float botton, float stopX) {
		double newPrice = 0;
		double averagePrice = 0;
		String secuvolume = null;
		String times = null;
		mPaint.setColor(Color.WHITE);
		if(!nFiveDay){
			if (mX >= left && mX <= stopX) {
				canvas.drawLine(mX, top - 6, mX, botton, mPaint);
				count = (int) ((mX - left) * 240 / (right - left));
				newPrice = Double.parseDouble(Tools.formatDouble(minuteline.perminutes.get(count).newprice));
				averagePrice =Double.parseDouble(Tools.formatDouble(minuteline.perminutes.get(count).averageprice));
				secuvolume = Tools.formatNum((minuteline.perminutes.get(count).secuvolume) / 100) + "手";
				times = Tools.getHM(minuteline.perminutes.get(count).fiveDayTime);
				setColor(mClose,newPrice,mPaint);
				canvas.drawText("成交价:"+newPrice, left+10, top-10, mPaint);
				setColor(mClose,averagePrice,mPaint);
				canvas.drawText("均价:"+averagePrice, left+130, top-10, mPaint);
				mPaint.setColor(Color.parseColor("#FFFFFF"));
				canvas.drawText("时间:"+times, left+210, top-10, mPaint);
				canvas.drawText("成交量:"+secuvolume, left+310, top-10, mPaint);				
			}
		}
		if(nFiveDay){
			if (mX >= left && mX <= right) {
				canvas.drawLine(mX, top - 6, mX, botton, mPaint);
				count = (int) ((mX - left) * perminutes.size() / (right - left));
				newPrice = Double.parseDouble(Tools.formatDouble(perminutes.get(count).newprice));
				averagePrice = Double.parseDouble(Tools.formatDouble(perminutes.get(count).averageprice));
				secuvolume = Tools.formatNum((perminutes.get(count).secuvolume) / 100) + "手";
				times = Tools.getHM(perminutes.get(count).fiveDayTime);
				setColor(mClose,newPrice,mPaint);
				canvas.drawText("成交价:"+newPrice, left+10, top-10, mPaint);
				setColor(mClose,averagePrice,mPaint);
				canvas.drawText("均价:"+averagePrice, left+130, top-10, mPaint);
				mPaint.setColor(Color.parseColor("#FFFFFF"));
				canvas.drawText("时间:"+times, left+210, top-10, mPaint);
				canvas.drawText("成交量:"+secuvolume, left+310, top-10, mPaint);				
			}
		}
	}
	
	private void setColor(double close, double value, Paint mPaint){
		if(value > close){
			mPaint.setColor(Color.parseColor("#BA2514"));
		}else if(value < close){
			mPaint.setColor(Color.parseColor("#3C7F00"));
		}else{
			mPaint.setColor(Color.parseColor("#FFFFFF"));
		}		
	}
}
