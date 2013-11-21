/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.bean.LineListBean;
import com.wxxr.mobile.stock.app.bean.QuotationListBean;
import com.wxxr.mobile.stock.app.bean.SearchStockListBean;
import com.wxxr.mobile.stock.app.bean.StockBaseInfoBean;
import com.wxxr.mobile.stock.app.bean.StockMinuteKBean;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.bean.StockTaxisListBean;
import com.wxxr.mobile.stock.app.mock.MockDataUtils;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.stock.hq.ejb.api.TaxisVO;
import com.wxxr.stock.restful.json.QuotationListVO;
import com.wxxr.stock.restful.resource.StockResource;

/**
 * @author wangxuyang
 * 
 */
public class InfoCenterManagementServiceImpl extends
		AbstractModule<IStockAppContext> implements
		IInfoCenterManagementService {

	private static final Trace log = Trace
			.register(InfoCenterManagementServiceImpl.class);
	private SearchStockListBean stockListbean = new SearchStockListBean();
	private StockQuotationBean stockQuotationbean = new StockQuotationBean();

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

	// ====================interface methods =====================
	@Override
	public SearchStockListBean searchStock(String keyword) {
		List<StockBaseInfoBean> list = new ArrayList<StockBaseInfoBean>();
		if (StringUtils.isEmpty(keyword)) {
			stockListbean.setSearchResult(list);
			return stockListbean;
		}
		List<StockBaseInfoBean> searchList = MockDataUtils
				.getAllMockDataForSearchStock();
		for (StockBaseInfoBean bean : searchList) {
			if (bean.toString().contains(keyword)) {
				list.add(bean);
			}
		}
		stockListbean.setSearchResult(list);
		return this.stockListbean;
	}

	private <T> T getRestService(Class<T> restResouce) {
		return context.getService(IRestProxyService.class).getRestService(
				restResouce);
	}

	@Override
	public StockMinuteKBean getMinuteline(Map<String, String> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LineListBean getDayline(String code, String market) {
		// TODO Auto-generated method stub
		return null;
	}
//=====================beans =====================
	private StockTaxisListBean stockList = new StockTaxisListBean();
	private QuotationListBean quotationListBean = new QuotationListBean();
	@Override
	public StockTaxisListBean getStocktaxis(String taxis, String orderby,
			long start, long limit) {
		final TaxisVO vo = new TaxisVO();
		vo.setLimit(limit);
		vo.setStart(start);
		vo.setOrderby(orderby);
		
		context.invokeLater(new Runnable() {
			public void run() {
				try {
					getRestService(StockResource.class).getStocktaxis(vo);
				} catch (Exception e) {
					log.warn("Error when fetch stock list", e);
				}
			}
		}, 1, TimeUnit.SECONDS);
		return null;
	}

	@Override
	public QuotationListBean getQuotations() {
		Future<QuotationListVO> future = context.getExecutor().submit(new Callable<QuotationListVO>() {
			public QuotationListVO call() throws Exception {
				QuotationListVO vo = getRestService(StockResource.class).getQuotation(null);
				return vo;
			}
		});
		try {
			QuotationListVO volist = future.get();
		}catch (Exception e) {
			log.warn("Failed to fetch quotation",e);
		}
		return quotationListBean;
	}

	@Override
	public StockQuotationBean getStockQuotation(String code, String market) {
		stockQuotationbean = MockDataUtils.getStockQuotation(code, market); 
			return stockQuotationbean;
//		return null;
	}

}
