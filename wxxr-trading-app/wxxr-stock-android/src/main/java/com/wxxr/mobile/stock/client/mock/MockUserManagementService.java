/**
 * 
 */
package com.wxxr.mobile.stock.client.mock;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.bean.PersonalHomePageBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.impl.UserManagementServiceImpl;

/**
 * @author wangxuyang
 * 
 */
public class MockUserManagementService extends UserManagementServiceImpl {

	@Override
	public UserBean getMyUserInfo() {
		UserBean userBean = new UserBean();
		userBean.setUserPic("resourceId:drawable/head2");
		userBean.setHomeBack("resourceId:drawable/back4");
		userBean.setNickName("海绵宝宝");
		return userBean;
	}

	@Override
	public UserBean getUserInfoById(String userId) {
		UserBean userBean = new UserBean();
		userBean.setUserPic("resourceId:drawable/head2");
		userBean.setHomeBack("resourceId:drawable/back4");
		userBean.setNickName("海绵宝宝");
		return userBean;
	}

	@Override
	public PersonalHomePageBean getOtherPersonalHomePage(String userId) {
		PersonalHomePageBean pHomeBean = new PersonalHomePageBean();
		pHomeBean.setActualCount(2);
		pHomeBean.setTotalProfit(30020.930);
		pHomeBean.setVoucherVol(15000L);
		pHomeBean.setVirtualCount(3);
		List<GainBean> actualList = new ArrayList<GainBean>();

		GainBean bean;
		bean = new GainBean();
		bean.setCloseTime("" + System.currentTimeMillis());
		bean.setMaxStockCode("601020");
		bean.setMaxStockName("智能机器");
		bean.setStatus(0);// 0 T+1日交易盘，1 T日交易盘
		bean.setSum(150000L);
		bean.setUserGain(2000L);
		bean.setTotalGain(10000L);
		bean.setOver("CLOSED");
		bean.setVirtual(false);
		actualList.add(bean);

		bean = new GainBean();
		bean.setCloseTime("" + System.currentTimeMillis());
		bean.setMaxStockCode("601021");
		bean.setMaxStockName("重庆板块");
		bean.setStatus(1);// 0 T+1日交易盘，1 T日交易盘
		bean.setSum(170000L);
		bean.setTotalGain(10000L);
		bean.setUserGain(1000L);
		bean.setOver("UNCLOSE");
		bean.setVirtual(false);
		actualList.add(bean);

		bean = new GainBean();
		bean.setCloseTime("" + System.currentTimeMillis());
		bean.setMaxStockCode("601022");
		bean.setMaxStockName("苏宁云商");
		bean.setStatus(1);// 0 T+1日交易盘，1 T日交易盘
		bean.setSum(800000L);
		bean.setTotalGain(10000L);
		bean.setUserGain(-1000L);
		bean.setOver("UNCLOSE");
		bean.setVirtual(false);
		actualList.add(bean);

		bean = new GainBean();
		bean.setCloseTime("" + System.currentTimeMillis());
		bean.setMaxStockCode("601026");
		bean.setMaxStockName("津滨发展");
		bean.setStatus(1);// 0 T+1日交易盘，1 T日交易盘
		bean.setSum(10000L);
		bean.setTotalGain(10000L);
		bean.setUserGain(-1000L);
		bean.setOver("CLOSED");
		bean.setVirtual(false);
		actualList.add(bean);

		pHomeBean.setActualList(actualList);

		List<GainBean> virtualList = new ArrayList<GainBean>();
		GainBean vBean;
		vBean = new GainBean();
		vBean.setCloseTime("" + System.currentTimeMillis());
		vBean.setMaxStockCode("601026");
		vBean.setMaxStockName("水利建设");
		vBean.setStatus(0);// 0 T+1日交易盘，1 T日交易盘
		vBean.setSum(20000L);
		vBean.setUserGain(-1000L);
		vBean.setTotalGain(10000L);
		vBean.setOver("CLOSED");
		vBean.setVirtual(true);
		virtualList.add(vBean);

		vBean = new GainBean();
		vBean.setCloseTime("" + System.currentTimeMillis());
		vBean.setMaxStockCode("601030");
		vBean.setMaxStockName("农林牧渔");
		vBean.setStatus(1);// 0 T+1日交易盘，1 T日交易盘
		vBean.setSum(90000L);
		vBean.setUserGain(1000L);
		vBean.setVirtual(true);
		vBean.setTotalGain(10000L);
		vBean.setOver("CLOSED");
		virtualList.add(vBean);

		vBean = new GainBean();
		vBean.setCloseTime("" + System.currentTimeMillis());
		vBean.setMaxStockCode("601032");
		vBean.setMaxStockName("航天军工");
		vBean.setStatus(0);// 0 T+1日交易盘，1 T日交易盘
		vBean.setSum(80000L);
		vBean.setTotalGain(10000L);
		vBean.setUserGain(2000L);
		vBean.setVirtual(true);
		vBean.setOver("UNCLOSE");
		virtualList.add(vBean);
		pHomeBean.setVirtualList(virtualList);

		return pHomeBean;
	}

