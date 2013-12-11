package com.wxxr.mobile.stock.client.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.IDataChangedListener;
import com.wxxr.mobile.core.ui.api.IObservableListDataProvider;
import com.wxxr.mobile.stock.app.bean.StockMinuteLineBean;
import com.wxxr.mobile.stock.client.utils.Utils;

public class MinuteLineView extends BasicLineView  implements IDataChangedListener  {
	
	private static Trace log = Trace.getLogger(MinuteLineView.class);
	private String stockType = "1";
	private IObservableListDataProvider dataProvider;
	private Paint mPaint;
	
	private float yesterdayClose; //昨天收价格
	private float highPrice;  //最高价
	private float secondPrice; //
	private float fourthPrice;
	private float lowPrice; //最低价
	private float maxSecuvolume; // 成交量最大值
	
	private float startX, startY, stopX, stopY;
	private int size;
	private boolean isZhiShu = false; //是否是指数，默认是false
	
	public MinuteLineView(Context context) {
		super(context);
		init();
	}
	public MinuteLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public MinuteLineView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	/**昨收价*/
	public void setStockClose(String val){
		if(val!=null)
		this.yesterdayClose = Float.parseFloat(val);
	}
	
	/**股票类型0-指数；1-个股*/
	public void setStockType(String val){
		this.stockType = val;
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
	public void dataSetChanged() {
		invalidate();
	}
	@Override
	public void dataItemChanged() {
		invalidate();
	}
	/**
	 * 初始化
	 * */
	private void init(){
		mPaint = new Paint();
	}
	
	/**初始化股票数据*/
	private void initStockData(){
		float newprice; //最新价
		float minprice = 0; //最低价
		float maxprice = 0; //最高价
		if(dataProvider==null && yesterdayClose == 0){
			log.info("MinuteLineView dataProvider="+dataProvider + "yesterdayClose="+yesterdayClose);
			return;
		}
		if(stockType.equals("0")){
			this.isZhiShu = true;
		}
		if(dataProvider.getItemCounts()>0){
			this.size = dataProvider.getItemCounts();
			float tempPrice = ((StockMinuteLineBean)dataProvider.getItem(0)).getPrice();
			float secuvolume = ((StockMinuteLineBean)dataProvider.getItem(0)).getSecuvolume();
			maxprice = tempPrice;
			minprice = tempPrice;
			for(int i=0;i<size;i++){
				StockMinuteLineBean stockMinuteLine = (StockMinuteLineBean)dataProvider.getItem(i);
				if(stockMinuteLine==null){
					return;
				}
				newprice = stockMinuteLine.getPrice();
				if(newprice - yesterdayClose>=0 && newprice > maxprice){
					maxprice = newprice;
				}
				if(newprice - yesterdayClose<0 && newprice < minprice){
					minprice = newprice;
				}
				if(!isZhiShu){
					float avprice = stockMinuteLine.getAvprice();
					if(avprice - yesterdayClose >=0 && avprice > maxprice){
						maxprice = avprice;
					}
					if(avprice - yesterdayClose < 0 && avprice < minprice){
						minprice = avprice;
					}
				}
				
				if(isZhiShu){
					float avgChangeRate = stockMinuteLine.getAvgChangeRate();
					float price = (float) (yesterdayClose * (avgChangeRate/100000.0))+yesterdayClose;
					if((price - yesterdayClose) >=0 && price > maxprice){
						maxprice = price;
					}
					if(price - yesterdayClose < 0 && price < minprice){
						minprice = price;
					}
				}
				
				float temp1 = stockMinuteLine.getSecuvolume();
				if(temp1 > secuvolume){
					secuvolume = temp1;
				}				
			}
			
			if(maxprice - yesterdayClose > yesterdayClose - minprice){
				highPrice = maxprice;
				lowPrice = yesterdayClose - (highPrice - yesterdayClose);
			}else{
				lowPrice = minprice;
				highPrice = yesterdayClose + (yesterdayClose - lowPrice);
			}
			if(lowPrice < 0){
				lowPrice = 0;
			}
			maxSecuvolume = secuvolume;
			secondPrice = (highPrice - yesterdayClose)/2+yesterdayClose;
			fourthPrice = yesterdayClose - (yesterdayClose - lowPrice)/2;			
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		canvas.save();
		/** 画布和画笔的抗锯齿处理*** */
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));		
		initStockData();
		/** 设置分时线表格左边的股票价格文字 */
		setOnDrawLeftTextFont(canvas);
		/** 设置分时线表格右边的涨跌额文字 */
		setOnDrawRightTextFont(canvas);	
		/** 画分时线 */
		setOnDrawMinuteLine(canvas);
		/** 画均线 */
		setOnDrawAverageLine(canvas);
		/** 设置分时线表格底边时间文字 */
		setOnDrawBottomTextFont(canvas);
		/** 画成交量柱状图的值 */
		setOnDrawSecuVolumeChart(canvas);
		/** 画成交量柱状图 */
		setOnDrawSecuVolume(canvas);	
		canvas.restore();
	}
	
