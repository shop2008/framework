package com.wxxr.mobile.stock.client.biz;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

public class MinuteAndFiveDayLineBaseState extends StockBaseState {
	private Paint mPaint;
	private MinuteLine minuteLine;
	private Context mContext;
	private double close;// 昨收价格
	private long maxSecuvolume;// 成交量最大值
	private double maxCha;// 最新价,均价与昨收价的最大差值
	private int size;
	private ArrayList<Perminute> perminutes;// 每分钟数据
	private Activity mView;
	private float startX, startY, stopX, stopY;
	private boolean zhishu = false;
	private ArrayList<MinuteLine> mFivDayLines; //5日数据
	
	private double maxprice = 0;// 最高价
	private double minprice = 60000;// 最低价
	private double newprice;
	private double averageprice;
	private long secuvolume;
	private double pinjun_zhangdie;
	private int stockUpColor;
	private int stockDownColor;
	private int stockAverageLineColor;
	private int stockCloseColor;	

	private MinuteAndFiveDayOnTouchBaseState minuteLineTouchBase = null;
	
	/**
	 * 分时
	 * */
	public MinuteAndFiveDayLineBaseState(Context context,MinuteLine minuteline){
		this.mContext = context;
		this.minuteLine = minuteline;
		mPaint = new Paint();
		String type =  minuteLine.type;
		if (minuteLine!=null && type!=null ) {
			if(type.equals("0")){
				zhishu = true;
			}
		}	
		minuteLineTouchBase = new MinuteAndFiveDayOnTouchBaseState(mContext, minuteLine);
	}

	/**
	 * 5日
	 * */
	public MinuteAndFiveDayLineBaseState(Context context,ArrayList<MinuteLine> mFiveDayLines, boolean zhishu){
		this.mContext = context;
		this.zhishu = zhishu;
		this.mFivDayLines = mFiveDayLines;
//		this.mView = view;
		mPaint = new Paint();
		minuteLineTouchBase = new MinuteAndFiveDayOnTouchBaseState(mContext, mFivDayLines,true);
	}
	
	
//	public void setDataProvider(MinuteLine minuteline){
//		if(minuteline!=null){
//			this.minuteLine = minuteline;
//			
//		}
//	}
//	public void setDataProvider(ArrayList<MinuteLine> mFiveDayLines){
//		if(mFiveDayLines!=null){
//			this.mFivDayLines = mFiveDayLines;
//				
//		}
//	}
	/**
	 * 获取分时、5day数据
	 * 得到最高价和最低价、最高成交量、计算均价
	 */	
	private void getStockData(){
		if(isFiveDay==5 && mFivDayLines.size() > 0){
			for(int i=0; i< mFivDayLines.size();i++){
				perminutes = mFivDayLines.get(i).perminutes;
				size = perminutes.size();
				for(int j=0; j < size; j++){
					newprice = perminutes.get(j).newprice;
					averageprice = perminutes.get(j).averageprice;
					secuvolume = perminutes.get(j).secuvolume;
					setVerify();
					setCountMaxCha();
				}
			}
		}else{
			close = minuteLine.close;
			perminutes = minuteLine.perminutes;
			size = perminutes.size();
			for (int i = 0; i < size; i++){
				newprice = perminutes.get(i).newprice;
				averageprice = perminutes.get(i).averageprice;
				secuvolume = perminutes.get(i).secuvolume;
				setVerify();
				setZhiShuVerify();
				setCountMaxCha();
			}
		}
	}
	
	private void setCountMaxCha(){
		maxCha = Math.abs(maxprice - close) >= Math.abs(close - minprice) ? Math.abs(maxprice - close) : Math.abs(close - minprice);
		if (maxCha == 0)
		{
			maxCha = Math.abs(close * 0.1);
		}		
	}
	
