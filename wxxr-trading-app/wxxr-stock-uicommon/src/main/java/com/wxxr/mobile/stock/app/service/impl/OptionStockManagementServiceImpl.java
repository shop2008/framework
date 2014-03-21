package com.wxxr.mobile.stock.app.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.AsyncFuture;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.common.AsyncUtils;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.common.GenericReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.IBindableEntityCache;
import com.wxxr.mobile.stock.app.common.IEntityFetcher;
import com.wxxr.mobile.stock.app.common.IEntityFilter;
import com.wxxr.mobile.stock.app.common.IEntityLoaderRegistry;
import com.wxxr.mobile.stock.app.db.OptionStock;
import com.wxxr.mobile.stock.app.service.IDBService;
import com.wxxr.mobile.stock.app.service.IOptionStockManagementService;
import com.wxxr.mobile.stock.app.service.IStockInfoSyncService;
import com.wxxr.mobile.stock.app.service.loader.OptionStockQuotationLoader;
import com.wxxr.stock.hq.ejb.api.StockQuotationVO;
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;

public class OptionStockManagementServiceImpl extends AbstractModule<IStockAppContext> implements IOptionStockManagementService {
	private static final Trace log = Trace.register("com.wxxr.mobile.stock.app.service.impl.OptionStockManagementServiceImpl");
	Map<String,OptionStock> cache = new HashMap<String, OptionStock>() ;
	
	private GenericReloadableEntityCache<String,StockQuotationBean,List<StockQuotationVO>> stockQuotationBean_cache;
	@Override
	public void add(String stockCode, String mc) {
		if (StringUtils.isBlank(stockCode)||StringUtils.isBlank(mc)) {
			return;
		}
		if (cache.containsKey(stockCode+"."+mc)) {
			throw new StockAppBizException("该股票已添加！");
		}
		OptionStock s = new OptionStock();
		s.setCreateDate(new Date());
		s.setMc(mc);
		s.setStockCode(stockCode);
		s.setNewprice(0l);
		s.setRisefallrate(0l);
		s.setPower(cache.size()+1);
		long id = getService(IDBService.class).getDaoSession().getOptionStockDao().insertOrReplace(s);
		s.setId(id);
		cache.put(stockCode+"."+mc,s);
		if(log.isDebugEnabled()){
			log.debug("Add option stock successfully:"+s.toString());
		}
		initStockQuotation(stockCode, mc);
		if (stockQuotationBean_cache!=null) {
			stockQuotationBean_cache.notifyDataChanged();
		}
		
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
		OptionStock optionStock = cache.remove(stockCode+"."+mc);
		if (optionStock!=null) {
			getService(IDBService.class).getDaoSession().getOptionStockDao().delete(optionStock);
			if (stockQuotationBean_cache!=null) {
				stockQuotationBean_cache.removeEntity(mc+stockCode);
				stockQuotationBean_cache.notifyDataChanged();
			}
		}
	}
	@Override
	public OptionStock find(String stockCode, String mc) {
		OptionStock stock = cache.get(stockCode+"."+mc);
		if (stock!=null) {
			return stock;
		}
		List<OptionStock> stocks = getService(IDBService.class).getDaoSession().getOptionStockDao().queryRaw(" where STOCK_CODE=? and MC=?", stockCode,mc);
		if (stocks!=null&&stocks.size()==1) {
			return stocks.get(0);
		}
		return null;
	}
	@Override
	public void update(OptionStock optionStock) {
		if (optionStock!=null) {
			getService(IDBService.class).getDaoSession().getOptionStockDao().update(optionStock);
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
	
	private IEntityFilter<StockQuotationBean> filter = new IEntityFilter<StockQuotationBean>() {

		@Override
		public boolean doFilter(StockQuotationBean entity) {
			return cache.containsKey(entity.getCode()+"."+entity.getMarket());
		}
	};
	@Override
	public BindableListWrapper<StockQuotationBean> getMyOptionStocks(String taxis, String orderby) {
		OptionStock[] stocks = getMyOptionStock();
		if (stocks!=null&&stocks.length>0) {
			for (OptionStock optionStock : stocks) {
				initStockQuotation(optionStock.getStockCode(), optionStock.getMc());
			}
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("options", stocks);
		if(this.myStocks == null){
			this.myStocks = this.stockQuotationBean_cache.getEntities(filter, new MyOptionStockComparator(taxis,orderby));
		}else{
			MyOptionStockComparator comp = (MyOptionStockComparator)this.myStocks.getComparator();
			comp.setFieldName(taxis);
			comp.setOrder(orderby);
			this.stockQuotationBean_cache.notifyDataChanged();
		}
		this.stockQuotationBean_cache.doReload(true, map, null);
		return this.myStocks;
	}
	
    private void initStockQuotation(String code, String market) {//初始化自选股，不加载行情
		if (StringUtils.isBlank(market)||StringUtils.isBlank(code)) {
			return;
		}
	    String mc=market+code;
        StockQuotationBean b =null;
	    if ((b=stockQuotationBean_cache.getEntity(mc))==null){
	        b=new StockQuotationBean();
	        OptionStock stock = null;
	        if (cache!=null&& (stock=cache.get(code+"."+market))!=null) {
	        	StockBaseInfo sInfo = getService(IStockInfoSyncService.class).getStockBaseInfoByCode(code, market);
	        	if (sInfo!=null) {
	        		b.setStockName(sInfo.getName());
				}
	        	b.setMarket(market);
	        	b.setCode(code);
	        	b.setPower(stock.getPower());
	        	b.setNewprice(stock.getNewprice()==null?0:stock.getNewprice());
	        	b.setRisefallrate(stock.getRisefallrate()==null?0:stock.getRisefallrate());
	        	b.setAdded(true);
			}
            stockQuotationBean_cache.putEntity(mc,b);
        }else{        	
        	OptionStock stock = null;
	        if (cache!=null&& (stock=cache.get(code+"."+market))!=null) {
	        	b.setPower(stock.getPower());	
	        }
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
	    registry.registerEntityLoader("myOptionStock", new OptionStockQuotationLoader());
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
	@Override
	public StockQuotationBean getOptionStockQuotation(String code, String market) {
		if (StringUtils.isBlank(market)||StringUtils.isBlank(code)) {
			return new StockQuotationBean();//fix闪退问题
		}
	    final String mc=market+code;
        final Map<String, Object> params=new HashMap<String, Object>(); 
        params.put("code", code);
        params.put("market", market);
	    if (stockQuotationBean_cache.getEntity(market+code)==null){
	        AsyncUtils.forceLoadNFetchAsyncInUI(this.stockQuotationBean_cache, params, new AsyncFuture<StockQuotationBean>(), new IEntityFetcher<StockQuotationBean>() {

				@Override
				public StockQuotationBean fetchFromCache(
						IBindableEntityCache<?, ?> cache) {
					return (StockQuotationBean)cache.getEntity(mc);
				}
			});
        }else{
            this.stockQuotationBean_cache.doReload(false,params,null);
        }
        return stockQuotationBean_cache.getEntity(mc);
	}
}
