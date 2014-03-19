package com.wxxr.mobile.stock.client.binding;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.common.AbstractEventBinding;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;

public class EditTextActionEventBinding  extends AbstractEventBinding {
	private EditText editText;
	
	
	public EditTextActionEventBinding(View view, String cmdName,
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
					actionId == EditorInfo.IME_ACTION_NEXT) {
				SimpleInputEvent inputEvent = new SimpleInputEvent("ActionDone",
						getField());
				
				String text = editText.getText()!=null?editText.getText().toString().trim():null;
				inputEvent.addProperty("textContent", ""+text);
				handleInputEvent(inputEvent);
				return true;
			}
			return false; // pass on to other listeners. 
		}
	};
	
	@Override
	public void activate(IView model) {
		super.activate(model);
		this.editText.setOnEditorActionListener(actionListener);
	};
	
	@Override
	public void deactivate() {
		super.deactivate();
		this.editText.setOnEditorActionListener(null);
	}

	@Override
	public void destroy() {
		deactivate();
		this.editText = null;
	}
}
