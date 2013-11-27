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
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.SearchStockListBean;
import com.wxxr.mobile.stock.app.bean.StockBaseInfoBean;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.client.widget.IStockSelectedCallBack;

/**
 * 股票搜索页面
 * @author duzhen
 *
 */
@View(name="stockSearchPage", withToolbar=true, description="搜索股票")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY,layoutId="R.layout.stock_search_layout")
public abstract class StockSearchViewPage extends PageBase implements IModelUpdater {

	private static Trace log = Trace.getLogger(StockSearchViewPage.class);

	@Bean
	String key;
	
	@Menu(items={"left"})
	private IMenu toolbar;
	
	@Field(valueKey="text")
	String searchEdit;
	DataField<String> searchEditField;
		
	@Bean(type=BindingType.Service)
	IInfoCenterManagementService infoCenterService;
	
	@Bean(type=BindingType.Pojo,express="${infoCenterService.searchStock(key)}")
	SearchStockListBean searchListBean;
	
	@Field(valueKey="options", binding="${searchListBean != null ? searchListBean.searchResult : null}")
	List<StockBaseInfoBean> searchList;

	private IStockSelectedCallBack onStockSelected;
	@Command(description="Invoke when a toolbar item was clicked",
			uiItems={
				@UIItem(id="left",label="返回",icon="resourceId:drawable/back_button")
			}
	)
	String toolbarClickedLeft(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("Toolbar item :left was clicked !");
		}
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}
	
	@OnShow
	void initStockView() {
		registerBean("key", "");
//		searchList = new ArrayList<StockBean>();
	}
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof Map) {
			Map temp = (Map) value;
			for (Object key : temp.keySet()) {
				Object tempt = temp.get(key);
				if (tempt != null && "result".equals(key)) {
					if(tempt instanceof IStockSelectedCallBack) {
						onStockSelected = (IStockSelectedCallBack)tempt;
					}
				}
			}
		}
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
	
	@Command(navigations = { 
			@Navigation(on = "SearchStockDetailPage", showPage = "SearchStockDetailPage")
			}
	)
	CommandResult handleItemClick(InputEvent event) {
		if (InputEvent.EVENT_TYPE_ITEM_CLICK.equals(event.getEventType())) {
			CommandResult result = new CommandResult();
			String code = "";
			String name = "";
			if (event.getProperty("position") instanceof Integer) {
				List<StockBaseInfoBean> stocks = (searchListBean != null ? searchListBean
						.getSearchResult() : null);
				int position = (Integer) event.getProperty("position");
				if (stocks != null && stocks.size() > 0) {
					StockBaseInfoBean bean = stocks
							.get(position);
					code = bean.getCode();
					name = bean.getName();
				}
			}
			if(onStockSelected != null) {
				onStockSelected.stockSelected(code, name);
				getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
				return null;
			}
			result.setResult("SearchStockDetailPage");
			result.setPayload(code);
			getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
			return result;
		}
		return null;
	}
	
	
}
