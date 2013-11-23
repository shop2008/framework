/**
 * 
 */
package com.wxxr.mobile.stock.app.mock;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.ArticleBean;
import com.wxxr.mobile.stock.app.bean.AuthInfoBean;
import com.wxxr.mobile.stock.app.bean.EarnRankItemBean;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.bean.MegagameRankBean;
import com.wxxr.mobile.stock.app.bean.MessageInfoBean;
import com.wxxr.mobile.stock.app.bean.MessageInfoListBean;
import com.wxxr.mobile.stock.app.bean.PersonalHomePageBean;
import com.wxxr.mobile.stock.app.bean.RegularTicketBean;
import com.wxxr.mobile.stock.app.bean.ScoreBean;
import com.wxxr.mobile.stock.app.bean.ScoreInfoBean;
import com.wxxr.mobile.stock.app.bean.StockBaseInfoBean;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.bean.StockTaxisBean;
import com.wxxr.mobile.stock.app.bean.StockTradingOrderBean;
import com.wxxr.mobile.stock.app.bean.TradeDetailBean;
import com.wxxr.mobile.stock.app.bean.TradeDetailListBean;
import com.wxxr.mobile.stock.app.bean.TradingAccInfoBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.bean.TradingRecordBean;
import com.wxxr.mobile.stock.app.bean.UserAssetBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.bean.UserCreateTradAccInfoBean;
import com.wxxr.mobile.stock.app.bean.WeekRankBean;

/**
 * @author wangxuyang
 * 
 */
public class MockDataUtils {
	
	
	public static UserBean userBean;
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
			
			t0ta = new TradingAccInfoBean();
			t0ta.setAcctID(1L);
			t0ta.setCreateDate(0);
			t0ta.setMaxStockCode("1233");
			t0ta.setMaxStockMarket("上海");
			t0ta.setMaxStockName("北京银行");
			t0ta.setOver("UNCLOSED");
			t0ta.setStatus(1);
			t0ta.setSum(2l);
			t0ta.setTotalGain(3l);
			t0ta.setVirtual(true);
			list.add(t0ta);
			
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
		t.setMarket("浦发银行");
		t.setFee(3990);
		t.setPrice(1256);
		t.setTax(1230);
		t.setVol(3500);
		t.setAmount(12000000);
		t.setDescribe("买入成交");
		list.add(t);
		
		t = new TradingRecordBean();
		t.setDate(System.currentTimeMillis());
		t.setBeDone(false);
		t.setDay(0);
		t.setCode("600531");
		t.setMarket("浦发银行");
		t.setFee(3990);
		t.setPrice(1256);
		t.setTax(1230);
		t.setVol(3500);
		t.setAmount(12000000);
		t.setDescribe("买入下单");
		list.add(t);
		
		t = new TradingRecordBean();
		t.setDate(System.currentTimeMillis());
		t.setBeDone(false);
		t.setDay(0);
		t.setCode("601521");
		t.setMarket("方正科技");
		t.setFee(3990);
		t.setPrice(1256);
		t.setTax(1230);
		t.setVol(3500);
		t.setAmount(12000000);
		t.setDescribe("买入下单");
		list.add(t);
		
		t = new TradingRecordBean();
		t.setDate(System.currentTimeMillis());
		t.setBeDone(true);
		t.setDay(0);
		t.setCode("602521");
		t.setMarket("方正科技");
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
	
