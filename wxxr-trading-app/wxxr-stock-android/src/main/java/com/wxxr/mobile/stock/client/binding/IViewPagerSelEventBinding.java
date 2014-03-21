package com.wxxr.mobile.stock.client.binding;

import android.view.View;

import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.AbstractEventBinding;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;
import com.wxxr.mobile.stock.client.widget.GuideSwiperView;

public class IViewPagerSelEventBinding extends AbstractEventBinding {

	private GuideSwiperView control;
	
	public IViewPagerSelEventBinding(View view,String cmdName,String field){
		this.control = (GuideSwiperView)view;
		setUIControl(this.control);
		setCommandName(cmdName);
		setFieldName(field);
	}
	
	private IViewPagerSelCallBack iViewSelCallBack = new IViewPagerSelCallBack() {
		
		@Override
		public void selected(int position) {
			IUIComponent field = getField();
			if (field != null) {
				SimpleInputEvent event = new SimpleInputEvent("SelCallBack",field);
				event.addProperty("selectPos", position);
				handleInputEvent(event);
			} else {
				SimpleInputEvent event = new SimpleInputEvent("SelCallBack",getModel());
				handleInputEvent(event);
			}
		}
	};
	
	
	
	@Override
	public void notifyDataChanged(ValueChangedEvent... events) {
	}

	@Override
	public void activate(IView model) {
		super.activate(model);
		this.control.setIViewPageSelCallBack(iViewSelCallBack);
	}

	@Override
	public void deactivate() {
		this.control.setIViewPageSelCallBack(null);
		super.deactivate();
	}
}
