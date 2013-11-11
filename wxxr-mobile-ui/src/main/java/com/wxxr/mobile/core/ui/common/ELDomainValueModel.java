/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import com.wxxr.javax.el.ELManager;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IDomainValueModel;
import com.wxxr.mobile.core.ui.api.ValidationError;

/**
 * @author neillin
 *
 */
public class ELDomainValueModel<T> extends ELBeanValueEvaluator<T> implements IDomainValueModel<T>{
	
	private final IDataField<T> field;
	
	public ELDomainValueModel(ELManager elMgr, String expr, IDataField<T> comp, Class<T> expectedType) {
		super(elMgr, expr, expectedType);
		this.field = comp;
	}


	@Override
	public ValidationError[] updateValue(Object value) {
		if(!isUpdatable()){
			throw new IllegalStateException("Value is readonly !");
		}
		this.valueExpr.setValue(this.elm.getELContext(), value);
		return null;
	}

	@Override
	public boolean isUpdatable() {
		return this.valueExpr.isReadOnly(this.elm.getELContext()) == false;
	}

	@Override
	public T getValue() {
		return super.doEvaluate();
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.ELBeanValueEvaluator#doEvaluate()
	 */
	@Override
	public T doEvaluate() {
		T val = getValue();
		this.field.setValue(val);
		return val;
	}
}
