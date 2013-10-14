package com.wxxr.mobile.android.ui;

import java.util.LinkedList;


public class AbstractEventListenerDispatcher<L> {
	private LinkedList<L> listeners;

	public void addListener(L listener){
		if(this.listeners == null){
			this.listeners = new LinkedList<L>();
		}
		if(!this.listeners.contains(listener)){
			this.listeners.add(listener);
		}
	}
	
	public boolean removeListener(L listener){
		if(this.listeners != null){
			return this.listeners.remove(listener);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	protected L[] getListeners(L[] a) {
		return listeners != null ? listeners.toArray(a) : (L[])new Object[0];
	}
}
