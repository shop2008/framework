/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.IUICommandHandler.ExecutionStep;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.core.util.ObjectUtils;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.bean.SearchStockListBean;
import com.wxxr.mobile.stock.app.bean.SearchUserListBean;
import com.wxxr.mobile.stock.app.bean.StockBaseInfoWrapper;
import com.wxxr.mobile.stock.app.bean.UserWrapper;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.biz.StockSelection;
import com.wxxr.mobile.stock.client.utils.Constants;

/**
 * 股票搜索页面
 * 
 * @author duzhen
 * 
 */
@View(name = "stockSearchPage",provideSelection = true)
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.stock_search_layout")
public abstract class StockSearchViewPage extends PageBase implements IModelUpdater {

	private static Trace log = Trace.getLogger(StockSearchViewPage.class);

	@Bean
	String key;

	@Bean
	String nickKey;
	int type = 0;
	
	/*@Menu(items = { "left" })
	private IMenu toolbar;*/

	@Field(valueKey = "text", binding = "${key}", visibleWhen="${nowSearchId==0?true:false}")
	String searchEdit;

	@Field(valueKey= "text", attributes={
			@Attribute(name = "keyBoardViewVisible", value = "${nowSearchId == 0?true:false}"),
			@Attribute(name = "keyBoardShow", value = "${keyboardShow}")
	})
	String stockSearchViewBody;
	
	@Bean(type = BindingType.Service)
	IInfoCenterManagementService infoCenterService;

	@Bean(type = BindingType.Service)
	IUserManagementService usrService;
	
	
	@Bean(type = BindingType.Pojo, express = "${infoCenterService.searchStock(key)}")
	SearchStockListBean searchListBean;

	@Bean(type = BindingType.Pojo, express = "${usrService.searchByNickName(nickKey)}")
	SearchUserListBean searchUserBean;
	
	
	/**股票搜索按钮*/
	@Field(valueKey="text", attributes = { @Attribute(name = "checked", value = "${nowSearchId == 0}")})
	String stockBtn;
	
	
	@Bean
	boolean isNullText = true;
	
	
	@Bean
	boolean isInputDone = false;
	
	@Field(valueKey="visible", binding="${nowSearchId==0?false:((searchUserBean!=null)&&(searchUserBean.searchResult!=null)&&(searchUserBean.searchResult.size()>0)?false:true)}", 
			attributes={@Attribute(name="text", value="${isNullText==true?'请输入玩家昵称':(isInputDone==true?'无搜索结果':'请输入玩家昵称')}")})
	boolean noDataBody;
	
	@Bean
	boolean keyboardShow = false;
	
	/**玩家搜索按钮*/
	@Field(valueKey="text", attributes = { @Attribute(name = "checked", value = "${nowSearchId == 1}")})
	String playerBtn;
	
	@Bean
	int nowSearchId = 0;
	
	
	
	
	
	@Field(valueKey="text", binding="${nickKey}", visibleWhen="${nowSearchId==1?true:false}")
	String nickSearchEdit;
	
/*	@Field(valueKey = "options", binding = "${searchListBean != null ? searchListBean.searchResult : null}")
	List<StockBaseInfoWrapper> searchList;*/

	@Field(valueKey = "options", binding = "${searchListBean != null ? searchListBean.searchResult : null}", visibleWhen="${nowSearchId==0}")
	List<StockBaseInfoWrapper> stockSearchList;
	
	
	@Field(valueKey = "options", binding = "${searchUserBean != null ? searchUserBean.searchResult : null}", visibleWhen="${nowSearchId==1}")
	List<UserWrapper> nickNameSearchList;
	
	/*@Command(description = "Invoke when a toolbar item was clicked", 
			uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style", visibleWhen = "${true}") })
	String toolbarClickedLeft(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("Toolbar item :left was clicked !");
		}
		hide();
		return null;
	}*/

	@OnShow
	void initStockView() {
		registerBean("key", "");
		registerBean("nickKey", "");
	}

	@OnUIDestroy
	void DestroyData() {
		registerBean("key", "");
		registerBean("nickKey", "");
		
		//registerBean("searchUserBean", null);
//		infoCenterService.searchStock(null);
	}

