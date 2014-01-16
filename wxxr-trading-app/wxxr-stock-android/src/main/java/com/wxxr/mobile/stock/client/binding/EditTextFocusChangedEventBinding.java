package com.wxxr.mobile.stock.client.binding;

import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.common.AbstractEventBinding;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;

public class EditTextFocusChangedEventBinding extends AbstractEventBinding {

	private EditText editText;

	public EditTextFocusChangedEventBinding(View view, String cmdName,
			String field) {
		this.editText = (EditText) view;
		setUIControl(this.editText);
		setCommandName(cmdName);
		setFieldName(field);
	}

	private OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			
//<<<<<<< EditTextFocusChangedEventBinding.java
			SimpleInputEvent event = new SimpleInputEvent("FocusChanged",
					getField());
			handleInputEvent(event);
/*=======
			mHasFocus = hasFocus;
			if(!mHasFocus) {
				if(monitor != null){
					monitor.fireTextChangedEvent();
					monitor.setActive(false);
				}
			} else {
				monitor.setActive(true);
			}*/
		}
	};
	
/*	private TextWatcher textWatcher = new TextWatcher() {
		
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
>>>>>>> 1.6
		}
	};*/

	@Override
	public void activate(IView model) {
		super.activate(model);
		this.editText.setOnFocusChangeListener(focusChangeListener);
	}

	@Override
	public void deactivate() {
		this.editText.setOnFocusChangeListener(null);
		super.deactivate();
	}

	@Override
	public void destroy() {
		deactivate();
		this.editText = null;
	}

}
