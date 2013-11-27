/**
 * 
 */
package com.wxxr.mobile.stock.client.binding;

import android.app.Service;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IAttributeUpdater;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.stock.client.R;
import com.wxxr.mobile.stock.client.utils.Utils;

/**
 * @author dz
 * 
 */
public class EditTextAttributeUpdater implements IAttributeUpdater<View> {

	boolean isShow = true;
	static LayoutInflater inflater = LayoutInflater.from(AppUtils
			.getFramework().getAndroidApplication());
	// 定义浮动窗口布局 获取浮动窗口视图所在布局
	final static RelativeLayout mFloatLayout = (RelativeLayout) inflater
			.inflate(R.layout.edit_text_error_info_layout, null);

	@Override
	public <T> void updateControl(final View control, AttributeKey<T> attrType,
			IUIComponent field, Object value) {
		if (!(control instanceof EditText)) {
			return;
		}
		EditText editTv = (EditText) control;
		// editTv.setBackgroundColor(Color.BLACK);
		// if(value != null) {
		// editTv.setEdgingShow(true);
		// } else {
		// editTv.setEdgingShow(false);
		// }
		AppUtils.runOnUIThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				createFloatView(AppUtils
						.getFramework().getAndroidApplication().getApplicationContext(), control,
						isShow);
				isShow = !isShow;
			}
		});

	}

	@Override
	public <T> T updateModel(View control, AttributeKey<T> attrType,
			IUIComponent field) {
		return null;
	}

	@Override
	public boolean acceptable(Object control) {
		return control instanceof View;
	}

	private void createFloatView(final Context c, final View view, boolean show) {

		final WindowManager.LayoutParams wmParams;
		// 创建浮动窗口设置布局参数的对象
		// 获取的是WindowManagerImpl.CompatModeWrapper
		final WindowManager mWindowManager = (WindowManager) c
				.getSystemService(Service.WINDOW_SERVICE);

		if (show) {
			wmParams = new WindowManager.LayoutParams();
			
			// 设置window type
			wmParams.type = LayoutParams.TYPE_PHONE;
			// 设置图片格式，效果为背景透明
			wmParams.format = PixelFormat.RGBA_8888;
			// 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
			wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
			// 调整悬浮窗显示的停靠位置为左侧置顶
			wmParams.gravity = Gravity.LEFT | Gravity.TOP;
			// 以屏幕左上角为原点，设置x、y初始值，相对于gravity
			
			int[] location = Utils.getViewLTPoint(view);//new int[2];
//			view.getLocationOnScreen(location);
		    int x = location[0];
		    int y = location[1];
		    
			wmParams.x = x;
			wmParams.y = y - Utils.getStatusBarHeight(c);

			// 设置悬浮窗口长宽数据
			wmParams.width = view.getWidth();//WindowManager.LayoutParams.WRAP_CONTENT;
			wmParams.height = view.getHeight();//WindowManager.LayoutParams.WRAP_CONTENT;

			// 添加mFloatLayout
			mWindowManager.addView(mFloatLayout, wmParams);

			mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
					.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
			mFloatLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
					view.requestFocus();
				}
			});
			if(view instanceof EditText) {
				EditText editTv = (EditText) view;
				editTv.setOnFocusChangeListener(new OnFocusChangeListener() {
					
					@Override
					public void onFocusChange(View v, boolean hasFocus ) {
						// TODO Auto-generated method stub
						if(!hasFocus) {
							if (mFloatLayout != null) {
								if(!isShow) {
									mWindowManager.removeView(mFloatLayout);
									isShow = true;
								}
							}
//							mFloatLayout.setVisibility(View.VISIBLE);
						}
					}
				});
			}
			
		} else {
			if (mFloatLayout != null) {
				if(!isShow)
					mWindowManager.removeView(mFloatLayout);
			}
		}

	}
}
