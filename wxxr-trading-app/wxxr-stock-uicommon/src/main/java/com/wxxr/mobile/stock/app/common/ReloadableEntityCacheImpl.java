/**
 * 
 */
package com.wxxr.mobile.stock.app.common;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.wxxr.mobile.core.command.api.CommandConstraintViolatedException;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutor;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.util.IAsyncCallback;
import com.wxxr.mobile.stock.app.StockAppBizException;


/**
 * @author neillin
 *
 */
public abstract class ReloadableEntityCacheImpl<K,V> implements IReloadableEntityCache<K, V> {
	private static final Trace log = Trace.register(ReloadableEntityCacheImpl.class);
	private Trace cLog;
	
	private static Reloader reloader;
	
	private static class Reloader extends Thread {
        
        private List<WeakReference<ReloadableEntityCacheImpl<?,?>>> refs ;
                
        public Reloader(){
            super("ReloadableEntityCache reloading Processor");
            super.setDaemon(true);
            refs = Collections.synchronizedList(new ArrayList<WeakReference<ReloadableEntityCacheImpl<?,?>>>());
        }
        
        public void run(){
            while(true){
                try {
                    Thread.sleep(10*1000);		// check for every 10 seconds
                } catch (InterruptedException e) {
                }
                try {
	                if(refs.isEmpty()){
	                	continue;
	                }
	                @SuppressWarnings("unchecked")
	                WeakReference<ReloadableEntityCacheImpl<?,?>>[] vals = refs.toArray(new WeakReference[refs.size()]);
	                for(WeakReference<ReloadableEntityCacheImpl<?,?>> ref: vals) {
	                	ReloadableEntityCacheImpl<?,?> list = ref.get();
	                    if(list != null){
	                    	if((System.currentTimeMillis() - list.lastUpdateTime) >= list.reloadIntervalInSeconds){
		                    	int numOfActiveClient = list.getNumberOfActiveClient();
	                    		if(list.isStopAutoReloadIfNotActiveClient() && (numOfActiveClient == 0)){
		                    		continue;
		                    	}
	                    		list.doReloadIfNeccessay();
	                    	}
	                    }else{
	                    	refs.remove(ref);
	                    }
					}
                }catch(Throwable t){
                	log.error("Caught throwable inside reload processor of ReloadableEntityCache", t);
                }
            }
        }
        
        public void add(ReloadableEntityCacheImpl<?,?> cache) {
        	WeakReference<ReloadableEntityCacheImpl<?,?>> ref = new WeakReference<ReloadableEntityCacheImpl<?,?>>(cache);
        	this.refs.add(ref);
        }
    }
	
	protected Trace getLog() {
		if(this.cLog == null){
			this.cLog = Trace.register(getClass().getPackage().getName()+".Cache_"+getEntityTypeName());
		}
		return this.cLog;
	}
	
	/**
	 * 
	 */
	protected static synchronized void startReloader() {
		if(reloader == null){
			reloader = new Reloader();
			reloader.start();
		}
	}


	private final String name;
	private Map<K,V> cache = new HashMap<K,V>();
	private ReentrantReadWriteLock rwLock;
	private Lock rlock, wlock;
	private long lastUpdateTime;
	private final int reloadIntervalInSeconds;
	private int minReloadIntervalInSeconds = 10;
	private List<WeakReference<ICacheUpdatedCallback>> callbacks = new ArrayList<WeakReference<ICacheUpdatedCallback>>();
	private volatile boolean inReloading = false;
	private boolean stopAutoReloadIfNotActiveClient = true;
	
	
	public ReloadableEntityCacheImpl(String name){
		this.name = name;
		this.reloadIntervalInSeconds = -1;
		init();
	}
	
	public ReloadableEntityCacheImpl(String name, int reloadInterval){
		this.name = name;
		init();
		this.reloadIntervalInSeconds = reloadInterval;
		if(this.reloadIntervalInSeconds > 0){
			startReloader();
			reloader.add(this);
		}
	}
	
	private WeakReference<ICacheUpdatedCallback> getCallbackReference(ICacheUpdatedCallback cb){
		for (WeakReference<ICacheUpdatedCallback> ref : this.callbacks) {
			if(ref.get() == cb){
				return ref;
			}
		}
		return null;
	}
	
