package com.wxxr.mobile.core.rpc.rest.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.wxxr.javax.ws.rs.Consumes;
import com.wxxr.javax.ws.rs.Produces;
import com.wxxr.javax.ws.rs.WebApplicationException;
import com.wxxr.javax.ws.rs.core.MediaType;
import com.wxxr.javax.ws.rs.core.MultivaluedMap;
import com.wxxr.javax.ws.rs.ext.Provider;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Consumes( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Provider
public class XStreamProvider extends AbstractEntityProvider<Object> {

	private static final String DEFAULT_ENCODING = "utf-8";

	private XStream xstreamXML,xstreamJSON;
	private List<Class<?>> processedXMLTypes = new ArrayList<Class<?>>();
	private List<Class<?>> processedJSONTypes = new ArrayList<Class<?>>();
	
	protected boolean isCollectionType(Class<?> type){
		return List.class.isAssignableFrom(type)||Set.class.isAssignableFrom(type)||Map.class.isAssignableFrom(type)||type.isArray();
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
			Annotation annotations[], MediaType mediaType) {
		return type.getAnnotation(XStreamAlias.class) != null || type.getAnnotation(XmlRootElement.class) != null || isCollectionType(type);
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation annotations[], MediaType mediaType) {
		return type.getAnnotation(XStreamAlias.class) != null || type.getAnnotation(XmlRootElement.class) != null || isCollectionType(type);
	}

	@Override
	public Object readFrom(Class<Object> aClass, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> map, InputStream stream)
			throws IOException, WebApplicationException {
		String encoding = getCharsetAsString(mediaType);
		XStream xStream = getXStream(aClass, mediaType,genericType);
		return xStream.fromXML(new InputStreamReader(stream, encoding));
	}

	@Override
	public void writeTo(Object o, Class<?> aClass, Type type,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> map, OutputStream stream)
			throws IOException, WebApplicationException {
		String encoding = getCharsetAsString(mediaType);
		XStream xStream = getXStream(o.getClass(), mediaType,null);
		xStream.toXML(o, new OutputStreamWriter(stream, encoding));
	}

	protected static String getCharsetAsString(MediaType m) {
		if (m == null) {
			return DEFAULT_ENCODING;
		}
		String result = m.getParameters().get("charset");
		return (result == null) ? DEFAULT_ENCODING : result;
	}

	protected synchronized XStream getXStream(Class<?> type, MediaType mediaType, Type genericType) {
		XStream xstream = createXStream(mediaType,genericType);
		if(xstream == this.xstreamJSON){
			processAnnotations(type, genericType, xstream,this.processedJSONTypes);
		}else if(xstream == this.xstreamXML){
			processAnnotations(type, genericType, xstream,this.processedXMLTypes);
		}
		return xstream;
	}

	/**
	 * @param type
	 * @param genericType
	 * @param xstream
	 */
	protected void processAnnotations(Class<?> type, Type genericType,
			XStream xstream,List<Class<?>> processed) {
		if(!processed.contains(type)){
			xstream.processAnnotations(type);
			processed.add(type);
		}
		if(genericType instanceof ParameterizedType){
			ParameterizedType pType = (ParameterizedType)genericType;
			Type[] actualTypes = pType.getActualTypeArguments();
			if(actualTypes != null){
				for (Type atype : actualTypes) {
					if(atype instanceof Class){
						if(!processed.contains(atype)){
							xstream.processAnnotations((Class<?>)atype);
							processed.add((Class<?>)atype);
						}
					}
				}
			}
		}
	}

	protected XStream createXStream(MediaType mediaType, Type genericType) {
//		System.out.println("XStreamProvider.createXStream()");
		XStream xstream = null;
		if (mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE)) {
			if(xstreamJSON == null){
				xstreamJSON = new XStream(new JettisonMappedXmlDriver());
				xstreamJSON.setMode(XStream.NO_REFERENCES);
			}
			xstream = xstreamJSON;
		} else {
			if(xstreamXML == null){
				xstreamXML = new XStream();
			}
			xstream = xstreamXML;
		}
		return xstream;
	}
}