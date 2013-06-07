/*
 * @(#)SimpleEventRouter.java	 May 18, 2011
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.mobile.core.event.api;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;

/**
 * @class desc A SimpleEventRouter.
 * 
 * @author Neil
 * @version v1.0 
 * @created time May 18, 2011  3:49:19 PM
 */
public class EventRouterImpl<T extends IKernelContext> extends AbstractModule<T> implements IEventRouter {

	private static final Trace log = Trace.register(EventRouterImpl.class);
	
	protected LinkedList<ListenerHolder> listeners = new LinkedList<ListenerHolder>();
	protected LinkedList<WeakReference<IStreamEventListener>> streamListeners = new LinkedList<WeakReference<IStreamEventListener>>();

	private static class ListenerHolder {
		WeakReference<IEventSelector> selector;
		WeakReference<IEventListener> listener;
		EventTypeSelector ref;
	}

	private static class EventTypeSelector implements IEventSelector {

		private Class<? extends IEventObject> type;

		public EventTypeSelector(Class<? extends IEventObject> eventType){
			this.type = eventType;
		}

		public boolean isEventApply(IEventObject event) {
			if(this.type == null){
				return true;
			}
			return this.type.isAssignableFrom(event.getClass());
		}     
	}

	private class EventBroadcaster implements Callable<Object> {

		private final IBroadcastEvent event;
		private final ListenerHolder[] allParties;

		public EventBroadcaster(IBroadcastEvent evt, ListenerHolder[] parties){
			this.event = evt;
			this.allParties = parties;
		}

		public Object call() {
			for (ListenerHolder h : allParties) {
				if(h != null){
					if((h.listener.get() == null)||((h.selector != null)&&(h.selector.get() == null))){
						continue;
					}
					
					if((h.selector == null)||h.selector.get().isEventApply(event)){
						handleEvent(h.listener.get(),event);
					}
				}
			}
			return null;
		}

	}
	
	private class EventPipeLine implements Callable<Object>,IListenerChain {

		private final IStreamEvent event;
		private final WeakReference<IStreamEventListener>[] allParties;
		private Map<String, Object> attrs;
		private int idx = 0;

		public EventPipeLine(IStreamEvent evt, WeakReference<IStreamEventListener>[] parties){
			this.event = evt;
			this.allParties = parties;
		}

		public Object call() {
			invokeNext(event);
			return null;
		}

		@Override
		public void invokeNext(IStreamEvent event) {
			IStreamEventListener l = null;
			for(;idx < allParties.length;idx++){
				WeakReference<IStreamEventListener> ref = allParties[idx];
				if((ref != null)&&(ref.get() != null)){
					l = ref.get();
					break;
				}
			}
			if(l != null){
				idx++;
				l.onEvent(event, this);
			}
			return;
			
		}

		@Override
		public void setAttribute(String name, Object val) {
			if(this.attrs == null){
				this.attrs = new HashMap<String, Object>();
			}
			this.attrs.put(name, val);
		}

		@Override
		public Object getAttribute(String name) {
			return this.attrs != null ? this.attrs.get(name) : null;
		}

		@Override
		public List<String> getAttributeNames() {
			return this.attrs != null ? new ArrayList<String>(this.attrs.keySet()) : null;
		}

	}


	private synchronized ListenerHolder[] getAllListeners() {
		return this.listeners.toArray(new ListenerHolder[this.listeners.size()]);
	}
	
	private synchronized WeakReference<IStreamEventListener>[] getAllStreamListeners() {
		return this.streamListeners.toArray(new WeakReference[this.streamListeners.size()]);
	}


	private synchronized ListenerHolder findListener(Class<? extends IEventObject> eventType, IEventListener listener) {
		for (ListenerHolder l : listeners) {
			if(l == null){
				continue;
			}
			if(l.listener.get() != listener){
				continue;
			}
			if(!(l.selector.get() instanceof EventTypeSelector)){
				continue;
			}
			if((eventType == null)&&(((EventTypeSelector)l.selector.get()).type != null)){
				continue;
			}
			if((eventType != null)&&(eventType.equals(((EventTypeSelector)l.selector.get()).type) != true)){
				continue;
			}
			return l;
		}
		return null;
	}

