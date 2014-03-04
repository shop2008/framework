package com.wxxr.stock.restful.resource;

import java.security.KeyStore;

import javax.net.ssl.HostnameVerifier;

import junit.framework.TestCase;

import com.wxxr.mobile.core.api.IUserAuthCredential;
import com.wxxr.mobile.core.api.IUserAuthManager;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.rpc.http.apache.AbstractHttpRpcService;
import com.wxxr.mobile.core.security.api.ISiteSecurityService;
import com.wxxr.mobile.stock.app.MockApplication;
import com.wxxr.mobile.stock.app.MockRestClient;
import com.wxxr.mobile.stock.app.RestBizException;
import com.wxxr.security.vo.BindMobileVO;
import com.wxxr.security.vo.SimpleResultVo;
import com.wxxr.security.vo.UpdatePwdVO;
import com.wxxr.security.vo.UserAuthenticaVO;
import com.wxxr.security.vo.UserBaseInfoVO;
import com.wxxr.security.vo.UserParamVO;
import com.wxxr.stock.common.valobject.ResultBaseVO;
import com.wxxr.stock.crm.customizing.ejb.api.ActivityUserVo;
import com.wxxr.stock.crm.customizing.ejb.api.UserVO;
import com.wxxr.stock.hq.ejb.api.UserAttributeVOs;


public class StockUserResourceTest extends TestCase{
	StockUserResource stockUserResource;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		init();
	}
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		stockUserResource=null;
	}

	protected void init() {
		AbstractHttpRpcService service = new AbstractHttpRpcService();
		service.setEnablegzip(false);
		MockApplication app = new MockApplication(){
			
			@Override
			protected void initModules() {
				
			}

		};
		IKernelContext context = app.getContext();
		context.registerService(IUserAuthManager.class, new IUserAuthManager() {
			@Override
			public IUserAuthCredential getAuthCredential(String host,
					String realm) {
				return new IUserAuthCredential() {
					
					@Override
					public String getUserName() {
						return "13500001009";
					}
					
					@Override
					public String getAuthPassword() {
						return "404662";
					}

				};
			}
		});
		service.startup(context);
		context.registerService(ISiteSecurityService.class, new ISiteSecurityService() {
			
			@Override
			public KeyStore getTrustKeyStore() {
				return null;
			}
						
			@Override
			public KeyStore getSiteKeyStore() {
				return null;
			}
			
			@Override
			public HostnameVerifier getHostnameVerifier() {
				return null;
			}
		});
		
		builder = new MockRestClient();
		builder.init(context);
		stockUserResource=builder.getRestService(StockUserResource.class,null,"http://192.168.123.44:8480/mobilestock2");
	}

	MockRestClient builder= null;

	
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

	public void testRegist() throws RestBizException{
		SimpleResultVo vo = builder.getRestService(UserResource.class,null,"http://192.168.123.44:8480/mobilestock2").register("13671279085");
		System.out.println(vo);
	}

//历史遗留问题，新锐财经使用
//	public UserBaseInfoVO info() throws RestBizException;	
//	public void testGetUserInfo(){
//		try {
//			UserBaseInfoVO info = stockUserResource.info();
//		} catch (RestBizException e) {
//			System.out.println(e.getMessage());
//		}
//	}
	
//	public ResultBaseVO updatePwd(UpdatePwdVO vo) throws RestBizException;
	public void testUpdatePwd(){
		try {
			UpdatePwdVO vo = new UpdatePwdVO();
			vo.setPassword("123456");
			vo.setOldPwd("123456");
			ResultBaseVO info = stockUserResource.updatePwd(vo);
		} catch (RestBizException e) {
			System.out.println(e.getMessage());
		}
	}

// 新锐财经使用
//	public UserPermisVO getUserPermis() throws RestBizException;
//	public void testGetUserPermis(){
//		try {
//			UserPermisVO info = stockUserResource.getUserPermis();
//		} catch (RestBizException e) {
//			System.out.println(e.getMessage());
//		}
//	}

