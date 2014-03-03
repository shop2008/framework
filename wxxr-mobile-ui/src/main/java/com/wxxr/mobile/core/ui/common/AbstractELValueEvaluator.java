/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.wxxr.javax.el.ELException;
import com.wxxr.javax.el.ELManager;
import com.wxxr.javax.el.ValueExpression;
import com.wxxr.mobile.core.async.api.AsyncFuture;
import com.wxxr.mobile.core.async.api.ExecAsyncException;
import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.async.api.ICancellable;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IEvaluatorContext;
import com.wxxr.mobile.core.ui.api.IValueConvertor;
import com.wxxr.mobile.core.ui.api.IValueEvaluator;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author neillin
 *
 */
public abstract class AbstractELValueEvaluator<T,V> implements IValueEvaluator<T> {
	private static final Trace log = Trace.getLogger(AbstractELValueEvaluator.class);
	
	private class EvaluatorCallback implements IAsyncCallback<Object> {

		private boolean cancelled;
		private ICancellable cancellable;
		
		@SuppressWarnings("unchecked")
		@Override
		public void success(final Object result) {
			if(log.isInfoEnabled()){
				log.info("Async evaluate return successfully of expression :"+express+", value :["+result+"]");
			}
			KUtils.runOnUIThread(new Runnable() {
				
				@Override
				public void run() {
					if(!cancelled){
						T value = null;
						if(convertor != null){
							value = convertor.toTargetTypeValue(expectedType.cast(result));
						}else{
							value = (T)result;
						}
						updateLocalValue(value);
					}
					clearUpdatingAttribute();
					callback = null;
				}
			});
		}

		@Override
		public void failed(final Throwable cause) {
			log.warn("Async evaluate failed ! exression :["+express+"]", cause);
			KUtils.runOnUIThread(new Runnable() {
				
				@Override
				public void run() {
					if(!cancelled){
						((ViewBase)elm.getView()).handleException(cause);
					}
					setupUpdatingFailedAttribute(cause);
					callback = null;
				}
			});
		}

		@Override
		public void cancelled() {
			if(log.isInfoEnabled()){
				log.info("Async evaluate of expression :["+express+"] cancelled !");
			}
			KUtils.runOnUIThread(new Runnable() {
				
				@Override
				public void run() {
					cancelled = true;
					clearUpdatingAttribute();
					callback = null;
				}
			});
		}

		@Override
		public void setCancellable(ICancellable cancellable) {
			this.cancellable = cancellable;
		}
		
		public void doCancel() {
			KUtils.runOnUIThread(new Runnable() {
				
				@Override
				public void run() {
					if((cancellable != null)&&(cancellable.isCancelled() == false)){
						cancellable.cancel();
					}
					cancelled = true;
					clearUpdatingAttribute();
					callback = null;
				}
			});
		}
		
	}
	protected final IEvaluatorContext elm;
	protected final String express;
	protected final ValueExpression valueExpr;
	protected final Class<V> expectedType;
	protected IValueConvertor<V, T> convertor;
	protected List<String> progressFieldNames;
	protected EvaluatorCallback callback;
	
