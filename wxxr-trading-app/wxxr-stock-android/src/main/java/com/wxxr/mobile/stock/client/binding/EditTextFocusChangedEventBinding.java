package com.wxxr.mobile.stock.client.binding;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AbstractEventBinding;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;
import com.wxxr.mobile.core.util.ObjectUtils;

public class EditTextFocusChangedEventBinding extends AbstractEventBinding {
	private static final Trace log = Trace.getLogger("com.wxxr.mobile.stock.client.binding.EditTextFocusChangedEventBinding");
	
	private class EventMonitor implements Runnable {
		private boolean keepRunning;
		private Thread workingThread;
		private long lastEditTime;
		private long timeoutInMills = 2000L;	// 2 seconds by default
		private String text = "";
		private boolean active;
		
		
		@Override
		public void run() {
			this.workingThread = Thread.currentThread();
			while(keepRunning){
				String newText = editText.getText().toString();
				if(isActive()&&((System.currentTimeMillis() - this.lastEditTime) >= this.timeoutInMills)&&(ObjectUtils.isEquals(this.text, newText) == false)){
					fireTextChangedEvent(newText);
				}else {
					try {
						Thread.sleep(200L);
					} catch (InterruptedException e) {
					}
				}
			}
		}
		
		
		public void start() {
			if(log.isDebugEnabled()){
				log.debug("Starting text changed event monitor");
			}
			this.keepRunning = true;
			new Thread(this, "TextChanged Event monitor").start();
		}
		
		public void stop() {
			if(log.isDebugEnabled()){
				log.debug("Stopping text changed event monitor");
			}
			this.keepRunning = false;
			this.lastEditTime = System.currentTimeMillis();
			this.text = editText.getText().toString();
			if((this.workingThread != null)&&this.workingThread.isAlive()){
				this.workingThread.interrupt();
				try {
					this.workingThread.join(1000L);
				} catch (InterruptedException e) {
				}
			}
			this.workingThread = null;
		}


		/**
		 * @return the lastEditTime
		 */
		@SuppressWarnings("unused")
		public long getLastEditTime() {
			return lastEditTime;
		}


		/**
		 * @param lastEditTime the lastEditTime to set
		 */
		public void setLastEditTime(long lastEditTime) {
			this.lastEditTime = lastEditTime;
		}


		/**
		 * @return the timeoutInMills
		 */
		@SuppressWarnings("unused")
		public long getTimeoutInMills() {
			return timeoutInMills;
		}


		/**
		 * @param timeoutInMills the timeoutInMills to set
		 */
		@SuppressWarnings("unused")
		public void setTimeoutInMills(long timeoutInMills) {
			this.timeoutInMills = timeoutInMills;
		}


		public void fireTextChangedEvent() {
			String content = editText.getText().toString();
			if(ObjectUtils.isEquals(this.text, content) == false) {
				fireTextChangedEvent(content);
			}
		}
		
		/**
		 * @param content
		 */
		protected void fireTextChangedEvent(String content) {
			if(log.isDebugEnabled()){
				log.debug("Going to fire Text changed event with content :["+content+"]");
			}
			SimpleInputEvent event = new SimpleInputEvent(InputEvent.EVENT_TYPE_TEXT_CHANGED,
					getField());
//			editText.setSelection(content.length());
			event.addProperty("changedText", ""+content);
			handleInputEvent(event);
			this.text = content;
		}


		/**
		 * @return the active
		 */
		public boolean isActive() {
			return active;
		}


		/**
		 * @param active the active to set
		 */
		public void setActive(boolean active) {
			if(this.active == active){
				return;
			}
			this.active = active;
			if(this.active){
				if(log.isDebugEnabled()){
					log.debug("Going to make text changed event monitor active");
				}
				editText.addTextChangedListener(textWatcher);
				editText.setOnEditorActionListener(actionListener);
			}else{
				if(log.isDebugEnabled()){
					log.debug("Going to make text changed event monitor inactive");
				}
				editText.removeTextChangedListener(textWatcher);
				editText.setOnEditorActionListener(null);
			}
		}
	}
	
	
	private EditText editText;
	private boolean mHasFocus;
	private EventMonitor monitor;
	
	public EditTextFocusChangedEventBinding(View view, String cmdName,
			String field) {
		this.editText = (EditText) view;
		setUIControl(this.editText);
		setCommandName(cmdName);
		setFieldName(field);
	}
	
	private EditText.OnEditorActionListener actionListener = new EditText.OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_SEARCH ||
					actionId == EditorInfo.IME_ACTION_DONE ||
					event.getAction() == KeyEvent.ACTION_DOWN &&
					event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
				if (event == null || !event.isShiftPressed()) {
					if(monitor != null){
						monitor.fireTextChangedEvent();
					}
					return true; // consume.
				}                
			}
			return false; // pass on to other listeners. 
		}
	};

	private OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			
			mHasFocus = hasFocus;
			if(!mHasFocus) {
				if(monitor != null){
					monitor.fireTextChangedEvent();
					monitor.setActive(false);
				}
			} else {
				monitor.setActive(true);
			}
		}
	};
	
	private TextWatcher textWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if(monitor != null){
				monitor.setLastEditTime(System.currentTimeMillis());
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
		}
	};


	@Override
	public void activate(IView model) {
		super.activate(model);
		this.monitor = new EventMonitor();
		this.monitor.start();
		this.editText.setOnFocusChangeListener(focusChangeListener);
	}

	@Override
	public void deactivate() {
		if(this.monitor != null){
			this.monitor.stop();
			this.monitor = null;
		}
		this.editText.setOnFocusChangeListener(null);
		super.deactivate();
	}

	@Override
	public void destroy() {
		deactivate();
		this.editText = null;
	}

}
