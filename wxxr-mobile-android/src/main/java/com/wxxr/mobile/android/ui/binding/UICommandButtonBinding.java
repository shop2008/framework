/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;

import java.util.Map;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.RUtils;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;
import com.wxxr.mobile.core.ui.common.UICommand;

/**
 * @author neillin
 *
 */
public class UICommandButtonBinding extends BasicFieldBinding implements OnClickListener{
	private static final Trace log = Trace.getLogger(UICommandButtonBinding.class);
	
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
		String val = command.getAttribute(AttributeKeys.label);
		if(val != null){
			Button button = (Button)this.pComponent;
			try {
				if(RUtils.isResourceIdURI(val)){
					button.setText(RUtils.getInstance().getResourceIdByURI(val));
				}else{
					button.setText(val);
				}
			} catch (Exception e) {
				log.error("Failed to set image for field :"+field.getName(), e);
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#activate(com.wxxr.mobile.core.ui.api.IView)
	 */
	@Override
	public void activate(IView model) {
		this.pComponent.setOnClickListener(this);
		IUIComponent comp = model.getChild(getFieldName());
		if(comp instanceof UICommand){
			this.command = (UICommand)comp;
		}else{
			throw new IllegalStateException("UICommandButtonBinding must bind to a UICommand field, current field :"+getField());
		}
		super.activate(model);
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
