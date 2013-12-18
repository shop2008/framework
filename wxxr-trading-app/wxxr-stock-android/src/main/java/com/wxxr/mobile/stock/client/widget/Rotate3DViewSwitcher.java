package com.wxxr.mobile.stock.client.widget;

import java.util.Date;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.util.DateUtil;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.client.R;

public class Rotate3DViewSwitcher extends ViewSwitcher {

	private static Trace log = Trace.getLogger(Rotate3DViewSwitcher.class);
	private float mHeight;
	private Context mContext;
	// mInUp,mOutUp分别构成向下翻页的进出动画
	private Rotate3dAnimation mInUp;
	private Rotate3dAnimation mOutUp;

	// mInDown,mOutDown分别构成向下翻页的进出动画
	private Rotate3dAnimation mInDown;
	private Rotate3dAnimation mOutDown;

//	private TextView mUpdateTime;
//	private Rotate3DViewSwitcher mSwitcher;
	private boolean mShowing;
	public Rotate3DViewSwitcher(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
		mContext = context;
		init();
	}

	public Rotate3DViewSwitcher(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		mInUp = createAnim(-90, 0, true, true);
		mOutUp = createAnim(0, 90, false, true);
		mInDown = createAnim(90, 0, true, false);
		mOutDown = createAnim(0, -90, false, false);
		// Rotate3DViewSwitcher主要用于View切换，比如从A切换到B，
		// setInAnimation()后，A将执行inAnimation，
		// setOutAnimation()后，B将执行OutAnimation
		setInAnimation(mInUp);
		setOutAnimation(mOutUp);
	}

	public void setTitle(String title) {
		TextView titleTv = (TextView) findViewById(R.id.tv_title_content);
		titleTv.setText(title);
	}
	
	public void setText(String message) {
		showNotification(message);
	}
	
	public void refreshComplete() {
		showNotification("最后更新：" + DateUtil.formatDate(new Date()));
	}

	private void showNotification(String message) {
		if(log.isDebugEnabled()) {
			log.debug("show notification :["+message+"]");
		}
		TextView msgTv = (TextView) findViewById(R.id.time);
		if(StringUtils.isBlank(message)) {
			msgTv.setText("");
			return;
		}
		if(mShowing)
			return;
		mShowing = true;
		msgTv.setText(message);
		setRorateUp();
		showNext();
		handler.sendEmptyMessageDelayed(0, 1500);
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			setRorateUp();
			setDisplayedChild(0);
			mShowing = false;
		}
	};
	
	private Rotate3dAnimation createAnim(float start, float end,
			boolean turnIn, boolean turnUp) {
		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end,
				turnIn, turnUp);
		rotation.setDuration(450);
		rotation.setFillAfter(false);
		rotation.setInterpolator(new LinearInterpolator());
		return rotation;
	}

	// 定义动作，向下滚动翻页
	public void setRorateDown() {
		if (getInAnimation() != mInDown) {
			setInAnimation(mInDown);
		}
		if (getOutAnimation() != mOutDown) {
			setOutAnimation(mOutDown);
		}
	}

	// 定义动作，向上滚动翻页
	public void setRorateUp() {
		if (getInAnimation() != mInUp) {
			setInAnimation(mInUp);
		}
		if (getOutAnimation() != mOutUp) {
			setOutAnimation(mOutUp);
		}
	}

	class Rotate3dAnimation extends Animation {
		private final float mFromDegrees;
		private final float mToDegrees;
		private float mCenterX;
		private float mCenterY;
		private final boolean mTurnIn;
		private final boolean mTurnUp;
		private Camera mCamera;

		public Rotate3dAnimation(float fromDegrees, float toDegrees,
				boolean turnIn, boolean turnUp) {
			mFromDegrees = fromDegrees;
			mToDegrees = toDegrees;
			mTurnIn = turnIn;
			mTurnUp = turnUp;
		}

		@Override
		public void initialize(int width, int height, int parentWidth,
				int parentHeight) {
			super.initialize(width, height, parentWidth, parentHeight);
			mCamera = new Camera();
			mCenterY = getHeight() / 2;
			mCenterX = getWidth() / 2;
		}

		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			final float fromDegrees = mFromDegrees;
			float degrees = fromDegrees
					+ ((mToDegrees - fromDegrees) * interpolatedTime);

			final float centerX = mCenterX;
			final float centerY = mCenterY;
			final Camera camera = mCamera;
			final int derection = mTurnUp ? 1 : -1;

			final Matrix matrix = t.getMatrix();

			camera.save();
			if (mTurnIn) {
				camera.translate(0.0f, derection * mCenterY
						* (interpolatedTime - 1.0f), 0.0f);
			} else {
				camera.translate(0.0f, derection * mCenterY
						* (interpolatedTime), 0.0f);
			}
			camera.rotateX(degrees);
			camera.getMatrix(matrix);
			camera.restore();

			matrix.preTranslate(-centerX, -centerY);
			matrix.postTranslate(centerX, centerY);
		}
	}
}