	private synchronized ListenerHolder findListener(IEventSelector selector, IEventListener listener) {
		for (ListenerHolder l : listeners) {
			if(l == null){
				continue;
			}
			if(l.listener.get() != listener){
				continue;
			}
			if(((selector == null)&&(l.selector== null))||(selector == l.selector.get())){
				return l;
			}
		}
		return null;
	}


	public synchronized void registerEventListener(Class<? extends IEventObject> eventType, IEventListener listener) {
		ListenerHolder l = findListener(eventType, listener);
		if(l == null){
			l = new ListenerHolder();
			if(eventType != null){
				l.ref = new EventTypeSelector(eventType);
				l.selector = new WeakReference<IEventSelector>(l.ref);
			}
			l.listener = new WeakReference<IEventListener>(listener);
			listeners.add(l);
		}
	}

	public synchronized boolean unregisterEventListener(Class<? extends IEventObject> eventType, IEventListener listener) {
		ListenerHolder l = findListener(eventType, listener);
		if(l != null){
			listeners.remove(l);
			return true;
		}
		return false;

	}

	public synchronized void registerEventListener(IEventSelector selector,
			IEventListener listener) {
		ListenerHolder l = findListener(selector, listener);
		if(l == null){
			l = new ListenerHolder();
			if(selector != null){
				l.selector = new WeakReference<IEventSelector>(selector);
			}
			l.listener = new WeakReference<IEventListener>(listener);
			listeners.add(l);
		}

	}

	public synchronized boolean unregisterEventListener(IEventSelector selector,
			IEventListener listener) {
		ListenerHolder l = findListener(selector, listener);
		if(l != null){
			listeners.remove(l);
			return true;
		}
		return false;

	}
	
	protected void handleEvent(IEventListener listener,IBroadcastEvent event) {
		listener.onEvent(event);
	}



	public Object routeEvent(IEventObject event) {
	
		Callable<Object> call = null;
		if(event instanceof IBroadcastEvent){
			call = new EventBroadcaster((IBroadcastEvent)event, getAllListeners());
		}else{
			call = new EventPipeLine((IStreamEvent)event, getAllStreamListeners());
		}
		if(log.isDebugEnabled()){
			log.debug("Going to route event :"+event);
		}
		if(event.needSyncProcessed()){
			try {
				return call.call();
			} catch (Exception e) {
				return null;
			}
		}else{
			return this.context.getExecutor().submit(call);
		}
	}

	@Override
	protected void initServiceDependency() {
	}

	@Override
	protected void startService() {
		context.registerService(IEventRouter.class, this);
		
	}

	@Override
	protected void stopService() {
		context.unregisterService(IEventRouter.class, this);
	}
	
	private WeakReference<IStreamEventListener> findListener(IStreamEventListener listener){
		for (WeakReference<IStreamEventListener> l : this.streamListeners) {
			if(l.get() == listener){
				return l;
			}
		}
		return null;
	}


	private boolean hasListener(IStreamEventListener listener){
		return findListener(listener) != null;
	}
	@Override
	public synchronized void addListenerFirst(IStreamEventListener listener) {
		if(!hasListener(listener)){
			this.streamListeners.addFirst(new WeakReference<IStreamEventListener>(listener));
		}
	}

	@Override
	public synchronized void addListenerLast(IStreamEventListener listener) {
		if(!hasListener(listener)){
			this.streamListeners.addLast(new WeakReference<IStreamEventListener>(listener));
		}
	}

	@Override
	public synchronized boolean removeListener(IStreamEventListener listener) {
		WeakReference<IStreamEventListener> l = findListener(listener);
		return l != null ? this.streamListeners.remove(l) : false;
	}

}
