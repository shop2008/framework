/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

import com.wxxr.mobile.core.i10n.api.IMessageI10NService;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.ExecAsyncException;
import com.wxxr.mobile.core.ui.api.IAsyncTaskControl;
import com.wxxr.mobile.core.ui.api.IDialog;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.INavigationDescriptor;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IPageDescriptor;
import com.wxxr.mobile.core.ui.api.IProgressGuard;
import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.api.IUICommandHandler;
import com.wxxr.mobile.core.ui.api.IUICommandExecutor;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IUIContainer;
import com.wxxr.mobile.core.ui.api.IUIExceptionHandler;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewDescriptor;
import com.wxxr.mobile.core.ui.api.IWorkbenchDescriptor;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.UIConstants;
import com.wxxr.mobile.core.util.IAsyncCallback;
import com.wxxr.mobile.core.util.ICancellable;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author neillin
 *
 */
public class SimpleCommandExecutor implements IUICommandExecutor,IUIExceptionHandler {
	private static final Trace log = Trace.register(SimpleCommandExecutor.class);
	private final IWorkbenchRTContext context;
	
	public SimpleCommandExecutor(IWorkbenchRTContext ctx){
		this.context = ctx;
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IUICommandExecutor#executeCommand(com.wxxr.mobile.core.ui.api.IUICommandHandler, java.lang.Object[])
	 */
	public void executeCommand(final String cmdName,final IView view,final IUICommandHandler cmdHandler, final InputEvent event) {
		final IAsyncCallback callback = event != null ? (IAsyncCallback)event.getProperty(InputEvent.PROPERTY_CALLBACK) : null;
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
					if(callback != null){
						callback.failed(cause);
					}
					processCommandResult(view, cmdHandler, result);
				}
				
				@Override
				protected void handleDone(Object returnVal) {
					if(callback != null){
						callback.success(returnVal);
					}
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
					try {
						return cmdHandler.execute(event);
					}catch(ExecAsyncException e){
						return e.getTaskControl().getFuture().get();
					}
				}
			});
		}else{
			Object cmdResult = null;
			try {
				cmdResult = cmdHandler.execute(event);
				if(callback != null){
					callback.success(cmdResult);
				}
			}catch(ExecAsyncException e){
				final IAsyncTaskControl taskControl = e.getTaskControl();
				final ICancellable cancellable = taskControl.getCancellable();
				InvocationMonitor monitor = new InvocationMonitor(context) {
					
					@Override
					protected void handleFailed(Throwable cause) {
						log.warn("Command :"+cmdName+" executed failed", cause);
						CommandResult result = new CommandResult();
						result.setResult("failed");
						result.setPayload(cause);
						if(callback != null){
							callback.failed(cause);
						}
						taskControl.unregisterProgressMonitor(this);
						processCommandResult(view, cmdHandler, result);
					}
					
					@Override
					protected void handleDone(Object returnVal) {
						taskControl.unregisterProgressMonitor(this);
						if(callback != null){
							callback.success(returnVal);
						}
						processCommandResult(view, cmdHandler, returnVal);
					}

					/* (non-Javadoc)
					 * @see com.wxxr.mobile.core.ui.common.InvocationMonitor#handleConceled(boolean)
					 */
					@Override
					protected void handleConceled(boolean bool) {
						taskControl.unregisterProgressMonitor(this);
						if((cancellable != null)&&(!cancellable.isCancelled())){
							cancellable.cancel();
						}
						if(callback != null){
							callback.failed(null);
						}
					}
				};
				
				monitor.setCancellable(cancellable != null);
				monitor.setMessage(e.getMessage());
				monitor.setSilentPeriod(0);
				monitor.setTitle(e.getTitle());
				taskControl.registerProgressMonitor(monitor);
				return;
			}catch(Throwable t){
				log.warn("Command :"+cmdName+" executed failed", t);
				CommandResult result = new CommandResult();
				result.setResult("failed");
				result.setPayload(t);
				cmdResult = result;
				if(callback != null){
					callback.failed(t);
				}
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
		INavigationDescriptor nextNavigation = getNextNavigation(view,result, payload,cmdHandler,navs);
		if(nextNavigation != null){
			doNavigate(view, payload, nextNavigation);
		}
	}
	
	/**
	 * @param view
	 * @param payload
	 * @param nextNavigation
	 */
	protected void doNavigate(IView view, Object payload,
			INavigationDescriptor nextNavigation) {
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
			if(payload instanceof Throwable){
				IMessageI10NService i10nService = this.context.getKernelContext().getService(IMessageI10NService.class);
				if(i10nService != null){
					message = i10nService.getMessageTemplate(message);
				}
				if(message.contains("%")){
					try {
						PatternLayout layout = new PatternLayout(message);
						message = layout.format(new LoggingEvent(this.getClass().getCanonicalName(), Logger.getLogger(this.getClass()), Level.WARN, ((Throwable)payload).getMessage(), (Throwable)payload));
					} catch (Throwable e) {
					}
				}
			}
			params.put(UIConstants.MESSAGEBOX_ATTRIBUTE_MESSAGE, message);
			final IDialog dialog = context.getWorkbenchManager().getWorkbench().createDialog(UIConstants.MESSAGE_BOX_ID, params);
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
		if(!nextNavigation.keepMenuOpen()){
			IUIContainer<?> parent = view;
			while(parent != null){
				if(parent instanceof IView){
					List<IMenu> menus = ((IView)parent).getChildren(IMenu.class);
					if(menus != null){
						for (IMenu iMenu : menus) {
							if(iMenu.isOnShow()){
								iMenu.hide();
							}
						}
					}
				}
				parent = parent.getParent();
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

	public INavigationDescriptor getNextNavigation(IView view,String status,Object payload,IUICommandHandler command,INavigationDescriptor[] navigationInfos) {
		if((status == null)||(navigationInfos == null)||(navigationInfos.length == 0)){
			return null;
		}
		INavigationDescriptor nav = null;
		if(payload instanceof Throwable){
			nav = findExceptionNavigation(view, payload, navigationInfos);
			if(nav == null){
				SimpleNavigationDescriptor simnav = new SimpleNavigationDescriptor();
				simnav.setMessage("resourceId:message/default_error_message");
				simnav.setResult("Exception");
				simnav.addParameter(UIConstants.MESSAGEBOX_ATTRIBUTE_AUTO_CLOSED, 2);
				simnav.addParameter(UIConstants.MESSAGEBOX_ATTRIBUTE_TITLE, "resourceId:message/default_error_message");
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
	 * @param view
	 * @param payload
	 * @param navigationInfos
	 * @return
	 */
	protected INavigationDescriptor findExceptionNavigation(IView view,
			Object payload, INavigationDescriptor[] navigationInfos) {
		INavigationDescriptor nav = null;
		IPage page = ModelUtils.getPage(view);
		if(navigationInfos != null) {
			nav = findMatchExceptionHandler((Throwable)payload, navigationInfos);
		}
		if(nav == null){
			IViewDescriptor viewDesc = this.context.getWorkbenchManager().getViewDescriptor(view.getName());
			INavigationDescriptor[] defaultNavs = viewDesc.getExceptionNavigations();
			if((defaultNavs != null)&&(defaultNavs.length > 0)){
				nav = findMatchExceptionHandler((Throwable)payload, defaultNavs);
			}
		}
		if((nav == null)&&(view != page)){
			IPageDescriptor pageDesc =  this.context.getWorkbenchManager().getPageDescriptor(page.getName());
			INavigationDescriptor[] defaultNavs = pageDesc.getExceptionNavigations();
			if((defaultNavs != null)&&(defaultNavs.length > 0)){
				nav = findMatchExceptionHandler((Throwable)payload, defaultNavs);
			}
		}
		if(nav == null){
			IWorkbenchDescriptor wbDesc = this.context.getWorkbenchManager().getWorkbenchDescriptor();
			INavigationDescriptor[] defaultNavs = wbDesc.getExceptionNavigations();
			if((defaultNavs != null)&&(defaultNavs.length > 0)){
				nav = findMatchExceptionHandler((Throwable)payload, defaultNavs);
			}
		}
		return nav;
	}
	/**
	 * @param payload
	 * @param navigationInfos
	 * @param nav
	 * @return
	 */
	protected INavigationDescriptor findMatchExceptionHandler(Throwable payload,
			INavigationDescriptor[] navigationInfos) {
		INavigationDescriptor nav = null;
		Class<?> clazz = payload.getClass();
		String clazzName = clazz.getSimpleName();
		while(true){
			nav = findMatchNavigation(clazzName, navigationInfos);
			if(nav != null){
				break;
			}else if("Throwable".equals(clazzName)){
				break;
			}else{
				clazz = clazz.getSuperclass();
				clazzName = clazz.getSimpleName();
			}
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
	
	@Override
	public boolean handleException(IUIComponent component, Throwable t) {
		IView view = ModelUtils.getView(component);
		INavigationDescriptor nav = findExceptionNavigation(view, t, null);
		if(nav != null){
			doNavigate(view, t, nav);
			return true;
		}
		return false;
	}

}
