/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import com.wxxr.mobile.core.ui.api.IEvaluatorContext;

/**
 * @author neillin
 *
 */
public class ELBeanValueEvaluator<T> extends AbstractELValueEvaluator<T, T> {

	public ELBeanValueEvaluator(IEvaluatorContext elMgr, String expr,Class<T> clazz) {
		super(elMgr, expr, clazz);
	}
}
