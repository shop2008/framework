/**
 * 
 */
package com.wxxr.mobile.core.test;

import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.microkernel.api.IServiceDecoratorBuilder;
import com.wxxr.mobile.core.microkernel.api.IServiceDelegateHolder;
import com.wxxr.mobile.core.microkernel.api.IStatefulService;
import com.wxxr.mobile.core.session.api.ISessionManager;

/**
 * @author neillin
 *
 */
public class TestCounterModule<T extends IKernelContext> extends AbstractModule<T> implements
		TestCounter, IStatefulService {

	private int count;
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.IStatefulService#clone()
	 */
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("that's weird, it should not happen !", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.IStatefulService#destroy(java.lang.Object)
	 */
	@Override
	public void destroy(Object serviceHandler) {

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.IStatefulService#getDecoratorBuilder()
	 */
	@Override
	public IServiceDecoratorBuilder getDecoratorBuilder() {
		return new IServiceDecoratorBuilder() {
			
			@Override
			public <D> D createServiceDecorator(Class<D> clazz,
					final IServiceDelegateHolder<D> holder) {
				if(clazz == TestCounter.class){
					return clazz.cast(new TestCounter() {
						@Override
						public int getCount() {
							return ((TestCounter)holder.getDelegate()).getCount();
						}
						
						@Override
						public void countUp() {
							((TestCounter)holder.getDelegate()).countUp();
						}
					});
				}
				throw new IllegalArgumentException("cannot create decorator for interface :"+clazz);
			}
		};
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.test.TestCounter#countUp()
	 */
	@Override
	public void countUp() {
		this.count++;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.test.TestCounter#getCount()
	 */
	@Override
	public int getCount() {
		return this.count;
	}

	@Override
	protected void initServiceDependency() {
		addRequiredService(ISessionManager.class);
	}

	@Override
	protected void startService() {
		context.registerService(TestCounter.class, this);
	}

	@Override
	protected void stopService() {
		context.unregisterService(TestCounter.class, this);
	}

}
