package com.wxxr.mobile.stock.client.model;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.bean.PersonalHomePageBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.binding.IRefreshCallback;

@View(name="userSucTradePage", withToolbar=true, description="我的成功操作")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_suc_trade_layout")
public abstract class UserSucTradePage extends PageBase implements IModelUpdater{

	@Bean(type = BindingType.Service)
	IUserManagementService userService;

	@Bean(type = BindingType.Pojo, express = "${userService!=null?userService.getMorePersonalRecords(0,3,false)}")
	PersonalHomePageBean actualHomeBean;
	
	@Bean(type = BindingType.Pojo, express = "${userService!=null?userService.getMorePersonalRecords(0,3,true)}")
	PersonalHomePageBean virtualHomeBean;
	
	@Field(valueKey = "options", binding = "${actualHomeBean!=null?actualHomeBean.actualList:null}")
	List<GainBean> actualRecordList;
	
	@Field(valueKey = "options", binding = "${virtualHomeBean!=null?virtualHomeBean.virtualList:null}")
	List<GainBean> virtualRecordsList;
	

	
	
	@Field(valueKey = "checked",  attributes={
			@Attribute(name = "checked", value = "${curItemId == 0}"),
			@Attribute(name = "textColor", value = "${curItemId == 0?'resourceId:color/white':'resourceId:color/gray'}")
			})
	boolean actualRecordBtn;

	@Field(valueKey = "checked", attributes={
			@Attribute(name = "checked", value = "${curItemId == 1}"),
			@Attribute(name = "textColor", value = "${curItemId == 1?'resourceId:color/white':'resourceId:color/gray'}")
			})
	boolean virtualRecordBtn;


	@Bean
	int curItemId = 0;
		
	
	@SuppressWarnings("unused")
	@Menu(items={"left","right"})
	private IMenu toolbar;
	
	@Command(
			uiItems={
				@UIItem(id="left",label="返回",icon="resourceId:drawable/back_button")
			}
	)
	String toolbarClickedLeft(InputEvent event){
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);	
		return null;
	}


	/**
	 * 显示所有成功交易记录
	 * 
	 * @param event
	 * @return
	 */
	String showActualRecords(InputEvent event) {
		curItemId = 0;
		registerBean("curItemId", curItemId);
		if (actualHomeBean != null)
			actualHomeBean.getActualList();
		return null;
	}

	/**
	 * 显示所有交易记录
	 * 
	 * @param event
	 * @return
	 */
	String showVirtualRecords(InputEvent event) {

		curItemId = 1;
		registerBean("curItemId", curItemId);
		if (virtualHomeBean != null)
			virtualHomeBean.getVirtualList();
		return null;
	}

	@Command(commandName = "virtualRecordItemClicked")
	String virtualRecordItemClicked(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_ITEM_CLICK)) {
			int position = (Integer) event.getProperty("position");
			if (virtualRecordsList != null && virtualRecordsList.size() > 0) {
				GainBean bean = virtualRecordsList.get(position);
				System.out.println("-----------"+bean.getMaxStockName());
			}

		}
		return null;
	}

	@Command(commandName = "actualRecordItemClicked")
	String actualRecordItemClicked(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_ITEM_CLICK)) {
			int position = (Integer) event.getProperty("position");
			if (actualRecordList != null && actualRecordList.size() > 0) {
				GainBean bean = actualRecordList.get(position);
				System.out.println("-----" + bean.getMaxStockName());
			}
		}
		return null;
	}

	@Command
	String handleActualTopRefresh(InputEvent event) {
		IRefreshCallback cb = (IRefreshCallback) event.getProperty("callback");
		showActualRecords(null);
		if (cb != null)
			cb.refreshSuccess();
		return null;
	}

	@Command
	String handleAllTopRefresh(InputEvent event) {
		IRefreshCallback cb = (IRefreshCallback) event.getProperty("callback");
		showVirtualRecords(null);
		if (cb != null)
			cb.refreshSuccess();
		return null;
	}
	
	@Override
	public void updateModel(Object value) {
		Map<String, String> map = (Map<String, String>)value;
		String isVirtual = map.get("isVirtual");
		int curItemId  = Integer.parseInt(isVirtual);
		registerBean("curItemId", curItemId);
	}

}
