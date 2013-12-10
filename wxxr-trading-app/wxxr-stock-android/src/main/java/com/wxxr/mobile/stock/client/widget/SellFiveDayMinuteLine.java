package com.wxxr.mobile.stock.client.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;

import com.wxxr.mobile.core.ui.api.IDataChangedListener;
import com.wxxr.mobile.core.ui.api.IObservableListDataProvider;
import com.wxxr.mobile.stock.app.bean.StockMinuteKBean;
import com.wxxr.mobile.stock.app.bean.StockMinuteLineBean;
import com.wxxr.mobile.stock.client.R;
import com.wxxr.mobile.stock.client.utils.Utils;

public class SellFiveDayMinuteLine extends BasicLineView implements IDataChangedListener   {
	private IObservableListDataProvider dataProvider;
	private Paint mPaint;
	private String stockType = "1";
	private float yesterdayClose; //昨天收价格
	private float highPrice;  //最高价
	private float secondPrice; //
	private float fourthPrice;
	private float lowPrice; //最低价
	private float maxSecuvolume; // 成交量最大值
	private float startX, startY, stopX, stopY;
	private int count;
	private int size;
	private List<StockMinuteLineBean> minuteData;
	private List<FiveDayMinuteLineBean> maxFiveDayData;
	private long buyPrice;
	private boolean flag = true;
	private float left;
	private float top;
	private boolean drawArrows = false;
	
	public SellFiveDayMinuteLine(Context context) {
		super(context);
		drawType(SELL_STATE);
		mPaint = new Paint();
	}
	public SellFiveDayMinuteLine(Context context, AttributeSet attrs) {
		super(context, attrs);
		drawType(SELL_STATE);
		mPaint = new Paint();
	}	
	public SellFiveDayMinuteLine(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		drawType(SELL_STATE);
		mPaint = new Paint();
	}
	
	/**股票类型0-指数；1-个股*/
	public void setStockType(String val){
		this.stockType = val;
	}
	
