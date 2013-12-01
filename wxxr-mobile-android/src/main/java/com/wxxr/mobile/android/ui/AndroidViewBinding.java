/**
 * 
 */
package com.wxxr.mobile.android.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IBindingContext;
import com.wxxr.mobile.core.ui.api.IEventBinder;
import com.wxxr.mobile.core.ui.api.IEventBinderManager;
import com.wxxr.mobile.core.ui.api.IFieldBinder;
import com.wxxr.mobile.core.ui.api.IFieldBinderManager;
import com.wxxr.mobile.core.ui.api.IFieldBinding;
import com.wxxr.mobile.core.ui.api.IMenuHandler;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author neillin
 *
 */
public class AndroidViewBinding implements IAndroidViewBinding{

	private static final Trace log = Trace.register(AndroidViewBinding.class);
	
	private class FieldBindingCreator implements IFieldBinding{
		private final View view;
		private final String fieldName;
		private final Map<String, String> params;
		private IFieldBinding binding;
		
		FieldBindingCreator(View v, String name, Map<String, String> map){
			if((v == null)||(name == null)){
				throw new IllegalArgumentException("Neither view nor field name could be NULL, view :"+v+", name :"+name);
			}
			this.view = v;
			this.fieldName = name;
			this.params = map;
		}
		
		public void createNActivateBinding(IView vmodel){
			IUIComponent field = vmodel.getChild(fieldName);
			if(field == null){
				throw new IllegalArgumentException("Cannot find field of :["+fieldName+"] from view :"+vmodel.getName());
			}
			if(binding == null){
				IFieldBinderManager mgr = bindingContext.getWorkbenchManager().getFieldBinderManager();
				IFieldBinder binder = mgr.getFieldBinder(field.getClass(),view.getClass());
				binding = binder.createBinding(new IAndroidBindingContext() {
					
					@Override
					public Context getUIContext() {
						return bindingContext.getUIContext();
					}
					
					@Override
					public View getBindingControl() {
						return view;
					}

					@Override
					public IWorkbenchManager getWorkbenchManager() {
						return bindingContext.getWorkbenchManager();
					}

					@Override
					public boolean isOnShow() {
						return bindingContext.isOnShow();
					}

					@Override
					public void hideView() {
						bindingContext.hideView();
					}
				}, fieldName, params);
				binding.init(runtimeContext);
			}
			binding.activate(vmodel);
			
		}

		@Override
		public void notifyDataChanged(ValueChangedEvent... events) {
			if(this.binding != null){
				this.binding.notifyDataChanged(events);
			}
			
		}

		@Override
		public void activate(IView model) {
			createNActivateBinding(model);
		}

		@Override
		public void deactivate() {
			if(this.binding != null){
				this.binding.deactivate();
			}
		}

		@Override
		public void destroy() {
			if(this.binding != null){
				this.binding.destroy();
				this.binding = null;
			}
		}

		@Override
		public void init(IWorkbenchRTContext ctx) {
		}

		@Override
		public Object getUIControl() {
			return view;
		}

		@Override
		public void updateModel() {
			if(this.binding != null){
				this.binding.updateModel();
			}
		}

		@Override
		public void refresh() {
			if(this.binding != null){
				this.binding.refresh();
			}
		}

//		@Override
//		public IUIComponent getValueModel() {
//			return this.binding != null ? this.binding.getValueModel() : null;
//		}
//
//		@Override
//		public View getViewModel() {
//			return this.view;
//		}
	}
	
