package com.wxxr.mobile.stock.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;

import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.bean.PersonalHomePageBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserLoginManagementService;
import com.wxxr.mobile.stock.client.biz.AccidSelection;
import com.wxxr.mobile.stock.client.utils.Constants;

@View(name = "otherUserPage", withToolbar=true, description="---的个人主页",provideSelection=true)
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.other_user_page_layout")
public abstract class OtherUserPage extends PageBase implements IModelUpdater {

	
	@OnShow
	void initData() {
		getAppToolbar().setTitle(userName+"的主页", null);
	}
	
	
	@Bean(type=BindingType.Service)
	IUserLoginManagementService service;
	
	@Bean(type=BindingType.Pojo, express="${service.getUserInfoById(userId)}")
	UserBean userBean;
	
	@Bean(type=BindingType.Pojo, express="${service!=null?service.getOtherPersonalHomePage(userId,false):null}")
	PersonalHomePageBean personalHomePageBean;
	
	@Field(valueKey="options", binding="${personalHomePageBean!=null?personalHomePageBean.allist:null}", 
			attributes={
			@Attribute(name="joinShareCount", value="${personalHomePageBean!=null?personalHomePageBean.virtualCount:0}"), 
			@Attribute(name="challengeShareCount", value="${personalHomePageBean!=null?personalHomePageBean.actualCount:0}"),
			@Attribute(name="userHomeBackUri", value="${userBean!=null?userBean.homeBack:'resourceId:drawable/back1'}"),
			@Attribute(name="userIconUri", value="${userBean!=null?userBean.userPic:'resourceId:drawable/head4'}"),
			@Attribute(name="totalScoreProfit", value="${personalHomePageBean!=null?personalHomePageBean.voucherVol:0}"),
			@Attribute(name="totalMoneyProfit", value="${personalHomePageBean!=null?personalHomePageBean.totalProfit:0.00}")
	})
	List<GainBean> successTradeRecords;
	
	private String userId;


	private String userName;
	
	
	@Command(commandName = "handleTradeRecordItemClick", navigations = {
			@Navigation(on = "operationDetails", showPage = "OperationDetails"),
			@Navigation(on = "SellOut", showPage = "sellTradingAccount"),
			@Navigation(on = "BuyIn", showPage = "TBuyTradingPage") })
	CommandResult handleTradeRecordItemClick(InputEvent event) {
		
		int position = (Integer) event.getProperty("position");
		List<GainBean> allList = null;
		allList = personalHomePageBean.getAllist();
		
		List<GainBean> virtualList = null;
		List<GainBean> actualList = null;
		
		GainBean actualBean = null;
		if(allList != null && allList.size()>0) {
			virtualList = new ArrayList<GainBean>();
			actualList = new ArrayList<GainBean>();
			
			for(GainBean vo: allList) {
				if(vo.getVirtual()) {
					virtualList.add(vo);
				} else {
					actualList.add(vo);
				}
			}
		}
		Comparator<GainBean> comparator = new Comparator<GainBean>() {

			@Override
			public int compare(final GainBean lhs, final GainBean rhs) {
				
				Long lh = null;
				Long rh = null;
				if(lhs != null) {
					lh = lhs.getTradingAccountId();
				}
				
				if(rhs != null) {
					rh = rhs.getTradingAccountId();
				}
				
				if(lh !=null && rh !=null) {
					return rh > lh? 1:-1;
				}
				return 0;
			}
		};
		List<GainBean> sortList = new ArrayList<GainBean>();
		if(virtualList!=null && virtualList.size()>0) {
			Collections.sort(virtualList, comparator);
			sortList.addAll(virtualList);
		}
		
		if(actualList!=null && actualList.size()>0) {
			Collections.sort(actualList, comparator);
			sortList.addAll(actualList);
		}
		
		actualBean = sortList.get(position);
		CommandResult result = null;	
		if (actualBean != null) {
			
			Long accId = actualBean.getTradingAccountId();
			String tradeStatus = actualBean.getOver();
			Boolean isVirtual = actualBean.getVirtual();
			result = new CommandResult();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("accid", accId);
			map.put("isVirtual", isVirtual);
			map.put("isSelf", true);
			result.setPayload(map);
			if ("CLOSED".equals(tradeStatus)) {
				result.setResult("operationDetails");
			} else if ("UNCLOSE".equals(tradeStatus)) {
				int status = actualBean.getStatus();
				if (status == 0) {
					// 进入卖出界面
					result.setResult("SellOut");
				} else if (status == 1) {
					// 进入买入界面
					result.setResult("BuyIn");
				}
			}
			updateSelection(new AccidSelection(String.valueOf(accId),
					isVirtual));
			
			return result;
		}
		
		return null;
	}
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof Map) {
			Map temp = (Map) value;
			for (Object key : temp.keySet()) {
				Object tempt = temp.get(key);
				if (tempt != null && Constants.KEY_USER_ID_FLAG.equals(key)) {
					if (tempt instanceof String) {
						userId = (String) tempt;
					}
					registerBean("userId", userId);
				}
				
				if (tempt != null && Constants.KEY_USER_NAME_FLAG.equals(key)) {
					if (tempt instanceof String) {
						userName = (String) tempt;
						registerBean("userName", userName);
					}
				}
			}
		}
	}
}
