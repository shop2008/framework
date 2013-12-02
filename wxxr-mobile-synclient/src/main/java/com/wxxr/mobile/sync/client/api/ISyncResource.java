package com.wxxr.mobile.sync.client.api;


import com.wxxr.javax.ws.rs.Consumes;
import com.wxxr.javax.ws.rs.POST;
import com.wxxr.javax.ws.rs.Path;
import com.wxxr.javax.ws.rs.Produces;
import com.wxxr.javax.ws.rs.QueryParam;
import com.wxxr.javax.ws.rs.core.MediaType;
import com.wxxr.mobile.sync.client.dto.UNodeDescriptorVO;

/**
 * 
 * @author wangxuyang
 *
 */
@Path("/rest/sync")
public interface ISyncResource {
   
   @POST
   @Path("/isDataChanged")
   @Produces( {MediaType.APPLICATION_JSON})
   @Consumes( {MediaType.APPLICATION_JSON})
   public UNodeDescriptorVO isDataChanged(@QueryParam("key") String key , @QueryParam("nodePath") String nodePath, @QueryParam("digest") String digest) throws Exception;
   
   @POST
   @Path("/getNodeData")
   @Produces(MediaType.WILDCARD)
   @Consumes( {MediaType.APPLICATION_JSON})
   public byte[] getNodeData(@QueryParam("key") String key , @QueryParam("nodePath") String nodePath) throws Exception;
}
