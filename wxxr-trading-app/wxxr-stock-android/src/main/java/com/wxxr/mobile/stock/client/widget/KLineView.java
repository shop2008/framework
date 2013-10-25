package com.wxxr.mobile.stock.client.widget;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
 * K���Զ���ؼ�
 * @author renwenjie
 */
public class KLineView extends SurfaceView implements Callback {

	private SurfaceHolder holder;
	private RenderThread thread;

	//private Stock stock;

	/**
	 * ��ָ�����������
	 */
	private float touchX ;
	
	/**
	 * ������ʼ������
	 */
	private float whiteStartX;
	
	
	/**
	 * ���ڸ�ʽ������
	 */
	private static DecimalFormat df;
	/**
	 * ���ʹ���
	 */
	private Paint mPaint;

	/**
	 * k������ 0--��ʾ��k, 1--��ʾ��k, 2--��ʾ��k, ��ֵͨ�������ļ�����
	 */
	private int klineType;
	/**
	 * ��ʼ���ݶȱ�(�Զ����)��������/����ߣ�
	 */
	private final float hBi = (float) 152 / 220;

	/**
	 * ���Ͻ��ݶȱ�(�Զ����)��������/����ߣ�
	 */
	private final float tBi = (float) 20 / 220;

	/**
	 * ��״ͼ�ϱ�Ե�ݶȱ�(�Զ����)��������/����ߣ�
	 */
	private final float zztBi = (float) 168 / 220;

	/**
	 * ��״ͼ�µױ��ݶȱ�(�Զ����)��������/����ߣ�
	 */
	private final float zzbBi = (float) 213 / 220;

	/** �����Ŀ� */
	public float cWidth;
	/** �����ĸ� */
	public float cHeight;
	/** ��ʱ�����½�X�� */
	public float mStartX;
	/** ��ʱ�����½�Y�� */
	public float mStartY;
	/** ��ʱ�����Ͻ�X�� */
	public float mEndX;
	/** ��ʱ�����Ͻ�Y�� */
	public float mEndY;
	/** ��״ͼ����Y�� */
	public float zzTopY;
	/** ��״ͼ�ױ�Y�� */
	public float zzBottomY;
	/** ��ʱ�߱�Ŀ� */
	public float fenshiWidth;
	/** ��ʱ�߱�ĸ� */
	public float fenshiHeight;

	private boolean isRender = false;
	/**
	 * ��ʼ���ȱ�(�Զ����)(������/�����)
	 */
	private float wBi;

	/**
	 * kLines.size()
	 */
	private int size;

	/**
	 * Ҫ��������ʾ������
	 */
	private int count = 50;

	/**
	 * ��߳ɽ���
	 */
	private double maxPrice = 0;

	/**
	 * ��ͳɽ���
	 */
	private double minPrice = 20000;

	/**
	 * �ɽ������ֵ
	 */
	private double maxSecuvolume = 0;

	//private List<Kline> klines;
	public float w;
	public List<Kline> klines;
	private Stock stock;

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

	public void setKlines(List<Kline> data) {
		//klines = data;
	}
	
	public void setStock(Stock stock) {
		//this.stock = stock;
	}
	
	

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (thread == null) {
			thread = new RenderThread(holder);
		}
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
		isRender = false;
		if (thread != null) {
			thread.requestExitAndWait();
			thread = null;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		touchX = event.getX();
		return true;
	}
	
	/**
	 * ��Ⱦ�߳�---����ʵʱ��Ⱦ
	 * @author renwenjie
	 *
	 */
	private class RenderThread extends Thread {
		private SurfaceHolder mHolder;

		public RenderThread(SurfaceHolder holder) {
			mHolder = holder;
		}

		@Override
		public void run() {
			while (isRender) {
				updateCanvas();
			}
		}

