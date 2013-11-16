/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.wxxr.javax.el.BeanNameResolver;
import com.wxxr.javax.el.ELManager;
import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.bean.api.PropertyChangeEvent;
import com.wxxr.mobile.core.bean.api.PropertyChangeListener;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.DomainValueChangedEvent;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IBindingValueChangedCallback;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IDomainValueModel;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IMenuCallback;
import com.wxxr.mobile.core.ui.api.IMenuHandler;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.ISelection;
import com.wxxr.mobile.core.ui.api.ISelectionProvider;
import com.wxxr.mobile.core.ui.api.ISimpleSelection;
import com.wxxr.mobile.core.ui.api.IStructureSelection;
import com.wxxr.mobile.core.ui.api.IUICommandHandler;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IValueEvaluator;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewBinding;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.ValidationError;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.util.LRUMap;
import com.wxxr.mobile.core.util.StringUtils;


/**
 * @author neillin
 *
 */
public abstract class ViewBase extends UIContainer<IUIComponent> implements IView {
	private static final Trace loger = Trace.register(ViewBase.class);
	private Trace vLog;

	protected Trace getLog() {
		if((vLog == null)&&(getName() != null)){
			vLog = Trace.getLogger("com.wxxr.mobile.core.ui."+(IPage.class.isAssignableFrom(getClass()) ? "P" : "V")+getName());
		}
		return vLog != null ? vLog : loger;
	}

	protected class BeanPropertyChangedListener implements PropertyChangeListener {
		private final String beanName;

		public BeanPropertyChangedListener(String name){
			this.beanName = name;
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if(getLog().isDebugEnabled()){
				getLog().debug("Receiving property changed event from bean :"+beanName+", event :["+evt+"]");
			}
			if(!isActive()){
				Object source = evt.getSource();
				if(source instanceof IBindableBean){
					((IBindableBean)source).removePropertyChangeListener(this);
				}
				return;
			}else {
				fireDataChangedEvent(new DomainValueChangedEventImpl(evt.getSource(), beanName, evt.getPropertyName()));
			}
		}
	}

//	private class EventQueue implements Runnable{
//		private LinkedList<ValueChangedEvent> pendingEvents;
//		private volatile Thread thread;
//
//		protected synchronized void addPendingEvent(ValueChangedEvent evt) {
//			if(pendingEvents == null){
//				this.pendingEvents = new LinkedList<ValueChangedEvent>();
//			}
//			pendingEvents.add(evt);
//		}
//
//		protected synchronized ValueChangedEvent[] getPendingEvents() {
//			ValueChangedEvent[] evts = null;
//			if((pendingEvents != null)&&(pendingEvents.size() > 0)){
//				ValueChangedEvent last = this.pendingEvents.getLast();
//				ValueChangedEvent first = this.pendingEvents.getFirst();
//				if(((System.currentTimeMillis() - first.getTimestamp()) > 2000L)||((System.currentTimeMillis() - last.getTimestamp()) > 500L)){
//					evts = this.pendingEvents.toArray(new ValueChangedEvent[0]);
//					this.pendingEvents.clear();
//				}
//			}
//			return evts;
//		}
//
//
//		@Override
//		public void run() {
//			this.thread = Thread.currentThread();
//			while(this.thread != null){
//				try {
//					ValueChangedEvent[] evts = getPendingEvents();
//					if(evts != null){
//						if(binding != null){
//							if(getLog().isDebugEnabled()){
//								getLog().debug("fire data changed events :"+StringUtils.join(evts));
//							}
//							binding.notifyDataChanged(evts);
//						}
//					}else{
//						try {
//							Thread.sleep(60L);
//						} catch (InterruptedException e) {
//						}
//					}
//				}catch(Throwable t){
//					getLog().error("Caught exception at event loop of viewbase", t);
//				}
//			}
//			if((this.pendingEvents != null)&&(this.pendingEvents.size() > 0)){
//				if(binding != null){
//					binding.notifyDataChanged(this.pendingEvents.toArray(new ValueChangedEvent[0]));
//				}
//				this.pendingEvents.clear();
//				this.pendingEvents = null;
//			}
//		}
//
//		public synchronized void start() {
//			new Thread(this).start();
//		}
//
//		public synchronized void stop() {
//			if(this.thread != null){
//				Thread t = this.thread;
//				this.thread = null;
//				if(t.isAlive()){
//					t.interrupt();
//					try {
//						t.join(1000L);
//					} catch (InterruptedException e) {
//					}
//				}
//			}
//		}
//	}

