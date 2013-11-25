package com.wxxr.mobile.stock.client.model;

import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.ViewGroup;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IViewGroup;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;

@View(name="OperationDetails", withToolbar=true, description="模拟盘")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY,layoutId="R.layout.operation_details_page_layout")
public abstract class OpetationDetailsPage extends PageBase implements IModelUpdater{
	static Trace log = Trace.getLogger(OpetationDetailsPage.class);
	
	@Menu(items={"left","right"})
	private IMenu toolbar;
	
	@ViewGroup(viewIds={"readRecord","auditDetail","mnAuditDetail"},defaultViewId="readRecord")
	private IViewGroup contents;

	@Command(description="Invoke when a toolbar item was clicked",
			uiItems={
				@UIItem(id="left",label="返回",icon="resourceId:drawable/back_button")
			}
	)
	String toolbarClickedLeft(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("Toolbar item :left was clicked !");
		}
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}
	
	
	String tempId = "#";
	int type = 0;
	
	@Bean
	boolean isVirtual;
	
	@Bean
	String accid;
	
	Object stockId;
	
	
	@OnShow
	void showView(){
		registerBean("isVirtual", false);
		registerBean("accid", accid);
		contents.resetViewStack();
	}
	
	@Command(navigations={@Navigation(on="readRecord",showView="readRecord")})
	String readRecordClick(InputEvent event){
		return "readRecord";
	}
	
	
	
	@Command(navigations={
			@Navigation(on="Audit",showView="auditDetail"),
			@Navigation(on="mnAudit",showView="mnAuditDetail")
			})
	CommandResult tabSelectedClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			CommandResult result = new CommandResult();
			if(isVirtual){
				result.setResult("mnAudit");
			}else{
				result.setResult("Audit");
			}
			registerBean("isVirtual", this.isVirtual);
			return result;
		}
		return null;
	}
	
	@Override
	public void updateModel(Object value) {
		if(value instanceof Map){
			Map temp = (Map)value;
	        for (Object key : temp.keySet()) {
	            Object tempt = temp.get(key);
	            if("accid".equals(key)){
	            	this.accid = tempt+"";
	            	this.stockId = tempt;
	            }
	            if("isVirtual".equals(key)){
	            	this.isVirtual = (Boolean)tempt;
	            }
	        }
	        log.info("TradingMainView virtual="+isVirtual+"accid= "+accid);
	        registerBean("isVirtual", isVirtual);
	        registerBean("accid", accid);
		}
	}
	
	
//	/**
//	 * 订单详情点击
//	 * 
//	 * @param event
//	 * @return
//	 */
//	@Command(description = "Invoke when a toolbar item was clicked", 
//			uiItems = { @UIItem(id = "right", label = "交易详情", icon = "resourceId:drawable/jyjl") }, 
//			navigations = { @Navigation(on = "ecordsPage", showPage = "TradingRecordsPage") })
//	CommandResult toolbarClickedRight(InputEvent event) {
//		CommandResult resutlt = new CommandResult();
//		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
//			if (accid != null) {
//				resutlt.setResult("ecordsPage");
//				resutlt.setPayload(accid);
//			}
//		}
//		return resutlt;
//	}
	
	
	/**
	 * 订单详情点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "right", label = "交易详情", icon = "resourceId:drawable/jyjl") }, navigations = { @Navigation(on = "*", showPage = "TradingRecordsPage") })
	CommandResult toolbarClickedRight(InputEvent event) {
		CommandResult resutl = new CommandResult();
		resutl.setResult("TradingRecordsPage");
		if (stockId != null) {
			resutl.setPayload(stockId);
		}else{
			return null;
		}
		return resutl;
	}
	
}
