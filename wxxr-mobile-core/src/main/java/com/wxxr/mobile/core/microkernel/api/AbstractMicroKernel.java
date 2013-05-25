/*
 * @(#)AbstractMicroKernel.java	 Oct 22, 2010
 *
 * Copyright 2004-2010 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.mobile.core.microkernel.api;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import com.wxxr.mobile.core.log.api.Trace;


/**
 * @class desc A AbstractMicroKernel.
 * 
 * @author Neil
 * @version v1.0 
 * @created time Oct 22, 2010  10:06:58 AM
 */
public abstract class AbstractMicroKernel<C extends IKernelContext, M extends IKernelModule<C>> implements IMicroKernel<C,M>{
	private static Trace log = Trace.register(AbstractMicroKernel.class);
	
	private IKernelContext abstractContext = new IKernelContext() {

		@Override
		public Object getAttribute(String key) {
			return attributes.get(key);
		}

		@Override
		public <T> T getService(Class<T> interfaceClazz) {
			return getLocalService(interfaceClazz);
		}



		@Override
		public <T> ServiceFuture<T> getServiceAsync(Class<T> interfaceClazz) {
			return getServiceFuture(interfaceClazz);
		}

		@Override
		public <T> void registerService(Class<T> interfaceClazz, T handler) {
			if(log.isInfoEnabled()){
				log.info("Register service :%s, handler : %s",interfaceClazz.getCanonicalName(),handler);
			}
			registerLocalService(interfaceClazz, handler);
		}

		@Override
		public void addKernelServiceListener(IKernelServiceListener listener) {
			addLocalKernelServiceListener(listener);
		}


		@Override
		public boolean removeKernelServiceListener(IKernelServiceListener listener){
			return removeLocalKernelServiceListener(listener);
		}



		@Override
		public Object removeAttribute(String key) {
			return removeLocalAttribute(key);
		}

		@Override
		public void setAttribute(String key, Object value) {
			setLocalAttribute(key, value);
		}

		@Override
		public <T> void unregisterService(Class<T> interfaceClazz, T handler) {
			if(log.isInfoEnabled()){
				log.info("Unregister service :%s, handler : %s",interfaceClazz.getCanonicalName(),handler);
			}
			unregisterLocalService(interfaceClazz, handler);
		}

		@Override
		public <T> void checkServiceAvailable(Class<T> interfaceClazz,
				IServiceAvailableCallback<T> callback) {
			addServiceAvailableCallback(interfaceClazz, callback);
		}

		@Override
		public ExecutorService getExecutor() {
			return getExecutorService();
		}
		
		@SuppressWarnings("rawtypes")
		@Override
		public IMicroKernel getKernel() {
			return AbstractMicroKernel.this;
		}
	};
	
//	private Element moduleConfigure;

	private LinkedList<M> modules = new LinkedList<M>();

	private LinkedList<M> createdModules = new LinkedList<M>();

	private boolean started = false;

	private Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();

	private HashMap<Class<?>, ServiceFuture<?>> serviceHandlers = new HashMap<Class<?>, ServiceFuture<?>>();
	private HashMap<Class<?>, List<WeakReference<IServiceAvailableCallback<?>>>> callbacks = new HashMap<Class<?>, List<WeakReference<IServiceAvailableCallback<?>>>>();

	private LinkedList<IKernelServiceListener> serviceListeners = new LinkedList<IKernelServiceListener>();
	private LinkedList<IModuleListener> moduleListeners = new LinkedList<IModuleListener>();

	protected IKernelContext getAbstractContext() {
		return this.abstractContext;
	}

	@SuppressWarnings("unchecked")
	public void start() throws Exception{
		fireKernelStarting();
		initModules();
		for (Object mod : getAllModules()) {
			startModule(((M)mod));
		}
		this.started = true;
		fireKernelStarted();
	}


	@SuppressWarnings("unchecked")
	public void stop(){
		fireKernelStopping();
		destroyModules();
		for (Object mod : getAllModules()) {
			stopModule(((M)mod));
		}
		this.started = false;
		fireKernelStopped();
	}


	protected <T>  void fireServiceRegistered(Class<T> clazz, T handler){
		IKernelServiceListener[] list = null;
		synchronized(this.serviceListeners){
			list = this.serviceListeners.toArray(new IKernelServiceListener[this.serviceListeners.size()]);
		}
		if(list != null){
			for (IKernelServiceListener l : list) {
				if(l.accepts(clazz)){
					l.serviceRegistered(clazz, handler);
				}
			}
		}
		List<WeakReference<IServiceAvailableCallback<?>>> cbs = null;
		synchronized(callbacks){
			cbs = callbacks.remove(clazz);
		}
		if(cbs != null){
			for (WeakReference<IServiceAvailableCallback<?>> ref : cbs) {
				@SuppressWarnings("unchecked")
				IServiceAvailableCallback<T> cb = (IServiceAvailableCallback<T>)ref.get();
				if(cb != null){
					cb.serviceAvailable(handler);
				}
			}
		}
	}

