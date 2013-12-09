package org.codehaus.jackson.xc;

import java.lang.annotation.Annotation;

import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.annotate.JsonSerialize.Typing;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.AnnotatedConstructor;
import org.codehaus.jackson.map.introspect.AnnotatedField;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.AnnotatedParameter;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.type.JavaType;

import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

/**
 * Annotation introspector that leverages JAXB annotations where applicable to JSON mapping.
 * <p/>
 * The following JAXB annotations were determined to be not-applicable:
 * <ul>
 * <li>{@link XmlAnyAttribute} because it applies only to Map<QName, String>, which jackson can't serialize
 * <li>{@link XmlAnyElement} because it applies only to JAXBElement, which jackson can't serialize
 * <li>{@link javax.xml.bind.annotation.XmlAttachmentRef}
 * <li>{@link XmlElementDecl}
 * <li>{@link XmlElementRefs} because Jackson doesn't have any support for 'named' collection items.
 * <li>{@link XmlElements} because Jackson doesn't have any support for 'named' collection items.
 * <li>{@link XmlID} because jackson' doesn't support referential integrity.
 * <li>{@link XmlIDREF} because jackson' doesn't support referential integrity.
 * <li>{@link javax.xml.bind.annotation.XmlInlineBinaryData}
 * <li>{@link javax.xml.bind.annotation.XmlList} because jackson doesn't support serializing collections to a single string.
 * <li>{@link javax.xml.bind.annotation.XmlMimeType}
 * <li>{@link javax.xml.bind.annotation.XmlMixed}
 * <li>{@link XmlNs}
 * <li>{@link XmlRegistry}
 * <li>{@link XmlRootElement} because there isn't an equivalent element name for a JSON object.
 * <li>{@link XmlSchema}
 * <li>{@link XmlSchemaType}
 * <li>{@link XmlSchemaTypes}
 * <li>{@link XmlSeeAlso}
 * </ul>
 *
 * Note also the following limitations:
 *
 * <ul>
 * <li>Any property annotated with {@link XmlValue} will have a property named 'value' on its JSON object.
 * </ul>
 *
 * @author Ryan Heaton
 */
public class JaxbAnnotationIntrospector extends AnnotationIntrospector{

	@Override
	public boolean isHandled(Annotation ann) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Boolean findCachability(AnnotatedClass ac) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String findRootName(AnnotatedClass ac) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] findPropertiesToIgnore(AnnotatedClass ac) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean findIgnoreUnknownProperties(AnnotatedClass ac) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VisibilityChecker<?> findAutoDetectVisibility(AnnotatedClass ac,
			VisibilityChecker<?> baseChecker) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isIgnorableMethod(AnnotatedMethod m) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isIgnorableConstructor(AnnotatedConstructor c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isIgnorableField(AnnotatedField f) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object findSerializer(Annotated am) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Inclusion findSerializationInclusion(Annotated a, Inclusion defValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> findSerializationType(Annotated a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Typing findSerializationTyping(Annotated a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?>[] findSerializationViews(Annotated a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] findSerializationPropertyOrder(AnnotatedClass ac) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean findSerializationSortAlphabetically(AnnotatedClass ac) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String findGettablePropertyName(AnnotatedMethod am) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasAsValueAnnotation(AnnotatedMethod am) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String findEnumValue(Enum<?> value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String findSerializablePropertyName(AnnotatedField af) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object findDeserializer(Annotated am) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends KeyDeserializer> findKeyDeserializer(Annotated am) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends JsonDeserializer<?>> findContentDeserializer(
			Annotated am) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> findDeserializationType(Annotated am, JavaType baseType,
			String propName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> findDeserializationKeyType(Annotated am,
			JavaType baseKeyType, String propName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> findDeserializationContentType(Annotated am,
			JavaType baseContentType, String propName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String findSettablePropertyName(AnnotatedMethod am) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String findDeserializablePropertyName(AnnotatedField af) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String findPropertyNameForParam(AnnotatedParameter param) {
		// TODO Auto-generated method stub
		return null;
	}

}
