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

import java.lang.annotation.ElementType;

import com.wxxr.javax.validation.ConstraintViolation;
import com.wxxr.javax.validation.Path;
import com.wxxr.javax.validation.metadata.ConstraintDescriptor;

/**
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
public class ConstraintViolationImpl<T> implements ConstraintViolation<T>, java.io.Serializable {
	private final String interpolatedMessage;
	private final T rootBean;
	private final Object value;
	private final Path propertyPath;
	private final Object leafBeanInstance;
	private final ConstraintDescriptor constraintDescriptor;
	private final String messageTemplate;
	private final Class<T> rootBeanClass;
	private final ElementType elementType;


	public ConstraintViolationImpl(String messageTemplate, String interpolatedMessage, Class<T> rootBeanClass,
								   T rootBean, Object leafBeanInstance, Object value,
								   Path propertyPath, ConstraintDescriptor constraintDescriptor, ElementType elementType) {
		this.messageTemplate = messageTemplate;
		this.interpolatedMessage = interpolatedMessage;
		this.rootBean = rootBean;
		this.value = value;
		this.propertyPath = propertyPath;
		this.leafBeanInstance = leafBeanInstance;
		this.constraintDescriptor = constraintDescriptor;
		this.rootBeanClass = rootBeanClass;
		this.elementType = elementType;
	}

	public String getMessage() {
		return interpolatedMessage;
	}

	public String getMessageTemplate() {
		return messageTemplate;
	}

	public T getRootBean() {
		return rootBean;
	}

	public Class<T> getRootBeanClass() {
		return rootBeanClass;
	}

	public Object getLeafBean() {
		return leafBeanInstance;
	}

	public Object getInvalidValue() {
		return value;
	}

	public Path getPropertyPath() {
		return propertyPath;
	}

	public ConstraintDescriptor getConstraintDescriptor() {
		return this.constraintDescriptor;
	}

	@Override
	@SuppressWarnings("SimplifiableIfStatement")
	// IMPORTANT - some behaviour of Validator depends on the correct implementation of this equals method!
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( !( o instanceof ConstraintViolationImpl ) ) {
			return false;
		}

		ConstraintViolationImpl that = ( ConstraintViolationImpl ) o;

		if ( interpolatedMessage != null ? !interpolatedMessage.equals( that.interpolatedMessage ) : that.interpolatedMessage != null ) {
			return false;
		}
		if ( propertyPath != null ? !propertyPath.equals( that.propertyPath ) : that.propertyPath != null ) {
			return false;
		}
		if ( rootBean != null ? !rootBean.equals( that.rootBean ) : that.rootBean != null ) {
			return false;
		}
		if ( leafBeanInstance != null ? !leafBeanInstance.equals( that.leafBeanInstance ) : that.leafBeanInstance != null ) {
			return false;
		}
		if ( elementType != null ? !elementType.equals( that.elementType ) : that.elementType != null ) {
			return false;
		}
		if ( value != null ? !value.equals( that.value ) : that.value != null ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = interpolatedMessage != null ? interpolatedMessage.hashCode() : 0;
		result = 31 * result + ( propertyPath != null ? propertyPath.hashCode() : 0 );
		result = 31 * result + ( rootBean != null ? rootBean.hashCode() : 0 );
		result = 31 * result + ( leafBeanInstance != null ? leafBeanInstance.hashCode() : 0 );
		result = 31 * result + ( value != null ? value.hashCode() : 0 );
		result = 31 * result + ( elementType != null ? elementType.hashCode() : 0 );
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append( "ConstraintViolationImpl" );
		sb.append( "{interpolatedMessage='" ).append( interpolatedMessage ).append( '\'' );
		sb.append( ", propertyPath=" ).append( propertyPath );
		sb.append( ", rootBeanClass=" ).append( rootBeanClass );
		sb.append( ", messageTemplate='" ).append( messageTemplate ).append( '\'' );
		sb.append( '}' );
		return sb.toString();
	}
}
