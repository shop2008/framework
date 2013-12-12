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
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.SearchStockListBean;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.client.biz.StockSelection;
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;

/**
 * 股票搜索页面
 * 
 * @author duzhen
 * 
 */
@View(name = "stockSearchPage", withToolbar = true, description = "搜索股票", provideSelection = true)
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.stock_search_layout")
public abstract class StockSearchViewPage extends PageBase implements IModelUpdater {

	private static Trace log = Trace.getLogger(StockSearchViewPage.class);

	@Bean
	String key;

	int type = 0;
	
	@Menu(items = { "left" })
	private IMenu toolbar;

	@Field(valueKey = "text", binding = "${key}")
	String searchEdit;

	@Bean(type = BindingType.Service)
	IInfoCenterManagementService infoCenterService;

	@Bean(type = BindingType.Pojo, express = "${infoCenterService.searchStock(key)}")
	SearchStockListBean searchListBean;

	@Field(valueKey = "options", binding = "${searchListBean != null ? searchListBean.searchResult : null}")
	List<StockBaseInfo> searchList;

	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("Toolbar item :left was clicked !");
		}
		hide();
		return null;
	}

	@OnShow
	void initStockView() {
		registerBean("key", "");
	}

	@OnUIDestroy
	void DestroyData() {
		registerBean("key", "");
		infoCenterService.searchStock(null);
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
			List<StockBaseInfo> stocks = (searchListBean != null ? searchListBean
					.getSearchResult() : null);
			int position = (Integer) event.getProperty("position");
			if (stocks != null && stocks.size() > 0) {
				StockBaseInfo bean = stocks.get(position);
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
}
