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
package org.hibernate.validator.metadata;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.validator.util.LoggerFactory;
import org.hibernate.validator.util.ReflectionHelper;

import com.wxxr.javax.validation.GroupDefinitionException;
import com.wxxr.javax.validation.GroupSequence;
import com.wxxr.javax.validation.Valid;
import com.wxxr.javax.validation.groups.Default;
import com.wxxr.javax.validation.metadata.BeanDescriptor;
import com.wxxr.javax.validation.metadata.PropertyDescriptor;
import com.wxxr.mobile.core.log.api.Trace;


/**
 * This class encapsulates all meta data needed for validation. Implementations of {@code Validator} interface can
 * instantiate an instance of this class and delegate the metadata extraction to it.
 *
 * @author Hardy Ferentschik
 */
public final class BeanMetaDataImpl<T> implements BeanMetaData<T> {

	private static final Trace log = LoggerFactory.make();

	/**
	 * The root bean class for this meta data.
	 */
	private final Class<T> beanClass;

	/**
	 * The main element descriptor for <code>beanClass</code>.
	 */
	private BeanDescriptorImpl<T> beanDescriptor;

	/**
	 * Map of all direct constraints which belong to the entity {@code beanClass}. The constraints are mapped to the class
	 * (eg super class or interface) in which they are defined.
	 */
	private Map<Class<?>, List<MetaConstraint<T, ? extends Annotation>>> metaConstraints = new HashMap<Class<?>, List<MetaConstraint<T, ? extends Annotation>>>();

	/**
	 * List of cascaded members.
	 */
	private List<Member> cascadedMembers = new ArrayList<Member>();

	/**
	 * Maps field and method names to their <code>ElementDescriptorImpl</code>.
	 */
	private Map<String, PropertyDescriptor> propertyDescriptors = new HashMap<String, PropertyDescriptor>();

	/**
	 * The default groups sequence for this bean class.
	 */
	private List<Class<?>> defaultGroupSequence = new ArrayList<Class<?>>();

	/**
	 * Object keeping track of all constraints.
	 */
	private final ConstraintHelper constraintHelper;

	/**
	 * A list of all property names in the class (constraint and un-constraint).
	 */
	// Used to avoid ReflectionHelper#containsMember which is slow
	private final Set<String> propertyNames = new HashSet<String>( 30 );

	public BeanMetaDataImpl(Class<T> beanClass, ConstraintHelper constraintHelper, BeanMetaDataCache beanMetaDataCache) {
		this(
				beanClass,
				constraintHelper,
				new ArrayList<Class<?>>(),
				new HashMap<Class<?>, List<MetaConstraint<T, ?>>>(),
				new ArrayList<Member>(),
				new AnnotationIgnores(),
				beanMetaDataCache
		);
	}

	public BeanMetaDataImpl(Class<T> beanClass,
							ConstraintHelper constraintHelper,
							List<Class<?>> defaultGroupSequence,
							Map<Class<?>, List<MetaConstraint<T, ?>>> constraints,
							List<Member> cascadedMembers,
							AnnotationIgnores annotationIgnores,
							BeanMetaDataCache beanMetaDataCache) {
		this.beanClass = beanClass;
		this.constraintHelper = constraintHelper;
		createMetaData( annotationIgnores, beanMetaDataCache );
		if ( !defaultGroupSequence.isEmpty() ) {
			setDefaultGroupSequence( defaultGroupSequence );
		}
		for ( Map.Entry<Class<?>, List<MetaConstraint<T, ?>>> entry : constraints.entrySet() ) {
			Class<?> clazz = entry.getKey();
			for ( MetaConstraint<T, ?> constraint : entry.getValue() ) {
				addMetaConstraint( clazz, constraint );
			}
		}
		for ( Member member : cascadedMembers ) {
			addCascadedMember( member );
		}
	}

	public Class<T> getBeanClass() {
		return beanClass;
	}

	public BeanDescriptor getBeanDescriptor() {
		return beanDescriptor;
	}

	public List<Member> getCascadedMembers() {
		return Collections.unmodifiableList( cascadedMembers );
	}

