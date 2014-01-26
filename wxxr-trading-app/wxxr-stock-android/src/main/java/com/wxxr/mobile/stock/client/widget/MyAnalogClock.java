package com.wxxr.mobile.stock.client.widget;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.wxxr.mobile.stock.client.R;

public class MyAnalogClock extends View {
	private Time mCalendar;
	private Drawable mHourHand;
	private Drawable mMinuteHand;
	private Drawable mSecondHand;
	private Drawable mDial;

	private int mDialWidth;
	private int mDialHeight;

	private boolean mAttached;

	private float mMinutes;
	private float mHour;

	private boolean mChanged;

	private static String debug = "MyAnalogClock";

	private static int SECONDS_FLAG = 0;
	private Message secondsMsg;
	private float mSeconds;

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 0:
				onTimeChanged();// 重新获取的系统的当前时间，得到时，分，秒
				invalidate();// 强制绘制，调用自身的onDraw();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	public MyAnalogClock(Context context) {
		this(context, null);
	}

	public MyAnalogClock(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public MyAnalogClock(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Resources r = context.getResources();

		if (mDial == null) {
			mDial = r.getDrawable(R.drawable.clock_dial);
		}

		if (mHourHand == null) {
			mHourHand = r.getDrawable(R.drawable.clock_hand_hour);
		}

		if (mMinuteHand == null) {
			mMinuteHand = r.getDrawable(R.drawable.clock_hand_minute);
		}

		if(mSecondHand == null) {
			mSecondHand = r.getDrawable(R.drawable.clock_hand_second);
		}
		mCalendar = new Time();

		mDialWidth = mDial.getIntrinsicWidth();
		mDialHeight = mDial.getIntrinsicHeight();

	}

	@Override
	/*
	 * * 吸附到窗体上
	 */
	protected void onAttachedToWindow() {
		Log.e(debug, "onAttachedToWindow");
		super.onAttachedToWindow();

		if (!mAttached) {
			mAttached = true;
			
		}
		mCalendar = new Time();
		onTimeChanged();
		initSecondsThread();
	}

	private void initSecondsThread() {
		secondsMsg = mHandler.obtainMessage(SECONDS_FLAG);
		Thread newThread = new Thread() {
			@Override
			public void run() {
				while (mAttached) {
					secondsMsg = mHandler.obtainMessage(SECONDS_FLAG);
					mHandler.sendMessage(secondsMsg);
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		};
		newThread.start();

	}

	@Override
	protected void onDetachedFromWindow() {
		Log.e(debug, "onDetachedFromWindow");
		super.onDetachedFromWindow();
		if (mAttached) {
			mAttached = false;
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.e(debug, "onMeasure");
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		float hScale = 1.0f;
		float vScale = 1.0f;

		if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
			hScale = (float) widthSize / (float) mDialWidth;
		}

		if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
			vScale = (float) heightSize / (float) mDialHeight;
		}

		float scale = Math.min(hScale, vScale);

		setMeasuredDimension(resolveSize((int) (mDialWidth * scale),
				widthMeasureSpec), resolveSize((int) (mDialHeight * scale),
				heightMeasureSpec));
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		Log.e(debug, "onSizeChanged");
		super.onSizeChanged(w, h, oldw, oldh);
		mChanged = true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.e(debug, "canvas");
		boolean changed = mChanged;
		if (changed) {
			mChanged = false;
		}

		int availableWidth = getWidth();
		int availableHeight = getHeight();

		int x = availableWidth / 2;
		int y = availableHeight / 2;

		final Drawable dial = mDial;
		int w = dial.getIntrinsicWidth();
		int h = dial.getIntrinsicHeight();

		boolean scaled = false;

		if (availableWidth < w || availableHeight < h) {
			scaled = true;
			float scale = Math.min((float) availableWidth / (float) w,
					(float) availableHeight / (float) h);
			canvas.save();
			canvas.scale(scale, scale, x, y);
		}

		if (changed) {
			dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
		}
		dial.draw(canvas);

		canvas.save();
		canvas.rotate(mHour / 12.0f * 360.0f, x, y);

		final Drawable hourHand = mHourHand;
		if (changed) {
			w = hourHand.getIntrinsicWidth();
			h = hourHand.getIntrinsicHeight();
			hourHand.setBounds(x - (6*w / 31), y - (h / 2), x + (25*w / 31), y
					+ (h / 2));
		}
		hourHand.draw(canvas);
		canvas.restore();

		canvas.save();
		canvas.rotate(mMinutes / 60.0f * 360.0f, x, y);

		final Drawable minuteHand = mMinuteHand;
		if (changed) {
			w = minuteHand.getIntrinsicWidth();
			h = minuteHand.getIntrinsicHeight();
			minuteHand.setBounds(x - (3*w / 17), y - (h / 2), x + (14*w / 17), y
					+ (h / 2));
		}
		minuteHand.draw(canvas);
		canvas.restore();

		canvas.save();
		canvas.rotate(mSeconds / 60.0f * 360.0f, x, y);

		final Drawable secondHand = mSecondHand;// //用时针来代替秒针
		if (changed) {
			w = secondHand.getIntrinsicWidth();
			h = secondHand.getIntrinsicHeight();
			secondHand.setBounds(x - (5*w/ 24), y - (h/ 2), x + (19*w / 24), y
					+ (h / 2));
		}
		secondHand.draw(canvas);
		canvas.restore();

		if (scaled) {
			canvas.restore();
		}
	}

	private void onTimeChanged() {
		Log.e(debug, "onTimeChanged");
		mCalendar.setToNow();// ///获取手机自身的当前时间，而非实际中的标准的北京时间

		int hour = mCalendar.hour-3;// 小时
		int minute = mCalendar.minute - 15;// 分钟
		int second = mCalendar.second - 15;// 秒

		mSeconds = second;
		mMinutes = minute + second / 60.0f;
		mHour = hour + mMinutes / 60.0f;

		mChanged = true;
	}

}