	private final int layoutResourceId;
	private final IAndroidBindingContext bindingContext;
	private final String viewId;
	private List<IBinding<IView>> bindings = new ArrayList<IBinding<IView>>();
	private Map<String,IFieldBinding> fieldBindings = new HashMap<String,IFieldBinding>();
//	private Map<String, FieldBindingCreator> bindingCreators = new HashMap<String, FieldBindingCreator>();
//	private Map<String, IBinding> bindings = new HashMap<String, IBinding>();
	private View layoutView;
	private Map<String, IMenuHandler> menuHandlers;
	private List<IMenuAdaptor> menuAdaptors;
	private IView model;
	private IWorkbenchRTContext runtimeContext;
//	private boolean onShow;
	private IViewCreationCallback callback = new IViewCreationCallback() {

		@Override
		public void onViewCreated(final View view, Context context,
				AttributeSet attrSet) {
//			IFieldBinderManager mgr = runtimeContext.getWorkbenchManager().getFieldBinderManager(Context.class);
			IEventBinderManager eventBinderMgr = bindingContext.getWorkbenchManager().getEventBinderManager();
//			@SuppressWarnings("unchecked")
//			IFieldBinder<Context,View> binder = (IFieldBinder<Context,View>)mgr.getFieldBinder(view.getClass());
			Map<String, String> map = parse(attrSet);
			String val = map != null ? map.get(IAndroidBinding.BINDING_FIELD_NAME) : null;
			if(val != null){
				if(log.isTraceEnabled()){
					log.trace("Found field binding :"+val+" of view :"+view);
				}
				HashMap<String, String> params = new HashMap<String, String>();
				HashMap<String, String> events = new HashMap<String, String>();
				int cnt = attrSet.getAttributeCount();
				for (int i = 0; i < cnt; i++) {
					String name = StringUtils.trimToNull(attrSet.getAttributeName(i));
					String value = StringUtils.trimToNull(attrSet.getAttributeValue(IAndroidBinding.BINDING_NAMESPACE, name));
					if(value != null){
						if(name.startsWith("on_")&&(name.length() > 4)){
							String event = name.substring(3);
							events.put(event, value);
							if(log.isTraceEnabled()){
								log.trace("Found event binding :"+event+" of view :"+view);
							}

						}else{
							params.put(name, value);
						}
					}
				}
				if(!"*".equals(val)){
					FieldBindingCreator binding = new FieldBindingCreator(view, val, params);
					bindings.add(binding);
					fieldBindings.put(val, binding);
				}
				if(events.size() > 0){
					for (String evtType : events.keySet()) {
						String cmdName = events.get(evtType);
						IEventBinder eBinder = eventBinderMgr.getFieldBinder(evtType);
						if(eBinder != null){
							IBinding<IView> eBinding = eBinder.createBinding(new IAndroidBindingContext() {
								
								@Override
								public Context getUIContext() {
									return bindingContext.getUIContext();
								}
								
								@Override
								public View getBindingControl() {
									return view;
								}

								@Override
								public IWorkbenchManager getWorkbenchManager() {
									return bindingContext.getWorkbenchManager();
								}

								@Override
								public boolean isOnShow() {
									return bindingContext.isOnShow();
								}

								@Override
								public void hideView() {
									bindingContext.hideView();
								}
							}, val, cmdName, params);
							bindings.add(eBinding);
						}
					}
				}
			}else{
				if(map != null){
					view.setTag(BING_ATTRS_TAG_ID, map);
				}else{
					view.setTag(BING_ATTRS_TAG_ID, Collections.EMPTY_MAP);
				}
			}
			if(map != null){
				String menuAdaptorClass = map.get("menuAdaptor");
				if(menuAdaptorClass != null){
					if(menuAdaptorClass.charAt(0) == '.'){
						menuAdaptorClass = AppUtils.getFramework().getApplicationId()+menuAdaptorClass;
					}
					try {
						IMenuAdaptor adaptor = (IMenuAdaptor)Class.forName(menuAdaptorClass).newInstance();
						adaptor.init(bindingContext, view);
						if(menuAdaptors == null){
							menuAdaptors = new ArrayList<IMenuAdaptor>();
						}
						menuAdaptors.add(adaptor);
					} catch (Throwable e) {
						log.error("Failed to create menu adptor from :"+menuAdaptorClass, e);
					}
				}
			}
		}

	};
	