//历史遗留问题，新锐财经使用
//	public ResultBaseVO bindMobile(BindMobileVO vo) throws RestBizException;
	public void testBindMobile(){
		try {
			BindMobileVO vo = new BindMobileVO();
			vo.setCode("111");
			vo.setMobileNum("13900001001");
			vo.setType("1");
			ResultBaseVO info = stockUserResource.bindMobile(vo);
		} catch (RestBizException e) {
			System.out.println(e.getMessage());
		}
	}

//历史遗留问题，新锐财经使用
//	public ResultBaseVO changeBindMobile(ChangeBindMobileVO vo)	throws RestBizException;
//	public void testChangeBindMobile(){
//		try {
//			ChangeBindMobileVO vo = new ChangeBindMobileVO();
//			vo.setNewBindMobile("13900001001");
//			vo.setVerifCode("433566");
//			ResultBaseVO info = stockUserResource.changeBindMobile(vo);
//		} catch (RestBizException e) {
//			System.out.println(e.getMessage());
//		}
//	}
	
//历史遗留问题，新锐财经使用	
//	public ResultBaseVO verifyUser(VerifyVO vo) throws RestBizException;
//	public void testVerifyUser(){
//		try {
//			VerifyVO vo = new VerifyVO();
//			vo.setPasswd("666666");
//			ResultBaseVO info = stockUserResource.verifyUser(vo);
//		} catch (RestBizException e) {
//			System.out.println(e.getMessage());
//		}
//	}

//	public ResultBaseVO token() throws RestBizException;

//	public TokenVO updateToken(TokenVO tokenVO) throws RestBizException;
	//FIXME: the device id and the device type is null, so can not be succeed!
	/*public void testUpdateToken(){
		try {
			TokenVO vo = new TokenVO();
			vo.setPollToken("");
			vo.setPushToken("");
			TokenVO info = stockUserResource.updateToken(vo);
			System.out.println(info);
		} catch (RestBizException e) {
			System.out.println(e.getMessage());
		}
	}*/

//	public UserBaseInfoVO getMobile() throws RestBizException;
	public void testGetMobile(){
		try {
			UserBaseInfoVO info = stockUserResource.getMobile();
		} catch (RestBizException e) {
			System.out.println(e.getMessage());
		}
	}

//	public ResultBaseVO updateNickName(UserParamVO vo);
	public void testUpdateNickName(){
		UserParamVO vo = new UserParamVO();
		vo.setNickName("666666");
		ResultBaseVO info = stockUserResource.updateNickName(vo);
	}

//	public UserVO getUser() throws Exception;
	public void testGetUser()throws Exception{
		UserVO info = stockUserResource.getUser();
	}
	
//	public ActivityUserVo getActivityUser() throws Exception;
	public void testGetActivityUser()throws Exception{
		ActivityUserVo info = stockUserResource.getActivityUser();
	}

//	public ResultBaseVO userAttributeIdentify(UserAuthenticaVO vo);
	public void testUserAttributeIdentify(){
		UserAuthenticaVO vo = new UserAuthenticaVO();
		vo.setAcctBank("工商银行");
		vo.setAcctName("13800001009");
		vo.setBankNum("6000045255534477");
		vo.setBankPosition("知春路分行");
		ResultBaseVO info = stockUserResource.userAttributeIdentify(vo);
	}

//	public ResultBaseVO updateAttributeIdentify(UserAuthenticaVO vo);
	public void testUpdateAttributeIdentify(){
		UserAuthenticaVO vo = new UserAuthenticaVO();
		vo.setAcctBank("工商银行");
		vo.setAcctName("13800001009");
		vo.setBankNum("6000045255534477");
		vo.setBankPosition("知春路分行");
		ResultBaseVO info = stockUserResource.updateAttributeIdentify(vo);
	}

//	public List<UserAttributeVO> getUserAttributes() throws Exception;
	public void testGetUserAttributes()throws Exception{
		UserAttributeVOs info = stockUserResource.getUserAttributes();
	}
}
