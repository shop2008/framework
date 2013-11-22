/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public class ExecAsyncException extends RuntimeException {
	
	private static final long serialVersionUID = 1482973930520876263L;
	
	private final IAsyncTaskControl taskControl;
	private final String title;
	
	public ExecAsyncException(IAsyncTaskControl control,String title,String message){
		super(message);
		this.taskControl = control;
		this.title = title;
	}


	/**
	 * @return the taskControl
	 */
	public IAsyncTaskControl getTaskControl() {
		return taskControl;
	}


	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	
}
