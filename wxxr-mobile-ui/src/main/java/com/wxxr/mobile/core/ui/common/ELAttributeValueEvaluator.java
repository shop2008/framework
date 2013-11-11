/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import com.wxxr.javax.el.ELManager;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;

/**
 * @author neillin
 *
 */
public class ELAttributeValueEvaluator extends ELBeanValueEvaluator<Object> {
	
	private final String key;
	private final IUIComponent component;
	
	public ELAttributeValueEvaluator(ELManager elMgr, String expr, String key, IUIComponent component){
		super(elMgr,expr,Object.class);
		this.key = key;
		this.component = component;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IValueEvaluator#doEvaluate()
	 */
	@Override
	public Object doEvaluate() {
		Object val = super.doEvaluate();
		AttributeKeys.updateAttribute(key, component, val);
		return val;
	}

}
