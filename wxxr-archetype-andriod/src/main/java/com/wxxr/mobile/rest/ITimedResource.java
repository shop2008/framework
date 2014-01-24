/**
 * 
 */
package com.wxxr.mobile.rest;

import com.wxxr.javax.ws.rs.Consumes;
import com.wxxr.javax.ws.rs.GET;
import com.wxxr.javax.ws.rs.Path;
import com.wxxr.javax.ws.rs.Produces;

/**
 * @author fudapeng
 *
 */
@Path("/rest/timeservice")
public interface ITimedResource {
	@GET
    @Path("/getTime")
    @Produces({ "application/json;charset=utf-8" })
	@Consumes
    public String getServerTime() throws Exception;
}