	/** 设置分时线表格左边的股票价格文字 */
	private void setOnDrawLeftTextFont(Canvas canvas){
		mPaint.setAntiAlias(true);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));  
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
		float textMarginLeft = fStartX - 5;
		float padding = 5;
		mPaint.setTextAlign(Paint.Align.RIGHT);
		mPaint.setColor(getStockUpColor());// 上涨
		String tempHighPice = String.format("%.2f", highPrice/1000);	
		canvas.drawText(tempHighPice, textMarginLeft, fStartY + padding, mPaint);
		String tempSecondPice = String.format("%.2f", secondPrice/1000);
		canvas.drawText(tempSecondPice, textMarginLeft, fStartY + padding + lineHeight*1 , mPaint);	
		mPaint.setColor(getStockCloseColor()); //均线
		String tempClosePice = String.format("%.2f", yesterdayClose/1000);
		canvas.drawText(tempClosePice, textMarginLeft, fStartY + padding + lineHeight*2, mPaint);
		mPaint.setPathEffect(null);
		mPaint.setColor(getStockDownColor()); //下跌
		String tempfourthPrice = String.format("%.2f", fourthPrice/1000);
		canvas.drawText(tempfourthPrice, textMarginLeft, fStartY + padding + lineHeight*3, mPaint);
		String templowPrice = String.format("%.2f", lowPrice/1000);
		canvas.drawText(templowPrice, textMarginLeft, fStartY + padding + lineHeight*4, mPaint);		
	}
	
	/** 设置分时线表格右边的涨跌额文字 */
	private void setOnDrawRightTextFont(Canvas canvas){
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
		float padding = 5;
		float textMarginRight = fStopX + 5;
		mPaint.setAntiAlias(true);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));  
		mPaint.setTextAlign(Paint.Align.LEFT);
		mPaint.setColor(getStockUpColor());
		if(highPrice>0 && yesterdayClose>0){
			String highPicePercentage = String.format("%.2f%%", (highPrice - yesterdayClose)/yesterdayClose*100.00);
			canvas.drawText(highPicePercentage, textMarginRight, fStartY + padding, mPaint);
		}else{
			canvas.drawText("0.00%", textMarginRight, fStartY + padding, mPaint);
		}
		if(secondPrice>0 && yesterdayClose>0){
			String secondPricePercentage = String.format("%.2f%%", (secondPrice - yesterdayClose)/yesterdayClose*100.00);
			canvas.drawText(secondPricePercentage, textMarginRight, fStartY + padding + lineHeight*1, mPaint);
		}else{
			canvas.drawText("0.00%", textMarginRight, fStartY + padding + lineHeight*1, mPaint);
		}
		mPaint.setColor(getStockCloseColor());
		canvas.drawText("0.00%", textMarginRight, fStartY + padding + lineHeight*2, mPaint);
		mPaint.setColor(getStockDownColor());
		if(fourthPrice>0 && yesterdayClose>0){
			String fourthPercentage = String.format("%.2f%%", (yesterdayClose - fourthPrice)/yesterdayClose*100.00);
			canvas.drawText(fourthPercentage, textMarginRight, fStartY + padding + lineHeight*3, mPaint);
		}else{
			canvas.drawText("0.00%", textMarginRight, fStartY + padding + lineHeight*3, mPaint);
		}
		if(lowPrice>0 && yesterdayClose>0){
			String lowPicePercentage = String.format("%.2f%%", (yesterdayClose - lowPrice)/yesterdayClose*100.00);
			canvas.drawText(lowPicePercentage, textMarginRight, fStartY + padding + lineHeight*4, mPaint);
		}else{
			canvas.drawText("0.00%", textMarginRight, fStartY + padding + lineHeight*4, mPaint);
		}
	}
	
	/** 设置分时线表格底边时间文字 */
	private void setOnDrawBottomTextFont(Canvas canvas){
		
		if (cWidth >= 700)
		{
			mPaint.setTextSize(16);
		}
		else if (cWidth >= 570)
		{
			mPaint.setTextSize(14);
		}
		else if (cWidth >= 450)
		{
			mPaint.setTextSize(10);
		}
		else if (cWidth >= 330)
		{
			mPaint.setTextSize(9);
		}
		else
		{
			mPaint.setTextSize(8);
		}
		mPaint.setAntiAlias(true);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));  
		mPaint.setColor(getStockCloseColor());
		float tempY = zStartY - (textLineHeight/3);
		mPaint.setTextAlign(Paint.Align.LEFT);
		canvas.drawText("09:30", fStartX, tempY, mPaint);
		mPaint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText("10:30", fStartX + tableW *1, tempY, mPaint);
		canvas.drawText("11:30/13:00",fStartX + tableW *2, tempY, mPaint);
		canvas.drawText("14:00", fStartX + tableW *3, tempY, mPaint);
		mPaint.setTextAlign(Paint.Align.RIGHT);
		canvas.drawText("15:00", fStopX, tempY, mPaint);	
	}
	
	
	/** 画分时线 */
	private void setOnDrawMinuteLine(Canvas canvas){
		float scale = 0.0f;
		if(highPrice - lowPrice>0){
			scale = (highPrice - lowPrice) / fHeight;
		}else{
			scale = (lowPrice - highPrice) / fHeight;
		}
		float width = (float) (zWidth / 240.0);
		for (int i = 0; i < size - 1; i++)
		{
			if (i <= 239)
			{
				mPaint.setAntiAlias(true);
				canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));  
				mPaint.setColor(getStockCloseColor());
				mPaint.setStrokeWidth(1);
				StockMinuteLineBean stockMinute = (StockMinuteLineBean)dataProvider.getItem(i);
				StockMinuteLineBean stockMinute1 = (StockMinuteLineBean)dataProvider.getItem(i+1);
				if (i <= 239 && stockMinute!=null && stockMinute1!=null)
				{
					float newprice = stockMinute.getPrice();
					// (最新价-昨收)*中间到上边框的像素/(昨收*变化量) ==像素变化量
					startX = fStartX + (width*i)+1.0f;
					stopX = fStartX + (width*(i+1))+1.0f;
					startY = fStopY - ((newprice - lowPrice)/scale);
					float newprice1 = stockMinute1.getPrice();
					stopY = fStopY - ((newprice1 - lowPrice)/scale);
					canvas.drawLine(startX, startY, stopX, stopY, mPaint);
					mPaint.setColor(Color.parseColor("#3d3e38"));
					canvas.drawLine(startX, startY + 1 , startX, fStopY+minuteBottomPadding-1, mPaint);
					canvas.drawLine(stopX, stopY + 1, stopX, fStopY+minuteBottomPadding-1, mPaint);
				}
			}
		}		
	}
	/**
	 * 画均线
	 * @param canvas
	 */
	private void setOnDrawAverageLine(Canvas canvas){
		float width = (float) (zWidth / 240.0);
		float scale = 0.0f;
		if(highPrice - lowPrice>0){
			scale = (highPrice - lowPrice) / fHeight;
		}else{
			scale = (lowPrice - highPrice) / fHeight;
		}
		if (!isZhiShu)
		{
			mPaint.setColor(getStockAverageLineColor());
			for (int i = 0; i < size-1; i++)
			{
				if (i <= 239)
				{
					mPaint.setAntiAlias(true);
					mPaint.setStrokeWidth(1);
					canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));  
					StockMinuteLineBean stockMinute = (StockMinuteLineBean) dataProvider.getItem(i);
					StockMinuteLineBean stockMinute1 = (StockMinuteLineBean) dataProvider.getItem(i+1);
					if(stockMinute!=null && stockMinute1!=null){
						startX = fStartX + (width*i)+1.0f;
						stopX = fStartX + (width*(i+1))+1.0f;
						float avprice = stockMinute.getAvprice();
						startY = fStopY - ((avprice - lowPrice)/scale);
						float avprice1 = stockMinute1.getAvprice();
						stopY = fStopY - ((avprice1 - lowPrice)/scale);
						canvas.drawLine(startX, startY, stopX, stopY, mPaint);
					}
				}
			}
		}
		
		if(isZhiShu){
			mPaint.setColor(getStockAverageLineColor());
			float middlePrice = (highPrice + lowPrice)/2;
			for (int i = 0; i < size-1; i++)
			{
				if (i <= 239)
				{
					mPaint.setAntiAlias(true);
					mPaint.setStrokeWidth(1);
					canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));  
					StockMinuteLineBean stockMinute = (StockMinuteLineBean) dataProvider.getItem(i);
					StockMinuteLineBean stockMinute1 = (StockMinuteLineBean) dataProvider.getItem(i+1);
					if(stockMinute!=null && stockMinute1!=null){
						startX = fStartX + (width*i)+1.0f;
						stopX = fStartX + (width*(i+1))+1.0f;
						float tempValue = (middlePrice * (stockMinute.getAvgChangeRate()/100000.0f)+middlePrice);
						startY = fStopY-((tempValue - lowPrice)/scale);
						float tempValue1 = (middlePrice * (stockMinute1.getAvgChangeRate()/100000.0f)+middlePrice);
						stopY = fStopY-((tempValue1 - lowPrice)/scale);
						canvas.drawLine(startX, startY, stopX, stopY, mPaint);
					}
				}
			}
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
			mPaint.setTextSize(12);
		}
		else if (cWidth >= 330)
		{
			mPaint.setTextSize(10);
		}
		else
		{
			mPaint.setTextSize(8);
		}
		mPaint.setAntiAlias(true);
		mPaint.setColor(getStockCloseColor());
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));  
		mPaint.setTextAlign(Paint.Align.RIGHT);
		canvas.drawText(Utils.formatNumber(maxSecuvolume, 1),zStartX - 5, zStartY+(textLineHeight/2), mPaint);
		canvas.drawText(Utils.formatNumber(maxSecuvolume, 2), zStartX - 5,zStopY - lineHeight+10, mPaint);
		mPaint.setTextAlign(Paint.Align.LEFT);
		canvas.drawText("单位:手", zStopX + 5, zStopY, mPaint);
	}
	
	/** 画成交量柱状图 */
	private void setOnDrawSecuVolume(Canvas canvas){
		mPaint.setColor(getStockAverageLineColor());
		float scale = maxSecuvolume/zHeight;
		float width = (float) (zWidth / 240.0);
		for (int i = 0; i < size; i++)
		{
			mPaint.setAntiAlias(true);
			canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG)); 
			mPaint.setStrokeWidth(2);
			StockMinuteLineBean stockMinute = (StockMinuteLineBean)dataProvider.getItem(i);
			// 成交量
			if (stockMinute!=null)
			{
				mPaint.setAntiAlias(true);
				canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));  
//				mPaint.setStrokeWidth(3);
				float secuvolume =stockMinute.getSecuvolume() >= 0 ? stockMinute.getSecuvolume() : 0;
				if (secuvolume >= 0)
				{
					// 柱状图顶点
					startX = zStartX+ (width*i)+(float)1.0;
					startY = zStopY - (secuvolume/scale);
					canvas.drawLine(startX, startY, startX, zStopY, mPaint);
				}
			}
		}		
	}
}
