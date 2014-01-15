/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.util.LRUMap;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.bean.LineListBean;
import com.wxxr.mobile.stock.app.bean.SearchStockListBean;
import com.wxxr.mobile.stock.app.bean.StockLineBean;
import com.wxxr.mobile.stock.app.bean.StockMinuteKBean;
import com.wxxr.mobile.stock.app.bean.StockMinuteLineBean;
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
import com.wxxr.stock.hq.ejb.api.StockLineVO;
import com.wxxr.stock.hq.ejb.api.StockMinuteKVO;
import com.wxxr.stock.hq.ejb.api.StockQuotationVO;
import com.wxxr.stock.hq.ejb.api.StockTaxisVO;
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;

/**
 * @author wangxuyang
 * 
 */
public class InfoCenterManagementServiceImpl extends
		AbstractModule<IStockAppContext> implements
		IInfoCenterManagementService {

	private static final Trace log = Trace.register("com.wxxr.mobile.stock.app.service.impl.InfoCenterManagementServiceImpl");
	
	@SuppressWarnings("unused")
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
	protected LineListBean lineListBean = new LineListBean();
	private LRUMap<String, BindableListWrapper<StockLineBean>> dayLineCache = new LRUMap<String, BindableListWrapper<StockLineBean>>(100, 10*60);
	private LRUMap<String, BindableListWrapper<StockMinuteKBean>> fiveDayMinKCache = new LRUMap<String, BindableListWrapper<StockMinuteKBean>>(100, 10*60);
	
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
        stockQuotationBean_cache=new GenericReloadableEntityCache<String, StockQuotationBean, List<StockQuotationVO>>("StockQuotation",30);
        registry.registerEntityLoader("StockQuotation", new StockQuotationLoader());
        
        
        stockMinuteKBean_cache=new GenericReloadableEntityCache<String, StockMinuteKBean, List<StockMinuteKVO>>("StockMinuteK");
        registry.registerEntityLoader("StockMinuteK", new StockMinuteKLoader());
        fiveDaystockMinuteKBean_cache=new GenericReloadableEntityCache<String, StockMinuteKBean, List<StockMinuteKVO>>("FiveDayStockMinuteK");
        registry.registerEntityLoader("FiveDayStockMinuteK", new FiveDayStockMinuteKLoader());
        
        dayStockLineBean_cache=new GenericReloadableEntityCache<String, StockLineBean, List<StockLineVO>>("DayStockLine");
        registry.registerEntityLoader("DayStockLine", new DayStockLineLoader());
        registry.registerEntityLoader("StockTaxis", new StockTaxisLoader());
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
	//分钟线
	public StockMinuteKBean getMinuteline(Map<String, String> params,boolean wait4finish) {
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
        this.stockMinuteKBean_cache.forceReload(p,wait4finish);
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
	     String key = new StringBuffer().append(market).append(code).toString();
	     
	     BindableListWrapper<StockLineBean> dayline = dayLineCache.get(key);
	     if(dayline == null) {
	    	 dayline = dayStockLineBean_cache.getEntities(new IEntityFilter<StockLineBean>(){
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
	          this.dayStockLineBean_cache.forceReload(p,false);
	          dayStockLineBean_cache.setCommandParameters(p);
	          dayline.setReloadParameters(p);
	          this.dayLineCache.put(key, dayline);
	     }else{
	    	 this.dayStockLineBean_cache.doReloadIfNeccessay(dayline.getReloadParameters());
	     }
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
    private GenericReloadableEntityCache<String,StockQuotationBean,List<StockQuotationVO>> stockQuotationBean_cache;
    //分钟线信息
    private GenericReloadableEntityCache<String,StockMinuteKBean,List<StockMinuteKVO>> stockMinuteKBean_cache;
    //5日分钟线信息
    private GenericReloadableEntityCache<String,StockMinuteKBean,List<StockMinuteKVO>> fiveDaystockMinuteKBean_cache;
    //日K
    private GenericReloadableEntityCache<String,StockLineBean,List<StockLineVO>> dayStockLineBean_cache;
    
	//查询股票行情
    private GenericReloadableEntityCache<String,StockTaxisBean,StockTaxisVO> stockTaxis_cache;
    
    private BindableListWrapper<StockTaxisBean> stockTaxisList;
	@Override
    public StockQuotationBean getStockQuotation(String code, String market) {
		if (StringUtils.isBlank(market)||StringUtils.isBlank(code)) {
			return new StockQuotationBean();//fix闪退问题
		}
	    String mc=market+code;
        Map<String, Object> params=new HashMap<String, Object>(); 
        params.put("code", code);
        params.put("market", market);
	    if (stockQuotationBean_cache.getEntity(market+code)==null){
	        StockQuotationBean b=new StockQuotationBean();
            stockQuotationBean_cache.putEntity(mc,b);
            this.stockQuotationBean_cache.forceReload(params,false);
        }else{
            this.stockQuotationBean_cache.doReloadIfNeccessay(params);

        }
         return stockQuotationBean_cache.getEntity(mc);
    }
	
	@Override
    public StockQuotationBean getSyncStockQuotation(String code, String market) {
		if (StringUtils.isBlank(market)||StringUtils.isBlank(code)) {
			return new StockQuotationBean();//fix闪退问题
		}
	    String mc=market+code;
        Map<String, Object> params=new HashMap<String, Object>(); 
        params.put("code", code);
        params.put("market", market);
	    if (stockQuotationBean_cache.getEntity(market+code)==null){
	        StockQuotationBean b=new StockQuotationBean();
            stockQuotationBean_cache.putEntity(mc,b);
            try {
            	Thread.sleep(500);
            } catch (InterruptedException e) {}
            this.stockQuotationBean_cache.forceReload(params,true);
        }else{
        	this.stockQuotationBean_cache.doReloadIfNeccessay(params);
        }
        return stockQuotationBean_cache.getEntity(mc);
    }
//=====================beans =====================
	
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

	public BindableListWrapper<StockMinuteKBean> getFiveDayMinuteline(final String code, final String market) {
	     if (StringUtils.isBlank(market) || StringUtils.isBlank(code) ){
             return null;
         }
	     String key = new StringBuffer().append(market).append(code).toString();

	    BindableListWrapper<StockMinuteKBean> stockMinuteKBeans = this.fiveDayMinKCache.get(key);
	    if(stockMinuteKBeans == null) {
	    	stockMinuteKBeans = fiveDaystockMinuteKBean_cache.getEntities(new IEntityFilter<StockMinuteKBean>(){
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
	        stockMinuteKBeans.setReloadParameters(p);
	        this.fiveDaystockMinuteKBean_cache.forceReload(p,false);
	        this.fiveDayMinKCache.put(key, stockMinuteKBeans);
	    }else{
	    	 this.fiveDaystockMinuteKBean_cache.doReloadIfNeccessay(stockMinuteKBeans.getReloadParameters());
	    }
	    
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
