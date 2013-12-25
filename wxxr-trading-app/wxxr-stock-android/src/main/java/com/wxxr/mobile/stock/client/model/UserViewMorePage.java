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

	@Bean(type = BindingType.Pojo, express = "${usrService.getMorePersonalRecords(0,myHomeALimit,false)}")
	BindableListWrapper<GainBean> myChallengeListBean;

	@Bean(type = BindingType.Pojo, express = "${usrService.getMorePersonalRecords(0,myHomeVLimit,true)}")
	BindableListWrapper<GainBean> myJoinListBean;

	@Field(valueKey = "options", binding = "${myChallengeListBean!=null?myChallengeListBean.data:null}", visibleWhen = "${curItemId == 0}")
	List<GainBean> actualRecordList;

	@Field(valueKey = "options", binding = "${myJoinListBean!=null?myJoinListBean.data:null}", visibleWhen = "${curItemId == 1}")
	List<GainBean> virtualRecordsList;

	@Field(valueKey = "checked", attributes = { @Attribute(name = "checked", value = "${curItemId == 0}"), })
	boolean actualRecordBtn;

	@Field(valueKey = "checked", attributes = { @Attribute(name = "checked", value = "${curItemId == 1}"), })
	boolean virtualRecordBtn;

	@Field(valueKey = "visible", binding = "${(curItemId==1)&&(((myJoinListBean.data!=null)?(myJoinListBean.data.size()>0?false:true):true))}")
	boolean noMoreVirtualRecordVisible;

	@Field(valueKey = "visible", binding = "${(curItemId==0)&&(((myChallengeListBean.data!=null)?(myChallengeListBean.data.size()>0?false:true):true))}")
	boolean noMoreActualRecordVisible;

	@Bean
	boolean isVirtual;

	/** 用户--参赛交易盘每页初始条目 */
	@Bean
	int myHomeVLimit = 15;

	/** 用户--挑战交易盘每页初始条目 */
	@Bean
	int myHomeALimit = 15;

	@Bean(type = BindingType.Pojo, express = "${userId!=null?usrService.getUserInfoById(userId):usrService.myUserInfo}")
	UserBean user;

	@SuppressWarnings("unused")
	@Menu(items = { "left"})
	private IMenu toolbar;

	@Command(uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}

	/**
	 * 挑战交易盘交易记录
	 * 
	 * @param event
	 * @return
	 */
	@Command
	String showActualRecords(InputEvent event) {
		registerBean("curItemId", 0);

		// 用户自己的挑战交易记录
		if (usrService != null) {
			usrService.getMorePersonalRecords(0, 15, false);
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
		registerBean("curItemId", 1);

		if (usrService != null) {
			usrService.getMorePersonalRecords(0, myHomeVLimit, true);
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
	String handleActualTopRefresh(InputEvent event) {
		
			if (usrService != null) {

				usrService.getMorePersonalRecords(0, this.myHomeALimit, true);
			}
		
		return null;
	}

	@Command
	String handleActualBottomRefresh(InputEvent event) {
		
			// 用户自己的挑战交易记录
			if (usrService != null) {

				this.myHomeALimit += 10;
				usrService.getMorePersonalRecords(0, this.myHomeALimit, true);
			}
		
		return null;
	}

	@Command
	String handleVirtualBottomRefresh(InputEvent event) {
		
			if (usrService != null) {
				this.myHomeVLimit += 10;
				usrService.getMorePersonalRecords(0, this.myHomeVLimit, true);
			}
		
		return null;
	}

	@Command
	String handleVirtualTopRefresh(InputEvent event) {
		
			// 用户自己的挑战交易记录
			if (usrService != null) {
				usrService.getMorePersonalRecords(0, this.myHomeVLimit, true);
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

	/*@OnShow
	void registerListener() {
		ISelectionService service = getUIContext().getWorkbenchManager()
				.getWorkbench().getSelectionService();
		ISelection selection = service.getSelection("otherUserPage");
		selectionChanged("otherUserPage", selection);
		service.addSelectionListener("otherUserPage", this);
	}

	@Override
	public void selectionChanged(String providerId, ISelection selection) {
		if (selection instanceof MyPageSelection) {
			MyPageSelection simpleSelection = (MyPageSelection) selection;
			this.userId = simpleSelection.getUsrId();
			registerBean("userId", this.userId);
		}
	}

	@OnHide
	void unregisterListener() {
		getUIContext().getWorkbenchManager().getWorkbench()
				.getSelectionService()
				.removeSelectionListener("otherUserPage", this);
	}*/

	@OnUIDestroy
	protected void clearData() {

	}
}
