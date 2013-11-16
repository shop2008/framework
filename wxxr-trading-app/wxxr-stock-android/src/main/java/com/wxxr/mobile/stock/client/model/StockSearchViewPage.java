/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.List;

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
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.SearchStockListBean;
import com.wxxr.mobile.stock.app.bean.StockBaseInfoBean;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;

/**
 * 股票搜索页面
 * @author duzhen
 *
 */
@View(name="stockSearchPage", withToolbar=true, description="搜索股票")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY,layoutId="R.layout.stock_search_layout")
public abstract class StockSearchViewPage extends PageBase {

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
			@Navigation(on = "home", showPage = "userLoginPage", params={
				@Parameter(name="p1",value="v1"),
				@Parameter(name="p2",value="v2")})
			}
	)
	String handleItemClick(InputEvent event) {
		return "home";
	}
}
