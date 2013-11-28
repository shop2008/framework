package com.wxxr.mobile.stock.client.binding;

import android.view.View;

import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;
import com.wxxr.mobile.stock.client.widget.PinnedHeaderListView;

public class IPinHeadItemClickBinding implements IBinding<IView>{

	private PinnedHeaderListView control;
	private String fieldName;
	private String commandName;
	private IView pModel;
	
	public IPinHeadItemClickBinding(View view,String cmdName,String field){
		this.control = (PinnedHeaderListView)view;
		this.commandName = cmdName;
		this.fieldName = field;
	}
	
	
	private IPinHeadViewItemClick iPinHeadViewItemClick = new IPinHeadViewItemClick() {
		
		@Override
		public void onItemClick(int position) {
			IUIComponent field = pModel.getChild(fieldName);
			if (field != null) {
				SimpleInputEvent event = new SimpleInputEvent("PinItemClick",field);
				event.addProperty("position", position);
				field.invokeCommand(commandName, event);
			} else {
				SimpleInputEvent event = new SimpleInputEvent("PinItemClick",pModel);
				pModel.invokeCommand(commandName, event);
			}
		}
	};
	
	@Override
	public void notifyDataChanged(ValueChangedEvent... events) {
		
	}

	@Override
	public void activate(IView model) {
		this.pModel = model;
		if(control instanceof PinnedHeaderListView) {
			((PinnedHeaderListView)control).setIPinHeaderItemClick(iPinHeadViewItemClick);
		} 	
		
	}

	@Override
	public void deactivate() {
		if(control instanceof PinnedHeaderListView) {
			((PinnedHeaderListView)control).setIPinHeaderItemClick(null);
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

}