	protected <T>  void fireServiceUnregistered(Class<T> clazz, T handler){
		IKernelServiceListener[] list = null;
		synchronized(this.serviceListeners){
			list = this.serviceListeners.toArray(new IKernelServiceListener[this.serviceListeners.size()]);
		}
		if(list != null){
			for (IKernelServiceListener l : list) {
				if(l.accepts(clazz)){
					l.serviceUnregistered(clazz, handler);
				}
			}
		}

	}
	
	protected void fireModuleRegistered(M module){
		IModuleListener[] list = null;
		synchronized(this.moduleListeners){
			list = this.moduleListeners.toArray(new IModuleListener[this.moduleListeners.size()]);
		}
		if(list != null){
			for (IModuleListener l : list) {
					l.moduleRegistered(module);
			}
		}
	}

	
	protected void fireModuleUnregistered(M module){
		IModuleListener[] list = null;
		synchronized(this.moduleListeners){
			list = this.moduleListeners.toArray(new IModuleListener[this.moduleListeners.size()]);
		}
		if(list != null){
			for (IModuleListener l : list) {
					l.moduleUnregistered(module);
			}
		}
	}

	protected void fireKernelStarting(){
		IModuleListener[] list = null;
		synchronized(this.moduleListeners){
			list = this.moduleListeners.toArray(new IModuleListener[this.moduleListeners.size()]);
		}
		if(list != null){
			for (IModuleListener l : list) {
					l.kernelStarting();
			}
		}
	}

	protected void fireKernelStarted(){
		IModuleListener[] list = null;
		synchronized(this.moduleListeners){
			list = this.moduleListeners.toArray(new IModuleListener[this.moduleListeners.size()]);
		}
		if(list != null){
			for (IModuleListener l : list) {
					l.kernelStarted();
			}
		}
	}

	protected void fireKernelStopping(){
		IModuleListener[] list = null;
		synchronized(this.moduleListeners){
			list = this.moduleListeners.toArray(new IModuleListener[this.moduleListeners.size()]);
		}
		if(list != null){
			for (IModuleListener l : list) {
					l.kernelStopping();
			}
		}
	}

	
	protected void fireKernelStopped(){
		IModuleListener[] list = null;
		synchronized(this.moduleListeners){
			list = this.moduleListeners.toArray(new IModuleListener[this.moduleListeners.size()]);
		}
		if(list != null){
			for (IModuleListener l : list) {
					l.kernelStopped();
			}
		}
	}

	protected <T> void addServiceAvailableCallback(Class<T> clazz, IServiceAvailableCallback<T> cb){
		if((clazz == null)||(cb == null)){
			throw new IllegalArgumentException("Service class and callback cannot be NULL !");
		}
		T service = getLocalService(clazz);
		if(service != null){
			cb.serviceAvailable(service);
		}else{
			synchronized(this.callbacks){
				List<WeakReference<IServiceAvailableCallback<?>>> list = callbacks.get(clazz);
				if(list == null){
					list = new ArrayList<WeakReference<IServiceAvailableCallback<?>>>();
					callbacks.put(clazz, list);
				}
				list.add(new WeakReference<IServiceAvailableCallback<?>>(cb));
			}
		}
	}

	protected <T> T getLocalService(Class<T> interfaceClazz) {
		ServiceFuture<T> handlers = getServiceFuture(interfaceClazz);
		return handlers.getService();
	}

	protected <T> void registerLocalService(Class<T> interfaceClazz, T handler) {
		if(getServiceFuture(interfaceClazz).addService(handler)){
			fireServiceRegistered(interfaceClazz, handler);
		}
	}

	@SuppressWarnings("unchecked")
	protected <T> ServiceFuture<T> getServiceFuture(Class<T> interfaceClazz) {
		ServiceFuture<T> handlers = null;
		synchronized(serviceHandlers){
			handlers = (ServiceFuture<T>)serviceHandlers.get(interfaceClazz);
			if(handlers == null){
				handlers = new ServiceFuture<T>();
				serviceHandlers.put(interfaceClazz, handlers);
			}
		}
		return handlers;
	}

	protected <T> void unregisterLocalService(Class<T> interfaceClazz, T handler) {
		if(getServiceFuture(interfaceClazz).removeService(handler)){
			fireServiceUnregistered(interfaceClazz, handler);
		}
	}
	
	protected void setLocalAttribute(String key, Object value) {
		attributes.put(key, value);
	}

	protected Object removeLocalAttribute(String key) {
		return attributes.remove(key);
	}

	
	protected void addLocalKernelServiceListener(IKernelServiceListener listener) {
		synchronized(serviceListeners){
			if(!serviceListeners.contains(listener)){
				serviceListeners.add(listener);
			}
		}
	}


	protected boolean removeLocalKernelServiceListener(IKernelServiceListener listener){
		synchronized(serviceListeners){
			return serviceListeners.remove(listener);
		}
	}

//	public void setModuleConfigure(Element serviceConfigure) {
//		if(null != serviceConfigure){
//			this.moduleConfigure = serviceConfigure;
//		}
//	}

