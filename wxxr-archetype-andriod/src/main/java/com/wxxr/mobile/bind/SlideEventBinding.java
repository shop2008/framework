package com.wxxr.mobile.bind;

import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;

import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AbstractEventBinding;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;
import com.wxxr.mobile.core.util.IAsyncCallback;

public class SlideEventBinding extends AbstractEventBinding {

	private View control;
	private static final int SWIPE_MIN_DISTANCE = 120; 
	private static final int SWIPE_MAX_OFF_PATH = 250; 
	private static final int   SWIPE_THRESHOLD_VELOCITY = 200;
	
	
	private GestureDetector gestureDetector = new GestureDetector(new SimpleOnGestureListener(){
		public boolean onFling(MotionEvent e1, MotionEvent e2,  float velocityX, float velocityY) {  
			if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs (velocityX) > SWIPE_THRESHOLD_VELOCITY) { 
				IUIComponent field = getField();
				SimpleInputEvent event = new SimpleInputEvent(InputEvent.EVENT_TYPE_SWIPE_LEFT,field);
				handleInputEvent(event);
			}
			return true;
		}
		
		public boolean onDoubleTap(MotionEvent e){
			IUIComponent field = getField();
			SimpleInputEvent event = new SimpleInputEvent(InputEvent.EVENT_TYPE_SWIPE_LEFT,field);
			handleInputEvent(event);
			return true;
		}
	});
	
	public SlideEventBinding(View bindingControl, String cmdName, String field) {
		control = bindingControl;
		super.setCommandName(cmdName);
		super.setFieldName(field);
		if(control != null){
//			control.setOnTouchListener(new OnTouchListener() {
//				
//				@Override
//				public boolean onTouch(View v, MotionEvent event) {
//					if(gestureDetector.onTouchEvent(event)){
//						return true;
//					}
//					return false;
//				}
//			});
			control.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					IUIComponent field = getField();
					SimpleInputEvent event = new SimpleInputEvent(InputEvent.EVENT_TYPE_SWIPE_LEFT,field);
					handleInputEvent(event);
					return true;
				}
			});
			
		}
	}
	
	@Override
	public void activate(IView model) {
		super.activate(model);
		
	}

}
