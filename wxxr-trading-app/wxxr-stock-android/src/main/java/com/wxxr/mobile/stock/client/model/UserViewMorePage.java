package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.biz.AccidSelection;

@View(name = "userViewMorePage", withToolbar = true, description = "我的成功操作", provideSelection=true)
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.user_view_more_layout")
public abstract class UserViewMorePage extends PageBase implements
		IModelUpdater{

	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Bean(type = BindingType.Pojo, express = "${usrService.getMorePersonalRecords(myHomeAStart,myHomeALimit,false)}")
	BindableListWrapper<GainBean> myChallengeListBean;

	@Bean(type = BindingType.Pojo, express = "${usrService.getMorePersonalRecords(myHomeVStart,myHomeVLimit,true)}")
	BindableListWrapper<GainBean> myJoinListBean;

	@Field(valueKey = "options", binding = "${myChallengeListBean!=null?myChallengeListBean.getData(true):null}", visibleWhen = "${curItemId == 0}", upateAsync=true)
	List<GainBean> actualRecordList;

	@Field(valueKey = "options", binding = "${myJoinListBean!=null?myJoinListBean.getData(true):null}", visibleWhen = "${curItemId == 1}", upateAsync=true)
	List<GainBean> virtualRecordsList;

	@Field(valueKey = "checked", attributes = { @Attribute(name = "checked", value = "${curItemId == 0}")})
	boolean actualRecordBtn;

	@Field(valueKey = "checked", attributes = { @Attribute(name = "checked", value = "${curItemId == 1}")})
	boolean virtualRecordBtn;

	/*@Field(valueKey = "visible", binding = "${(curItemId==1)&&(((myJoinListBean.data!=null)?(myJoinListBean.data.size()>0?false:true):true))}")
	boolean noMoreVirtualRecordVisible;*/

	/*@Field(valueKey = "visible", binding = "${(curItemId==0)&&(((myChallengeListBean.data!=null)?(myChallengeListBean.data.size()>0?false:true):true))}")
	boolean noMoreActualRecordVisible;*/

	@Bean
	boolean isVirtual;

	/** 用户--参赛交易盘每页初始条目 */
	@Bean
	int myHomeVLimit = 20;

	/** 用户--挑战交易盘每页初始条目 */
	@Bean
	int myHomeALimit = 20;
	
	@Bean
	int myHomeVStart = 0;
	
	@Bean
	int myHomeAStart = 0;

	int curItemId = 0;
	@SuppressWarnings("unused")
	@Menu(items = { "left"})
	private IMenu toolbar;

	@Command(uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}

	@Field(valueKey = "text",visibleWhen = "${curItemId==1}",attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "${myJoinListBean!=null&&myJoinListBean.data!=null&&myJoinListBean.data.size()>0?true:false}")})
	String virtualRefreshView;
	
	@Field(valueKey = "text",visibleWhen = "${curItemId==0}",attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "${myChallengeListBean!=null&&myChallengeListBean.data!=null&&myChallengeListBean.data.size()>0?true:false}")})
	String actualRefreshView;
	
	
	/**
	 * 挑战交易盘交易记录
	 * 
	 * @param event
	 * @return
	 */
	@Command
	String showActualRecords(InputEvent event) {
		curItemId = 0;
		registerBean("curItemId", 0);

		// 用户自己的挑战交易记录
		if (usrService != null) {
			usrService.getMorePersonalRecords(0, myChallengeListBean.getData().size(), false);
		}

		return null;
	}

	/**
	 * 参赛交易盘操作记录
	 * 
	 * @param event
	 * @return
	 */
	@Command
	String showVirtualRecords(InputEvent event) {
		
		curItemId = 1;
		registerBean("curItemId", 1);

		if (usrService != null) {
			usrService.getMorePersonalRecords(0, myJoinListBean.getData().size(), true);
		}

		return null;
	}

	@Command(commandName = "virtualRecordItemClicked", navigations = {
			@Navigation(on = "operationDetails", showPage = "OperationDetails"),
			@Navigation(on = "SellOut", showPage = "sellTradingAccount"),
			@Navigation(on = "BuyIn", showPage = "TBuyTradingPage") })
	CommandResult virtualRecordItemClicked(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_ITEM_CLICK)) {
			int position = (Integer) event.getProperty("position");
			GainBean virtualBean = null;

			if (myJoinListBean != null) {
				List<GainBean> virtualList = myJoinListBean.getData();
				if (virtualList != null && virtualList.size() > 0) {
					virtualBean = virtualList.get(position);
				}
			}

			CommandResult result = null;
			if (virtualBean != null) {
				Long accId = virtualBean.getTradingAccountId();
				String tradeStatus = virtualBean.getOver();
				Boolean isVirtual = virtualBean.getVirtual();
				result = new CommandResult();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("accid", accId);
				map.put("isVirtual", isVirtual);
				map.put("isSelf", true);
				result.setPayload(map);
				if ("CLOSED".equals(tradeStatus)) {
					result.setResult("operationDetails");
				} else if ("UNCLOSE".equals(tradeStatus)) {
					int status = virtualBean.getStatus();
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
			}
			return result;
		}
		return null;
	}

	@Command(commandName = "actualRecordItemClicked", navigations = {
			@Navigation(on = "operationDetails", showPage = "OperationDetails"),
			@Navigation(on = "SellOut", showPage = "sellTradingAccount"),
			@Navigation(on = "BuyIn", showPage = "TBuyTradingPage") })
	CommandResult actualRecordItemClicked(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_ITEM_CLICK)) {
			int position = (Integer) event.getProperty("position");
			GainBean actualBean = null;
			
				if (myChallengeListBean != null) {
					List<GainBean> actualList = myChallengeListBean.getData();
					if (actualList != null && actualList.size() > 0) {
						actualBean = actualList.get(position);
					}
				
				}
			CommandResult result = null;
			if (actualBean != null) {
				/** 交易盘ID */
				Long accId = actualBean.getTradingAccountId();
				String tradeStatus = actualBean.getOver();
				Boolean isVirtual = actualBean.getVirtual();
				result = new CommandResult();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("accId", accId);
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
			}
			return result;
		}
		return null;
	}

	@Command
	String handleActualRefresh(InputEvent event) {
			
		if(event.getEventType().equals("TopRefresh")) {
			if (usrService != null) {
					usrService.getMorePersonalRecords(0, myChallengeListBean.getData().size(), false, true);
				}
		} else if(event.getEventType().equals("BottomRefresh")) {
			int completeSize = 0;
			if(myChallengeListBean != null)
				completeSize = myChallengeListBean.getData().size();
			myHomeAStart = completeSize;
			
			if(usrService != null) {
				usrService.getMorePersonalRecords(myHomeAStart, myHomeALimit, false, true);
			}
		}
		return null;
	}

	

