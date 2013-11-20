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
package org.hibernate.validator;

import org.hibernate.validator.cfg.ConstraintMapping;

import com.wxxr.javax.validation.Configuration;

/**
 * Uniquely identifies Hibernate Validator in the Bean Validation bootstrap
 * strategy. Also contains Hibernate Validator specific configurations.
 *
 * @author Emmanuel Bernard
 * @author Gunnar Morling
 */
public interface HibernateValidatorConfiguration extends Configuration<HibernateValidatorConfiguration> {

	/**
	 * Adds the specified {@link ConstraintMapping} instance to the configuration. Constraints configured in {@code mapping}
	 * will be added to the constraints configured via annotations and/or xml.
	 *
	 * @param mapping {@code ConstraintMapping} instance containing programmatic configured constraints
	 *
	 * @return {@code this} following the chaining method pattern
	 *
	 * @throws IllegalArgumentException if {@code mapping} is {@code null}
	 */
	HibernateValidatorConfiguration addMapping(ConstraintMapping mapping);
}
