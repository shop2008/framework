/**
 * 
 */
package com.wxxr.mobile.stock.app.mock;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.MegagameRankBean;
import com.wxxr.mobile.stock.app.bean.RegularTicketBean;
import com.wxxr.mobile.stock.app.bean.StockBaseInfoBean;
import com.wxxr.mobile.stock.app.bean.TradingAccInfoBean;
import com.wxxr.mobile.stock.app.bean.TradingRecordBean;
import com.wxxr.mobile.stock.app.bean.WeekRankBean;

/**
 * @author wangxuyang
 * 
 */
public class MockDataUtils {
	// ========================mock data ======================
	public static List<TradingAccInfoBean> mockData(int type) {
		List<TradingAccInfoBean> list = new ArrayList<TradingAccInfoBean>();
		TradingAccInfoBean t0ta = null;
		switch (type) {
		case 0:
			// ====mock T日实盘，未结算，盈利
			t0ta = new TradingAccInfoBean();
			t0ta.setAcctID(1L);
			t0ta.setCreateDate(0);
			t0ta.setMaxStockCode("1233");
			t0ta.setMaxStockMarket("上海");
			t0ta.setMaxStockName("中华人寿");
			t0ta.setOver("UNCLOSED");
			t0ta.setStatus(1);
			t0ta.setSum(2l);
			t0ta.setTotalGain(3l);
			t0ta.setVirtual(true);
			list.add(t0ta);
			break;
		case 1:
			// ===== mock T+1日模拟盘，已结算
			t0ta = new TradingAccInfoBean();
			t0ta.setAcctID(1L);
			t0ta.setCreateDate(0);
			t0ta.setMaxStockCode("601169");
			t0ta.setMaxStockMarket("601169");
			t0ta.setMaxStockName("北京银行");
			t0ta.setOver("CLOSED");
			t0ta.setStatus(0);
			t0ta.setSum(10L);
			t0ta.setTotalGain(1253L);
			t0ta.setVirtual(true);
			list.add(t0ta);

			// ==== mock T+1日实盘、未结算，盈利
			t0ta = new TradingAccInfoBean();
			t0ta.setAcctID(1L);
			t0ta.setCreateDate(0);
			t0ta.setMaxStockCode("601169");
			t0ta.setMaxStockMarket("601169");
			t0ta.setMaxStockName("北京银行");
			t0ta.setOver("UNCLOSE");
			t0ta.setStatus(0);
			t0ta.setSum(12L);
			t0ta.setTotalGain(12343l);
			t0ta.setVirtual(false);
			list.add(t0ta);
			// ==== mock T+1日实盘、未结算，亏损
			t0ta = new TradingAccInfoBean();
			t0ta.setAcctID(1L);
			t0ta.setCreateDate(0);
			t0ta.setMaxStockCode("601169");
			t0ta.setMaxStockMarket("601169");
			t0ta.setMaxStockName("北京银行");
			t0ta.setOver("CLOSED");
			t0ta.setStatus(0);
			t0ta.setSum(22L);
			t0ta.setTotalGain(-663l);
			t0ta.setVirtual(false);
			list.add(t0ta);
			// ==== mock T+1日实盘、未结算，平盘
			t0ta = new TradingAccInfoBean();
			t0ta.setAcctID(1L);
			t0ta.setCreateDate(0);
			t0ta.setMaxStockCode("601169");
			t0ta.setMaxStockMarket("601169");
			t0ta.setMaxStockName("北京银行");
			t0ta.setOver("UNCLOSE");
			t0ta.setStatus(0);
			t0ta.setSum(22L);
			t0ta.setTotalGain(-123l);
			t0ta.setVirtual(true);
			list.add(t0ta);
			// ==== mock T+1日实盘、已结算，亏损
			t0ta = new TradingAccInfoBean();
			t0ta.setAcctID(1L);
			t0ta.setCreateDate(0);
			t0ta.setMaxStockCode("601169");
			t0ta.setMaxStockMarket("601169");
			t0ta.setMaxStockName("北京银行");
			t0ta.setOver("CLOSED");
			t0ta.setStatus(0);
			t0ta.setSum(32L);
			t0ta.setTotalGain(-233l);
			t0ta.setVirtual(true);
			list.add(t0ta);
			break;
		default:
			break;
		}
		return list;
	}

	public static List<TradingRecordBean> mockTradingRecord() {
		List<TradingRecordBean> list = new ArrayList<TradingRecordBean>();
		TradingRecordBean t = new TradingRecordBean();
		t.setDate(System.currentTimeMillis());
		t.setBeDone(true);
		t.setDay(0);
		t.setCode("600521");
		t.setFee(3990);
		t.setPrice(1256);
		t.setTax(1230);
		t.setVol(3500);
		t.setAmount(12000000);
		t.setDescribe("买入成交");
		list.add(t);
		return list;
	}

	public static List<MegagameRankBean> mockRankData(String t)
			throws StockAppBizException {
		List<MegagameRankBean> list = new ArrayList<MegagameRankBean>();
		for (int i = 0; i < 10; i++) {
			MegagameRankBean mr = new MegagameRankBean();
			mr.setAcctID(1);
			mr.setNickName("模拟用户" + i);
			mr.setGainRate("3%");

			if (t.equals("T")) {
				mr.setMaxStockCode("600521");
				mr.setMaxStockMarket("华海药业");
			} else {
				mr.setMaxStockCode("600175");
				mr.setMaxStockMarket("美都控股");
			}

			list.add(mr);
		}
		return list;
	}

	public static List<RegularTicketBean> mockRegularTicketRank()
			throws StockAppBizException {
		List<RegularTicketBean> list = new ArrayList<RegularTicketBean>();
		for (int i = 0; i < 10; i++) {
			RegularTicketBean mr = new RegularTicketBean();
			mr.setNickName("模拟用户" + i);
			mr.setRegular(1000 * (10 - i));
			mr.setGainCount(6 - i % 5);
			list.add(mr);
		}
		return list;
	}

	public static List<WeekRankBean> mockWeekRank() throws StockAppBizException {

		List<WeekRankBean> list = new ArrayList<WeekRankBean>();
		for (int i = 0; i < 10; i++) {
			WeekRankBean mr = new WeekRankBean();
			mr.setDates("2013年11月04日-2013年11月08日");
			mr.setGainCount(6 - i % 5);
			mr.setNickName("模拟用户" + i);
			mr.setGainRate(String.format("%s", 15 - i) + "%");
			list.add(mr);
		}
		return list;
	}
	
	public static  List<StockBaseInfoBean> getAllMockDataForSearchStock() {
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
	// ====================mockData end ==========
}
