package com.wxxr.mobile.core.microkernel.api;

public interface IAdaptable {
	<T> T getAdaptor(Class<T> clazz);
}
