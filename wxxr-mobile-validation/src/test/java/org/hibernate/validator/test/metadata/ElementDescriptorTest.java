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
package org.hibernate.validator.test.metadata;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.Set;

import org.hibernate.validator.test.util.TestUtil;
import org.testng.annotations.Test;

import com.wxxr.javax.validation.Validator;
import com.wxxr.javax.validation.metadata.BeanDescriptor;
import com.wxxr.javax.validation.metadata.ConstraintDescriptor;
import com.wxxr.javax.validation.metadata.ElementDescriptor;


/**
 * @author Hardy Ferentschik
 */
public class ElementDescriptorTest {


	@Test
	public void testGetTypeForConstrainedBean() {
		Validator validator = TestUtil.getValidator();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Customer.class );
		assertEquals( beanDescriptor.getElementClass(), Customer.class, "Wrong type." );
	}

	@Test
	public void testGetTypeForConstrainedProperty() {
		ElementDescriptor elementDescriptor = TestUtil.getPropertyDescriptor( Order.class, "orderNumber" );
		assertEquals( elementDescriptor.getElementClass(), Integer.class, "Wrong type." );
	}

	/**
	 * HV-95
	 */
	@Test
	public void testElementDescriptorForProperty() {
		ElementDescriptor elementDescriptor = TestUtil.getPropertyDescriptor( Order.class, "orderNumber" );
		Set<ConstraintDescriptor<?>> constraintDescriptors = elementDescriptor.getConstraintDescriptors();
		assertTrue( constraintDescriptors.size() == 1, "There should be a descriptor" );
	}

	/**
	 * HV-95
	 */
	@Test
	public void testElementDescriptorImmutable() {
		ElementDescriptor elementDescriptor = TestUtil.getPropertyDescriptor( Order.class, "orderNumber" );
		Set<ConstraintDescriptor<?>> constraintDescriptors = elementDescriptor.getConstraintDescriptors();

		try {
			constraintDescriptors.add( null );
			fail( "Set should be immutable" );
		}
		catch ( UnsupportedOperationException e ) {
			// success
		}

		try {
			constraintDescriptors.remove( null );
			fail( "Set should be immutable" );
		}
		catch ( UnsupportedOperationException e ) {
			// success
		}
	}
}
