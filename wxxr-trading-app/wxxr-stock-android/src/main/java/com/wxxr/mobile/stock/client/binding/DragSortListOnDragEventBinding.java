package com.wxxr.mobile.stock.client.binding;

import android.view.View;

import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.common.AbstractEventBinding;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;
import com.wxxr.mobile.stock.client.widget.NewDragSortListView;
import com.wxxr.mobile.stock.client.widget.StockInfoBean;
import com.wxxr.moblie.stock.client.dslv.DragSortListView;

public class DragSortListOnDragEventBinding extends AbstractEventBinding{

	private DragSortListView control;
	
	public DragSortListOnDragEventBinding(View view,String cmdName,String field){
		this.control = (NewDragSortListView)view;
		setUIControl(this.control);
		setCommandName(cmdName);
		setFieldName(field);
	}
	
	//拖动事件
	private DragSortListView.DropListener drop = new DragSortListView.DropListener() {
		
		@Override
		public void drop(int from, int to) {
			IUIComponent field = getField();
			if (field != null) {
				((NewDragSortListView)control).MoveAdapterData(from, to);
				SimpleInputEvent event = new SimpleInputEvent("DropItemEvent",field);
				event.addProperty("from", from);
				event.addProperty("to", to);
				handleInputEvent(event);
			} else {
				SimpleInputEvent event = new SimpleInputEvent("DropItemEvent",getModel());
				handleInputEvent(event);
			}			
		}
	};
	//删除事件
	private NewDragSortListView.RemoveListener  remove = new NewDragSortListView.RemoveListener() {
		@Override
		public void remove(int which) {
			NewDragSortListView listView = ((NewDragSortListView)control);
			IUIComponent field = getField();
			if (field != null) {
				StockInfoBean data = listView.getRemoveData(which);
				listView.RemoveData(which);
				SimpleInputEvent event = new SimpleInputEvent("RemoveItemEvent",field);
				if(data!=null){
					event.addProperty("code", data.getCode());
					event.addProperty("market", data.getMarket());
				}
				event.addProperty("which", which);
				handleInputEvent(event);
			} else {
				SimpleInputEvent event = new SimpleInputEvent("RemoveItemEvent",getModel());
				handleInputEvent(event);
			}	
		}
	};

	private NewDragSortListView.MoveTopListener moveTop = new NewDragSortListView.MoveTopListener(){
		@Override
		public void moveTop(int top) {
			IUIComponent field = getField();
			if (field != null) {
				((NewDragSortListView)control).MoveAdapterData(top, 0);
				SimpleInputEvent event = new SimpleInputEvent("MoveTopItemEvent",field);
				event.addProperty("from", top);
				event.addProperty("to", 0);
				handleInputEvent(event);
			} else {
				SimpleInputEvent event = new SimpleInputEvent("MoveTopItemEvent",getModel());
				handleInputEvent(event);
			}			
		}
	};
	
	@Override
	public void activate(IView model) {
		super.activate(model);
		if(control instanceof DragSortListView) {
			((NewDragSortListView)control).setDropListener(drop);
			((NewDragSortListView)control).setRemoveListener(remove);
			((NewDragSortListView)control).setMoveTopListener(moveTop);
		} 	
		
	}

	@Override
	public void deactivate() {
		super.deactivate();
	}


	@Override
	public void destroy() {
		deactivate();
		this.control = null;
	}
}
