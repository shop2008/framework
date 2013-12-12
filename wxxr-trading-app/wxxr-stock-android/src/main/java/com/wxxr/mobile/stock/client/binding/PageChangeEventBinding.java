/**
 * 
 */
package com.wxxr.mobile.stock.client.binding;


import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.AbstractEventBinding;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;

/**
 * @author dz
 *
 */
public class PageChangeEventBinding extends AbstractEventBinding implements  OnPageChangeListener {

	private ViewPager control;
	
	public PageChangeEventBinding(View view,String cmdName,String field){
		this.control = (ViewPager)view;
		setUIControl(control);
		setCommandName(cmdName);
		setFieldName(field);
	}
	
	@Override
	public void notifyDataChanged(ValueChangedEvent... events) {
	}

	@Override
	public void activate(IView model) {
		super.activate(model);
		control.setOnPageChangeListener(this);	
	}

	@Override
	public void deactivate() {
		control.setOnPageChangeListener(null);
		super.deactivate();
		
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
	public void doUpdate() {
		
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {

		IUIComponent field = getField();
		if(field != null){
			SimpleInputEvent event = new SimpleInputEvent("PageChange",field);
			event.addProperty("position", position);
			event.addProperty("size", control.getAdapter().getCount());
			handleInputEvent(event);
		}else{
			SimpleInputEvent event = new SimpleInputEvent("PageChange",getModel());
			event.addProperty("position", position);
			event.addProperty("size", control.getAdapter().getCount());
			handleInputEvent(event);
		}	
	
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		
	}

}
