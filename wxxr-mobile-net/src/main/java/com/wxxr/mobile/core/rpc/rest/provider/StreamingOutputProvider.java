package com.wxxr.mobile.core.rpc.rest.provider;
import com.wxxr.javax.ws.rs.Produces;
import com.wxxr.javax.ws.rs.core.MediaType;
import com.wxxr.javax.ws.rs.core.MultivaluedMap;
import com.wxxr.javax.ws.rs.core.StreamingOutput;
import com.wxxr.javax.ws.rs.ext.MessageBodyWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision$
 */
@Produces("*/*")
public class StreamingOutputProvider implements MessageBodyWriter<StreamingOutput>
{
   public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
   {
      return StreamingOutput.class.isAssignableFrom(type);
   }

   public long getSize(StreamingOutput streamingOutput, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
   {
      return -1;
   }

   public void writeTo(StreamingOutput streamingOutput, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException
   {
      streamingOutput.write(entityStream);
   }
}
