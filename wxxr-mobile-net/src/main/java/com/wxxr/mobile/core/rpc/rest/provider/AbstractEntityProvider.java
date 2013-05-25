package com.wxxr.mobile.core.rpc.rest.provider;
import com.wxxr.javax.ws.rs.core.MediaType;
import com.wxxr.javax.ws.rs.ext.MessageBodyReader;
import com.wxxr.javax.ws.rs.ext.MessageBodyWriter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * A AbstractEntityProvider.
 *
 * @param <T>
 * @author <a href="ryan@damnhandy.com>Ryan J. McDonough</a>
 * @version $Revision$
 */
public abstract class AbstractEntityProvider<T>
        implements MessageBodyReader<T>, MessageBodyWriter<T>
{

   public long getSize(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
   {
      return -1;
   }

}
