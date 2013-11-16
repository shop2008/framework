/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.bean.SearchStockListBean;
import com.wxxr.mobile.stock.app.bean.StockBaseInfoBean;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;

/**
 * @author wangxuyang
 * 
 */
public class InfoCenterManagementServiceImpl extends AbstractModule<IStockAppContext>
		implements IInfoCenterManagementService {

	private static final Trace log = Trace.register(InfoCenterManagementServiceImpl.class);
	private SearchStockListBean stockListbean = new SearchStockListBean();
	//====================interface methods =====================
	@Override
	public SearchStockListBean searchStock(String keyword) {
		// TODO Auto-generated method stub
		List<StockBaseInfoBean> list = new ArrayList<StockBaseInfoBean>();
		if(StringUtils.isEmpty(keyword)) {
			stockListbean.setSearchResult(list);
			return stockListbean;
		}
		List<StockBaseInfoBean> searchList = getAllMockData();
		for(StockBaseInfoBean bean : searchList) {
			if(bean.toString().contains(keyword)) {
				list.add(bean);
			}
		}
		stockListbean.setSearchResult(list);
		return this.stockListbean;
	}

	private List<StockBaseInfoBean> getAllMockData() {
		List<StockBaseInfoBean> searchList = new ArrayList<StockBaseInfoBean>();
		StockBaseInfoBean s;
//		for(int i=0;i<10;i++) {
			s = new StockBaseInfoBean();
			s.setName("招商地产");
			s.setCode("000024");
			searchList.add(s);
			
			s = new StockBaseInfoBean();
			s.setName("中山公用");
			s.setCode("000685");
			searchList.add(s);
			
			s = new StockBaseInfoBean();
			s.setName("中色股份");
			s.setCode("000758");
			searchList.add(s);
			
			s = new StockBaseInfoBean();
			s.setName("武汉中商");
			s.setCode("000785");
			searchList.add(s);
			
			s = new StockBaseInfoBean();
			s.setName("中水渔业");
			s.setCode("000798");
			searchList.add(s);
			
			s = new StockBaseInfoBean();
			s.setName("宗申动力");
			s.setCode("001696");
			searchList.add(s);
			
			s = new StockBaseInfoBean();
			s.setName("招商地产");
			s.setCode("600024");
			searchList.add(s);
			
			s = new StockBaseInfoBean();
			s.setName("中山公用");
			s.setCode("600685");
			searchList.add(s);
			
			s = new StockBaseInfoBean();
			s.setName("中色股份");
			s.setCode("600758");
			searchList.add(s);
			
			s = new StockBaseInfoBean();
			s.setName("武汉中商");
			s.setCode("600785");
			searchList.add(s);
			
			s = new StockBaseInfoBean();
			s.setName("中水渔业");
			s.setCode("600798");
			searchList.add(s);
			
			s = new StockBaseInfoBean();
			s.setName("宗申动力");
			s.setCode("601696");
			searchList.add(s);
		return searchList;
	}
	
	// ====================module life cycle methods ==================
	@Override
	protected void initServiceDependency() {
		addRequiredService(IRestProxyService.class);

	}


	@Override
	protected void startService() {
		context.registerService(IInfoCenterManagementService.class, this);

	}


	@Override
	protected void stopService() {
		context.unregisterService(IInfoCenterManagementService.class, this);
	}


}