	private void setVerify(){
		if (maxprice < newprice) maxprice = newprice;
		if (minprice > newprice) minprice = newprice;
		if (!zhishu)
		{
			if (maxprice < averageprice) maxprice = averageprice;
			if (minprice > averageprice) minprice = averageprice;
		}
		if (maxSecuvolume < secuvolume){
			maxSecuvolume = secuvolume;	
		}
	}
	
	
	private void setZhiShuVerify(){
		if (zhishu && minuteLine.code.equals("000001"))
		{
			double dd;
			for (int i = 0; i < size; i++)
			{
				dd = perminutes.get(i).zhangdiexian;
				dd = dd / 100000.0;
				pinjun_zhangdie = close * (1.0 + dd);
				if (maxprice < pinjun_zhangdie){maxprice = pinjun_zhangdie;}
				if (minprice > pinjun_zhangdie){minprice = pinjun_zhangdie;}
			}
		}
		if (zhishu && minuteLine.code.equals("399001"))
		{
			double temp;
			for (int i = 0; i < size; i++)
			{
				temp = perminutes.get(i).zhangdiexian;
				temp = temp / 100000.0;
				pinjun_zhangdie = close * (1.0 + temp);
				if (maxprice < pinjun_zhangdie) maxprice = pinjun_zhangdie;
				if (minprice > pinjun_zhangdie) minprice = pinjun_zhangdie;
			}
		}		
		
	}
	
	
	@Override
	public void doDraw(Canvas canvas) {
		super.doDraw(canvas);
		if (minuteLine.close == 0)
			return;
		//分时数据
		getStockData(); //setMinuteLineData();
		//得到最高价和最低价、最高成交量、计算均价 */
		canvas.save();
		//** 设置分时线表格左边的股票价格文字 */
		setOnDrawLeftTextFont(canvas);
		//** 设置分时线表格右边的涨跌额文字 */
		setOnDrawRightTextFont(canvas);
		//** 设置分时线表格底边时间文字 */
		setOnDrawBottomTextFont(canvas);
		//** 画成交量柱状图的值 */
		setOnDrawSecuVolumeChart(canvas);
		//** 设置均线 只有分时线时候才有*/
		if(isFiveDay==4){
			setOnDrawAverageLine(canvas);
			//** 画分时线 */
			setOnDrawMinuteLine(canvas);
			//** 画成交量柱状图 */
			setOnDrawSecuVolume(canvas);			
		}
		if(isFiveDay==5){
			//** 画5日分时线 */
			setOnDrawFiveDayMinuteLine(canvas);	
			//** 画5日成交量柱状图 */
			setOnDrawFiveDaySecuVolume(canvas);
		}
		canvas.restore();
		if (!Tools.isPortrait(mContext) && isFiveDay==4)
		{
			minuteLineTouchBase.doDraw(canvas, mStartX, mEndX, mEndY, zzBottomY, stopX);
		}
		if(isFiveDay==5){
			float left = (float) (mEndX - fenshiWidth / 5 * mFivDayLines.size());
			minuteLineTouchBase.doDraw(canvas, left, right, mEndY, zzBottomY, stopX);			
		}
	}	
	@Override
	public void doTouch(MotionEvent event) {
		if (!Tools.isPortrait(mContext))
		{
			minuteLineTouchBase.doTouch(event);
		}
	}
	
	/** 设置分时线表格左边的股票价格文字 */
	private void setOnDrawLeftTextFont(Canvas canvas){
		if (cWidth >= 570)
		{
			mPaint.setTextSize(17);
		}
		else if (cWidth >= 450)
		{
			mPaint.setTextSize(15);
		}
		else if (cWidth >= 330)
		{
			mPaint.setTextSize(10);
		}
		else
		{
			mPaint.setTextSize(9);
		}
		mPaint.setTextAlign(Paint.Align.RIGHT);
		mPaint.setColor(STOCK_RED);// 上涨
		canvas.drawText(formatDouble(close + maxCha), mStartX - 3, mEndY + 3, mPaint);
		canvas.drawText(formatDouble((maxCha * 2 / 3) + close), mStartX - 3, mEndY + 3 + (fenshiHeight / 6), mPaint);
		canvas.drawText(formatDouble((maxCha * 1 / 3) + close), mStartX - 3, mEndY + 3 + fenshiHeight / 3, mPaint);
		mPaint.setColor(Color.WHITE); //均线
		canvas.drawText(formatDouble(close), mStartX - 3, mEndY + 3 + fenshiHeight / 2, mPaint);
		mPaint.setColor(STOCK_GREEN); //下跌
		canvas.drawText(formatDouble(close - (maxCha * 1 / 3)), mStartX - 3, mEndY + 3 + (fenshiHeight / 6) * 4, mPaint);
		canvas.drawText(formatDouble(close - (maxCha * 2 / 3)), mStartX - 3, mEndY + 3 + (fenshiHeight / 6) * 5, mPaint);
		canvas.drawText(formatDouble(close - maxCha), mStartX - 3, mStartY - 1, mPaint);		
	}
	
