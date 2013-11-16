/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.core.ui.api.ISelection;
import com.wxxr.mobile.core.ui.api.ISelectionChangedListener;
import com.wxxr.mobile.core.ui.api.ISelectionProvider;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author neillin
 *
 */
public class SelectionProviderSupport implements ISelectionProvider {
	private final String providerId;
	private ISelection selection;
	private List<ISelectionChangedListener> listeners;
	
	public SelectionProviderSupport(String pid){
		if(StringUtils.isBlank(pid)){
			throw new IllegalArgumentException("Invalid provider id : NULL");
		}
		this.providerId = pid;
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ISelectionProvider#getSelection()
	 */
	@Override
	public ISelection getSelection() {
		return this.selection;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ISelectionProvider#addSelectionListener(com.wxxr.mobile.core.ui.api.ISelectionChangedListener)
	 */
	@Override
	public synchronized void addSelectionListener(ISelectionChangedListener listener) {
		if(this.listeners == null){
			this.listeners = new ArrayList<ISelectionChangedListener>();
		}
		if(!this.listeners.contains(listener)){
			this.listeners.add(listener);
		}

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ISelectionProvider#removeSelectionListener(com.wxxr.mobile.core.ui.api.ISelectionChangedListener)
	 */
	@Override
	public synchronized boolean removeSelectionListener(ISelectionChangedListener listener) {
		if(this.listeners != null){
			return this.listeners.remove(listener);
		}
		return false;
	}
	
	protected synchronized ISelectionChangedListener[] getAllListeners() {
		return this.listeners != null ? this.listeners.toArray(new ISelectionChangedListener[0]) : null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ISelectionProvider#getProviderId()
	 */
	@Override
	public String getProviderId() {
		return this.providerId;
	}
	
	protected void fireSelectionChangedEvent(){
		ISelectionChangedListener[] list = getAllListeners();
		if((list != null)&&(list.length > 0)){
			for (ISelectionChangedListener l : list) {
				l.selectionChanged(getProviderId(),getSelection());
			}
		}
	}

	public synchronized void setSelection(ISelection object){
		if(!ModelUtils.isEquals(this.selection, object)){
			this.selection = object;
			fireSelectionChangedEvent();
		}
	}
	
	public void clearSelection() {
		this.selection = null;
		fireSelectionChangedEvent();
	}
	
	public void clear() {
		this.selection = null;
		if(this.listeners != null){
			this.listeners.clear();
			this.listeners = null;
		}
	}
}
