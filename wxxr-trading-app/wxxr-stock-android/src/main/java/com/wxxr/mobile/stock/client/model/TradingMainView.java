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
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
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
	
	
	/**获取T日数据*/
	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;
	
	@Field(valueKey="text", attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "false")})
	String acctRefreshView;
	
	
	@Bean(type=BindingType.Pojo,express="${tradingService.getAllTradingAccountList()}",enableWhen="${idManager.userAuthenticated}")
    BindableListWrapper<TradingAccInfoBean> AllTradingAccountList;
	
	@Field(valueKey="options",binding="${AllTradingAccountList!=null?AllTradingAccountList.getData(true):null}", upateAsync=true)
	List<TradingAccInfoBean> alltrading;	   
	
	
	/**交易盘类型  0-模拟盘；1-实盘*/
	int type=0;
	/**状态 0-未结算 ； 1-已结算*/
	int status=0;
	
	
	@Command
	String handleTopRefresh(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("TradingMainView : handleTMegaTopRefresh");
		}
		this.articleService.getHomeArticles(0, 4);
		this.tradingService.getAllTradingAccountList();
		return null;
	}	
	
	@Command
	String handlerReTryClicked(InputEvent event) {
		tradingService.getAllTradingAccountList();
		return null;
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
	
	
	@Command(navigations={
			@Navigation(on="operationDetails",showPage="OperationDetails",params={@Parameter(name = "add2BackStack", value = "false")}),
			@Navigation(on="sellTradingAccount",showPage="sellTradingAccount"),
			@Navigation(on="TBuyTradingPage",showPage="TBuyTradingPage")
			})
	CommandResult tradingMessageClick(InputEvent event){
		if("PinItemClick".equals(event.getEventType())){
			CommandResult resutl = new CommandResult();
			if(event.getProperty("position") instanceof Integer){
				int position = (Integer) event.getProperty("position");
				List<TradingAccInfoBean> tradingList = AllTradingAccountList.getData();
				if(tradingList!=null && tradingList.size()>0){
					TradingAccInfoBean tempTradingA = tradingList.get(position);
					String acctId = String.valueOf(tempTradingA.getAcctID());
					boolean isVirtual = tempTradingA.getVirtual();
					boolean isSelf = true;
					Long accid = tempTradingA.getAcctID();
					String over = tempTradingA.getOver();
					int status = tempTradingA.getStatus();
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put(Constants.KEY_ACCOUNT_ID_FLAG, accid);
					map.put(Constants.KEY_VIRTUAL_FLAG, isVirtual);
					map.put(Constants.KEY_SELF_FLAG, isSelf);
					if(status==1){
						resutl.setPayload(map);
						resutl.setResult("TBuyTradingPage");
					}else if(status==0){
						if("CLOSED".equals(over)){
							resutl.setPayload(map);
							resutl.setResult("operationDetails");
						}
						else if("UNCLOSE".equals(over)){
							resutl.setResult("sellTradingAccount");
							resutl.setPayload(map);
						}
					}
					updateSelection(new AccidSelection(acctId, isVirtual));
					return resutl;
				}
			}		
		}
		return null;
	}		
}
