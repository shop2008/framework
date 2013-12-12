/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;


import android.view.View;
import android.view.View.OnClickListener;

import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AbstractEventBinding;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;

/**
 * @author neillin
 *
 */
public class ClickEventBinding extends AbstractEventBinding implements OnClickListener {

	
	public ClickEventBinding(View view,String cmdName,String field){
		super.setUIControl(view);
		super.setCommandName(cmdName);
		super.setFieldName(field);
	}
	

	@Override
	public void activate(IView model) {
		super.activate(model);
		((View)getUIControl()).setOnClickListener(this);
	}

	@Override
	public void deactivate() {
		((View)getUIControl()).setOnClickListener(null);
		super.deactivate();
		
	}

	@Override
	public void onClick(View v) {
		IUIComponent field = getField();
		if(field != null){
			handleInputEvent(new SimpleInputEvent(InputEvent.EVENT_TYPE_CLICK,field));
		}else{
			handleInputEvent(new SimpleInputEvent(InputEvent.EVENT_TYPE_CLICK,getModel()));
		}
	}

}
