/**
 * 
 */
package com.wxxr.mobile.stock.client.ui;

import java.util.Map;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.common.AbstractToolbarView;
import com.wxxr.mobile.stock.app.IStockAppToolbar;

/**
 * @author neillin
 *
 */
public abstract class StockAppToolbar extends AbstractToolbarView implements IStockAppToolbar {
	public static final String MESSAGE_FIELD_NAME = "message";
	public static final String TITLE_FIELD_NAME = "title";
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.client.IStockAppToolbar#showNotification(java.lang.String, java.util.Map)
	 */
	@Override
	public void showNotification(String message, Map<String, String> parameters) {
		IDataField<String> field = getField(MESSAGE_FIELD_NAME);
		if(field != null){
			field.setValue(message);
			if(parameters != null){
				for (String key : parameters.keySet()) {
					AttributeKey<?> attrKey = getUIContext().getWorkbenchManager().getFieldAttributeManager().getAttributeKey(key);
					if(attrKey != null){
						attrKey.updateAttributeWithString(field, parameters.get(key));
					}
				}
			}
		}
	}
	@Override
	public void setTitle(String message, Map<String, String> parameters) {
		IDataField<String> field = getField(TITLE_FIELD_NAME);
		if(field != null){
			field.setValue(message);
			if(parameters != null){
				for (String key : parameters.keySet()) {
					AttributeKey<?> attrKey = getUIContext().getWorkbenchManager().getFieldAttributeManager().getAttributeKey(key);
					if(attrKey != null){
						attrKey.updateAttributeWithString(field, parameters.get(key));
					}
				}
			}
		}
	}
}