	public void setBuyPrice(long buy){
		this.buyPrice = buy;
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
	
	/**昨收价*/
	public void setStockClose(String val){
		this.yesterdayClose = Float.parseFloat(val);
	}
	private void initData(){
		if(this.dataProvider==null){
			return;
		}
		if(dataProvider.getItemCounts()>0){
			maxFiveDayData = new ArrayList<FiveDayMinuteLineBean>();
			this.count = dataProvider.getItemCounts();
			for(int i=0;i<count;i++){
				StockMinuteKBean fiveDayStock = (StockMinuteKBean) dataProvider.getItem(i);
				if(fiveDayStock!=null){
					getHighestOrLowest(fiveDayStock);
				}
			}
			getTwoDayMinuteData();
		}
	}
	
	private void getHighestOrLowest(StockMinuteKBean stockMinute){
		float minprice = 0; //最低价
		float maxprice = 0; //最高价
		float highPrice;
		float lowPrice;
		float maxSecuvolume;
		float yesterdayClose = Float.parseFloat(stockMinute.getClose());
		minuteData = stockMinute.getList();
		if(minuteData!=null && minuteData.size()>0){
			this.size = minuteData.size();
			float tempPrice = minuteData.get(0).getPrice();
			minprice = tempPrice;
			maxprice = tempPrice;
			maxSecuvolume = minuteData.get(0).getSecuvolume();
			for(int i=0; i<size; i++){
				StockMinuteLineBean stockMinuteLine = minuteData.get(i);
				if(stockMinuteLine!=null){
					float newprice = stockMinuteLine.getPrice();
					if(newprice - yesterdayClose>=0 && newprice > maxprice){
						maxprice = newprice;
					}
					else if(newprice - yesterdayClose<0 && newprice < minprice){
						minprice = newprice;
					}
					float avprice = stockMinuteLine.getAvprice();
					if(avprice - yesterdayClose >=0 && avprice > maxprice){
						maxprice = avprice;
					}
					if(avprice - yesterdayClose < 0 && avprice < minprice){
						minprice = avprice;
					}
					float tempSecuvolume = stockMinuteLine.getSecuvolume();
					if(tempSecuvolume > maxSecuvolume){
						maxSecuvolume = tempSecuvolume;
					}
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
			FiveDayMinuteLineBean tempBean = new FiveDayMinuteLineBean(highPrice,lowPrice,maxSecuvolume);
			maxFiveDayData.add(tempBean);
//			secondPrice = (highPrice - yesterdayClose)/2+yesterdayClose;
//			fourthPrice = yesterdayClose - (yesterdayClose - lowPrice)/2;	
		}
	}
	
	
	
	private void getTwoDayMinuteData(){
		if(maxFiveDayData!=null){
		    float yHighPrice1=0;
		    float yHighPrice2=0;
		    float yLowPrice1=0;
		    float yLowPrice2=0;
		    float yMaxSecuvolume1=0;
		    float yMaxSecuvolume2=0;	
		    for(int i=0; i<maxFiveDayData.size();i++){
		    	if(i==0){
		    		yHighPrice1 = maxFiveDayData.get(i).getHighPrice();
		    		yLowPrice1 = maxFiveDayData.get(i).getLowPrice();
		    		yMaxSecuvolume1 = maxFiveDayData.get(i).getMaxSecuvolume();
		    	}else if(i==1){
		    		yHighPrice2 = maxFiveDayData.get(i).getHighPrice();
		    		yLowPrice2 = maxFiveDayData.get(i).getLowPrice();
		    		yMaxSecuvolume2 = maxFiveDayData.get(i).getMaxSecuvolume();
		    	}
		    }
		    this.highPrice = yHighPrice1 > yHighPrice2 ? yHighPrice1 : yHighPrice2;
		    this.lowPrice = yLowPrice1 > yLowPrice2 ? yLowPrice2 : yLowPrice1;
		    this.maxSecuvolume = yMaxSecuvolume1 > yMaxSecuvolume2 ? yMaxSecuvolume1 : yMaxSecuvolume2;
		    this.yesterdayClose = highPrice - (highPrice - lowPrice)/2;
		    this.secondPrice = (highPrice - yesterdayClose)/2+yesterdayClose;
			this.fourthPrice = yesterdayClose - (yesterdayClose - lowPrice)/2;	
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		canvas.save();
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
		mPaint.setAntiAlias(true);
		initData();
		drawLeftText(canvas); //画左边文字
		drawRightText(canvas); //画右边文字
		drawSecuVolumeText(canvas); //画成交量文字
		drawChart(canvas);
		canvas.restore();
		drawArrows(canvas,this.left,this.top,mPaint);
	}
	
	/**画左边文字*/
	private void drawLeftText(Canvas canvas){
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
	/**画右边文字*/
	private void drawRightText(Canvas canvas){
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
		String highPicePercentage = String.format("%.2f%%", (highPrice - yesterdayClose)/yesterdayClose*100.00);
		canvas.drawText(highPicePercentage, textMarginRight, fStartY + padding, mPaint);
		String secondPricePercentage = String.format("%.2f%%", (secondPrice - yesterdayClose)/yesterdayClose*100.00);
		canvas.drawText(secondPricePercentage, textMarginRight, fStartY + padding + lineHeight*1, mPaint);

		mPaint.setColor(getStockCloseColor());
		canvas.drawText("0.00%", textMarginRight, fStartY + padding + lineHeight*2, mPaint);
		mPaint.setColor(getStockDownColor());
		
		String fourthPercentage = String.format("%.2f%%", (yesterdayClose - fourthPrice)/yesterdayClose*100.00);
		canvas.drawText(fourthPercentage, textMarginRight, fStartY + padding + lineHeight*3, mPaint);
		String lowPicePercentage = String.format("%.2f%%", (yesterdayClose - lowPrice)/yesterdayClose*100.00);
		canvas.drawText(lowPicePercentage, textMarginRight, fStartY + padding + lineHeight*4, mPaint);
		
	}
	/**画成交量文字*/
	private void drawSecuVolumeText(Canvas canvas){
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
	/**画昨天成交量图柱*/
	private void drawChart(Canvas canvas){
		for (int i = 0; i < count; i++)
		{
			StockMinuteKBean fiveDayStock = null;
			//今天的成交量
			if(i==0){
				fiveDayStock = (StockMinuteKBean) dataProvider.getItem(i);
				if(fiveDayStock!=null){
					drawTodayMinuteLine(fiveDayStock,canvas);
					TodaySecuVolume(fiveDayStock,canvas);	
				}
			}else if(i==1){ //昨天成交量
				fiveDayStock = (StockMinuteKBean) dataProvider.getItem(i);
				if(fiveDayStock!=null){
					drawYesterDayMinuteLine(fiveDayStock, canvas);
					YesterdaySecuVolume(fiveDayStock,canvas);	
				}
			}
		}
	}

	/**画昨天分时线*/
	private void drawYesterDayMinuteLine(StockMinuteKBean stockMinuteK, Canvas canvas){
		float scale = 0.0f;
		if(highPrice - lowPrice>0){
			scale = (highPrice - lowPrice) / fHeight;
		}else{
			scale = (lowPrice - highPrice) / fHeight;
		}
		float width = (float) ((sellWidthScale*2) / 50.0);	
		List<StockMinuteLineBean> temp = stockMinuteK.getList();
		if(temp!=null && temp.size()>0){
			for(int i=0;i<temp.size()-1;i++){
				mPaint.setAntiAlias(true);
				canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));  
				mPaint.setColor(getStockCloseColor());
				mPaint.setStrokeWidth(1);
				if(i<=49){
					StockMinuteLineBean tempStockMinute = temp.get(i);
					StockMinuteLineBean tempStockMinute1 = temp.get(i+1);
					if(tempStockMinute!=null && tempStockMinute1!=null){
						float newprice = tempStockMinute.getPrice();
						int tempNewPrice = (int) (newprice/10);
						int buyPrice = (int) this.buyPrice;
						// (最新价-昨收)*中间到上边框的像素/(昨收*变化量) ==像素变化量
						startX = fStartX + (width*i)+1.0f;
						stopX = fStartX + (width*(i+1))+1.0f;
						startY = fStopY - ((newprice - lowPrice)/scale);
						float newprice1 = tempStockMinute1.getPrice();
						stopY = fStopY - ((newprice1 - lowPrice)/scale);
						canvas.drawLine(startX, startY, stopX, stopY, mPaint);
						mPaint.setColor(Color.parseColor("#3d3e38"));
						canvas.drawLine(startX, startY + 1 , startX, fStopY-1, mPaint);
						canvas.drawLine(stopX, stopY + 1, stopX, fStopY-1, mPaint);
//						canvas.saveLayer(left, top, right, bottom, paint, saveFlags)
						if(buyPrice == tempNewPrice && flag){
							this.flag = false;
							this.drawArrows = true;
							this.left = startX;
							this.top = startY;
						}
					}
				}
			}
		}		
	}
	
	private void drawArrows(Canvas canvas,float left, float top, Paint mPaint){
		if(drawArrows){
			canvas.save();
			Bitmap photo = BitmapFactory.decodeResource(this.getResources(), R.drawable.min_arrows).copy(Bitmap.Config.ARGB_8888, true);
			Bitmap newBitmap = Bitmap.createBitmap(photo);
			canvas.drawBitmap(newBitmap, left, top, mPaint);
			canvas.restore();
		}
	}
	
	/**画今天分时线*/
	private void drawTodayMinuteLine(StockMinuteKBean stockMinuteK, Canvas canvas){
		float scale = 0.0f;
		if(highPrice - lowPrice>0){
			scale = (highPrice - lowPrice) / fHeight;
		}else{
			scale = (lowPrice - highPrice) / fHeight;
		}
		float width = (float) ((sellWidthScale*2) / 50.0);	
		List<StockMinuteLineBean> temp = stockMinuteK.getList();
		if(temp!=null && temp.size()>0){
			for(int i=0;i<temp.size()-1;i++){
				mPaint.setAntiAlias(true);
				canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));  
				mPaint.setColor(getStockCloseColor());
				mPaint.setStrokeWidth(1);
				if(i<=49){
					StockMinuteLineBean tempStockMinute = temp.get(i);
					StockMinuteLineBean tempStockMinute1 = temp.get(i+1);
					if(tempStockMinute!=null && tempStockMinute1!=null){
						float newprice = tempStockMinute.getPrice();
						// (最新价-昨收)*中间到上边框的像素/(昨收*变化量) ==像素变化量
						startX = fStartX + sellWidthScale*2+hSpace + (width*i)+1.0f;
						stopX = fStartX + sellWidthScale*2+hSpace + (width*(i+1))+1.0f;
						startY = fStopY - ((newprice - lowPrice)/scale);
						float newprice1 = tempStockMinute1.getPrice();
						stopY = fStopY - ((newprice1 - lowPrice)/scale);
						canvas.drawLine(startX, startY, stopX, stopY, mPaint);
						mPaint.setColor(Color.parseColor("#3d3e38"));
						canvas.drawLine(startX, startY + 1 , startX, fStopY-1, mPaint);
						canvas.drawLine(stopX, stopY + 1, stopX, fStopY-1, mPaint);
					}
				}
			}
		}
	}	
	
	private void YesterdaySecuVolume(StockMinuteKBean stockMinuteK,Canvas canvas){
		mPaint.setColor(getStockAverageLineColor());
		mPaint.setAntiAlias(true);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG)); 
		mPaint.setStrokeWidth(3);
		float scale = maxSecuvolume/zHeight;
		float width = (float) ((sellWidthScale*2) / 50.0);		
		List<StockMinuteLineBean> temp = stockMinuteK.getList();
		if(temp!=null && temp.size()>0){
			int liNum = temp.size() > 50 ? 50 : temp.size();
			for(int j=0;j<liNum;j++){
				StockMinuteLineBean tempStockMinute = temp.get(j);
				if(tempStockMinute!=null){
					float secuvolume =tempStockMinute.getSecuvolume() >= 0 ? tempStockMinute.getSecuvolume() : 0;
					startX = zStartX+ (width*j)+(float)1.0;
					startY = zStopY - (secuvolume/scale);
					canvas.drawLine(startX, startY, startX, zStopY, mPaint);
				}
			}
		}
	}
	private void TodaySecuVolume(StockMinuteKBean stockMinuteK,Canvas canvas){
		mPaint.setColor(getStockAverageLineColor());
		mPaint.setAntiAlias(true);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG)); 
		mPaint.setStrokeWidth(3);
		float scale = maxSecuvolume/zHeight;
		float width = (float) ((sellWidthScale*2) / 50.0);		
		List<StockMinuteLineBean> temp = stockMinuteK.getList();
		if(temp!=null && temp.size()>0){
			int liNum = temp.size() > 50 ? 50 : temp.size();
			for(int j=0;j<liNum;j++){
				StockMinuteLineBean tempStockMinute = temp.get(j);
				if(tempStockMinute!=null){
					float secuvolume =tempStockMinute.getSecuvolume() >= 0 ? tempStockMinute.getSecuvolume() : 0;
					startX = zStartX+sellWidthScale*2+hSpace+ (width*j)+(float)2.0;
					startY = zStopY - (secuvolume/scale);
					canvas.drawLine(startX, startY, startX, zStopY, mPaint);
				}
			}
		}
	}	
}