	private BeanNameResolver beanNameResolver = new BeanNameResolver() {

		/* (non-Javadoc)
		 * @see com.wxxr.javax.el.BeanNameResolver#getBean(java.lang.String)
		 */
		@Override
		public Object getBean(String beanName) {
			if("selection".equals(beanName)){
				return getSelection();
			}
			if("selections".equals(beanName)){
				return getSelections();
			}
			Object bean = beans != null ? beans.get(beanName) : null;
			return bean != null ? bean : getChild(beanName);
		}

		/**
		 * @return
		 */
		protected Object getSelection() {
			ISelection selection  = getUIContext().getWorkbenchManager().getWorkbench().getSelectionService().getSelection();
			if(selection instanceof ISimpleSelection){
				return ((ISimpleSelection)selection).getSelected();
			}else if(selection instanceof IStructureSelection){
				Object[] vals = ((IStructureSelection)selection).toArray();
				return (vals != null)&& (vals.length > 0) ? vals[0] : null;
			}else{
				return null;
			}
		}
		
		/**
		 * @return
		 */
		protected Object[] getSelections() {
			ISelection selection  = getUIContext().getWorkbenchManager().getWorkbench().getSelectionService().getSelection();
			if(selection instanceof ISimpleSelection){
				return new Object[] { ((ISimpleSelection)selection).getSelected()};
			}else if(selection instanceof IStructureSelection){
				return ((IStructureSelection)selection).toArray();
			}else{
				return null;
			}
		}

		/* (non-Javadoc)
		 * @see com.wxxr.javax.el.BeanNameResolver#isNameResolved(java.lang.String)
		 */
		@Override
		public boolean isNameResolved(String beanName) {
			return (getChild(beanName) != null)||((beans != null)&&(beans.containsKey(beanName)))||"selection".equals(beanName)||"selections".equals(beanName);
		}
	};
	private IViewBinding binding;
	private Map<String, IUICommandHandler> commands;
//	private EventQueue eventQueue;
	private IMenuCallback menuCallback;
	private ELManager elm;
	private Map<String, Object> beans;
	private List<IValueEvaluator<?>> evaluators;
	private List<IDomainValueModel<?>> domainModels;
	private boolean enableSelectionProvider;
	private SelectionProviderSupport selectionProvider;
	private LRUMap<String, BeanPropertyChangedListener> beanListeners = new LRUMap<String, BeanPropertyChangedListener>(10, 10*60);

	public ViewBase() {
		doInit();
	}

	public ViewBase(String name) {
		super(name);
		doInit();
	}

	protected void doInit(){
		onCreate();
	}

	public boolean isActive() {
		return this.binding != null && this.binding.isOnShow();
	}

	public void show(){
		this.show(true);
	}

	public ELManager getELManager(boolean createIfNotExisting){
		if(createIfNotExisting&&(this.elm == null)){
			this.elm = new ELManager();
			initELManager(this.elm);
		}
		return this.elm;
	}

	/**
	 * 
	 */
	protected void initELManager(ELManager mgr) {
		mgr.addBeanNameResolver(this.beanNameResolver);
		mgr.importClass(AttributeKeys.class.getCanonicalName());
	}

	protected BeanPropertyChangedListener getBeanListener(String name){
		BeanPropertyChangedListener l = this.beanListeners.get(name);
		if(l == null){
			l = new BeanPropertyChangedListener(name);
			this.beanListeners.put(name, l);
		}
		return l;
	}

	protected void registerBean(String name, Object bean){
		if(this.beans == null){
			this.beans = new HashMap<String, Object>();
		}
		Object oldBean = this.beans.get(name);
		this.beans.put(name, bean);
		if(!ModelUtils.isEquals(oldBean, bean)){
			if(!isEventDisabled()){
				DomainValueChangedEventImpl evt = new DomainValueChangedEventImpl(this, name);
				fireDataChangedEvent(evt);
			}
			if(bean instanceof IBindableBean){
				((IBindableBean)bean).addPropertyChangeListener(getBeanListener(name));
			}
		}
	}

	protected void registerService(String name, Object service){
		if(this.beans == null){
			this.beans = new HashMap<String, Object>();
		}
		this.beans.put(name, service);
	}

	protected ViewBase unregisterService(String name){
		if(this.beans != null){
			this.beans.remove(name);
		}
		return this;
	}


	protected ViewBase unregisterBean(String name){
		if(this.beans != null){
			this.beans.remove(name);
		}
		return this;
	}

	protected void registerDomainModel(IDomainValueModel<?> model){
		if(this.domainModels == null){
			this.domainModels = new ArrayList<IDomainValueModel<?>>();
		}
		if(!this.domainModels.contains(model)){
			this.domainModels.add(model);
		}
	}

	protected ViewBase unregisterDomainModel(IDomainValueModel<?> model){
		if(this.domainModels != null){
			this.domainModels.remove(model);
		}
		return this;
	}


	protected void addValueEvaluator(IValueEvaluator<?> evaluator){
		if(this.evaluators == null){
			this.evaluators = new ArrayList<IValueEvaluator<?>>();
		}
		if(!this.evaluators.contains(evaluator)){
			this.evaluators.add(evaluator);
		}
	}

	protected ViewBase removeValueEvaluator(IValueEvaluator<?> evaluator){
		if(this.evaluators != null){
			this.evaluators.remove(evaluator);
		}
		return this;
	}

