/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.core.ui.api.IUICommandHandler;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.CommandHandlerDecorator;

/**
 * @author neillin
 *
 */
public class MessageBoxCommandHandler extends CommandHandlerDecorator {

	private final IView msgBox;
	/**
	 * @param handler
	 */
	public MessageBoxCommandHandler(IUICommandHandler handler,IView view) {
		super(handler);
		this.msgBox = view;
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.CommandHandlerDecorator#execute(com.wxxr.mobile.core.ui.api.InputEvent)
	 */
	@Override
	public Object execute(InputEvent event) {
		this.msgBox.hide();
		return super.execute(event);
	}
	
	

}
