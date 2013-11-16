/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.ui.api.ISelection;
import com.wxxr.mobile.core.ui.api.ISelectionChangedListener;
import com.wxxr.mobile.core.ui.api.ISelectionProvider;
import com.wxxr.mobile.core.ui.api.ISelectionService;

/**
 * @author neillin
 *
 */
public class SelectionServiceSupport implements ISelectionService, ISelectionChangedListener {

	private Map<String, List<ISelectionChangedListener>> listeners;
	private Stack<ISelectionProvider> stack = new Stack<ISelectionProvider>();
	private Map<String, ISelection> map = new HashMap<String, ISelection>();
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ISelectionService#addSelectionListener(com.wxxr.mobile.core.ui.api.ISelectionChangedListener)
	 */
	@Override
	public synchronized void addSelectionListener(ISelectionChangedListener listener) {
		addSelectionListener("*",listener);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ISelectionService#addSelectionListener(java.lang.String, com.wxxr.mobile.core.ui.api.ISelectionChangedListener)
	 */
	@Override
	public synchronized void addSelectionListener(String providerId,ISelectionChangedListener listener) {
		if(this.listeners == null){
			this.listeners = new HashMap<String, List<ISelectionChangedListener>>();
		}
		List<ISelectionChangedListener> list = this.listeners.get(providerId);
		if(list == null){
			list = new ArrayList<ISelectionChangedListener>();
			this.listeners.put(providerId, list);
		}
		if(!list.contains(listener)){
			list.add(listener);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ISelectionService#removeSelectionListener(com.wxxr.mobile.core.ui.api.ISelectionChangedListener)
	 */
	@Override
	public boolean removeSelectionListener(ISelectionChangedListener listener) {
		return removeSelectionListener("*", listener);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ISelectionService#removeSelectionListener(java.lang.String, com.wxxr.mobile.core.ui.api.ISelectionChangedListener)
	 */
	@Override
	public  synchronized boolean removeSelectionListener(String providerId,ISelectionChangedListener listener) {
		if(this.listeners == null){
			return false;
		}
		boolean val = false;
		if(this.listeners != null){
			if("*".equals(providerId)){
				for (List<ISelectionChangedListener> list : this.listeners.values()) {
					val = val || list.remove(listener);
				}
			}
		}else{
			List<ISelectionChangedListener> list = this.listeners.get(providerId);
			if(list != null){
				val = list.remove(listener);
			}
		}
		return val;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ISelectionService#getSelection()
	 */
	@Override
	public ISelection getSelection() {
		String provId = getCurrentProviderId();
		return provId == null ? null : this.map.get(provId);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ISelectionService#getSelection(java.lang.String)
	 */
	@Override
	public ISelection getSelection(String providerId) {
		return this.map.get(providerId);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ISelectionService#registerProvider(com.wxxr.mobile.core.ui.api.ISelectionProvider)
	 */
	@Override
	public void registerProvider(ISelectionProvider provider) {
		if(this.stack.contains(provider)){
			this.stack.remove(provider);
		}
		this.stack.push(provider);
		provider.addSelectionListener(this);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ISelectionService#unregisterProvider(com.wxxr.mobile.core.ui.api.ISelectionProvider)
	 */
	@Override
	public boolean unregisterProvider(ISelectionProvider provider) {
		this.map.remove(provider.getProviderId());
		provider.removeSelectionListener(this);
		return this.stack.remove(provider);
	}
	
	protected synchronized ISelectionChangedListener[] getAllListeners(String providerId){
		ArrayList<ISelectionChangedListener> result = null;
		List<ISelectionChangedListener> list = this.listeners != null ? this.listeners.get(providerId) : null;
		if((list != null)&&(list.size() > 0)){
			result = new ArrayList<ISelectionChangedListener>();
			result.addAll(list);
		}
		if("*".equals(providerId) == false){
			list = this.listeners != null ? this.listeners.get("*") : null;
			if((list != null)&&(list.size() > 0)){
				if(result == null){
					result = new ArrayList<ISelectionChangedListener>();
				}
				for (ISelectionChangedListener l : list) {
					if(!result.contains(l)){
						result.add(l);
					}
				}
			}
		}
		return result != null ? result.toArray(new ISelectionChangedListener[0]) : null;
	}

	@Override
	public void selectionChanged(final String providerId, final ISelection selection) {
		this.map.put(providerId, selection);
		final ISelectionChangedListener[] list = getAllListeners(providerId);
		if((list != null)&&(list.length > 0)){
			KUtils.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					for (ISelectionChangedListener iSelectionChangedListener : list) {
						iSelectionChangedListener.selectionChanged(providerId, selection);
					}
				}
			}, 0, null);
		}
		
	}

	@Override
	public String getCurrentProviderId() {
		return this.stack.isEmpty() ? null : this.stack.peek().getProviderId();
	}

}
