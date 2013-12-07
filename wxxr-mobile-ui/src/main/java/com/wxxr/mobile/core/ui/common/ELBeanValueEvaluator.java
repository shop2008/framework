/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import com.wxxr.javax.el.ELManager;
import com.wxxr.javax.el.ValueExpression;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.IEvaluatorContext;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author neillin
 *
 */
public class ELBeanValueEvaluator<T> extends AbstractELValueEvaluator<T, T> {
	private static final Trace log=Trace.getLogger(ELBeanValueEvaluator.class);
	
	protected final String enabledWhenExpress;
	protected final ValueExpression enabledWhenExpr;
	private T defaultValue;

	/**
	 * @return the defaultValue
	 */
	public T getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(T defaultValue) {
		this.defaultValue = defaultValue;
	}

	public ELBeanValueEvaluator(IEvaluatorContext elMgr, String expr, String enabledWhen,Class<T> clazz) {
		super(elMgr, expr, clazz);
		this.enabledWhenExpress = StringUtils.trimToNull(enabledWhen);
		if(this.enabledWhenExpress != null){
			this.enabledWhenExpr = ELManager.getExpressionFactory().createValueExpression(this.elm.getELContext(), this.enabledWhenExpress, Boolean.class);
		}else{
			this.enabledWhenExpr = null;
		}
	}

	private boolean checkEnableCondition() {
		if(this.enabledWhenExpr == null){
			return true;
		}
		Boolean val = (Boolean)this.enabledWhenExpr.getValue(this.elm.getELContext());
		return val.booleanValue();
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.AbstractELValueEvaluator#doEvaluate()
	 */
	@Override
	public T doEvaluate() {
		if(log.isDebugEnabled()){
			log.debug("Going to evaluate express :["+express+"], expected type :"+expectedType.getCanonicalName());
		}
		if(checkEnableCondition()){
			return super.doEvaluate();
		}else{
			return this.defaultValue;
		}
	}
}
