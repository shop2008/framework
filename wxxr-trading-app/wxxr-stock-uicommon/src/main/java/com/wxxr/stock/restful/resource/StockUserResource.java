package com.wxxr.stock.restful.resource;

import com.wxxr.javax.ws.rs.Consumes;
import com.wxxr.javax.ws.rs.GET;
import com.wxxr.javax.ws.rs.POST;
import com.wxxr.javax.ws.rs.Path;
import com.wxxr.javax.ws.rs.Produces;
import com.wxxr.javax.ws.rs.core.Response;
import com.wxxr.mobile.stock.app.RestBizException;
import com.wxxr.security.vo.BindMobileVO;
import com.wxxr.security.vo.SimpleResultVo;
import com.wxxr.security.vo.UpdatePwdVO;
import com.wxxr.security.vo.UserAuthenticaVO;
import com.wxxr.security.vo.UserBaseInfoVO;
import com.wxxr.security.vo.UserParamVO;
import com.wxxr.stock.common.valobject.ResultBaseVO;
import com.wxxr.stock.crm.customizing.ejb.api.ActivityUserVo;
import com.wxxr.stock.crm.customizing.ejb.api.TokenVO;
import com.wxxr.stock.crm.customizing.ejb.api.UserVO;
import com.wxxr.stock.hq.ejb.api.UserAttributeVOs;

@Path("/secure/user")
public interface StockUserResource {





	/**
	 * 用户登陆后绑定客户端app 所有的信息在ejb那里取得
	 * 
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/bindApp")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json" })
	public Response bindApp() throws RestBizException;

	/**
	 * 用户登陆后绑定客户端app 所有的信息在ejb那里取得
	 * 
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/unbindApp")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json" })
	public Response unbindApp() throws RestBizException;

	/**
	 * 用户登陆后绑定客户端app 所有的信息在ejb那里取得
	 * 
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/isBindApp")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json" })
	public SimpleResultVo isBindApp() throws RestBizException;

	@POST
	@Path("/updatePwd")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json" })
	public ResultBaseVO updatePwd(UpdatePwdVO vo) throws RestBizException;
	@POST
	@Path("/bindmobile")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json" })
	public ResultBaseVO bindMobile(BindMobileVO vo) throws RestBizException;

	@POST
	@Path("/pollAndPushToken")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json" })
	public TokenVO updateToken(TokenVO tokenVO) throws RestBizException;

	@GET
	@Path("/getmobile")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json" })
	public UserBaseInfoVO getMobile() throws RestBizException;

	@POST
	@Path("/updateNickName")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json;charset=utf-8" })
	public ResultBaseVO updateNickName(UserParamVO vo);

	@GET
	@Path("/getUser")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json" })
	public UserVO getUser() throws Exception;

	@GET
	@Path("/getActivityUser")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json" })
	public ActivityUserVo getActivityUser() throws Exception;

	@POST
	@Path("/userAttrIdentify")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json" })
	public ResultBaseVO userAttributeIdentify(UserAuthenticaVO vo);

	@POST
	@Path("/updateAttrIdentify")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json" })
	public ResultBaseVO updateAttributeIdentify(UserAuthenticaVO vo);

	@GET
	@Path("/getUserAttributes")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json" })
	public UserAttributeVOs getUserAttributes() throws Exception;
}
