/**
 * 
 */
package com.wxxr.mobile.android.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.View;

/**
 * @author neillin
 *
 */
public class BindableViewFactory implements Factory {

	private final LayoutInflater inflater;
	private final IViewCreationCallback callback;

	public BindableViewFactory(IViewCreationCallback cb,LayoutInflater inflater){
		this.inflater = inflater;
		this.callback = cb;
	}

	/* (non-Javadoc)
	 * @see android.view.LayoutInflater.Factory#onCreateView(java.lang.String, android.content.Context, android.util.AttributeSet)
	 */
	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		try {
			View view = this.inflater.createView(getViewNameFromLayoutTag(name), null, attrs);
			this.callback.onViewCreated(view, context, attrs);
//			IFieldBinderManager mgr = this.bindingContext.getServiceContext().getService(IFieldBinderManager.class);
//			IAndroidFieldBinder factory = mgr.getBindingStrategy(view.getClass());
//			String val = StringUtils.trimToNull(attrs.getAttributeValue(IAndroidBinding.BINDING_NAMESPACE, IAndroidBinding.BINDING_FIELD_NAME));
//			if(val != null){
//				IUIComponent valModel = this.bindingContext.getValueModel(val);
//				if(valModel != null){
//					factory.createBinding(bindingContext, view, valModel);
//				}
//			}
			return view;
	    } catch (InflateException e) {
            throw e;

        } catch (ClassNotFoundException e) {
            InflateException ie = new InflateException(attrs.getPositionDescription()
                    + ": Error inflating class " + name);
            ie.initCause(e);
            throw ie;

        } catch (Throwable e) {
            InflateException ie = new InflateException(attrs.getPositionDescription()
                    + ": Error inflating class " + name);
            ie.initCause(e);
            throw ie;
        }
	}


	public String getViewNameFromLayoutTag(String tagName) {
		StringBuilder nameBuilder = new StringBuilder();

		if ("View".equals(tagName) || "ViewGroup".equals(tagName))
			nameBuilder.append("android.view.");
		else if (!viewNameIsFullyQualified(tagName))
			nameBuilder.append("android.widget.");

		nameBuilder.append(tagName);
		return nameBuilder.toString();
	}

	private boolean viewNameIsFullyQualified(String name) {
		return name.contains(".");
	}

}
