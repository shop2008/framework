/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.Set;

import com.wxxr.javax.validation.ConstraintViolation;
import com.wxxr.javax.validation.Validator;
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
		if(this.valueExpr.isReadOnly(this.elm.getELContext())){
			throw new IllegalStateException("Binding expression on field :"+field.getName()+" is read only, cannot be updated :["+this.valueExpr.getExpressionString()+"]");
		}
		Validator validator = this.elm.getUIContext().getWorkbenchManager().getValidator();
		if(validator != null){
			String beanName = this.valueExpr.getReferringBeanNames().get(0);
			String propertyName = this.valueExpr.getReferringPropertyNames().get(0);
			Set<?> errors  = validator.validateValue(this.elm.getBean(beanName).getClass(), propertyName, value);
			if((errors != null)&&(errors.size() > 0)){
				int size = errors.size();
				ValidationError[] vErrs  = new ValidationError[size];
				int cnt = 0;
				for (Object err : errors) {
					ConstraintViolation<?> cerr = (ConstraintViolation<?>)err;
					vErrs[cnt] = new ValidationError(cerr.getMessageTemplate(), cerr.getMessage(), field.getName());
					cnt++;			
				}
				return vErrs;
			}
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
