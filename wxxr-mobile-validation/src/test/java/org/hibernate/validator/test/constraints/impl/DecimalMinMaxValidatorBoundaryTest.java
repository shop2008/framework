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
package org.hibernate.validator.test.constraints.impl;

import static java.lang.annotation.ElementType.FIELD;
import static org.hibernate.validator.test.util.TestUtil.assertCorrectConstraintTypes;
import static org.hibernate.validator.test.util.TestUtil.assertNumberOfViolations;

import java.util.Set;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.cfg.ConstraintMapping;
import org.hibernate.validator.cfg.defs.DecimalMaxDef;
import org.hibernate.validator.cfg.defs.DecimalMinDef;
import org.hibernate.validator.test.ResourceBundleMessageInterpolator;
import org.hibernate.validator.test.util.TestUtil;
import org.testng.annotations.Test;

import com.wxxr.javax.validation.ConstraintViolation;
import com.wxxr.javax.validation.Validator;
import com.wxxr.javax.validation.ValidatorFactory;
import com.wxxr.javax.validation.constraints.DecimalMin;

/**
 * @author Hardy Ferentschik
 */
public class DecimalMinMaxValidatorBoundaryTest {
	public double d;

	@Test
	public void testDecimalMinValue() {

		// use programmatic mapping api to configure constraint
		ConstraintMapping mapping = new ConstraintMapping();
		mapping.type( DecimalMinMaxValidatorBoundaryTest.class )
				.property( "d", FIELD )
				.constraint( DecimalMinDef.class )
				.value( "0.100000000000000005" );

		HibernateValidatorConfiguration config = TestUtil.getConfiguration( HibernateValidator.class );
		config.addMapping( mapping ).messageInterpolator(new ResourceBundleMessageInterpolator());

		ValidatorFactory factory = config.buildValidatorFactory();
		Validator validator = factory.getValidator();

		this.d = 0.1;

		Set<ConstraintViolation<DecimalMinMaxValidatorBoundaryTest>> constraintViolations = validator.validate( this );
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintTypes( constraintViolations, DecimalMin.class );
	}

	@Test
	public void testDecimalMaxValue() {

		// use programmatic mapping api to configure constraint
		ConstraintMapping mapping = new ConstraintMapping();
		mapping.type( DecimalMinMaxValidatorBoundaryTest.class )
				.property( "d", FIELD )
				.constraint( DecimalMaxDef.class )
				.value( "0.1" );

		HibernateValidatorConfiguration config = TestUtil.getConfiguration( HibernateValidator.class );
		config.addMapping( mapping ).messageInterpolator(new ResourceBundleMessageInterpolator());

		ValidatorFactory factory = config.buildValidatorFactory();
		Validator validator = factory.getValidator();

		this.d = 0.1;

		Set<ConstraintViolation<DecimalMinMaxValidatorBoundaryTest>> constraintViolations = validator.validate( this );
		assertNumberOfViolations( constraintViolations, 0 );
	}
}