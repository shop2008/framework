/**
 * 
 */
package com.wxxr.mobile.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.wxxr.mobile.core.microkernel.api.IStatefulServiceFactory;

/**
 * @author wangyan
 *
 */
@Target(value={ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface StatefulService {
	Class<? extends IStatefulServiceFactory> factoryClass();
}
