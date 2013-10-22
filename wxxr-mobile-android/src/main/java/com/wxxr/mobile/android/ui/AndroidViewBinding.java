/**
 * 
 */
package com.wxxr.mobile.android.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IBindingContext;
import com.wxxr.mobile.core.ui.api.IEventBinder;
import com.wxxr.mobile.core.ui.api.IEventBinderManager;
import com.wxxr.mobile.core.ui.api.IFieldBinder;
import com.wxxr.mobile.core.ui.api.IFieldBinderManager;
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
public class AndroidViewBinding implements IAndroidBinding<IView>{

	private static final Trace log = Trace.register(AndroidViewBinding.class);
	
	private class FieldBindingCreator implements IBinding<IView>{
		private final View view;
		private final String fieldName;
		private final Map<String, String> params;
		private IBinding<IUIComponent> binding;
		
		FieldBindingCreator(View v, String name, Map<String, String> map){
			this.view = v;
			this.fieldName = name;
			this.params = map;
		}
		
		public void createNActivateBinding(IView vmodel){
			IUIComponent field = vmodel.getChild(fieldName);
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
				}, fieldName, params);
				binding.init(runtimeContext);
			}
			binding.activate(field);
			
		}

		@Override
		public void notifyDataChanged(ValueChangedEvent event) {
			if(this.binding != null){
				this.binding.notifyDataChanged(event);
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
				this.binding = null;
			}
		}

		@Override
		public void destroy() {
			
		}

		@Override
		public void init(IWorkbenchRTContext ctx) {
		}

		@Override
		public Object getUIControl() {
			return view;
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
	private List<IBinding<IView>> bindings = new ArrayList<IBinding<IView>>();
//	private Map<String, FieldBindingCreator> bindingCreators = new HashMap<String, FieldBindingCreator>();
//	private Map<String, IBinding> bindings = new HashMap<String, IBinding>();
	private View layoutView;
//	private IUIComponent model;
	private IWorkbenchRTContext runtimeContext;
	private IViewCreationCallback callback = new IViewCreationCallback() {
				
		@Override
		public void onViewCreated(final View view, Context context,
				AttributeSet attrSet) {
//			IFieldBinderManager mgr = runtimeContext.getWorkbenchManager().getFieldBinderManager(Context.class);
			IEventBinderManager eventBinderMgr = bindingContext.getWorkbenchManager().getEventBinderManager();
//			@SuppressWarnings("unchecked")
//			IFieldBinder<Context,View> binder = (IFieldBinder<Context,View>)mgr.getFieldBinder(view.getClass());
			String val = StringUtils.trimToNull(attrSet.getAttributeValue(IAndroidBinding.BINDING_NAMESPACE, IAndroidBinding.BINDING_FIELD_NAME));
			if(val != null){
				if(log.isDebugEnabled()){
					log.debug("Found field binding :"+val+" of view :"+view);
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
							if(log.isDebugEnabled()){
								log.debug("Found event binding :"+event+" of view :"+view);
							}

						}else{
							params.put(name, value);
						}
					}
				}
				bindings.add(new FieldBindingCreator(view, val, params));
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
							}, val, cmdName, params);
							bindings.add(eBinding);
						}
					}
				}
			}else{
				Map<String, String> map = parse(attrSet);
				if(map != null){
					view.setTag(BING_ATTRS_TAG_ID, map);
				}else{
					view.setTag(BING_ATTRS_TAG_ID, Collections.EMPTY_MAP);
				}
			}
			
		}

	};
	
    public Map<String, String> parse(AttributeSet attributeSet) {
	Map<String, String> bindingAttributes = null;

	for (int i = 0; i < attributeSet.getAttributeCount(); i++) {
	    String attributeName = attributeSet.getAttributeName(i);
	    String attributeValue = attributeSet.getAttributeValue(BINDING_NAMESPACE, attributeName);

	    if (attributeValue != null){
	    	if(bindingAttributes == null){
	    		bindingAttributes = new HashMap<String, String>();
	    	}
	    	bindingAttributes.put(attributeName, attributeValue);
	    }
	}

	return bindingAttributes;
    }

	
	public AndroidViewBinding(IBindingContext ctx,int layoutResId){
		this.layoutResourceId = layoutResId;
		this.bindingContext = (IAndroidBindingContext)ctx;
		this.layoutView = initView();
	}

	
	protected View initView() {
		if(log.isDebugEnabled()){
			log.debug("Going to inflater layout :"+this.layoutResourceId);
		}
		LayoutInflater inflater = ((LayoutInflater)this.bindingContext.getUIContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).cloneInContext(this.bindingContext.getUIContext());
		inflater.setFactory(new BindableViewFactory(this.callback,inflater));
		return inflater.inflate(this.layoutResourceId,null);
	}
		
	@Override
	public void notifyDataChanged(ValueChangedEvent event) {
		for (IBinding<IView> b : bindings) {
			b.notifyDataChanged(event);
		}
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
//		this.model = null;

	}
	
	@Override
	public void activate(IView model) {
//		this.model = model;
		for (IBinding<IView> b : bindings) {
			b.activate(model);
		}
	}


	@Override
	public Object getUIControl() {
		return this.layoutView;
	}
	
}
