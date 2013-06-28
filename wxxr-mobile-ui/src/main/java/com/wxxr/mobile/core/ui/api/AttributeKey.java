package com.wxxr.mobile.core.ui.api;

public interface AttributeKey<T> {
	Class<T> getValueType();
	IValueConvertor<T> getValueConvertor();
	String getName();
}
