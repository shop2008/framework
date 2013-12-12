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
import com.wxxr.mobile.core.ui.common.AbstractEventBinding;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;

public class SpinnerItemClickEventBinding extends AbstractEventBinding implements OnItemSelectedListener{

	private Spinner control;
	
	public SpinnerItemClickEventBinding(View view,String cmdName,String field){
		this.control = (Spinner)view;
		setUIControl(this.control);
		setCommandName(cmdName);
		setFieldName(field);
	}
	

	@Override
	public void activate(IView model) {
		super.activate(model);
		if(control instanceof Spinner) {
			((Spinner)control).setOnItemSelectedListener(this);
		} 	
		
	}

	@Override
	public void deactivate() {
		if(control instanceof Spinner) {
			((Spinner)control).setOnItemSelectedListener(null);
		} 
		super.deactivate();
	}


	@Override
	public void destroy() {
		deactivate();
		this.control = null;
	}


	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		IUIComponent field = getField();
		if (field != null) {
			SimpleInputEvent event = new SimpleInputEvent("SpinnerItemSelected",field);
			event.addProperty("position", position);
			handleInputEvent(event);
		} else {
			SimpleInputEvent event = new SimpleInputEvent("SpinnerItemSelected",getModel());
			handleInputEvent(event);
		}		
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

}
