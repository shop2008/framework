package com.wxxr.mobile.core.rpc.impl;

import com.wxxr.javax.ws.rs.Consumes;
import com.wxxr.javax.ws.rs.GET;
import com.wxxr.javax.ws.rs.Path;
import com.wxxr.javax.ws.rs.Produces;

@Path("/rest/user")
public interface StockUserResource {

	
	@GET
	@Path("/getUser")
	@Produces({ "application/json" })
	@Consumes({ "application/json" })
	public UserVO getUser() throws Exception;
}
