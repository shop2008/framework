/**
 * 
 */
package com.wxxr.mobile.core.test;

/**
 * @author neillin
 *
 */
public class TestService3Plugin implements TestService3 {
	private final TestService3 delegate;
	
	public TestService3Plugin(TestService3 realOne){
		this.delegate = realOne;
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.test.TestService3#helloWorld()
	 */
	@Override
	public String helloWorld() {
		return "{"+this.delegate.helloWorld()+"}";
	}

}
