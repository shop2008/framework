package com.wxxr.mobile.stock.client.binding;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;

public class TextChangedEventBinding implements IBinding<IView> {

	private EditText editText;
	private String fieldName;
	private String commandName;
	private IView pModel;
	
	public TextChangedEventBinding(View view,String cmdName,String field) {
		this.editText = (EditText)view; 
		this.commandName = cmdName;
		this.fieldName = field;
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
			String text = s.toString();
			if (!TextUtils.isEmpty(text)) {
				SimpleInputEvent event = new SimpleInputEvent(InputEvent.EVENT_TYPE_TEXT_CHANGED,
						pModel);
				editText.setSelection(text.length());
				event.addProperty("changedText", ""+text);
				IUIComponent field = pModel.getChild(fieldName);
				if (field != null) {
					field.invokeCommand(commandName, event);
				} else {
					pModel.invokeCommand(commandName, event);
				}
			}	
		}
	};

	@Override
	public void notifyDataChanged(ValueChangedEvent... events) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activate(IView model) {
		this.editText.addTextChangedListener(textWatcher);
		this.pModel = model;
	}

	@Override
	public void deactivate() {
		this.editText.addTextChangedListener(null);
		this.pModel = null;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		deactivate();
		this.editText = null;
	}

	@Override
	public void init(IWorkbenchRTContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getUIControl() {
		return this.editText;
	}
}
