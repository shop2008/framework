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
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.biz.AccidSelection;
import com.wxxr.mobile.stock.client.utils.Constants;

@View(name = "OtherViewMorePage", withToolbar = true, description = "xxx的成功操作", provideSelection=true)
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.user_view_more_layout")
public abstract class OtherViewMorePage extends PageBase implements IModelUpdater{

	@Bean(type = BindingType.Service)
	IUserManagementService usrService;
	@Bean(type=BindingType.Pojo,express="${userId!=null?(usrService.getMoreOtherPersonal(userId,0,otherHomeALimit,false)):null}")
	BindableListWrapper<GainBean> otherChallengeListBean;

	@Bean(type=BindingType.Pojo,express="${userId!=null?(usrService.getMoreOtherPersonal(userId,0,otherHomeVLimit,true)):null}")
	BindableListWrapper<GainBean> otherJoinListBean;
	
	@Field(valueKey = "options", binding = "${otherChallengeListBean!=null?otherChallengeListBean.data:null}", visibleWhen = "${curItemId == 0}")
	List<GainBean> actualRecordList;

	@Field(valueKey = "options", binding = "${otherJoinListBean!=null?otherJoinListBean.data:null}", visibleWhen = "${curItemId == 1}")
	List<GainBean> virtualRecordsList;

	@Field(valueKey = "checked", attributes = { @Attribute(name = "checked", value = "${curItemId == 0}"), })
	boolean actualRecordBtn;

	@Field(valueKey = "checked", attributes = { @Attribute(name = "checked", value = "${curItemId == 1}"), })
	boolean virtualRecordBtn;

	@Field(valueKey = "visible", binding = "${(curItemId==1)&&(((otherJoinListBean.data!=null)?(otherJoinListBean.data.size()>0?false:true):true))}")
	boolean noMoreVirtualRecordVisible;

	@Field(valueKey = "visible", binding = "${(curItemId==0)&&(((otherChallengeListBean.data!=null)?(otherChallengeListBean.data.size()>0?false:true):true))}")
	boolean noMoreActualRecordVisible;


	/** 其它用户--挑战交易盘每页初始条目 */
	@Bean
	int otherHomeALimit = 15;

	/** 其它用户--参赛交易盘每页初始条目 */
	@Bean
	int otherHomeVLimit = 15;
	
	@Bean
	boolean isVirtual;
	
	@OnShow
	void initData() {
		if(nickName != null) {
			getAppToolbar().setTitle(nickName+"的成功操作", null);
		}
	}
	
	String userId;
	
	String nickName;
	
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
			usrService.getMoreOtherPersonal(userId, 0, otherHomeALimit, false);
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
			usrService.getMoreOtherPersonal(userId, 0, otherHomeVLimit, true);
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

			if (otherJoinListBean != null) {
				List<GainBean> virtualList = otherJoinListBean.getData();
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
				map.put("isSelf", false);
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
			
				if (otherChallengeListBean != null) {
					List<GainBean> actualList = otherChallengeListBean.getData();
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
				
				map.put("isSelf", false);
				
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
				usrService.getMoreOtherPersonal(userId,0, this.otherHomeALimit, true);
			}
		
		return null;
	}

	@Command
	String handleActualBottomRefresh(InputEvent event) {
		
			// 用户自己的挑战交易记录
			if (usrService != null) {
				this.otherHomeALimit += 10;
				usrService.getMoreOtherPersonal(userId,0, this.otherHomeALimit, true);
			}
		
		return null;
	}

	@Command
	String handleVirtualBottomRefresh(InputEvent event) {
		
			if (usrService != null) {
				this.otherHomeVLimit += 10;
				usrService.getMoreOtherPersonal(userId,0, this.otherHomeVLimit, true);
			}
		
		return null;
	}

	@Command
	String handleVirtualTopRefresh(InputEvent event) {
		
			// 用户自己的挑战交易记录
			if (usrService != null) {
				usrService.getMoreOtherPersonal(userId,0, this.otherHomeVLimit, true);
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
						registerBean("curItemId", isVirtual?1:0);
					}
				}
				
				if (tempt != null && "userId".equals(key)) {
					if (tempt instanceof String) {
						String userId = (String) tempt;
						this.userId = userId;
						registerBean("userId", userId);
					}
				}
				
				if (tempt != null && Constants.KEY_USER_NAME_FLAG.equals(key)) {
					if (tempt instanceof String) {
						String userNickName = (String) tempt;
						this.nickName = userNickName;
					}
				}
			}
		}
	}
}
