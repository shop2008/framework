/*
 * Copyright 2009 IIZUKA Software Technologies Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.jtype;

import java.lang.reflect.Array;

/**
 * Provides utility methods for working with classes.
 * 
 * @author Mark Hobson
 * @version $Id$
 * @see Class
 */
final class ClassUtils
{
	// constructors -----------------------------------------------------------
	
	private ClassUtils()
	{
		throw new AssertionError();
	}
	
	// public methods ---------------------------------------------------------
	
	public static String getUnqualifiedClassName(Class<?> klass)
	{
		return getUnqualifiedClassName(klass.getName());
	}

	public static String getUnqualifiedClassName(String className)
	{
		int dot = className.lastIndexOf('.');
		
		return (dot == -1) ? className : className.substring(dot + 1);
	}
	
	public static String getSimpleClassName(Class<?> klass)
	{
		return getSimpleClassName(klass.getName());
	}
	
	public static String getSimpleClassName(String className)
	{
		int index = className.lastIndexOf('$');
		
		if (index == -1)
		{
			index = className.lastIndexOf('.');
		}
		
		return (index == -1) ? className : className.substring(index + 1);
	}
	
	public static Class<?> getArrayType(Class<?> componentType)
	{
		return Array.newInstance(componentType, 0).getClass();
	}
}
