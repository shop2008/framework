package com.wxxr.mobile.stock.security;

import java.io.IOException;

/**
 * 用于前台处理异常的接口通信过程的IO异常 比如：无网络、超时
 * @author zhengjincheng
 *
 */
public interface IExceptionHandler {
	public void handle(IOException e);
}
