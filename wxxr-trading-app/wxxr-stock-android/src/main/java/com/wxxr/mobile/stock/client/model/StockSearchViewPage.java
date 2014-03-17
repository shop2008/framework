/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.List;
import java.util.Map;



import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.SearchStockListBean;
import com.wxxr.mobile.stock.app.bean.StockBaseInfoWrapper;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.client.biz.StockSelection;

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

	int type = 0;
	
	/*@Menu(items = { "left" })
	private IMenu toolbar;*/

	@Field(valueKey = "text", binding = "${key}")
	String searchEdit;

	@Field(valueKey= "text", attributes={
			@Attribute(name = "hintText", value = "${nowSearchId == 0?'拼音/股票代码':'玩家昵称'}"),
			@Attribute(name = "keyBoardViewVisible", value = "${nowSearchId == 0?true:false}"),
			@Attribute(name = "keyBoardShow", value = "${keyboardShow}")
	})
	String stockSearchViewBody;
	
	@Bean(type = BindingType.Service)
	IInfoCenterManagementService infoCenterService;

	@Bean(type = BindingType.Pojo, express = "${infoCenterService.searchStock(key)}")
	SearchStockListBean searchListBean;

	/**股票搜索按钮*/
	@Field(valueKey="text", attributes = { @Attribute(name = "checked", value = "${nowSearchId == 0}")})
	String stockBtn;
	
	
	@Bean
	boolean keyboardShow = false;
	
	/**玩家搜索按钮*/
	@Field(valueKey="text", attributes = { @Attribute(name = "checked", value = "${nowSearchId == 1}")})
	String playerBtn;
	
	@Bean
	int nowSearchId = 0;
	
	
/*	@Field(valueKey = "options", binding = "${searchListBean != null ? searchListBean.searchResult : null}")
	List<StockBaseInfoWrapper> searchList;*/

	@Field(valueKey = "options", binding = "${searchListBean != null ? searchListBean.searchResult : null}", visibleWhen="${nowSearchId==0}")
	List<StockBaseInfoWrapper> stockSearchList;
	
	
	@Field(valueKey = "options", binding = "${searchListBean != null ? searchListBean.searchResult : null}", visibleWhen="${nowSearchId==1}")
	List<StockBaseInfoWrapper> nickNameSearchList;
	
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
		
	}

	@OnUIDestroy
	void DestroyData() {
		registerBean("key", "");
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
		nowSearchId = 1;
		registerBean("nowSearchId", nowSearchId);
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
}
