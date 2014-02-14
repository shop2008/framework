package com.wxxr.mobile.stock.app.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.common.GenericReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.IEntityLoaderRegistry;
import com.wxxr.mobile.stock.app.db.OptionStock;
import com.wxxr.mobile.stock.app.service.IDBService;
import com.wxxr.mobile.stock.app.service.IOptionStockManagementService;
import com.wxxr.mobile.stock.app.service.loader.StockQuotationLoader;
import com.wxxr.stock.hq.ejb.api.StockQuotationVO;

public class OptionStockManagementServiceImpl extends AbstractModule<IStockAppContext> implements IOptionStockManagementService {
	private static final Trace log = Trace.register("com.wxxr.mobile.stock.app.service.impl.OptionStockManagementServiceImpl");
	Map<String,OptionStock> cache;
	
	private GenericReloadableEntityCache<String,StockQuotationBean,List<StockQuotationVO>> stockQuotationBean_cache;
	@Override
	public void add(String stockCode, String mc) {
		if (StringUtils.isBlank(stockCode)||StringUtils.isBlank(mc)) {
			return;
		}
		if (cache==null) {
			cache = new HashMap<String, OptionStock>();
		}
		if (cache.containsKey(stockCode+"."+mc)) {
			throw new StockAppBizException("该股票已添加！");
		}
		OptionStock s = new OptionStock();
		s.setCreateDate(new Date());
		s.setMc(mc);
		s.setStockCode(stockCode);
		s.setPower(cache.size()+1);
		long id = getService(IDBService.class).getDaoSession().getOptionStockDao().insertOrReplace(s);
		s.setId(id);
		cache.put(stockCode+"."+mc,s);
	}
	@Override
	public void updateOrder(Map<String, Integer> orders) {
		if (orders!=null&&orders.size()>0) {
			for (Map.Entry<String, Integer> entry : orders.entrySet()) {
				OptionStock stock = cache.get(entry.getKey());
				stock.setPower(entry.getValue());
				getService(IDBService.class).getDaoSession().getOptionStockDao().update(stock);
			}
		}
	}

	@Override
	public void delete(String stockCode, String mc) {
		List<OptionStock> stocks = getService(IDBService.class).getDaoSession().getOptionStockDao().queryRaw(" where STOCK_CODE=? and MC=?", stockCode,mc);
		if (log.isDebugEnabled()) {
			log.debug("stocks:"+stocks);
		}
		if (stocks!=null&&stocks.size()==1) {
			getService(IDBService.class).getDaoSession().getOptionStockDao().delete(stocks.get(0));
		}
		cache.remove(stockCode+"."+mc);
		if (stockQuotationBean_cache!=null) {
			stockQuotationBean_cache.removeEntity(mc+stockCode);
		}

	}

	
	private OptionStock[] getMyOptionStock() {
		if (cache==null||cache.isEmpty()) {
			loadMyStocks();
		}
		if (cache!=null) {
			return cache.values().toArray(new OptionStock[cache.size()]);
		}
		return null;
	}
	@SuppressWarnings("unused")
	private static class MyOptionStockComparator implements Comparator<StockQuotationBean> {

		private String fieldName,order;

		/**
		 * @return the fieldName
		 */
		public String getFieldName() {
			return fieldName;
		}

		/**
		 * @return the order
		 */
		public String getOrder() {
			return order;
		}

		/**
		 * @param fieldName the fieldName to set
		 */
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

		/**
		 * @param order the order to set
		 */
		public void setOrder(String order) {
			this.order = order;
		}

		public MyOptionStockComparator(String name, String order){
			this.fieldName = name;
			this.order = order;
		}

