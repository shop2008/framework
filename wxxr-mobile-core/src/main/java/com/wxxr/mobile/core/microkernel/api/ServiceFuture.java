package com.wxxr.mobile.core.microkernel.api;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ServiceFuture<T> implements Future<T> {
	
	   private CountDownLatch latch = new CountDownLatch(1);
	   private LinkedList<T> services = new LinkedList<T>();
	   
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return false;
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public boolean isDone() {
		return this.latch.getCount() == 0;
	}

	public T[] getServices(T[] vals) {
		synchronized(this.services){
			return this.services.toArray(vals);
		}
	}
	
	public T getService() {
		synchronized(this.services){
			return this.services.isEmpty() ? null : this.services.iterator().next();
		}
	}
	
	@Override
	public T get() throws InterruptedException {
		try {
			return get(-1,null);
		} catch (TimeoutException e) {	// should not happen
			return null;
		}	
	}

	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException,TimeoutException {
		synchronized(this.services){
			if(this.services.size() > 0){
				return this.services.iterator().next();
			}else if(this.latch == null){
				this.latch = new CountDownLatch(1);
			}
		}
		if(timeout == -1){
			this.latch.await();
		}else{
			if (!this.latch.await(timeout,unit)) {
				throw new TimeoutException("Timeout when waiting for service!!!");
			}
			
		}
		synchronized(this.services){
			return this.services.iterator().next();
		}	
	}
	
	boolean addService(T object) {
		synchronized(this.services){
			if(!this.services.contains(object)){
				this.services.add(object);
				if((this.latch != null)&&(this.latch.getCount() > 0)){
					this.latch.countDown();
					this.latch = null;
				}
				return true;
			}
			return false;
		}
	}
	
	boolean removeService(T object) {
		synchronized(this.services){
			return this.services.remove(object);
		}
	}
}
