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
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.PullMessageBean;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name = "userNewsPage", withToolbar = true, description = "消息", provideSelection = true)
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.user_news_center_page_layout")
public abstract class UserNewsPage extends PageBase {

	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Bean(type = BindingType.Service)
	ITradingManagementService tradingService;

	@Bean(type = BindingType.Pojo, express = "${usrService.remindMessageBean}")
	BindableListWrapper<RemindMessageBean> accountTradeListBean;

	@Bean(type = BindingType.Pojo, express = "${usrService.getPullMessageBean(start,limit)}")
	BindableListWrapper<PullMessageBean> infoNoticeListBean;

	/** 账户&交易-数据 */
	@Field(valueKey = "options", binding = "${accountTradeListBean!=null?accountTradeListBean.data:null}", visibleWhen = "${(curItemId==0)&&(accountTradeListBean.data!=null?(accountTradeListBean.data.size()>0?true:false):false)}")
	List<RemindMessageBean> accountTradeInfos;

	/** 当账户&交易数据为空时显示 */
	@Field(valueKey = "visible", binding = "${false}")
	boolean newsAccountNullVisible;

	/** 更新RadioButton的选中状态 */
	@Field(valueKey = "checked", attributes = { @Attribute(name = "checked", value = "${curItemId == 0}") })
	boolean accountTrades;

	/** 更新RadioButton的选中状态 */
	@Field(valueKey = "checked", attributes = { @Attribute(name = "checked", value = "${curItemId == 1}") })
	boolean infoNotices;

	/** 资讯&公告-数据 */
	@Field(valueKey = "options", binding = "${infoNoticeListBean!=null?infoNoticeListBean.data:null}", visibleWhen = "${(curItemId==1)&&(infoNoticeListBean.data!=null?(infoNoticeListBean.data.size()>0?true:false):false)}")
	List<PullMessageBean> noticeInfos;

	@Field(valueKey = "visible", binding = "${false}")
	boolean noticeInfosNullVisible;

	@Bean
	int curItemId = 0;

	@Bean
	int start = 0;

	@Bean
	int limit = 10;
	@Menu(items = { "left" })
	private IMenu toolbar;

	@Field(attributes = {
			@Attribute(name = "enablePullDownRefresh", value = "true"),
			@Attribute(name = "enablePullUpRefresh", value = "${infoNoticeListBean!=null&&infoNoticeListBean.data!=null&&infoNoticeListBean.data.size()>0?true:false}") })
	String noticeRefreshView;

	@Command(uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}

	@OnShow
	protected void initData() {
		usrService.readAllUnremindMessage();
	}

	@Command
	String showInfoNotices(InputEvent event) {
		curItemId = 1;
		registerBean("curItemId", curItemId);

		if (usrService != null) {
			usrService.getPullMessageBean(0, infoNoticeListBean.getData().size());
		}
		return null;
	}

	@Command
	String showAccountTrades(InputEvent event) {
		curItemId = 0;
		registerBean("curItemId", curItemId);
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			// accountTradeListBean.getData();
			if (usrService != null) {
				usrService.getRemindMessageBean();
			}
		}
		return null;
	}

	@Command
	String handleNoticeTopRefresh(InputEvent event) {
		if(event.getEventType().equals("TopRefresh")) {
			
			showInfoNotices(event);
		} else if(event.getEventType().equals("BottomRefresh")) {
			int completeLoadSize = 0;
			if (infoNoticeListBean != null)
				completeLoadSize += infoNoticeListBean.getData().size();
			start += completeLoadSize;
			if (usrService != null) {
				usrService.getPullMessageBean(start, limit);
			}
		}
		return null;
	}

	@Command
	String handleNoticeBottomRefresh(InputEvent event) {
		int completeLoadSize = 0;
		if (infoNoticeListBean != null)
			completeLoadSize += infoNoticeListBean.getData().size();
		start += completeLoadSize;
		if (usrService != null) {
			usrService.getPullMessageBean(start, limit);
		}
		return null;
	}
}
