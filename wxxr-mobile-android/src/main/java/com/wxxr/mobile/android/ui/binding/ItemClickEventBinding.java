/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;


import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

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
public class ItemClickEventBinding implements IBinding<IView>,OnItemClickListener {

	private AdapterView<?> control;
	private String fieldName,commandName;
	private IView pModel;
	
	public ItemClickEventBinding(View view,String cmdName,String field){
		this.control = (AdapterView<?>)view;
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
		this.control.setOnItemClickListener(this);
		this.pModel = model;
	}

	@Override
	public void deactivate() {
//		ClickEventListenerDispatcher dispatcher = (ClickEventListenerDispatcher)control.getTag(ClickEventListenerDispatcher.TAG_ID);
//		if(dispatcher != null){
//			dispatcher.removeListener(this);
//		}
		this.control.setOnItemClickListener(null);
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		SimpleInputEvent event = new SimpleInputEvent(InputEvent.EVENT_TYPE_ITEM_CLICK,this.pModel);
		event.addProperty("position", position);
		IUIComponent field = this.pModel.getChild(this.fieldName);
		if(field != null){
			field.invokeCommand(commandName, event);
		}else{
			this.pModel.invokeCommand(commandName, event);
		}
	}

}
