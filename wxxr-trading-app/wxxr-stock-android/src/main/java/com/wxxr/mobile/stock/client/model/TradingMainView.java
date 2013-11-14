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
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.client.bean.ArticleBean;
import com.wxxr.mobile.stock.client.bean.MyArticlesBean;
import com.wxxr.mobile.stock.client.bean.TradingAccountBean;
import com.wxxr.mobile.stock.client.service.IArticleManagementService;
import com.wxxr.mobile.stock.client.service.ITradingManagementService;

/**
 * @author neillin
 *
 */
@View(name="tradingMain", description="短线放大镜")
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.home_view_layout")
public abstract class TradingMainView extends ViewBase{
	private static final Trace log = Trace.register(TradingMainView.class);
	
	
	/**获取文章*/
	@Bean(type=BindingType.Service)
	IArticleManagementService articleService;
	
	@Bean(type=BindingType.Pojo,express="${articleService.getMyArticles(0, 4, 15)}")
	MyArticlesBean myArticles;
	
	/**绑定文章*/
	@Field(valueKey="options",binding="${myArticles!=null?myArticles.homeArticles:null}")
	List<ArticleBean> articles;
	
	
	/**获取T日数据*/
	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;
	
	@Bean(type=BindingType.Pojo,express="${tradingService.getTradingAccountList(0)}")
	List<TradingAccountBean> tradingAccount;

	@Field(valueKey="options",binding="${tradingAccount!=null?tradingAccount:null}")
	List<TradingAccountBean> tradingT;
	
	
	/**获取T+1日数据*/
	
	@Bean(type=BindingType.Pojo,express="${tradingService.getTradingAccountList(1)}")
	List<TradingAccountBean> tradingAccount1;

	@Field(valueKey="options",binding="${tradingAccount1!=null?tradingAccount1:null}")
	List<TradingAccountBean> tradingT1;
	
	
	
	/**交易盘类型  0-模拟盘；1-实盘*/
	String type;
	
	@OnShow
	protected void updataArticles() {
	}

	/**
	 * 创建买入页跳转
	 * 
	 * */
	@Command
	String createBuyClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			getUIContext().getWorkbenchManager().getPageNavigator().showPage(getUIContext().getWorkbenchManager().getWorkbench().getPage("creataBuyTradePage"), null, null);
		}
		return null;
	}
	
	//点击T日列表跳转
	@Command
	String T_TradingMessageClick(InputEvent event){
		return null;
	}
	//点击T+1日列表跳转
	
	
//	@Command
//	String T1_TradingMessageClick(InputEvent event){
//		if(InputEvent.EVENT_TYPE_ITEM_CLICK.equals(event.getEventType())){
//			getUIContext().getWorkbenchManager().getPageNavigator().showPage(getUIContext().getWorkbenchManager().getWorkbench().getPage("OperationDetails"), null, null);
//		}
//		return null;
//	}
	
	@Command(
			navigations={
				@Navigation(on="operationDetails",showPage="OperationDetails",params={@Parameter(name="stockType",value="${type}")})
				})
	String tradingMessageClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_ITEM_CLICK.equals(event.getEventType())){
//			if(event.getProperty("position") instanceof Integer){
//				int position = (Integer) event.getProperty("position");
//				if(tradingT1!=null && tradingT1.size()>0){
//					TradingAccount tempTradingA = tradingT1.get(position);
//					this.type = String.valueOf(tempTradingA.getType());
//				}
//			}
			return "operationDetails";
		}
		return null;
	}
}
