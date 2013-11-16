/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IDialog;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.INavigationDescriptor;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IProgressGuard;
import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.api.IUICommandHandler;
import com.wxxr.mobile.core.ui.api.IUICommandExecutor;
import com.wxxr.mobile.core.ui.api.IUIContainer;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.UIConstants;
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
			Object cmdResult = null;
			try {
				cmdResult = cmdHandler.execute(event);
			}catch(Throwable t){
				log.warn("Command :"+cmdName+" executed failed", t);
				CommandResult result = new CommandResult();
				result.setResult("failed");
				result.setPayload(t);
				cmdResult = result;
			}
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
		INavigationDescriptor nextNavigation = getNextNavigation(result, payload,cmdHandler,navs);
		if(nextNavigation != null){
			String toPage = StringUtils.trimToNull(nextNavigation.getToPage());
			String toView = StringUtils.trimToNull(nextNavigation.getToView());
			String toDialog = StringUtils.trimToNull(nextNavigation.getToDialog());
			String message = StringUtils.trimToNull(nextNavigation.getMessage());
			boolean closeCurrentView = nextNavigation.getCloseCurrentView();
			Map<String, Object> params = getNavigationParameters(payload,nextNavigation);
			if(toPage != null){
				if(closeCurrentView){
					IPage page = getPage(view);
					if(page != null){
						page.hide();
					}
				}
				context.getWorkbenchManager().getWorkbench().showPage(toPage, params, null);
			}else if(toView != null){
				showView(view, toView, params,closeCurrentView);
			}else if(toDialog != null){
				context.getWorkbenchManager().getWorkbench().createDialog(toDialog, params).show();
			}else if(message != null){
				String cmdId = (String)params.remove(UIConstants.MESSAGEBOX_ATTRIBUTE_ON_CANCEL);
				if(cmdId != null){
					IUICommand command = view.getChild(cmdId, IUICommand.class);
					if(command != null){
						params.put(UIConstants.MESSAGEBOX_ATTRIBUTE_RIGHT_BUTTON, command);
					}
				}
				cmdId = (String)params.remove(UIConstants.MESSAGEBOX_ATTRIBUTE_ON_OK);
				if(cmdId != null){
					IUICommand command = view.getChild(cmdId, IUICommand.class);
					if(command != null){
						params.put(UIConstants.MESSAGEBOX_ATTRIBUTE_LEFT_BUTTON, command);
					}
				}
				final IDialog dialog = context.getWorkbenchManager().getWorkbench().createDialog(message, params);
				dialog.show();
				Object val = params.get(UIConstants.MESSAGEBOX_ATTRIBUTE_AUTO_CLOSED);
				int autoCloseInSeconds = -1;
				if(val instanceof Integer){
					autoCloseInSeconds = ((Integer)val).intValue();
				}else if(val instanceof String){
					try {
						autoCloseInSeconds = Integer.parseInt((String)val);
					}catch(NumberFormatException e){}
				}
				if(autoCloseInSeconds > 0){
					KUtils.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							dialog.dismiss();
						}
					}, autoCloseInSeconds, autoCloseInSeconds > 100 ? TimeUnit.MILLISECONDS : TimeUnit.SECONDS);
				}
			}
		}
	}
	
	
	protected void showView(IView sourceView,String toViewId,Map<String, Object> params, boolean closeCurrentView) {
		IPage page = getPage(sourceView);
		IView v = page != null ? page.getView(toViewId) : null;
		if(v == null){
			v = context.getWorkbenchManager().getWorkbench().createNInitializedView(toViewId);
		}
		boolean add2backstack = true;
//		Map<String, Object> params = getNavigationParameters(payload,nextNavigation);
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
		if(closeCurrentView&&(page != null)){
			page.hideView(sourceView.getName());
		}
		page.showView(toViewId,add2backstack);

	}
	/**
	 * @param payload
	 * @param nextNavigation
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Map<String, Object> getNavigationParameters(Object payload,
			INavigationDescriptor nextNavigation) {
		Map<String, Object> params = nextNavigation.getParameters();
		if(payload != null){
			if((params != null)&&(params.size() > 0)){
				params = new HashMap<String, Object>(params);
			}else{
				params = new HashMap<String, Object>();
			}
			if((payload instanceof Map)&&(((Map)payload).size() > 0)&&(((Map)payload).keySet().iterator().next() instanceof String)){
				params.putAll((Map<String,Object>)payload);
			}else{
				params.put("result", payload);
			}
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

	public INavigationDescriptor getNextNavigation(String status,Object payload,IUICommandHandler command,INavigationDescriptor[] navigationInfos) {
		if((status == null)||(navigationInfos == null)||(navigationInfos.length == 0)){
			return null;
		}
		INavigationDescriptor nav = null;
		if(payload instanceof Throwable){
			Class<?> clazz = payload.getClass();
			String clazzName = clazz.getSimpleName();
			while(true){
				nav = findMatchNavigation(clazzName, navigationInfos);
				if(nav != null){
					break;
				}else if("Exception".equals(clazzName)){
					break;
				}else{
					clazz = clazz.getSuperclass();
					clazzName = clazz.getSimpleName();
				}
			}
			if(nav == null){
				SimpleNavigationDescriptor simnav = new SimpleNavigationDescriptor();
				simnav.setMessage("resourceId:messages/default_error_message");
				simnav.setResult("Exception");
				simnav.addParameter(UIConstants.MESSAGEBOX_ATTRIBUTE_AUTO_CLOSED, 2);
				simnav.addParameter(UIConstants.MESSAGEBOX_ATTRIBUTE_TITLE, "resourceId:messages/default_error_message");
				simnav.addParameter(UIConstants.MESSAGEBOX_ATTRIBUTE_ICON, "resourceId:image/default_error_icon");
				simnav.addParameterObject("result", payload);
				nav = simnav;
			}
		}else{
			nav = findMatchNavigation(status, navigationInfos);
		}
		return nav;
	}
	/**
	 * @param status
	 * @param navigationInfos
	 * @return
	 */
	protected INavigationDescriptor findMatchNavigation(String status,
			INavigationDescriptor[] navigationInfos) {
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
