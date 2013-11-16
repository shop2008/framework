/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import org.apache.http.auth.AUTH;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.AuditDetailBean;
import com.wxxr.mobile.stock.app.bean.DealDetailBean;
import com.wxxr.mobile.stock.app.bean.MegagameRankBean;
import com.wxxr.mobile.stock.app.bean.RankListBean;
import com.wxxr.mobile.stock.app.bean.RegularTicketBean;
import com.wxxr.mobile.stock.app.bean.StockTradingOrderBean;
import com.wxxr.mobile.stock.app.bean.TradingAccInfoBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountListBean;
import com.wxxr.mobile.stock.app.bean.TradingRecordBean;
import com.wxxr.mobile.stock.app.bean.UserCreateTradAccInfoBean;
import com.wxxr.mobile.stock.app.bean.WeekRankBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.stock.restful.resource.TradingResourse;
import com.wxxr.stock.trading.ejb.api.AuditDetailVO;
import com.wxxr.stock.trading.ejb.api.MegagameRankVO;
import com.wxxr.stock.trading.ejb.api.RegularTicketVO;
import com.wxxr.stock.trading.ejb.api.StockResultVO;
import com.wxxr.stock.trading.ejb.api.StockTradingOrderVO;
import com.wxxr.stock.trading.ejb.api.TradingAccInfoVO;
import com.wxxr.stock.trading.ejb.api.TradingAccountVO;
import com.wxxr.stock.trading.ejb.api.UserCreateTradAccInfoVO;
import com.wxxr.stock.trading.ejb.api.WeekRankVO;

/**
 * 交易管理模块
 * 
 * @author wangxuyang 
 * 
 */
