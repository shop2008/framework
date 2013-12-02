package com.wxxr.mobile.stock.client.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.View;

public class BasicLineView extends View {

	private static final int STOCK_RED = Color.parseColor("#BA2514"); //红色
	private static final int STOCK_GREEN = Color.parseColor("#3C7F00"); // 绿色
	private static final int STOCK_YELLOW = Color.parseColor("#FFE400");//黄色
	private static final int STOCK_WHITE = Color.parseColor("#FFFFFF"); // 白色
	private static final int STOCK_BLACK = Color.parseColor("#000000"); //黑色
	private static final int STOCK_GRAY = Color.parseColor("#F0F0F0"); //灰色
	private static final int STOCK_BLUE = Color.parseColor("#0966be"); //蓝色	
	private static final int BORDER_COLOR = Color.parseColor("#BA2514");
	private static final int SHADOW_COLOR = Color.parseColor("#3d3e38");
	/** 边框颜色*/
//	private int borderColor;
	private int stockUpColor; // 上涨
	private int stockDownColor;// 下跌
	private int stockAverageLineColor; //均线
	private int stockCloseColor; //昨收
	private int stockBorderColor; //边框
	private int shadowColor;
	private Paint mPaint;
	public float cWidth;
	public float cHeight;
	private float mStartX;
	private float mStartY;
	private float mStopX;
	private float mStopY;
	
	public float lineHeight; //每条线直接的距离
	private float marginTop = 8f;
	private float marginBottom = 8f;
	public float minuteTopPadding = 8f;
	public float minuteBottomPadding = 8f;
	public float textLineHeight = 20f;
	private float marginLeft = 60f;
	private float marginRight = 60f;
	private float mLineNumber = 240f; //240条股票信息
	public float fHeight;//分钟线框的高度
	public float fWidth; //分钟线框的宽度
	public float zWidth; 
	public float zHeight;
	
	public float zStartX;
	public float zStartY; 
	public float zStopX;
	public float zStopY;
	
	public float fStartY;
	public float fStopY;
	public float fStartX;
	public float fStopX;
	public float wScale; //宽度比
	public float tableW;
	public static final String BUY_STATE = "BUY";
	public static final String SELL_STATE = "SELL";
	public float hSpace = 8.0f;
	public float sellWidthScale;
	public float sellRightStartX;
	private String stockState = BasicLineView.BUY_STATE;
//	public float fHeightScale; //分时线纵比；
//	public float zHeightScale; //柱状图纵度比；
	
	public BasicLineView(Context context) {
		super(context);
		init();
	}
	public BasicLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public BasicLineView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	public int getStockUpColor() {
		return stockUpColor;
	}
	public void setStockUpColor(int stockUpColor) {
		if(stockUpColor!=0){
			this.stockUpColor = stockUpColor;
		}else{
			this.stockUpColor = BasicLineView.STOCK_RED;
		}
	}
	public int getStockDownColor() {
		return stockDownColor;
	}
	public void setStockDownColor(int stockDownColor) {
		if(stockDownColor!=0){
			this.stockDownColor = stockDownColor;
		}else{
			this.stockDownColor = BasicLineView.STOCK_GREEN;
		}
	}
	public int getStockAverageLineColor() {
		return stockAverageLineColor;
	}
	public void setStockAverageLineColor(int stockAverageLineColor) {
		if(stockAverageLineColor!=0){
			this.stockAverageLineColor = stockAverageLineColor;
		}else{
			this.stockAverageLineColor = BasicLineView.STOCK_YELLOW;
		}
	}
	public int getStockCloseColor() {
		return stockCloseColor;
	}
	public void setStockCloseColor(int stockCloseColor) {
		if(stockCloseColor!=0){
			this.stockCloseColor = stockCloseColor;
		}else{
			this.stockCloseColor = BasicLineView.STOCK_WHITE;
		}
	}
	public void setBorderColor(int color) {
		if(color!=0){
			this.stockBorderColor = color;
		}else{
			this.stockBorderColor = BasicLineView.BORDER_COLOR;
		}
	}
	public int getBorderColor(int color) {
		return this.stockBorderColor;
	}
	
	public int getShadowColor() {
		return shadowColor;
	}
	public void setShadowColor(int shadowColor) {
		if(shadowColor!=0){
			this.shadowColor = shadowColor;
		}else{
			this.shadowColor = BasicLineView.SHADOW_COLOR;
		}
	}
	private void init(){
		mPaint = new Paint();
	}
	
	/**计算 画布大小、X、Y坐标*/
	private void countData(Canvas canvas){
		this.cWidth = canvas.getWidth();
		this.cHeight = canvas.getHeight();
		this.fWidth = cWidth - (marginLeft + marginRight);
		this.zWidth = this.fWidth;
		this.lineHeight  = (cHeight - (marginTop + marginBottom + minuteTopPadding + minuteBottomPadding + textLineHeight)) / 6;
		this.fHeight = lineHeight * 4;
		this.zHeight = lineHeight * 2;
		
		this.mStartX = marginLeft;
		this.mStopX = cWidth - marginRight;
		
		this.zStartX = this.fStartX = marginLeft;
		this.zStopX = this.fStopX = cWidth - marginRight;
		
		this.zStartY = cHeight - marginBottom - lineHeight*2;
		this.zStopY = cHeight - marginBottom;
		
		this.fStartY = marginTop + minuteTopPadding;
		this.fStopY = marginTop + minuteTopPadding + lineHeight*4;
		
		this.wScale = mLineNumber / fWidth;
		this.tableW = fWidth / 4;
		this.sellWidthScale = (fWidth - hSpace) / 4;
		this.sellRightStartX = marginLeft + (sellWidthScale * 2) + hSpace;
	}
	
