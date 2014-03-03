/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.Set;

import com.wxxr.javax.validation.ConstraintViolation;
import com.wxxr.javax.validation.Validator;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IDomainValueModel;
import com.wxxr.mobile.core.ui.api.IEvaluatorContext;
import com.wxxr.mobile.core.ui.api.IValueConvertor;
import com.wxxr.mobile.core.ui.api.ValidationError;

/**
 * @author neillin
 *
 */
public class ELDomainValueModel<T,V> extends AbstractELValueEvaluator<T,V> implements IDomainValueModel<T>{
	private static final Trace log = Trace.getLogger(ELDomainValueModel.class);
	
	private final IDataField<T> field;
	private Boolean updatable;
	private boolean updateAsync = false;
	private int silentInSeconds = 1;
	

	public ELDomainValueModel(IEvaluatorContext elMgr, String expr, IDataField<T> comp, Class<V> expectedType) {
		super(elMgr, expr, expectedType);
		this.field = comp;
//		addProgressFieldName(this.field.getName());
	}


	@Override
	public ValidationError[] updateDomainValue(Object value) {
		if(!isUpdatable()){
			throw new IllegalStateException("Value is readonly !");
		}
		if(convertor != null){
			value = convertor.toSourceTypeValue(convertor.getTargetType().cast(value));
		}
		if(this.valueExpr.isReadOnly(this.elm.getELContext())){
			throw new IllegalStateException("Binding expression on field :"+field.getName()+" is read only, cannot be updated :["+this.valueExpr.getExpressionString()+"]");
		}
		this.valueExpr.setValue(this.elm.getELContext(), value);
		Validator validator = this.elm.getUIContext().getWorkbenchManager().getValidator();
		if(validator != null){
			String beanName = this.valueExpr.getReferringBeanNames().get(0);
			String propertyName = this.valueExpr.getReferringPropertyNames().get(0);
			Set<?> errors  = validator.validateProperty(this.elm.getBean(beanName), propertyName);
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
		return null;
	}

	@Override
	public boolean isUpdatable() {
		if(this.updatable == null){
			this.updatable = this.valueExpr.isReadOnly(this.elm.getELContext()) == false;
		}
		return this.updatable.booleanValue();
	}



//	/* (non-Javadoc)
//	 * @see com.wxxr.mobile.core.ui.common.ELBeanValueEvaluator#doEvaluate()
//	 */
//	@Override
//	public T doEvaluate() {
//		if(log.isDebugEnabled()){
//			log.debug("Going to evaluate expr :["+express+"] for data field:/["+field+"]");
//		}
//
//		if(!updateAsync){
//			return updateFieldValue(null);
//		}else{
//			final AsyncFuture<Object> future = new AsyncFuture<Object>();
//			KUtils.invokeLater(new Runnable() {
//
//				@Override
//				public void run() {
//					field.setAttribute(AttributeKeys.valueUpdating, future);
//					updateFieldValue(future.getInternalCallback());
//				}
//			});
//			try {
//				return future.get(1, TimeUnit.SECONDS);
//			}catch(InterruptedException e){
//				return null;
//			}catch(ExecutionException e){
//				return null;
//			}catch(TimeoutException e){
//				this.field.setAttribute(AttributeKeys.valueUpdating, future);
//			}
//			return null;
//		}
//	}


//	/**
//	 * @return
//	 */
//	protected T updateFieldValue(IAsyncCallback<Object> cb) {
//		try {
//			T val = getValue();
//			this.field.setValue(val);
//			if(updateAsync){
//				this.field.removeAttribute(AttributeKeys.valueUpdatedFailed);
//				this.field.removeAttribute(AttributeKeys.valueUpdating);
//			}
//			if(cb != null){
//				cb.success(val);
//			}
//			return val;
//		}catch(Throwable t){
//			log.warn("Failed to update value of data field :"+ this.field.getName()+" from express :"+this.valueExpr.getExpressionString(), t);
//			if(updateAsync){
//				this.field.setAttribute(AttributeKeys.valueUpdatedFailed, t);
//				this.field.removeAttribute(AttributeKeys.valueUpdating);
//			}
//			IView view = ModelUtils.getView(field);
//			if(view instanceof ViewBase){
//				((ViewBase)view).handleStartupException(t);
//			}
//			if(cb != null){
//				cb.failed(t);
//			}
//			return this.field.getValue();
//		}
//	}
//

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.AbstractELValueEvaluator#setConvertor(com.wxxr.mobile.core.ui.api.IValueConvertor)
	 */
	@Override
	public ELDomainValueModel<T, V> setConvertor(
			IValueConvertor<V, T> convertor) {
		super.setConvertor(convertor);
		return this;
	}


	/**
	 * @return the silentInSeconds
	 */
	public int getSilentInSeconds() {
		return silentInSeconds;
	}


	/**
	 * @param silentInSeconds the silentInSeconds to set
	 */
	public void setSilentInSeconds(int silentInSeconds) {
		this.silentInSeconds = silentInSeconds;
	}


	/**
	 * @return the updateAsync
	 */
	public boolean isUpdateAsync() {
		return updateAsync;
	}


	/**
	 * @param updateAsync the updateAsync to set
	 */
	public ELDomainValueModel<T,V> setUpdateAsync(boolean updateAsync) {
		this.updateAsync = updateAsync;
		if(this.updateAsync){
			addProgressFieldName(this.field.getName());
		}
		return this;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "value model of field=" + field.getName() + ", express ="
				+ this.valueExpr.getExpressionString()+", refering beans :["+this.valueExpr.getReferringBeanNames()
				+"], refering properties :["+this.valueExpr.getReferringPropertyNames()+ "]";
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IValueEvaluator#updateLocalValue(java.lang.Object)
	 */
	@Override
	public void updateLocalValue(T val) {
		if(log.isInfoEnabled()){
			log.info("Going to update local value to data field value of :"+field.getName()+", value :["+val+"]");
		}
		this.field.setValue(val);
	}
	
	
}
