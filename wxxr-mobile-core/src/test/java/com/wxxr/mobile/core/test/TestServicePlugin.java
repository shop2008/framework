/**
 * 
 */
package com.wxxr.mobile.core.test;

import java.util.HashMap;
import java.util.Map;

import com.wxxr.mobile.core.microkernel.api.IKernelComponent;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.microkernel.api.IServiceFeaturePlugin;
import com.wxxr.mobile.core.microkernel.api.IServicePluginChain;

/**
 * @author neillin
 *
 */
public class TestServicePlugin implements IServiceFeaturePlugin,
		IKernelComponent {

	private IKernelContext kCtx;
	private Map<Integer, TestService3Plugin> map;
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.IKernelComponent#init(com.wxxr.mobile.core.microkernel.api.IKernelContext)
	 */
	@Override
	public void init(IKernelContext context) {
		this.kCtx = context;
		map = new HashMap<Integer, TestService3Plugin>();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.IKernelComponent#destroy()
	 */
	@Override
	public void destroy() {
		if(this.map != null){
			this.map.clear();
			this.map = null;
		}
		this.kCtx = null;

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.IServiceDecorator#decorateServiceHandler(java.lang.Class, java.lang.Object, com.wxxr.mobile.core.microkernel.api.IServiceDecoratorChain)
	 */
	@Override
	public <T> T buildServiceHandler(Class<T> serviceInterface,
			Object handler, IServicePluginChain chain) {
		if(handler instanceof TestService3){
			Integer id = System.identityHashCode(handler);
			TestService3Plugin decorator = this.map.get(id);
			if(decorator == null){
				decorator = new TestService3Plugin((TestService3)handler);
				this.map.put(id, decorator);
			}
			handler = decorator;
		}
		return chain.invokeNext(serviceInterface, handler);
	}
	
	public boolean isInitialized() {
		return this.kCtx != null;
	}

}
