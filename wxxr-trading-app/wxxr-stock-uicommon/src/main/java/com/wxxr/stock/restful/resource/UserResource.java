/**
 * 
 */
package com.wxxr.stock.restful.resource;

import java.util.List;

import com.wxxr.javax.ws.rs.Consumes;
import com.wxxr.javax.ws.rs.GET;
import com.wxxr.javax.ws.rs.POST;
import com.wxxr.javax.ws.rs.Path;
import com.wxxr.javax.ws.rs.Produces;
import com.wxxr.javax.ws.rs.QueryParam;
import com.wxxr.javax.ws.rs.core.Response;
import com.wxxr.mobile.stock.app.RestBizException;
import com.wxxr.security.vo.SimpleResultVo;
import com.wxxr.security.vo.UserParamVO;
import com.wxxr.stock.crm.customizing.ejb.api.SearchNickNameVO;
import com.wxxr.stock.crm.customizing.ejb.api.UserVO;
import com.wxxr.stock.restful.json.RegQueryVO;

/**
 * @author wangyan
 *
 */
@Path("/rest/user")
public interface UserResource {

	/**
	 * 用户使用手机号注册，如果没有注册过，则直接使用短信把密码发给他 如果已经注册过，就告诉客户端已经注册
	 * 
	 * @param phoneNum
	 *            用户手机号
	 * @return 所创建用户的缺省信息--UserBaseInfoVO--->String
	 * @throws Exception
	 */
	@GET
	@Path("/regUser")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json;charset=utf-8" })
	public SimpleResultVo register(@QueryParam("phone") String phoneNum)
			throws RestBizException;

	/**
	 * 用户使用手机号注册，如果没有注册过，则直接使用短信把密码发给他 如果已经注册过，就告诉客户端已经注册
	 * 
	 * @param phoneNum
	 *            用户手机号
	 * @return 所创建用户的缺省信息--UserBaseInfoVO--->String
	 * @throws Exception
	 */
	@GET
	@Path("/regUser")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json;charset=utf-8" })
	public SimpleResultVo register(@QueryParam("phone") String phoneNum,@QueryParam("password") String password)
			throws RestBizException;
	/**
	 * 用户首次使用时缺省注册流程，系统首先验证SHA1值是否正确，然后注册缺省用户，并将信息返回给手机客户端
	 * 
	 * @param phoneNum
	 *            用户手机号
	 * @return 所创建用户的缺省信息--UserBaseInfoVO--->String
	 * @throws Exception
	 */
	@GET
	@Path("/resetPass")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json" })
	public Response resetPassword(@QueryParam("phone") String phoneNum)
			throws RestBizException;
	@POST
	@Path("/registerWithPass")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json" })
	public SimpleResultVo registerWithPassword(RegQueryVO regInfo) throws RestBizException;
	
	@POST
    @Path("/getMatchUsers")
    @Produces( { "application/json" })
	@Consumes({ "application/json" })
	public SearchNickNameVO getUserByNickName(UserParamVO vo) throws Exception;

}
