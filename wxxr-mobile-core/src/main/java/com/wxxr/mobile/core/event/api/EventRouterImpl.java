/*
 * @(#)SimpleEventRouter.java	 May 18, 2011
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.mobile.core.event.api;

import java.util.LinkedList;
import java.util.concurrent.Callable;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.microkernel.api.IKernelModule;

/**
 * @class desc A SimpleEventRouter.
 * 
 * @author Neil
 * @version v1.0 
 * @created time May 18, 2011  3:49:19 PM
 */
public class EventRouterImpl<T extends IKernelContext, M extends IKernelModule<T>> extends AbstractModule<T> implements IEventRouter {

	private static final Trace log = Trace.register(EventRouterImpl.class);
	
	protected LinkedList<ListenerHolder> listeners = new LinkedList<ListenerHolder>();

	private static class ListenerHolder {
		IEventSelector selector;
		IEventListener listener;
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

	private class EventDispatcher implements Callable<Object> {

		private final IEventObject event;
		private final ListenerHolder[] allParties;

		public EventDispatcher(IEventObject evt, ListenerHolder[] parties){
			this.event = evt;
			this.allParties = parties;
		}

		public Object call() {
			for (ListenerHolder h : allParties) {
				if(h != null){
					if((h.selector == null)||h.selector.isEventApply(event)){
						handleEvent(h.listener,event);
					}
				}
			}
			return null;
		}

	}

	private synchronized ListenerHolder[] getAllListeners() {
		return this.listeners.toArray(new ListenerHolder[this.listeners.size()]);
	}

	private synchronized ListenerHolder findListener(Class<? extends IEventObject> eventType, IEventListener listener) {
		for (ListenerHolder l : listeners) {
			if(l == null){
				continue;
			}
			if(l.listener != listener){
				continue;
			}
			if(!(l.selector instanceof EventTypeSelector)){
				continue;
			}
			if((eventType == null)&&(((EventTypeSelector)l.selector).type != null)){
				continue;
			}
			if((eventType != null)&&(eventType.equals(((EventTypeSelector)l.selector).type) != true)){
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
			if(l.listener != listener){
				continue;
			}
			if(((selector == null)&&(l.selector== null))||(selector == l.selector)){
				return l;
			}
		}
		return null;
	}


	public synchronized void registerEventListener(Class<? extends IEventObject> eventType, IEventListener listener) {
		ListenerHolder l = findListener(eventType, listener);
		if(l == null){
			l = new ListenerHolder();
			l.selector = new EventTypeSelector(eventType);
			l.listener = listener;
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
			l.selector = selector;
			l.listener = listener;
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
	
	protected void handleEvent(IEventListener listener,IEventObject event) {
		listener.onEvent(event);
	}



	public Object routeEvent(IEventObject event) {
		EventDispatcher dispatcher = new EventDispatcher(event, getAllListeners());
		if(log.isDebugEnabled()){
			log.debug("Going to route event :"+event);
		}
		if(event.needSyncProcessed()){
			return dispatcher.call();
		}else{
			return this.context.getExecutor().submit(dispatcher);
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

}