//	@Command
//	String handleVirtualBottomRefresh(InputEvent event) {
//		
//		int completeSize = 0;
//		if(myJoinListBean != null)
//			completeSize = myJoinListBean.getData().size();
//		myHomeVStart += completeSize;
//		if(usrService != null) {
//			usrService.getMorePersonalRecords(myHomeVStart, myHomeVLimit, true);
//		}
//		return null;
//	}
	
	
//	@Command
//	String handleActualBottomRefresh(InputEvent event) {
//		int completeSize = 0;
//		if(myChallengeListBean != null)
//			completeSize = myChallengeListBean.getData().size();
//		myHomeAStart += completeSize;
//		if(usrService != null) {
//			usrService.getMorePersonalRecords(myHomeAStart, myHomeALimit, false);
//		}
//		return null;
//	}

	@Command
	String handleVirtualRefresh(InputEvent event) {
			if (event.getEventType().equals("TopRefresh")) {
				// 用户自己的挑战交易记录
				if (usrService != null) {
					usrService.getMorePersonalRecords(0, myJoinListBean
							.getData().size(), true, true);
				}
			} else if(event.getEventType().equals("BottomRefresh")) {
				int completeSize = 0;
				if(myJoinListBean != null)
					completeSize = myJoinListBean.getData().size();
				myHomeVStart += completeSize;
				if(usrService != null) {
					usrService.getMorePersonalRecords(myHomeVStart, myHomeVLimit, true, true);
				}
			}
		return null;
	}

	@Override
	public void updateModel(Object value) {

		if (value instanceof Map) {
			Map temp = (Map) value;
			for (Object key : temp.keySet()) {
				Object tempt = temp.get(key);
				if (tempt != null && "isVirtual".equals(key)) {
					if (tempt instanceof Boolean) {
						Boolean isVirtual = (Boolean) tempt;
						this.isVirtual = isVirtual;
						registerBean("curItemId", isVirtual ? 1 : 0);
					}
				}
			}
		}
	}

	@Command
	String handlerReTryClicked(InputEvent event) {
		
		if(curItemId == 0) {
			usrService.getMorePersonalRecords(0, 20, false);
		} else if(curItemId == 1) {
			usrService.getMorePersonalRecords(0, 20, true);
		}
		return null;
	}
}
