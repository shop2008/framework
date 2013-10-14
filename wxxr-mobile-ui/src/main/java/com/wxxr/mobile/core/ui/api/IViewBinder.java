/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * 这个接口的实现类负责将View, Page绑定到具体的实现界面上，比如绑定到android界面的Activity,Fragment或Composite view上
 * @author neillin
 *
 */
public interface IViewBinder {
	IBinding<IView> createBinding(IBindingContext context,IBindingDescriptor descriptor);
}
