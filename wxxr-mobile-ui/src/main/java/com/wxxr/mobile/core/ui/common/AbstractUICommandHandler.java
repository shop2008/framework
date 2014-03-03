/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.wxxr.javax.validation.ConstraintViolation;
import com.wxxr.javax.validation.Validator;
import com.wxxr.mobile.core.command.annotation.ConstraintLiteral;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.INavigationDescriptor;
import com.wxxr.mobile.core.ui.api.IProgressGuard;
import com.wxxr.mobile.core.ui.api.IUICommandHandler;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.ValidationError;
import com.wxxr.mobile.core.ui.api.ValidationException;

/**
 * @author neillin
 *
 */
public abstract class AbstractUICommandHandler implements IUICommandHandler {

	private LinkedList<INavigationDescriptor> navs;
	private IProgressGuard progressGuard;
	private List<ConstraintLiteral> constraints;
	
	public AbstractUICommandHandler addConstraint(ConstraintLiteral constraint){
		if(constraint == null){
			throw new IllegalArgumentException("Invalid constraint : NULL !");
		}
		if(this.constraints == null){
			this.constraints = new LinkedList<ConstraintLiteral>();
		}
		if(!this.constraints.contains(constraint)){
			this.constraints.add(constraint);
		}
		return this;
	}
	
	public void validateUserInput() throws ValidationException {
		
	}
	
	protected void validateBean(IWorkbenchRTContext ctx,Object bean,String message, Class<?>... groups) throws ValidationException{
		Validator validator = ctx.getWorkbenchManager().getValidator();
		if(validator != null){
			Set<ConstraintViolation<Object>> errs = null;
			if((groups != null)&&(groups.length > 0)){
				for (Class<?> grp : groups) {
					errs = validator.validate(bean, grp);
					if((errs != null)&&(errs.size() > 0)){
						break;
					}
				}
			}else{
				errs = validator.validate(bean);
			}
			if((errs != null)&&(errs.size() > 0)){
				int size = errs.size();
				ValidationError[] vErrs  = new ValidationError[size];
				int cnt = 0;
				for (ConstraintViolation<?> cerr : errs) {
					String property = cerr.getPropertyPath() != null ? cerr.getPropertyPath().toString() : null;
					vErrs[cnt] = new ValidationError(cerr.getMessageTemplate(), cerr.getMessage(),property);
					cnt++;			
				}
				ValidationException ex = new ValidationException(message);
				ex.setDetals(vErrs);
				throw ex;
			}
		}
	}
	
	protected void updateFields(IWorkbenchRTContext ctx, IView view, String message, String... fields) throws ValidationException {
		ArrayList<ValidationError> errors = new ArrayList<ValidationError>();
		for (String fname : fields) {
			IDataField<?> field = view.getChild(fname, IDataField.class);
			if(field != null){
				field.updateModel();
				ValidationError[] vErrs  = field.getValidationErrors();
				if(vErrs != null){
					for (ValidationError err : vErrs) {
						errors.add(err);
					}
				}
			}
		}
		if(errors.size() > 0){
			if(message == null){
				message = "resourceId:message/field_validation_errors";
			}
			ValidationException ex = new ValidationException(message);
			ex.setDetals(errors.toArray(new ValidationError[0]));
			throw ex;
		}
	}
	
	
	
	public AbstractUICommandHandler removeConstraint(ConstraintLiteral constraint){
		if(this.constraints != null){
			this.constraints.remove(constraint);
		}
		return this;
	}
	
	
	public ConstraintLiteral[] getConstraints() {
		return this.constraints != null ? this.constraints.toArray(new ConstraintLiteral[0]) : null;
	}

	public INavigationDescriptor[] getNavigations() {
		return this.navs != null && this.navs.isEmpty() == false ? 
				this.navs.toArray(new INavigationDescriptor[this.navs.size()]) :
				new INavigationDescriptor[0];
	}
	
	public AbstractUICommandHandler addNavigation(INavigationDescriptor nav){
		if(this.navs == null){
			this.navs = new LinkedList<INavigationDescriptor>();
		}
		if(!this.navs.contains(nav)){
			this.navs.addLast(nav);
		}
		return this;
	}
	
	public AbstractUICommandHandler removeNavigation(INavigationDescriptor nav){
		if(this.navs != null){
			this.navs.remove(nav);
		}
		return this;
	}

	/**
	 * @return the progressGuard
	 */
	public IProgressGuard getProgressGuard() {
		return progressGuard;
	}

	/**
	 * @param progressGuard the progressGuard to set
	 */
	public void setProgressGuard(IProgressGuard progressGuard) {
		this.progressGuard = progressGuard;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IUICommandHandler#doProcess(com.wxxr.mobile.core.ui.api.InputEvent)
	 */
	@Override
	public Object doProcess(InputEvent event) {
		return execute(ExecutionStep.PROCESS,event,null);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IUICommandHandler#doNavigation(com.wxxr.mobile.core.ui.api.InputEvent, java.lang.Object)
	 */
	@Override
	public Object doNavigation(InputEvent event, Object result) {
		return execute(ExecutionStep.NAVIGATION, event, result);
	}
	
	protected Object execute(ExecutionStep step, InputEvent event, Object result) {
		if(step == ExecutionStep.PROCESS){
			return execute(event);
		}else{
			return result;
		}
	}
	
	protected Object execute(InputEvent event) {
		return null;
	}

}
