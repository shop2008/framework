/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;

import java.util.Map;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.core.ui.api.IViewGroup;
import com.wxxr.mobile.core.ui.common.AttributeKeys;

/**
 * @author neillin
 *
 */
public class ViewGroupFieldBinding extends BasicFieldBinding {

	public ViewGroupFieldBinding(IAndroidBindingContext ctx, String fieldName,
			Map<String, String> attrSet) {
		super(ctx, fieldName, attrSet);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#updateUI(boolean)
	 */
	@Override
	protected void updateUI(boolean recursive) {
		super.updateUI(recursive);
		IViewGroup vg = (IViewGroup)getField();
		if(vg.hasAttribute(AttributeKeys.visible)&&(vg.getAttribute(AttributeKeys.visible) == false)){
			return;
		}
		boolean backable =true;
		String activeViewId = vg.getActiveViewId();
		if(activeViewId == null){
			activeViewId = vg.getDefaultViewId();
			backable = false;
		}
		if(activeViewId != null){
			vg.activateView(activeViewId,backable);
		}
	}

}
