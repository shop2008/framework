/**
 * 
 */
package com.wxxr.mobile.stock.client.binding;


import android.view.View;
import android.view.ViewGroup;

import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;
import com.wxxr.mobile.core.util.IAsyncCallback;
import com.wxxr.mobile.stock.client.widget.PullToRefreshListView;
import com.wxxr.mobile.stock.client.widget.PullToRefreshListView.IRefreshViewListener;
import com.wxxr.mobile.stock.client.widget.PullToRefreshView;
import com.wxxr.mobile.stock.client.widget.PullToRefreshView.OnFooterRefreshListener;
import com.wxxr.mobile.stock.client.widget.PullToRefreshView.OnHeaderRefreshListener;

/**
 * @author neillin
 *
 */
public class RefreshEventBinding implements IBinding<IView> {

	private ViewGroup control;
	private String fieldName,commandName;
	private IView pModel;
	private OnHeaderRefreshListener headerListener = new OnHeaderRefreshListener() {
		
		@Override
		public void onHeaderRefresh(PullToRefreshView view) {
			
			IUIComponent field = pModel.getChild(fieldName);
			if(field != null){
				SimpleInputEvent event = new SimpleInputEvent("TopRefresh",field);
				event.addProperty(InputEvent.PROPERTY_CALLBACK, new IAsyncCallback() {
					
					@Override
					public void success(Object arg0) {
						if(control instanceof PullToRefreshView) {
							((PullToRefreshView)control).onHeaderRefreshComplete();
						}
					}
					
					@Override
					public void failed(Object arg0) {
						if(control instanceof PullToRefreshView) {
							((PullToRefreshView)control).onHeaderRefreshComplete();
						}
					}
				});
				field.invokeCommand(commandName, event);
			}else{
				SimpleInputEvent event = new SimpleInputEvent("TopRefresh",pModel);
				pModel.invokeCommand(commandName, event);
			}			
		}
	};
	
	private OnFooterRefreshListener footerListener = new OnFooterRefreshListener() {
		
		@Override
		public void onFooterRefresh(PullToRefreshView view) {
			
			IUIComponent field = pModel.getChild(fieldName);
			if(field != null){
				SimpleInputEvent event = new SimpleInputEvent("BottomRefresh",field);
				event.addProperty(InputEvent.PROPERTY_CALLBACK, new IAsyncCallback() {
					
					@Override
					public void success(Object arg0) {
						if(control instanceof PullToRefreshView) {
							((PullToRefreshView)control).onFooterRefreshComplete();
						}
					}
					
					@Override
					public void failed(Object arg0) {
						if(control instanceof PullToRefreshView) {
							((PullToRefreshView)control).onFooterRefreshComplete();
						}
					}
				});
				field.invokeCommand(commandName, event);
			}else{
				SimpleInputEvent event = new SimpleInputEvent("BottomRefresh",pModel);
				pModel.invokeCommand(commandName, event);
			}			
		}
	};
	
	private IRefreshViewListener refreshListener = new IRefreshViewListener() {

		@Override
		public void onRefresh() {
			IUIComponent field = pModel.getChild(fieldName);
			if(field != null){
				SimpleInputEvent event = new SimpleInputEvent("TopRefresh",field);
				event.addProperty(InputEvent.PROPERTY_CALLBACK, new IAsyncCallback() {
					
					@Override
					public void success(Object arg0) {
						if(control instanceof PullToRefreshListView) {
							((PullToRefreshListView)control).stopRefresh();
						}
					}
					
					@Override
					public void failed(Object arg0) {
						if(control instanceof PullToRefreshListView) {
							((PullToRefreshListView)control).stopRefresh();
						}
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
			IUIComponent field = pModel.getChild(fieldName);
			if(field != null){
				SimpleInputEvent event = new SimpleInputEvent("BottomRefresh",field);
				event.addProperty(InputEvent.PROPERTY_CALLBACK, new IAsyncCallback() {
					
					@Override
					public void success(Object arg0) {
						if(control instanceof PullToRefreshListView) {
							((PullToRefreshListView)control).stopLoadMore();
						}
					}
					
					@Override
					public void failed(Object arg0) {
						if(control instanceof PullToRefreshListView) {
							((PullToRefreshListView)control).stopLoadMore();
						}
					}
				});
				field.invokeCommand(commandName, event);
			}else{
				SimpleInputEvent event = new SimpleInputEvent("BottomRefresh",pModel);
				pModel.invokeCommand(commandName, event);
			}	
		}
	};
	public RefreshEventBinding(View view,String cmdName,String field){
		this.control = (ViewGroup)view;
		this.commandName = cmdName;
		this.fieldName = field;
	}
	
	@Override
	public void notifyDataChanged(ValueChangedEvent... events) {
	}

	@Override
	public void activate(IView model) {
		this.pModel = model;
		if(control instanceof PullToRefreshListView) {
			((PullToRefreshListView)control).setRefreshViewListener(refreshListener);
		} else if(control instanceof PullToRefreshView) {
			((PullToRefreshView)control).setOnHeaderRefreshListener(headerListener);
			((PullToRefreshView)control).setOnFooterRefreshListener(footerListener);
		}		
	}

	@Override
	public void deactivate() {
		if(control instanceof PullToRefreshListView) {
			((PullToRefreshListView)control).setRefreshViewListener(null);
		} else if(control instanceof PullToRefreshView) {
			((PullToRefreshView)control).setOnHeaderRefreshListener(null);
			((PullToRefreshView)control).setOnFooterRefreshListener(null);
		}
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

}
