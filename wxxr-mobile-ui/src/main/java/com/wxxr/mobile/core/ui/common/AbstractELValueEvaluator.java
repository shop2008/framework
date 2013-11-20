/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.List;

import com.wxxr.javax.el.ELManager;
import com.wxxr.javax.el.ValueExpression;
import com.wxxr.mobile.core.ui.api.IEvaluatorContext;
import com.wxxr.mobile.core.ui.api.IValueConvertor;
import com.wxxr.mobile.core.ui.api.IValueEvaluator;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;

/**
 * @author neillin
 *
 */
public class AbstractELValueEvaluator<T,V> implements IValueEvaluator<T> {
	
	protected final IEvaluatorContext elm;
	protected final String express;
	protected final ValueExpression valueExpr;
	protected final Class<V> expectedType;
	protected IValueConvertor<V, T> convertor;
	
	public AbstractELValueEvaluator(IEvaluatorContext elMgr, String expr, Class<V> clazz){
		this.elm = elMgr;
		this.express = expr;
		this.expectedType = clazz;
		this.valueExpr = ELManager.getExpressionFactory().createValueExpression(this.elm.getELContext(), this.express, expectedType);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IValueEvaluator#doEvaluate()
	 */
	@Override
	public T doEvaluate() {
		Object obj = this.valueExpr.getValue(this.elm.getELContext());
		if(convertor != null){
			return convertor.toTargetTypeValue(expectedType.cast(obj));
		}else{
			return (T)obj;
		}
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

	/**
	 * @return the convertor
	 */
	public IValueConvertor<V, T> getConvertor() {
		return convertor;
	}

	/**
	 * @param convertor the convertor to set
	 */
	public AbstractELValueEvaluator<T, V> setConvertor(IValueConvertor<V, T> convertor) {
		this.convertor = convertor;
		return this;
	}


}
