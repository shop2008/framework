package com.wxxr.stock.restful.resource;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.rpc.rest.ResteasyRestClientService;
import com.wxxr.stock.hq.ejb.api.StockMinuteKVO;
import com.wxxr.stock.hq.ejb.api.StockQuotationVO;
import com.wxxr.stock.hq.ejb.api.TaxisVO;
import com.wxxr.stock.notification.ejb.api.MessageVO;
import com.wxxr.stock.restful.json.BaseInfoListVO;
import com.wxxr.stock.restful.json.ComponentstocksListVO;
import com.wxxr.stock.restful.json.LineListVO;
import com.wxxr.stock.restful.json.ParamVO;
import com.wxxr.stock.restful.json.PlateTaxisListVO;
import com.wxxr.stock.restful.json.QuotationListVO;
import com.wxxr.stock.restful.json.StockTaxisListVO;


public class StockResourceTest  extends TestCase{

	private IRestProxyService restService=new ResteasyRestClientService();
	
	public void testGetAllStockInfo() throws Exception{
		BaseInfoListVO a = restService.getRestService(StockResource.class).getAllStockInfo();
	}
	
	public void testGetQuotation()throws Exception{
		List<ParamVO> list = new ArrayList<ParamVO>();
		ParamVO p = new ParamVO();
		p.setMarket("SH");
		p.setCode("600000");
		list.add(p);
		QuotationListVO a = restService.getRestService(StockResource.class).getQuotation(list);
	}
	
	public void testGetQuotation1()throws Exception{
		List<ParamVO> list = new ArrayList<ParamVO>();
		ParamVO p = new ParamVO();
		p.setMarket("SH");
		p.setCode("600000");
		list.add(p);
		QuotationListVO a = restService.getRestService(StockResource.class).getQuotation("SH","600000");
	}

	public void testGetMinuteline()throws Exception{
		ParamVO p = new ParamVO();
		p.setMarket("SH");
		p.setCode("600000");
		StockMinuteKVO a = restService.getRestService(StockResource.class).getMinuteline(p);
	}
	   
	public void testGetDayline()throws Exception{
		ParamVO p = new ParamVO();
		p.setMarket("SH");
		p.setCode("600000");
		p.setStart(0L);
		p.setLimit(10L);
		LineListVO a = restService.getRestService(StockResource.class).getDayline(p);
	}
	
	public void testGetFiveDayMinuteline()throws Exception{
		ParamVO p = new ParamVO();
		p.setMarket("SH");
		p.setCode("600000");
		p.setStart(0L);
		p.setLimit(10L);
		List<StockMinuteKVO> a = restService.getRestService(StockResource.class).getFiveDayMinuteline(p);
	}
	
	public void testGetStockHQListByBlockId()throws Exception{
		TaxisVO p = new TaxisVO();
		p.setOrderby("desc");
		p.setStart(0L);
		p.setLimit(10L);
		p.setBlockId(47503L);
		p.setTaxis("risefallrate");
		List<StockQuotationVO> a = restService.getRestService(StockResource.class).getStockHQListByBlockId(p);
	}
	
	public void testGetWeekline()throws Exception{
		ParamVO p = new ParamVO();
		p.setMarket("SH");
		p.setCode("600000");
		p.setStart(0L);
		p.setLimit(10L);
		LineListVO a = restService.getRestService(StockResource.class).getWeekline(p);
	}
	public void testGetMonthline()throws Exception{
		ParamVO p = new ParamVO();
		p.setMarket("SH");
		p.setCode("600000");
		p.setStart(0L);
		p.setLimit(10L);
		LineListVO a = restService.getRestService(StockResource.class).getMonthline(p);
	}
	public void testGetStocktaxis()throws Exception{
		TaxisVO p = new TaxisVO();
		p.setOrderby("desc");
		p.setStart(0L);
		p.setLimit(10L);
		p.setBlockId(47503L);
		p.setTaxis("risefallrate");
		StockTaxisListVO a = restService.getRestService(StockResource.class).getStocktaxis(p);
	}
	public void testGetPlatetaxis()throws Exception{
		TaxisVO p = new TaxisVO();
		p.setOrderby("desc");
		p.setStart(0L);
		p.setLimit(10L);
		p.setBlockId(47503L);
		p.setTaxis("risefallrate");
		PlateTaxisListVO a = restService.getRestService(StockResource.class).getPlatetaxis(p);
	}
	public void testGetRefenceBlockHQ()throws Exception{
		List<ParamVO> list = new ArrayList<ParamVO>();
		ParamVO p = new ParamVO();
		p.setMarket("SH");
		p.setCode("600000");
		list.add(p);
		PlateTaxisListVO a = restService.getRestService(StockResource.class).getRefenceBlockHQ(list);
	}


    //public ComponentstocksListVO getComponentstocks(ParamVO paramVO) throws Exception ;
    public void testGetComponentstocks()throws Exception{
		ParamVO p = new ParamVO();
		p.setMarket("SH");
		p.setCode("600000");
		p.setStart(0L);
		p.setLimit(10L);
		ComponentstocksListVO a = restService.getRestService(StockResource.class).getComponentstocks(p);
	}

    //public StockTaxisListVO getIndexPreview(TaxisVO vo) throws Exception;
    public void testGetIndexPreview()throws Exception{
		TaxisVO p = new TaxisVO();
		p.setOrderby("desc");
		p.setStart(0L);
		p.setLimit(10L);
		p.setBlockId(47503L);
		p.setTaxis("risefallrate");
		StockTaxisListVO a = restService.getRestService(StockResource.class).getIndexPreview(p);
	}
}
