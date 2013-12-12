/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;


import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AbstractEventBinding;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;

/**
 * @author neillin
 *
 */
public class ItemClickEventBinding extends AbstractEventBinding implements OnItemClickListener {

	private AdapterView<?> control;
	
	public ItemClickEventBinding(View view,String cmdName,String field){
		this.control = (AdapterView<?>)view;
		super.setUIControl(this.control);
		super.setCommandName(cmdName);
		super.setFieldName(field);
	}

	@Override
	public void activate(IView model) {
		super.activate(model);
		this.control.setOnItemClickListener(this);
	}

	@Override
	public void deactivate() {
		this.control.setOnItemClickListener(null);
		super.deactivate();
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		SimpleInputEvent event = new SimpleInputEvent(InputEvent.EVENT_TYPE_ITEM_CLICK,getModel());
		Object list = getUIControl();
		if (list instanceof ListView) {
			ListView l = ((ListView) list);
			if(position < l.getHeaderViewsCount()) {
				return;
			} else if(position >= l.getCount() - l.getHeaderViewsCount()) {
				return;
			} else {
				position -= l.getHeaderViewsCount();
			}
		}
		event.addProperty("position", position);
		handleInputEvent(event);
	}

}
