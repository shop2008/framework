/**
 * 
 */
package org.hibernate.validator.xml;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.util.List;
import java.util.Set;

import org.hibernate.validator.metadata.AnnotationIgnores;
import org.hibernate.validator.metadata.MetaConstraint;

/**
 * @author neillin
 *
 */
public interface XmlMappingParser {
	Set<Class<?>> getXmlConfiguredClasses();
	AnnotationIgnores getAnnotationIgnores();
	<T> List<MetaConstraint<T, ? extends Annotation>> getConstraintsForClass(Class<T> beanClass);
	List<Member> getCascadedMembersForClass( Class<?> clazz );
	List<Class<?>> getDefaultSequenceForClass(Class<?> beanClass);
	void parse(Set<InputStream> mappingStreams);
}