	/**模拟交易记录*/
	public static List<GainBean> mockTradeRecord(int type) {
		
		List<GainBean> tradeRecordList = new ArrayList<GainBean>();
		switch (type) {
		case 0:
			//成功记录
			GainBean mockSucData;
			
			mockSucData = new GainBean();
			mockSucData.setMaxStockName("北京银行");
			mockSucData.setMaxStockCode("6001023");
			mockSucData.setCloseTime("2013-11-1");
			mockSucData.setTotalGain(200L);
			mockSucData.setSum(10000L);
			tradeRecordList.add(mockSucData);
			
			
			mockSucData = new GainBean();
			mockSucData.setMaxStockName("交通银行");
			mockSucData.setMaxStockCode("6001022");
			mockSucData.setCloseTime("2013-11-1");
			mockSucData.setTotalGain(-200L);
			mockSucData.setSum(20000L);
		    tradeRecordList.add(mockSucData);
			
		    mockSucData = new GainBean();
		    mockSucData.setMaxStockName("招商银行");
		    mockSucData.setMaxStockCode("6001025");
		    mockSucData.setCloseTime("2013-11-1");
		    mockSucData.setTotalGain(200L);
		    mockSucData.setSum(10000L);
		    tradeRecordList.add(mockSucData);
			break;
			
		case 1:
			
			//全部交易记录
			GainBean mockAllData;
			
			mockAllData = new GainBean();
			mockAllData.setMaxStockName("北京银行");
			mockAllData.setMaxStockCode("6001023");
			mockAllData.setCloseTime("2013-11-1");
			mockAllData.setTotalGain(200L);
			mockAllData.setSum(10000L);
			tradeRecordList.add(mockAllData);
			
			
			mockAllData = new GainBean();
			mockAllData.setMaxStockName("交通银行");
			mockAllData.setMaxStockCode("6001022");
			mockAllData.setCloseTime("2013-11-1");
			mockAllData.setTotalGain(-200L);
			mockAllData.setSum(20000L);
		    tradeRecordList.add(mockAllData);
			
		    mockAllData = new GainBean();
			mockAllData.setMaxStockName("招商银行");
			mockAllData.setMaxStockCode("6001025");
			mockAllData.setCloseTime("2013-11-1");
			mockAllData.setTotalGain(200L);
			mockAllData.setSum(10000L);
		    tradeRecordList.add(mockAllData);
			
		    mockAllData = new GainBean();
			mockAllData.setMaxStockName("农业银行");
			mockAllData.setMaxStockCode("6001032");
			mockAllData.setCloseTime("2013-11-1");
			mockAllData.setTotalGain(-200L);
			mockAllData.setSum(20000L);
		    tradeRecordList.add(mockAllData);
			
		    mockAllData = new GainBean();
			mockAllData.setMaxStockName("哈尔滨银行");
			mockAllData.setMaxStockCode("6001088");
			mockAllData.setCloseTime("2013-11-1");
			mockAllData.setTotalGain(400L);
			mockAllData.setSum(20000L);
		    tradeRecordList.add(mockAllData);
			break;
		case 2:
			//挑战交易盘
			
			GainBean mockChallengeDate;
			
			mockChallengeDate = new GainBean();
			mockChallengeDate.setMaxStockName("北京银行");
			mockChallengeDate.setMaxStockCode("6001023");
			mockChallengeDate.setCloseTime("2013-11-1");
			mockChallengeDate.setTotalGain(200L);
			mockChallengeDate.setSum(10000L);
			tradeRecordList.add(mockChallengeDate);
			
			
			mockChallengeDate = new GainBean();
			mockChallengeDate.setMaxStockName("交通银行");
			mockChallengeDate.setMaxStockCode("6001022");
			mockChallengeDate.setCloseTime("2013-11-1");
			mockChallengeDate.setTotalGain(-200L);
			mockChallengeDate.setSum(20000L);
		    tradeRecordList.add(mockChallengeDate);
			
		    mockChallengeDate = new GainBean();
			mockChallengeDate.setMaxStockName("招商银行");
			mockChallengeDate.setMaxStockCode("6001025");
			mockChallengeDate.setCloseTime("2013-11-1");
			mockChallengeDate.setTotalGain(200L);
			mockChallengeDate.setSum(10000L);
		    tradeRecordList.add(mockChallengeDate);
			
		    mockChallengeDate = new GainBean();
			mockChallengeDate.setMaxStockName("农业银行");
			mockChallengeDate.setMaxStockCode("6001032");
			mockChallengeDate.setCloseTime("2013-11-1");
			mockChallengeDate.setTotalGain(-200L);
			mockChallengeDate.setSum(20000L);
		    tradeRecordList.add(mockChallengeDate);
			
		    mockChallengeDate = new GainBean();
			mockChallengeDate.setMaxStockName("哈尔滨银行");
			mockChallengeDate.setMaxStockCode("6001088");
			mockChallengeDate.setCloseTime("2013-11-1");
			mockChallengeDate.setTotalGain(400L);
			mockChallengeDate.setSum(20000L);
		    tradeRecordList.add(mockChallengeDate);
			break;
			
		case 3:
			
			//参赛交易盘
			GainBean mockJoinData;
			
			mockJoinData = new GainBean();
			mockJoinData.setMaxStockName("北京银行");
			mockJoinData.setMaxStockCode("6001023");
			mockJoinData.setCloseTime("2013-11-1");
			mockJoinData.setTotalGain(200L);
			mockJoinData.setSum(10000L);
			tradeRecordList.add(mockJoinData);
			
			
			mockJoinData = new GainBean();
			mockJoinData.setMaxStockName("交通银行");
			mockJoinData.setMaxStockCode("6001022");
			mockJoinData.setCloseTime("2013-11-1");
			mockJoinData.setTotalGain(-200L);
			mockJoinData.setSum(20000L);
		    tradeRecordList.add(mockJoinData);
			
		    mockJoinData = new GainBean();
			mockJoinData.setMaxStockName("农业银行");
			mockJoinData.setMaxStockCode("6001032");
			mockJoinData.setCloseTime("2013-11-1");
			mockJoinData.setTotalGain(-200L);
			mockJoinData.setSum(20000L);
		    tradeRecordList.add(mockJoinData);
			break;
		default:
			break;
		}
		
		return tradeRecordList;
	}
	
