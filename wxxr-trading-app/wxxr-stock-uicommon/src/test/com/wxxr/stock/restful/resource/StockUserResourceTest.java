package com.wxxr.stock.restful.resource;

import java.util.List;

import junit.framework.TestCase;

import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.rpc.rest.ResteasyRestClientService;
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
import com.wxxr.stock.hq.ejb.api.TaxisVO;
import com.wxxr.stock.restful.json.StockTaxisListVO;


public class StockUserResourceTest extends TestCase{
	IRestProxyService restService=new ResteasyRestClientService();
	
	//public SimpleResultVo register(@QueryParam("phone") String phoneNum)throws RestBizException;
    public void testRegister(){
		try {
			SimpleResultVo a = restService.getRestService(StockUserResource.class).register("13800001009");
		} catch (RestBizException e) {
			System.out.println(e.getMessage());
		}
	}
	

	
	//public Response resetPassword(@QueryParam("phone") String phoneNum)throws RestBizException;
//    public void testResetPassword(){
//		TaxisVO p = new TaxisVO();
//		p.setOrderby("desc");
//		p.setStart(0L);
//		p.setLimit(10L);
//		p.setBlockId(47503L);
//		p.setTaxis("risefallrate");
//		Response a = restService.getRestService(StockResource.class).getIndexPreview(p);
//	}

//	public Response bindApp() throws RestBizException;
//	
//	public Response unbindApp() throws RestBizException;

//	public SimpleResultVo isBindApp() throws RestBizException;

//	public Response regist(RegistVO query) throws RestBizException;

//	public UserBaseInfoVO info() throws RestBizException;	
	public void testGetUserInfo(){
		try {
			UserBaseInfoVO info = restService.getRestService(StockUserResource.class).info();
		} catch (RestBizException e) {
			System.out.println(e.getMessage());
		}
	}
	
//	public ResultBaseVO updatePwd(UpdatePwdVO vo) throws RestBizException;
	public void testUpdatePwd(){
		try {
			UpdatePwdVO vo = new UpdatePwdVO();
			vo.setPassword("123456");
			vo.setPassword("123456");
			ResultBaseVO info = restService.getRestService(StockUserResource.class).updatePwd(vo);
		} catch (RestBizException e) {
			System.out.println(e.getMessage());
		}
	}

//	public UserPermisVO getUserPermis() throws RestBizException;
	public void testGetUserPermis(){
		try {
			UserPermisVO info = restService.getRestService(StockUserResource.class).getUserPermis();
		} catch (RestBizException e) {
			System.out.println(e.getMessage());
		}
	}
	
//	public ResultBaseVO bindMobile(BindMobileVO vo) throws RestBizException;
	public void testBindMobile(){
		try {
			BindMobileVO vo = new BindMobileVO();
			vo.setCode("111");
			vo.setMobileNum("13900001001");
			vo.setType("1");
			ResultBaseVO info = restService.getRestService(StockUserResource.class).bindMobile(vo);
		} catch (RestBizException e) {
			System.out.println(e.getMessage());
		}
	}

//	public ResultBaseVO changeBindMobile(ChangeBindMobileVO vo)	throws RestBizException;
	public void testChangeBindMobile(){
		try {
			ChangeBindMobileVO vo = new ChangeBindMobileVO();
			vo.setNewBindMobile("13900001001");
			vo.setVerifCode("433566");
			ResultBaseVO info = restService.getRestService(StockUserResource.class).changeBindMobile(vo);
		} catch (RestBizException e) {
			System.out.println(e.getMessage());
		}
	}
	
//	public ResultBaseVO verifyUser(VerifyVO vo) throws RestBizException;
	public void testVerifyUser(){
		try {
			VerifyVO vo = new VerifyVO();
			vo.setPasswd("666666");
			ResultBaseVO info = restService.getRestService(StockUserResource.class).verifyUser(vo);
		} catch (RestBizException e) {
			System.out.println(e.getMessage());
		}
	}

//	public ResultBaseVO token() throws RestBizException;

//	public TokenVO updateToken(TokenVO tokenVO) throws RestBizException;
	public void testUpdateToken(){
		try {
			TokenVO vo = new TokenVO();
			vo.setPollToken("666666");
			vo.setPushToken("ccccccc");
			TokenVO info = restService.getRestService(StockUserResource.class).updateToken(vo);
		} catch (RestBizException e) {
			System.out.println(e.getMessage());
		}
	}

//	public UserBaseInfoVO getMobile() throws RestBizException;
	public void testGetMobile(){
		try {
			UserBaseInfoVO info = restService.getRestService(StockUserResource.class).getMobile();
		} catch (RestBizException e) {
			System.out.println(e.getMessage());
		}
	}

//	public ResultBaseVO updateNickName(UserParamVO vo);
	public void testUpdateNickName(){
		UserParamVO vo = new UserParamVO();
		vo.setNickName("666666");
		ResultBaseVO info = restService.getRestService(StockUserResource.class).updateNickName(vo);
	}

//	public UserVO getUser() throws Exception;
	public void testGetUser()throws Exception{
		UserVO info = restService.getRestService(StockUserResource.class).getUser();
	}
	
//	public ActivityUserVo getActivityUser() throws Exception;
	public void testGetActivityUser()throws Exception{
		ActivityUserVo info = restService.getRestService(StockUserResource.class).getActivityUser();
	}

//	public ResultBaseVO userAttributeIdentify(UserAuthenticaVO vo);
	public void testUserAttributeIdentify(){
		UserAuthenticaVO vo = new UserAuthenticaVO();
		vo.setAcctBank("工商银行");
		vo.setAcctName("13800001009");
		vo.setBankNum("6000045255534477");
		vo.setBankPosition("知春路分行");
		ResultBaseVO info = restService.getRestService(StockUserResource.class).userAttributeIdentify(vo);
	}

//	public ResultBaseVO updateAttributeIdentify(UserAuthenticaVO vo);
	public void testUpdateAttributeIdentify(){
		UserAuthenticaVO vo = new UserAuthenticaVO();
		vo.setAcctBank("工商银行");
		vo.setAcctName("13800001009");
		vo.setBankNum("6000045255534477");
		vo.setBankPosition("知春路分行");
		ResultBaseVO info = restService.getRestService(StockUserResource.class).updateAttributeIdentify(vo);
	}

//	public List<UserAttributeVO> getUserAttributes() throws Exception;
	public void testGetUserAttributes()throws Exception{
		List<UserAttributeVO> info = restService.getRestService(StockUserResource.class).getUserAttributes();
	}
}