		public void updateCanvas() {
			Canvas c = null;
			try {
				c = mHolder.lockCanvas(null);
				/**
				 *  ָ������
				 */
				if (c != null) {
					c.drawColor(Color.BLACK);
					/**
					 * ��ͼ
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
			if (klines != null) {
				drawKline(c);
			}

		}



		public void requestExitAndWait() {
			// ������̱߳��Ϊ��ɣ����ϲ����������߳�
			try {
				join();
			} catch (InterruptedException ex) {
			}
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
		if (klines != null && klines.size() > 0) {
			size = klines.size();
			count = size <= 50 ? size : 50;// ȱʡ���ȡ50������ͼ

			double temp;
			for (int i = 0; i < count; i++) {
				temp = Double.parseDouble(klines.get(i).high);
				if (maxPrice < temp)
					maxPrice = temp;
				temp = Double.parseDouble(klines.get(i).low);
				if (minPrice > temp)
					minPrice = temp;
				temp = Double.parseDouble(klines.get(i).secuvolume);
				if (maxSecuvolume < temp)
					maxSecuvolume = temp;
			}
		}
	}
	
	/**
	 * ˢ�½���
	 */
	public void notifyDataSetChanged() {
		if (thread != null)
			thread.updateCanvas();
	}

	public void drawKline(Canvas canvas) {
		// �ڱ�������ϻ�����
		
		
		if (klines == null) {
			return;
		}
		if (klines.size() == 0) {
			return;
		}

		setData2();
		/** ������ָ������￪ʼ************************************************ */

		/** ��ջ�����ʽ */
		mPaint.setPathEffect(null);
		canvas.save();

		/** ���ñ����ߵĹ�Ʊ�۸����� */
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

		/** �������7��ֵ */
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

		/** ���ñ��ױ�ʱ������ */
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
		canvas.drawText(klines.get(0).date, mStartX + 31, zzTopY - 1, mPaint);
		canvas.drawText(klines.get(count / 2).date, mStartX + fenshiWidth / 2,
				zzTopY - 1, mPaint);
		canvas.drawText(klines.get(count - 1).date, mEndX - 31, zzTopY - 1,
				mPaint);

		/** ���ɽ�����״ͼ��ֵ */
		if (cWidth >= 570) {
			mPaint.setTextSize(10);
		} else if (cWidth >= 450) {
			mPaint.setTextSize(8);
		} else if (cWidth >= 330) {
			mPaint.setTextSize(6);
		} else {
			mPaint.setTextSize(4);
		}
		/** �ɽ������� */
		mPaint.setTextAlign(Paint.Align.RIGHT);
		canvas.drawText(formatNum(maxSecuvolume / 100), mStartX - 3,
				zzTopY + 10, mPaint);
		canvas.drawText(formatNum(maxSecuvolume / 200), mStartX - 3, zzTopY
				+ (zzBottomY - zzTopY) / 2 + 8, mPaint);
		/** ��λ���֣� */
		canvas.drawText("��λ:��", mStartX - 3, zzBottomY + 4, mPaint);

		/** ����ͼ���� */
		/** ��ջ�����ʽ */
		mPaint.setPathEffect(null);// ���Ч��
		canvas.save();
		float left = mStartX, top, right, bottom;
		float startX, startY, stopY;
		mPaint.setStyle(Paint.Style.FILL);

		w = fenshiWidth / (count * 3);

		
		/**
		 * ��ÿ����״ͼ�İ���
		 */
		drawWhiteLine(canvas, mStartX, mEndX, mEndY, zzBottomY, count, w);
	
		for (int i = 0; i < count; i++) {

			double open = Double.parseDouble(klines.get(i).open);
			double high = Double.parseDouble(klines.get(i).high);
			double low = Double.parseDouble(klines.get(i).low);
			double newprice = Double.parseDouble(klines.get(i).newprice);
			double secuvolume = Double.parseDouble(klines.get(i).secuvolume);

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
			// ������
			if (open == newprice || a == b) {
				canvas.drawLine(left, top, right, bottom, mPaint);
			} else {
				canvas.drawRect(left, top, right, bottom, mPaint);
			}

			// ����״ͼ
			if (secuvolume != 0) {
				
				top = (float) (zzBottomY - secuvolume * (zzBottomY - zzTopY)
						/ maxSecuvolume + 1);
				float deltaY = 0;
				if (top > zzBottomY) {
					deltaY = top - zzBottomY;
					top = top - 2* deltaY;
				}
				
				/**
				 * ��״ͼ�߶�
				 */
				float absDisY = zzBottomY - top;
				/**
				 * �ײ��߿�߶�
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
			// ������
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

	/*public void setKlineData(ArrayList<Kline> data) {

	}*/

	public void drawBgProgram(Canvas canvas) {
		/** ����������ʽ */
		mPaint.setColor(Color.RED);
		mPaint.setStyle(Paint.Style.STROKE);
		// mPaint.setColor(Color.DKGRAY);
		// Path path = new Path();
		// path.moveTo(0, 10);
		// path.lineTo(480, 10);
		PathEffect effects = new DashPathEffect(new float[] { 2, 2, 2, 2 }, 1);
		mPaint.setPathEffect(effects);
		// canvas.drawPath(path, mPaint);

		/** ������ */

		// if (!Tools.isPortrait(context))
		// {
		// ��ʱ��
		for (int i = 0; i < 7; i++) {
			canvas.drawLine(mStartX, mStartY - i * (fenshiHeight / 6), mEndX,
					mStartY - i * (fenshiHeight / 6), mPaint);
		}
		// ��״ͼ��һ��������
		canvas.drawLine(mStartX, zzTopY + (zzBottomY - zzTopY) / 2, mEndX,
				zzTopY + (zzBottomY - zzTopY) / 2, mPaint);

		/** ������ */
		for (int j = 1; j < 4; j++) {
			float x = mStartX + j * (fenshiWidth / 4);
			// ��ʱ��
			canvas.drawLine(x, mStartY + 6, x, mEndY - 6, mPaint);
			// ��״ͼ��
			canvas.drawLine(x, zzTopY, x, zzBottomY, mPaint);
		}
		// }

		/** ��ջ�����ʽ */
		mPaint.setPathEffect(null);

		/** ��Χ�߿� */
		// mPaint.setColor(Color.DKGRAY);
		mPaint.setColor(Color.RED);
		canvas.drawLine(mStartX, mEndY - 6, mEndX, mEndY - 6, mPaint);// ��ʱ�Ϸ�����
		canvas.drawLine(mStartX, mStartY + 6, mEndX, mStartY + 6, mPaint);// ��ʱ�·�����
		canvas.drawLine(mStartX, mStartY + 6, mStartX, mEndY - 6, mPaint);// ��ʱ������
		canvas.drawLine(mEndX, mStartY + 6, mEndX, mEndY - 6, mPaint);// ��ʱ������
		canvas.drawLine(mStartX, zzTopY, mEndX, zzTopY, mPaint);// ��״ͼ�Ϸ�����
		canvas.drawLine(mStartX, zzBottomY, mEndX, zzBottomY, mPaint);// ��״ͼ�ױߺ���
		canvas.drawLine(mStartX, zzBottomY, mStartX, zzTopY, mPaint);// ��״ͼ������
		canvas.drawLine(mEndX, zzBottomY, mEndX, zzTopY, mPaint);// ��״ͼ������
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
		// �ɽ���������С��1��ֱ����ʾ����9453��
		// ����1�򣬼��򣬱���1λС������1140.5��
		// ����1�ڣ����ڣ�����1λС������1.5�ڡ�
		if (value >= 100000000000d) {// ���ڵ���1ǧ��
			return round(value / 100000000, 1) + "��";// 6,666��
		} else if (value >= 100000000) {// ���ڵ���1��
			return round((value / 100000000), 1) + "��";// 666.6��
		} else if (value > 10000000 && value < 100000000) {// ����1ǧ��С��1��
			return round(value / 10000, 1) + "��";// 6666��
		} else if (value >= 10000) {// ���ڵ���һ��
			return round((value / 10000), 1) + "��";// 666.6��
		} else {// С��һ��
			return String.valueOf(round(value, 1));// 6,666
		}
	}
	
	
	/**
	 * FIXME �ṩС��λ�������봦��������ǿ�ѧ���㣬�κ���ҵ���㶼Ӧ��ʹ��BigDecimal������double����Ϊdouble���Ͳ���ȷ��
	 * 
	 * @param v
	 *            ��Ҫ�������������
	 * @param scale
	 *            С�������ࡱ������λ
	 * @return ���������Ľ��
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
	 * FIXME ��#���ǿ���Ϊ�գ���0���ǲ�����0ռλ���������Ƿָ���;���硰#��##0.0#��
	 * 
	 * @param v
	 *            ��Ҫ��ʽ��������
	 * @param scale
	 *            С��������١�������λ
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
	 * FIXME С��ת��Ϊ�ٷ���,����2λС��(����������)
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

}
