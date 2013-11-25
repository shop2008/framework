/**
 * 
 */
package com.wxxr.mobile.core.command.api;

import java.lang.annotation.Annotation;

/**
 * @author neillin
 *
 */
public class RequiredNetNotAvailablexception extends
		CommandConstraintViolatedException {

	private static final long serialVersionUID = -4803874472598447075L;

	public RequiredNetNotAvailablexception(Annotation ann) {
		super(ann);
	}

}
