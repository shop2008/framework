/**
 * 
 */
package com.wxxr.mobile.stock.client.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.stock.client.IStockAppContext;
import com.wxxr.mobile.stock.client.bean.StockBean;
import com.wxxr.mobile.stock.client.service.IInfoCenterManagementService;

/**
 * @author wangxuyang
 * 
 */
public class InfoCenterManagementServiceImpl extends AbstractModule<IStockAppContext>
		implements IInfoCenterManagementService {

	private static final Trace log = Trace.register(InfoCenterManagementServiceImpl.class);
	//====================interface methods =====================
	@Override
	public List<StockBean> searchStock(String keyword) {
		// TODO Auto-generated method stub
		List<StockBean> list = new ArrayList<StockBean>();
		List<StockBean> searchList = getAllMockData();
		for(StockBean bean : searchList) {
			if(bean.toString().contains(keyword)) {
				list.add(bean);
			}
		}
		return list;
	}

	private List<StockBean> getAllMockData() {
		List<StockBean> searchList = new ArrayList<StockBean>();
		StockBean s;
//		for(int i=0;i<10;i++) {
			s = new StockBean();
			s.setName("招商地产");
			s.setCode("000024");
			searchList.add(s);
			
			s = new StockBean();
			s.setName("中山公用");
			s.setCode("000685");
			searchList.add(s);
			
			s = new StockBean();
			s.setName("中色股份");
			s.setCode("000758");
			searchList.add(s);
			
			s = new StockBean();
			s.setName("武汉中商");
			s.setCode("000785");
			searchList.add(s);
			
			s = new StockBean();
			s.setName("中水渔业");
			s.setCode("000798");
			searchList.add(s);
			
			s = new StockBean();
			s.setName("宗申动力");
			s.setCode("001696");
			searchList.add(s);
			
			s = new StockBean();
			s.setName("招商地产");
			s.setCode("600024");
			searchList.add(s);
			
			s = new StockBean();
			s.setName("中山公用");
			s.setCode("600685");
			searchList.add(s);
			
			s = new StockBean();
			s.setName("中色股份");
			s.setCode("600758");
			searchList.add(s);
			
			s = new StockBean();
			s.setName("武汉中商");
			s.setCode("600785");
			searchList.add(s);
			
			s = new StockBean();
			s.setName("中水渔业");
			s.setCode("600798");
			searchList.add(s);
			
			s = new StockBean();
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