	@Override
	public PersonalHomePageBean getMyPersonalHomePage() {
		PersonalHomePageBean pHomeBean = new PersonalHomePageBean();
		pHomeBean.setActualCount(2);
		pHomeBean.setTotalProfit(30020.930);
		pHomeBean.setVoucherVol(15000L);
		pHomeBean.setVirtualCount(3);
		List<GainBean> actualList = new ArrayList<GainBean>();

		GainBean bean;
		bean = new GainBean();
		bean.setCloseTime("" + System.currentTimeMillis());
		bean.setMaxStockCode("601020");
		bean.setMaxStockName("智能机器");
		bean.setStatus(0);// 0 T+1日交易盘，1 T日交易盘
		bean.setSum(150000L);
		bean.setUserGain(2000L);
		bean.setTotalGain(10000L);
		bean.setOver("CLOSED");
		bean.setVirtual(false);
		actualList.add(bean);

		bean = new GainBean();
		bean.setCloseTime("" + System.currentTimeMillis());
		bean.setMaxStockCode("601021");
		bean.setMaxStockName("重庆板块");
		bean.setStatus(1);// 0 T+1日交易盘，1 T日交易盘
		bean.setSum(170000L);
		bean.setUserGain(1000L);
		bean.setTotalGain(10000L);
		bean.setVirtual(false);
		bean.setOver("UNCLOSE");
		actualList.add(bean);

		bean = new GainBean();
		bean.setCloseTime("" + System.currentTimeMillis());
		bean.setMaxStockCode("601022");
		bean.setMaxStockName("苏宁云商");
		bean.setStatus(1);// 0 T+1日交易盘，1 T日交易盘
		bean.setSum(800000L);
		bean.setUserGain(-1000L);
		bean.setTotalGain(10000L);
		bean.setVirtual(false);
		bean.setOver("CLOSED");
		actualList.add(bean);

		bean = new GainBean();
		bean.setCloseTime("" + System.currentTimeMillis());
		bean.setMaxStockCode("601026");
		bean.setMaxStockName("津滨发展");
		bean.setStatus(1);// 0 T+1日交易盘，1 T日交易盘
		bean.setSum(10000L);
		bean.setTotalGain(10000L);
		bean.setUserGain(-1000L);
		bean.setVirtual(false);
		bean.setOver("UNCLOSE");
		actualList.add(bean);

		pHomeBean.setActualList(actualList);

		List<GainBean> virtualList = new ArrayList<GainBean>();
		GainBean vBean;
		vBean = new GainBean();
		vBean.setCloseTime("" + System.currentTimeMillis());
		vBean.setMaxStockCode("601026");
		vBean.setMaxStockName("水利建设");
		vBean.setStatus(0);// 0 T+1日交易盘，1 T日交易盘
		vBean.setSum(20000L);
		vBean.setUserGain(-1000L);
		bean.setTotalGain(10000L);
		bean.setOver("UNCLOSE");
		vBean.setVirtual(true);
		virtualList.add(vBean);

		vBean = new GainBean();
		vBean.setCloseTime("" + System.currentTimeMillis());
		vBean.setMaxStockCode("601030");
		vBean.setMaxStockName("农林牧渔");
		vBean.setStatus(1);// 0 T+1日交易盘，1 T日交易盘
		vBean.setSum(90000L);
		vBean.setUserGain(1000L);
		bean.setOver("UNCLOSE");
		bean.setTotalGain(10000L);
		vBean.setVirtual(true);
		virtualList.add(vBean);

		vBean = new GainBean();
		vBean.setCloseTime("" + System.currentTimeMillis());
		vBean.setMaxStockCode("601032");
		vBean.setMaxStockName("航天军工");
		vBean.setStatus(0);// 0 T+1日交易盘，1 T日交易盘
		vBean.setSum(80000L);
		vBean.setUserGain(2000L);
		bean.setTotalGain(10000L);
		bean.setOver("CLOSED");
		vBean.setVirtual(true);
		virtualList.add(vBean);
		pHomeBean.setVirtualList(virtualList);

		return pHomeBean;
	}