	public Map<Class<?>, List<MetaConstraint<T, ? extends Annotation>>> getMetaConstraintsAsMap() {
		return Collections.unmodifiableMap( metaConstraints );
	}

	public List<MetaConstraint<T, ? extends Annotation>> getMetaConstraintsAsList() {
		List<MetaConstraint<T, ? extends Annotation>> constraintList = new ArrayList<MetaConstraint<T, ? extends Annotation>>();
		for ( List<MetaConstraint<T, ? extends Annotation>> list : metaConstraints.values() ) {
			constraintList.addAll( list );
		}
		return Collections.unmodifiableList( constraintList );
	}

	public PropertyDescriptor getPropertyDescriptor(String property) {
		return propertyDescriptors.get( property );
	}

	public boolean isPropertyPresent(String name) {
		return propertyNames.contains( name );
	}

	public List<Class<?>> getDefaultGroupSequence() {
		return Collections.unmodifiableList( defaultGroupSequence );
	}

	public boolean defaultGroupSequenceIsRedefined() {
		return defaultGroupSequence.size() > 1;
	}

	public Set<PropertyDescriptor> getConstrainedProperties() {
		return Collections.unmodifiableSet( new HashSet<PropertyDescriptor>( propertyDescriptors.values() ) );
	}

	private void setDefaultGroupSequence(List<Class<?>> groupSequence) {
		defaultGroupSequence = new ArrayList<Class<?>>();
		boolean groupSequenceContainsDefault = false;
		for ( Class<?> group : groupSequence ) {
			if ( group.getName().equals( beanClass.getName() ) ) {
				defaultGroupSequence.add( Default.class );
				groupSequenceContainsDefault = true;
			}
			else if ( group.getName().equals( Default.class.getName() ) ) {
				throw new GroupDefinitionException( "'Default.class' cannot appear in default group sequence list." );
			}
			else {
				defaultGroupSequence.add( group );
			}
		}
		if ( !groupSequenceContainsDefault ) {
			throw new GroupDefinitionException( beanClass.getName() + " must be part of the redefined default group sequence." );
		}
		if ( log.isTraceEnabled() ) {
			log.trace(
					"Members of the default group sequence for bean {} are: {}",
					beanClass.getName(),
					defaultGroupSequence
			);
		}
	}

	private void addMetaConstraint(Class<?> clazz, MetaConstraint<T, ? extends Annotation> metaConstraint) {
		// first we add the meta constraint to our meta constraint map
		List<MetaConstraint<T, ? extends Annotation>> constraintList;
		if ( !metaConstraints.containsKey( clazz ) ) {
			constraintList = new ArrayList<MetaConstraint<T, ? extends Annotation>>();
			metaConstraints.put( clazz, constraintList );
		}
		else {
			constraintList = metaConstraints.get( clazz );
		}
		constraintList.add( metaConstraint );

		// but we also have to update the descriptors exposing the BV metadata API
		if ( metaConstraint.getElementType() == ElementType.TYPE ) {
			beanDescriptor.addConstraintDescriptor( metaConstraint.getDescriptor() );
		}
		else {
			PropertyDescriptorImpl propertyDescriptor = ( PropertyDescriptorImpl ) propertyDescriptors.get(
					metaConstraint.getPropertyName()
			);
			if ( propertyDescriptor == null ) {
				Member member = metaConstraint.getMember();
				propertyDescriptor = addPropertyDescriptorForMember( member, isValidAnnotationPresent( member ) );
			}
			propertyDescriptor.addConstraintDescriptor( metaConstraint.getDescriptor() );
		}
	}

	private void addCascadedMember(Member member) {
		ReflectionHelper.setAccessibility( member );
		cascadedMembers.add( member );
		addPropertyDescriptorForMember( member, true );
	}

	/**
	 * Create bean descriptor, find all classes/subclasses/interfaces which have to be taken in consideration
	 * for this validator and create meta data.
	 *
	 * @param annotationIgnores Data structure keeping track on which annotation should be ignored
	 * @param beanMetaDataCache The bean meta data cache
	 */
	private void createMetaData(AnnotationIgnores annotationIgnores, BeanMetaDataCache beanMetaDataCache) {
		beanDescriptor = new BeanDescriptorImpl<T>( this );
		initDefaultGroupSequence();
		List<Class<?>> classes = ReflectionHelper.computeClassHierarchy( beanClass );
		for ( Class<?> current : classes ) {
			initClass( current, annotationIgnores, beanMetaDataCache );
		}
	}

