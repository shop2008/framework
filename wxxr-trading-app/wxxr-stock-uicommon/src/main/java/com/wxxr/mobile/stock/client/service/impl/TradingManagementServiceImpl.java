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
import com.wxxr.mobile.stock.client.StockAppBizException;
import com.wxxr.mobile.stock.client.bean.MegagameRank;
import com.wxxr.mobile.stock.client.bean.RegularTicket;
import com.wxxr.mobile.stock.client.bean.TradingAccount;
import com.wxxr.mobile.stock.client.bean.UserCreateTradAccInfo;
import com.wxxr.mobile.stock.client.bean.WeekRank;
import com.wxxr.mobile.stock.client.service.ITradingManagementService;
import com.wxxr.stock.restful.resource.TradingResourse;
import com.wxxr.stock.trading.ejb.api.TradingAccInfoVO;

/**
 * 交易管理模块
 * 
 * @author wangxuyang
 * 
 */
public class TradingManagementServiceImpl extends AbstractModule<IStockAppContext>
		implements ITradingManagementService {

	private static final Trace log = Trace.register(TradingManagementServiceImpl.class);
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
		public List<TradingAccount> getTradingAccountList(int type) {
			if (log.isDebugEnabled()) {
				log.debug(String.format("getTradingAccountList[type: %s]", type));
			}
			String userId ="13671279085";
			try {
				List<TradingAccInfoVO> list = getService(IRestProxyService.class).getRestService(TradingResourse.class).getTradingAccountList();
				log.info(list==null?"null":list.toString());
			} catch (Throwable e) {
				log.error("", e);
			}
			return mockData(type);
		}

		public List<TradingAccount> getTradingAccountList(String userId, int type) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public TradingAccount getTradingAccount(Long id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Long createTradingAccount(String type, String credit, String stops,
				String fee1, String freezing) throws StockAppBizException {
			return null;
		}
		@Override
		public List<MegagameRank> getTMegagameRank() throws StockAppBizException {
			return mockRankData("T");
		}

		@Override
		public List<MegagameRank> getT1MegagameRank() throws StockAppBizException {
			return mockRankData("T+1");
		}

		@Override
		public List<RegularTicket> getRegularTicketRank() throws StockAppBizException {
			return mockRegularTicketRank();
		}

		@Override
		public List<WeekRank> getWeekRank() throws StockAppBizException {
			return mockWeekRank();
		}
		// =================private method =======================================
		private List<TradingAccount> mockData(int type) {
			List<TradingAccount> list = new ArrayList<TradingAccount>();
			TradingAccount t0ta = null;
			switch (type) {
			case 0:
				// ====mock T日实盘，未结算，盈利
				t0ta = new TradingAccount();
				t0ta.setId(1l);
				t0ta.setInitCredit(100000);
				t0ta.setIncome(3000.01f);
				t0ta.setStockCode("600521");
				t0ta.setStockName("华海药业");
				t0ta.setType(1);// 实盘
				t0ta.setStatus(0);// 未结算
				list.add(t0ta);
				// ====mock T日实盘，未结算，亏损
				t0ta = new TradingAccount();
				t0ta.setId(1l);
				t0ta.setInitCredit(100000);
				t0ta.setIncome(-3000.01f);
				t0ta.setStockCode("600521");
				t0ta.setStockName("华海药业");
				t0ta.setType(1);// 实盘
				t0ta.setStatus(0);// 未结算
				list.add(t0ta);
				// =====mock T日模拟盘，未结算，平盘
				t0ta = new TradingAccount();
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
				t0ta = new TradingAccount();
				t0ta.setId(2l);
				t0ta.setInitCredit(100000);
				t0ta.setIncome(300.01f);
				t0ta.setStockCode("601169");
				t0ta.setStockName("北京银行");
				t0ta.setType(0);// 模拟盘
				t0ta.setStatus(1);// 结算
				list.add(t0ta);
				// ==== mock T+1日实盘、未结算，盈利
				t0ta = new TradingAccount();
				t0ta.setId(1002l);
				t0ta.setInitCredit(50000);
				t0ta.setIncome(300.01f);
				t0ta.setStockCode("601169");
				t0ta.setStockName("北京银行");
				t0ta.setType(1);// 实盘
				t0ta.setStatus(0);// 未结算
				list.add(t0ta);
				// ==== mock T+1日实盘、未结算，亏损
				t0ta = new TradingAccount();
				t0ta.setId(1002l);
				t0ta.setInitCredit(50000);
				t0ta.setIncome(-300.01f);
				t0ta.setStockCode("601169");
				t0ta.setStockName("北京银行");
				t0ta.setType(1);// 实盘
				t0ta.setStatus(0);// 未结算
				list.add(t0ta);
				// ==== mock T+1日实盘、未结算，平盘
				t0ta = new TradingAccount();
				t0ta.setId(1002l);
				t0ta.setInitCredit(50000);
				t0ta.setIncome(0);
				t0ta.setStockCode("601169");
				t0ta.setStockName("北京银行");
				t0ta.setType(1);// 实盘
				t0ta.setStatus(0);// 未结算
				list.add(t0ta);
				// ==== mock T+1日实盘、已结算，亏损
				t0ta = new TradingAccount();
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
		public List<MegagameRank> mockRankData(String t) throws StockAppBizException {
			List<MegagameRank> list = new ArrayList<MegagameRank>();
			for (int i = 0; i < 10; i++) {
				MegagameRank mr = new MegagameRank();
				mr.setAcctID(1);
				mr.setNickName("模拟用户"+i);
				mr.setGainRate("3%");
				
				if (t.equals("T")) {
					mr.setMaxStockCode("600521");
					mr.setMaxStockMarket("华海药业");
				}else{
					mr.setMaxStockCode("600175");
					mr.setMaxStockMarket("美都控股");
				}
				
				list.add(mr);
			}
			return list;
		}
		public List<RegularTicket> mockRegularTicketRank() throws StockAppBizException {
			List<RegularTicket> list = new ArrayList<RegularTicket>();
			for (int i = 0; i < 10; i++) {
				RegularTicket mr = new RegularTicket();
				mr.setNickName("模拟用户"+i);
				mr.setRegular(1000*(10-i));
				mr.setGainCount(6-i%5);
				list.add(mr);
			}
			return list;
		}
		public List<WeekRank> mockWeekRank() throws StockAppBizException {
			List<WeekRank> list = new ArrayList<WeekRank>();
			for (int i = 0; i < 10; i++) {
				WeekRank mr = new WeekRank();
				mr.setDates("2013年11月04日-2013年11月08日");
				mr.setGainCount(6-i%5);
				mr.setNickName("模拟用户"+i);
				mr.setGainRate(String.format("%s", 15-i)+"%");
				list.add(mr);
			}
			return list;
		}
	//{"UserCreateTradAccInfoVO":{"capitalRate":0.05,"costRate":0.00399,"depositRate":0.05,"maxAmount":30000000,"rateString":"0.08;0.10,0.12;0.13,0.05;0.08","voucherCostRate":0.00399}}
	public UserCreateTradAccInfo getUserCreateTradAccInfo() {
		UserCreateTradAccInfo info = new UserCreateTradAccInfo();
		info.setCapitalRate(0.05f);
		info.setCostRate(0.00399f);
		info.setDepositRate(0.05f);
		info.setMaxAmount(30000000l);
		info.setRateString("0.08;0.10,0.12;0.13,0.05;0.08");
		info.setVoucherCostRate(0.00399f);
		return info;
	}



}
