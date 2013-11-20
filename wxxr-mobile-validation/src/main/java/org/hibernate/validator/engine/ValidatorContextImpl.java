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
package org.hibernate.validator.engine;

import org.hibernate.validator.metadata.BeanMetaDataCache;
import org.hibernate.validator.metadata.ConstraintHelper;

import com.wxxr.javax.validation.ConstraintValidatorFactory;
import com.wxxr.javax.validation.MessageInterpolator;
import com.wxxr.javax.validation.TraversableResolver;
import com.wxxr.javax.validation.Validator;
import com.wxxr.javax.validation.ValidatorContext;

/**
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
public class ValidatorContextImpl implements ValidatorContext {
	private MessageInterpolator messageInterpolator;
	private TraversableResolver traversableResolver;
	private ConstraintValidatorFactory constraintValidatorFactory;
	private final MessageInterpolator factoryMessageInterpolator;
	private final TraversableResolver factoryTraversableResolver;
	private final ConstraintValidatorFactory factoryConstraintValidatorFactory;
	private final ConstraintHelper constraintHelper;
	private final BeanMetaDataCache beanMetaDataCache;

	public ValidatorContextImpl(ConstraintValidatorFactory constraintValidatorFactory,
								MessageInterpolator factoryMessageInterpolator,
								TraversableResolver factoryTraversableResolver,
								ConstraintHelper constraintHelper,
								BeanMetaDataCache beanMetaDataCache) {
		this.factoryConstraintValidatorFactory = constraintValidatorFactory;
		this.factoryMessageInterpolator = factoryMessageInterpolator;
		this.factoryTraversableResolver = factoryTraversableResolver;
		this.constraintHelper = constraintHelper;
		this.beanMetaDataCache = beanMetaDataCache;
		messageInterpolator( factoryMessageInterpolator );
		traversableResolver( factoryTraversableResolver );
		constraintValidatorFactory( factoryConstraintValidatorFactory );
	}

	public ValidatorContext messageInterpolator(MessageInterpolator messageInterpolator) {
		if ( messageInterpolator == null ) {
			this.messageInterpolator = factoryMessageInterpolator;
		}
		else {
			this.messageInterpolator = messageInterpolator;
		}
		return this;
	}

	public ValidatorContext traversableResolver(TraversableResolver traversableResolver) {
		if ( traversableResolver == null ) {
			this.traversableResolver = factoryTraversableResolver;
		}
		else {
			this.traversableResolver = traversableResolver;
		}
		return this;
	}

	public ValidatorContext constraintValidatorFactory(ConstraintValidatorFactory factory) {
		if ( constraintValidatorFactory == null ) {
			this.constraintValidatorFactory = factoryConstraintValidatorFactory;
		}
		else {
			this.constraintValidatorFactory = factory;
		}
		return this;
	}

	public Validator getValidator() {
		return new ValidatorImpl(
				constraintValidatorFactory,
				messageInterpolator,
				traversableResolver,
				constraintHelper,
				beanMetaDataCache
		);
	}
}
