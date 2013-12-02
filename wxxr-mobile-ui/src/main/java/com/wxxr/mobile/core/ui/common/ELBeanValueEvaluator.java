/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.IEvaluatorContext;

/**
 * @author neillin
 *
 */
public class ELBeanValueEvaluator<T> extends AbstractELValueEvaluator<T, T> {
	private static final Trace log=Trace.getLogger(ELBeanValueEvaluator.class);
	
	public ELBeanValueEvaluator(IEvaluatorContext elMgr, String expr,Class<T> clazz) {
		super(elMgr, expr, clazz);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.AbstractELValueEvaluator#doEvaluate()
	 */
	@Override
	public T doEvaluate() {
		if(log.isDebugEnabled()){
			log.debug("Going to evaluate express :["+express+"], expected type :"+expectedType.getCanonicalName());
		}
		return super.doEvaluate();
	}
}
