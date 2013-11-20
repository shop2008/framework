// $Id$
/*
* JBoss, Home of Professional Open Source
* Copyright 2009, Red Hat, Inc. and/or its affiliates, and individual contributors
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
package org.hibernate.validator.test.constraints.impl;

import static org.testng.Assert.assertEquals;

import java.util.Set;

import org.hibernate.validator.test.util.TestUtil;
import org.testng.annotations.Test;

import com.wxxr.javax.validation.ConstraintViolation;
import com.wxxr.javax.validation.Validator;

/**
 * @author Hardy Ferentschik
 */
public class FutureValidatorTest {

	/**
	 * HV-158
	 */
	@Test
	public void testFutureAndPast() {
		Validator validator = TestUtil.getValidator();
		DateHolder dateHolder = new DateHolder();
		Set<ConstraintViolation<DateHolder>> constraintViolations = validator.validate( dateHolder );
		assertEquals( constraintViolations.size(), 1, "Wrong number of constraints" );
	}
}
