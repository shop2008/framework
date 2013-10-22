/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.LinkedList;

import com.wxxr.mobile.core.ui.api.IListDataProvider;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.api.InputEvent;

/**
 * @author neillin
 *
 */
public class MenuBase extends UIComponent implements IMenu {

	private LinkedList<String> commandIds = new LinkedList<String>();
	private IListDataProvider provider;
	
	public MenuBase(){
	}
	
	public MenuBase(String[] cmdIds){
		if(cmdIds != null){
			for (String id : cmdIds) {
				this.commandIds.add(id);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IUIContainer#getChild(java.lang.String)
	 */
	@Override
	public IUICommand getCommand(String name) {
		return getParent().getChild(name, IUICommand.class);
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IMenu#getCommandIds()
	 */
	@Override
	public String[] getCommandIds() {
		return this.commandIds != null ? this.commandIds.toArray(new String[this.commandIds.size()]) : new String[0];
	}
	

	@Override
	public IMenu addCommand(String cmdId) {
		if(!this.commandIds.contains(cmdId)){
			this.commandIds.add(cmdId);
		}
		return this;
	}

	@Override
	public IMenu removeCommand(String cmdId) {
		this.commandIds.remove(cmdId);
		return this;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.UIComponent#getAdaptor(java.lang.Class)
	 */
	@Override
	public <T> T getAdaptor(Class<T> clazz) {
		if(clazz == IListDataProvider.class){
			if(this.provider == null){
				this.provider = new IListDataProvider() {
					
					@Override
					public Object getItemId(Object item) {
						return (long)((IUICommand)item).getName().hashCode();
					}
					
					@Override
					public int getItemCounts() {
						return commandIds.size();
					}
					
					@Override
					public Object getItem(int i) {
						return getCommand(commandIds.get(i));
					}
				};
			}
			return clazz.cast(this.provider);
		}
		return super.getAdaptor(clazz);
	}

	protected void handleItemClick(IUICommand command,InputEvent event) {
		 event.addProperty("ItemClicked", command);
		 command.invokeCommand(null, event);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.UIComponent#invokeCommand(java.lang.String, com.wxxr.mobile.core.ui.api.InputEvent)
	 */
	@Override
	public void invokeCommand(String cmdName, InputEvent event) {
		if("handleItemClick".equals(cmdName)){
			int position = (Integer)event.getProperty("position");
			handleItemClick((IUICommand)this.provider.getItem(position),event);
		}
		super.invokeCommand(cmdName, event);
	}
}
