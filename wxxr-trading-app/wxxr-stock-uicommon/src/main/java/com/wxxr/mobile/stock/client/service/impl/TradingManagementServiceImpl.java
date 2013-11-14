/**
 * 
 */
package com.wxxr.mobile.stock.client.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.stock.client.IStockAppContext;
import com.wxxr.mobile.stock.client.StockAppBizException;
import com.wxxr.mobile.stock.client.bean.MegagameRankBean;
import com.wxxr.mobile.stock.client.bean.RankListBean;
import com.wxxr.mobile.stock.client.bean.RegularTicketBean;
import com.wxxr.mobile.stock.client.bean.TradingAccountBean;
import com.wxxr.mobile.stock.client.bean.TradingAccountListBean;
import com.wxxr.mobile.stock.client.bean.UserCreateTradAccInfoBean;
import com.wxxr.mobile.stock.client.bean.WeekRankBean;
import com.wxxr.mobile.stock.client.service.ITradingManagementService;
import com.wxxr.stock.restful.resource.TradingResourse;
import com.wxxr.stock.trading.ejb.api.TradingAccInfoVO;

/**
 * 交易管理模块
 * 
 * @author wangxuyang
 * 
 */
public class TradingManagementServiceImpl extends
		AbstractModule<IStockAppContext> implements ITradingManagementService {

	private static final Trace log = Trace
			.register(TradingManagementServiceImpl.class);
	//=========================beans =======================
	private RankListBean rank = new RankListBean();
	private TradingAccountListBean myTradingAccounts = new TradingAccountListBean();
	// =================module life cycle methods=============================
	@Override
	protected void initServiceDependency() {
		addRequiredService(IRestProxyService.class);
	}

	@Override
	protected void startService() {
		context.registerService(ITradingManagementService.class, this);
	}

	@Override
	protected void stopService() {
		context.unregisterService(ITradingManagementService.class, this);
	}

	// =================interface method =====================================

	public TradingAccountListBean getTradingAccountList() {
		myTradingAccounts.setT0TradingAccounts(mockData(0));
		myTradingAccounts.setT1TradingAccountBeans(mockData(1));
		context.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					
					List<TradingAccInfoVO> vo = getService(IRestProxyService.class).getRestService(
							TradingResourse.class).getTradingAccountList();
				} catch (Throwable e) {
					log.error("fetch data error",e);
				}
			}
		}, 2, TimeUnit.SECONDS);

		return myTradingAccounts;
	}

	@Override
	public TradingAccountBean getTradingAccount(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long createTradingAccount(String type, String credit, String stops,
			String fee1, String freezing) throws StockAppBizException {
		return null;
	}

	@Override
	public RankListBean getTMegagameRank() throws StockAppBizException {
		rank.setTRankBeans(mockRankData("T"));
		return rank;
	}

	@Override
	public RankListBean getT1MegagameRank() throws StockAppBizException {
		rank.setT1RankBeans(mockRankData("T+1"));
		return rank;
	}

	@Override
	public RankListBean getRegularTicketRank()
			throws StockAppBizException {
		rank.setRegularTicketBeans(mockRegularTicketRank());
		return rank;
	}

	@Override
	public RankListBean getWeekRank() throws StockAppBizException {
		rank.setWeekRanKBeans(mockWeekRank());
		return rank;
	}

	// =================private method =======================================
	private List<TradingAccountBean> mockData(int type) {
		List<TradingAccountBean> list = new ArrayList<TradingAccountBean>();
		TradingAccountBean t0ta = null;
		switch (type) {
		case 0:
			// ====mock T日实盘，未结算，盈利
			t0ta = new TradingAccountBean();
			t0ta.setId(1l);
			t0ta.setInitCredit(100000);
			t0ta.setIncome(3000.01f);
			t0ta.setStockCode("600521");
			t0ta.setStockName("华海药业");
			t0ta.setType(1);// 实盘
			t0ta.setStatus(0);// 未结算
			list.add(t0ta);
			// ====mock T日实盘，未结算，亏损
			t0ta = new TradingAccountBean();
			t0ta.setId(1l);
			t0ta.setInitCredit(100000);
			t0ta.setIncome(-3000.01f);
			t0ta.setStockCode("600521");
			t0ta.setStockName("华海药业");
			t0ta.setType(1);// 实盘
			t0ta.setStatus(0);// 未结算
			list.add(t0ta);
			// =====mock T日模拟盘，未结算，平盘
			t0ta = new TradingAccountBean();
			t0ta.setId(2l);
			t0ta.setInitCredit(50000);
			t0ta.setIncome(0);
			t0ta.setStockCode("601169");
			t0ta.setStockName("北京银行");
			t0ta.setType(0);// 模拟盘
			t0ta.setStatus(0);// 未结算
			list.add(t0ta);
			break;
		case 1:
			// ===== mock T+1日模拟盘，已结算
			t0ta = new TradingAccountBean();
			t0ta.setId(2l);
			t0ta.setInitCredit(100000);
			t0ta.setIncome(300.01f);
			t0ta.setStockCode("601169");
			t0ta.setStockName("北京银行");
			t0ta.setType(0);// 模拟盘
			t0ta.setStatus(1);// 结算
			list.add(t0ta);
			// ==== mock T+1日实盘、未结算，盈利
			t0ta = new TradingAccountBean();
			t0ta.setId(1002l);
			t0ta.setInitCredit(50000);
			t0ta.setIncome(300.01f);
			t0ta.setStockCode("601169");
			t0ta.setStockName("北京银行");
			t0ta.setType(1);// 实盘
			t0ta.setStatus(0);// 未结算
			list.add(t0ta);
			// ==== mock T+1日实盘、未结算，亏损
			t0ta = new TradingAccountBean();
			t0ta.setId(1002l);
			t0ta.setInitCredit(50000);
			t0ta.setIncome(-300.01f);
			t0ta.setStockCode("601169");
			t0ta.setStockName("北京银行");
			t0ta.setType(1);// 实盘
			t0ta.setStatus(0);// 未结算
			list.add(t0ta);
			// ==== mock T+1日实盘、未结算，平盘
			t0ta = new TradingAccountBean();
			t0ta.setId(1002l);
			t0ta.setInitCredit(50000);
			t0ta.setIncome(0);
			t0ta.setStockCode("601169");
			t0ta.setStockName("北京银行");
			t0ta.setType(1);// 实盘
			t0ta.setStatus(0);// 未结算
			list.add(t0ta);
			// ==== mock T+1日实盘、已结算，亏损
			t0ta = new TradingAccountBean();
			t0ta.setId(1002l);
			t0ta.setInitCredit(50000);
			t0ta.setIncome(-300.01f);
			t0ta.setStockCode("601169");
			t0ta.setStockName("北京银行");
			t0ta.setType(1);// 实盘
			t0ta.setStatus(1);// 结算
			list.add(t0ta);
			break;
		default:
			break;
		}
		return list;
	}

	public List<MegagameRankBean> mockRankData(String t)
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

	public List<RegularTicketBean> mockRegularTicketRank()
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

	public List<WeekRankBean> mockWeekRank() throws StockAppBizException {
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

	public UserCreateTradAccInfoBean getUserCreateTradAccInfo() {
		UserCreateTradAccInfoBean info = new UserCreateTradAccInfoBean();
		info.setCapitalRate(0.05f);
		info.setCostRate(0.00399f);
		info.setDepositRate(0.05f);
		info.setMaxAmount(30000000l);
		info.setRateString("0.08;0.10,0.12;0.13,0.05;0.08");
		info.setVoucherCostRate(0.00399f);
		return info;
	}

	@Override
	public TradingAccountListBean getMyTradingAccountList()
			throws StockAppBizException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TradingAccountListBean getOtherTradingAccountList(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
