/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

import com.wxxr.mobile.core.async.api.AsyncFuture;
import com.wxxr.mobile.core.async.api.ExecAsyncException;
import com.wxxr.mobile.core.async.api.IAsyncCallable;
import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.async.api.ICancellable;
import com.wxxr.mobile.core.async.api.InProgressTask;
import com.wxxr.mobile.core.command.annotation.ConstraintLiteral;
import com.wxxr.mobile.core.command.api.ICommandExecutor;
import com.wxxr.mobile.core.i10n.api.IMessageI10NService;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IDialog;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.INavigationDescriptor;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IPageDescriptor;
import com.wxxr.mobile.core.ui.api.IProgressGuard;
import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.api.IUICommandExecutor;
import com.wxxr.mobile.core.ui.api.IUICommandHandler;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IUIContainer;
import com.wxxr.mobile.core.ui.api.IUIExceptionHandler;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewDescriptor;
import com.wxxr.mobile.core.ui.api.IWorkbenchDescriptor;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.UIConstants;
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
		final List<IAsyncCallback<Object>> attachCBs = event != null ? (List)event.getProperty(InputEvent.PROPERTY_ATTACH_CALLBACK) : null;
		final IAsyncCallback<Object> cb = event != null ? (IAsyncCallback)event.getProperty(InputEvent.PROPERTY_CALLBACK) : null;
		final IAsyncCallback<Object> callback;
		if(cb != null){
			callback = new IAsyncCallback<Object>() {
				
				@Override
				public void success(Object value) {
					final Object result = cmdHandler.doNavigation(event, value);
					KUtils.runOnUIThread(new Runnable() {						
						@Override
						public void run() {
							
							cb.success(result);
							if(attachCBs != null){
								for (IAsyncCallback<Object> iAsyncCallback : attachCBs) {
									iAsyncCallback.success(result);
								}
							}
						}
					});
				}
				
				@Override
				public void failed(final Throwable cause) {
					if(cause instanceof ExecAsyncException){
						ExecAsyncException ex = (ExecAsyncException)cause;
						ex.getTaskControl().onResult((IAsyncCallback)this);
					}else {
						KUtils.runOnUIThread(new Runnable() {
							
							@Override
							public void run() {
								cb.failed(cause);
								if(attachCBs != null){
									for (IAsyncCallback<Object> iAsyncCallback : attachCBs) {
										iAsyncCallback.failed(cause);
									}
								}
							}
						});
					}
				}

				@Override
				public void setCancellable(final ICancellable cancellable) {
					KUtils.runOnUIThread(new Runnable() {
						
						@Override
						public void run() {
							cb.setCancellable(cancellable);
							if(attachCBs != null){
								for (IAsyncCallback<Object> iAsyncCallback : attachCBs) {
									iAsyncCallback.setCancellable(cancellable);
								}
							}
						}
					});
				}

				@Override
				public void cancelled() {
					KUtils.runOnUIThread(new Runnable() {
						
						@Override
						public void run() {
							cb.cancelled();
							if(attachCBs != null){
								for (IAsyncCallback<Object> iAsyncCallback : attachCBs) {
									iAsyncCallback.cancelled();
								}
							}
						}
					});
				}
			};
		}else{
			callback = null;
		}
		if(callback != null){
			KUtils.getService(ICommandExecutor.class).submit(new IAsyncCallable<Object>() {

				/* (non-Javadoc)
				 * @see java.util.concurrent.Callable#call()
				 */
				@Override
				public void call(IAsyncCallback<Object> cb) {
					try {
						checkCommandConstraint(cmdHandler);
						cmdHandler.validateUserInput();
						cb.success(cmdHandler.doProcess(event));
					}catch(Throwable t){
						cb.failed(t);
					}
				}
				
			}, callback);
		}else{
			doExecute(cmdName, view, cmdHandler, event);
		}
	}
	
	/**
	 * @param cmdHandler
	 */
	protected void checkCommandConstraint(IUICommandHandler cmdHandler) {
		ConstraintLiteral[] constraints = cmdHandler.getConstraints();
		if((constraints != null)&&(constraints.length > 0)){
			ICommandExecutor service = context.getKernelContext().getService(ICommandExecutor.class);
			if(service != null){
				service.validationConstraints(constraints);
			}
		}
	}
	
	/**
	 * @param cmdName
	 * @param view
	 * @param cmdHandler
	 * @param event
	 * @param callback
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void doExecute(final String cmdName, final IView view,
			final IUICommandHandler cmdHandler, final InputEvent event) {
		final List<IAsyncCallback<Object>> attachCBs = event != null ? (List)event.getProperty(InputEvent.PROPERTY_ATTACH_CALLBACK) : null;
		IAsyncCallback<Object> attachCallback = null;
		if((attachCBs != null)&&(attachCBs.size() > 0)){
			attachCallback = new IAsyncCallback<Object>() {
				
				@Override
				public void success(final Object result) {
					KUtils.runOnUIThread(new Runnable() {
						
						@Override
						public void run() {
							if(attachCBs != null){
								for (IAsyncCallback<Object> iAsyncCallback : attachCBs) {
									iAsyncCallback.success(result);
								}
							}
						}
					});
				}
				
				@Override
				public void failed(final Throwable cause) {
					if(cause instanceof ExecAsyncException){
						ExecAsyncException ex = (ExecAsyncException)cause;
						ex.getTaskControl().onResult((IAsyncCallback)this);
					}else {
						KUtils.runOnUIThread(new Runnable() {
							
							@Override
							public void run() {
								if(attachCBs != null){
									for (IAsyncCallback<Object> iAsyncCallback : attachCBs) {
										iAsyncCallback.failed(cause);
									}
								}
							}
						});
					}
				}

				@Override
				public void setCancellable(final ICancellable cancellable) {
					KUtils.runOnUIThread(new Runnable() {
						
						@Override
						public void run() {
							if(attachCBs != null){
								for (IAsyncCallback<Object> iAsyncCallback : attachCBs) {
									iAsyncCallback.setCancellable(cancellable);
								}
							}
						}
					});
				}

				@Override
				public void cancelled() {
					KUtils.runOnUIThread(new Runnable() {
						
						@Override
						public void run() {
							if(attachCBs != null){
								for (IAsyncCallback<Object> iAsyncCallback : attachCBs) {
									iAsyncCallback.cancelled();
								}
							}
						}
					});
				}
			};
		}
		Object cmdResult = null;
		try {
			checkCommandConstraint(cmdHandler);
			cmdHandler.validateUserInput();
			cmdResult = cmdHandler.doNavigation(event,cmdHandler.doProcess(event));
			if(attachCallback != null){
				attachCallback.success(cmdResult);
			}
		}catch(ExecAsyncException e){
			final AsyncFuture<?> taskControl = e.getTaskControl();
			final ICancellable cancellable = taskControl.getCancellable();
			final InProgressTask progress = taskControl.getProgress();
			if(attachCallback != null){
				taskControl.addCallback((IAsyncCallback)attachCallback);
			}
			IProgressGuard guard = cmdHandler.getProgressGuard();
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
	
					/* (non-Javadoc)
					 * @see com.wxxr.mobile.core.ui.common.InvocationMonitor#handleConceled(boolean)
					 */
					@Override
					protected void handleConceled(boolean bool) {
						if((cancellable != null)&&(!cancellable.isCancelled())){
							cancellable.cancel();
						}
					}
				};
				if(guard != null){
					monitor.setIcon(guard.getIcon());
					monitor.setMessage(guard.getMessage());
					monitor.setSilentPeriod(guard.getSilentPeriod());
					monitor.setTitle(guard.getTitle());
				}else{
					monitor.setMessage("resourceId:message/default_async_message");
					monitor.setSilentPeriod(1);
					monitor.setTitle("resourceId:message/default_async_title");
				}
				if(progress != null){
					progress.registerProgressMonitor(monitor);
				}else{
					monitor.beginTask(-1);
					taskControl.onResult(getMonitorCallback(view, event, cmdHandler, monitor));
				}
			return;
		}catch(Throwable t){
			log.warn("Command :"+cmdName+" executed failed", t);
			CommandResult result = new CommandResult();
			result.setResult("failed");
			result.setPayload(t);
			cmdResult = result;
			if(attachCallback != null){
				attachCallback.failed(t);
			}
		}
		processCommandResult(view, cmdHandler, cmdResult);
	}
	
	@SuppressWarnings("rawtypes")
	protected IAsyncCallback getMonitorCallback(final IView view,final InputEvent event,final IUICommandHandler handler,final InvocationMonitor monitor) {
			return new IAsyncCallback<Object>() {
				
				@Override
				public void success(Object  value) {
					Object result = handler.doNavigation(event, value);
					monitor.done(result);
//					processCommandResult(view, handler, result);
				}
				
				@Override
				public void setCancellable(ICancellable cancellable) {
					monitor.setCancellable(cancellable);
				}
				
				@Override
				public void failed(Throwable cause) {
					if(cause instanceof ExecAsyncException){
						ExecAsyncException ex = (ExecAsyncException)cause;
						ex.getTaskControl().onResult((IAsyncCallback)this);
					}else {
						monitor.taskFailed(cause, null);
//						CommandResult result = new CommandResult();
//						result.setResult("failed");
//						result.setPayload(cause);
//						processCommandResult(view, handler, result);
					}
				}

				@Override
				public void cancelled() {
					monitor.taskCanceled(true);
				}
			};
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
				}else{
					params.put(UIConstants.MESSAGEBOX_ATTRIBUTE_RIGHT_BUTTON, cmdId);
				}
			}
			cmdId = (String)params.remove(UIConstants.MESSAGEBOX_ATTRIBUTE_ON_OK);
			if(cmdId != null){
				IUICommand command = view.getChild(cmdId, IUICommand.class);
				if(command != null){
					params.put(UIConstants.MESSAGEBOX_ATTRIBUTE_LEFT_BUTTON, command);
				}else{
					params.put(UIConstants.MESSAGEBOX_ATTRIBUTE_LEFT_BUTTON, cmdId);
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
			Object back = params.get(UIConstants.MESSAGEBOX_ATTRIBUTE_CANCELABLE);
			if (back instanceof String) {
				if(back.equals("false"))
					dialog.setCancelable(Boolean.parseBoolean((String) back));
				else
					dialog.setCancelable(true);
			}
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
		if((params != null)&&(params.size() > 0)){
			params = new HashMap<String, Object>(params);
		}else{
			params = new HashMap<String, Object>();
		}
		if(payload != null){
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
		if(status == null){
			return null;
		}
		INavigationDescriptor nav = null;
		if(payload instanceof Throwable){
			nav = findExceptionNavigation(view, (Throwable)payload, navigationInfos);
//			if(nav == null){
//				SimpleNavigationDescriptor simnav = new SimpleNavigationDescriptor();
//				simnav.setMessage("resourceId:message/default_error_message");
//				simnav.setResult("Exception");
//				simnav.addParameter(UIConstants.MESSAGEBOX_ATTRIBUTE_AUTO_CLOSED, 2);
//				simnav.addParameter(UIConstants.MESSAGEBOX_ATTRIBUTE_TITLE, "resourceId:message/default_error_message");
//				simnav.addParameter(UIConstants.MESSAGEBOX_ATTRIBUTE_ICON, "resourceId:image/default_error_icon");
//				simnav.addParameterObject("result", payload);
//				nav = simnav;
//			}
		}else if((navigationInfos == null)||(navigationInfos.length == 0)){
			return null;
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
			Throwable payload, INavigationDescriptor[] navigationInfos) {
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
		if((nav == null)&&(view != page)&&(page != null)){
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
