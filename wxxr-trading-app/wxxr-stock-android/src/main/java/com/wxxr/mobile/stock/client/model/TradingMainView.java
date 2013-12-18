/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.annotation.SecurityConstraint;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.ArticleBean;
import com.wxxr.mobile.stock.app.bean.TradingAccInfoBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.IArticleManagementService;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.biz.AccidSelection;
import com.wxxr.mobile.stock.client.utils.Constants;

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
	
	@Bean(type=BindingType.Service)
	IUserIdentityManager idManager;
	
	@Bean(express="${articleService.getHomeArticles(0, 4)}")
	BindableListWrapper<ArticleBean> myArticles;
	
	/**绑定文章*/
	@Field(valueKey="options",binding="${myArticles.data}")
	List<ArticleBean> articles;
	
	
	/**获取T日数据*/
	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;

	@Bean(type=BindingType.Pojo,express="${tradingService.getT0TradingAccountList()}",enableWhen="${idManager.userAuthenticated}")
	BindableListWrapper<TradingAccInfoBean> t0TradingAccountList;
	
	@Field(valueKey="options",binding="${t0TradingAccountList.getData()}")
	List<TradingAccInfoBean> tradingT;
	
	@Field(valueKey="visible",visibleWhen="${t0TradingAccountList.getData()!=null&&t0TradingAccountList.getData().size()>0?true:false}")
	boolean isVisibleT;
	/**获取T+1日数据*/
	@Bean(type=BindingType.Pojo,express="${tradingService.getT1TradingAccountList()}",enableWhen="${idManager.userAuthenticated}")
    BindableListWrapper<TradingAccInfoBean> t1TradingAccountList;
	   @Field(valueKey="options",binding="${t1TradingAccountList.getData()}")
	List<TradingAccInfoBean> tradingT1;
	   
	@Field(valueKey="visible",visibleWhen="${t1TradingAccountList.getData()!=null&&t1TradingAccountList.getData().size()>0?true:false}")
	boolean isVisibleT1;
	
	@Field(attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "false")})
	String acctRefreshView;
	
	
	
	/**交易盘类型  0-模拟盘；1-实盘*/
	int type=0;
	/**状态 0-未结算 ； 1-已结算*/
	int status=0;
	
	
	@Command
	String handleTopRefresh(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("TradingMainView : handleTMegaTopRefresh");
		}
		articleService.getHomeArticles(0, 4);
		BindableListWrapper<TradingAccInfoBean> t0 = tradingService.getT0TradingAccountList();
		this.t0TradingAccountList = t0;
		BindableListWrapper<TradingAccInfoBean> t1 = tradingService.getT1TradingAccountList();
		this.t1TradingAccountList = t1;
		registerBean("t0TradingAccountList", this.t0TradingAccountList);
		registerBean("t1TradingAccountList", this.t1TradingAccountList);
		return null;
	}	
	
	@OnShow
	protected void updataArticles() {
	}

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
	
	//点击T日列表跳转
	@Command(navigations={@Navigation(on="TBuyTradingPage",showPage="TBuyTradingPage")})
	CommandResult T_TradingMessageClick(InputEvent event) {
		if (InputEvent.EVENT_TYPE_ITEM_CLICK.equals(event.getEventType())) {
			CommandResult resutl = new CommandResult();
			Long acctId = 0L;
			boolean isVirtual = true;
			boolean isSelf = true;
			if (event.getProperty("position") instanceof Integer) {
				List<TradingAccInfoBean> trading = t0TradingAccountList.getData();
				int position = (Integer) event.getProperty("position");
				if (trading != null && trading.size() > 0) {
					TradingAccInfoBean bean = trading.get(position);
					acctId = bean.getAcctID();
					isVirtual = bean.getVirtual();
				}
			}
			resutl.setResult("TBuyTradingPage");
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(Constants.KEY_ACCOUNT_ID_FLAG, acctId);
			map.put(Constants.KEY_VIRTUAL_FLAG, isVirtual);
			map.put(Constants.KEY_SELF_FLAG, isSelf);
			resutl.setPayload(map);
			return resutl;
		}
		return null;
	}
	//点击T+1日列表跳转
		
	@Command(navigations={
			@Navigation(on="operationDetails",showPage="OperationDetails",params={
					@Parameter(name = "add2BackStack", value = "false")
			}),
			@Navigation(on="sellTradingAccount",showPage="sellTradingAccount")
			})
	CommandResult tradingMessageClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_ITEM_CLICK.equals(event.getEventType())){
			CommandResult resutl = new CommandResult();
			if(event.getProperty("position") instanceof Integer){
				int position = (Integer) event.getProperty("position");
				List<TradingAccInfoBean> tradingList = t1TradingAccountList.getData();
				if(tradingList!=null && tradingList.size()>0){
					TradingAccInfoBean tempTradingA = tradingList.get(position);
					this.type = tempTradingA.getVirtual()?0:1;
					String acctId = String.valueOf(tempTradingA.getAcctID());
					//是否是模拟盘：true 模拟盘 false 实盘
					boolean isVirtual = tempTradingA.getVirtual();
					Long accid = tempTradingA.getAcctID();
					String over = tempTradingA.getOver();
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put(Constants.KEY_ACCOUNT_ID_FLAG, accid);
					map.put(Constants.KEY_VIRTUAL_FLAG, isVirtual);
					if("CLOSED".equals(over)){
						resutl.setPayload(map);
						resutl.setResult("operationDetails");
						updateSelection(new AccidSelection(acctId, isVirtual));
					}
					if("UNCLOSE".equals(over)){
						resutl.setResult("sellTradingAccount");
						resutl.setPayload(map);
					}
					return resutl;
				}
			}
		}
		return null;
	}
}