	private void initClass(Class<?> clazz, AnnotationIgnores annotationIgnores, BeanMetaDataCache beanMetaDataCache) {
		initClassConstraints( clazz, annotationIgnores, beanMetaDataCache );
		initMethodConstraints( clazz, annotationIgnores, beanMetaDataCache );
		initFieldConstraints( clazz, annotationIgnores, beanMetaDataCache );
	}

	/**
	 * Checks whether there is a default group sequence defined for this class.
	 * See HV-113.
	 */
	private void initDefaultGroupSequence() {
		List<Class<?>> groupSequence = new ArrayList<Class<?>>();
		GroupSequence groupSequenceAnnotation = beanClass.getAnnotation( GroupSequence.class );
		if ( groupSequenceAnnotation == null ) {
			groupSequence.add( beanClass );
		}
		else {
			groupSequence.addAll( Arrays.asList( groupSequenceAnnotation.value() ) );
		}

		setDefaultGroupSequence( groupSequence );
	}

	private void initFieldConstraints(Class<?> clazz, AnnotationIgnores annotationIgnores, BeanMetaDataCache beanMetaDataCache) {
		final Field[] fields = ReflectionHelper.getFields( clazz );
		for ( Field field : fields ) {
			addToPropertyNameList( field );

			// HV-172
			if ( Modifier.isStatic( field.getModifiers() ) ) {
				continue;
			}

			if ( annotationIgnores.isIgnoreAnnotations( field ) ) {
				continue;
			}

			// HV-262
			BeanMetaDataImpl<?> cachedMetaData = beanMetaDataCache.getBeanMetaData( clazz );
			List<ConstraintDescriptorImpl<?>> fieldMetaData;
			boolean cachedFieldIsCascaded = false;
			if ( cachedMetaData != null && cachedMetaData.getMetaConstraintsAsMap().get( clazz ) != null ) {
				fieldMetaData = new ArrayList<ConstraintDescriptorImpl<?>>();
				cachedFieldIsCascaded = cachedMetaData.getCascadedMembers().contains( field );
				for ( MetaConstraint<?, ?> metaConstraint : cachedMetaData.getMetaConstraintsAsMap().get( clazz ) ) {
					ConstraintDescriptorImpl<?> descriptor = metaConstraint.getDescriptor();
					if ( descriptor.getElementType() == ElementType.FIELD
							&& metaConstraint.getPropertyName().equals( ReflectionHelper.getPropertyName( field ) ) ) {
						fieldMetaData.add( descriptor );
					}
				}
			}
			else {
				fieldMetaData = findConstraints( field, ElementType.FIELD );
			}

			for ( ConstraintDescriptorImpl<?> constraintDescription : fieldMetaData ) {
				ReflectionHelper.setAccessibility( field );
				MetaConstraint<T, ?> metaConstraint = createMetaConstraint( field, constraintDescription );
				addMetaConstraint( clazz, metaConstraint );
			}

			if ( cachedFieldIsCascaded || field.isAnnotationPresent( Valid.class ) ) {
				addCascadedMember( field );
			}
		}
	}

	private void addToPropertyNameList(Member member) {
		String name = ReflectionHelper.getPropertyName( member );
		if ( name != null ) {
			propertyNames.add( name );
		}
	}

