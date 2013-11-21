// $Id$
/*
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
package org.hibernate.validator.test.cfg;

import com.wxxr.javax.validation.ConstraintValidator;
import com.wxxr.javax.validation.ConstraintValidatorContext;


/**
 * @author Hardy Ferentschik
 */
public class MarathonConstraintValidator implements ConstraintValidator<MarathonConstraint, Marathon> {
	private int minRunners;

	public void initialize(MarathonConstraint constraintAnnotation) {
		minRunners = constraintAnnotation.minRunner();
	}

	public boolean isValid(Marathon m, ConstraintValidatorContext context) {
		return m.getRunners().size() >= minRunners;
	}
}

