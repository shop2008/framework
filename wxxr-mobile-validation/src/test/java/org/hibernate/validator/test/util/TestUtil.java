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
package org.hibernate.validator.test.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.FileAssert.fail;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.engine.ConstraintValidatorFactoryImpl;
import org.hibernate.validator.engine.PathImpl;
import org.hibernate.validator.engine.resolver.DefaultTraversableResolver;
import org.hibernate.validator.test.ResourceBundleMessageInterpolator;
import org.hibernate.validator.util.LoggerFactory;

import com.wxxr.javax.validation.Configuration;
import com.wxxr.javax.validation.ConstraintValidatorFactory;
import com.wxxr.javax.validation.ConstraintViolation;
import com.wxxr.javax.validation.MessageInterpolator;
import com.wxxr.javax.validation.Path;
import com.wxxr.javax.validation.TraversableResolver;
import com.wxxr.javax.validation.Validation;
import com.wxxr.javax.validation.ValidationProviderResolver;
import com.wxxr.javax.validation.Validator;
import com.wxxr.javax.validation.metadata.PropertyDescriptor;
import com.wxxr.javax.validation.spi.ConfigurationState;
import com.wxxr.javax.validation.spi.ValidationProvider;
import com.wxxr.mobile.core.log.api.Trace;

/**
 * Tests for the <code>ReflectionHelper</code>.
 *
 * @author Hardy Ferentschik
 */
public class TestUtil {
	private static final Trace log = LoggerFactory.make();

	private static Validator hibernateValidator;

	private TestUtil() {
	}

	public static Validator getValidator() {
		if ( hibernateValidator == null ) {
//			Configuration configuration = getConfiguration( Locale.ENGLISH );
//			configuration.traversableResolver( new DummyTraversableResolver() );
//			hibernateValidator = configuration.buildValidatorFactory().getValidator();
			hibernateValidator = new HibernateValidator().buildValidatorFactory(new ConfigurationState() {
				
				private DefaultTraversableResolver defaultTraversableResolver = new DefaultTraversableResolver();
				private ConstraintValidatorFactoryImpl vFactory = new ConstraintValidatorFactoryImpl();
				private ResourceBundleMessageInterpolator msgInterpolator = new ResourceBundleMessageInterpolator();
				@Override
				public boolean isIgnoreXmlConfiguration() {
					return false;
				}
				
				@Override
				public TraversableResolver getTraversableResolver() {
					return defaultTraversableResolver;
				}
				
				@Override
				public Map<String, String> getProperties() {
					return null;
				}
				
				@Override
				public MessageInterpolator getMessageInterpolator() {
					return msgInterpolator;
				}
				
				@Override
				public Set<InputStream> getMappingStreams() {
					return null;
				}
				
				@Override
				public ConstraintValidatorFactory getConstraintValidatorFactory() {
					return vFactory;
				}
			}).getValidator();
		}
		return hibernateValidator;
	}

	public static Configuration<HibernateValidatorConfiguration> getConfiguration() {
		return getConfiguration( HibernateValidator.class, Locale.ENGLISH );
	}

	public static Configuration<HibernateValidatorConfiguration> getConfiguration(Locale locale) {
		return getConfiguration( HibernateValidator.class, locale );
	}

	public static <T extends Configuration<T>, U extends ValidationProvider<T>> T getConfiguration(Class<U> type) {
		return getConfiguration( type, Locale.ENGLISH );
	}

	public static <T extends Configuration<T>, U extends ValidationProvider<T>> T getConfiguration(Class<U> type, Locale locale) {
		Locale.setDefault( locale );
		return Validation.byProvider( type ).providerResolver(new ValidationProviderResolver() {
			
			@Override
			public List<ValidationProvider<?>> getValidationProviders() {
				List<ValidationProvider<?>> result = new ArrayList<ValidationProvider<?>>();
				result.add(new HibernateValidator());
				return result;
			}
		}).configure();
	}

	/**
	 * @param path The path to the xml file which should server as <code>validation.xml</code> for the returned
	 * <code>Validator</code>.
	 *
	 * @return A <code>Validator</code> instance which respects the configuration specified in the file with the path
	 *         <code>path</code>.
	 */
	public static Validator getValidatorWithCustomConfiguration(String path) {
		Thread.currentThread().setContextClassLoader( new CustomValidationXmlClassLoader( path ) );
		return getConfiguration().buildValidatorFactory().getValidator();
	}