	private void initMethodConstraints(Class<?> clazz, AnnotationIgnores annotationIgnores, BeanMetaDataCache beanMetaDataCache) {
		final Method[] declaredMethods = ReflectionHelper.getMethods( clazz );
		for ( Method method : declaredMethods ) {
			addToPropertyNameList( method );

			// HV-172
			if ( Modifier.isStatic( method.getModifiers() ) ) {
				continue;
			}

			if ( annotationIgnores.isIgnoreAnnotations( method ) ) {
				continue;
			}

			// HV-262
			BeanMetaDataImpl<?> cachedMetaData = beanMetaDataCache.getBeanMetaData( clazz );
			List<ConstraintDescriptorImpl<?>> methodMetaData;
			boolean cachedMethodIsCascaded = false;
			if ( cachedMetaData != null && cachedMetaData.getMetaConstraintsAsMap().get( clazz ) != null ) {
				cachedMethodIsCascaded = cachedMetaData.getCascadedMembers().contains( method );
				methodMetaData = new ArrayList<ConstraintDescriptorImpl<?>>();
				for ( MetaConstraint<?, ?> metaConstraint : cachedMetaData.getMetaConstraintsAsMap().get( clazz ) ) {
					ConstraintDescriptorImpl<?> descriptor = metaConstraint.getDescriptor();
					if ( descriptor.getElementType() == ElementType.METHOD
							&& metaConstraint.getPropertyName().equals( ReflectionHelper.getPropertyName( method ) ) ) {
						methodMetaData.add( descriptor );
					}
				}
			}
			else {
				methodMetaData = findConstraints( method, ElementType.METHOD );
			}

			for ( ConstraintDescriptorImpl<?> constraintDescription : methodMetaData ) {
				ReflectionHelper.setAccessibility( method );
				MetaConstraint<T, ?> metaConstraint = createMetaConstraint( method, constraintDescription );
				addMetaConstraint( clazz, metaConstraint );
			}

			if ( cachedMethodIsCascaded || method.isAnnotationPresent( Valid.class ) ) {
				addCascadedMember( method );
			}
		}
	}

	private PropertyDescriptorImpl addPropertyDescriptorForMember(Member member, boolean isCascaded) {
		String name = ReflectionHelper.getPropertyName( member );
		PropertyDescriptorImpl propertyDescriptor = ( PropertyDescriptorImpl ) propertyDescriptors.get(
				name
		);
		if ( propertyDescriptor == null ) {
			propertyDescriptor = new PropertyDescriptorImpl(
					ReflectionHelper.getType( member ),
					isCascaded,
					name,
					this
			);
			propertyDescriptors.put( name, propertyDescriptor );
		}
		return propertyDescriptor;
	}

	private boolean isValidAnnotationPresent(Member member) {
		return ( ( AnnotatedElement ) member ).isAnnotationPresent( Valid.class );
	}

	private void initClassConstraints(Class<?> clazz, AnnotationIgnores annotationIgnores, BeanMetaDataCache beanMetaDataCache) {
		if ( annotationIgnores.isIgnoreAnnotations( clazz ) ) {
			return;
		}

		// HV-262
		BeanMetaDataImpl<?> cachedMetaData = beanMetaDataCache.getBeanMetaData( clazz );
		List<ConstraintDescriptorImpl<?>> classMetaData;
		if ( cachedMetaData != null && cachedMetaData.getMetaConstraintsAsMap().get( clazz ) != null ) {
			classMetaData = new ArrayList<ConstraintDescriptorImpl<?>>();
			for ( MetaConstraint<?, ?> metaConstraint : cachedMetaData.getMetaConstraintsAsMap().get( clazz ) ) {
				ConstraintDescriptorImpl<?> descriptor = metaConstraint.getDescriptor();
				if ( descriptor.getElementType() == ElementType.TYPE ) {
					classMetaData.add( descriptor );
				}
			}
		}
		else {
			classMetaData = findClassLevelConstraints( clazz );
		}

		for ( ConstraintDescriptorImpl<?> constraintDescription : classMetaData ) {
			MetaConstraint<T, ?> metaConstraint = createMetaConstraint( null, constraintDescription );
			addMetaConstraint( clazz, metaConstraint );
		}
	}

	private <A extends Annotation> MetaConstraint<T, ?> createMetaConstraint(Member m, ConstraintDescriptorImpl<A> descriptor) {
		return new MetaConstraint<T, A>( beanClass, m, descriptor );
	}