		@Override
		public int compare(StockQuotationBean o1, StockQuotationBean o2) {
			Long v1 = null;
			Long v2 = null;
			if("newprice".equals(this.fieldName)){
				v1 = o1.getNewprice();
				v2 = o2.getNewprice();
			}else if("risefallrate".equals(this.fieldName)){
				v1 = o1.getRisefallrate();
				v2 = o2.getRisefallrate();
			}else{
				v1= o1.getPower()==null?0l:Long.valueOf(o1.getPower());
				v2= o2.getPower()==null?0l:Long.valueOf(o2.getPower());
				return v1.compareTo(v2);
			}
			if(v1 == null){
				v1 = 0L;
			}
			if(v2 == null){
				v2 = 0L;
			}
			if("desc".equals(order)){
				return v2.compareTo(v1);
			}else{
				return v1.compareTo(v2);
			}
		}
	}
	private BindableListWrapper<StockQuotationBean> myStocks;
	@Override
	public BindableListWrapper<StockQuotationBean> getMyOptionStocks(String taxis, String orderby) {
		OptionStock[] stocks = getMyOptionStock();
		if (stocks!=null&&stocks.length>0) {
			for (OptionStock optionStock : stocks) {
				loadStockQuotation(optionStock.getStockCode(), optionStock.getMc());
			}
		}
		if(this.myStocks == null){
			this.myStocks = this.stockQuotationBean_cache.getEntities(null, new MyOptionStockComparator(taxis,orderby));
		}else{
			MyOptionStockComparator comp = (MyOptionStockComparator)this.myStocks.getComparator();
			comp.setFieldName(taxis);
			comp.setOrder(orderby);
		}
		return this.myStocks;
	}
    private void loadStockQuotation(String code, String market) {
		if (StringUtils.isBlank(market)||StringUtils.isBlank(code)) {
			return;
		}
	    String mc=market+code;
        Map<String, Object> params=new HashMap<String, Object>(); 
        params.put("code", code);
        params.put("market", market);
        StockQuotationBean b =null;
	    if ((b=stockQuotationBean_cache.getEntity(mc))==null){
	        b=new StockQuotationBean();
	        OptionStock stock = null;
	        if (cache!=null&& (stock=cache.get(code+"."+market))!=null) {
	        	b.setPower(stock.getPower());
			}
            stockQuotationBean_cache.putEntity(mc,b);
            this.stockQuotationBean_cache.forceReload(params,true);
        }else{        	
        	OptionStock stock = null;
 	        if (cache!=null&& (stock=cache.get(code+"."+market))!=null) {
 	        	b.setPower(stock.getPower());
 			}
        	this.stockQuotationBean_cache.doReloadIfNeccessay(params,true);
        }
       
    }
    private Comparator<OptionStock> c = new Comparator<OptionStock>() {
		@Override
		public int compare(OptionStock o1, OptionStock o2) {
			if (o1.getPower()!=null&&o2.getPower()!=null) {
				return o1.getPower().compareTo(o2.getPower());
			}
			return 0;
		}
	};
	private void loadMyStocks(){
		if (!getService(IUserIdentityManager.class).isUserAuthenticated()) {
			return;
		}
		
		List<OptionStock> stocks = getService(IDBService.class).getDaoSession().getOptionStockDao().loadAll();
		if (stocks!=null&&stocks.size()>0) {
			Collections.sort(stocks,c);
			if (cache==null) {
				cache = new HashMap<String, OptionStock>();
			}
			for (OptionStock optionStock : stocks) {
				cache.put(optionStock.getStockCode()+"."+optionStock.getMc(),optionStock);
			}
		}
	}


	@Override
	protected void initServiceDependency() {
		addRequiredService(IDBService.class);
		addRequiredService(IUserIdentityManager.class);
		addRequiredService(IEntityLoaderRegistry.class);
	}

	@Override
	protected void startService() {
		context.registerService(IOptionStockManagementService.class, this);
		IEntityLoaderRegistry registry = getService(IEntityLoaderRegistry.class);
	    stockQuotationBean_cache=new GenericReloadableEntityCache<String, StockQuotationBean, List<StockQuotationVO>>("myOptionStock",30);
	    registry.registerEntityLoader("myOptionStock", new StockQuotationLoader());
	    doInit();
	   
	}

	@Override
	protected void stopService() {
		context.unregisterService(IOptionStockManagementService.class, this);
		cache.clear();
		cache = null;
		if (stockQuotationBean_cache!=null) {
				stockQuotationBean_cache.clear();
				stockQuotationBean_cache = null;
		}
	}

	protected void doInit() {
		if (cache!=null) {
			cache.clear();
		}
		if (stockQuotationBean_cache!=null) {
			stockQuotationBean_cache.clear();
		}
		loadMyStocks();
	}
	@Override
	public boolean isAdded(String stockCode, String mc) {
		return cache!=null&&cache.containsKey(stockCode+"."+mc);
	}

	
	
}
