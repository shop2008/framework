/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IFieldBinding;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewBinding;

/**
 * @author neillin
 *
 */
public abstract class ModelUtils {
	
	public static IFieldBinding getFieldBinding(IUIComponent field) {
		IView v = getView(field);
		IBinding<IView> binding = v != null ? v.getBinding() : null;
		return binding != null ? ((IViewBinding)binding).getFieldBinding(field.getName()) : null;
	}
	
	public static IView getView(IUIComponent p) {
		if(p instanceof IView){
			return (IView)p;
		}else if(p != null){
			return getView(p.getParent());
		}else{
			return null;
		}
	}
	
	public static IPage getPage(IUIComponent p) {
		if(p instanceof IPage){
			return (IPage)p;
		}else if(p != null){
			return getPage(p.getParent());
		}else{
			return null;
		}
	}
	
	public static boolean isEquals(Object obj1, Object obj2){
		if(obj1 == obj2){
			return  true;
		}
		if((obj1 == null)&&(obj2 == null)){
			return true;
		}
		if((obj1 == null)||(obj2 == null)){
			return false;
		}
		return obj1.equals(obj2);
	}

}
