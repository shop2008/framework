/**
 * 
 */
package com.wxxr.mobile.core.ui.common;


import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewGroup;


/**
 * 在一个view group中可能有多个视图（view），但只能有一个视图是激活的，没有视图的view group必须是隐藏的
 * @author neillin
 *
 */
public class ViewGroupBase extends UIContainer<IView> implements IViewGroup {

	private Stack<String> viewStack = new Stack<String>();

	public String[] getViewIds() {
		List<String> ids = null;
		List<IView> pages = getChildren(IView.class);
		for (IView v : pages) {
			if(ids == null){
				ids = new LinkedList<String>();
			}
			ids.add(v.getName());
		}
		return ids != null ? ids.toArray(new String[ids.size()]) : null;
	}

	public String getActiveViewId() {
		return this.viewStack.isEmpty() ?  null : this.viewStack.peek();
	}

	public void activateView(String name) {
		IView view = getView(name);
		if(view != null){
			if(this.viewStack.isEmpty()){
				setAttribute(AttributeKeys.visible, true);
			}
			view.show();
			this.viewStack.remove(name);
			this.viewStack.push(name);
		}
	}

	public void deactivateView(String name) {
		IView view = getView(name);
		if(view != null){
			view.hide();
		}
		if(name.equals(getActiveViewId())){
			this.viewStack.pop();
			String nextViewId = getActiveViewId();
			view = nextViewId != null ? getView(nextViewId) : null;
			if(view != null){
				view.show();
			}
		}else{
			this.viewStack.remove(name);
		}
		if(this.viewStack.isEmpty()){
			setAttribute(AttributeKeys.visible, false);
		}
	}

	public IView getView(String name) {
		return (IView)getChild(name);
	}

	public boolean hasView(String name) {
		return getView(name) != null;
	}

}
