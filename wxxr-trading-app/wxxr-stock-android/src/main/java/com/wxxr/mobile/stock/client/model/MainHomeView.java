/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

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
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.service.IArticleManagementService;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.app.v2.bean.BaseMenuItem;

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
	
	
//	@Command(navigations={
//			@Navigation(on="operationDetails",showPage="OperationDetails",params={@Parameter(name = "add2BackStack", value = "false")}),
//			@Navigation(on="sellTradingAccount",showPage="sellTradingAccount"),
//			@Navigation(on="TBuyTradingPage",showPage="TBuyTradingPage")
//			})
//	CommandResult homeMessageClick(InputEvent event){
//		if("PinItemClick".equals(event.getEventType())){
//			CommandResult resutl = new CommandResult();
//			if(event.getProperty("position") instanceof Integer){
//				int position = (Integer) event.getProperty("position");
//				List<TradingAccInfoBean> tradingList = homeMenuList.getData();
//				if(tradingList!=null && tradingList.size()>0){
//					TradingAccInfoBean tempTradingA = tradingList.get(position);
//					String acctId = String.valueOf(tempTradingA.getAcctID());
//					boolean isVirtual = tempTradingA.getVirtual();
//					boolean isSelf = true;
//					Long accid = tempTradingA.getAcctID();
//					String over = tempTradingA.getOver();
//					int status = tempTradingA.getStatus();
//					HashMap<String, Object> map = new HashMap<String, Object>();
//					map.put(Constants.KEY_ACCOUNT_ID_FLAG, accid);
//					map.put(Constants.KEY_VIRTUAL_FLAG, isVirtual);
//					map.put(Constants.KEY_SELF_FLAG, isSelf);
//					if(status==1){
//						resutl.setPayload(map);
//						resutl.setResult("TBuyTradingPage");
//					}else if(status==0){
//						if("CLOSED".equals(over)){
//							resutl.setPayload(map);
//							resutl.setResult("operationDetails");
//						}
//						else if("UNCLOSE".equals(over)){
//							resutl.setResult("sellTradingAccount");
//							resutl.setPayload(map);
//						}
//					}
//					updateSelection(new AccidSelection(acctId, isVirtual));
//					return resutl;
//				}
//			}		
//		}
//		return null;
//	}		
}
