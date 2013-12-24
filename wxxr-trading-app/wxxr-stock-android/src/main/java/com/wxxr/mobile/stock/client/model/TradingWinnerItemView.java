/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnHide;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IReusableUIModel;
import com.wxxr.mobile.core.ui.api.ISelection;
import com.wxxr.mobile.core.ui.api.ISelectionChangedListener;
import com.wxxr.mobile.core.ui.api.ISelectionService;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.EarnRankItemBean;
import com.wxxr.mobile.stock.client.biz.AccidSelection;

/**
 * @author wangxuyang
 *
 */
@View(name="earnRankListItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.earn_money_rank_layout_item")
public abstract class TradingWinnerItemView extends ViewBase implements IModelUpdater,ISelectionChangedListener,IReusableUIModel {
	
	static Trace log = Trace.getLogger(TradingWinnerItemView.class);
	@Bean
	EarnRankItemBean earnRank;
	
	@Field(valueKey="text",binding="${earnRank!=null?earnRank.title:'--'}")
	String text;
	
	@Field(valueKey="imageURI",binding="${imgUrl!=null?imgUrl:null}",visibleWhen="${isOpen}")
	String imageUrl;
	
	@Bean
	String imgUrl;
	
	@Field(valueKey="visible",visibleWhen="${isOpen}")
	boolean isLoading = false;
	
	@Bean
	boolean isOpen = false;
	
	
	int postion = 0;
	
	private boolean flag = true;
	
	@Field(valueKey="enabled",binding="${isOpen}")
	boolean isClose;
	
	@Override
	public void updateModel(Object value) {
		if(value instanceof EarnRankItemBean){
			earnRank = (EarnRankItemBean) value;
			log.info("TradingWinnerItemView acctId= "+earnRank.getAcctId()+ "ImgUrl= " +earnRank.getImgUrl());
			registerBean("earnRank", value);
			
		}
	}
	
	@Command(navigations={@Navigation(on="operationDetails",showPage="OperationDetails")})
	CommandResult earnRankItemClick(InputEvent event){
		CommandResult result = new CommandResult();
		if(earnRank!=null){
			String accid = earnRank.getAcctId();
			if(accid!=null){
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("isVirtual", true);
				map.put("accid", accid);
				result.setPayload(map);
				result.setResult("operationDetails");
				return result;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ISelectionChangedListener#selectionChanged(java.lang.String, com.wxxr.mobile.core.ui.api.ISelection)
	 */
	@Override
	public void selectionChanged(String providerId, ISelection selection) {
		if(selection instanceof AccidSelection){
			AccidSelection selectionTemp = (AccidSelection) selection;
			if(selectionTemp!=null){
				Integer selectedPosition = selectionTemp.getPosition();
				Integer myPosition = (Integer)getProperty("_item_position");
				if(selectedPosition.equals(myPosition)){
					this.isOpen = true;
					if(earnRank!=null){
						if(earnRank.getImgUrl().endsWith(".jpg") || earnRank.getImgUrl().endsWith(".gif") || earnRank.getImgUrl().endsWith(".png")){
	                		this.imgUrl = earnRank.getImgUrl();
	                	}else{
	                		this.imgUrl = "resourceId:drawable/defimage";
	                	}
						registerBean("imgUrl", this.imgUrl);
					}
				}else{
					this.isOpen = false;
				}
				registerBean("isOpen", this.isOpen);
			}
		}
	}
	
	@OnShow
	void registerListener(){
		ISelectionService service = getUIContext().getWorkbenchManager().getWorkbench().getSelectionService();
		ISelection selection = service.getSelection("tradingWinner");
		selectionChanged("tradingWinner", selection);
		service.addSelectionListener("tradingWinner",this);
	}
	
	@OnHide
	void unregisterListener() {
		getUIContext().getWorkbenchManager().getWorkbench().getSelectionService().removeSelectionListener("tradingWinner",this);
	}

	@Override
	public void reset() {
		isOpen = false;
		registerBean("isOpen", this.isOpen);
	}
}
