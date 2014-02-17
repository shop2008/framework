package com.wxxr.mobile.stock.client.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.IDataChangedListener;
import com.wxxr.mobile.core.ui.api.IObservableListDataProvider;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.client.R;
import com.wxxr.moblie.stock.client.dslv.DragSortListView;

public class NewDragSortListView extends DragSortListView implements IDataChangedListener, android.widget.AdapterView.OnItemClickListener{

	final Trace log = Trace.getLogger(NewDragSortListView.class);
	private IObservableListDataProvider dataProvider;
	private ArrayList<StockInfoBean> stockInfo = null;
	private DragSortListAdapter adapter = null;
	private Context context;
	public NewDragSortListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.setOnItemClickListener(this);
	}
	public IObservableListDataProvider getDataProvider() {
		return dataProvider;
	}
	public void setDataProvider(IObservableListDataProvider dataProvider) {
		IObservableListDataProvider oldProv = this.dataProvider;
		this.dataProvider = dataProvider;
		if(this.dataProvider != null){
			this.dataProvider.registerDataChangedListener(this);
		}else if(oldProv != null){
			oldProv.unregisterDataChangedListener(this);
		}
	}	
	
	
	//交换数据
	public void MoveAdapterData(int from, int to){
		if(from == to)
			return;
		if(stockInfo!=null && stockInfo.size()>0){
			StockInfoBean item = stockInfo.get(from);
			stockInfo.remove(item);
			stockInfo.add(to, item);
			adapter.setData(stockInfo);
			adapter.notifyDataSetChanged();
			this.moveCheckState(from, to);
		}
	}
	
	public StockInfoBean getRemoveData(int postion){
		if(stockInfo!=null && stockInfo.size()>0){
			StockInfoBean removeData = stockInfo.get(postion);
			if(removeData!=null){
				return removeData;
			}
		}
		return null;
	}
	
	//删除数据
	public void RemoveData(int which){
//		initData();
		if(stockInfo!=null && stockInfo.size()>0){
			StockInfoBean item = stockInfo.get(which);
			stockInfo.remove(item);
			log.info("NewDragSortListView removeData Count = "+ stockInfo.size() +"ItemData= " +item.getCode() +"  which=  "+which);
			adapter.setData(stockInfo);
			adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void dataSetChanged() {
		initData();
	}

	@Override
	public void dataItemChanged() {
		initData();
	}
	
	//初始化数据
	private void initData(){
		if(dataProvider == null)
			return;
		StockInfoBean info = null;
		stockInfo = new ArrayList<StockInfoBean>();
		int size = dataProvider.getItemCounts();
		for(int i=0; i<size; i++){
			StockQuotationBean stockBean = (StockQuotationBean) dataProvider.getItem(i);
			if(stockBean!=null){
				info = new StockInfoBean(stockBean.getCode(), stockBean.getMarket(), stockBean.getStockName());
				stockInfo.add(info);
			}
		}
		if(stockInfo!=null && stockInfo.size()>0){
			adapter = new DragSortListAdapter(context, stockInfo); 
			setAdapter(adapter);
		}
		this.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}
	
	public class DragSortListAdapter extends ArrayAdapter<StockInfoBean>{
		private List<StockInfoBean> datas;
		private Context context;
		public DragSortListAdapter(Context context, List<StockInfoBean> data) {
			super(context, 0);
			this.context = context;
			this.datas = data;
		}
		public void setData(List<StockInfoBean> data){
			this.datas = data;
		}
		@Override
		public StockInfoBean getItem(int position) {
			return datas.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		public void setSelect(int position){
			if(!datas.get(position).isSelected()){
				datas.get(position).setSelected(true);
				for(int i=0; i<datas.size();i++){
					if(i != position){
						datas.get(i).setSelected(false);
					}
				}
			}
			notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
			return datas.size();
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(context).inflate(R.layout.price_info_center_edit_item_view,null);
			TextView stockName = (TextView) convertView.findViewById(R.id.stock_name);
			TextView click_remove = (TextView) convertView.findViewById(R.id.click_remove);
			if(datas.get(position).isSelected()){
				click_remove.setVisibility(View.VISIBLE);
			}else{
				click_remove.setVisibility(View.GONE);
			}
			stockName.setText(datas.get(position).getName());
			TextView stockCode = (TextView) convertView.findViewById(R.id.stock_code);
			stockCode.setText(datas.get(position).getCode());
			RadioButton radio = (RadioButton) convertView.findViewById(R.id.selected_radio);
			radio.setChecked(datas.get(position).isSelected());
			return convertView;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(adapter!=null){
			adapter.setSelect(position);
		}
	}
}
