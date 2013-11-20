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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.validator.util.IdentitySet;

import com.wxxr.javax.validation.ConstraintValidatorFactory;
import com.wxxr.javax.validation.ConstraintViolation;
import com.wxxr.javax.validation.MessageInterpolator;
import com.wxxr.javax.validation.TraversableResolver;
import com.wxxr.javax.validation.metadata.ConstraintDescriptor;

/**
 * Context object keeping track of all important data for a top level {@link javax.validation.Validator#validate(Object, Class[])} },
 * {@link javax.validation.Validator#validateValue(Class, String, Object, Class[])}  } or {@link javax.validation.Validator#validateProperty(Object, String, Class[])}  call.
 * <p/>
 * we use this object to collect all failing constraints, but also to cache the caching traversable resolver for a full stack call.
 *
 * @author Hardy Ferentschik
 * @author Emmanuel Bernard
 */
public class ValidationContext<T> {

	/**
	 * The root bean of the validation.
	 */
	private final T rootBean;

	/**
	 * The root bean class of the validation.
	 */
	private final Class<T> rootBeanClass;

	/**
	 * Maps a group to an identity set to keep track of already validated objects. We have to make sure
	 * that each object gets only validated once per group and property path.
	 */
	private final Map<Class<?>, IdentitySet> processedObjects;

	/**
	 * Maps an object to a list of paths in which it has been invalidated.
	 */
	private final Map<Object, Set<PathImpl>> processedPaths;

	/**
	 * A list of all failing constraints so far.
	 */
	private final List<ConstraintViolation<T>> failingConstraintViolations;

	/**
	 * Flag indicating whether an object can only be validated once per group or once per group AND validation path.
	 *
	 * @todo Make this boolean a configurable item.
	 */
	private boolean allowOneValidationPerPath = true;

	/**
	 * The message resolver which should be used in this context.
	 */
	private final MessageInterpolator messageInterpolator;

	/**
	 * The constraint factory which should be used in this context.
	 */
	private final ConstraintValidatorFactory constraintValidatorFactory;

	/**
	 * Allows a JPA provider to decide whether a property should be validated.
	 */
	private final TraversableResolver traversableResolver;

	public static <T> ValidationContext<T> getContextForValidate(T object, MessageInterpolator messageInterpolator, ConstraintValidatorFactory constraintValidatorFactory, TraversableResolver traversableResolver) {
		@SuppressWarnings("unchecked")
		Class<T> rootBeanClass = ( Class<T> ) object.getClass();
		return new ValidationContext<T>(
				rootBeanClass, object, messageInterpolator, constraintValidatorFactory, traversableResolver
		);
	}

	public static <T> ValidationContext<T> getContextForValidateProperty(T rootBean, MessageInterpolator messageInterpolator, ConstraintValidatorFactory constraintValidatorFactory, TraversableResolver traversableResolver) {
		@SuppressWarnings("unchecked")
		Class<T> rootBeanClass = ( Class<T> ) rootBean.getClass();
		return new ValidationContext<T>(
				rootBeanClass, rootBean, messageInterpolator, constraintValidatorFactory, traversableResolver
		);
	}

	public static <T> ValidationContext<T> getContextForValidateValue(Class<T> rootBeanClass, MessageInterpolator messageInterpolator, ConstraintValidatorFactory constraintValidatorFactory, TraversableResolver traversableResolver) {
		return new ValidationContext<T>(
				rootBeanClass,
				null,
				messageInterpolator,
				constraintValidatorFactory,
				traversableResolver
		);
	}

	private ValidationContext(Class<T> rootBeanClass, T rootBean, MessageInterpolator messageInterpolator, ConstraintValidatorFactory constraintValidatorFactory, TraversableResolver traversableResolver) {
		this.rootBean = rootBean;
		this.rootBeanClass = rootBeanClass;
		this.messageInterpolator = messageInterpolator;
		this.constraintValidatorFactory = constraintValidatorFactory;
		this.traversableResolver = traversableResolver;

		processedObjects = new HashMap<Class<?>, IdentitySet>();
		processedPaths = new IdentityHashMap<Object, Set<PathImpl>>();
		failingConstraintViolations = new ArrayList<ConstraintViolation<T>>();
	}

	public T getRootBean() {
		return rootBean;
	}

	public Class<T> getRootBeanClass() {
		return rootBeanClass;
	}

	public TraversableResolver getTraversableResolver() {
		return traversableResolver;
	}

	public MessageInterpolator getMessageInterpolator() {
		return messageInterpolator;
	}