	private void clearCallbacks(){
		for (Iterator<WeakReference<ICacheUpdatedCallback>> itr = this.callbacks.iterator();itr.hasNext();) {
			WeakReference<ICacheUpdatedCallback> ref = itr.next();
			if(ref.get() == null){
				itr.remove();
				if(getLog().isDebugEnabled()){
					getLog().debug("a cache callback was GC, remove it from callback list !");
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IBindableEntityCache#registerCallback(com.wxxr.mobile.stock.app.common.ICacheUpdatedCallback)
	 */
	@Override
	public void registerCallback(ICacheUpdatedCallback cb){
		synchronized(this.callbacks) {
			if(getCallbackReference(cb) == null){
				this.callbacks.add(new WeakReference<ICacheUpdatedCallback>(cb));
				if(getLog().isDebugEnabled()){
					getLog().debug("cache callback :["+cb+"] was registered !");
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IBindableEntityCache#unregisterCallback(com.wxxr.mobile.stock.app.common.ICacheUpdatedCallback)
	 */
	@Override
	public void unregisterCallback(ICacheUpdatedCallback cb){
		synchronized(this.callbacks){
			if(getLog().isDebugEnabled()){
				getLog().debug("Going to unregister cache callback :"+cb);
			}
			WeakReference<ICacheUpdatedCallback> ref = getCallbackReference(cb);
			if(ref != null){
				this.callbacks.remove(ref);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected WeakReference<ICacheUpdatedCallback>[] getCallbacks(){
		synchronized(this.callbacks){
			clearCallbacks();
			return this.callbacks.toArray(new WeakReference[this.callbacks.size()]);
		}
	}


	public int getNumberOfActiveClient() {
		WeakReference<ICacheUpdatedCallback>[] cbs = getCallbacks();
		return cbs != null ? cbs.length : 0;
	}

	protected void init() {
		this.rwLock = new ReentrantReadWriteLock();
		this.rlock = this.rwLock.readLock();
		this.wlock = this.rwLock.writeLock();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IReloadableEntityCache#doReloadIfNeccessay()
	 */
	@Override
	public void doReloadIfNeccessay(){
		doReloadIfNeccessay(null);
	}
	
	public void doReloadIfNeccessay(Map<String, Object> params) {
		if(inReloading){
			if(getLog().isDebugEnabled()){
				getLog().debug("cache is still in loading, reloading aborted, cache name :"+getEntityTypeName());
			}
			return;
		}
		int secondsElapsed = (int)((System.currentTimeMillis() - lastUpdateTime)/1000L);
		if(secondsElapsed < minReloadIntervalInSeconds){
			if(getLog().isDebugEnabled()){
				getLog().debug("minimum reload interval is ["+this.minReloadIntervalInSeconds+"] seconds, seconds elapsed since last reload :"+secondsElapsed);
			}
			return;
		}
		if(getLog().isDebugEnabled()){
			getLog().debug("cache is going to be reloaded ...");
		}
		doReload(false,params);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IReloadableEntityCache#forceReload()
	 */
	@Override
	public void forceReload(boolean wait4Finish) {
		if(getLog().isDebugEnabled()){
			getLog().debug("cache is forced to reload ...");
		}
		doReload(wait4Finish,null);
	}
	
	/**
	 * 
	 */
	protected void doReload(boolean wait4Finish,Map<String, Object> params) {
		final ICommand<?> cmd = getReloadCommand(params);
		if(cmd == null){
			log.info("Not reload will be performanced since getReloadCommand() return null !");
			return;
		}
		IAsyncCallback callback = new IAsyncCallback() {
			
			@Override
			public void success(Object result) {
					if(getLog().isDebugEnabled()){
						getLog().debug("Entity loader command was execute successfuly, size of result list :"+ (result != null ? ((List)result).size() : 0));
					}
					if(processReloadResult(cmd,result)){
						WeakReference<ICacheUpdatedCallback>[] refs = getCallbacks();
						if(getLog().isDebugEnabled()){
							getLog().debug("Entity cache was updated , going to notify bindable list warpper, callback number :"+(refs != null ? refs.length : 0));
						}
						for (WeakReference<ICacheUpdatedCallback> ref : refs) {
							ICacheUpdatedCallback cb = ref.get();
							if(cb != null){
								if(getLog().isDebugEnabled()){
									getLog().debug("notify list warpper :"+cb);
								}
								cb.dataChanged(ReloadableEntityCacheImpl.this);
							}
						}
					}
					inReloading = false;
			}
			
			@Override
			public void failed(Object cause) {
					handleReloadFailed(cmd,cause);
					inReloading = false;
			}
		};
		if(!wait4Finish){
			this.inReloading = true;
			KUtils.getService(ICommandExecutor.class).submitCommand(cmd, callback);
		}else{
			Future<?> future = KUtils.getService(ICommandExecutor.class).submitCommand(cmd);
			try {
				callback.success(future.get());
			} catch (InterruptedException e) {
				return;
			} catch (ExecutionException e) {
				Throwable t = e.getCause();
				handleReloadFailed(cmd,t);
				if(t instanceof StockAppBizException){
					throw (StockAppBizException)t;
				}else if( t instanceof CommandConstraintViolatedException){
					throw (CommandConstraintViolatedException)t;
				}else{
					throw new StockAppBizException("Caught exception when try to reload data for cache :"+getEntityTypeName(),t);
				}
			}
		}
	}
		
	protected abstract ICommand<?> getReloadCommand(Map<String, Object> params);
	
	protected abstract boolean processReloadResult(ICommand<?> cmd,Object result);
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IBindableEntityCache#getEntity(java.lang.Object)
	 */
	@Override
	public V getEntity(Object key) {
		rlock.lock();
		try {
			return this.cache.get(key);
		}finally{
			rlock.unlock();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IBindableEntityCache#getAllKeys()
	 */
	@Override
	public Object[] getAllKeys() {
		rlock.lock();
		try {
			return this.cache.keySet().toArray();
		}finally{
			rlock.unlock();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IBindableEntityCache#getValues()
	 */
	@Override
	public Object[] getValues() {
		rlock.lock();
		try {
			return this.cache.values().toArray();
		}finally{
			rlock.unlock();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IBindableEntityCache#putEntity(K, V)
	 */
	@Override
	public void putEntity(K key, V value){
		wlock.lock();
		try {
			this.cache.put(key, value);
		}finally{
			wlock.unlock();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IBindableEntityCache#removeEntity(K)
	 */
	@Override
	public V removeEntity(K key){
		wlock.lock();
		try {
			return this.cache.remove(key);
		}finally{
			wlock.unlock();
		}
	}

	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IBindableEntityCache#clear()
	 */
	@Override
	public void clear() {
		wlock.lock();
		try {
			this.cache.clear();
		}finally{
			wlock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IBindableEntityCache#getCacheSize()
	 */
	@Override
	public int getCacheSize() {
		rlock.lock();
		try {
			return this.cache.size();
		}finally{
			rlock.unlock();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IBindableEntityCache#acceptVisitor(com.wxxr.mobile.stock.app.common.ICacheVisitor)
	 */
	@Override
	public void acceptVisitor(ICacheVisitor<V> visitor){
		rlock.lock();
		try {
			for (Entry<K, V> entry : this.cache.entrySet()) {
				if(visitor.visit(entry.getKey(), entry.getValue()) == false){
					break;
				}
			}
		}finally{
			rlock.unlock();
		}
	}
	
	protected void handleReloadFailed(ICommand<?> cmd,Object cause) {
		getLog().warn("Failed to reload list data of :"+name, cause);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IReloadableEntityCache#getLastUpdateTime()
	 */
	@Override
	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IReloadableEntityCache#getMinReloadIntervalInSeconds()
	 */
	@Override
	public int getMinReloadIntervalInSeconds() {
		return minReloadIntervalInSeconds;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IReloadableEntityCache#setMinReloadIntervalInSeconds(int)
	 */
	@Override
	public void setMinReloadIntervalInSeconds(int minReloadIntervalInSeconds) {
		this.minReloadIntervalInSeconds = minReloadIntervalInSeconds;
	}

	/**
	 * @return the reloadIntervalInSeconds
	 */
	@Override
	public int getAutoReloadIntervalInSeconds() {
		return reloadIntervalInSeconds;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IBindableEntityCache#getEntities(com.wxxr.mobile.stock.app.common.IEntityFilter, java.util.Comparator)
	 */
	@Override
	public BindableListWrapper<V> getEntities(IEntityFilter<V> filter,
			Comparator<V> comparator) {
		return new BindableListWrapper<V>(this,filter,comparator);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IBindableEntityCache#getEntityTypeName()
	 */
	@Override
	public String getEntityTypeName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IReloadableEntityCache#forceReload(java.util.Map, boolean)
	 */
	@Override
	public void forceReload(Map<String, Object> params, boolean wait4Finish) {
		doReload(wait4Finish,params);
	}

	/**
	 * @return the stopAutoReloadIfNotActiveClient
	 */
	public boolean isStopAutoReloadIfNotActiveClient() {
		return stopAutoReloadIfNotActiveClient;
	}

	/**
	 * @param stopAutoReloadIfNotActiveClient the stopAutoReloadIfNotActiveClient to set
	 */
	public void setStopAutoReloadIfNotActiveClient(
			boolean stopAutoReloadIfNotActiveClient) {
		this.stopAutoReloadIfNotActiveClient = stopAutoReloadIfNotActiveClient;
	}


}