	public AbstractELValueEvaluator(IEvaluatorContext elMgr, String expr, Class<V> clazz){
		this.elm = elMgr;
		this.express = expr;
		this.expectedType = clazz;
		this.valueExpr = ELManager.getExpressionFactory().createValueExpression(this.elm.getELContext(), this.express, expectedType);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IValueEvaluator#doEvaluate()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void doEvaluate() {
		if(this.callback != null){
//			this.callback.doCancel();
//			this.callback = null;
			return;
		}
		clearUpdatingAttribute();
		try {
			T value = null;
			Object obj = this.valueExpr.getValue(this.elm.getELContext());
			if(convertor != null){
				value = convertor.toTargetTypeValue(expectedType.cast(obj));
			}else{
				value = (T)obj;
			}
			updateLocalValue(value);
		}catch(ELException e){
			if(e.getCause() instanceof ExecAsyncException){
				ExecAsyncException asyncEx = (ExecAsyncException)e.getCause();
				evaluateAsync(asyncEx);
			}else{
				throw e;
			}
		}
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void evaluateAsync(ExecAsyncException ex){
		if(log.isInfoEnabled()){
			log.info("Caught ExecAsyncException, start to evaluate expression :["+express+"] asynchroniously !");
		}
		AsyncFuture contl = ex.getTaskControl();
		final AsyncFuture<Object> async = contl;
		if(this.callback != null){
			this.callback.doCancel();
			this.callback = null;
		}
		this.callback = new EvaluatorCallback();
		setupUpdatingAttribute(async);
		async.onResult(this.callback);
	}

	/**
	 * 
	 */
	protected void clearUpdatingAttribute() {
		if(this.progressFieldNames != null){
			for (String fname : this.progressFieldNames) {
				@SuppressWarnings("unchecked")
				IDataField<T> field = elm.getView().getChild(fname, IDataField.class);
				if(field != null){
					field.removeAttribute(AttributeKeys.valueUpdatedFailed);
					field.removeAttribute(AttributeKeys.valueUpdating);
				}
			}
		}
	}

	/**
	 * @param async
	 */
	protected void setupUpdatingAttribute(final AsyncFuture<Object> async) {
		if(this.progressFieldNames != null){
			for (String fname : this.progressFieldNames) {
				@SuppressWarnings("unchecked")
				IDataField<T> field = elm.getView().getChild(fname, IDataField.class);
				if(field != null){
					field.setAttribute(AttributeKeys.valueUpdating,async);
					field.removeAttribute(AttributeKeys.valueUpdatedFailed);
				}
			}
		}
	}

	/**
	 * @param async
	 */
	protected void setupUpdatingFailedAttribute(final Throwable cause) {
		if(this.progressFieldNames != null){
			for (String fname : this.progressFieldNames) {
				@SuppressWarnings("unchecked")
				IDataField<T> field = elm.getView().getChild(fname, IDataField.class);
				if(field != null){
					field.setAttribute(AttributeKeys.valueUpdatedFailed,cause);
					field.removeAttribute(AttributeKeys.valueUpdating);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IValueEvaluator#valueEffectedBy(com.wxxr.mobile.core.ui.api.ValueChangedEvent)
	 */
	@Override
	public boolean valueEffectedBy(ValueChangedEvent event) {
		List<String> beanNames = this.valueExpr.getReferringBeanNames();
		if(beanNames == null){
			return false;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "value evaluator express=" + express
				+ ", expectedType=" + expectedType + "]";
	}

	/**
	 * @return the progressFieldName
	 */
	public List<String> getProgressFieldName() {
		return progressFieldNames;
	}

	/**
	 * @param progressFieldNames the progressFieldName to set
	 */
	public void addProgressFieldName(String name) {
		if(StringUtils.isBlank(name)){
			throw new IllegalArgumentException("Invalid field name : NULL!");
		}
		if(this.progressFieldNames == null){
			this.progressFieldNames = new ArrayList<String>();
		}
		if(this.progressFieldNames.contains(name) == false) {
			this.progressFieldNames.add(name);
		}
	}
	
	public void addProgressFieldNames(String... names) {
		if(names != null){
			for (String name : names) {
				addProgressFieldName(name);
			}
		}
	}

	
	/**
	 * @param progressFieldNames the progressFieldName to set
	 */
	public boolean removeProgressFieldName(String name) {
		return this.progressFieldNames != null ? this.progressFieldNames.remove(name) : false;
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IValueEvaluator#valueEffectedByBean(java.lang.String)
	 */
	@Override
	public List<String> getDependingBeans() {
		List<String> beanNames = this.valueExpr.getReferringBeanNames();
		return beanNames == null ? Collections.EMPTY_LIST : beanNames;
	}


}
