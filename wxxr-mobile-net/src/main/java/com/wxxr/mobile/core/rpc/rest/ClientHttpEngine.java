package com.wxxr.mobile.core.rpc.rest;

import com.wxxr.mobile.core.async.api.IAsyncCallback;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision$
 */
public interface ClientHttpEngine
{
   void invoke(ClientInvocation request,IAsyncCallback<ClientResponse> callback);
   void close();

}
