package com.wxxr.mobile.stock.security;
/**
 * 应用层发出HTTP请求后，回调方法
 * @author zhengjincheng
 *
 */
public interface ICallBack {
	public void onSuccess(IHttpResponse t);
	public void onFailed(IHttpResponse t);
}
