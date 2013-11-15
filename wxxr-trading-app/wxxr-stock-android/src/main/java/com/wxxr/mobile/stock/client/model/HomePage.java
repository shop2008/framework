/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.ViewGroup;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.api.IViewGroup;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

/**
 * @author neillin
 */
@View(name="home",withToolbar=true, description="短线放大镜")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY,layoutId="R.layout.home_page")
public abstract class HomePage extends PageBase {
	static Trace log;
	
	@Bean(type=BindingType.Service)
	IUserManagementService usrMgr;
	
	@Bean(type=BindingType.Pojo,express="${usrMgr.myUserInfo}")
	UserBean userInfo;
	
	@Menu(items={"home","page1","page2","page3","page4"})
	private IMenu leftMenu;
	
	@Menu(items={"left","right","search"})
	private IMenu toolbar;
	
	@ViewGroup(viewIds={"tradingMain","tradingWinner","infoCenter","championShip","helpCenter"})
	private IViewGroup contents;
	
	
	@Command(description="Invoke when a toolbar item was clicked",
			uiItems={
				@UIItem(id="left",label="左菜单",icon="resourceId:drawable/list_button")
			}
	)
	String toolbarClickedLeft(InputEvent event){
			if(log.isDebugEnabled()){
				log.debug("Toolbar item :left was clicked !");
			}
			rightMenu.hide();
			if(leftMenu.isOnShow()){
				leftMenu.hide();
			}else{
				leftMenu.show();
			}
		return null;
	}
	
	@Command(description="Invoke when a toolbar item was clicked",
			uiItems={
				@UIItem(id="right",label="右菜单",icon="resourceId:drawable/message_button")
			}
	)
	String toolbarClickedRight(InputEvent event){
			if(log.isDebugEnabled()){
				log.debug("Toolbar item :right was clicked !");
			}
			leftMenu.hide();
			if(rightMenu.isOnShow()){
				rightMenu.hide();
			}else{
				rightMenu.show();
			}
		return null;
	}
	
	@Command(description="Invoke when a toolbar item was clicked",
			uiItems={
				@UIItem(id="search",label="搜索",icon="resourceId:drawable/finds")
			}, 
	navigations={
			@Navigation(on="*",showPage="stockSearchPage")}
	)
	String toolbarClickedSearch(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("Toolbar item :search was clicked !");
		}
		return "";
	}
	
	
	@Command(description="Invoke when a menu item was clicked",commandName="doNavigation",
			uiItems={
				@UIItem(id="home",label="首页",icon="resourceId:drawable/home"),
				@UIItem(id="page1",label="赚钱榜",icon="resourceId:drawable/zpb"),
				@UIItem(id="page2",label="行情中心",icon="resourceId:drawable/hqzx"),
				@UIItem(id="page3",label="大赛排行榜",icon="resourceId:drawable/dsphb"),
				@UIItem(id="page4",label="帮助中心",icon="resourceId:drawable/help")
			},
			navigations={
				@Navigation(on="home",showView="tradingMain",params={
						@Parameter(name="p1",value="v1"),
						@Parameter(name="p2",value="v2")
				}),
				@Navigation(on="page1",showView="tradingWinner"),
				@Navigation(on="page2",showView="infoCenter"),
				@Navigation(on="page3",showView="championShip"),
				@Navigation(on="page4",showView="helpCenter")
			}
	)
	@ExeGuard(title="测试1111",message="2222 55长时间调用，进度弹出框...",silentPeriod=1,cancellable=true)
	String menuClicked(InputEvent event){
		if(InputEvent.EVENT_TYPE_ITEM_CLICK.equals(event.getEventType())){
			String name = ((IUICommand)event.getProperty("ItemClicked")).getName();
			if(log.isDebugEnabled()){
				log.debug("Menu item :"+name+" was clicked !");
			}
			if(leftMenu.isOnShow()){
				leftMenu.hide();
			}
//			String title = ((UICommand)rightMenu.getCommand(name)).getAttribute(AttributeKeys.title);
//			IStockAppToolbar tool = ((IStockAppToolbar)getAppToolbar());
//			if(tool != null){
//				tool.setTitle(title, null);
//			}
			if("page4".equals(name)){
				try {
					Thread.sleep(5000L);
				} catch (InterruptedException e) {
				}
			}
			return name;
		}
		return null;
	}
	@Menu(items={"rhome","rpage1","rpage2","rpage3","rpage4"})
	private IMenu rightMenu;
	
	@Command(description="Invoke when a menu item was clicked",commandName="doNavigationRight",
			uiItems={
				@UIItem(id="rhome",label="我的认证",icon="resourceId:drawable/rz",visibleWhen="${userInfo != null}"),
				@UIItem(id="rpage1",label="我的账户",icon="resourceId:drawable/myzh",visibleWhen="${userInfo != null}"),
				@UIItem(id="rpage2",label="交易记录",icon="resourceId:drawable/jyjl",visibleWhen="${userInfo != null}"),
				@UIItem(id="rpage3",label="设置",icon="resourceId:drawable/seting"),
				@UIItem(id="rpage4",label="版本:1.4.0",icon="resourceId:drawable/v_default")
			},
			navigations={
				@Navigation(on="rhome",showPage="userLoginPage",params={
						@Parameter(name="p1",value="v1"),
						@Parameter(name="p2",value="v2")
				}),
				@Navigation(on="rpage1",showPage="userAccountPage"),
				@Navigation(on="rpage2",showPage="userAuthPage"),
				@Navigation(on="rpage3",showPage="appSetPage"),
				@Navigation(on="rpage4",showPage="myAuthPage")
			}
	)
	String menuRightClicked(InputEvent event){
		if(InputEvent.EVENT_TYPE_ITEM_CLICK.equals(event.getEventType())){
			String name = ((IUICommand)event.getProperty("ItemClicked")).getName();
			if(log.isDebugEnabled()){
				log.debug("Menu item :"+name+" was clicked !");
			}
			if(rightMenu.isOnShow()){
				rightMenu.hide();
			}
			return name;
		}
		return null;
	}
		
}