	/** 设置分时线表格右边的涨跌额文字 */
	private void setOnDrawRightTextFont(Canvas canvas){
		mPaint.setTextAlign(Paint.Align.LEFT);
		mPaint.setColor(STOCK_RED);
		canvas.drawText("+" + Tools.formatDouble((maxCha / close) * 100) + "%", mEndX + 3, mEndY + 3, mPaint);
		canvas.drawText("+" + Tools.formatDouble((maxCha * 2 / 3) / close * 100) + "%", mEndX + 3, mEndY + 3
				+ (fenshiHeight / 6), mPaint);
		canvas.drawText("+" + Tools.formatDouble((maxCha * 1 / 3) / close * 100) + "%", mEndX + 3, mEndY + 3
				+ fenshiHeight / 3, mPaint);
		mPaint.setColor(Color.WHITE);
		canvas.drawText("  0.00%", mEndX + 3, mEndY + 3 + fenshiHeight / 2, mPaint);
		mPaint.setColor(STOCK_GREEN);
		canvas.drawText("-" + Tools.formatDouble((maxCha * 1 / 3) / close * 100) + "%", mEndX + 3, mEndY + 3
				+ (fenshiHeight / 6) * 4, mPaint);
		canvas.drawText("-" + Tools.formatDouble((maxCha * 2 / 3) / close * 100) + "%", mEndX + 3, mEndY + 3
				+ (fenshiHeight / 6) * 5, mPaint);
		canvas.drawText("-" + Tools.formatDouble((maxCha / close) * 100) + "%", mEndX + 3, mStartY - 1, mPaint);		
	}
	
	/** 设置分时线表格底边时间文字 */
	private void setOnDrawBottomTextFont(Canvas canvas){
		if (cWidth >= 570)
		{
			mPaint.setTextSize(10);
		}
		else if (cWidth >= 450)
		{
			mPaint.setTextSize(8);
		}
		else if (cWidth >= 330)
		{
			mPaint.setTextSize(7);
		}
		else
		{
			mPaint.setTextSize(5);
		}
		mPaint.setTextAlign(Paint.Align.CENTER);
		mPaint.setColor(Color.WHITE);
		//5日
		if(isFiveDay==5){
			for(int k=0; k< mFivDayLines.size();k++){
				canvas.drawText(mFivDayLines.get(k).datetime, (float) (mStartX + fenshiWidth * (0.9 - k * 0.2)), zzTopY - 2,mPaint);
			}
		}else{
			canvas.drawText("09:30", mStartX, zzTopY - 2, mPaint);
			canvas.drawText("10:30", mStartX + (fenshiWidth / 4), zzTopY - 2, mPaint);
			canvas.drawText("11:30/13:00", mStartX + fenshiWidth / 2, zzTopY - 2, mPaint);
			canvas.drawText("14:00", mStartX + 3 * (fenshiWidth / 4), zzTopY - 2, mPaint);
			canvas.drawText("15:00", mEndX, zzTopY - 2, mPaint);	
		}
	}
	
	/** 画成交量柱状图的值 */
	private void setOnDrawSecuVolumeChart(Canvas canvas){
		if (cWidth >= 570)
		{
			mPaint.setTextSize(16);
		}
		else if (cWidth >= 450)
		{
			mPaint.setTextSize(15);
		}
		else if (cWidth >= 330)
		{
			mPaint.setTextSize(10);
		}
		else
		{
			mPaint.setTextSize(9);
		}
		mPaint.setTextAlign(Paint.Align.RIGHT);
		canvas.drawText(Tools.formatNum(maxSecuvolume / 100), mStartX - 3, zzTopY + 13, mPaint);
		canvas.drawText(Tools.formatNum(maxSecuvolume / 200), mStartX - 3, zzTopY + (zzBottomY - zzTopY) / 2 + 9, mPaint);//(zzBottomY - zzTopY) / 2 = 35
		mPaint.setTextAlign(Paint.Align.LEFT);
		canvas.drawText("单位:手", mEndX + 3, zzBottomY - 3, mPaint);
	}
	