	/**
	 * Examines the given annotation to see whether it is a single- or multi-valued constraint annotation.
	 *
	 * @param clazz the class we are currently processing
	 * @param annotation The annotation to examine
	 * @param type the element type on which the annotation/constraint is placed on
	 *
	 * @return A list of constraint descriptors or the empty list in case <code>annotation</code> is neither a
	 *         single nor multi-valued annotation.
	 */
	private <A extends Annotation> List<ConstraintDescriptorImpl<?>> findConstraintAnnotations(Class<?> clazz, A annotation, ElementType type) {
		List<ConstraintDescriptorImpl<?>> constraintDescriptors = new ArrayList<ConstraintDescriptorImpl<?>>();

		List<Annotation> constraints = new ArrayList<Annotation>();
		Class<? extends Annotation> annotationType = annotation.annotationType();
		if ( constraintHelper.isConstraintAnnotation( annotationType )
				|| constraintHelper.isBuiltinConstraint( annotationType ) ) {
			constraints.add( annotation );
		}
		else if ( constraintHelper.isMultiValueConstraint( annotationType ) ) {
			constraints.addAll( constraintHelper.getMultiValueConstraints( annotation ) );
		}

		for ( Annotation constraint : constraints ) {
			final ConstraintDescriptorImpl constraintDescriptor = buildConstraintDescriptor( clazz, constraint, type );
			constraintDescriptors.add( constraintDescriptor );
		}
		return constraintDescriptors;
	}

	@SuppressWarnings("unchecked")
	private <A extends Annotation> ConstraintDescriptorImpl buildConstraintDescriptor(Class<?> clazz, A annotation, ElementType type) {
		ConstraintDescriptorImpl constraintDescriptor;
		ConstraintOrigin definedIn = determineOrigin( clazz );
		if ( clazz.isInterface() && !clazz.equals( beanClass ) ) {
			constraintDescriptor = new ConstraintDescriptorImpl(
					annotation, constraintHelper, clazz, type, definedIn
			);
		}
		else {
			constraintDescriptor = new ConstraintDescriptorImpl( annotation, constraintHelper, type, definedIn );
		}
		return constraintDescriptor;
	}

	/**
	 * Finds all constraint annotations defined for the given class and returns them in a list of
	 * constraint descriptors.
	 *
	 * @param beanClass The class to check for constraints annotations.
	 *
	 * @return A list of constraint descriptors for all constraint specified on the given class.
	 */
	private List<ConstraintDescriptorImpl<?>> findClassLevelConstraints(Class<?> beanClass) {
		List<ConstraintDescriptorImpl<?>> metaData = new ArrayList<ConstraintDescriptorImpl<?>>();
		for ( Annotation annotation : beanClass.getAnnotations() ) {
			metaData.addAll( findConstraintAnnotations( beanClass, annotation, ElementType.TYPE ) );
		}
		return metaData;
	}


	/**
	 * Finds all constraint annotations defined for the given field/method and returns them in a list of
	 * constraint descriptors.
	 *
	 * @param member The fields or method to check for constraints annotations.
	 * @param type The element type the constraint/annotation is placed on.
	 *
	 * @return A list of constraint descriptors for all constraint specified for the given field or method.
	 */
	private List<ConstraintDescriptorImpl<?>> findConstraints(Member member, ElementType type) {
		assert member instanceof Field || member instanceof Method;

		List<ConstraintDescriptorImpl<?>> metaData = new ArrayList<ConstraintDescriptorImpl<?>>();
		for ( Annotation annotation : ( ( AnnotatedElement ) member ).getAnnotations() ) {
			metaData.addAll( findConstraintAnnotations( member.getDeclaringClass(), annotation, type ) );
		}

		return metaData;
	}

	private ConstraintOrigin determineOrigin(Class<?> clazz) {
		if ( clazz.equals( beanClass ) ) {
			return ConstraintOrigin.DEFINED_LOCALLY;
		}
		else {
			return ConstraintOrigin.DEFINED_IN_HIERARCHY;
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append( "BeanMetaDataImpl" );
		sb.append( "{beanClass=" ).append( beanClass );
		sb.append( ", beanDescriptor=" ).append( beanDescriptor );
		sb.append( ", metaConstraints=" ).append( metaConstraints );
		sb.append( ", cascadedMembers=" ).append( cascadedMembers );
		sb.append( ", propertyDescriptors=" ).append( propertyDescriptors );
		sb.append( ", defaultGroupSequence=" ).append( defaultGroupSequence );
		sb.append( ", constraintHelper=" ).append( constraintHelper );
		sb.append( '}' );
		return sb.toString();
	}
}
