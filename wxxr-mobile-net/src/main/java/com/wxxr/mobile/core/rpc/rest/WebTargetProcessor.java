package com.wxxr.mobile.core.rpc.rest;

import com.wxxr.javax.ws.rs.client.WebTarget;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision$
 */
public interface WebTargetProcessor
{
   WebTarget build(WebTarget target, Object param);
}
