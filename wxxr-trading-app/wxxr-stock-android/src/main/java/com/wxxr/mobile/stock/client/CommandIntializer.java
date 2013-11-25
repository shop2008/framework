/**
 * 
 */
package com.wxxr.mobile.stock.client;

import com.wxxr.mobile.core.command.api.ICommandExecutor;
import com.wxxr.mobile.core.command.common.NetworkConstaintValidator;
import com.wxxr.mobile.core.command.common.SecurityConstaintValidator;

/**
 * @author neillin
 *
 */
public abstract class CommandIntializer {
	public static void initBizCommand(ICommandExecutor cmdExecutor) {
		cmdExecutor.registerCommandValidator(new NetworkConstaintValidator());
		cmdExecutor.registerCommandValidator(new  SecurityConstaintValidator());
	}
}
