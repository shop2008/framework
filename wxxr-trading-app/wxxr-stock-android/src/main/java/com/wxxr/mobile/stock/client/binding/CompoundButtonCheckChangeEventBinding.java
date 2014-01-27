package com.wxxr.mobile.stock.client.binding;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AbstractEventBinding;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;

public class CompoundButtonCheckChangeEventBinding extends AbstractEventBinding{
private static Trace log = Trace.getLogger(TextChangedEventBinding.class);
	
	private CompoundButton compoundButton;
	
	public CompoundButtonCheckChangeEventBinding(View view,String cmdName,String field) {
		this.compoundButton = (CompoundButton)view; 
		setUIControl(this.compoundButton);
		setCommandName(cmdName);
		setFieldName(field);
	}
	
	private OnCheckedChangeListener checkChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			SimpleInputEvent event = new SimpleInputEvent("CheckChanged",
					getField());
			event.addProperty("isChecked", ""+isChecked);
			if(log.isDebugEnabled()) {
				log.debug("push message enabled "+isChecked);
			}
			handleInputEvent(event);
		}
	};

	@Override
	public void activate(IView model) {
		super.activate(model);
		this.compoundButton.setOnCheckedChangeListener(checkChangeListener);
	}

	@Override
	public void deactivate() {
		this.compoundButton.setOnCheckedChangeListener(null);
		super.deactivate();
	}


	@Override
	public void destroy() {
		deactivate();
		this.compoundButton = null;
	}
}