	public <U, V> ConstraintViolationImpl<T> createConstraintViolation(ValueContext<U, V> localContext, MessageAndPath messageAndPath, ConstraintDescriptor<?> descriptor) {
		String messageTemplate = messageAndPath.getMessage();
		String interpolatedMessage = messageInterpolator.interpolate(
				messageTemplate,
				new MessageInterpolatorContext( descriptor, localContext.getCurrentValidatedValue() )
		);
		return new ConstraintViolationImpl<T>(
				messageTemplate,
				interpolatedMessage,
				getRootBeanClass(),
				getRootBean(),
				localContext.getCurrentBean(),
				localContext.getCurrentValidatedValue(),
				messageAndPath.getPath(),
				descriptor,
				localContext.getElementType()
		);
	}

	public <U, V> List<ConstraintViolationImpl<T>> createConstraintViolations(ValueContext<U, V> localContext, ConstraintValidatorContextImpl constraintValidatorContext) {
		List<ConstraintViolationImpl<T>> constraintViolations = new ArrayList<ConstraintViolationImpl<T>>();
		for ( MessageAndPath messageAndPath : constraintValidatorContext.getMessageAndPathList() ) {
			ConstraintViolationImpl<T> violation = createConstraintViolation(
					localContext, messageAndPath, constraintValidatorContext.getConstraintDescriptor()
			);
			constraintViolations.add( violation );
		}
		return constraintViolations;
	}

	public ConstraintValidatorFactory getConstraintValidatorFactory() {
		return constraintValidatorFactory;
	}

	public boolean isAlreadyValidated(Object value, Class<?> group, PathImpl path) {
		boolean alreadyValidated;
		alreadyValidated = isAlreadyValidatedForCurrentGroup( value, group );

		if ( alreadyValidated && allowOneValidationPerPath ) {
			alreadyValidated = isAlreadyValidatedForPath( value, path );
		}
		return alreadyValidated;
	}

	public void markProcessed(Object value, Class<?> group, PathImpl path) {
		markProcessForCurrentGroup( value, group );
		if ( allowOneValidationPerPath ) {
			markProcessedForCurrentPath( value, path );
		}
	}

	private void addConstraintFailure(ConstraintViolation<T> failingConstraintViolation) {
		// NOTE: we are relying on the fact that ConstraintViolation.equals() is implemented correctly.
		int i = failingConstraintViolations.indexOf( failingConstraintViolation );
		if ( i == -1 ) {
			failingConstraintViolations.add( failingConstraintViolation );
		}
	}

	public void addConstraintFailures(List<ConstraintViolation<T>> failingConstraintViolations) {
		for ( ConstraintViolation<T> violation : failingConstraintViolations ) {
			addConstraintFailure( violation );
		}
	}

	public List<ConstraintViolation<T>> getFailingConstraints() {
		return failingConstraintViolations;
	}

	private boolean isAlreadyValidatedForPath(Object value, PathImpl path) {
		Set<PathImpl> pathSet = processedPaths.get( value );
		if ( pathSet == null ) {
			return false;
		}

		for ( PathImpl p : pathSet ) {
			if ( p.isRootPath() || path.isRootPath() || p.isSubPathOf( path ) || path.isSubPathOf( p ) ) {
				return true;
			}
		}

		return false;
	}

	private boolean isAlreadyValidatedForCurrentGroup(Object value, Class<?> group) {
		final IdentitySet objectsProcessedInCurrentGroups = processedObjects.get( group );
		return objectsProcessedInCurrentGroups != null && objectsProcessedInCurrentGroups.contains( value );
	}

	private void markProcessedForCurrentPath(Object value, PathImpl path) {
		// hmm - not sure if the current definiton of Path and Node are consistent. Shouldn't a simple property
		// of a entity have a parent node?
		PathImpl parentPath = path.getPathWithoutLeafNode();
		if ( parentPath == null ) {
			parentPath = PathImpl.createNewPath( null );
		}

		if ( processedPaths.containsKey( value ) ) {
			processedPaths.get( value ).add( parentPath );
		}
		else {
			Set<PathImpl> set = new HashSet<PathImpl>();
			set.add( parentPath );
			processedPaths.put( value, set );
		}
	}


	private void markProcessForCurrentGroup(Object value, Class<?> group) {
		if ( processedObjects.containsKey( group ) ) {
			processedObjects.get( group ).add( value );
		}
		else {
			IdentitySet set = new IdentitySet();
			set.add( value );
			processedObjects.put( group, set );
		}
	}
}
