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
package org.hibernate.validator.test.engine;

import static org.hibernate.validator.test.util.TestUtil.assertCorrectPropertyPaths;
import static org.hibernate.validator.test.util.TestUtil.assertNumberOfViolations;
import static org.testng.Assert.assertTrue;

import java.util.Set;

import org.hibernate.validator.test.util.TestUtil;
import org.testng.annotations.Test;

import com.wxxr.javax.validation.ConstraintViolation;
import com.wxxr.javax.validation.Validator;
import com.wxxr.javax.validation.constraints.AssertTrue;
import com.wxxr.javax.validation.constraints.NotNull;
import com.wxxr.javax.validation.metadata.BeanDescriptor;

/**
 * @author Hardy Ferentschik
 */
public class ValidatorTest {

	/**
	 * HV-208
	 */
	@Test
	public void testPropertyPathDoesNotStartWithLeadingDot() {
		Validator validator = TestUtil.getValidator();
		A testInstance = new A();
		Set<ConstraintViolation<A>> constraintViolations = validator.validate( testInstance );
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectPropertyPaths( constraintViolations, "b" );
	}

	/**
	 * HV-132 - supper hasBoolean format
	 */
	@Test
	public void testHasBoolean() {
		Validator validator = TestUtil.getValidator();
		BeanDescriptor beanDescr = validator.getConstraintsForClass( B.class );
		assertTrue( beanDescr.isBeanConstrained() );
	}

	class A {
		@NotNull
		String b;
	}

	class B {
		private boolean b;

		@AssertTrue
		public boolean hasB() {
			return b;
		}
	}
}
