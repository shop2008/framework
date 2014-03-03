/**
 * 
 */
package com.wxxr.mobile.core.ui.api;


/**
 * @author neillin
 *
 */
public interface IDataField<T> extends IUIComponent{
	ValidationError[] getValidationErrors();
	T getValue();
	AttributeKey<T> getValueKey();
	void setValue(T val);
	/**
	 * 读取这个field绑定的界面输入，验证并更新这field的有效值。如果这个field是只读的，调用这个方法应该不做任何更新
	 */
	void updateModel();
}