    public Map<String, String> parse(AttributeSet attributeSet) {
	Map<String, String> bindingAttributes = null;

	for (int i = 0; i < attributeSet.getAttributeCount(); i++) {
	    String attributeName = attributeSet.getAttributeName(i);
	    String attributeValue = StringUtils.trimToNull(attributeSet.getAttributeValue(BINDING_NAMESPACE, attributeName));

	    if (attributeValue != null){
	    	if(bindingAttributes == null){
	    		bindingAttributes = new HashMap<String, String>();
	    	}
	    	bindingAttributes.put(attributeName, attributeValue);
	    }
	}

	return bindingAttributes;
    }

	
	public AndroidViewBinding(IBindingContext ctx,int layoutResId,String viewId){
		this.layoutResourceId = layoutResId;
		this.bindingContext = (IAndroidBindingContext)ctx;
		this.layoutView = initView();
		this.viewId = viewId;
	}

	
	protected View initView() {
		if(log.isTraceEnabled()){
			log.trace("Going to inflater layout :"+this.layoutResourceId);
		}
		LayoutInflater inflater = ((LayoutInflater)this.bindingContext.getUIContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).cloneInContext(this.bindingContext.getUIContext());
		inflater.setFactory(new BindableViewFactory(this.callback,inflater));
		return inflater.inflate(this.layoutResourceId,null);
	}
		
	@Override
	public void notifyDataChanged(final ValueChangedEvent... events) {
//		AppUtils.runOnUIThread(new Runnable() {
//			
//			@Override
//			public void run() {
//				for (IBinding<IView> b : bindings) {
//					b.notifyDataChanged(events);
//				}
//			}
//		});
	}
	
	@Override
	public void init(IWorkbenchRTContext ctx) {
		this.runtimeContext = ctx;
		for (IBinding<IView> b : bindings) {
			b.init(ctx);
		}
	}
	
//	@Override
//	public View getViewModel() {
//		return layoutView;
//	}
//	
//	@Override
//	public IView getValueModel() {
//		return this.model;
//	}
	
	@Override
	public void destroy() {
		if(this.menuAdaptors != null){
			for (IMenuAdaptor adaptor : this.menuAdaptors) {
				adaptor.destroy();
			}
			this.menuAdaptors.clear();
			this.menuAdaptors = null;
		}
		for (IBinding<IView> b : bindings) {
			b.destroy();
		}
		this.runtimeContext = null;
	}
	
	@Override
	public void deactivate() {
		for (IBinding<IView> b : bindings) {
			b.deactivate();
		}
		this.model.doUnbinding(this);
		this.model = null;

	}
	
	@Override
	public void activate(IView model) {
		this.model = model;
		model.doBinding(this);
		for (IBinding<IView> b : bindings) {
			b.activate(model);
		}
	}


	@Override
	public Object getUIControl() {
		return this.layoutView;
	}


	@Override
	public IFieldBinding getFieldBinding(String fieldName) {
		return this.fieldBindings.get(fieldName);
	}
	
	public void registerMenuHandler(String menuId, IMenuHandler handler){
		if(this.menuHandlers == null){
			this.menuHandlers = new HashMap<String, IMenuHandler>();
		}
		this.menuHandlers.put(menuId, handler);
	}
	
	public void unregisterMenuHandlr(String menuId,IMenuHandler handler){
		IMenuHandler h = getMenuHandler(menuId);
		if(h == handler){
			this.menuHandlers.remove(menuId);
		}
	}
	
	public IMenuHandler getMenuHandler(String menuId){
		IMenuHandler handler = null;
		if(this.menuAdaptors != null){
			for (IMenuAdaptor adapter : this.menuAdaptors) {
				if((handler = adapter.getMenuHandler(menuId)) != null){
					return handler;
				}
			}
		}
		return this.menuHandlers != null ? this.menuHandlers.get(menuId) : null;
	}


	@Override
	public void refresh() {
		for (IBinding<IView> b : bindings) {
			b.refresh();
		}
	}


	@Override
	public boolean isOnShow() {
		return this.bindingContext.isOnShow();
	}


	@Override
	public String getBindingViewId() {
		return this.viewId;
	}


	@Override
	public void hide() {
		if(isOnShow()){
			this.bindingContext.hideView();
		}
	}

}
