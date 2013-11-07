/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.Map;

import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.INavigationDescriptor;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IUICommandHandler;
import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.api.IUICommandExecutor;
import com.wxxr.mobile.core.ui.api.IUIContainer;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author neillin
 *
 */
public class SimpleCommandExecutor implements IUICommandExecutor {
	private final IWorkbenchRTContext context;
	
	public SimpleCommandExecutor(IWorkbenchRTContext ctx){
		this.context = ctx;
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IUICommandExecutor#executeCommand(com.wxxr.mobile.core.ui.api.IUICommandHandler, java.lang.Object[])
	 */
	public void executeCommand(String cmdName,IView view,IUICommandHandler cmdHandler, InputEvent event) {
		String result = cmdHandler.execute(event);
		INavigationDescriptor[] navs = cmdHandler.getNavigations();
		INavigationDescriptor nextNavigation = getNextNavigation(result, cmdHandler,navs);
		if(nextNavigation != null){
			String toPage = StringUtils.trimToNull(nextNavigation.getToPage());
			String toView = StringUtils.trimToNull(nextNavigation.getToView());
			String message = StringUtils.trimToNull(nextNavigation.getMessage());
			if(toPage != null){
				context.getWorkbenchManager().getWorkbench().showPage(toPage, nextNavigation.getParameters(), null);
			}else if(toView != null){
				IPage page = getPage(view);
				IView v = page.getView(toView);
				boolean add2backstack = true;
				Map<String, String> params = nextNavigation.getParameters();
				if((params != null)&&(params.size() > 0)){
					IModelUpdater updater = v.getAdaptor(IModelUpdater.class);
					if(updater != null){
						updater.updateModel(params);
					}
					String val = params.get("add2BackStack");
					if("false".equalsIgnoreCase(val)){
						add2backstack = false;
					}
				}
				page.showView(toView,add2backstack);
			}else if(message != null){
				context.getWorkbenchManager().getWorkbench().showMessageBox(message, nextNavigation.getParameters());
			}
		}

	}

	protected IPage getPage(IView view) {
		IUIContainer<?> v = view ; 
		while(v != null){
			if(v instanceof IPage){
				return (IPage)v;
			}
			v = v.getParent();
		}
		return null;
	}

	public INavigationDescriptor getNextNavigation(String status,IUICommandHandler command,INavigationDescriptor[] navigationInfos) {
		if((status == null)||(navigationInfos == null)||(navigationInfos.length == 0)){
			return null;
		}
		INavigationDescriptor possibleMatch = null;
		for (INavigationDescriptor desc : navigationInfos) {
			String thisStatus = desc.getResult();
			if ((thisStatus != null) && thisStatus.equals(status)) {
				return desc;
			}
			if ((thisStatus != null)&&thisStatus.equals("*"))
				possibleMatch = desc;
		}
		if (possibleMatch != null)
			return possibleMatch;
		return null;
	}

}