	@Override
	public PersonalHomePageBean getMoreOtherPersonal(String userId, int start,
			int limit, boolean virtual) {
		PersonalHomePageBean pHomePageBean = new PersonalHomePageBean();

		if (virtual) {
			List<GainBean> virtualList = new ArrayList<GainBean>();

			GainBean vBean;
			vBean = new GainBean();
			vBean.setCloseTime("" + System.currentTimeMillis());
			vBean.setMaxStockCode("666666");
			vBean.setMaxStockName("他人");
			vBean.setStatus(0);// 0 T+1日交易盘，1 T日交易盘
			vBean.setSum(20000L);
			vBean.setTradingAccountId(1020L);
			vBean.setUserGain(-1000L);
			vBean.setTotalGain(10000L);
			vBean.setVirtual(true);
			vBean.setOver("UNCLOSE");
			virtualList.add(vBean);
			
			vBean = new GainBean();
			vBean.setCloseTime("" + System.currentTimeMillis());
			vBean.setMaxStockCode("601026");
			vBean.setMaxStockName("水利建设");
			vBean.setStatus(0);// 0 T+1日交易盘，1 T日交易盘
			vBean.setSum(20000L);
			vBean.setUserGain(-1000L);
			vBean.setTotalGain(10000L);
			vBean.setVirtual(true);
			vBean.setOver("CLOSED");
			virtualList.add(vBean);

			vBean = new GainBean();
			vBean.setCloseTime("" + System.currentTimeMillis());
			vBean.setMaxStockCode("601030");
			vBean.setMaxStockName("农林牧渔");
			vBean.setStatus(1);// 0 T+1日交易盘，1 T日交易盘
			vBean.setSum(90000L);
			vBean.setUserGain(1000L);
			vBean.setTotalGain(10000L);
			vBean.setVirtual(true);
			vBean.setOver("UNCLOSE");
			virtualList.add(vBean);

			vBean = new GainBean();
			vBean.setCloseTime("" + System.currentTimeMillis());
			vBean.setMaxStockCode("601032");
			vBean.setMaxStockName("航天军工");
			vBean.setStatus(0);// 0 T+1日交易盘，1 T日交易盘
			vBean.setSum(80000L);
			vBean.setUserGain(2000L);
			vBean.setTotalGain(10000L);
			vBean.setOver("CLOSED");
			vBean.setVirtual(true);
			virtualList.add(vBean);

			pHomePageBean.setVirtualList(virtualList);
		} else {
			List<GainBean> actualList = new ArrayList<GainBean>();

			GainBean bean;
			bean = new GainBean();
			bean.setCloseTime("" + System.currentTimeMillis());
			bean.setMaxStockCode("888888");
			bean.setTradingAccountId(1020L);
			bean.setMaxStockName("他人");
			bean.setStatus(1);// 0 T+1日交易盘，1 T日交易盘
			bean.setSum(150000L);
			bean.setUserGain(2000L);
			bean.setTotalGain(10000L);
			bean.setVirtual(false);
			bean.setOver("UNCLOSE");
			actualList.add(bean);
			
			bean = new GainBean();
			bean.setCloseTime("" + System.currentTimeMillis());
			bean.setMaxStockCode("601020");
			bean.setMaxStockName("智能机器");
			bean.setStatus(0);// 0 T+1日交易盘，1 T日交易盘
			bean.setSum(150000L);
			bean.setUserGain(2000L);
			bean.setTotalGain(10000L);
			bean.setVirtual(false);
			bean.setOver("CLOSED");
			actualList.add(bean);

			bean = new GainBean();
			bean.setCloseTime("" + System.currentTimeMillis());
			bean.setMaxStockCode("601021");
			bean.setMaxStockName("重庆板块");
			bean.setStatus(1);// 0 T+1日交易盘，1 T日交易盘
			bean.setSum(170000L);
			bean.setUserGain(1000L);
			bean.setTotalGain(10000L);
			bean.setVirtual(false);
			bean.setOver("UNCLOSE");
			actualList.add(bean);

			bean = new GainBean();
			bean.setCloseTime("" + System.currentTimeMillis());
			bean.setMaxStockCode("601022");
			bean.setMaxStockName("苏宁云商");
			bean.setStatus(1);// 0 T+1日交易盘，1 T日交易盘
			bean.setSum(800000L);
			bean.setUserGain(-1000L);
			bean.setTotalGain(10000L);
			bean.setVirtual(false);
			bean.setOver("CLOSED");
			actualList.add(bean);

			bean = new GainBean();
			bean.setCloseTime("" + System.currentTimeMillis());
			bean.setMaxStockCode("601026");
			bean.setMaxStockName("津滨发展");
			bean.setStatus(1);// 0 T+1日交易盘，1 T日交易盘
			bean.setSum(10000L);
			bean.setTotalGain(10000L);
			bean.setUserGain(-1000L);
			bean.setVirtual(false);
			actualList.add(bean);
			bean.setOver("UNCLOSE");
			pHomePageBean.setActualList(actualList);
		}
		return pHomePageBean;
	}

