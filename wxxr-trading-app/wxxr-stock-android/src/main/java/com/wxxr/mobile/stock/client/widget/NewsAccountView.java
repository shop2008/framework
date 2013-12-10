package com.wxxr.mobile.stock.client.widget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.wxxr.mobile.core.ui.api.IObservableListDataProvider;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.client.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class NewsAccountView extends PinnedHeaderListView {

	private PinHeaderListAdapter pinHeaderAdapter;
	private int mLocationPosition = -1;
	
	public NewsAccountView(Context context) {
		super(context);
	}

	public NewsAccountView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NewsAccountView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	/** Adapter ------- begin */
	private class PinHeaderListAdapter extends BaseAdapter implements OnScrollListener, SectionIndexer, PinnedHeaderAdapter {

		private LayoutInflater inflater;
		/**所有标签数据*/
		private List<String> labels;
		/**记录每个标签对应位址*/
		private List<Integer> labelPositions;
		
		public PinHeaderListAdapter(IObservableListDataProvider provider) {
			dataProvider = provider;
			inflater = LayoutInflater.from(getContext());
			handleData(provider);
		}
		
		private void handleData(IObservableListDataProvider provider) {
			if (provider != null && provider.getItemCounts() > 0) {
				labels = new ArrayList<String>();
				labelPositions = new ArrayList<Integer>();
				int index = 0;
				boolean isChange = false;
				RemindMessageBean curMessage = null;
				for(int i=0;i<provider.getItemCounts();i++) {
					Object object = provider.getItem(i);
					if (object instanceof RemindMessageBean) {
						RemindMessageBean messageInfoBean = (RemindMessageBean) object;
						if (i==0) {
							curMessage = messageInfoBean;
							labels.add(curMessage.getCreatedDate());
							labelPositions.add(index);
						} else {
							if (!messageInfoBean.getCreatedDate().equals(curMessage.getCreatedDate())) {
								labels.add(messageInfoBean.getCreatedDate());
								index = i;
								isChange = true;
								labelPositions.add(index);
							}
							
							if (isChange) {
								curMessage = messageInfoBean;
								isChange = false;
							}
						}
						
					}
				}
			}
		}
		
		/*private String long2StrYMD(String time) {
			long longTime = Long.parseLong(time);
			Date date = new Date(longTime);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			return format.format(date);
		}*/
		
		private String long2StrHM(String time) {
			long longTime = Long.parseLong(time);
			Date date = new Date(longTime);
			
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			return format.format(date);
		}
		
		@Override
		public int getCount() {
			if (dataProvider != null) {
				return dataProvider.getItemCounts();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			if (dataProvider != null) {
				return dataProvider.getItem(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int section = getSectionForPosition(position);
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.user_news_item_layout, null);
			}
			LinearLayout mHeaderParent = (LinearLayout) convertView
					.findViewById(R.id.header_container);
			TextView mHeaderText = (TextView) convertView
					.findViewById(R.id.date_label);
			if (getPositionForSection(section) == position) {
				mHeaderParent.setVisibility(View.VISIBLE);
				mHeaderText.setText(labels.get(section));
			} else {
				mHeaderParent.setVisibility(View.GONE);
			}
			
			Object object = dataProvider.getItem(position);
			if(object!=null && object instanceof RemindMessageBean) {
				RemindMessageBean infoBean = (RemindMessageBean) object;
				TextView news_title = (TextView) convertView.findViewById(R.id.news_title);
				TextView news_date = (TextView) convertView.findViewById(R.id.news_date);
				TextView news_content = (TextView) convertView.findViewById(R.id.news_content);
			
				news_title.setText(infoBean.getTitle());
				news_content.setText(infoBean.getContent());
				news_date.setText(long2StrHM(infoBean.getCreatedDate()));
				return convertView;
			}
			
			return null;
		}

		@Override
		public int getPinnedHeaderState(int position) {
			int realPosition = position;
			if (realPosition < 0
					|| (mLocationPosition != -1 && mLocationPosition == realPosition)) {
				return PINNED_HEADER_GONE;
			}
			mLocationPosition = -1;
			int section = getSectionForPosition(realPosition);
			int nextSectionPosition = getPositionForSection(section + 1);
			if (nextSectionPosition != -1
					&& realPosition == nextSectionPosition - 1) {
				return PINNED_HEADER_PUSHED_UP;
			}
			return PINNED_HEADER_VISIBLE;
		}

		@Override
		public void configurePinnedHeader(View header, int position, int alpha) {
			int realPosition = position;
			int section = getSectionForPosition(realPosition);
			
			if (getSections() != null && getSections().length > 0) {
				
				String title = (String) getSections()[section];
				
				if (header instanceof TextView) {
					TextView tv = (TextView) header;
					tv.setText(title);
				}
			}
		}

		@Override
		public Object[] getSections() {
			if (labels != null && labels.size() >0) {
				return labels.toArray();
			}
			return null;
		}

		@Override
		public int getPositionForSection(int section) {
			if (labels != null && labels.size()>0) {
				if (section < 0 || section >= labels.size()) {
					return -1;
				}
				
				if (labelPositions != null && labelPositions.size()>0) {
					return labelPositions.get(section);
				}
			}
			return 0;
		}

		@Override
		public int getSectionForPosition(int position) {
			if (position < 0 || position >= getCount()) {
				return -1;
			}
			
			if (labelPositions != null && labelPositions.size()>0) {
				int index = Arrays.binarySearch(labelPositions.toArray(),
						position);
				return index >= 0 ? index : -index - 2;
			}
			return -1;
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (view instanceof NewsAccountView) {
				((NewsAccountView) view).configureHeaderView(firstVisibleItem);
			}
		}
		
	}
	
	protected void updateUI() {
		
		if (pinHeaderAdapter == null) {
			pinHeaderAdapter = new PinHeaderListAdapter(this.dataProvider);
		}
		setAdapter(pinHeaderAdapter);
		setOnScrollListener(pinHeaderAdapter);
		
		setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				itemClicked(position);
			}
		});
		setPinnedHeaderView(LayoutInflater.from(getContext()).inflate(R.layout.header_layout, this, false));
	}
}
