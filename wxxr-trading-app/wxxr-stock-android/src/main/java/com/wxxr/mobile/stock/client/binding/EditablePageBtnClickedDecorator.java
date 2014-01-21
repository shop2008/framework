package com.wxxr.mobile.stock.client.binding;

import android.view.View;

import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.InputEventDecorator;
import com.wxxr.mobile.core.ui.api.InputEventHandlingContext;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;

public class EditablePageBtnClickedDecorator implements InputEventDecorator {
	private final InputEventDecorator next;
	
	public EditablePageBtnClickedDecorator(InputEventDecorator decor){
		this.next = decor;
	}
	
	@Override
	public void handleEvent(InputEventHandlingContext context, InputEvent event) {
		final View v = (View)context.getUIControl();
		v.setFocusable(true);
		v.setFocusableInTouchMode(true);
		v.requestFocus();
		((SimpleInputEvent)event).addProperty(InputEvent.PROPERTY_SOURCE_VIEW,v);
		
		this.next.handleEvent(context, event);
	}
	
}
