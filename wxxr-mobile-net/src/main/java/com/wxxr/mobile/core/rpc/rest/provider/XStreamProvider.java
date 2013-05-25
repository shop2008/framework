package com.wxxr.mobile.core.rpc.rest.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

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

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
			Annotation annotations[], MediaType mediaType) {
		return type.getAnnotation(XStreamAlias.class) != null || type.getAnnotation(XmlRootElement.class) != null;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation annotations[], MediaType mediaType) {
		return type.getAnnotation(XStreamAlias.class) != null || type.getAnnotation(XmlRootElement.class) != null;
	}

	@Override
	public Object readFrom(Class<Object> aClass, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> map, InputStream stream)
			throws IOException, WebApplicationException {
		String encoding = getCharsetAsString(mediaType);
		XStream xStream = getXStream(aClass, mediaType);
		return xStream.fromXML(new InputStreamReader(stream, encoding));
	}

	@Override
	public void writeTo(Object o, Class<?> aClass, Type type,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> map, OutputStream stream)
			throws IOException, WebApplicationException {
		String encoding = getCharsetAsString(mediaType);
		XStream xStream = getXStream(o.getClass(), mediaType);
		xStream.toXML(o, new OutputStreamWriter(stream, encoding));
	}

	protected static String getCharsetAsString(MediaType m) {
		if (m == null) {
			return DEFAULT_ENCODING;
		}
		String result = m.getParameters().get("charset");
		return (result == null) ? DEFAULT_ENCODING : result;
	}

	protected synchronized XStream getXStream(Class<?> type, MediaType mediaType) {
		XStream xstream = createXStream(mediaType);
		xstream.processAnnotations(type);
		return xstream;
	}

	protected XStream createXStream(MediaType mediaType) {
		System.out.println("XStreamProvider.createXStream()");
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