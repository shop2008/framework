/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IEvaluationContext;
import com.wxxr.mobile.core.ui.api.IUICommandHandler;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IValueEvaluator;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.ValidationError;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;


/**
 * @author neillin
 *
 */
public abstract class ViewBase extends UIContainer<IUIComponent> implements IView {

	private class EventQueue implements Runnable{
		private LinkedList<ValueChangedEvent> pendingEvents;
		private volatile Thread thread;
		
		protected synchronized void addPendingEvent(ValueChangedEvent evt) {
			if(pendingEvents == null){
				this.pendingEvents = new LinkedList<ValueChangedEvent>();
			}
			pendingEvents.add(evt);
		}
		
		protected synchronized ValueChangedEvent[] getPendingEvents() {
			ValueChangedEvent[] evts = null;
			if((pendingEvents != null)&&(pendingEvents.size() > 0)){
				ValueChangedEvent last = this.pendingEvents.getLast();
				ValueChangedEvent first = this.pendingEvents.getFirst();
				if(((System.currentTimeMillis() - first.getTimestamp()) > 2000L)||((System.currentTimeMillis() - last.getTimestamp()) > 500L)){
					evts = this.pendingEvents.toArray(new ValueChangedEvent[0]);
					this.pendingEvents.clear();
				}
			}
			return evts;
		}


		@Override
		public void run() {
			this.thread = Thread.currentThread();
			while(this.thread != null){
				ValueChangedEvent[] evts = getPendingEvents();
				if(evts != null){
					if(binding != null){
						binding.notifyDataChanged(evts);
					}
				}else{
					try {
						Thread.sleep(60L);
					} catch (InterruptedException e) {
					}
				}
			}
			if((this.pendingEvents != null)&&(this.pendingEvents.size() > 0)){
				if(binding != null){
					binding.notifyDataChanged(this.pendingEvents.toArray(new ValueChangedEvent[0]));
				}
				this.pendingEvents.clear();
				this.pendingEvents = null;
			}
		}
		
		public synchronized void start() {
			new Thread(this).start();
		}
		
		public synchronized void stop() {
			if(this.thread != null){
				Thread t = this.thread;
				this.thread = null;
				if(t.isAlive()){
					t.interrupt();
					try {
						t.join(1000L);
					} catch (InterruptedException e) {
					}
				}
			}
		}
	}
	
	private IBinding<IView> binding;
	private Map<String, IUICommandHandler> commands;
	private boolean active = false;
	private EventQueue eventQueue;
	
	public ViewBase() {
		onCreate();
	}
	
	public ViewBase(String name) {
		super(name);
		onCreate();
	}

	public boolean isActive() {
		return this.active;
	}

	public void show() {
		getUIContext().getWorkbenchManager().getPageNavigator().showView(this);
		this.active = true;
	}

	public void hide() {
		getUIContext().getWorkbenchManager().getPageNavigator().hideView(this);
		this.active = false;
	}

	public List<ValidationError> getErrors() {
		List<ValidationError> errors = null;
		for (IDataField<?> fld : getChildren(IDataField.class)) {
			ValidationError[] errs = fld.getValidationErrors();
			if(errs != null){
				for (ValidationError err : errs) {
					if(errors == null){
						errors = new ArrayList<ValidationError>();
					}
					errors.add(err);
				}
			}
		}
		return errors;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IBindable#doBinding(com.wxxr.mobile.core.ui.api.IBinding)
	 */
	public void doBinding(IBinding<IView> binding) {
		onShow(binding);
		this.binding = binding;
	}

	protected void onShow(IBinding<IView> binding){
		
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IBindable#doUnbinding(com.wxxr.mobile.core.ui.api.IBinding)
	 */
	public boolean doUnbinding(IBinding<IView> binding) {
		if(this.binding == binding){
			this.binding = null;
			onHide(binding);
			if(this.eventQueue != null){
				this.eventQueue.stop();
				this.eventQueue = null;
			}
			return true;
		}
		return false;
	}
	
	protected void onHide(IBinding<IView> binding) {
		
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.impl.AbstractUIComponent#fireDataChangedEvent(com.wxxr.mobile.core.ui.api.ValueChangedEvent)
	 */
	@Override
	protected void fireDataChangedEvent(ValueChangedEvent event) {
		if(this.binding != null){
			if(this.eventQueue == null){
				this.eventQueue = new EventQueue();
				this.eventQueue.start();
			}
			this.eventQueue.addPendingEvent(event);
		}
		super.fireDataChangedEvent(event);
		onDataChanged(event);
	}
	
	protected void onDataChanged(ValueChangedEvent event){
		
	}
	
	protected ViewBase addUICommand(String cmdName,IUICommandHandler command){
		if(this.commands == null){
			this.commands = new HashMap<String, IUICommandHandler>();
		}
		this.commands.put(cmdName, command);
		return this;
	}
	
	protected ViewBase removeUICommand(String cmdName,IUICommandHandler command){
		if(this.commands != null){
			IUICommandHandler cmd = this.commands.get(cmdName);
			if(cmd == command){
				this.commands.remove(cmdName);
			}
		}
		return this;
	}


	protected boolean hasCommand(String cmdName){
		return this.commands != null && this.commands.containsKey(cmdName);
	}
	
	protected abstract void onCreate();
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.AbstractUIComponent#invokeCommand(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void invokeCommand(String cmdName, InputEvent event) {
		if(hasCommand(cmdName)){
			getUIContext().getWorkbenchManager().getCommandExecutor().executeCommand(cmdName,this,this.commands.get(cmdName), event);
		}else{
			super.invokeCommand(cmdName, event);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected <T> IDataField<T> getField(String name){
		return getChild(name, IDataField.class);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IBindable#getBinding()
	 */
	@Override
	public IBinding<IView> getBinding() {
		return this.binding;
	}

}