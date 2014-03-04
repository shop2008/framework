package com.wxxr.mobile.stock.client.widget;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.IDataChangedListener;
import com.wxxr.mobile.core.ui.api.IObservableListDataProvider;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.bean.StockLineBean;
import com.wxxr.mobile.stock.client.R;
import com.wxxr.mobile.stock.client.utils.Utils;

public class KlineViewNew extends View implements IDataChangedListener {
	private static Trace log = Trace.getLogger(KlineViewNew.class);
	/** 画布的宽 */
	public float cWidth;
	/** 画布的高 */
	public float cHeight;
	private float mStartX;
	private float mStartY;
	private float mStopX;
	private float mStopY;
	/**
	 * 要求蜡烛显示的数量
	 */
	private int count = 50; //缺省最多50跟柱子
	private int size;
	private float wScale; //每根柱子的宽度
	/** K线表的宽 */
	public float fenshiWidth;
	/** K线表的高 */
	public float fenshiHeight; //K线的高度
	public float marginTop = 8;//上外边距
	public float marginBottom = 8;//下外边距
	public float marginLeft;//左外边距
	public float marginRight = 30;//右外边距
	public float paddingTop = 8; //上内边距
	public float paddingBottom = 8; //下内边距
	public float vSpace = 8.0f;//K线与成交量之间的间隙
	public float lineHeight; //每行的高度
	public float fiveDayLine = 5; //5天数据
	public float dayWidth;//每天数据表的宽度
	private float maxPrice = 0; // 最高成交价
	private float minPrice = 20000; //最低成交价
	private float maxSecuvolume = 0; //成交量最大值
	private final static int K_LINE_LABEL_Y_NUM = 4;
	private Paint mPaint;
	private DecimalFormat df;
	private IObservableListDataProvider dataProvider;
	private float textSize;
	private Context context;
	private float padding;
	public KlineViewNew(Context context) {
		super(context);
		this.context = context;
	}
	public KlineViewNew(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	public KlineViewNew(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
	}
	public IObservableListDataProvider getDataProvider() {
		return dataProvider;
	}
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
		postInvalidate();
	}

