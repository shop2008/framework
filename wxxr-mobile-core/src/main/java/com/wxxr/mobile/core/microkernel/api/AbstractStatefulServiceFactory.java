/**
 * 
 */
package com.wxxr.mobile.core.microkernel.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.wxxr.mobile.core.security.api.IUserIdentityManager;

/**
 * @author wangyan
 *
 */
public abstract class AbstractStatefulServiceFactory<T extends IKernelContext>  extends AbstractModule<T> implements IStatefulServiceFactory{

	private Map<String,IStatefulService> store=new HashMap<String,IStatefulService>();
	
	private IStatefulService service;
	@Override
	public IStatefulService createService() {
		boolean userAuthenticated=getContext().getService(IUserIdentityManager.class).isUserAuthenticated();
		if(userAuthenticated){
			String userId=getContext().getService(IUserIdentityManager.class).getUserId();
			IStatefulService srv=store.get(userId);
			if(srv==null){
				Collection<IStatefulService> values=store.values();
				store.clear();
				stopService(values);
				srv=createStatefulService();
				store.put(userId, srv);
				srv.init(getContext());
				srv.startService();
			}
			return srv;
		}else{
			if(service==null){
				service=createStatefulService();
				service.init(getContext());
				service.startService();
			}
			return service;
		}
	}

	protected void stopService(final Collection<IStatefulService> services) {
		getContext().getExecutor().execute(new Runnable() {
			@Override
			public void run() {
				if(services!=null){
					for(IStatefulService s:services){
						s.stopService();
					}
				}
				
			}
		});
	}

	protected abstract IKernelContext getContext();

	protected abstract IStatefulService createStatefulService();
}
