package com.wxxr.mobile.stock.client.binding;

import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.common.AbstractEventBinding;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;

public class EditTextFocusChangedEnventBinding extends AbstractEventBinding {

	private EditText editText;

	public EditTextFocusChangedEnventBinding(View view, String cmdName,
			String field) {
		this.editText = (EditText) view;
		setUIControl(this.editText);
		setCommandName(cmdName);
		setFieldName(field);
	}

	private OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			
			
			if(hasFocus == false) {
				SimpleInputEvent event = new SimpleInputEvent("FocusChanged",
						getField());
				handleInputEvent(event);
			}
		}
	};

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
