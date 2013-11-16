/**
 * 
 */
package com.wxxr.mobile.stock.app.service;

import java.util.List;

import com.wxxr.javax.ws.rs.QueryParam;
import com.wxxr.mobile.stock.app.bean.TradingRecordBean;

/**
 * @author wangxuyang
 *
 */
public interface ITradingRecordManagementService {
	  List<TradingRecordBean> getTradingAccountRecord(@QueryParam("acctID") String acctID,@QueryParam("start") int start,@QueryParam("limit") int limit) throws Exception ;
}
