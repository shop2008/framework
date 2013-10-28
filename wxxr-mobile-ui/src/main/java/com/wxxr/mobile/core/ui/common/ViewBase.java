/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.wxxr.mobile.core.ui.api.UIError;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;


/**
 * @author neillin
 *
 */
public abstract class ViewBase extends UIContainer<IUIComponent> implements IView {

	private IBinding<IView> binding;
	private List<IValueEvaluator> elvaluators;
	private Map<String, IUICommandHandler> commands;
	private boolean active = false;
	private IEvaluationContext evalContext = new IEvaluationContext() {

		public IUIComponent getField(String name) {
			return getChild(name);
		}

		public Object getBean(String name) {
			return getUIContext().getDomainModel(name);
		}
	};
	
	public ViewBase() {
		init();
	}
	
	public ViewBase(String name) {
		super(name);
		init();
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

	public List<UIError> getErrors() {
		List<UIError> errors = null;
		for (IDataField<?> fld : getChildren(IDataField.class)) {
			UIError err = fld.getUIError();
			if(err != null){
				if(errors == null){
					errors = new ArrayList<UIError>();
				}
				errors.add(err);
			}
		}
		return errors;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IBindable#doBinding(com.wxxr.mobile.core.ui.api.IBinding)
	 */
	public void doBinding(IBinding<IView> binding) {
		this.binding = binding;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IBindable#doUnbinding(com.wxxr.mobile.core.ui.api.IBinding)
	 */
	public boolean doUnbinding(IBinding<IView> binding) {
		if(this.binding == binding){
			this.binding = null;
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.impl.AbstractUIComponent#fireDataChangedEvent(com.wxxr.mobile.core.ui.api.ValueChangedEvent)
	 */
	@Override
	protected void fireDataChangedEvent(ValueChangedEvent event) {
		if(this.binding != null){
			this.binding.notifyDataChanged(event);
		}
		super.fireDataChangedEvent(event);
		invokeValueEvaluators(event);
	}
	
	protected void invokeValueEvaluators(final ValueChangedEvent event){
		if(this.elvaluators != null){
			getUIContext().getKernelContext().invokeLater(new Runnable() {				
				public void run() {
					for (IValueEvaluator eval : elvaluators) {
						if(eval.valueEffectedBy(event)){
							eval.doEvaluate(evalContext);
						}
					}
				}
			}, 10, TimeUnit.MILLISECONDS);
		}
	}
	
	protected ViewBase registerValueEvaluator(IValueEvaluator eval) {
		if(this.elvaluators == null){
			this.elvaluators = new ArrayList<IValueEvaluator>();
		}
		if(!this.elvaluators.contains(eval)){
			this.elvaluators.add(eval);
		}
		return this;
	}
	
	protected ViewBase unregisterValueEvaluator(IValueEvaluator eval) {
		if(this.elvaluators != null){
			this.elvaluators.remove(eval);
		}
		return this;
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
	
	protected abstract void init();
	
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