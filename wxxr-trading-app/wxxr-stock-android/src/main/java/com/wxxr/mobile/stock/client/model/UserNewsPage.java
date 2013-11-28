package com.wxxr.mobile.stock.client.model;

import java.util.List;



import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.MessageInfoBean;
import com.wxxr.mobile.stock.app.bean.MessageInfoListBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.binding.IRefreshCallback;

@View(name="userNewsPage", withToolbar=true, description="消息")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_news_center_page_layout")
public abstract class UserNewsPage extends PageBase {

	@Bean(type=BindingType.Service)
	IUserManagementService usrService;
	
	@Bean(type=BindingType.Pojo, express="${usrService.myMessageInfos}")
	MessageInfoListBean messageList;
	
	/**账户&交易-数据*/
	@Field(valueKey="options", binding="${messageList!=null?messageList.messageInfos:null}", visibleWhen="${(messageList!=null?messageList.messageInfos!=null?true:false:false)&&(curItemId==0)}")
	List<MessageInfoBean> accountTradeInfos;
	
	/**当账户&交易数据为空时显示*/
	@Field(valueKey="visible", binding="${(messageList!=null?messageList.messageInfos!=null?false:true:true)&&(curItemId==0)}")
	boolean newsAccountNullVisible;

	/**更新RadioButton的选中状态*/
	@Field(valueKey="checked", attributes = {
			@Attribute(name = "checked", value = "${curItemId == 0}"),
			@Attribute(name = "textColor", value = "${curItemId == 0?'resourceId:color/white':'resourceId:color/gray'}") 
			})
	boolean accountTrades;
	
	/**更新RadioButton的选中状态*/
	@Field(valueKey="checked", attributes = {
			@Attribute(name = "checked", value = "${curItemId == 1}"),
			@Attribute(name = "textColor", value = "${curItemId == 1?'resourceId:color/white':'resourceId:color/gray'}") })
	boolean infoNotices;
	
	

	/**资讯&公告-数据*/
	@Field(valueKey="options", binding="${messageList!=null?messageList.messageInfos:null}", visibleWhen="${(messageList!=null?messageList.messageInfos!=null?true:false:false)&&(curItemId==1)}")
	List<MessageInfoBean> noticeInfos;
	
	@Field(valueKey="visible", binding="${(messageList!=null?messageList.messageInfos!=null?false:true:true)&&(curItemId==1)}")
	boolean noticeInfosNullVisible;
	
	@Bean
	int curItemId = 0;
	
	@Menu(items={"left"})
	private IMenu toolbar;
	
	
	
	
	@Field(attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "false")})
	String noticeRefreshView;
	@Command(
			uiItems={
				@UIItem(id="left",label="返回",icon="resourceId:drawable/back_button")
			}
	)
	String toolbarClickedLeft(InputEvent event){
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);	
		return null;
	}
	
	
	
	@Command
	String showInfoNotices(InputEvent event) {
		curItemId = 1;
		registerBean("curItemId", curItemId);
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			if (usrService != null) {
				//usrService.getMyMessageInfos();
			}
		}
		return null;
	}
	
	@Command
	String showAccountTrades(InputEvent event) {
		curItemId = 0;
		registerBean("curItemId", curItemId);
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			if (usrService != null) {
				//usrService.getMyMessageInfos();
			}
		}
		return null;
	}
	
	
	
	@Command
	String handleNoticeTopRefresh(InputEvent event) {
		IRefreshCallback cb = (IRefreshCallback) event.getProperty("callback");
		
		if (cb != null)
			cb.refreshSuccess();
		return null;
	}
	
	@Command
	String newsItemClick(InputEvent event) {
		System.out.println("****news******"+event.getProperty("position"));
		return null;
	}
	
	@Command
	String noticeItemClick(InputEvent event) {
		System.out.println("****notice******"+event.getProperty("position"));
		return null;
	}
}
