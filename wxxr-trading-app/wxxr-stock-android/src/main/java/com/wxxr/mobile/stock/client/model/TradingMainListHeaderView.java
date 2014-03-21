/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.annotation.SecurityConstraint;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.ArticleBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.IArticleManagementService;

/**
 * @author neillin
 *
 */
@View(name="TradingMainListHeaderView")
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.home_view_list_header")
public abstract class TradingMainListHeaderView extends ViewBase{
	private static final Trace log = Trace.register(TradingMainListHeaderView.class);
	
	
	/**获取文章*/
	@Bean(type=BindingType.Service)
	IArticleManagementService articleService;
	
	@Bean(type=BindingType.Service)
	IUserIdentityManager idManager;
	
	@Bean(express="${articleService.getHomeArticles(0, 4)}")
	BindableListWrapper<ArticleBean> myArticles;
	
	/**绑定文章*/ 
	@Field(valueKey="options",binding="${myArticles.data}")
	List<ArticleBean> articles; 

	/**
	 * 创建买入页跳转
	 * 
	 * */
	@Command(navigations={
		@Navigation(on="createBuy",showPage="creataBuyTradePage"),
		@Navigation(on = "*", message = "请先登录", params = {
					@Parameter(name = "title", value = "提示"),
					@Parameter(name = "onOK", value = "leftok"),
					@Parameter(name = "onCanceled", value = "取消")})				
		})
	@SecurityConstraint(allowRoles={})
	@NetworkConstraint
	String createBuyClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			return "createBuy";
		}
		return "";
	}
	@Command(uiItems=@UIItem(id="leftok",label="确定",icon="resourceId:drawable/home"),navigations={
		@Navigation(on="*",showPage="userLoginPage")
	})
	String clearTradingAccount(InputEvent event){
		IView v = (IView)event.getProperty(InputEvent.PROPERTY_SOURCE_VIEW);
		if(v != null)
			v.hide();
		return "";
	}	
}
