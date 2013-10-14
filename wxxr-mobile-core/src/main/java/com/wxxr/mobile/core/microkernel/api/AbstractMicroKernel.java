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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.core.api.IProgressMonitor;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.util.ICancellable;


/**
 * @class desc A AbstractMicroKernel.
 * 
 * @author Neil
 * @version v1.0 
 * @created time Oct 22, 2010  10:06:58 AM
 */
public abstract class AbstractMicroKernel<C extends IKernelContext, M extends IKernelModule<C>> implements IMicroKernel<C,M>{
	private static Trace log = Trace.register(AbstractMicroKernel.class);
	protected ThreadLocal<IProgressMonitor> monitorHolder = new ThreadLocal<IProgressMonitor>();
	
	protected class AbstractContext implements IKernelContext {

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
			if(!interfaceClazz.isInterface()){
				throw new IllegalArgumentException("pass in interface class is not a interface :"+interfaceClazz.getCanonicalName());
			}
			List<Class<?>> exportingServices = getExportingInterfaces(interfaceClazz);
			if(exportingServices.isEmpty()){
				exportingServices.add(interfaceClazz);
			}
			for (Class<?> clazz : exportingServices) {
				registerLocalService(clazz, handler);
			}
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
			List<Class<?>> exportingServices = getExportingInterfaces(interfaceClazz);
			if(exportingServices.isEmpty()){
				exportingServices.add(interfaceClazz);
			}
			for (Class<?> clazz : exportingServices) {
				unregisterLocalService(clazz, handler);
			}
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

		@Override
		public ICancellable invokeLater(final Runnable task, long delay, TimeUnit unit) {
			final TimerTask t = new TimerTask() {
				
				@Override
				public void run() {
					try {
						getExecutor().execute(task);
					}catch(Throwable t){
						log.warn("Caught throwable when execute scheduled task, task discarded :"+task, t);
					}
				}
			};
			timer.schedule(t, TimeUnit.MILLISECONDS.convert(delay, unit));
			return new ICancellable() {
				private boolean cancelled = false;
				@Override
				public boolean isCancelled() {
					return cancelled;
				}
				
				@Override
				public void cancel() {
					cancelled = t.cancel();
				}
			};
		}
	};
	
//	private Element moduleConfigure;

	private LinkedList<M> modules = new LinkedList<M>();

//	private LinkedList<M> createdModules = new LinkedList<M>();

	private boolean started = false;

	private Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();

	private HashMap<Class<?>, ServiceFuture<?>> serviceHandlers = new HashMap<Class<?>, ServiceFuture<?>>();
	private HashMap<Class<?>, List<WeakReference<IServiceAvailableCallback<?>>>> callbacks = new HashMap<Class<?>, List<WeakReference<IServiceAvailableCallback<?>>>>();

	private LinkedList<IKernelServiceListener> serviceListeners = new LinkedList<IKernelServiceListener>();
	private LinkedList<IModuleListener> moduleListeners = new LinkedList<IModuleListener>();
	
	private Timer timer;

	public void start(IProgressMonitor monitor){
		monitor.setTaskName("Startup Kernel");
		this.monitorHolder.set(monitor);
		try {
			start();
			monitor.done(this);
		}catch(Throwable t){
			log.fatal("Failed to start kernel", t);
			monitor.taskFailed(t,"Failed to start kernel");
		}finally{
			this.monitorHolder.set(null);
		}
	}
	
	public void stop(IProgressMonitor monitor){
		monitor.setTaskName("Shutdown Kernel");
		this.monitorHolder.set(monitor);
		try {
			stop();
			monitor.done(this);
		}catch(Throwable t){
			log.fatal("Failed to stop kernel", t);
			monitor.taskFailed(t,"Failed to stop kernel");
		}finally{
			this.monitorHolder.set(null);
		}
		
	}