	public static UserBean mockUserInfo() {
		if (userBean == null) {
			userBean = new UserBean();
			userBean.setPhoneNumber("15611545676");
			userBean.setHomeBack("resourceId:drawable/back2");
			userBean.setUserPic("resourceId:drawable/head2");
			userBean.setJoinShared(""+3);
			
			userBean.setNickName("李四");
			userBean.setScore(""+900);
			userBean.setLogin(true);
			userBean.setMessagePushSettingOn(true);
			
			UserAssetBean usrAsset = new UserAssetBean();
			usrAsset.setBalance(2000L);
			usrAsset.setFrozen(200);
			usrAsset.setUsableBal(1800L);
			userBean.setUserAsset(usrAsset);
			
			AuthInfoBean bankInfo = new AuthInfoBean();
			bankInfo.setAccountName("李四");
			bankInfo.setBankNum("6222565412512124234");
			bankInfo.setBankAddr("北京市海淀区");
			bankInfo.setBankName("工商银行");
			userBean.setBankInfo(bankInfo);
		}
		return userBean;
	}
	
	public static PersonalHomePageBean mockPersonalHome() {
		PersonalHomePageBean pageBean = new PersonalHomePageBean();
		pageBean.setActualCount(3);
		pageBean.setVirtualCount(2);
		List<GainBean> actualList = new ArrayList<GainBean>();
		GainBean bean;
		bean = new GainBean();
		bean.setCloseTime("2013-10-31");
		bean.setMaxStockName("工商银行");
		bean.setMaxStockCode("620102");
		bean.setStatus(0);
		bean.setSum(10000L);
		bean.setUserGain(200L);
		actualList.add(bean);
		
		bean = new GainBean();
		bean.setCloseTime("2013-11-1");
		bean.setMaxStockName("和记黄埔");
		bean.setMaxStockCode("620108");
		bean.setStatus(0);
		bean.setSum(20000L);
		bean.setUserGain(-200L);
		actualList.add(bean);
		
		bean = new GainBean();
		bean.setCloseTime("2013-11-6");
		bean.setMaxStockName("中国石化");
		bean.setMaxStockCode("620104");
		bean.setStatus(0);
		bean.setSum(20000L);
		bean.setUserGain(-300L);
		actualList.add(bean);
		
		pageBean.setActualList(actualList);
		
		List<GainBean> virtualList = new ArrayList<GainBean>();
		bean = new GainBean();
		bean.setCloseTime("2013-11-2");
		bean.setMaxStockName("无限新锐");
		bean.setMaxStockCode("622929");
		bean.setStatus(0);
		bean.setSum(80000L);
		bean.setUserGain(10000L);
		virtualList.add(bean);
		
		bean = new GainBean();
		bean.setCloseTime("2013-11-2");
		bean.setMaxStockName("百度科技");
		bean.setMaxStockCode("622909");
		bean.setStatus(0);
		bean.setSum(20000L);
		bean.setUserGain(100L);
		virtualList.add(bean);
		
		pageBean.setVirtualList(virtualList);
		pageBean.setTotalProfit(2000.00);
		pageBean.setVoucherVol(200L);
		return pageBean;
	}
	
