package com.wxxr.stock.restful.resource;

import com.wxxr.javax.ws.rs.core.Response;
import com.wxxr.mobile.core.async.api.Async;
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

public interface StockUserResourceAsync {





	/**
	 * 用户登陆后绑定客户端app 所有的信息在ejb那里取得
	 * 
	 * @return
	 * @throws Exception
	 */
	public Async<Response> bindApp() ;

	/**
	 * 用户登陆后绑定客户端app 所有的信息在ejb那里取得
	 * 
	 * @return
	 * @throws Exception
	 */
	public Async<Response> unbindApp() ;

	/**
	 * 用户登陆后绑定客户端app 所有的信息在ejb那里取得
	 * 
	 * @return
	 * @throws Exception
	 */
	public Async<SimpleResultVo> isBindApp() ;

	public Async<ResultBaseVO> updatePwd(UpdatePwdVO vo) ;
	
	public Async<ResultBaseVO> bindMobile(BindMobileVO vo) ;

	public Async<TokenVO> updateToken(TokenVO tokenVO) ;

	public Async<UserBaseInfoVO> getMobile() ;

	public Async<ResultBaseVO> updateNickName(UserParamVO vo);

	public Async<UserVO> getUser() ;

	public Async<ActivityUserVo> getActivityUser() ;

	public Async<ResultBaseVO> userAttributeIdentify(UserAuthenticaVO vo);

	public Async<ResultBaseVO> updateAttributeIdentify(UserAuthenticaVO vo);

	public Async<UserAttributeVOs> getUserAttributes() ;
}
