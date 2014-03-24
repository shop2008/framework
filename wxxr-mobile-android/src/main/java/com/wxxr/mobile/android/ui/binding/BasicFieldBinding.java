/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;

import java.util.Map;

import android.view.View;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.core.ui.common.AbstractFieldBinding;
import com.wxxr.mobile.core.ui.common.AttributeKeys;

/**
 * @author neillin
 *
 */
public class BasicFieldBinding extends AbstractFieldBinding {
	
	
	public BasicFieldBinding(IAndroidBindingContext ctx, String fieldName,Map<String,String> attrSet){
		super(attrSet);
		super.setUIControl(ctx.getBindingControl());
		super.setFieldName(fieldName);
//		super.setBindingAttrs(attrSet);
		super.setBindingContext(ctx);
	}
	
	/**
	 * 
	 */
	protected void setUIEnabled() {
		Boolean bool = getViewModel().getChild(getFieldName()).getAttribute(AttributeKeys.enabled);
		boolean val = bool != null ? bool.booleanValue() : false;
		((View)getUIControl()).setEnabled(val);
	}
	
	protected void setUIVisibility() {
		boolean val = isVisible();
		if(val){
			((View)getUIControl()).setVisibility(View.VISIBLE);
		}else{
			((View)getUIControl()).setVisibility(View.GONE);
		}
	}
	


}
