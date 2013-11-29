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
import com.wxxr.security.vo.BindMobileVO;
import com.wxxr.security.vo.ChangeBindMobileVO;
import com.wxxr.security.vo.RegistVO;
import com.wxxr.security.vo.SimpleResultVo;
import com.wxxr.security.vo.UpdatePwdVO;
import com.wxxr.security.vo.UserAuthenticaVO;
import com.wxxr.security.vo.UserBaseInfoVO;
import com.wxxr.security.vo.UserParamVO;
import com.wxxr.security.vo.UserPermisVO;
import com.wxxr.security.vo.VerifyVO;
import com.wxxr.stock.common.valobject.ResultBaseVO;
import com.wxxr.stock.crm.customizing.ejb.api.ActivityUserVo;
import com.wxxr.stock.crm.customizing.ejb.api.TokenVO;
import com.wxxr.stock.crm.customizing.ejb.api.UserAttributeVO;
import com.wxxr.stock.crm.customizing.ejb.api.UserVO;

@Path("/secure/user")
public interface StockUserResource {

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
	@Consumes({ "application/json" })
	public SimpleResultVo register(@QueryParam("phone") String phoneNum)
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
	@Path("/regist")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json" })
	public Response regist(RegistVO query) throws RestBizException;

	@GET
	@Path("/info")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json" })
	public UserBaseInfoVO info() throws RestBizException;

	@POST
	@Path("/updatePwd")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json" })
	public ResultBaseVO updatePwd(UpdatePwdVO vo) throws RestBizException;

	@GET
	@Path("/getuserpermis")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json" })
	public UserPermisVO getUserPermis() throws RestBizException;

	@POST
	@Path("/bindmobile")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json" })
	public ResultBaseVO bindMobile(BindMobileVO vo) throws RestBizException;

	@POST
	@Path("/changebindmobile")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json" })
	public ResultBaseVO changeBindMobile(ChangeBindMobileVO vo)
			throws RestBizException;

	@POST
	@Path("/verify")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json" })
	public ResultBaseVO verifyUser(VerifyVO vo) throws RestBizException;

	@GET
	@Path("/token")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json" })
	public ResultBaseVO token() throws RestBizException;

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
	@Consumes({ "application/json" })
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
	public List<UserAttributeVO> getUserAttributes() throws Exception;
}