	@Command
	String searchTextChanged(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_TEXT_CHANGED)) {
			String key = (String) event.getProperty("changedText");
			registerBean("key", key);
			if (infoCenterService != null)
				infoCenterService.searchStock(key);
		}
		return null;
	}

	@Command
	String handleItemClick(InputEvent event) {
		if (event.getProperty("position") instanceof Integer) {
			List<StockBaseInfoWrapper> stocks = (searchListBean != null ? searchListBean
					.getSearchResult() : null);
			int position = (Integer) event.getProperty("position");
			if (stocks != null && stocks.size() > 0) {
				StockBaseInfoWrapper bean = stocks.get(position);
				String code = bean.getCode();
				String name = bean.getName();
				String market = bean.getMc();
				updateSelection(new StockSelection(market, code, name, type));
				hide();
			}
		}
		return null;
	}
	
	@Override
	public void updateModel(Object data) {
		if (data instanceof Map) {
			Map result = (Map) data;
			for (Object key : result.keySet()) {
				if ("result".equals(key) && result.get(key) instanceof Integer) {
					type = (Integer) result.get(key);
				}
			}
		}
	}
	
	@Command
	String back(InputEvent event) {
		hide();
		return null;
	}
	
	@Command
	String playerBtnClicked(InputEvent event) {
		keyboardShow = false;
		registerBean("keyboardShow", keyboardShow);
		nowSearchId = 1;
		registerBean("nowSearchId", nowSearchId);
		nickKey = "";
		registerBean("nickKey", nickKey);
		return null;
	}
	
	@Command
	String stockBtnClicked(InputEvent event) {
		keyboardShow = true;
		registerBean("keyboardShow", keyboardShow);
		
		nowSearchId = 0;
		registerBean("nowSearchId", nowSearchId);
		return null;
	}
	
	
	@Command
	String inputDoneAndSearch(ExecutionStep step, InputEvent event, Object result) {
		
		if(event.getEventType().equals("ActionDone")) {
			switch (step) {
			case PROCESS:
			
				isInputDone = true;
				registerBean("isInputDone", isInputDone);
				String textContent = (String) event.getProperty("textContent");
				
				if(StringUtils.isNotEmpty(textContent)) {
					nickKey = textContent;
					registerBean("nickKey", nickKey);
					if(usrService != null) {
						usrService.searchByNickName(nickKey);
					}
				}
				break;
				
			case NAVIGATION:
				break;
			default:
				break;
			}
			
		}
		return null;
	}
	
	@Command(navigations = { @Navigation(on = "otherUserPage", showPage = "otherUserPage"),
			@Navigation(on = "userPage", showPage = "userPage")})
	CommandResult handleNickItemClick(InputEvent event) {
		
		if (event.getProperty("position") instanceof Integer) {
			int position = (Integer) event.getProperty("position");
			List<UserWrapper> users = null;
			if(searchUserBean!=null) {
				users = searchUserBean.getSearchResult();
			}
			
			if(users != null && users.size()>0) {
				UserWrapper userWrapper = users.get(position);
				CommandResult result = new CommandResult();
				HashMap<String, Object> map = new HashMap<String, Object>();
				boolean isSelf = false;
				String userId = userWrapper.getMoblie();
				String user = AppUtils.getService(IUserIdentityManager.class)
						.getUserId();
				isSelf = ObjectUtils.isEquals(userId, user);
				map.put(Constants.KEY_USER_ID_FLAG, userId);
				map.put(Constants.KEY_USER_NAME_FLAG, userWrapper.getNickName());
				result.setPayload(map);
				if (isSelf) {
					result.setResult("userPage");
				} else {
					result.setResult("otherUserPage");
				}
				return result;
			}
			
			return null;
		}
		
		return null;
	}
	
	
	@Command
	String textIsNull(InputEvent event) {
		
		if(event.getEventType().equals("TextChanged")) {
			String s = (String)event.getProperty("changedText");
			
			
			if (StringUtils.isBlank(s)) {
				isNullText = true;
				registerBean("isNullText", isNullText);
			} else {
				isNullText = false;
				registerBean("isNullText", isNullText);
				isInputDone = false;
				registerBean("isInputDone", isInputDone);
			}
		}
		return null;
	}
}