	/**
	 * 设置均线
	 * @param canvas
	 */
	private void setOnDrawAverageLine(Canvas canvas){
		if (!zhishu)
		{
			mPaint.setColor(STOCK_YELLOW);
			for (int i = 0; i < size - 1; i++)
			{
				if (i <= 239)
				{
					startX = mStartX + i * fenshiWidth / 240 + 1;
					startY = (float) ((fenshiHeight / 2 + mEndY) + ((close - perminutes.get(i).averageprice) * 10000
							* fenshiHeight / 2)
							/ (maxCha * 10000));
					stopX = mStartX + (i + 1) * fenshiWidth / 240 + 1;
					stopY = (float) ((fenshiHeight / 2 + mEndY) + ((close - perminutes.get(i + 1).averageprice) * 10000
							* fenshiHeight / 2)
							/ (maxCha * 10000));
					if (perminutes.get(i).averageprice != 0)
					{
						canvas.drawLine(startX, startY, stopX, stopY, mPaint);
					}
				}
			}
		}
		
		if ((zhishu && minuteLine.code.equals("000001"))||(zhishu && minuteLine.code.equals("399001")))
		{
			mPaint.setColor(STOCK_YELLOW);
			double dd;
			double need_data ;
			double dd2 ;
			double need_data2;
			for (int i = 0; i < size - 1; i++)
			{
				if (i <= 239)
				{
					dd = perminutes.get(i).zhangdiexian;
					dd = dd / 100000.0;
					need_data = close * (1.0 + dd);
					// (最新价-昨收)*中间到上边框的像素/(昨收*变化量) ==像素变化量
					startY = (float) ((fenshiHeight / 2 + mEndY) + ((close - need_data) * 10000 * fenshiHeight / 2)
							/ (maxCha * 10000));
					dd2 = perminutes.get(i + 1).zhangdiexian;
					dd2 = dd2 / 100000.0;
					need_data2 = close * (1.0 + dd2);
					stopY = (float) ((fenshiHeight / 2 + mEndY) + ((close - need_data2) * 10000 * fenshiHeight / 2)
							/ (maxCha * 10000));
					startX = mStartX + i * fenshiWidth / 240 + 1;
					stopX = mStartX + (i + 1) * fenshiWidth / 240 + 1;
					canvas.drawLine(startX, startY, stopX, stopY, mPaint);
				}
			}
		}
	}
	/** 画分时线 */
	private void setOnDrawMinuteLine(Canvas canvas){
		for (int i = 0; i < size - 1; i++)
		{
			mPaint.setColor(Color.WHITE);
			if (i <= 239)
			{
				double newprice = perminutes.get(i).newprice;
				// (最新价-昨收)*中间到上边框的像素/(昨收*变化量) ==像素变化量
				startY = (float) ((fenshiHeight / 2 + mEndY) + ((close - newprice) * 10000 * fenshiHeight / 2)
						/ (maxCha * 10000));
				newprice = perminutes.get(i + 1).newprice;
				stopY = (float) ((fenshiHeight / 2 + mEndY) + ((close - newprice) * 10000 * fenshiHeight / 2)
						/ (maxCha * 10000));
				startX = mStartX + i * fenshiWidth / 240 + 1;
				stopX = mStartX + (i + 1) * fenshiWidth / 240 + 1;
				canvas.drawLine(startX, startY, stopX, stopY, mPaint);
				//面积图
				mPaint.setColor(Color.parseColor("#3d3d3d"));
				canvas.drawLine(startX, startY + 1 , startX, mStartY + 6, mPaint);
				canvas.drawLine(stopX, stopY + 1, stopX, mStartY + 6, mPaint);
			}
		}		
	}
	
