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
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.ArticleBean;
import com.wxxr.mobile.stock.app.bean.MyArticlesBean;
import com.wxxr.mobile.stock.app.bean.TradingAccInfoBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountListBean;
import com.wxxr.mobile.stock.app.service.IArticleManagementService;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;

/**
 * @author neillin
 *
 */
@View(name="tradingMain", description="短线放大镜",provideSelection=true)
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
	
	@Bean(type=BindingType.Pojo,express="${tradingService.getHomePageTradingAccountList()}")
	TradingAccountListBean tradingAccount;

	@Field(valueKey="options",binding="${tradingAccount!=null?tradingAccount.t0TradingAccounts:null}")
	List<TradingAccInfoBean> tradingT;
	
	@Field(valueKey="visible",visibleWhen="${tradingAccount.t0TradingAccounts!=null?true:false}")
	boolean isVisibleT;
	/**获取T+1日数据*/

	@Field(valueKey="options",binding="${tradingAccount!=null?tradingAccount.t1TradingAccounts:null}")
	List<TradingAccInfoBean> tradingT1;
	
	@Field(valueKey="visible",visibleWhen="${tradingAccount.t1TradingAccounts!=null?true:false}")
	boolean isVisibleT1;
	
	
	
	
	/**交易盘类型  0-模拟盘；1-实盘*/
	int type=0;
	/**状态 0-未结算 ； 1-已结算*/
	int status=0;
	
	@OnShow
	protected void updataArticles() {
	}

	/**
	 * 创建买入页跳转
	 * 
	 * */
	@Command(navigations={
		@Navigation(on="createBuy",showPage="creataBuyTradePage")
		})
	String createBuyClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			return "createBuy";
		}
		return null;
	} 
	
	//点击T日列表跳转
	@Command(navigations={@Navigation(on="TBuyTradingPage",showPage="TBuyTradingPage")})
	CommandResult T_TradingMessageClick(InputEvent event) {
		if (InputEvent.EVENT_TYPE_ITEM_CLICK.equals(event.getEventType())) {
			CommandResult resutl = new CommandResult();
			Long acctId = 0L;
			if (event.getProperty("position") instanceof Integer) {
				int position = (Integer) event.getProperty("position");
				if (tradingT != null && tradingT.size() > 0) {
					TradingAccInfoBean tempTradingA = tradingT.get(position);
					acctId = tempTradingA.getAcctID();
				}
			}
			resutl.setResult("TBuyTradingPage");
			resutl.setPayload(acctId);
			return resutl;
		}
		return null;
	}
	//点击T+1日列表跳转
		
	@Command(navigations={
			@Navigation(on="operationDetails",showPage="OperationDetails"),
			@Navigation(on="sellTradingAccount",showPage="sellTradingAccount")
			})
	CommandResult tradingMessageClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_ITEM_CLICK.equals(event.getEventType())){
			CommandResult resutl = new CommandResult();
			if(event.getProperty("position") instanceof Integer){
				int position = (Integer) event.getProperty("position");
				List<TradingAccInfoBean> tradingList = tradingAccount.getT1TradingAccounts();
				if(tradingList!=null && tradingList.size()>0){
					TradingAccInfoBean tempTradingA = tradingList.get(position);
					this.type = tempTradingA.getVirtual()?0:1;
					String acctId = String.valueOf(tempTradingA.getAcctID());
					//是否是模拟盘：true 模拟盘 false 实盘
					boolean isVirtual = tempTradingA.getVirtual();
					String over = tempTradingA.getOver();
					updateSelection(acctId);
					resutl.setPayload(isVirtual);
					if("CLOSED".equals(over)){
						resutl.setResult("operationDetails");
					}
					if("UNCLOSE".equals(over)){
						resutl.setResult("sellTradingAccount");
						resutl.setPayload(isVirtual);
					}
					return resutl;
				}
			}
		}
		return null;
	}
}
