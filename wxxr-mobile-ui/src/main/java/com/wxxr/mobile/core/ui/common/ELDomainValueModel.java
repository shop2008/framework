/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IDomainValueModel;
import com.wxxr.mobile.core.ui.api.IEvaluatorContext;
import com.wxxr.mobile.core.ui.api.ValidationError;

/**
 * @author neillin
 *
 */
public class ELDomainValueModel<T,V> extends AbstractELValueEvaluator<T,V> implements IDomainValueModel<T>{
	
	private final IDataField<T> field;
	
	public ELDomainValueModel(IEvaluatorContext elMgr, String expr, IDataField<T> comp, Class<V> expectedType) {
		super(elMgr, expr, expectedType);
		this.field = comp;
	}


	@Override
	public ValidationError[] updateValue(Object value) {
		if(!isUpdatable()){
			throw new IllegalStateException("Value is readonly !");
		}
		if(convertor != null){
			value = convertor.toSourceTypeValue(convertor.getTargetType().cast(value));
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
