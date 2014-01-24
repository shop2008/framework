/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.service.IArticleManagementService;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.app.v2.bean.BaseMenuItem;
import com.wxxr.mobile.stock.app.v2.bean.ChampionShipMessageMenuItem;
import com.wxxr.mobile.stock.app.v2.bean.MessageMenuItem;
import com.wxxr.mobile.stock.app.v2.bean.SignInMessageMenuItem;
import com.wxxr.mobile.stock.app.v2.bean.TradingAccountMenuItem;
import com.wxxr.mobile.stock.client.biz.AccidSelection;
import com.wxxr.mobile.stock.client.utils.Constants;

/**
 * @author dz
 *
 */
@View(name="MainHomeView", description="短线放大镜",provideSelection=true)
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.main_home_view_layout")
public abstract class MainHomeView extends ViewBase{
	private static final Trace log = Trace.register(MainHomeView.class);
	
	@Bean(type=BindingType.Service)
	IUserIdentityManager idManager;
	
	/**获取文章*/
	@Bean(type=BindingType.Service)
	IArticleManagementService articleService;
	
	/**获取首页数据*/
	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;
	
	@Bean(type=BindingType.Pojo,express="${tradingService.getHomeMenuList()}")//,enableWhen="${idManager.userAuthenticated}")
    List<BaseMenuItem> homeMenuList;
	
	@Field(valueKey="options",binding="${homeMenuList!=null?homeMenuList:null}")//, upateAsync=true)
	List<BaseMenuItem> homeView;
	DataField<List> homeViewField;
	
	
	@Field(valueKey="text", attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "false")})
	String refreshView;
	
	@Command
	String handleRefresh(InputEvent event) {
		if("TopRefresh".equals(event.getEventType())) {
			if (log.isDebugEnabled()) {
				log.debug("MainHomeView : handleRefresh");
			}
//			this.articleService.getHomeArticles(0, 4);
//			this.tradingService.getHomeMenuList();
		}
		return null;
	}	
	
	@Command
	String handlerReTryClicked(InputEvent event) {
		homeViewField.getDomainModel().doEvaluate();
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**交易盘类型  0-模拟盘；1-实盘*/
	int type=0;
	/**状态 0-未结算 ； 1-已结算*/
	int status=0;
	
	
	@Command(navigations={
			@Navigation(on="ChampionShipPage",showPage="championShip"),
			@Navigation(on="TBuyTradingPage",showPage="TBuyTradingPage"),
			@Navigation(on="sellTradingAccount",showPage="sellTradingAccount"),
			@Navigation(on="operationDetails",showPage="OperationDetails",params={@Parameter(name = "add2BackStack", value = "false")})
			})
	CommandResult homeMessageClick(InputEvent event){
			CommandResult resutl = new CommandResult();
			if(event.getProperty("position") instanceof Integer){
				int position = (Integer) event.getProperty("position");
				List<BaseMenuItem> menuList = tradingService.getHomeMenuList();
				if(menuList!=null && menuList.size()>0){
					BaseMenuItem menu = menuList.get(position);
					if(menu instanceof ChampionShipMessageMenuItem) {
						resutl.setResult("ChampionShipPage");
					} else if(menu instanceof TradingAccountMenuItem) {
						TradingAccountMenuItem m = (TradingAccountMenuItem)menu;
						
						String acctId = m.getAcctId();
						boolean isVirtual = m.getType()!=null&&m.getType().startsWith("0")?true:false;
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put(Constants.KEY_ACCOUNT_ID_FLAG, acctId);
						map.put(Constants.KEY_SELF_FLAG, true);
						map.put(Constants.KEY_VIRTUAL_FLAG, isVirtual);
						resutl.setPayload(map);
						if("0".equals(m.getStatus())) {
							if("13".equals(m.getType())) {
								
							} else if("1d".equals(m.getType())) {
								
							} else {
								resutl.setResult("TBuyTradingPage");
							}
						} else if("1".equals(m.getStatus())) {
							if("13".equals(m.getType())) {
								
							} else if("1d".equals(m.getType())) {
								
							} else {
								resutl.setResult("sellTradingAccount");
							}
						} else if("2".equals(m.getStatus())) {
							if("13".equals(m.getType())) {
								
							} else if("1d".equals(m.getType())) {
								
							} else {
								resutl.setResult("operationDetails");
							}
						}
						updateSelection(new AccidSelection(acctId, isVirtual));
					} else if(menu instanceof SignInMessageMenuItem) {
						resutl.setResult("ChampionShipPage");
					} else if(menu instanceof MessageMenuItem) {
						resutl.setResult("ChampionShipPage");
					}
					return resutl;
				}
			}		
		return null;
	}		
}
