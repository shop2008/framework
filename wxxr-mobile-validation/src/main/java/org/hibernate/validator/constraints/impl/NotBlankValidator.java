/*
 * $Id$
 *
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and/or its affiliates, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hibernate.validator.constraints.impl;

import org.hibernate.validator.constraints.NotBlank;

import com.wxxr.javax.validation.ConstraintValidator;
import com.wxxr.javax.validation.ConstraintValidatorContext;

/**
 * Check that a string's trimmed length is not empty.
 *
 * @author Hardy Ferentschik
 */
public class NotBlankValidator implements ConstraintValidator<NotBlank, String> {

	public void initialize(NotBlank annotation) {
	}

	/**
	 * Checks that the trimmed string is not empty.
	 *
	 * @param s The string to validate.
	 * @param constraintValidatorContext context in which the constraint is evaluated.
	 *
	 * @return Returns <code>true</code> if the string is <code>null</code> or the length of <code>s</code> between the specified
	 *         <code>min</code> and <code>max</code> values (inclusive), <code>false</code> otherwise.
	 */
	public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
		if ( s == null ) {
			return true;
		}

		return s.trim().length() > 0;
	}
}