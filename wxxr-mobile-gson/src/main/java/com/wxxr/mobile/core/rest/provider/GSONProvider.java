package com.wxxr.mobile.core.rest.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.reflect.TypeToken;
import com.wxxr.javax.ws.rs.Consumes;
import com.wxxr.javax.ws.rs.Produces;
import com.wxxr.javax.ws.rs.WebApplicationException;
import com.wxxr.javax.ws.rs.core.MediaType;
import com.wxxr.javax.ws.rs.core.MultivaluedMap;
import com.wxxr.javax.ws.rs.ext.MessageBodyReader;
import com.wxxr.javax.ws.rs.ext.MessageBodyWriter;
import com.wxxr.javax.ws.rs.ext.Provider;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.core.util.Types;

/**
 * Provider for GSon &lt;-> Object marshalling. Uses the following mime types:
 * 
 * <pre>
 * <code>
 *   text/gson
 *   text/x-gson
 *   application/x-gson</code>
 * </pre>
 * 
 * @author wangxuyang
 */
@Provider
@Consumes({ "text/gson", "text/x-gson", "application/x-gson","application/json"})
@Produces({ "text/gson", "text/x-gson", "application/x-gson","application/json"})
public class GSONProvider implements MessageBodyReader<Object>, MessageBodyWriter<Object> {
	private Gson gson;
	
   public long getSize(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
   {
      return -1;
   }

	// MessageBodyReader

	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return isValidType(type);
	}
	private Gson buildGson(){
		return new GsonBuilder()
		// .excludeFieldsWithoutExposeAnnotation()
		// //不导出实体中没有用@Expose注解的属性
				//.enableComplexMapKeySerialization() // 支持Map的key为复杂对象的形式
				.serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
				.setLongSerializationPolicy(LongSerializationPolicy.STRING)// 时间转化为特定格式
				// .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)//会把字段首字母大写,注:对于实体上使用了@SerializedName注解的不会生效.
				.setPrettyPrinting() // 对json结果格式化.
				.setVersion(1.0) // 有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.
									// @Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么
									// @Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.
				.create();
	}
	

	// MessageBodyWriter

	protected boolean isValidType(Class<?> type) {
		if (List.class.isAssignableFrom(type) || Set.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type) || type.isArray()) {
			return true;
		}
//		if (StreamingOutput.class.isAssignableFrom(type))
//			return false;
//		String className = type.getName();
//		if (className.startsWith("java."))
//			return false;
//		if (className.startsWith("javax."))
//			return false;
//		if (type.isPrimitive())
//			return false;

