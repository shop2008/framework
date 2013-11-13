/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;

import java.util.Map;

import android.view.View;
import android.view.View.OnClickListener;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;
import com.wxxr.mobile.core.ui.common.UICommand;

/**
 * @author neillin
 *
 */
public class UICommandButtonBinding extends BasicFieldBinding implements OnClickListener{

	private UICommand command;
	
	public UICommandButtonBinding(IAndroidBindingContext ctx, String fieldName,
			Map<String, String> attrSet) {
		super(ctx, fieldName, attrSet);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#updateUI(boolean)
	 */
	@Override
	protected void updateUI(boolean recursive) {
		super.updateUI(recursive);
		
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#activate(com.wxxr.mobile.core.ui.api.IView)
	 */
	@Override
	public void activate(IView model) {
		this.pComponent.setOnClickListener(this);
		super.activate(model);
		if(getField() instanceof UICommand){
			this.command = (UICommand)getField();
		}else{
			throw new IllegalStateException("UICommandButtonBinding must bind to a UICommand field, current field :"+getField());
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#deactivate()
	 */
	@Override
	public void deactivate() {
		this.pComponent.setOnClickListener(null);
		super.deactivate();
	}

	@Override
	public void onClick(View v) {
		SimpleInputEvent event = new SimpleInputEvent(InputEvent.EVENT_TYPE_CLICK, this.command);
		command.invokeCommand(null, event);
	}

}
