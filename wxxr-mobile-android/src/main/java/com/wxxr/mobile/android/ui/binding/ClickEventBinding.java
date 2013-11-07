/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;


import android.view.View;
import android.view.View.OnClickListener;

import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;

/**
 * @author neillin
 *
 */
public class ClickEventBinding implements IBinding<IView>,OnClickListener {

	private View control;
	private String commandName, fieldName;
	private IView pModel;
	
	public ClickEventBinding(View view,String cmdName,String field){
		this.control = view;
		this.commandName = cmdName;
		this.fieldName = field;
	}
	
	@Override
	public void notifyDataChanged(ValueChangedEvent... events) {
	}

	@Override
	public void activate(IView model) {
//		ClickEventListenerDispatcher dispatcher = (ClickEventListenerDispatcher)control.getTag(ClickEventListenerDispatcher.TAG_ID);
//		if(dispatcher == null){
//			dispatcher = new ClickEventListenerDispatcher();
//			control.setTag(ClickEventListenerDispatcher.TAG_ID, dispatcher);
//			control.setOnClickListener(dispatcher);
//		}
//		dispatcher.addListener(this);
		this.control.setOnClickListener(this);
		this.pModel = model;
	}

	@Override
	public void deactivate() {
//		ClickEventListenerDispatcher dispatcher = (ClickEventListenerDispatcher)control.getTag(ClickEventListenerDispatcher.TAG_ID);
//		if(dispatcher != null){
//			dispatcher.removeListener(this);
//		}
		this.control.setOnClickListener(null);
		this.pModel = null;
		
	}

	@Override
	public void destroy() {
		deactivate();
		this.control = null;
	}

	@Override
	public void init(IWorkbenchRTContext ctx) {
		
	}

	@Override
	public Object getUIControl() {
		return this.control;
	}

	@Override
	public void onClick(View v) {
		IUIComponent field = this.pModel.getChild(this.fieldName);
		if(field != null){
			SimpleInputEvent event = new SimpleInputEvent(InputEvent.EVENT_TYPE_CLICK,field);
			field.invokeCommand(commandName, event);
		}else{
			SimpleInputEvent event = new SimpleInputEvent(InputEvent.EVENT_TYPE_CLICK,this.pModel);
			this.pModel.invokeCommand(commandName, event);
		}
	}

	@Override
	public void refresh() {
	}

}
