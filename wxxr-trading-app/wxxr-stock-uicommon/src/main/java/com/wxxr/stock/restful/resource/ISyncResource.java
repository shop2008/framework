package com.wxxr.stock.restful.resource;


import com.wxxr.javax.ws.rs.Consumes;
import com.wxxr.javax.ws.rs.GET;
import com.wxxr.javax.ws.rs.POST;
import com.wxxr.javax.ws.rs.Path;
import com.wxxr.javax.ws.rs.Produces;
import com.wxxr.javax.ws.rs.QueryParam;
import com.wxxr.javax.ws.rs.core.MediaType;
import com.wxxr.mobile.sync.client.dto.UNodeDescriptorVO;
import com.wxxr.stock.common.valobject.ResultBaseVO;

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
   
   @GET
   @Path("/getNodeDescriptor")
   @Produces( {MediaType.APPLICATION_JSON})
   public UNodeDescriptorVO getNodeDescriptor(@QueryParam("key") String key , @QueryParam("nodePath") String nodePath) throws Exception;
   
   @GET
   @Path("/getDataDigest")
   @Produces( {MediaType.APPLICATION_JSON})
   public ResultBaseVO getDataDigest(@QueryParam("key") String key , @QueryParam("nodePath") String nodePath) throws Exception;
}
