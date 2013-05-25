package com.wxxr.mobile.core.rpc.rest;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision$
 */
public interface ClientHttpEngine
{
   ClientResponse invoke(ClientInvocation request);
   void close();

}
