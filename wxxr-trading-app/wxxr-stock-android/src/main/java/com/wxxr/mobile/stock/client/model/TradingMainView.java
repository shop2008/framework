/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.List;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.client.StockAppBizException;
import com.wxxr.mobile.stock.client.bean.Article;
import com.wxxr.mobile.stock.client.bean.TradingAccount;
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
	//文章
	@Field(valueKey="options")
	List<Article> articles;
	DataField<List> articlesField;
	
	
	
	//T日
	@Field(valueKey="options")
	List<TradingAccount> tradingT;
	DataField<List> tradingTField;
	
	@Field(valueKey="enabled")
	boolean isVisibleT; //是否显示
	DataField<Boolean> isVisibleTField;
	
	//T+1日
	@Field(valueKey="options")
	List<TradingAccount> tradingT1;
	DataField<List> tradingT1Field;
	
	@Field(valueKey="enabled")
	boolean isVisibleT1; //是否显示
	DataField<Boolean> isVisibleT1Field;
	
	@OnShow
	protected void updataArticles() {
		//获取文章数据
		articles = getUIContext().getKernelContext().getService(IArticleManagementService.class).getNewArticles(0, 4, 15);
		articlesField.setValue(articles);
		
		try {
			//获取T日数据
			List<TradingAccount> tempTrading = getUIContext().getKernelContext().getService(ITradingManagementService.class).getTradingAccountList(0);
			if(tempTrading!=null && tempTrading.size()>0){
				tradingT = tempTrading;
				tradingTField.setValue(tradingT);
				isVisibleT = true;
			}else{
				isVisibleT = false;
			}
			//获取T+1日数据
			List<TradingAccount> tempTrading1 = getUIContext().getKernelContext().getService(ITradingManagementService.class).getTradingAccountList(1);
			if(tempTrading1!=null && tempTrading1.size()>0){
				tradingT1 = tempTrading1;
				tradingT1Field.setValue(tradingT1);
				isVisibleT1 = true;
			}else{
				isVisibleT1 = false;
			}
		} catch (StockAppBizException e) {
			
		}
	}

	/**
	 * 创建买入页跳转
	 * 
	 * */
	@Command
	String createBuyClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			if(getUIContext()!=null){
				log.info("createBuyClick: click me");
			}
//			getUIContext().getWorkbenchManager().getPageNavigator().showPage(arg0, null, null);
		}
		return null;
	}
}
