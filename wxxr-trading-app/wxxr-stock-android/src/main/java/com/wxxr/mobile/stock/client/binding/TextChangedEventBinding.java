package com.wxxr.mobile.stock.client.binding;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AbstractEventBinding;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;
import com.wxxr.mobile.stock.client.widget.TextSpinnerView;

public class TextChangedEventBinding extends AbstractEventBinding {

	private static Trace log = Trace.getLogger(TextChangedEventBinding.class);
	
	private EditText editText;
	
	public TextChangedEventBinding(View view,String cmdName,String field) {
		this.editText = (EditText)view; 
		setUIControl(this.editText);
		setCommandName(cmdName);
		setFieldName(field);
	}
	
	private TextWatcher textWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			
			if (null == s) {
				return;
			}
			int position = -1;
			String text = s.toString();
			if(log.isDebugEnabled()) {
				log.debug("afterTextChanged , text is : " + text);
			}
			SimpleInputEvent event = new SimpleInputEvent(InputEvent.EVENT_TYPE_TEXT_CHANGED,
					getField());
			editText.setSelection(text.length());
			event.addProperty("changedText", ""+text);
			if(editText instanceof TextSpinnerView){
				if(editText.getTag() instanceof Integer){
					position = (Integer) editText.getTag();
				}
				event.addProperty("position", position);
			}
			handleInputEvent(event);
		}
	};

	@Override
	public void activate(IView model) {
		super.activate(model);
		this.editText.addTextChangedListener(textWatcher);
	}

	@Override
	public void deactivate() {
		this.editText.removeTextChangedListener(textWatcher);
		super.deactivate();
	}


	@Override
	public void destroy() {
		deactivate();
		this.editText = null;
	}

}
