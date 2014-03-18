/**
 * 
 */
package com.wxxr.stock.restful.resource;

import java.util.List;

import com.wxxr.javax.ws.rs.core.Response;
import com.wxxr.mobile.core.async.api.Async;
import com.wxxr.security.vo.SimpleResultVo;
import com.wxxr.security.vo.UserParamVO;
import com.wxxr.stock.crm.customizing.ejb.api.UserVO;
import com.wxxr.stock.restful.json.RegQueryVO;

/**
 * @author wangyan
 *
 */
public interface UserResourceAsync {

	/**
	 * 用户使用手机号注册，如果没有注册过，则直接使用短信把密码发给他 如果已经注册过，就告诉客户端已经注册
	 * 
	 * @param phoneNum
	 *            用户手机号
	 * @return 所创建用户的缺省信息--UserBaseInfoVO--->String
	 * @throws Exception
	 */
	public Async<SimpleResultVo> register(String phoneNum);
			

	/**
	 * 用户使用手机号注册，如果没有注册过，则直接使用短信把密码发给他 如果已经注册过，就告诉客户端已经注册
	 * 
	 * @param phoneNum
	 *            用户手机号
	 * @return 所创建用户的缺省信息--UserBaseInfoVO--->String
	 * @throws Exception
	 */
	public Async<SimpleResultVo> register(String phoneNum, String password);

	
	/**
	 * 用户首次使用时缺省注册流程，系统首先验证SHA1值是否正确，然后注册缺省用户，并将信息返回给手机客户端
	 * 
	 * @param phoneNum
	 *            用户手机号
	 * @return 所创建用户的缺省信息--UserBaseInfoVO--->String
	 * @throws Exception
	 */
	public Async<Response> resetPassword(String phoneNum);
	
	public Async<SimpleResultVo> registerWithPassword(RegQueryVO regInfo);
	
	public Async<List<UserVO>> getUserByNickName(UserParamVO vo);

			
}
