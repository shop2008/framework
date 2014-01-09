/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.bean.LineListBean;
import com.wxxr.mobile.stock.app.bean.QuotationListBean;
import com.wxxr.mobile.stock.app.bean.SearchStockListBean;
import com.wxxr.mobile.stock.app.bean.StockLineBean;
import com.wxxr.mobile.stock.app.bean.StockMinuteKBean;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.bean.StockTaxisBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.common.GenericReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.IEntityFilter;
import com.wxxr.mobile.stock.app.common.IEntityLoaderRegistry;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.app.service.IStockInfoSyncService;
import com.wxxr.mobile.stock.app.service.loader.DayStockLineLoader;
import com.wxxr.mobile.stock.app.service.loader.FiveDayStockMinuteKLoader;
import com.wxxr.mobile.stock.app.service.loader.StockMinuteKLoader;
import com.wxxr.mobile.stock.app.service.loader.StockQuotationLoader;
import com.wxxr.mobile.stock.app.service.loader.StockTaxisLoader;
import com.wxxr.stock.hq.ejb.api.StockTaxisVO;
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;
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
			.register("com.wxxr.mobile.stock.app.service.impl.InfoCenterManagementServiceImpl");
	private static class StockTaxisComparator implements Comparator<StockTaxisBean> {

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

		public StockTaxisComparator(String name, String order){
			this.fieldName = name;
			this.order = order;
		}

		@Override
		public int compare(StockTaxisBean o1, StockTaxisBean o2) {
			Long v1 = null;
			Long v2 = null;
			if("newprice".equals(this.fieldName)){
				v1 = o1.getNewprice();
				v2 = o2.getNewprice();
			}else if("risefallrate".equals(this.fieldName)){
				v1 = o1.getRisefallrate();
				v2 = o2.getRisefallrate();
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
	private SearchStockListBean stockListbean = new SearchStockListBean();
	private StockQuotationBean stockQuotationbean = new StockQuotationBean();
	protected LineListBean lineListBean = new LineListBean();
	
	// ====================module life cycle methods ==================
	@Override
	protected void initServiceDependency() {
		addRequiredService(IRestProxyService.class);
        addRequiredService(IEntityLoaderRegistry.class);

	}

	@Override
	protected void startService() {
	    
		context.registerService(IInfoCenterManagementService.class, this);
        IEntityLoaderRegistry registry = getService(IEntityLoaderRegistry.class);
        stockQuotationBean_cache=new GenericReloadableEntityCache<String, StockQuotationBean, List>("StockQuotation",30);
        context.getService(IEntityLoaderRegistry.class).registerEntityLoader("StockQuotation", new StockQuotationLoader());
        
        
        stockMinuteKBean_cache=new GenericReloadableEntityCache<String, StockMinuteKBean, List>("StockMinuteK");
        context.getService(IEntityLoaderRegistry.class).registerEntityLoader("StockMinuteK", new StockMinuteKLoader());
        fiveDaystockMinuteKBean_cache=new GenericReloadableEntityCache<String, StockMinuteKBean, List>("FiveDayStockMinuteK");
        context.getService(IEntityLoaderRegistry.class).registerEntityLoader("FiveDayStockMinuteK", new FiveDayStockMinuteKLoader());
        
        dayStockLineBean_cache=new GenericReloadableEntityCache<String, StockLineBean, List>("DayStockLine");
        context.getService(IEntityLoaderRegistry.class).registerEntityLoader("DayStockLine", new DayStockLineLoader());
        context.getService(IEntityLoaderRegistry.class).registerEntityLoader("StockTaxis", new StockTaxisLoader());
	}

	@Override
	protected void stopService() {
		context.unregisterService(IInfoCenterManagementService.class, this);
	}

	// ====================interface methods =====================
	@Override
	public SearchStockListBean searchStock(final String keyword) {
		List<StockBaseInfo> ret = getService(IStockInfoSyncService.class).getStockInfos(new IEntityFilter<StockBaseInfo>() {
			public boolean doFilter(StockBaseInfo entity) {
				if (StringUtils.isBlank(keyword)) {
					return false;
				}
				return (entity.getType()==1||entity.getType()==2)&&(entity.getAbbr().startsWith(keyword)||entity.getCode().startsWith(keyword));
			}
		});
		
		stockListbean.setSearchResult(ret);
		return this.stockListbean;
	}

	private <T> T getRestService(Class<T> restResouce) {
		return context.getService(IRestProxyService.class).getRestService(
				restResouce);
	}
	//分钟线
	@Override
	public StockMinuteKBean getMinuteline(Map<String, String> params) {
		if(params == null)
			return null;
	   String market= params.get("market");
	   String code= params.get("code");
	   if (StringUtils.isBlank(market) || StringUtils.isBlank(code) ){
	       return null;
	   }
	    String mc=market+code;
        if (stockMinuteKBean_cache.getEntity(mc)==null){
            StockMinuteKBean b=new StockMinuteKBean();
            b.setMarket(market);
            b.setCode(code);
            stockMinuteKBean_cache.putEntity(mc,b);
        }
        Map<String, Object> p=new HashMap<String, Object>(); 
        p.put("code", code);
        p.put("market", market);
        this.stockMinuteKBean_cache.forceReload(p,true);
        return stockMinuteKBean_cache.getEntity(mc);
	}
	//K线
	@Override
	public LineListBean getDayline(String code, String market) {
     
		return null;
	}
	 public BindableListWrapper<StockLineBean> getDayStockline(final String code, final String market){
	     if (StringUtils.isBlank(market) || StringUtils.isBlank(code) ){
             return null;
         }
	     BindableListWrapper<StockLineBean> dayline = dayStockLineBean_cache.getEntities(new IEntityFilter<StockLineBean>(){
	            @Override
	            public boolean doFilter(StockLineBean entity) {
	                if (entity.getMarket().equals(market) && entity.getCode().equals(code)){
	                    return true;
	                }
	                return false;
	            }
	            
	        }, new  StockLineBeanComparator());
	     
          Map<String, Object> p=new HashMap<String, Object>(); 
          p.put("code", code);
          p.put("market", market);
          this.dayStockLineBean_cache.forceReload(p,true);
          dayStockLineBean_cache.setCommandParameters(p);
          return dayline;

	 }
	class StockLineBeanComparator implements Comparator<StockLineBean>{
        @Override
        public int compare(StockLineBean b1, StockLineBean b2) {
            if (b1.getDate()!=null &&b2.getDate()!=null ){
                Long d1=Long.valueOf(b1.getDate());
                Long d2= Long.valueOf(b2.getDate());
                return d1>d2?-1:1;
            }
            return 0;
        }
	}
	//行情信息
    private GenericReloadableEntityCache<String,StockQuotationBean,List> stockQuotationBean_cache;
    //分钟线信息
    private GenericReloadableEntityCache<String,StockMinuteKBean,List> stockMinuteKBean_cache;
    //5日分钟线信息
    private GenericReloadableEntityCache<String,StockMinuteKBean,List> fiveDaystockMinuteKBean_cache;
    //日K
    private GenericReloadableEntityCache<String,StockLineBean,List> dayStockLineBean_cache;
    
	//查询股票行情
    private GenericReloadableEntityCache<String,StockTaxisBean,StockTaxisVO> stockTaxis_cache;
    
    private BindableListWrapper<StockTaxisBean> stockTaxisList;
	@Override
    public StockQuotationBean getStockQuotation(String code, String market) {
		if (StringUtils.isBlank(market)||StringUtils.isBlank(code)) {
			return new StockQuotationBean();//fix闪退问题
		}
	    String mc=market+code;
	    if (stockQuotationBean_cache.getEntity(market+code)==null){
	        StockQuotationBean b=new StockQuotationBean();
            stockQuotationBean_cache.putEntity(mc,b);
        }
        Map<String, Object> params=new HashMap<String, Object>(); 
        params.put("code", code);
        params.put("market", market);

        this.stockQuotationBean_cache.forceReload(params,true);
        return stockQuotationBean_cache.getEntity(mc);
    }
	@Override
    public StockQuotationBean getSyncStockQuotation(String code, String market) {
		if (StringUtils.isBlank(market)||StringUtils.isBlank(code)) {
			return new StockQuotationBean();//fix闪退问题
		}
	    String mc=market+code;
	    if (stockQuotationBean_cache.getEntity(market+code)==null){
	        StockQuotationBean b=new StockQuotationBean();
            stockQuotationBean_cache.putEntity(mc,b);
        }
        Map<String, Object> params=new HashMap<String, Object>(); 
        params.put("code", code);
        params.put("market", market);

        this.stockQuotationBean_cache.forceReload(params,true);
        return stockQuotationBean_cache.getEntity(mc);
    }
//=====================beans =====================
	private QuotationListBean quotationListBean = new QuotationListBean();
	
	@Override
	public void reloadStocktaxis(String taxis, String orderby,long start, long limit){
		if(stockTaxis_cache == null){
			this.stockTaxis_cache = new GenericReloadableEntityCache<String, StockTaxisBean, StockTaxisVO>("StockTaxis");
		}
		if(this.stockTaxisList == null){
			this.stockTaxisList = this.stockTaxis_cache.getEntities(null, new StockTaxisComparator(taxis,orderby));
		}else{
			StockTaxisComparator comp = (StockTaxisComparator)this.stockTaxisList.getComparator();
			comp.setFieldName(taxis);
			comp.setOrder(orderby);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("taxis",taxis);
		params.put("orderby", orderby);
		params.put("start", (long)start);
		params.put("limit", (long)limit);
		this.stockTaxis_cache.forceReload(params, true);
	}
	
	
	@Override
	public BindableListWrapper<StockTaxisBean> getStocktaxis(final String taxis, final String orderby,
			long start, long limit) {
		if(stockTaxis_cache == null){
			this.stockTaxis_cache = new GenericReloadableEntityCache<String, StockTaxisBean, StockTaxisVO>("StockTaxis");
		}
		if(this.stockTaxisList == null){
			this.stockTaxisList = this.stockTaxis_cache.getEntities(null, new StockTaxisComparator(taxis,orderby));
		}else{
			StockTaxisComparator comp = (StockTaxisComparator)this.stockTaxisList.getComparator();
			comp.setFieldName(taxis);
			comp.setOrder(orderby);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("taxis",taxis);
		params.put("orderby", orderby);
		params.put("start", (long)start);
		params.put("limit", (long)limit);
		this.stockTaxis_cache.doReloadIfNeccessay(params);
		return this.stockTaxisList;
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
	
	

	public BindableListWrapper<StockMinuteKBean> getFiveDayMinuteline(final String code, final String market) {
	    BindableListWrapper<StockMinuteKBean> stockMinuteKBeans = fiveDaystockMinuteKBean_cache.getEntities(new IEntityFilter<StockMinuteKBean>(){
            @Override
            public boolean doFilter(StockMinuteKBean entity) {
                if (entity.getMarket().equals(market) && entity.getCode().equals(code)){
                    return true;
                }
                return false;
            }
            
        }, new StockMinuteKBeanComparator());
	    
	        Map<String, Object> p=new HashMap<String, Object>(); 
	        p.put("code", code);
	        p.put("market", market);
	        this.fiveDaystockMinuteKBean_cache.forceReload(p,true);
		return stockMinuteKBeans;
	}
	class StockMinuteKBeanComparator implements Comparator<StockMinuteKBean>{
        @Override
        public int compare(StockMinuteKBean b1, StockMinuteKBean b2) {
            if (b1.getDate()!=null &&b2.getDate()!=null ){
                Long d1=Long.valueOf(b1.getDate());
                Long d2= Long.valueOf(b2.getDate());
                return d1>d2?-1:1;
            }
            return 0;
        }
    }

}
