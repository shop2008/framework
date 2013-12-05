package com.wxxr.mobile.stock.client.binding;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;

public class SpinnerItemClickEventBinding implements IBinding<IView>,OnItemSelectedListener{

	private Spinner control;
	private String fieldName;
	private String commandName;
	private IView pModel;
	
	public SpinnerItemClickEventBinding(View view,String cmdName,String field){
		this.control = (Spinner)view;
		this.commandName = cmdName;
		this.fieldName = field;
	}
	
	
	@Override
	public void notifyDataChanged(ValueChangedEvent... events) {
		
	}

	@Override
	public void activate(IView model) {
		this.pModel = model;
		if(control instanceof Spinner) {
			((Spinner)control).setOnItemSelectedListener(this);
		} 	
		
	}

	@Override
	public void deactivate() {
		if(control instanceof Spinner) {
			((Spinner)control).setOnItemSelectedListener(null);
		} 
		this.pModel = null;
	}

	@Override
	public void refresh() {
		
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
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		IUIComponent field = pModel.getChild(fieldName);
		if (field != null) {
			SimpleInputEvent event = new SimpleInputEvent("SpinnerItemSelected",field);
			event.addProperty("position", position);
			field.invokeCommand(commandName, event);
		} else {
			SimpleInputEvent event = new SimpleInputEvent("SpinnerItemSelected",pModel);
			pModel.invokeCommand(commandName, event);
		}		
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	

//	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
//		IUIComponent field = pModel.getChild(fieldName);
//		if (field != null) {
//			SimpleInputEvent event = new SimpleInputEvent("SpinnerItemClick",field);
//			event.addProperty("position", position);
//			field.invokeCommand(commandName, event);
//		} else {
//			SimpleInputEvent event = new SimpleInputEvent("SpinnerItemClick",pModel);
//			pModel.invokeCommand(commandName, event);
//		}
//	}

}
