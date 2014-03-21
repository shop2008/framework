package com.wxxr.mobile.stock.client.biz;
import java.util.ArrayList;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class StockChartView extends SurfaceView implements SurfaceHolder.Callback{

	private Context context;
	private AttributeSet attrs;
	private int defStyle;
	private MinuteLine minuteLine;
	public IStockBaseState stockState = null;
	private ArrayList<MinuteLine> mFiveDayLines;
	private SurfaceHolder holder;
	private DrawChartThread thread;
	private boolean isFiveDay = false;
//	SlidingDrawer
	public boolean isFiveDay() {
		return isFiveDay;
	}
	public void setFiveDay(boolean isFiveDay) {
		this.isFiveDay = isFiveDay;
	}
	public StockChartView(Context context) {
		super(context);
		this.context = context;
		init();
	}	
	public StockChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.attrs = attrs;
		init();
	}
	public StockChartView(Context context, AttributeSet attrs,int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		this.attrs = attrs;
		this.defStyle = defStyle;
		init();
	}
	
	/**
	 * 设置分时数据
	 * */
	public void setDataProvider(MinuteLine minuteLine){
		if(minuteLine!=null){
			this.minuteLine = minuteLine;
			stockState = new MinuteAndFiveDayLineBaseState(context,minuteLine);
		}
	}
	
	/**
	 * 设置5日数据
	 * */
	public void setDataProvider(ArrayList<MinuteLine> list){
		if(list!=null){
			this.mFiveDayLines = list;
			stockState = new MinuteAndFiveDayLineBaseState(context,mFiveDayLines,true);
			stockState.setFiveDay(5);			
		}
	}
	
	/**
	 * 刷新界面
	 */
	public void notifyDataSetChanged() {
		if(thread!=null){
			thread.updateCanvas();
		}
	}	
	
	private void init(){
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
//		TypedArray arrs = context.obtainStyledAttributes(attrs,R.styleable.stockChartView);
//		//是否是5日数据
//		isFiveDay = arrs.getBoolean(R.styleable.stockChartView_stockLineType, false);
//		int borderColor = arrs.getColor(R.styleable.stockChartView_stockBorderColor, Color.parseColor("#FF0000")); //边框
//		if(borderColor>0)
//			stockState.setBorderColor(borderColor);
//		int upColor = arrs.getColor(R.styleable.stockChartView_stockUpTextColor, Color.parseColor("#FF0000")); //上涨
//		if(upColor>0)
//			stockState.setStockUpColor(upColor);
//		int downColor = arrs.getColor(R.styleable.stockChartView_stockDownTextColor, Color.parseColor("#3C7F00")); //下跌
//		if(downColor>0)
//			stockState.setStockDownColor(downColor);
//		int averageColor = arrs.getColor(R.styleable.stockChartView_stockAverageLineColor, Color.parseColor("#FFE400")); //均线
//		if(averageColor>0)
//			stockState.setStockAverageLineColor(averageColor);
//		int closeColor = arrs.getColor(R.styleable.stockChartView_stockCloseTextColor, Color.parseColor("#FFFFFF")); //昨收
//		if(closeColor>0)
//			stockState.setStockCloseColor(closeColor);
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if(thread==null){
			thread = new DrawChartThread(holder);
		}
		thread.start();
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if(thread!=null){
			thread.requestExitAndWait();
			thread = null;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(thread!=null)
			thread.doTouch(event);
		return true;
	}
	
	private class DrawChartThread extends Thread{
		private SurfaceHolder mHolder;
		public DrawChartThread(SurfaceHolder holder){
			mHolder = holder;
		}
		@Override
		public void run() {
			updateCanvas();
//			super.run();
		}
		
		public void doTouch(MotionEvent event) {
			stockState.doTouch(event);
			updateCanvas();
		}
		
		public void updateCanvas() {
			Canvas c = null;
			try {
				c = mHolder.lockCanvas(null);
				if (c != null) {
					c.drawColor(Color.BLACK);
					doDraw(c);
				}
			} finally {
				if (c != null)
					mHolder.unlockCanvasAndPost(c);
			}
		}		
		private void doDraw(Canvas canvas){
			if(stockState!=null){
				stockState.doDraw(canvas);
			}
		}
		
		public void requestExitAndWait() {
			try {
				join();
			} catch (InterruptedException ex) {
			}
		}		
	}
}
