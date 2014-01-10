package com.wxxr.mobile.stock.client.binding;

import java.util.Collections;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.common.AbstractEventBinding;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;
import com.wxxr.mobile.stock.client.widget.PinnedHeaderListView;

public class GroupByItemClickEventBinding extends AbstractEventBinding
		implements OnItemClickListener {

	private PinnedHeaderListView control;
	
	public GroupByItemClickEventBinding(View view,String cmdName,String field){
		this.control = (PinnedHeaderListView)view;
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
		super.deactivate();
		this.control.setOnItemClickListener(null);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		SimpleInputEvent event = new SimpleInputEvent("PinItemClick",getModel());
		Object list = getUIControl();
		
		int titleCountBeforeCurPos = 0;
		if (list instanceof PinnedHeaderListView) {
			PinnedHeaderListView l = ((PinnedHeaderListView) list);
			ListAdapter adapter = l.getAdapter();
			if(adapter instanceof AbstractPinnedHeaderListAdapter) {
				AbstractPinnedHeaderListAdapter groupAdapter = (AbstractPinnedHeaderListAdapter)l.getAdapter();
			
				//Object itemData = groupAdapter.getItem(position);
				List<Integer> titlePos = groupAdapter.getTitleSection();
				if(!titlePos.contains(position)) {
					titlePos.add(position);
				}
				
				Collections.sort(titlePos);
				
				for(int i=0;i<titlePos.size();i++) {
					Integer curPos = titlePos.get(i);
					if(curPos.intValue() == position) {
						titleCountBeforeCurPos += i;
						break;
					}
				}
			}
			
			
			if(position < l.getHeaderViewsCount()) {
				return;
			} else if(position >= l.getCount() + l.getHeaderViewsCount()) {
				return;
			} else {
				position -= l.getHeaderViewsCount();
			}
			
			
		}
		event.addProperty("position", position - titleCountBeforeCurPos);
		handleInputEvent(event);
	}

}
