/**
 * 
 */
package com.wxxr.mobile.stock.client.binding;


import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;

/**
 * @author dz
 *
 */
public class PageChangeEventBinding implements IBinding<IView>, OnPageChangeListener {

	private ViewPager control;
	private String fieldName,commandName;
	private IView pModel;
	
	public PageChangeEventBinding(View view,String cmdName,String field){
		this.control = (ViewPager)view;
		this.commandName = cmdName;
		this.fieldName = field;
	}
	
	@Override
	public void notifyDataChanged(ValueChangedEvent... events) {
	}

	@Override
	public void activate(IView model) {
		this.pModel = model;
		control.setOnPageChangeListener(this);	
	}

	@Override
	public void deactivate() {
		control.setOnPageChangeListener(null);
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
	public void doUpdate() {
		
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub

		IUIComponent field = pModel.getChild(fieldName);
		if(field != null){
			SimpleInputEvent event = new SimpleInputEvent("PageChange",field);
			event.addProperty("position", position);
			event.addProperty("size", control.getAdapter().getCount());
			field.invokeCommand(commandName, event);
		}else{
			SimpleInputEvent event = new SimpleInputEvent("PageChange",pModel);
			event.addProperty("position", position);
			event.addProperty("size", control.getAdapter().getCount());
			pModel.invokeCommand(commandName, event);
		}	
	
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub
		
	}

}
