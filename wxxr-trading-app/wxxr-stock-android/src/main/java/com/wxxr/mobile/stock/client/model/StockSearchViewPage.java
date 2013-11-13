/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.client.bean.StockBean;
import com.wxxr.mobile.stock.client.service.IInfoCenterManagementService;

/**
 * 股票搜索页面
 * @author duzhen
 *
 */
@View(name="stockSearchPage", withToolbar=true, description="搜索股票")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY,layoutId="R.layout.stock_search_layout")
public abstract class StockSearchViewPage extends PageBase {

	private static Trace log = Trace.getLogger(StockSearchViewPage.class);
	
	@Menu(items={"left"})
	private IMenu toolbar;
	
	@Field(valueKey="text")
	String searchEdit;
	DataField<String> searchEditField;
		
	@Field(valueKey="options")
	List<StockBean> searchList;
	DataField<List> searchListField;
	
	@Command(description="Invoke when a toolbar item was clicked",
			uiItems={
				@UIItem(id="left",label="返回",icon="resourceId:drawable/back_button")
			},
			navigations={
				@Navigation(on="stockSearchPage",showPage="home")
			}
	)
	String toolbarClickedLeft(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("Toolbar item :left was clicked !");
		}
//		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return "stockSearchPage";
	}
	
	@OnShow
	void initStockView() {
		searchList = new ArrayList<StockBean>();
	}
	
	@Command
	String searchTextChanged(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_TEXT_CHANGED)) {
			String text = (String) event.getProperty("changedText");
			searchEditField.setValue(text);
			if(StringUtils.isNotEmpty(text)) {
				searchList.clear();
				List<StockBean> stock = getUIContext().getKernelContext()
						.getService(IInfoCenterManagementService.class).searchStock("");
				searchList.addAll(stock);
//				Stock s;
////				for(int i=0;i<10;i++) {
//					s = new Stock();
//					s.setName("招商地产");
//					s.setCode("000024");
//					searchList.add(s);
//					
//					s = new Stock();
//					s.setName("中山公用");
//					s.setCode("000685");
//					searchList.add(s);
//					
//					s = new Stock();
//					s.setName("中色股份");
//					s.setCode("000758");
//					searchList.add(s);
//					
//					s = new Stock();
//					s.setName("武汉中商");
//					s.setCode("000785");
//					searchList.add(s);
//					
//					s = new Stock();
//					s.setName("中水渔业");
//					s.setCode("000798");
//					searchList.add(s);
//					
//					s = new Stock();
//					s.setName("宗申动力");
//					s.setCode("001696");
//					searchList.add(s);
//				}
				searchListField.setValue(searchList);
			} else {
				searchList.clear();
				searchListField.setValue(searchList);
			}
		}
		return null;
	}
	
	@Command(navigations = { 
			@Navigation(on = "home", showPage = "userLoginPage") })
	String handleItemClick(InputEvent event) {
		return "home";
	}
}
