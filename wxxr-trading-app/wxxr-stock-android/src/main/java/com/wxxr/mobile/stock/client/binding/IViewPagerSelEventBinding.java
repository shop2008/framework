package com.wxxr.mobile.stock.client.binding;

import android.view.View;

import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;
import com.wxxr.mobile.stock.client.widget.GuideSwiperView;

public class IViewPagerSelEventBinding implements IBinding<IView> {

	private GuideSwiperView control;
	private String fieldName,commandName;
	private IView pModel;
	
	public IViewPagerSelEventBinding(View view,String cmdName,String field){
		this.control = (GuideSwiperView)view;
		this.commandName = cmdName;
		this.fieldName = field;
	}
	
	private IViewPagerSelCallBack iViewSelCallBack = new IViewPagerSelCallBack() {
		
		@Override
		public void selected(int position) {
			IUIComponent field = pModel.getChild(fieldName);
			if (field != null) {
				SimpleInputEvent event = new SimpleInputEvent("SelCallBack",field);
				event.addProperty("selectPos", position);
				
				field.invokeCommand(commandName, event);
			} else {
				SimpleInputEvent event = new SimpleInputEvent("SelCallBack",pModel);
				pModel.invokeCommand(commandName, event);
			}
		}
	};
	
	
	
	@Override
	public void notifyDataChanged(ValueChangedEvent... events) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activate(IView model) {
		this.pModel = model;
		this.control.setIViewPageSelCallBack(iViewSelCallBack);
	}

	@Override
	public void deactivate() {
		this.pModel = null;
		this.control.setIViewPageSelCallBack(null);
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		deactivate();
		this.control = null;
	}

	@Override
	public void init(IWorkbenchRTContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getUIControl() {
		return this.control;
	}

}