	public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String property) {
		Validator validator = getValidator();
		return validator.getConstraintsForClass( clazz ).getConstraintsForProperty( property );
	}

	public static <T> void assertCorrectConstraintViolationMessages(Set<ConstraintViolation<T>> violations, String... messages) {
		List<String> actualMessages = new ArrayList<String>();
		for ( ConstraintViolation<?> violation : violations ) {
			actualMessages.add( violation.getMessage() );
		}

		assertEquals( actualMessages.size(), messages.length, "Wrong number of error messages" );

		for ( String expectedMessage : messages ) {
			assertTrue(
					actualMessages.contains( expectedMessage ),
					"The message '" + expectedMessage + "' should have been in the list of actual messages: " + actualMessages
			);
			actualMessages.remove( expectedMessage );
		}
		assertTrue(
				actualMessages.isEmpty(), "Actual messages contained more messages as specified expected messages"
		);
	}

	public static <T> void assertCorrectConstraintTypes(Set<ConstraintViolation<T>> violations, Class<?>... expectedConstraintTypes) {
		List<String> actualConstraintTypes = new ArrayList<String>();
		for ( ConstraintViolation<?> violation : violations ) {
			actualConstraintTypes.add(
					( ( Annotation ) violation.getConstraintDescriptor().getAnnotation() ).annotationType().getName()
			);
		}

		assertEquals(
				expectedConstraintTypes.length, actualConstraintTypes.size(), "Wrong number of constraint types."
		);

		for ( Class<?> expectedConstraintType : expectedConstraintTypes ) {
			assertTrue(
					actualConstraintTypes.contains( expectedConstraintType.getName() ),
					"The constraint type " + expectedConstraintType.getName() + " should have been violated."
			);
		}
	}

	public static <T> void assertCorrectPropertyPaths(Set<ConstraintViolation<T>> violations, String... propertyPaths) {
		List<Path> propertyPathsOfViolations = new ArrayList<Path>();
		for ( ConstraintViolation<?> violation : violations ) {
			propertyPathsOfViolations.add( violation.getPropertyPath() );
		}

		for ( String propertyPath : propertyPaths ) {
			Path expectedPath = PathImpl.createPathFromString( propertyPath );
			boolean containsPath = false;
			for ( Path actualPath : propertyPathsOfViolations ) {
				if ( assertEqualPaths( expectedPath, actualPath ) ) {
					containsPath = true;
					break;
				}
			}
			if ( !containsPath ) {
				fail( expectedPath + " is not in the list of path instances contained in the actual constraint violations: " + propertyPathsOfViolations );
			}
		}
	}

	public static void assertConstraintViolation(ConstraintViolation violation, String errorMessage, Class rootBean, Object invalidValue, String propertyPath) {
		assertEquals(
				violation.getPropertyPath(),
				PathImpl.createPathFromString( propertyPath ),
				"Wrong propertyPath"
		);
		assertConstraintViolation( violation, errorMessage, rootBean, invalidValue );
	}

	public static void assertConstraintViolation(ConstraintViolation violation, String errorMessage, Class rootBean, Object invalidValue) {
		assertEquals(
				violation.getInvalidValue(),
				invalidValue,
				"Wrong invalid value"
		);
		assertConstraintViolation( violation, errorMessage, rootBean );
	}

	public static void assertConstraintViolation(ConstraintViolation violation, String errorMessage, Class rootBean) {
		assertEquals(
				violation.getRootBean().getClass(),
				rootBean,
				"Wrong root bean type"
		);
		assertConstraintViolation( violation, errorMessage );
	}

	public static void assertConstraintViolation(ConstraintViolation violation, String message) {
		assertEquals( violation.getMessage(), message, "Wrong message" );
	}

	public static void assertNumberOfViolations(Set violations, int expectedViolations) {
		assertEquals( violations.size(), expectedViolations, "Wrong number of constraint violations" );
	}

	public static boolean assertEqualPaths(Path p1, Path p2) {
		Iterator<Path.Node> p1Iterator = p1.iterator();
		Iterator<Path.Node> p2Iterator = p2.iterator();
		while ( p1Iterator.hasNext() ) {
			Path.Node p1Node = p1Iterator.next();
			if ( !p2Iterator.hasNext() ) {
				return false;
			}
			Path.Node p2Node = p2Iterator.next();

			// do the comparison on the node values
			if ( p2Node.getName() == null ) {
				if ( p1Node.getName() != null ) {
					return false;
				}
			}
			else if ( !p2Node.getName().equals( p1Node.getName() ) ) {
				return false;
			}

			if ( p2Node.isInIterable() != p1Node.isInIterable() ) {
				return false;
			}


			if ( p2Node.getIndex() == null ) {
				if ( p1Node.getIndex() != null ) {
					return false;
				}
			}
			else if ( !p2Node.getIndex().equals( p1Node.getIndex() ) ) {
				return false;
			}

			if ( p2Node.getKey() == null ) {
				if ( p1Node.getKey() != null ) {
					return false;
				}
			}
			else if ( !p2Node.getKey().equals( p1Node.getKey() ) ) {
				return false;
			}
		}

		return !p2Iterator.hasNext();
	}

	private static class CustomValidationXmlClassLoader extends ClassLoader {
		private final String customValidationXmlPath;

		CustomValidationXmlClassLoader(String pathToCustomValidationXml) {
			super( CustomValidationXmlClassLoader.class.getClassLoader() );
			customValidationXmlPath = pathToCustomValidationXml;
		}

		public InputStream getResourceAsStream(String path) {
			String finalPath = path;
			if ( "META-INF/validation.xml".equals( path ) ) {
				log.info( "Using {} as validation.xml", customValidationXmlPath );
				finalPath = customValidationXmlPath;
			}
			return super.getResourceAsStream( finalPath );
		}
	}
}
