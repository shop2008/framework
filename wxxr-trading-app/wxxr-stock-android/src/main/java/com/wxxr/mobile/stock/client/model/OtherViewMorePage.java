package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
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
import com.wxxr.mobile.stock.app.service.IUserLoginManagementService;
import com.wxxr.mobile.stock.client.biz.AccidSelection;
import com.wxxr.mobile.stock.client.utils.Constants;

@View(name = "OtherViewMorePage", withToolbar = true, description = "xxx的成功操作", provideSelection=true)
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.user_view_more_layout")
public abstract class OtherViewMorePage extends PageBase implements IModelUpdater{

	@Bean(type = BindingType.Service)
	IUserLoginManagementService usrService;
	@Bean(type=BindingType.Pojo,express="${userId!=null?(usrService.getMoreOtherPersonal(userId,otherHomeAStart,otherHomeALimit,false)):null}")
	BindableListWrapper<GainBean> otherChallengeListBean;

	@Bean(type=BindingType.Pojo,express="${userId!=null?(usrService.getMoreOtherPersonal(userId,otherHomeVStart,otherHomeVLimit,true)):null}")
	BindableListWrapper<GainBean> otherJoinListBean;
	
	@Field(valueKey = "options", upateAsync=true,binding = "${otherChallengeListBean!=null?otherChallengeListBean.getData(true):null}", visibleWhen = "${curItemId == 0}")
	List<GainBean> actualRecordList;

	@Field(valueKey = "options", upateAsync=true,binding = "${otherJoinListBean!=null?otherJoinListBean.getData(true):null}", visibleWhen = "${curItemId == 1}")
	List<GainBean> virtualRecordsList;

	@Field(valueKey = "checked", attributes = { @Attribute(name = "checked", value = "${curItemId == 0}")})
	boolean actualRecordBtn;

	@Field(valueKey = "checked", attributes = { @Attribute(name = "checked", value = "${curItemId == 1}")})
	boolean virtualRecordBtn;

	/*@Field(valueKey = "visible", binding = "${(curItemId==1)&&(((otherJoinListBean.data!=null)?(otherJoinListBean.data.size()>0?false:true):true))}")
	boolean noMoreVirtualRecordVisible;

	@Field(valueKey = "visible", binding = "${(curItemId==0)&&(((otherChallengeListBean.data!=null)?(otherChallengeListBean.data.size()>0?false:true):true))}")
	boolean noMoreActualRecordVisible;*/


	/** 其它用户--挑战交易盘每页初始条目 */
	@Bean
	int otherHomeALimit = 20;

	/** 其它用户--参赛交易盘每页初始条目 */
	@Bean
	int otherHomeVLimit = 20;
	
	@Bean
	int otherHomeVStart = 0;
	
	@Bean
	int otherHomeAStart = 0;
	@Bean
	boolean isVirtual;
	
	@Field(valueKey = "text",visibleWhen = "${curItemId==1}",attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "${otherJoinListBean!=null&&otherJoinListBean.data!=null&&otherJoinListBean.data.size()>0?true:false}")})
	String virtualRefreshView;
	
	@Field(valueKey = "text",visibleWhen = "${curItemId==0}",attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "${otherChallengeListBean!=null&&otherChallengeListBean.data!=null&&otherChallengeListBean.data.size()>0?true:false}")})
	String actualRefreshView;
	
	@OnShow
	void initData() {
		if(nickName != null) {
			getAppToolbar().setTitle(nickName+"的交易", null);
		}
	}
	
	String userId;
	
	String nickName;
	
	@Bean
	int curItemId = 1;
	
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
	//@ExeGuard(title = "提示", message = "正在获取数据，请稍后...", silentPeriod = 500, cancellable = true)
	String showActualRecords(InputEvent event) {
		curItemId = 0;
		registerBean("curItemId", 0);

		// 用户自己的挑战交易记录
		if (usrService != null) {
			usrService.getMoreOtherPersonal(userId,0, otherChallengeListBean.getData().size(), false);
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
	//@ExeGuard(title = "提示", message = "正在获取数据，请稍后...", silentPeriod = 500, cancellable = true)
	String showVirtualRecords(InputEvent event) {
		
		curItemId = 1;
		registerBean("curItemId", 1);

		if (usrService != null) {
			usrService.getMoreOtherPersonal(userId,0, otherChallengeListBean.getData().size(), true);
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
			
		
		if (event.getEventType().equals("TopRefresh")) {
			showActualRecords(event);
		} else if(event.getEventType().equals("BottomRefresh")) {
			int completeSize = 0;
			if(otherChallengeListBean != null)
				completeSize = otherChallengeListBean.getData().size();
			otherHomeAStart = completeSize;
			if(usrService != null) {
				usrService.getMoreOtherPersonal(userId,otherHomeAStart, otherHomeALimit, false);
			}
		}
		return null;
	}

	

	@Command
	String handleVirtualBottomRefresh(InputEvent event) {
		
		int completeSize = 0;
		if(otherJoinListBean != null)
			completeSize = otherJoinListBean.getData().size();
		otherHomeVStart = completeSize;
		if(usrService != null) {
			usrService.getMoreOtherPersonal(userId,otherHomeVStart, otherHomeVLimit, true);
		}
		return null;
	}
	
	
	@Command
	String handleActualBottomRefresh(InputEvent event) {
		int completeSize = 0;
		if(otherChallengeListBean != null)
			completeSize = otherChallengeListBean.getData().size();
		otherHomeAStart += completeSize;
		if(usrService != null) {
			usrService.getMoreOtherPersonal(userId,otherHomeAStart, otherHomeALimit, false);
		}
		return null;
	}

	@Command
	String handleVirtualTopRefresh(InputEvent event) {
		if(event.getEventType().equals("TopRefresh")) {
			showVirtualRecords(event);
		} else if(event.getEventType().equals("BottomRefresh")) {
			int completeSize = 0;
			if(otherJoinListBean != null)
				completeSize = otherJoinListBean.getData().size();
			otherHomeVStart = completeSize;
			if(usrService != null) {
				usrService.getMoreOtherPersonal(userId,otherHomeVStart, otherHomeVLimit, true);
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
	
	@Command
	String handlerReTryClicked(InputEvent event) {
		
		if(curItemId == 0) {
			usrService.getMoreOtherPersonal(userId, 0, 20, false);
		} else if(curItemId == 1) {
			usrService.getMoreOtherPersonal(userId, 0, 20, true);
		}
		return null;
	}
}
