package com.wxxr.mobile.stock.client.binding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.common.AbstractEventBinding;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;
import com.wxxr.mobile.stock.client.widget.PinnedHeaderListView;

public class GroupByItemClickEventBinding extends AbstractEventBinding
		implements OnItemClickListener {

	private PinnedHeaderListView control;

	private int unItemPosition;
	public GroupByItemClickEventBinding(View view, String cmdName, String field) {
		this.control = (PinnedHeaderListView) view;
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
		
		SimpleInputEvent event = new SimpleInputEvent("PinItemClick",
				getModel());
		Object list = getUIControl();
		PinnedHeaderListView l = null;
		int titleCountBeforeCurPos = 0;

		int realPosition = 0;
		int unItemPosition = 0;
		if (list instanceof PinnedHeaderListView) {
			l = ((PinnedHeaderListView) list);
			ListAdapter adapter = l.getAdapter();
			if (adapter instanceof AbstractPinnedHeaderListAdapter) {
				AbstractPinnedHeaderListAdapter groupAdapter = (AbstractPinnedHeaderListAdapter) l
						.getAdapter();
				unItemPosition = groupAdapter.getUnItemPosition();
				// Object itemData = groupAdapter.getItem(position);
				List<Integer> titlePos = groupAdapter.getTitleSection();
				if (!titlePos.contains(position)) {
					titlePos.add(position);
				}

				Collections.sort(titlePos);

				for (int i = 0; i < titlePos.size(); i++) {
					Integer curPos = titlePos.get(i);
					if (curPos.intValue() == position) {
						titleCountBeforeCurPos += i;
						break;
					}
				}

				
				realPosition = position - titleCountBeforeCurPos;

				if(unItemPosition > 0) {
					if(realPosition > unItemPosition) {
						realPosition -= 1;
					}
				}
			} else if (adapter instanceof HeaderViewListAdapter) {
				HeaderViewListAdapter headerAdapter = (HeaderViewListAdapter) adapter;

				ListAdapter wrappedAdapter = headerAdapter.getWrappedAdapter();

				int headerCount = headerAdapter.getHeadersCount();
				if (wrappedAdapter instanceof AbstractPinnedHeaderListAdapter) {
					AbstractPinnedHeaderListAdapter abstractAdapter = (AbstractPinnedHeaderListAdapter) wrappedAdapter;
					unItemPosition = abstractAdapter.getUnItemPosition();
					List<Integer> titlePos = abstractAdapter.getTitleSection();
					List<Integer> newTitlePos = null;
					if (titlePos != null && titlePos.size() > 0) {
						newTitlePos = new ArrayList<Integer>();
						
						for(int i=0;i<titlePos.size();i++) {
							newTitlePos.add(titlePos.get(i) + headerCount);
						}
						
						if (!newTitlePos.contains(position)) {
							newTitlePos.add(position);
						}

						Collections.sort(newTitlePos);

						for (int i = 0; i < newTitlePos.size(); i++) {
							Integer curPos = newTitlePos.get(i);
							if (curPos.intValue() == position) {
								titleCountBeforeCurPos += i;
								break;
							}
						}
					}
				}
				realPosition = position - titleCountBeforeCurPos - headerCount;
				if(unItemPosition > 0) {
					if(realPosition > unItemPosition) {
						realPosition -= 1;
					}
				}
			}
			event.addProperty("position", realPosition);
			handleInputEvent(event);
		}

	}

}