		return false;
	}

	
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return isValidType(type);
	}
	
	@Override
	public Object readFrom(Class<Object> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		if (!(genericType instanceof ParameterizedType))
			throw new IllegalArgumentException("Reader = " + this
					+ " recived genericType = " + genericType
					+ ", but it is not instance of " + ParameterizedType.class);

		ParameterizedType param = (ParameterizedType) genericType;
		Type baseType = param.getActualTypeArguments()[0];
		Class<?> rawType = Types.getRawType(baseType);
		try {
			if (gson == null) {
				gson = buildGson();
			}
			List<Object> list = gson.fromJson(new InputStreamReader(entityStream), TypeToken.get(type).getType());
			List<Object> result = null;
			if (list!=null&&list.size()>0) {
				result = new ArrayList<Object>();
				for (Object object : list) {
					if (object instanceof Map) {
						Map<String,Map<?,?>> map = (Map)object;
						for (Map.Entry<String,Map<?,?>> item : map.entrySet()) {
							if (rawType!=null) {
								Map<?,?> l1 = item.getValue();
								Field[] fileds = getCollectionTypeFileds(rawType);
								Map<String,Object> collectValues = null;
								if (fileds!=null&&fileds.length>0) {
									for (Field field : fileds) {
										Class<?> clazz = field.getType();
										if (Map.class.isAssignableFrom(clazz)) {//处理map不能解析的问题
											Map<?,?> l2 = (Map<?, ?>) l1.remove(field.getName());
											if (l2!=null) {
												List l3 = (List)l2.get("entry");
												Map<String,String> ret = null;
												if (l3!=null&&l3.size()>0) {													
													ret = new HashMap<String, String>();
													for (Object o : l3) {
														Entry entry = gson.fromJson(gson.toJson(o), TypeToken.get(Entry.class).getType());
														ret.put(entry.getKey(), entry.getValue());
													}
												}
												if (collectValues==null) {
													collectValues = new HashMap<String, Object>();
												}
												collectValues.put(field.getName(), ret);
											}
										}
									}
								}
								Object obj = gson.fromJson(gson.toJson(l1), TypeToken.get(rawType).getType());
								setCollectionTypeField(rawType,obj,collectValues);
								result.add(obj);
							}
						}						
					}else if (!(object instanceof Collection)) {
						result.add(object);
					}
				}
			}
			return result;

		} catch (JsonIOException ye) {
			//logger.debug("Failed to decode json: {0}", ye.getMessage());
			throw new IOException("Failed to decode json", ye);
		}catch (JsonSyntaxException e) {
			//logger.debug("Failed to decode json: {0}", e.getMessage());
			throw new IOException("Failed to decode json", e);
		} catch (Exception e) {
			//logger.debug("Failed to decode json: {0}", e.getMessage());
			throw new IOException("Failed to decode json", e);
		}

	}
	private void setCollectionTypeField(Class<?> clazz, Object obj,
			Map<String, Object> collectValues) throws Exception {
		if (clazz==null||obj==null||collectValues==null) {
			return;
		}
		for (String fName : collectValues.keySet()) {
			Field f =  null;
			try {
				f = clazz.getDeclaredField(fName);
				if (f!=null) {
					f.setAccessible(true);
					f.set(obj, collectValues.get(fName));
				}
			} catch (Exception e) {
				throw e;
			} 
		}
	}
	private class Entry{
		private String key;
		private String value;
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
	public static Field[] getCollectionTypeFileds(Class<?> clazz){
		if (clazz==null) {
			return null;
		}
		Field[] fields = clazz.getDeclaredFields();		
		if (fields!=null&&fields.length>0) {
			List<Field> list = new ArrayList<Field>();
			for (Field field : fields) {
				if (Collection.class.isAssignableFrom(field.getType())
						||field.getType().isArray()||Map.class.isAssignableFrom(field.getType())) {
					list.add(field);
				}
			}
			return list.toArray(new Field[list.size()]);
		}
		return null;
	}
	@Override
	public void writeTo(Object t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {
		if (!(genericType instanceof ParameterizedType))
			throw new IllegalArgumentException("Reader = " + this
					+ " recived genericType = " + genericType
					+ ", but it is not instance of " + ParameterizedType.class);

		ParameterizedType param = (ParameterizedType) genericType;
		Type baseType = param.getActualTypeArguments()[0];
		Class<?> rawType = Types.getRawType(baseType);
		String typeTag = null;
		if (rawType!=null&&rawType.isAnnotationPresent(XmlRootElement.class)) {
			XmlRootElement ann = rawType.getAnnotation(XmlRootElement.class);
			if (ann!=null) {
				typeTag = ann.name();
			}
		}
		try {
			if (gson==null) {
				gson = buildGson();
			}
			String jsonString = gson.toJson(t);
			if (StringUtils.isNotBlank(typeTag)) {
				List list = gson.fromJson(jsonString, List.class);
				if (list!=null) {
					List<String> l = new ArrayList<String>();
					for (Object obj : list) {
						l.add(typeTag+"\u003d"+obj);
					}
					if (l.size()>0) {
						jsonString = gson.toJson(l);
					}
				}
			}
			entityStream.write(jsonString.getBytes());

		} catch (Exception e) {
			throw new IOException(e);
		}

	}

}