	public void registerKernelModule(M module) {
		boolean added = false;
		synchronized(modules){
			if(!this.modules.contains(module)){
				this.modules.add(module);
				added = true;
			}
		}
		if(added){
			if(this.started){
				startModule(module);
			}
			fireModuleRegistered(module);
		}
	}

	/**
	 * @param module
	 */
	protected void startModule(M module) {
		if(log.isInfoEnabled()){
			log.info("Starting module :["+module+"] ...");
		}
		module.start(getContext());
		if(log.isInfoEnabled()){
			log.info("Module :["+module+"] started !");
		}
	}

	public void unregisterKernelModule(M module) {
		boolean removed = false;
		synchronized(modules){
			removed = this.modules.remove(module);
		}
		if(removed){
			if(this.started){
				stopModule(module);
			}   
			fireModuleUnregistered(module);
		}
	}

	/**
	 * @param module
	 */
	protected void stopModule(M module) {
		if(log.isInfoEnabled()){
			log.info("Stopping module :["+module+"] ...");
		}
		module.stop();
		if(log.isInfoEnabled()){
			log.info("Module :["+module+"] stopped !");
		}
	}


	abstract protected C getContext();


	protected void destroyModules() {
		for (M mod : this.createdModules) {
			try {
				unregisterKernelModule(mod);
			}catch(Exception e){
				log.warn("Failed to unregister module :"+mod, e);
			}
		}
		this.createdModules.clear();
	}

//	protected void initModules() throws Exception {
//		if(moduleConfigure == null){
//			return;
//		}
//		ClassLoader cl = Thread.currentThread().getContextClassLoader();
//		NodeList nodes = moduleConfigure.getElementsByTagName("module");
//		int len = nodes.getLength();
//		for (int i = 0; i < len; i++) {
//			Node node = nodes.item(i);
//			if(node instanceof Element){
//				Element elem = (Element)node;
//				String clsName = elem.getAttribute("class");
//				clsName = clsName != null ? clsName.trim() : null;
//				clsName = clsName != null && clsName.length() > 0 ? clsName : null;
//				if(clsName != null){
//					@SuppressWarnings("unchecked")
//					Class<M> clazz = (Class<M>)cl.loadClass(clsName);
//					M mod = clazz.newInstance();
//					Method initMethod = null;
//					try {
//						initMethod = clazz.getMethod("init", new Class[]{Element.class});
//					}catch(Exception e){
//						log.warn("Cannot find init(Element) method to init module :"+mod, e);
//					}
//					if(initMethod != null){
//						initMethod.invoke(mod, new Object[]{elem});
//					}
//					registerKernelModule(mod);
//					this.createdModules.add(mod);
//				}
//			}
//		}
//	}

	protected abstract void initModules();
	
	public Object[] getAllModules() {
		synchronized(modules){
			if(modules.isEmpty()){
				return new IKernelModule[0];
			}else{
				return modules.toArray(new IKernelModule[modules.size()]);
			}
		}
	}
	
	protected abstract ExecutorService getExecutorService();

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.IMicroKernel#getAllRegisteredModules()
	 */
	@Override
	public ModuleStatus[] getAllRegisteredModules() {
		ModuleStatus[] mods = null;
		synchronized(modules){
			int len = modules.size();
			if(len > 0){
				mods = new ModuleStatus[len];
				for(int i=0 ; i < len; i++){
					mods[i] = modules.get(i).getStatus();
				}
			}
		}
		return mods;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.IMicroKernel#getService(java.lang.Class)
	 */
	@Override
	public <S> S getService(Class<S> interfaceClazz) {
		return getAbstractContext().getService(interfaceClazz);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.IMicroKernel#getServiceAsync(java.lang.Class)
	 */
	@Override
	public <S> ServiceFuture<S> getServiceAsync(Class<S> interfaceClazz) {
		return getAbstractContext().getServiceAsync(interfaceClazz);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.IMicroKernel#checkServiceAvailable(java.lang.Class, com.wxxr.mobile.core.microkernel.api.IServiceAvailableCallback)
	 */
	@Override
	public <S> void checkServiceAvailable(Class<S> interfaceClazz,
			IServiceAvailableCallback<S> callback) {
		getAbstractContext().checkServiceAvailable(interfaceClazz, callback);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.IMicroKernel#addModuleListener(com.wxxr.mobile.core.microkernel.api.IModuleListener)
	 */
	@Override
	public void addModuleListener(IModuleListener listener) {
		synchronized(moduleListeners){
			if(!moduleListeners.contains(listener)){
				moduleListeners.add(listener);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.IMicroKernel#removeModuleListener(com.wxxr.mobile.core.microkernel.api.IModuleListener)
	 */
	@Override
	public boolean removeModuleListener(IModuleListener listener) {
		synchronized(moduleListeners){
			return moduleListeners.remove(listener);
		}
	}
}
