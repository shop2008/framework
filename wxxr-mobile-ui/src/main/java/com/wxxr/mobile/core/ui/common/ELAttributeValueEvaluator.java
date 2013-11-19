/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import com.wxxr.mobile.core.ui.api.IEvaluatorContext;
import com.wxxr.mobile.core.ui.api.IUIComponent;

/**
 * @author neillin
 *
 */
public class ELAttributeValueEvaluator extends AbstractELValueEvaluator<Object,Object> {
	
	private final String key;
	private final IUIComponent component;
	
	public ELAttributeValueEvaluator(IEvaluatorContext elMgr, String expr, String key, IUIComponent component){
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