	public void show(boolean backable) {
		getUIContext().getWorkbenchManager().getPageNavigator().showView(this,backable);
	}

	public void hide() {
		getUIContext().getWorkbenchManager().getPageNavigator().hideView(this);
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
		List<IMenu> menus = getChildren(IMenu.class);
		if((menus != null)&&(menus.size() > 0)){
			for (IMenu iMenu : menus) {
				IMenuHandler handler = ((IViewBinding)binding).getMenuHandler(iMenu.getName());
				if(handler == null){
					continue;
				}
				if(this.menuCallback == null){
					this.menuCallback = new IMenuCallback() {

						@Override
						public void onShow(String menuName) {
							onMenuShow(menuName);
						}

						@Override
						public void onHide(String menuName) {
							onMenuHide(menuName);
						}
					};
				}
				handler.setMenuCallback(this.menuCallback);
			}
		}
		this.binding = (IViewBinding)binding;
		forceValueEvalution();
		if(this.beans != null){
			for (Entry<String,Object> entry: this.beans.entrySet()) {
				Object bean = entry.getValue();
				if(bean instanceof IBindableBean){
					((IBindableBean)bean).addPropertyChangeListener(getBeanListener(entry.getKey()));
				}
			}
		}
	}

	protected void onShow(IBinding<IView> binding){

	}

	protected void onMenuShow(String menuId){

	}

	protected void onMenuHide(String menuId){

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IBindable#doUnbinding(com.wxxr.mobile.core.ui.api.IBinding)
	 */
	public boolean doUnbinding(IBinding<IView> binding) {
		if(this.binding == binding){
			this.binding = null;
			if(this.beans != null){
				for (Entry<String,Object> entry: this.beans.entrySet()) {
					Object bean = entry.getValue();
					if(bean instanceof IBindableBean){
						BeanPropertyChangedListener l = getBeanListener(entry.getKey());
						if(l != null){
							((IBindableBean)bean).addPropertyChangeListener(l);
						}
					}
				}
			}
			onHide(binding);
			List<IMenu> menus = getChildren(IMenu.class);
			if((menus != null)&&(menus.size() > 0)){
				for (IMenu iMenu : menus) {
					IMenuHandler handler = ((IViewBinding)binding).getMenuHandler(iMenu.getName());
					if(handler != null){
						handler.setMenuCallback(null);
					}
				}
			}
			this.menuCallback = null;
//			if(this.eventQueue != null){
//				this.eventQueue.stop();
//				this.eventQueue = null;
//			}
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
		if(getLog().isDebugEnabled()){
			getLog().debug("processing event :", event);
		}
		onDataChanged(event);
		if(this.evaluators != null){
			for (IValueEvaluator<?> eval : this.evaluators) {
				if(eval.valueEffectedBy(event)){
					eval.doEvaluate();
				}
			}
		}
		if(event instanceof DomainValueChangedEvent){
			if(this.domainModels != null){
				for (IDomainValueModel<?> m : this.domainModels) {
					if(m.valueEffectedBy(event)){
						m.doEvaluate();
					}
				}
			}
//			return;
		}
//		if(this.binding != null){
//			if(this.eventQueue == null){
//				this.eventQueue = new EventQueue();
//				this.eventQueue.start();
//			}
//			this.eventQueue.addPendingEvent(event);
//		}
	}

	protected void forceValueEvalution() {
		if(this.domainModels != null){
			for (IDomainValueModel<?> m : this.domainModels) {
				m.doEvaluate();
			}
		}
		if(this.evaluators != null){
			for (IValueEvaluator<?> eval : this.evaluators) {
				eval.doEvaluate();
			}
		}
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
	
	@Override
	public void setValueChangedCallback(IBindingValueChangedCallback cb) {
		
	}

	/**
	 * @return the enableSelectionProvider
	 */
	public boolean isEnableSelectionProvider() {
		return enableSelectionProvider;
	}

	/**
	 * @param enableSelectionProvider the enableSelectionProvider to set
	 */
	public void setEnableSelectionProvider(boolean enableSelectionProvider) {
		this.enableSelectionProvider = enableSelectionProvider;
	}

	protected void updateSelection(Object... selections){
		SelectionProviderSupport provider = getSelectionProvider();
		if(provider == null){
			throw new IllegalStateException("Selection provider must be enabled before update selection !");
		}
		if(selections.length > 1){
			StructureSelectionImpl impl = new StructureSelectionImpl();
			for (Object val : selections) {
				impl.addSelection(val);
			}
			provider.setSelection(impl);
		}else if(selections.length == 1){
			SimpleSelectionImpl impl = new SimpleSelectionImpl();
			impl.setSelected(selections[0]);
			provider.setSelection(impl);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IView#getSelectionProvider()
	 */
	@Override
	public SelectionProviderSupport getSelectionProvider() {
		if(enableSelectionProvider && (this.selectionProvider == null)){
			this.selectionProvider = new SelectionProviderSupport(getName());
		}
		return this.selectionProvider;
	}	


}