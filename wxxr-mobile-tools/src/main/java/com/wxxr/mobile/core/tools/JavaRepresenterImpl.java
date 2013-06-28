package com.wxxr.mobile.core.tools;

import org.apache.commons.lang.StringEscapeUtils;

class JavaRepresenterImpl implements JavaRepresenter {

	@Override
	public String getJavaRepresentation(Object value) {
		if (value instanceof String) {
			return getStringRepresentation((String) value);
		} else if (value instanceof Class<?>) {
			return getClassRepresentation((Class<?>) value);
		} else {
			return String.valueOf(value);
		}
	}

	private String getClassRepresentation(Class<?> clazz) {
		return clazz.getSimpleName() + ".class";
	}

	private String getStringRepresentation(String value) {
		return '"' + StringEscapeUtils.escapeJava(value) + '"';
	}

}