	@Override
	public PersonalHomePageBean getMorePersonalRecords(int start, int limit,
			boolean virtual) {
		PersonalHomePageBean pHomePageBean = new PersonalHomePageBean();

		if (virtual) {
			List<GainBean> virtualList = new ArrayList<GainBean>();

			GainBean vBean;
			vBean = new GainBean();
			vBean.setCloseTime("" + System.currentTimeMillis());
			vBean.setMaxStockCode("601026");
			vBean.setMaxStockName("水利建设");
			vBean.setStatus(0);// 0 T+1日交易盘，1 T日交易盘
			vBean.setSum(20000L);
			vBean.setUserGain(-1000L);
			vBean.setTotalGain(10000L);
			vBean.setVirtual(true);
			vBean.setOver("CLOSED");
			virtualList.add(vBean);

			vBean = new GainBean();
			vBean.setCloseTime("" + System.currentTimeMillis());
			vBean.setMaxStockCode("601030");
			vBean.setMaxStockName("农林牧渔");
			vBean.setStatus(1);// 0 T+1日交易盘，1 T日交易盘
			vBean.setSum(90000L);
			vBean.setUserGain(1000L);
			vBean.setTotalGain(10000L);
			vBean.setVirtual(true);
			vBean.setOver("UNCLOSE");
			virtualList.add(vBean);

			vBean = new GainBean();
			vBean.setCloseTime("" + System.currentTimeMillis());
			vBean.setMaxStockCode("601032");
			vBean.setMaxStockName("航天军工");
			vBean.setStatus(0);// 0 T+1日交易盘，1 T日交易盘
			vBean.setSum(80000L);
			vBean.setUserGain(2000L);
			vBean.setTotalGain(10000L);
			vBean.setOver("CLOSED");
			vBean.setVirtual(true);
			virtualList.add(vBean);

			pHomePageBean.setVirtualList(virtualList);
		} else {
			List<GainBean> actualList = new ArrayList<GainBean>();

			GainBean bean;
			bean = new GainBean();
			bean.setCloseTime("" + System.currentTimeMillis());
			bean.setMaxStockCode("601020");
			bean.setMaxStockName("智能机器");
			bean.setStatus(0);// 0 T+1日交易盘，1 T日交易盘
			bean.setSum(150000L);
			bean.setUserGain(2000L);
			bean.setTotalGain(10000L);
			bean.setVirtual(false);
			bean.setOver("CLOSED");
			actualList.add(bean);

			bean = new GainBean();
			bean.setCloseTime("" + System.currentTimeMillis());
			bean.setMaxStockCode("601021");
			bean.setMaxStockName("重庆板块");
			bean.setStatus(1);// 0 T+1日交易盘，1 T日交易盘
			bean.setSum(170000L);
			bean.setUserGain(1000L);
			bean.setTotalGain(10000L);
			bean.setVirtual(false);
			bean.setOver("UNCLOSE");
			actualList.add(bean);

			bean = new GainBean();
			bean.setCloseTime("" + System.currentTimeMillis());
			bean.setMaxStockCode("601022");
			bean.setMaxStockName("苏宁云商");
			bean.setStatus(1);// 0 T+1日交易盘，1 T日交易盘
			bean.setSum(800000L);
			bean.setUserGain(-1000L);
			bean.setTotalGain(10000L);
			bean.setVirtual(false);
			bean.setOver("CLOSED");
			actualList.add(bean);

			bean = new GainBean();
			bean.setCloseTime("" + System.currentTimeMillis());
			bean.setMaxStockCode("601026");
			bean.setMaxStockName("津滨发展");
			bean.setStatus(1);// 0 T+1日交易盘，1 T日交易盘
			bean.setSum(10000L);
			bean.setTotalGain(10000L);
			bean.setUserGain(-1000L);
			bean.setVirtual(false);
			actualList.add(bean);
			bean.setOver("UNCLOSE");
			pHomePageBean.setActualList(actualList);
		}
		return pHomePageBean;
	}
}