	/** 画5日分时线 */
	float right = mEndX;
	private void setOnDrawFiveDayMinuteLine(Canvas canvas){
		for(int i=0;i<mFivDayLines.size();i++){
			MinuteLine mLine = mFivDayLines.get(i);
			for(int j=0;j<mLine.perminutes.size()-1; j++){
				mPaint.setColor(Color.WHITE);
				double newprice = mLine.perminutes.get(j).newprice;
				// (最新价-昨收)*中间到上边框的像素/(昨收*变化量) ==像素变化量
				startY = (float) ((fenshiHeight / 2 + mEndY) + ((close - newprice) * 10000 * fenshiHeight / 2)
						/ (maxCha * 10000));
				newprice = mLine.perminutes.get(j + 1).newprice;
				stopY = (float) ((fenshiHeight / 2 + mEndY) + ((close - newprice) * 10000 * fenshiHeight / 2)
						/ (maxCha * 10000));
				startX = mStartX + fenshiWidth / 5 * (4 - i) + j * fenshiWidth / 5 / 48 + 1;
				stopX = mStartX + fenshiWidth / 5 * (4 - i) + (j + 1) * fenshiWidth / 5 / 48 + 1;
				canvas.drawLine(startX, startY, stopX, stopY, mPaint);
				// 面积图
				mPaint.setColor(Color.parseColor("#3d3d3d"));
				canvas.drawLine(startX, startY + 1, startX, mStartY + 6, mPaint);
				canvas.drawLine(stopX, stopY + 1, stopX, mStartY + 6, mPaint);				
			}
			if(i==0){
				right = stopX;
			}
		}
	}
	
	
	/** 画成交量柱状图 */
	private void setOnDrawSecuVolume(Canvas canvas){
		mPaint.setColor(Color.parseColor("#ffe400"));
		for (int i = 1; i < size - 1; i++)
		{
			// 成交量
			if (i <= 239)
			{
				float secuvolume = perminutes.get(i).secuvolume;
				if (secuvolume != 0)
				{
					// 柱状图顶点
					stopY = (float) (zzBottomY - ((secuvolume / maxSecuvolume) * (zzBottomY - zzTopY)));
					startX = mStartX + i * fenshiWidth / 240;
					canvas.drawLine(startX, zzBottomY, startX, stopY, mPaint);
				}
			}
		}		
	}
	/** 画5日成交量柱状图 */
	private void setOnDrawFiveDaySecuVolume(Canvas canvas){
		mPaint.setColor(Color.parseColor("#ffe400"));
		for(int i=0;i<mFivDayLines.size();i++){
			MinuteLine mLine = mFivDayLines.get(i);
			for (int j = 0, m = mLine.perminutes.size() - 1; j < m; j++)
			{
				// 成交量
				float secuvolume = mLine.perminutes.get(j).secuvolume;
				if (secuvolume != 0)
				{
					// 柱状图顶点
					stopY = (float) (zzBottomY - ((secuvolume / maxSecuvolume) * (zzBottomY - zzTopY)));
					startX = mStartX + fenshiWidth / 5 * (4 - i) + j * fenshiWidth / 5 / 48;
					canvas.drawLine(startX, zzBottomY, startX, stopY, mPaint);
				}
			}			
		}
	}
	
	/**
	 * 上涨股票颜色：线条、文字
	 * */
	@Override
	public void setStockUpColor(int upColor) {
		super.setStockUpColor(upColor);
		this.stockUpColor = upColor;
		if(stockUpColor == 0)
			stockUpColor = STOCK_RED;
	}
	
	
	/**
	 * 下跌股票颜色：线条、文字
	 * 
	 * */
	@Override
	public void setStockDownColor(int downColor) {
		super.setStockDownColor(downColor);
		this.stockDownColor = downColor;
		if(stockDownColor == 0)
			stockDownColor = STOCK_GREEN;
		
	}
	/**
	 * 昨收股票颜色：线条、文字
	 * 
	 * */
	@Override
	public void setStockCloseColor(int closeColor) {
		super.setStockCloseColor(closeColor);
		this.stockCloseColor = closeColor;
		if(stockCloseColor==0)
			stockCloseColor = STOCK_WHITE;
	}
	
	
	
	@Override
	public void setStockAverageLineColor(int averagColor) {
		super.setStockAverageLineColor(averagColor);
		this.stockAverageLineColor = averagColor;
		if(stockAverageLineColor==0)
			stockAverageLineColor = STOCK_YELLOW;
	}
	
	//格式化
	private String formatDouble(double value)
	{
		if (minuteLine.code!=null && minuteLine.code.substring(0, 1).equals("9"))
		{
			return Tools.formatDouble3(value);
		}
		else
		{
			return Tools.formatDouble(value);
		}
	}	
}
