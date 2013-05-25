package com.wxxr.mobile.core.rpc.rest;

import com.wxxr.javax.ws.rs.Path;
import com.wxxr.javax.ws.rs.client.WebTarget;
import com.wxxr.javax.ws.rs.container.DynamicFeature;
import com.wxxr.javax.ws.rs.container.ResourceInfo;
import com.wxxr.javax.ws.rs.core.Configuration;
import com.wxxr.javax.ws.rs.core.MediaType;
import com.wxxr.javax.ws.rs.core.Response;

import java.lang.reflect.Method;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision$
 */
@SuppressWarnings("unchecked")
public class ClientInvoker implements MethodInvoker
{
   protected String httpMethod;
   protected Method method;
   protected Class declaring;
   protected MediaType accepts;
   protected Object[] processors;
   protected WebTarget webTarget;
   protected boolean followRedirects;
   protected EntityExtractor extractor;
   protected DefaultEntityExtractorFactory entityExtractorFactory;
   protected Configuration invokerConfig;


   public ClientInvoker(ResteasyWebTarget parent, Class declaring, Method method, ProxyConfig config)
   {
      // webTarget must be a clone so that it has a cloned ClientConfiguration so we can apply DynamicFeature
      if (method.isAnnotationPresent(Path.class))
      {
         this.webTarget = parent.path(method);
      }
      else
      {
         this.webTarget = parent.clone();
      }
      this.declaring = declaring;
      this.method = method;
      invokerConfig = this.webTarget.getConfiguration();
      ResourceInfo info = new ResourceInfo()
      {
         @Override
         public Method getResourceMethod()
         {
            return ClientInvoker.this.method;
         }

         @Override
         public Class<?> getResourceClass()
         {
            return ClientInvoker.this.declaring;
         }
      };
//      for (DynamicFeature feature : invokerConfig.getDynamicFeatures())
//      {
//         feature.configure(info, new FeatureContextDelegate(invokerConfig));
//      }
//

      this.processors = ProcessorFactory.createProcessors(declaring, method, invokerConfig, config.getDefaultConsumes());
      accepts = MediaTypeHelper.getProduces(declaring, method, config.getDefaultProduces());
      entityExtractorFactory = new DefaultEntityExtractorFactory();
      this.extractor = entityExtractorFactory.createExtractor(method);
   }

   public MediaType getAccepts()
   {
      return accepts;
   }

   public Method getMethod()
   {
      return method;
   }

   public Class getDeclaring()
   {
      return declaring;
   }

   public Object invoke(Object[] args)
   {
      ClientInvocation request = createRequest(args);
      Response response = request.invoke();
      ClientContext context = new ClientContext(request, response, entityExtractorFactory);
      return extractor.extractEntity(context, null);
   }

   protected ClientInvocation createRequest(Object[] args)
   {
      WebTarget target = this.webTarget;
      for (int i = 0; i < processors.length; i++)
      {
         if (processors != null && processors[i] instanceof WebTargetProcessor)
         {
            WebTargetProcessor processor = (WebTargetProcessor)processors[i];
            target = processor.build(target, args[i]);

         }
      }

      ClientInvocationBuilder builder = null;
      if (accepts != null)
      {
         builder = (ClientInvocationBuilder)target.request(accepts);
      }
      else
      {
         builder = (ClientInvocationBuilder)target.request();
      }

      for (int i = 0; i < processors.length; i++)
      {
         if (processors != null && processors[i] instanceof InvocationProcessor)
         {
            InvocationProcessor processor = (InvocationProcessor)processors[i];
            processor.process(builder, args[i]);

         }
      }
      return (ClientInvocation)builder.build(httpMethod);
   }

   public String getHttpMethod()
   {
      return httpMethod;
   }

   public void setHttpMethod(String httpMethod)
   {
      this.httpMethod = httpMethod;
   }

   public boolean isFollowRedirects()
   {
      return followRedirects;
   }

   public void setFollowRedirects(boolean followRedirects)
   {
      this.followRedirects = followRedirects;
   }

   public void followRedirects()
   {
      setFollowRedirects(true);
   }
}