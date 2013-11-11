/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.el.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.ResourceBundle;

import com.wxxr.mobile.core.log.api.Trace;

/**
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author$
 */
public final class MessageFactory {
	private static final Trace log = Trace.register(MessageFactory.class);
	 private static String messages =
	"error.convert=Cannot convert {0} of type {1} to {2}\n"+
	"error.compare=Cannot compare {0} to {1}\n"+
	"error.function=Problems calling function ''{0}''\n"+
	"error.function.syntax=Syntax error in calling function ''{0}''\n"+
	"error.unreachable.base=Target Unreachable, identifier ''{0}'' resolved to null\n"+
	"error.unreachable.property=Target Unreachable, ''{0}'' returned null\n"+
	"error.resolver.unhandled=ELResolver did not handle type: {0} with property of ''{1}''\n"+
	"error.resolver.unhandled.null=ELResolver cannot handle a null base Object with identifier ''{0}''\n"+
	"error.value.literal.write=ValueExpression is a literal and not writable: {0}\n"+
	"error.null=Expression cannot be null\n"+
	"error.mixed=Expression cannot contain both '#{..}' and '${..}' : {0}\n"+
	"error.method=Not a valid MethodExpression : {0}\n"+
	"error.method.nullParms=Parameter types cannot be null\n"+
	"error.value.expectedType=Expected type cannot be null\n"+
	"error.eval=Error Evaluating {0} : {1}\n"+
	"error.syntax.set=Illegal Syntax for Set Operation\n"+
	"error.syntax.assign=Illegal Syntax for Assign Operation\n"+
	"error.method.notfound=Method not found: {0}.{1}({2})\n"+
	"error.property.notfound=Property ''{1}'' not found on {0}\n"+
	"error.fnMapper.null=Expression uses functions, but no FunctionMapper was provided\n"+
	"error.fnMapper.method=Function ''{0}'' not found\n"+
	"error.fnMapper.paramcount=Function ''{0}'' specifies {1} params, but {2} were supplied\n"+
	"error.context.null=ELContext was null\n"+
	"error.array.outofbounds=Index {0} is out of bounds for array of size {1}\n"+
	"error.list.outofbounds=Index {0} is out of bounds for list of size {1}\n"+
	"error.property.notfound=Property ''{1}'' not found on type: {0}\n"+
	"error.property.invocation=Property ''{1}'' threw an exception from type: {0}\n"+
	"error.property.notreadable=Property ''{1}'' doesn't have a 'get' specified on type: {0}\n"+
	"error.property.notwritable=Property ''{1}'' doesn't have a 'set' specified on type: {0}\n"+
	"error.method.name=An instance of {0} is specified as the static method name, it must be a String\n"+
	"error.class.notfound=The specified class ''{0}'' not found\n"+
	"error.lambda.call=A Lambda expression must return another Lambda expression in this syntax\n"+
	"error.lambda.parameter.readonly=The Lambda parameter ''{0}'' is not writable";

    protected static Properties bundle;
    
    static {
    	bundle = new Properties();
    	try {
			bundle.load(new ByteArrayInputStream(messages.getBytes()));
		} catch (IOException e) {
			log.warn("Failed to load in message", e);
		}
    }
    /**
     * 
     */
    public MessageFactory() {
        super();
    }
    
    public static String get(final String key) {
        return bundle.getProperty(key);
    }

    public static String get(final String key, final Object obj0) {
        return getArray(key, new Object[] { obj0 });
    }

    public static String get(final String key, final Object obj0,
            final Object obj1) {
        return getArray(key, new Object[] { obj0, obj1 });
    }

    public static String get(final String key, final Object obj0,
            final Object obj1, final Object obj2) {
        return getArray(key, new Object[] { obj0, obj1, obj2 });
    }

    public static String get(final String key, final Object obj0,
            final Object obj1, final Object obj2, final Object obj3) {
        return getArray(key, new Object[] { obj0, obj1, obj2, obj3 });
    }

    public static String get(final String key, final Object obj0,
            final Object obj1, final Object obj2, final Object obj3,
            final Object obj4) {
        return getArray(key, new Object[] { obj0, obj1, obj2, obj3, obj4 });
    }

    public static String getArray(final String key, final Object[] objA) {
        return MessageFormat.format(bundle.getProperty(key), objA);
    }

}