	@SuppressWarnings("unchecked")
	public void start() throws Exception{
		IProgressMonitor monitor = this.monitorHolder.get();
		fireKernelStarting();
		initModules();
		IKernelModule<C>[] mods = getAllModules();
		if(monitor != null){
			monitor.beginTask(mods.length);
		}
		int cnt = 1;
		for (IKernelModule<C> mod : mods) {
			if(monitor != null){
				monitor.updateProgress(cnt, "Start module :["+mod.getModuleName()+"] ...");
			}
			startModule(((M)mod));
			cnt++;
		}
		timer = new Timer("MicroKernel Timer Thread");
		this.started = true;
		fireKernelStarted();
	}


	@SuppressWarnings("unchecked")
	public void stop(){
		IProgressMonitor monitor = this.monitorHolder.get();
		fireKernelStopping();
//		destroyModules();
		IKernelModule<C>[] mods = getAllModules();
		if(monitor != null){
			monitor.beginTask(mods.length);
		}
		int cnt = 1;
		for (IKernelModule<C> mod : getAllModules()) {
			if(monitor != null){
				monitor.updateProgress(cnt, "stop module :["+mod.getModuleName()+"] ...");
			}
			stopModule(((M)mod));
			cnt++;
		}
		if(timer != null){
			timer.purge();
			timer.cancel();
			timer = null;
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
		synchronized(modules){
			for (M m : this.modules) {
				if(m instanceof AbstractModule){
					((AbstractModule<?>)m).notifyKernelStarted();
				}
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

	protected <T> void registerLocalService(Class<T> interfaceClazz, Object handler) {
		if(getServiceFuture(interfaceClazz).addService(interfaceClazz.cast(handler))){
			fireServiceRegistered(interfaceClazz, interfaceClazz.cast(handler));
		}
	}

	protected List<Class<?>> getExportingInterfaces(Class<?> clazz){
		ArrayList<Class<?>> list = new ArrayList<Class<?>>();
		getExportingInterfaces(clazz, list);
		return list;
	}
	
	protected boolean isValidServiceInterface(Class<?> clazz){
		if(!clazz.isInterface()){
			return false;
		}
		String pkgName = clazz.getCanonicalName();
		if(pkgName.startsWith("java.")||pkgName.startsWith("javax.")||pkgName.startsWith("sun.")||pkgName.startsWith("com.sun.")){
			return false;
		}
		return true;
	}
	
	protected void getExportingInterfaces(Class<?> clazz,List<Class<?>> list){
		if(!isValidServiceInterface(clazz)){
			return;
		}
		Class<?>[] interfaces = clazz.getInterfaces();
		if(interfaces != null){
			for (Class<?> class1 : interfaces) {
				getExportingInterfaces(class1,list);
			}
		}
		list.add(clazz);
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

	protected <T> void unregisterLocalService(Class<T> interfaceClazz, Object handler) {
		if(getServiceFuture(interfaceClazz).removeService(interfaceClazz.cast(handler))){
			fireServiceUnregistered(interfaceClazz, interfaceClazz.cast(handler));
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


//	protected void destroyModules() {
//		for (M mod : this.createdModules) {
//			try {
//				unregisterKernelModule(mod);
//			}catch(Exception e){
//				log.warn("Failed to unregister module :"+mod, e);
//			}
//		}
//		this.createdModules.clear();
//	}

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
	
	public IKernelModule<C>[] getAllModules() {
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
		return getContext().getService(interfaceClazz);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.IMicroKernel#getServiceAsync(java.lang.Class)
	 */
	@Override
	public <S> ServiceFuture<S> getServiceAsync(Class<S> interfaceClazz) {
		return getContext().getServiceAsync(interfaceClazz);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.IMicroKernel#checkServiceAvailable(java.lang.Class, com.wxxr.mobile.core.microkernel.api.IServiceAvailableCallback)
	 */
	@Override
	public <S> void checkServiceAvailable(Class<S> interfaceClazz,
			IServiceAvailableCallback<S> callback) {
		getContext().checkServiceAvailable(interfaceClazz, callback);
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

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.IMicroKernel#invokeLater(java.lang.Runnable, long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public ICancellable invokeLater(Runnable task, long delay, TimeUnit unit) {
		return getContext().invokeLater(task, delay, unit);
	}
	
}
