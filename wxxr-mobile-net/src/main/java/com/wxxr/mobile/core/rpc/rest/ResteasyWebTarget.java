package com.wxxr.mobile.core.rpc.rest;
import java.lang.reflect.Method;

import com.wxxr.javax.ws.rs.client.WebTarget;
import com.wxxr.javax.ws.rs.core.MultivaluedMap;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision$
 */
public interface ResteasyWebTarget extends WebTarget
{
   ResteasyClient getResteasyClient();

   <T> T proxy(Class<T> proxyInterface);

   <T> ProxyBuilder<T> proxyBuilder(Class<T> proxyInterface);


   ResteasyWebTarget queryParams(MultivaluedMap<String, Object> parameters) throws IllegalArgumentException, NullPointerException;

   /**
    * Will encode any '{}' characters and not treat them as template parameters
    */
   ResteasyWebTarget queryParamNoTemplate(String name, Object... values) throws NullPointerException;

   /**
    * Will encode any '{}' characters and not treat them as template parameters
    */
   ResteasyWebTarget queryParamsNoTemplate(MultivaluedMap<String, Object> parameters) throws IllegalArgumentException, NullPointerException;

   ResteasyWebTarget path(Class<?> resource) throws IllegalArgumentException;

   ResteasyWebTarget path(Method method) throws IllegalArgumentException;

   ResteasyWebTarget clone();

}
