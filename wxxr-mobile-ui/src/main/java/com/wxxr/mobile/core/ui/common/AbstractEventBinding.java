/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import com.wxxr.mobile.core.ui.api.IBindingDecoratorRegistry;
import com.wxxr.mobile.core.ui.api.IEventBinding;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.InputEventDecorator;
import com.wxxr.mobile.core.ui.api.InputEventHandlingContext;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;

/**
 * @author neillin
 *
 */
public abstract class AbstractEventBinding implements IEventBinding {

	private InputEventDecorator decorator;
	private IView pModel;
	private IWorkbenchRTContext context;
	private String commandName, fieldName;
	private Object uiControl;
	private InputEventHandlingContext handlingCtx = new InputEventHandlingContext() {
		
		@Override
		public IWorkbenchRTContext getWorkbenchContext() {
			return context;
		}
		
		@Override
		public IView getViewModel() {
			return pModel;
		}
		
		@Override
		public Object getUIControl() {
			return uiControl;
		}
		
		@Override
		public String getFieldName() {
			return fieldName;
		}
		
		@Override
		public String getCommandName() {
			return commandName;
		}
	};
	

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IBinding#notifyDataChanged(com.wxxr.mobile.core.ui.api.ValueChangedEvent[])
	 */
	@Override
	public void notifyDataChanged(ValueChangedEvent... events) {

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IBinding#activate(com.wxxr.mobile.core.ui.api.IUIComponent)
	 */
	@Override
	public void activate(IView model) {
		this.pModel = model;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IBinding#deactivate()
	 */
	@Override
	public void deactivate() {
		this.pModel = null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IBinding#doUpdate()
	 */
	@Override
	public void doUpdate() {
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IBinding#destroy()
	 */
	@Override
	public void destroy() {
		this.decorator = null;
	}

	protected IUIComponent getField(){
		if(this.fieldName != null){
			return this.pModel.getChild(this.fieldName);
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IBinding#init(com.wxxr.mobile.core.ui.api.IWorkbenchRTContext)
	 */
	@Override
	public void init(IWorkbenchRTContext ctx) {
		this.context = ctx;
		this.decorator = new InputEventDecorator() {
			
			@Override
			public void handleEvent(InputEventHandlingContext context, InputEvent event) {
				IUIComponent field = getField();
				if(field != null){
					field.invokeCommand(commandName, event);
				}else{
					pModel.invokeCommand(commandName, event);
				}				
			}
		};
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IDecoratable#doDecorate(java.lang.String[])
	 */
	@Override
	public InputEventDecorator doDecorate(String... decoratorNames) {
		IBindingDecoratorRegistry registry = this.context.getWorkbenchManager().getBindingDecoratorRegistry();
		if((decoratorNames != null)&&(decoratorNames.length > 0)){
			for (String name : decoratorNames) {
				this.decorator = registry.createDecorator(name, this.decorator);
			}
		}
		return this.decorator;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IDecoratable#getDecoratorClass()
	 */
	@Override
	public Class<InputEventDecorator> getDecoratorClass() {
		return InputEventDecorator.class;
	}

	/**
	 * @return the pModel
	 */
	protected IView getModel() {
		return pModel;
	}

	/**
	 * @return the context
	 */
	protected IWorkbenchRTContext getContext() {
		return context;
	}

	/**
	 * @return the commandName
	 */
	protected String getCommandName() {
		return commandName;
	}

	/**
	 * @return the fieldName
	 */
	protected String getFieldName() {
		return fieldName;
	}

	/**
	 * @param commandName the commandName to set
	 */
	protected void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	/**
	 * @param fieldName the fieldName to set
	 */
	protected void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	
	protected void handleInputEvent(InputEvent event){
		this.decorator.handleEvent(handlingCtx, event);
	}

	/**
	 * @return the uiControl
	 */
	public Object getUIControl() {
		return uiControl;
	}

	/**
	 * @param uiControl the uiControl to set
	 */
	protected void setUIControl(Object uiControl) {
		this.uiControl = uiControl;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IBinding#isInitialized()
	 */
	@Override
	public boolean isInitialized() {
		return this.context != null;
	}
}
