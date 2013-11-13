/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.INavigationDescriptor;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IProgressGuard;
import com.wxxr.mobile.core.ui.api.IUICommandHandler;
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
	private static final Trace log = Trace.register(SimpleCommandExecutor.class);
	private final IWorkbenchRTContext context;
	
	public SimpleCommandExecutor(IWorkbenchRTContext ctx){
		this.context = ctx;
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IUICommandExecutor#executeCommand(com.wxxr.mobile.core.ui.api.IUICommandHandler, java.lang.Object[])
	 */
	public void executeCommand(final String cmdName,final IView view,final IUICommandHandler cmdHandler, final InputEvent event) {
		IProgressGuard guard = cmdHandler.getProgressGuard();
		if(guard != null){
			if(log.isDebugEnabled()){
				log.debug("Command :"+cmdName+" will be executed asynchronously with monitor guard :"+guard);
			}
			InvocationMonitor monitor = new InvocationMonitor(context) {
				
				@Override
				protected void handleFailed(Throwable cause) {
					log.warn("Command :"+cmdName+" executed failed", cause);
					CommandResult result = new CommandResult();
					result.setResult("failed");
					result.setPayload(cause);
					processCommandResult(view, cmdHandler, result);
				}
				
				@Override
				protected void handleDone(Object returnVal) {
					processCommandResult(view, cmdHandler, returnVal);
				}
			};
			
			monitor.setCancellable(guard.isCancellable());
			monitor.setIcon(guard.getIcon());
			monitor.setMessage(guard.getMessage());
			monitor.setSilentPeriod(guard.getSilentPeriod());
			monitor.setTitle(guard.getTitle());
			monitor.executeOnMonitor(new Callable<Object>() {			
				@Override
				public Object call() throws Exception {
					return cmdHandler.execute(event);
				}
			});
		}else{
			Object cmdResult = cmdHandler.execute(event);
			processCommandResult(view, cmdHandler, cmdResult);
		}
	}
	/**
	 * @param view
	 * @param cmdHandler
	 * @param cmdResult
	 */
	protected void processCommandResult(IView view, IUICommandHandler cmdHandler, Object cmdResult) {
		String result = null;
		Object payload = null;
		if(cmdResult instanceof String){
			result = (String)cmdResult;
		}else if(cmdResult instanceof CommandResult){
			result = ((CommandResult)cmdResult).getResult();
			payload = ((CommandResult)cmdResult).getPayload();
		}
		INavigationDescriptor[] navs = cmdHandler.getNavigations();
		INavigationDescriptor nextNavigation = getNextNavigation(result, cmdHandler,navs);
		if(nextNavigation != null){
			String toPage = StringUtils.trimToNull(nextNavigation.getToPage());
			String toView = StringUtils.trimToNull(nextNavigation.getToView());
			String message = StringUtils.trimToNull(nextNavigation.getMessage());
			if(toPage != null){
				Map<String, Object> params = getNavigationParameters(payload,nextNavigation);
				context.getWorkbenchManager().getWorkbench().showPage(toPage, params, null);
			}else if(toView != null){
				IPage page = getPage(view);
				IView v = page != null ? page.getView(toView) : null;
				if(v == null){
					v = context.getWorkbenchManager().getWorkbench().createNInitializedView(toView);
				}
				boolean add2backstack = true;
				Map<String, Object> params = getNavigationParameters(payload,nextNavigation);
				if((params != null)&&(params.size() > 0)){
					IModelUpdater updater = v.getAdaptor(IModelUpdater.class);
					if(updater != null){
						updater.updateModel(params);
					}
					String val = (String)params.get("add2BackStack");
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
	
	/**
	 * @param payload
	 * @param nextNavigation
	 * @return
	 */
	protected Map<String, Object> getNavigationParameters(Object payload,
			INavigationDescriptor nextNavigation) {
		Map<String, Object> params = nextNavigation.getParameters();
		if(payload != null){
			if((params != null)&&(params.size() > 0)){
				params = new HashMap<String, Object>(params);
			}else{
				params = new HashMap<String, Object>();
			}
			params.put("result", payload);
		}
		return params;
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
