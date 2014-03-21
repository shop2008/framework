/**
 * 
 */
package com.wxxr.mobile.stock.app.common;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.core.async.api.Async;
import com.wxxr.mobile.core.async.api.AsyncFuture;
import com.wxxr.mobile.core.async.api.ExecAsyncException;
import com.wxxr.mobile.core.async.api.IAsyncCallable;
import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.async.api.IDataConverter;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutor;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.stock.app.StockAppBizException;

/**
 * @author neillin
 *
 */
public abstract class AsyncUtils {
	
	public static void invokeLater(Runnable task, long delay, TimeUnit unit) {
		KUtils.getService(ICommandExecutor.class).invokeLater(task, delay, unit);
	}
	
	public static void forceLoadAsyncInUI(IReloadableEntityCache<?, ?> cache, Map<String, Object> params) throws ExecAsyncException, StockAppBizException {
		AsyncFuture<Boolean> future = new AsyncFuture<Boolean>();
		cache.doReload(true, params, future.getInternalCallback());
		handleAsyncFuture(future);
	}
	
	public static <V> V forceLoadNFetchAsyncInUI(final IReloadableEntityCache<?, ?> cache, final Map<String, Object> params, final AsyncFuture<V> future, final IEntityFetcher<V> fetcher) throws ExecAsyncException, StockAppBizException {
		cache.doReload(true, params, new HybridDelegateCallback<Boolean, V>(future.getInternalCallback()) {

			@Override
			protected V getTargetValue(Boolean value) {
				return fetcher.fetchFromCache(cache);
			}
		});
		
		return handleAsyncFuture(future);
	}

	
	public static void forceLoadInSync(IReloadableEntityCache<?, ?> cache, Map<String, Object> params) throws StockAppBizException {
		AsyncFuture<Boolean> future = new AsyncFuture<Boolean>();
		cache.doReload(true, params, future.getInternalCallback());
		try {
			future.get();
		} catch (InterruptedException e) {
			throw new StockAppBizException("Execution interrupted !");
		} catch (ExecutionException e) {
			Throwable cause = e.getCause();
			if(cause instanceof StockAppBizException){
				throw (StockAppBizException)cause;
			}
			throw new StockAppBizException("系统错误",e);
		}
	}
	
	public static <S,T> T execCommandAsyncInUI(final ICommand<S> command, IDataConverter<S, T> converter) throws ExecAsyncException,StockAppBizException {
		return execCommandAsyncInUI(KUtils.getService(ICommandExecutor.class), command, converter);
	}
	
	public static <S,T> T execCommandAsyncInUI(final ICommandExecutor executor,final ICommand<S> command, IDataConverter<S, T> converter) throws ExecAsyncException,StockAppBizException {
		AsyncFuture<T> future = AsyncFuture.newAsyncFuture(new Async<S>() {

			@Override
			public void onResult(IAsyncCallback<S> callback) {
				executor.submitCommand(command,callback);
			}
		}, converter);
		return handleAsyncFuture(future);
	}

	public static <T> T execCommandAsyncInUI(ICommand<T> command) throws ExecAsyncException,StockAppBizException {
		return execCommandAsyncInUI(KUtils.getService(ICommandExecutor.class), command);
	}

	public static <T> T execCommandAsyncInUI(ICommandExecutor executor,ICommand<T> command) throws ExecAsyncException,StockAppBizException {
		AsyncFuture<T> future = new AsyncFuture<T>();
		executor.submitCommand(command, future.getInternalCallback());
		return handleAsyncFuture(future);
	}
	
	
	public static void execRunnableAsyncInUI(Runnable task) throws ExecAsyncException,StockAppBizException{
		execRunnableAsyncInUI(KUtils.getService(ICommandExecutor.class), task);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void execRunnableAsyncInUI(ICommandExecutor executor,Runnable task) throws ExecAsyncException,StockAppBizException{
		AsyncFuture<Object> future = new AsyncFuture<Object>();
		executor.submit(task, (IAsyncCallback)future.getInternalCallback());
		handleAsyncFuture(future);
	}
	
	public static <S,T> T execCallableAsyncInUI(final Callable<S> task, IDataConverter<S, T> converter) throws ExecAsyncException,StockAppBizException {
		return execCallableAsyncInUI(KUtils.getService(ICommandExecutor.class), task,converter);
	}
	
	public static <S,T> T execCallableAsyncInUI(final ICommandExecutor executor,final Callable<S> task, IDataConverter<S, T> converter) throws ExecAsyncException,StockAppBizException {
		AsyncFuture<T> future = AsyncFuture.newAsyncFuture(new Async<S>() {

			@Override
			public void onResult(IAsyncCallback<S> callback) {
				executor.submit(task,callback);
			}
		}, converter);
		return handleAsyncFuture(future);
	}

	
	public static <S,T> T execCallableAsyncInUI(final IAsyncCallable<S> task, IDataConverter<S, T> converter) throws ExecAsyncException,StockAppBizException {
		return execCallableAsyncInUI(KUtils.getService(ICommandExecutor.class), task,converter);
	}
	
	public static <S,T> T execCallableAsyncInUI(final ICommandExecutor executor,final IAsyncCallable<S> task, IDataConverter<S, T> converter) throws ExecAsyncException,StockAppBizException {
		AsyncFuture<T> future = AsyncFuture.newAsyncFuture(new Async<S>() {

			@Override
			public void onResult(IAsyncCallback<S> callback) {
				executor.submit(task,callback);
			}
		}, converter);
		return handleAsyncFuture(future);
	}

	/**
	 * @param future
	 * @return
	 */
	public static <T> T handleAsyncFuture(AsyncFuture<T> future) {
		if(KUtils.isCurrentUIThread()){
			throw new ExecAsyncException(future);
		}else{
			try {
				return future.get();
			} catch (InterruptedException e) {
				throw new StockAppBizException("Execution interrupted !");
			} catch (ExecutionException e) {
				Throwable cause = e.getCause();
				if(cause instanceof StockAppBizException){
					throw (StockAppBizException)cause;
				}
				throw new StockAppBizException("系统错误",e);
			}
		}
	}

	
	public static <T> T execCallableAsyncInUI(IAsyncCallable<T> task) throws ExecAsyncException,StockAppBizException {
		return execCallableAsyncInUI(KUtils.getService(ICommandExecutor.class), task);
	}
	
	public static <T> T execCallableAsyncInUI(ICommandExecutor executor,IAsyncCallable<T> task) throws ExecAsyncException,StockAppBizException {
		AsyncFuture<T> future = new AsyncFuture<T>();
		executor.submit(task, future.getInternalCallback());
		return handleAsyncFuture(future);
	}

}
