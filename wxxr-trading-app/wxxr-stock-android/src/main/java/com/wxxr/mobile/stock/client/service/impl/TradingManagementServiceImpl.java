/**
 * 
 */
package com.wxxr.mobile.stock.client.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.stock.client.IStockAppContext;
import com.wxxr.mobile.stock.client.StockAppBizException;
import com.wxxr.mobile.stock.client.bean.TradingAccount;
import com.wxxr.mobile.stock.client.service.ITradingManagementService;

/**
 * 交易管理模块
 * 
 * @author wangxuyang
 * 
 */
public class TradingManagementServiceImpl extends AbstractModule<IStockAppContext>
		implements ITradingManagementService {

	private static final Trace log = Trace.register(TradingManagementServiceImpl.class);

	// =================interface method =====================================
	public List<TradingAccount> getTradingAccountList(int type) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("getTradingAccountList[type: %s]", type));
		}
		// 从上下文取出当前用户
		// String userId = (String) context.getAttribute("userId");
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
		// TODO Auto-generated method stub
		return null;
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

	// =================module life cycle methods=============================

	@Override
	protected void initServiceDependency() {

	}

	@Override
	protected void startService() {
		context.registerService(ITradingManagementService.class, this);
	}

	@Override
	protected void stopService() {
		context.unregisterService(ITradingManagementService.class, this);
	}

}