	public void mockLogOut() {
		if (userBean != null) {
			userBean = null;
		}
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
	
	
	public static ScoreInfoBean mockScoreInfo() {
		ScoreInfoBean scoreInfoBean = new ScoreInfoBean();
		scoreInfoBean.setBalance(300+"");
		List<ScoreBean> scoreBeans = new ArrayList<ScoreBean>();
		
		ScoreBean scoreBean;
		
		scoreBean = new ScoreBean();
		scoreBean.setAmount(200f);
		scoreBean.setCatagory("推荐好友奖励");
		scoreBean.setDate("2013-10-20");
		scoreBeans.add(scoreBean);
		
		scoreBean = new ScoreBean();
		scoreBean.setAmount(300f);
		scoreBean.setCatagory("评论奖励");
		scoreBean.setDate("2013-10-25");
		scoreBeans.add(scoreBean);
		
		
		scoreBean = new ScoreBean();
		scoreBean.setAmount(100f);
		scoreBean.setCatagory("新手奖励");
		scoreBean.setDate("2013-10-26");
		scoreBeans.add(scoreBean);
		
		
		scoreBean = new ScoreBean();
		scoreBean.setAmount(-200f);
		scoreBean.setCatagory("申请实盘");
		scoreBean.setDate("2013-10-28");
		scoreBeans.add(scoreBean);
		scoreInfoBean.setScores(scoreBeans);
		return scoreInfoBean;
	}
	
	public static TradeDetailListBean mockTradeDetails() {
		
		TradeDetailListBean tradeDetailListBean = new TradeDetailListBean();
		
		List<TradeDetailBean> tradeDetails = new ArrayList<TradeDetailBean>();
		
		TradeDetailBean detailBean;
		detailBean = new TradeDetailBean();
		detailBean.setTradeAmount(200f);
		detailBean.setTradeCatagory("充入现金");
		detailBean.setTradeDate("2013-11-11");
		
		tradeDetails.add(detailBean);
		
		detailBean = new TradeDetailBean();
		detailBean.setTradeAmount(300f);
		detailBean.setTradeCatagory("实盘结盘收益");
		detailBean.setTradeDate("2013-11-10");
		tradeDetails.add(detailBean);
		
		detailBean = new TradeDetailBean();
		detailBean.setTradeAmount(300f);
		detailBean.setTradeCatagory("其它");
		detailBean.setTradeDate("2013-11-8");
		tradeDetails.add(detailBean);
		
		tradeDetailListBean.setTradeDetails(tradeDetails);
		return tradeDetailListBean;
	}
	
	public static TradingAccountBean mockTradingAccountInfo() {
		TradingAccountBean myTradingAccount = new TradingAccountBean();
		
		myTradingAccount.setId(1L);
		myTradingAccount.setBuyDay(System.currentTimeMillis());
		myTradingAccount.setSellDay(System.currentTimeMillis()+24*3600*1000);
		myTradingAccount.setApplyFee(300000);
		myTradingAccount.setAvalibleFee(36000);
		myTradingAccount.setGainRate(12);
		myTradingAccount.setTotalGain(3000);
		myTradingAccount.setLossLimit(-500);
		myTradingAccount.setStatus(1);
		List<StockTradingOrderBean> tradingOrders = new ArrayList<StockTradingOrderBean>();
		StockTradingOrderBean item = new StockTradingOrderBean();
		item.setStockName("唐山港");
		item.setStockCode("601000");
		item.setCurrentPirce(306L);
		item.setChangeRate(66L);
		item.setBuy(300L);
		item.setAmount(1000L);
		item.setGain(100L);
		item.setGainRate(1L);
		tradingOrders.add(item);
		
		item = new StockTradingOrderBean();
		item.setStockName("浦发银行");
		item.setStockCode("601000");
		item.setCurrentPirce(506L);
		item.setChangeRate(-66L);
		item.setBuy(500L);
		item.setAmount(2000L);
		item.setGain(-99L);
		item.setGainRate(-16L);
		tradingOrders.add(item);
		
		myTradingAccount.setTradingOrders(tradingOrders);
		
		return myTradingAccount;
	}
	
	public static MessageInfoListBean mockMessageInfos() {
		
		MessageInfoListBean messageListBean = new MessageInfoListBean();
		
		List<MessageInfoBean> messageInfoBeans = new ArrayList<MessageInfoBean>();
		MessageInfoBean messageInfo;
		
		messageInfo = new MessageInfoBean();
		messageInfo.setTitle("平安银行模拟盘");
		messageInfo.setContent("开始系统清仓，清仓时仓位80.88%");
		messageInfo.setDate(System.currentTimeMillis());
		messageInfoBeans.add(messageInfo);
		
		messageInfo = new MessageInfoBean();
		messageInfo.setTitle("实盘买入平安银行");
		messageInfo.setContent("9.98元成交2000股，清仓时仓位80.88%");
		messageInfo.setDate(System.currentTimeMillis());
		messageInfoBeans.add(messageInfo);
		
		messageInfo = new MessageInfoBean();
		messageInfo.setTitle("幸运获得实盘券");
		messageInfo.setContent("恭喜您创建了新锐操盘交易分享平台的第1万个实盘，您将收到我们赠送的50000元实盘券");
		messageInfo.setDate(System.currentTimeMillis());
		messageInfoBeans.add(messageInfo);
		
		messageListBean.setMessageInfos(messageInfoBeans);
		return messageListBean;
	}
	// ====================mockData end ==========
	
	
	public static UserCreateTradAccInfoBean getUserCreateTradAccInfo(){
		UserCreateTradAccInfoBean info = new UserCreateTradAccInfoBean();
		info.setCapitalRate(0.05f);
		info.setCostRate(0.00399f);
		info.setDepositRate(0.05f);
		info.setMaxAmount(300000l);
		info.setRateString("0.08;0.10,0.12;0.13,0.05;0.08");
		info.setVoucherCostRate(0.00399f);	
		return info;
	}
	
	public static List<ArticleBean> mockData(){
		List<ArticleBean> articles = new ArrayList<ArticleBean>();
		String[] titles = {"Google","Baidu","Sina","网易"};
		String[] articleUrls = {"http://www.google.com.hk/","http://www.baidu.com","http://www.sina.com","http://www.163.com"};
		String[] imageUrls = {"http://www.google.com.hk/logos/doodles/2013/raymond-loewys-120th-birthday-ca-fr-us-nl-uk-ie-6388231276855296-hp.jpg",
				"http://www.baidu.com/img/bdlogo.gif","http://ui.sina.com/assets/img/www/worldmap.jpg",
				"http://image.gxq.com.cn/upload/ad/2013/11/07/bd7bae043de331091b93d9394eec0298.jpg"};
		for (int i = 0; i < 4; i++) {
			ArticleBean article = new ArticleBean();
			article.setId(String.format("%s", i));
			article.setTitle(titles[i]);
			article.setAbstractInfo(titles[i]+"'s 摘要 ");
			article.setArticleUrl(articleUrls[i]);
			article.setImageUrl(imageUrls[i]);
			article.setType(15);
			articles.add(article);
		}
		return articles;
	}
	
	
	
	//上海（上证指数）
	public static StockQuotationBean getSHStockQuotation(){
		StockQuotationBean sh = new StockQuotationBean();
		sh.setCode("000001");
		sh.setMarket("上证指数");
		sh.setClose(220661L);
		sh.setNewprice(220661L); 
		sh.setRisefallrate(62L);
		sh.setChange(1349L);
		sh.setClose(220666L);
		return sh;
	}
	
	//深圳成指
	public static StockQuotationBean getSZStockQuotation(){
		StockQuotationBean sz = new StockQuotationBean();
		sz.setCode("3900001");
		sz.setMarket("深圳成指");
		sz.setClose(851068L);
		sz.setNewprice(851068L); 
		sz.setRisefallrate(58L);
		sz.setChange(4947L);
		sz.setClose(851060L);
		return sz;
	}
	
	// 5档盘口
	public static StockQuotationBean getStockQuotation(String code, String market) {
		StockQuotationBean bean = new StockQuotationBean();
		bean.setCode("002002");
		bean.setMarket("SZ");
		bean.setChange(494L);
		bean.setRisefallrate(58L);
		bean.setNewprice(510L);
		bean.setClose(506L);
		bean.setBuyprice1(569L);
		bean.setBuyprice2(568L);
		bean.setBuyprice3(567L);
		bean.setBuyprice4(566L);
		bean.setBuyprice5(565L);
		
		bean.setBuyvolume1(560L);
		bean.setBuyvolume2(60L);
		bean.setBuyvolume3(360L);
		bean.setBuyvolume4(20L);
		bean.setBuyvolume5(50L);
		
		bean.setSellprice1(564L);
		bean.setSellprice2(563L);
		bean.setSellprice3(562L);
		bean.setSellprice4(561L);
		bean.setSellprice5(560L);
		
		bean.setSellvolume1(560L);
		bean.setSellvolume2(60L);
		bean.setSellvolume3(360L);
		bean.setSellvolume4(20L);
		bean.setSellvolume5(50L);
		
		bean.setSellsum(1000000L);
		bean.setBuysum(500000L);
		
		return bean;
	}
	
	public static List<StockTaxisBean> getStockTaxis(){
		ArrayList<StockTaxisBean> stockTaxis = new ArrayList<StockTaxisBean>();
		StockTaxisBean taxis;
		
		taxis = new StockTaxisBean();
		taxis.setCode("600877");
		taxis.setName("中国嘉陵");
		taxis.setNewprice(402L);
		taxis.setRisefallrate(1014L);
		stockTaxis.add(taxis);
		
		taxis = new StockTaxisBean();
		taxis.setCode("600839");
		taxis.setName("四川长虹");
		taxis.setNewprice(326L);
		taxis.setRisefallrate(1014L);
		stockTaxis.add(taxis);
		
		taxis = new StockTaxisBean();
		taxis.setCode("600130");
		taxis.setName("波导股份");
		taxis.setNewprice(436L);
		taxis.setRisefallrate(1010L);
		stockTaxis.add(taxis);
		
		taxis = new StockTaxisBean();
		taxis.setCode("600590");
		taxis.setName("泰豪科技");
		taxis.setNewprice(688L);
		taxis.setRisefallrate(1008L);
		stockTaxis.add(taxis);
		
		return stockTaxis;
	}
	
	public static List<EarnRankItemBean> getEarnRank(){
		ArrayList<EarnRankItemBean> rank = new ArrayList<EarnRankItemBean>();
		EarnRankItemBean earnRank;
		
		earnRank = new EarnRankItemBean();
		earnRank.setAcctId("001");
		earnRank.setImgUrl("http://hqpiczs.dfcfw.com/EM_Quote2010PictureProducter/picture/0000011R.png");
		earnRank.setTitle("动视暴雪第三季度财报：净利润同比下滑75%");
		rank.add(earnRank);
		
		earnRank = new EarnRankItemBean();
		earnRank.setAcctId("002");
		earnRank.setImgUrl("http://i0.sinaimg.cn/cj/201311/20131120/image_sinajs_cn_newchart_png_min_cn_min_n_sh000001_183028.png");
		earnRank.setTitle("券商称后市看涨");
		rank.add(earnRank);
		
		earnRank = new EarnRankItemBean();
		earnRank.setAcctId("003");
		earnRank.setImgUrl("http://image.sinajs.cn/newchart/png/min/cn_min/n/sh000001.png");
		earnRank.setTitle("沪指涨0.61%收复2200 军工航天概念股现涨停潮");
		rank.add(earnRank);
		
		earnRank = new EarnRankItemBean();
		earnRank.setAcctId("004");
		earnRank.setImgUrl("http://i2.sinaimg.cn/cj/201311/20131120/image_sinajs_cn_newchart_png_min_cn_min_n_sh000001_160126.png");
		earnRank.setTitle("天津自贸区板块大涨3.09% 6股涨停");
		rank.add(earnRank);
		
		earnRank = new EarnRankItemBean();
		earnRank.setAcctId("005");
		earnRank.setImgUrl("http://i3.sinaimg.cn/cj/201311/20131120/image_sinajs_cn_newchart_png_min_cn_min_n_sh000001_155918.png");
		earnRank.setTitle("仍将震荡 关注改革受益板块的轮动");
		rank.add(earnRank);
		return rank;
	}
}
