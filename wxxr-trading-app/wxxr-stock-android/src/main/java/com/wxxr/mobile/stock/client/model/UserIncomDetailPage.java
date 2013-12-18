package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.GainPayDetailBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

/**
 * 收支明细界面
 * @author renwenjie
 *
 */
@View(name="userIncomDetailPage", withToolbar=true, description="收支明细")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.income_detail_layout")
public abstract class UserIncomDetailPage extends PageBase {

	static Trace log;
	
	@Field(valueKey = "options", binding="${gainPayDetailListBean!=null?gainPayDetailListBean.data:null}",
			visibleWhen="${gainPayDetailListBean!=null?(gainPayDetailListBean.data!=null?(gainPayDetailListBean.data.size()>0?true:false):false):false}",
				attributes = {@Attribute(name = "enablePullDownRefresh", value="true"),
			  @Attribute(name = "enablePullUpRefresh", value="true")})
	List<GainPayDetailBean> incomeDetails;
	
	@Bean(type=BindingType.Service)
	IUserManagementService usrService;
	
	@Field(valueKey = "visible", binding = "${gainPayDetailListBean!=null?(gainPayDetailListBean.data!=null?(gainPayDetailListBean.data.size()>0?false:true):true):true}")
	boolean noDataVisible;
	
	
	@Bean(type=BindingType.Pojo, express="${usrService.getGPDetails(start,limit)}")
	BindableListWrapper<GainPayDetailBean> gainPayDetailListBean;
	
	@Menu(items = { "left" })
	private IMenu toolbar;
	
	@Bean
	int start = 0;
	
	@Bean
	int limit = 10;
	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}
	
	/**处理下拉刷新*/
	@Command
	String handleTopRefresh(InputEvent event) {
		if(log.isDebugEnabled()) {
			log.debug("UserIncomDetailPage : handleTopRefresh");
		}
		
		if(usrService != null) {
			/*start += limit;
			limit += limit;
			
			registerBean("start", start);
			registerBean("limit", limit);
			gainPayDetailListBean = usrService.getGPDetails(start, limit);*/
		}
		
		return null;
		
	}
	
	/**处理上拉刷新*/
	@Command
	String handleBottomRefresh(InputEvent event) {
		if(log.isDebugEnabled()) {
			log.debug("UserIncomDetailPage : handleBottomRefresh");
		}
		return null;
		
	}
	
	
}