	@Override
	public void dataSetChanged() {
		postInvalidate();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		initCanvasData();
		initData();
		drawTable(canvas);
		drawLeftText(canvas);
		drawBottomDateText(canvas);
		drawKLine(canvas);
		drawSecuVolumeText(canvas);
		drawSecuVolume(canvas);
	}
	//初始化画布数据
	private void initCanvasData(){
		mPaint = new Paint();
		df = new DecimalFormat("0.00");
		this.cWidth = this.getWidth();
		this.cHeight = this.getHeight();
		this.marginLeft = Utils.adjustPadding(this.context);
//		this.marginRight = Utils.adjustPadding(cWidth, cHeight) - 30;
		this.fenshiWidth = this.cWidth - this.marginLeft - this.marginRight;
		this.vSpace = Utils.adjustVSpace(this.context);
		this.fenshiHeight = this.cHeight - this.marginTop - this.paddingTop - this.marginBottom - this.vSpace - this.marginBottom;
		this.lineHeight = this.fenshiHeight / 6;
		this.wScale = this.fenshiWidth / this.count;
		this.dayWidth = this.fenshiWidth / this.fiveDayLine;
		this.mStartX = this.marginLeft;
		this.mStopX = this.cWidth - this.marginRight;
		this.mStartY = this.marginTop;
		this.mStopY = this.marginTop;
	}
	//画表格
	private void drawTable(Canvas canvas){
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));	
		mPaint.setColor(Color.parseColor("#535353"));
		mPaint.setStyle(Paint.Style.STROKE);
		//K线横线
		canvas.drawLine(mStartX, mStartY, mStopX, mStopY, mPaint);
		for(int i=0; i<5; i++){
			mStartY = mStopY = this.marginTop + this.paddingTop + i *  lineHeight;
			canvas.drawLine(mStartX, mStartY, mStopX, mStopY, mPaint);
		}
		mStartY = mStopY = this.marginTop + this.paddingTop + lineHeight * 4 + marginBottom;
		canvas.drawLine(mStartX, mStartY, mStopX, mStopY, mPaint);
		//K线竖线
		mStartY = this.marginTop;
		mStopY = this.marginTop + this.paddingTop + lineHeight * 4 + marginBottom;
		for(int i=0; i<6; i++){
			mStartX = mStopX = this.marginLeft + i * dayWidth;
			canvas.drawLine(mStartX, mStartY, mStopX, mStopY, mPaint);
		}
		
		//画成交量
		mStartX = this.marginLeft;
		mStopX = this.cWidth - this.marginRight;
		float tempH = this.marginTop + this.paddingTop + this.lineHeight * 4 + this.marginBottom + this.vSpace;
		for(int i=0; i<3; i++){
			mStartY = mStopY = tempH + lineHeight * i;
			canvas.drawLine(mStartX, mStartY, mStopX, mStopY, mPaint);
		}
		
		mStartY = tempH;
		mStopY = tempH + lineHeight * 2;
		for(int i=0; i<6; i++){
			mStartX = mStopX = this.marginLeft + i * dayWidth;
			canvas.drawLine(mStartX, mStartY, mStopX, mStopY, mPaint);
		}
	}
	//初始化数据
	private void initData(){
		if(dataProvider == null)
			return;
		this.size = dataProvider.getItemCounts();
		if(size==0) return;
		maxPrice = Float.parseFloat(String.valueOf(((StockLineBean) dataProvider.getItem(0)).getHigh()));
		minPrice = Float.parseFloat(String.valueOf(((StockLineBean) dataProvider.getItem(0)).getLow()));
		maxSecuvolume = Float.parseFloat(String.valueOf(((StockLineBean) dataProvider.getItem(0)).getSecuvolume()));
		float temp;
		for(int i=0; i<size; i++){
			temp = Float.parseFloat(String.valueOf(((StockLineBean) dataProvider.getItem(i)).getHigh()));
			if (maxPrice < temp) maxPrice = temp;
			temp = Float.parseFloat(String.valueOf(((StockLineBean) dataProvider.getItem(i)).getLow()));
			if (minPrice > temp) minPrice = temp;
			temp = Float.parseFloat(String.valueOf(((StockLineBean) dataProvider.getItem(i)).getSecuvolume()));
			if (maxSecuvolume < temp) maxSecuvolume = temp;			
		}
	}
	//画左边文字
	private void drawLeftText(Canvas canvas){
		mPaint.reset();
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setAntiAlias(true);
		/** 设置表格左边的股票价格文字 */
		mPaint.setTextSize(Utils.adjustFontSize(this.context));
		mPaint.setTextAlign(Paint.Align.RIGHT);
		float price1 = maxPrice;
		float price2 = (maxPrice - minPrice) * (K_LINE_LABEL_Y_NUM - 1) / K_LINE_LABEL_Y_NUM + minPrice;
		float price3 = (maxPrice - minPrice) * (K_LINE_LABEL_Y_NUM - 2) / K_LINE_LABEL_Y_NUM + minPrice;
		float price4 = (maxPrice - minPrice) * (K_LINE_LABEL_Y_NUM - 3) / K_LINE_LABEL_Y_NUM + minPrice;
		float price5 = minPrice;
		mPaint.setColor(getRedColor());
		float tempPadding = 6;
		float tempY = marginTop + paddingTop + tempPadding;
		canvas.drawText(formatDouble(price1 / 1000), marginLeft - 5, tempY, mPaint);
		canvas.drawText(formatDouble(price2 / 1000), marginLeft - 5, tempY + lineHeight * 1, mPaint);
		mPaint.setColor(getWhiteColor());
		canvas.drawText(formatDouble(price3 / 1000), marginLeft - 5, tempY + lineHeight * 2, mPaint);
		mPaint.setColor(getGreenColor());
		canvas.drawText(formatDouble(price4 / 1000), marginLeft - 5, tempY + lineHeight * 3, mPaint);
		canvas.drawText(formatDouble(price5 / 1000), marginLeft - 5, tempY + lineHeight * 4,mPaint);
	}
	
	private void drawBottomDateText(Canvas canvas){
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setAntiAlias(true);
		/** 设置表格左边的股票价格文字 */
		mPaint.setTextSize(Utils.adjustFontSize(this.context));
		mPaint.setTextAlign(Paint.Align.RIGHT);
		mPaint.setColor(getWhiteColor());
		ArrayList<String> dateArr = new ArrayList<String>();
		String dateVal = null;
		String oneDate = null,twoDate = null,threeDate = null,fourDate = null,temp5 = null;
		if(size>10 && size<=20){
			StockLineBean date = (StockLineBean) dataProvider.getItem(size-10);
			oneDate = date.getDate();
			StockLineBean dateTemp = (StockLineBean) dataProvider.getItem(0);
			twoDate = dateTemp.getDate();
		}else if(size>20 && size<=30){
			StockLineBean date = (StockLineBean) dataProvider.getItem(size -10);
			oneDate = date.getDate();
			StockLineBean dateTemp = (StockLineBean) dataProvider.getItem(size- 10*2);
			twoDate = dateTemp.getDate();
		}else if(size>30 && size<=40){
			StockLineBean date = (StockLineBean) dataProvider.getItem(size-10);
			oneDate = date.getDate();
			StockLineBean dateTemp = (StockLineBean) dataProvider.getItem(size- 10*2);
			twoDate = dateTemp.getDate();	
			StockLineBean dateTemp1 = (StockLineBean) dataProvider.getItem(size - 10*3);
			threeDate = dateTemp1.getDate();
		}else if(size>40 && size<=50){
			StockLineBean date = (StockLineBean) dataProvider.getItem(size -10);
			oneDate = date.getDate();
			StockLineBean dateTemp = (StockLineBean) dataProvider.getItem(size - 10*2);
			twoDate = dateTemp.getDate();	
			StockLineBean dateTemp1 = (StockLineBean) dataProvider.getItem(size - 10*3);
			threeDate = dateTemp1.getDate();
			StockLineBean dateTemp2 = (StockLineBean) dataProvider.getItem(size - 10*4);
			fourDate = dateTemp2.getDate();
		}else if(size>0 && size<=10){
			StockLineBean date = (StockLineBean) dataProvider.getItem(size-1);
			oneDate = date.getDate();
		}
		mPaint.setColor(getWhiteColor());
		mPaint.setTextSize(Utils.adjustFontSize(this.context));
		log.info(oneDate+"::"+twoDate+"::"+threeDate+"::"+fourDate);
		float tempY = this.cHeight - this.marginBottom - lineHeight*2-this.vSpace/3;
		if(oneDate!=null && !StringUtils.isBlank(oneDate)){
			int textWidth = getRectDrawTextWidth(mPaint,oneDate);
			canvas.drawText(oneDate, this.marginLeft+this.dayWidth+textWidth/2, tempY, mPaint);
		}
		if(twoDate!=null && !StringUtils.isBlank(twoDate)){
			int textWidth = getRectDrawTextWidth(mPaint,twoDate);
			canvas.drawText(twoDate, this.marginLeft+this.dayWidth*2+textWidth/2, tempY, mPaint);
		}
		if(threeDate!=null && !StringUtils.isBlank(threeDate)){
			int textWidth = getRectDrawTextWidth(mPaint,threeDate);
			canvas.drawText(threeDate, this.marginLeft+this.dayWidth*3 + textWidth/2, tempY, mPaint);
		}
		if(fourDate!=null && !StringUtils.isBlank(fourDate)){
			int textWidth = getRectDrawTextWidth(mPaint,fourDate);
			canvas.drawText(fourDate, this.marginLeft+this.dayWidth*4 + textWidth/2, tempY, mPaint);
		}
	}
	
	private int getRectDrawTextWidth(Paint paint, String str){
		if(str==null || str.length()==0) return 0;
		Rect rect = new Rect(); 
		paint.getTextBounds(str, 0, str.length(), rect);
		return rect.width();
	}
	//画K线图
	private void drawKLine(Canvas canvas){
		float left,top,right,bottom;
		float kHeight =  lineHeight * 4 ;
		float startX, startY, stopY;
		for (int i = size - 1, j = 0; i >= 0; i--, j++) {
			float open = Float.parseFloat(String.valueOf(((StockLineBean) dataProvider.getItem(i)).getOpen()));
			float newprice = Float.parseFloat(String.valueOf(((StockLineBean) dataProvider.getItem(i)).getPrice()));
			float high = Float.parseFloat(String.valueOf(((StockLineBean) dataProvider.getItem(i)).getHigh()));
			float low = Float.parseFloat(String.valueOf(((StockLineBean) dataProvider.getItem(i)).getLow()));
			left = marginLeft + this.wScale * j;
			right = marginLeft + this.wScale * (j+1) - 1;
			if(open>=newprice){
				mPaint.setColor(getGreenColor());
				top = (float) (this.marginTop + this.paddingTop + kHeight - (open - minPrice) / (maxPrice - minPrice) * kHeight );
				bottom = (float) (this.marginTop + this.paddingTop + kHeight - (newprice - minPrice) / (maxPrice - minPrice) * kHeight);
			}else{
				mPaint.setColor(getRedColor());
				top = (float) (this.marginTop + this.paddingTop + kHeight - (newprice - minPrice) / (maxPrice - minPrice) * kHeight );
				bottom = (float) (this.marginTop + this.paddingTop + kHeight - (open - minPrice) / (maxPrice - minPrice)  * kHeight );
			}
			int tempTop = (int) top;
			int tempBottom = (int) bottom;
			if (open == newprice && high == low) {
				mPaint.setStrokeWidth(2);
				mPaint.setColor(getWhiteColor());
				canvas.drawLine(left, top, right, bottom, mPaint);
			}else if (open == newprice || tempTop == tempBottom) {
				mPaint.setStrokeWidth(2);
				if(open>=newprice){
					mPaint.setColor(getGreenColor());
				}else{
					mPaint.setColor(getRedColor());
				}
				canvas.drawLine(left, tempTop, right, tempBottom, mPaint);
			}else {
				if(Math.abs(bottom - top) < 1)
					top = bottom -1;
				canvas.drawRect(left, top, right, bottom, mPaint);
			}
			startY = (float) (this.marginTop + this.paddingTop + kHeight - (high - minPrice) / (maxPrice - minPrice) * kHeight );
			stopY = (float) (this.marginTop + this.paddingTop + kHeight - (low - minPrice) / (maxPrice - minPrice) * kHeight);
			startX = left + wScale / 2;
			canvas.drawLine(startX, startY, startX, stopY, mPaint);
		}
	}
	
	//画成交量值
	private void drawSecuVolumeText(Canvas canvas){
		mPaint.setTextAlign(Paint.Align.RIGHT);
		mPaint.setColor(getWhiteColor());
		canvas.drawText(formatNum(maxSecuvolume / 100), marginLeft - 5, this.cHeight - this.marginBottom - lineHeight * 2 + 24, mPaint);
		canvas.drawText(formatNum(maxSecuvolume / 200), marginLeft - 5, this.cHeight - this.marginBottom - lineHeight * 1 + 15, mPaint);
		/** 单位（手） */
		canvas.drawText("单位:手", marginLeft - 5, this.cHeight - this.marginBottom, mPaint);		
	}
	
	//画成交量图
	private void drawSecuVolume(Canvas canvas){
		float left,top,right,bottom;
		float tempY = this.cHeight - this.marginBottom;
		for (int i = size - 1, j = 0; i >= 0; i--, j++) {
			float open = Float.parseFloat(String.valueOf(((StockLineBean) dataProvider.getItem(i)).getOpen()));
			float newprice = Float.parseFloat(String.valueOf(((StockLineBean) dataProvider.getItem(i)).getPrice()));
			float secuvolume = Float.parseFloat(String.valueOf(((StockLineBean) dataProvider.getItem(i)).getSecuvolume()));	
			if(open>=newprice){
				mPaint.setColor(getGreenColor());
			}else{
				mPaint.setColor(getRedColor());
			}
			float scale = maxSecuvolume/(lineHeight * 2);
			if(secuvolume!=0){
				left = marginLeft + this.wScale * j;
				right = marginLeft + this.wScale * (j+1)-1;
				top = tempY - secuvolume/scale;
				bottom = tempY;
				canvas.drawRect(left, top, right, bottom, mPaint);
			}
		}
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
	
	private int getRedColor(){
		return getResources().getColor(R.color.red);
	}
	
	private int getGreenColor(){
		return getResources().getColor(R.color.stock_down);
	}
	
	private int getWhiteColor(){
		return getResources().getColor(R.color.white);
	}
}
