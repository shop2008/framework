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
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;
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
				event.addProperty("callback", new IRefreshCallback() {
					
					@Override
					public void refreshSuccess() {
						// TODO Auto-generated method stub
						if(control instanceof PullToRefreshView) {
							((PullToRefreshView)control).onHeaderRefreshComplete();
						}
					}
					
					@Override
					public void refreshFailed(String message) {
						// TODO Auto-generated method stub
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
				event.addProperty("callback", new IRefreshCallback() {
					
					@Override
					public void refreshSuccess() {
						// TODO Auto-generated method stub
						if(control instanceof PullToRefreshView) {
							((PullToRefreshView)control).onFooterRefreshComplete();
						}
					}
					
					@Override
					public void refreshFailed(String message) {
						// TODO Auto-generated method stub
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
			// TODO Auto-generated method stub
			IUIComponent field = pModel.getChild(fieldName);
			if(field != null){
				SimpleInputEvent event = new SimpleInputEvent("TopRefresh",field);
				event.addProperty("callback", new IRefreshCallback() {
					
					@Override
					public void refreshSuccess() {
						// TODO Auto-generated method stub
						if(control instanceof PullToRefreshListView) {
							((PullToRefreshListView)control).stopRefresh();
						}
					}
					
					@Override
					public void refreshFailed(String message) {
						// TODO Auto-generated method stub
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
			// TODO Auto-generated method stub
			IUIComponent field = pModel.getChild(fieldName);
			if(field != null){
				SimpleInputEvent event = new SimpleInputEvent("BottomRefresh",field);
				event.addProperty("callback", new IRefreshCallback() {
					
					@Override
					public void refreshSuccess() {
						// TODO Auto-generated method stub
						if(control instanceof PullToRefreshListView) {
							((PullToRefreshListView)control).stopLoadMore();
						}
					}
					
					@Override
					public void refreshFailed(String message) {
						// TODO Auto-generated method stub
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
//		if(control instanceof PullToRefreshListView) {
//			this.control = (PullToRefreshListView)view;
//		} else if(control instanceof PullToRefreshScrollView) {
//			this.control = (PullToRefreshScrollView)view;
//		}
		this.control = (ViewGroup)view;
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
		if(control instanceof PullToRefreshListView) {
			((PullToRefreshListView)control).setRefreshViewListener(refreshListener);
		} else if(control instanceof PullToRefreshView) {
			((PullToRefreshView)control).setOnHeaderRefreshListener(headerListener);
			((PullToRefreshView)control).setOnFooterRefreshListener(footerListener);
		}
		this.pModel = model;
	}

	@Override
	public void deactivate() {
//		this.control.setOnHeaderRefreshListener(null);
//		this.control.setOnFooterRefreshListener(null);
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
	public void refresh() {
		
	}

}
