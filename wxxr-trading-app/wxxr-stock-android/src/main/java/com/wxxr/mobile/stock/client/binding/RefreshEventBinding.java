/**
 * 
 */
package com.wxxr.mobile.stock.client.binding;


import android.view.View;

import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;
import com.wxxr.mobile.stock.client.widget.PullToRefreshListView;
import com.wxxr.mobile.stock.client.widget.PullToRefreshListView.IRefreshListViewListener;

/**
 * @author neillin
 *
 */
public class RefreshEventBinding implements IBinding<IView> {

	private PullToRefreshListView control;
	private String fieldName,commandName;
	private IView pModel;
//	private OnHeaderRefreshListener headerListener = new OnHeaderRefreshListener() {
//		
//		@Override
//		public void onHeaderRefresh(PullToRefreshView view) {
//			SimpleInputEvent event = new SimpleInputEvent("TopRefresh",pModel);
//			IUIComponent field = pModel.getChild(fieldName);
//			if(field != null){
//				field.invokeCommand(commandName, event);
//			}else{
//				pModel.invokeCommand(commandName, event);
//			}			
//		}
//	};
//	
//	private OnFooterRefreshListener footerListener = new OnFooterRefreshListener() {
//		
//		@Override
//		public void onFooterRefresh(PullToRefreshView view) {
//			SimpleInputEvent event = new SimpleInputEvent("BottomRefresh",pModel);
//			IUIComponent field = pModel.getChild(fieldName);
//			if(field != null){
//				field.invokeCommand(commandName, event);
//			}else{
//				pModel.invokeCommand(commandName, event);
//			}			
//		}
//	};
	
	private IRefreshListViewListener refreshListener = new IRefreshListViewListener() {

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			IUIComponent field = pModel.getChild(fieldName);
			if(field != null){
				SimpleInputEvent event = new SimpleInputEvent("TopRefresh",field);
				event.addProperty("callback", new IRefreshCallback() {
					
					@Override
					public void refreshSuccess() {
						// TODO Auto-generated method stub
						control.stopRefresh();
					}
					
					@Override
					public void refreshFailed(String message) {
						// TODO Auto-generated method stub
						control.stopRefresh();
					}
				});
				field.invokeCommand(commandName, event);
			}else{
				SimpleInputEvent event = new SimpleInputEvent("TopRefresh",pModel);
				pModel.invokeCommand(commandName, event);
			}
		}
		@Override
		public void onLoadMore() {
			// TODO Auto-generated method stub
			IUIComponent field = pModel.getChild(fieldName);
			if(field != null){
				SimpleInputEvent event = new SimpleInputEvent("BottomRefresh",field);
				field.invokeCommand(commandName, event);
			}else{
				SimpleInputEvent event = new SimpleInputEvent("BottomRefresh",pModel);
				pModel.invokeCommand(commandName, event);
			}	
		}
	};
	public RefreshEventBinding(View view,String cmdName,String field){
		this.control = (PullToRefreshListView)view;
		this.commandName = cmdName;
		this.fieldName = field;
	}
	
	@Override
	public void notifyDataChanged(ValueChangedEvent... events) {
	}

	@Override
	public void activate(IView model) {
//		this.control.setOnHeaderRefreshListener(headerListener);
//		this.control.setOnFooterRefreshListener(footerListener);
		this.control.setRefreshListViewListener(refreshListener);
		this.pModel = model;
	}

	@Override
	public void deactivate() {
//		this.control.setOnHeaderRefreshListener(null);
//		this.control.setOnFooterRefreshListener(null);
		this.control.setRefreshListViewListener(null);
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
	public void refresh() {
		
	}

}
