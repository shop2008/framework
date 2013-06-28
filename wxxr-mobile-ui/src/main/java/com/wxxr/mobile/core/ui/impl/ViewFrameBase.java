/**
 * 
 */
package com.wxxr.mobile.core.ui.impl;

import java.util.LinkedList;
import java.util.List;

import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewFrame;

/**
 * 在一个view frame中可能有多个视图（view），但只能有一个视图是激活的，没有视图的view frame必须是隐藏的
 * @author neillin
 *
 */
public class ViewFrameBase extends AbstractUIContainer implements IViewFrame {

	private LinkedList<IView> activeViews;
	
	public List<IView> getViews() {
		return getChildren(IView.class);
	}

	public IView getActiveView() {
		makeSureViewInitialized();
		for (IView v : this.activeViews) {
			if(v.isActive()){
				return v;
			}
		}
		return null;
	}

	public IView getView(String name) {
		return name != null ? getChild(name, IView.class) : null;
	}
	
	protected synchronized void makeSureViewInitialized() {
		if(this.activeViews == null){
			activeViews = new LinkedList<IView>();
			for (IView v : getChildren(IView.class)) {
				activeViews.add(v);
			}
		}
	}

	/**
	 * 传进的参数name可能为空，如果是空并且当前还没有活跃的视图，则激活最前的视图
	 */
	public void activateView(String name) {
		makeSureViewInitialized();
		IView active = getActiveView();
		IView view = getView(name);
		if((view != null)&&(view.isActive() == false)){
			if(active != null){
				active.deactivate();
			}
			view.activate();
			active = view;
		}
		if((active == null)&&(name == null)){
			active = this.activeViews.get(0);
			active.activate();
		}
	}

	/**
	 * 传进的参数name可能为空，如果是空，则隐藏当前活跃的视图，显示下一个视图。
	 * 如果当前只有一个视图，则仍保存当前视图的激活状态
	 */
	public void deactivateView(String name) {
		makeSureViewInitialized();
		IView view = getView(name);
		if((view != null)&&view.isActive()){
			if(this.activeViews.size() == 1){	//该视图是唯一的视图，不能隐藏
				return;
			}
			view.deactivate();
			view = getNextView(view);
			view.activate();
		}
		
	}

	/**
	 * @param view
	 * @return
	 */
	protected IView getNextView(IView view) {
		int idx = this.activeViews.indexOf(view);
		idx++;
		if(idx >= this.activeViews.size()){
			idx = 0;
		}
		view = this.activeViews.get(idx);
		return view;
	}

	public void setVisible(boolean bool) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.impl.AbstractUIContainer#destroy()
	 */
	@Override
	public void destroy() {
		if(this.activeViews != null){
			this.activeViews.clear();
			this.activeViews = null;
		}
		super.destroy();
	}

	public void addView(IView view) {
		super.add((ViewBase)view);
	}

	public boolean removeView(IView view) {
		return super.remove((ViewBase)view);
	}
}
