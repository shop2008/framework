package com.wxxr.mobile.core.rpc.rest;


import com.wxxr.javax.ws.rs.Path;
import com.wxxr.javax.ws.rs.PathParam;
import com.wxxr.javax.ws.rs.client.WebTarget;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;

public class SubResourceInvoker implements MethodInvoker
{
   final ProxyConfig config;
   final Class<?> iface;
   final Method method;
   final ResteasyWebTarget parent;
   String[] pathParams;
   boolean hasPathParams;

   public SubResourceInvoker(ResteasyWebTarget parent, Method method, ProxyConfig config)
   {
      this.config = config;
      this.method = method;
      this.iface = method.getReturnType();
      pathParams = new String[method.getParameterTypes().length];
      for (int i = 0; i < pathParams.length; i++)
      {
         Annotation[] paramAnnotations = method.getParameterAnnotations()[i];
         for (Annotation annotation : paramAnnotations)
         {
            if (annotation instanceof PathParam)
            {
               String name = ((PathParam) annotation).value();
               pathParams[i] = name;
               hasPathParams = true;
               break;
            }
         }
      }
      if (method.isAnnotationPresent(Path.class))
      {
         parent = (ResteasyWebTarget)parent.path(method.getAnnotation(Path.class).value());
      }
      this.parent = parent;

   }

   @Override
   public Object invoke(Object[] args)
   {
      ResteasyWebTarget target = parent;
      if (hasPathParams)
      {
         HashMap<String, Object> params = new HashMap<String, Object>();
         for (int i = 0; i < pathParams.length; i++)
         {
            if (pathParams[i] != null)
            {
               params.put(pathParams[i], args[i]);
            }
         }
         target = (ResteasyWebTarget)parent.resolveTemplates(params);
      }
      return ProxyBuilder.proxy(iface, target, config);
   }
}