public class TradingManagementServiceImpl extends
		AbstractModule<IStockAppContext> implements ITradingManagementService {

	private static final Trace log = Trace.register(TradingManagementServiceImpl.class);
	//=========================beans =======================
	/**
	 * 排行榜列表
	 */
	private RankListBean rank = new RankListBean();
	/**
	 * 我的交易盘列表
	 */
	private TradingAccountListBean myTradingAccounts = new TradingAccountListBean();
	/**
	 * 我的交易盘详情
	 */
	private TradingAccountBean myTradingAccount = new TradingAccountBean();
	/**
	 * 成交详情
	 */
	private DealDetailBean dealDetailBean = new DealDetailBean();
	/**
	 * 清算详情
	 */
	private AuditDetailBean auditDetailBean = new AuditDetailBean();
	/**
	 * 创建交易盘的数值
	 */
	private UserCreateTradAccInfoBean createTDConfig = new UserCreateTradAccInfoBean();
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
		timer.cancel();
	}

	// =================interface method =====================================
	private Timer timer = new Timer();
	public TradingAccountListBean getTradingAccountList() {
		context.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					List<TradingAccInfoVO> vo = getService(IRestProxyService.class).getRestService(
							TradingResourse.class).getTradingAccountList();
					if (vo!=null) {
						
					}else{
						if (!context.getApplication().isInDebugMode()) {
							myTradingAccounts.getT0TradingAccounts();
						}
					}
					
					
				} catch (Throwable e) {
					log.error("fetch data error",e);
				}
			}
		}, 10, TimeUnit.SECONDS);

		return myTradingAccounts;
	}
	@Override
	public TradingAccountListBean getHomePageTradingAccountList()
			throws StockAppBizException {
		if(context.getApplication().isInDebugMode()){
			myTradingAccounts.setT1TradingAccountBeans(mockData(1));
			myTradingAccounts.setT0TradingAccounts(mockData(0));
			return myTradingAccounts;
		}
		//如果未登录
		context.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					List<TradingAccInfoVO> volist = getService(IRestProxyService.class).getRestService(
							TradingResourse.class).getTradingAccountList();
					if (volist!=null&&volist.size()>0) {
						List<TradingAccInfoBean> t0_list = new ArrayList<TradingAccInfoBean>();
						List<TradingAccInfoBean> t1_list = new ArrayList<TradingAccInfoBean>();
						for (TradingAccInfoVO vo : volist) {
							if (vo.getStatus()==1) {
								t0_list.add(fromVO(vo));
							}else{
								t1_list.add(fromVO(vo));
							}
						}
						myTradingAccounts.setT0TradingAccounts(t0_list);
						myTradingAccounts.setT1TradingAccountBeans(t1_list);
					}
				} catch (Throwable e) {
					log.error("fetch data error",e);
				}
			}
		}, 0, TimeUnit.SECONDS);
		return myTradingAccounts;
	}

	public RankListBean getTMegagameRank() throws StockAppBizException {
		if(context.getApplication().isInDebugMode()) {
			rank.setTRankBeans(mockRankData("T"));
			return rank;
		}
		context.invokeLater(new Runnable() {
			public void run() {
				try {
					List<MegagameRankVO> volist = getService(IRestProxyService.class).getRestService(TradingResourse.class).getTMegagameRank();
					if (volist!=null&&volist.size()>0) {
						List<MegagameRankBean> beanList = new ArrayList<MegagameRankBean>();
						int rankNo = 1;
						for (MegagameRankVO vo : volist) {
							MegagameRankBean bean = fromVO(vo);
							bean.setRankSeq(rankNo++);
							beanList.add(bean);
						}
						rank.setTRankBeans(beanList);
					}
				} catch (Throwable e) {
					log.warn("Error when fetching week rank",e);
				}			
			}
		}, 0, TimeUnit.SECONDS);
		return rank;
	}
	@Override
	public RankListBean getT1MegagameRank() throws StockAppBizException {
		if(context.getApplication().isInDebugMode()) {
			rank.setT1RankBeans(mockRankData("T1"));
			return rank;
		}
		context.invokeLater(new Runnable() {
			public void run() {
				try {
					List<MegagameRankVO> volist = getService(IRestProxyService.class).getRestService(TradingResourse.class).getTPlusMegagameRank();
					if (volist!=null&&volist.size()>0) {
						List<MegagameRankBean> beanList = new ArrayList<MegagameRankBean>();
						int rankNo = 1;
						for (MegagameRankVO vo : volist) {
							MegagameRankBean bean = fromVO(vo);
							bean.setRankSeq(rankNo++);
							beanList.add(bean);
						}
						rank.setT1RankBeans(beanList);
					}
				} catch (Throwable e) {
					log.warn("Error when fetching week rank",e);
				}			
			}
		}, 0, TimeUnit.SECONDS);
		return rank;
	}

	@Override
	public RankListBean getRegularTicketRank()
			throws StockAppBizException {
		if(context.getApplication().isInDebugMode()) {
			rank.setRegularTicketBeans(mockRegularTicketRank());
			return rank;
		}
		context.invokeLater(new Runnable() {
			public void run() {
				try {
					List<RegularTicketVO> volist = getService(IRestProxyService.class).getRestService(TradingResourse.class).getRegularTicketRank();
					if (volist!=null&&volist.size()>0) {
						List<RegularTicketBean> beanList = new ArrayList<RegularTicketBean>();
						int rankNo = 1;
						for (RegularTicketVO vo : volist) {
							RegularTicketBean bean = fromVO(vo);
							bean.setRankSeq(rankNo++);
							beanList.add(bean);
						}
						rank.setRegularTicketBeans(beanList);
					}
				} catch (Throwable e) {
					log.warn("Error when fetching week rank",e);
				}			
			}

			
		}, 0, TimeUnit.SECONDS);
		return rank;
	}

	@Override
	public RankListBean getWeekRank() throws StockAppBizException {
		if(context.getApplication().isInDebugMode()) {
			rank.setWeekRanKBeans(mockWeekRank());
			return rank;
		}
		context.invokeLater(new Runnable() {
			public void run() {
				try {
					List<WeekRankVO> volist = getService(IRestProxyService.class).getRestService(TradingResourse.class).getWeekRank();
					if (volist!=null&&volist.size()>0) {
						List<WeekRankBean> beanList = new ArrayList<WeekRankBean>();
						int rankNo =1;
						for (WeekRankVO weekRankVO : volist) {
							WeekRankBean bean = fromVO(weekRankVO);
							bean.setRankSeq(rankNo++);
							beanList.add(bean);
						}
						rank.setWeekRanKBeans(beanList);
					}
				} catch (Throwable e) {
					log.warn("Error when fetching week rank",e);
				}			
			}
		}, 0, TimeUnit.SECONDS);
		return rank;
	}
	@Override
	public TradingAccountBean getTradingAccountInfo(final String acctID)
			throws StockAppBizException {
			context.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						TradingAccountVO vo = getService(IRestProxyService.class).getRestService(TradingResourse.class).getAccount(acctID);
						if (vo!=null) {
							myTradingAccount.setId(vo.getId());
							myTradingAccount.setApplyFee(vo.getApplyFee());
							myTradingAccount.setAvalibleFee(vo.getAvalibleFee());
							myTradingAccount.setBuyDay(vo.getBuyDay());
							myTradingAccount.setFrozenVol(vo.getFrozenVol());
							myTradingAccount.setGainRate(vo.getGainRate());
							myTradingAccount.setLossLimit(vo.getLossLimit());
							myTradingAccount.setMaxStockCode(vo.getMaxStockCode());
							myTradingAccount.setMaxStockMarket(vo.getMaxStockMarket());
							myTradingAccount.setOver(vo.getOver());
							myTradingAccount.setSellDay(vo.getSellDay());
							myTradingAccount.setStatus(vo.getStatus());
							myTradingAccount.setTotalGain(vo.getTotalGain());
							myTradingAccount.setType(vo.getType());
							List<StockTradingOrderVO> orderVos = vo.getTradingOrders();
							if (orderVos!=null) {
								List<StockTradingOrderBean> list = new ArrayList<StockTradingOrderBean>();
								for (StockTradingOrderVO order : orderVos) {
									list.add(fromVO(order));
								}
								myTradingAccount.setTradingOrders(list);
							}
							myTradingAccount.setUsedFee(vo.getUsedFee());
							myTradingAccount.setVirtual(vo.isVirtual());
						}
					} catch (Exception e) {
						log.error(String.format("Error when fetch trading account info[id=%s]", acctID),e);
						throw new StockAppBizException("网络不给力，请稍候再试");
					}
				}
			}, 0, TimeUnit.SECONDS);
			return myTradingAccount;
	}
	// =================private method =======================================
	private TradingAccInfoBean fromVO(TradingAccInfoVO vo){
		if (vo==null) {
			return null;
		}
		TradingAccInfoBean bean = new TradingAccInfoBean();
		bean.setAcctID(vo.getAcctID());
		bean.setCreateDate(vo.getCreateDate());
		bean.setMaxStockCode(vo.getMaxStockCode());
		bean.setMaxStockMarket(vo.getMaxStockMarket());
		bean.setMaxStockName(vo.getMaxStockName());
		bean.setOver(vo.getOver());
		bean.setStatus(vo.getStatus());
		bean.setSum(vo.getSum());
		bean.setTotalGain(vo.getTotalGain());
		bean.setVirtual(vo.isVirtual());		
		return bean;
	}
	
	private StockTradingOrderBean fromVO(StockTradingOrderVO vo){
		if (vo==null) {
			return null;
		}
		StockTradingOrderBean bean = new StockTradingOrderBean();
		bean.setAmount(vo.getAmount());
		bean.setBuy(vo.getBuy());
		bean.setChangeRate(vo.getChangeRate());
		bean.setCurrentPirce(vo.getCurrentPirce());
		bean.setGain(vo.getGain());
		bean.setGainRate(vo.getGainRate());
		bean.setId(vo.getId());
		bean.setMarketCode(vo.getMarketCode());
		bean.setStatus(vo.getStatus());
		bean.setStockCode(vo.getStockCode());
		return bean;
	}
	
		private List<TradingAccInfoBean> mockData(int type) {
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
		private List<TradingRecordBean> mockTradingRecord(){
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
	private WeekRankBean fromVO(WeekRankVO vo){
		if (vo==null) {
			return null;
		}
		WeekRankBean bean = new WeekRankBean();
		bean.setDates(vo.getDates());
		bean.setGainCount(vo.getGainCount());
		bean.setGainRate(vo.getGainRate());
		bean.setGainRates(vo.getGainRates());
		bean.setNickName(vo.getNickName());
		bean.setTotalGain(vo.getTotalGain());
		bean.setUserId(vo.getUesrId());
		return bean;
	}	
	private MegagameRankBean fromVO(MegagameRankVO vo) {
		if (vo==null) {
			return null;
		}
		MegagameRankBean bean = new MegagameRankBean();
		bean.setAcctID(vo.getAcctID());
		bean.setGainRate(vo.getGainRate());
		bean.setGainRates(vo.getGainRates());
		bean.setMaxStockCode(vo.getMaxStockCode());
		bean.setMaxStockMarket(vo.getMaxStockMarket());
		bean.setNickName(vo.getNickName());
		bean.setOver(vo.getOver());
		bean.setStatus(vo.getStatus());
		bean.setTotalGain(vo.getTotalGain());
		bean.setUserId(vo.getUesrId());
		return bean;
	}

	private  RegularTicketBean fromVO(RegularTicketVO vo) {
		if (vo==null) {
			return null;
		}
		RegularTicketBean bean = new RegularTicketBean();
		bean.setGainCount(vo.getGainCount());
		bean.setNickName(vo.getNickName());
		bean.setRegular(vo.getRegular());
		return bean;
	}
	private void checkLogin(){
		if (!getService(UserManagementServiceImpl.class).isLogin()) {
			getService(IWorkbenchManager.class).getWorkbench().showPage("", null, null);
		}
	}
	public UserCreateTradAccInfoBean getUserCreateTradAccInfo() {
		checkLogin();
		context.invokeLater(new Runnable() {			
			public void run() {
				UserCreateTradAccInfoVO vo = null;
				try {
					vo = context.getService(IRestProxyService.class).getRestService(TradingResourse.class).getCreateStrategyInfo();
					if (vo!=null) {
						createTDConfig.setCapitalRate(vo.getCapitalRate());
						createTDConfig.setCostRate(vo.getCostRate());
						createTDConfig.setDepositRate(vo.getDepositRate());
						createTDConfig.setMaxAmount(vo.getMaxAmount());
						createTDConfig.setRateString(vo.getRateString());
						createTDConfig.setVoucherCostRate(0.00399f);
					}
				} catch (Throwable e) {
					log.warn("Failed to create trading account",e);
					throw new StockAppBizException(e.getMessage());
				}
			}
		}, 0, TimeUnit.SECONDS);
		return createTDConfig;
	}

	@Override
	public TradingAccountListBean getMyTradingAccountList()
			throws StockAppBizException {
		return null;
	}

	@Override
	public TradingAccountListBean getOtherTradingAccountList(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DealDetailBean getDealDetail(String accId) {
		context.invokeLater(new Runnable() {
			@Override
			public void run() {
				
			}
		}, 0, TimeUnit.SECONDS);
		dealDetailBean.setFund("10万");
		dealDetailBean.setPlRisk(0.001f);
		dealDetailBean.setUserGain(1000);
		dealDetailBean.setImgUrl(new String[]{"#"});
		dealDetailBean.setTradingRecords(mockTradingRecord());
		return dealDetailBean;
	}
	
	@Override
	public AuditDetailBean getAuditDetail(final String acctId) {
		if (context.getApplication().isInDebugMode()) {
			log.info("getAuditDetail: accid= "+acctId);
			auditDetailBean.setAccountPay("122.3");
			auditDetailBean.setBuyDay("2013-11-12");
			auditDetailBean.setDeadline("2013-11-13");
			auditDetailBean.setBuyAverage("39.9");
			auditDetailBean.setId("123");
			auditDetailBean.setFrozenAmount("0");
			auditDetailBean.setFund("10万");
			return auditDetailBean;
		}
		context.invokeLater(new Runnable() {
			public void run() {
				AuditDetailVO vo = null;
				try {
					vo = getRestService(TradingResourse.class).getAuditDetail(acctId);
				} catch (Throwable e) {
					log.warn("Failed to fetch audit detail info",e);
				}
				if (vo!=null) {
					auditDetailBean.setAccountPay(vo.getAccountPay());
					auditDetailBean.setBuyDay(vo.getBuyDay());
					auditDetailBean.setBuyAverage(vo.getBuyAverage());
					auditDetailBean.setCapitalRate(vo.getCapitalRate());
					auditDetailBean.setCost(vo.getCost());
					auditDetailBean.setDeadline(vo.getDeadline());
					auditDetailBean.setFrozenAmount(vo.getFrozenAmount());
					auditDetailBean.setFund(vo.getFund());					
					auditDetailBean.setId(vo.getId());
					auditDetailBean.setPayOut(vo.getPayOut());
					auditDetailBean.setPlRisk(vo.getPlRisk());
					auditDetailBean.setSellAverage(vo.getSellAverage());
					auditDetailBean.setTotalGain(vo.getTotalGain());
					auditDetailBean.setTradingCost(vo.getTradingCost());
					auditDetailBean.setTradingDate(vo.getTradingDate());
					auditDetailBean.setType(vo.getType());
					auditDetailBean.setUnfreezeAmount(vo.getFrozenAmount());
					auditDetailBean.setUserGain(vo.getUserGain());
					auditDetailBean.setVirtual(vo.isVirtual());
					
				}
			}
		}, 0, TimeUnit.SECONDS);
		
		return auditDetailBean;
	}

	@Override
	public void createTradingAccount(final Long captitalAmount, final float capitalRate,
			final boolean virtual, final float depositRate) throws StockAppBizException {
			checkLogin();
			context.invokeLater(new Runnable() {
				public void run() {
					try {
						StockResultVO vo = getRestService(TradingResourse.class).createTradingAccount(captitalAmount, capitalRate, virtual, depositRate);
						if (vo!=null) {
							if (vo.getSuccOrNot()==0) {
								if (log.isDebugEnabled()) {
									log.debug("Failed to create trading account, caused by "+vo.getCause());
								}
								throw new StockAppBizException(vo.getCause());
							}
							if (vo.getSuccOrNot()==1) {
								if (log.isDebugEnabled()) {
									log.debug("Create trading account successfully.");
								}
							}
						}
					} catch (Throwable e) {
						log.warn("Failed to create trading account",e);
						throw new StockAppBizException(e.getMessage());
					}
					
					
				}
			}, 0, TimeUnit.SECONDS);
	
	}
	private <T> T getRestService(Class<T> restResouce){
		return context.getService(IRestProxyService.class).getRestService(restResouce);
	}
	@Override
	public void buyStock(final String acctID, final String market, final String code,
			 String price,String amount) throws StockAppBizException {
		checkLogin();
		//check validation
		final long cPrice = Long.valueOf(price);//需要判断处理
		final long c_amount = Long.valueOf(amount);
		context.invokeLater(new Runnable() {
			public void run() {
				StockResultVO vo = null;
				try {
					vo = getRestService(TradingResourse.class).buyStock(acctID, market, code, cPrice, c_amount);
					if (vo!=null) {
						if (vo.getSuccOrNot()==0) {//表示失败
							if (log.isDebugEnabled()) {
								log.debug("Failed to buy stock, caused by "+vo.getCause());
							}
							throw new StockAppBizException(vo.getCause());
						}
						if (vo.getSuccOrNot()==1) {//表示成功
							if (log.isDebugEnabled()) {
								log.debug("Buy stock successfully.");
							}
						}
					}
				} catch (Exception e) {
					log.warn("Failed to buy stock",e);
					throw new StockAppBizException(e.getMessage());
				}
				
			}
		}, 0, TimeUnit.SECONDS);
		
	}

	@Override
	public void sellStock(final String acctID, final String market, final String code,
			String price, String amount) throws StockAppBizException {
		
		checkLogin();
		//check validation
		final long cPrice = Long.valueOf(price);//需要判断处理
		final long c_amount = Long.valueOf(amount);
		context.invokeLater(new Runnable() {
			public void run() {
				StockResultVO vo = null;
				try {
					vo = getRestService(TradingResourse.class).sellStock(acctID, market, code, cPrice, c_amount);
					if (vo!=null) {
						if (vo.getSuccOrNot()==0) {//表示失败
							if (log.isDebugEnabled()) {
								log.debug("Failed to sell stock, caused by "+vo.getCause());
							}
							throw new StockAppBizException(vo.getCause());
						}
						if (vo.getSuccOrNot()==1) {//表示成功
							if (log.isDebugEnabled()) {
								log.debug("Sell stock successfully.");
							}
						}
					}
				} catch (Exception e) {
					log.warn("Failed to sell stock",e);
					throw new StockAppBizException(e.getMessage());
				}
				
			}
		}, 0, TimeUnit.SECONDS);
	}

	@Override
	public void cancelOrder(final String orderID) throws StockAppBizException {
		checkLogin();
		//check validation
		context.invokeLater(new Runnable() {
			public void run() {
				StockResultVO vo = null;
				try {
					vo = getRestService(TradingResourse.class).cancelOrder(orderID);
					if (vo!=null) {
						if (vo.getSuccOrNot()==0) {//表示失败
							if (log.isDebugEnabled()) {
								log.debug("Failed to cancel order, caused by "+vo.getCause());
							}
							throw new StockAppBizException(vo.getCause());
						}
						if (vo.getSuccOrNot()==1) {//表示成功
							if (log.isDebugEnabled()) {
								log.debug("Cancel order successfully.");
							}
						}
					}
				} catch (Exception e) {
					log.warn("Failed to cancel order",e);
					throw new StockAppBizException(e.getMessage());
				}
				
			}
		}, 0, TimeUnit.SECONDS);
	}

	@Override
	public void quickBuy(final Long captitalAmount,  String capitalRate,
			final boolean virtual, final String stockMarket, final String stockCode,
			 String stockBuyAmount,  String depositRate)
			throws StockAppBizException {
		checkLogin();
		//check validation
		final float _capitalRate= Float.valueOf(capitalRate);
		final long _stockBuyAmount = Long.valueOf(stockBuyAmount);
		final float _depositRate = Float.valueOf(depositRate);
		context.invokeLater(new Runnable() {
			public void run() {
				StockResultVO vo = null;
				try {
					vo = getRestService(TradingResourse.class).quickBuy(captitalAmount, _capitalRate, virtual, stockMarket, stockCode, _stockBuyAmount, _depositRate);
					if (vo!=null) {
						if (vo.getSuccOrNot()==0) {//表示失败
							if (log.isDebugEnabled()) {
								log.debug("Failed to cancel order, caused by "+vo.getCause());
							}
							throw new StockAppBizException(vo.getCause());
						}
						if (vo.getSuccOrNot()==1) {//表示成功
							if (log.isDebugEnabled()) {
								log.debug("Cancel order successfully.");
							}
						}
					}
				} catch (Throwable e) {
					log.warn("Failed to cancel order",e);
					throw new StockAppBizException(e.getMessage());
				}
				
			}
		}, 0, TimeUnit.SECONDS);
		
	}

}
