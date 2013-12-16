package com.wxxr.stock.restful.resource;

import com.wxxr.javax.ws.rs.Consumes;
import com.wxxr.javax.ws.rs.GET;
import com.wxxr.javax.ws.rs.Path;
import com.wxxr.javax.ws.rs.Produces;
import com.wxxr.javax.ws.rs.QueryParam;
import com.wxxr.javax.ws.rs.core.MediaType;

@Path("/rest/url")
public interface IURLLocatorResource {	
	@GET
    @Path("/urlSettings")
	@Produces({MediaType.WILDCARD})
	@Consumes
	byte[] getURLSettings(@QueryParam("digest") String digest);
}