	protected void drawType(String state){
		if(state!=null){
			this.stockState = state;
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		countData(canvas);
		if(stockState!=null && BUY_STATE.equals(stockState)){
			onBuyMinuteDraw(canvas);
		}
		if(stockState!=null && SELL_STATE.equals(stockState)){
			onSellMinuteDraw(canvas);
		}
	}
	
	private void onBuyMinuteDraw(Canvas canvas){
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));	
		mPaint.setColor(stockBorderColor);
		mPaint.setStyle(Paint.Style.STROKE);
		//分时线最上边线
		canvas.drawLine(mStartX, marginTop, mStopX, marginTop, mPaint);
		//分时横线---begin
		for(int i=0; i<5;i++){
			mStartY = mStopY = (marginTop + minuteTopPadding) + i* lineHeight;
			canvas.drawLine(mStartX, mStartY, mStopX, mStopY , mPaint);
		}
		//分时最下边线条
		mStartY = mStopY = (marginTop + minuteTopPadding)+lineHeight*4+minuteBottomPadding;
		canvas.drawLine(mStartX, mStartY, mStopX, mStopY, mPaint);
		
		/** 分时纵线线 */
		float tempWScae = fWidth/4;
		for (int j = 0; j < 5; j++)
		{
			mStartX = mStopX = marginLeft + tempWScae * j ;
			mStartY= marginTop;
			mStopY = marginTop + minuteTopPadding + lineHeight*4 + minuteBottomPadding;
			canvas.drawLine(mStartX, mStartY, mStopX, mStopY, mPaint);
		}
		
		//成交量横线
		float tempH = (marginTop + minuteTopPadding)+lineHeight*4+minuteBottomPadding;
		for(int i=0; i<3;i++){
			this.mStartX = marginLeft;
			mStartY = mStopY = tempH + textLineHeight + lineHeight*i;
			canvas.drawLine(mStartX,mStartY , mStopX, mStopY, mPaint);
		}
		
		//成交量纵线
		for (int j = 0; j < 5; j++)
		{
			mStartX = mStopX = marginLeft + tempWScae * j ;
			mStartY= tempH+textLineHeight;
			mStopY = tempH+textLineHeight+lineHeight*2;
			canvas.drawLine(mStartX, mStartY, mStopX, mStopY, mPaint);
		}
		canvas.save();			
	}
	
	private void onSellMinuteDraw(Canvas canvas){
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));	
		mPaint.setColor(stockBorderColor);
		mPaint.setStyle(Paint.Style.STROKE);
		//分时上下两条横线
		mStartY = mStopY = (marginTop + minuteTopPadding);
		canvas.drawLine(mStartX, mStartY, mStopX, mStopY , mPaint);
		mStartY = mStopY = (marginTop + minuteTopPadding) + 4* lineHeight;
		//量分时图间距
		
		//分时纵线
//		float tempWScae =  (fWidth - hSpace) / 4;
		//左边分时竖线
		for(int i = 0; i<3; i++){
			mStartX = mStopX = marginLeft + sellWidthScale * i;
			mStartY= marginTop + minuteTopPadding;
			mStopY = (marginTop + minuteTopPadding) + 4* lineHeight;
			canvas.drawLine(mStartX, mStartY, mStopX, mStopY , mPaint);
		}
		//右边分时竖线
//		float tempX = marginLeft + (sellWidthScale * 2) + hSpace;
		for(int j = 0; j<3; j++){
			mStartX = mStopX = sellRightStartX + sellWidthScale * j;
			mStopY = (marginTop + minuteTopPadding) + 4* lineHeight;
			canvas.drawLine(mStartX, mStartY, mStopX, mStopY , mPaint);
		}
		//成交量
		//上边线
		float tempH = (marginTop + minuteTopPadding)+lineHeight*4+minuteBottomPadding;
		mStartY= tempH+textLineHeight;
		mStartX = marginLeft;
		mStopX = cWidth - marginRight;
		canvas.drawLine(mStartX, mStartY, mStopX, mStopY , mPaint);
		//左边线
		mStartY= tempH+textLineHeight;
		mStartX = mStopX = marginLeft;
		mStopY = tempH+textLineHeight+lineHeight*2;
		canvas.drawLine(mStartX, mStartY, mStopX, mStopY , mPaint);
		//下边线
		mStartX = marginLeft;
		mStopX = cWidth - marginRight;
		mStartY = mStopY = (tempH+textLineHeight)+lineHeight*2;
		canvas.drawLine(mStartX, mStartY, mStopX, mStopY , mPaint);
		//右边框线
		mStartY= tempH+textLineHeight;
		mStopY = tempH+textLineHeight+lineHeight*2;
		mStartX = mStopX = cWidth - marginRight;
		canvas.drawLine(mStartX, mStartY, mStopX, mStopY , mPaint);
		
		canvas.save();	
	}
}
