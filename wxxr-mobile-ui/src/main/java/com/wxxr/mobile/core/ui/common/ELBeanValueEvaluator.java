/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.List;

import com.wxxr.javax.el.ELManager;
import com.wxxr.javax.el.ValueExpression;
import com.wxxr.mobile.core.ui.api.IValueEvaluator;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;

/**
 * @author neillin
 *
 */
public class ELBeanValueEvaluator<T> implements IValueEvaluator<T> {
	
	protected final ELManager elm;
	protected final String express;
	protected final ValueExpression valueExpr;
	protected final Class<T> expectedType;
	
	public ELBeanValueEvaluator(ELManager elMgr, String expr, Class<T> clazz){
		this.elm = elMgr;
		this.express = expr;
		this.expectedType = clazz;
		this.valueExpr = ELManager.getExpressionFactory().createValueExpression(this.elm.getELContext(), this.express, this.expectedType);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IValueEvaluator#doEvaluate()
	 */
	@Override
	public T doEvaluate() {
		Object obj = this.valueExpr.getValue(this.elm.getELContext());
		return this.expectedType.cast(obj);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IValueEvaluator#valueEffectedBy(com.wxxr.mobile.core.ui.api.ValueChangedEvent)
	 */
	@Override
	public boolean valueEffectedBy(ValueChangedEvent event) {
		List<String> beanNames = this.valueExpr.getReferringBeanNames();
		if(beanNames == null){
			return true;
		}
		return beanNames.contains(event.getSourceName());
	}

}
