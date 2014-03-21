package com.wxxr.mobile.stock.app.bean;

import java.util.List;

import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.bean.api.IPropertyChangeListener;
import com.wxxr.mobile.core.ui.common.BindableBeanSupport;

public class VoucherDetailsBean implements IBindableBean {
    
    private final BindableBeanSupport emitter = new BindableBeanSupport(this);
	private long addToday;//当日增加
    private long reduceToday;//当日减少
	private long bal;//余额
	private List<GainPayDetailBean> list;//实盘积分明细
	public long getAddToday() {
        return addToday;
    }

    public void setAddToday(long addToday) {
        long old = this.addToday;
        this.addToday = addToday;
        this.emitter.firePropertyChange("addToday", old, this.addToday);    
        }

    public long getReduceToday() {
        return reduceToday;
    }

    public void setReduceToday(long reduceToday) {
        long old = this.reduceToday;
        this.reduceToday = reduceToday;
        this.emitter.firePropertyChange("reduceToday", old, this.reduceToday);    
        
    }

    public long getBal() {
        return bal;
    }

    public void setBal(long bal) {
        long old = this.bal;
        this.bal = bal;
        this.emitter.firePropertyChange("bal", old, this.bal);    
    }    

    public List<GainPayDetailBean> getList() {
        return list;
    }

    public void setList(List<GainPayDetailBean> list) {
        List<GainPayDetailBean> old = this.list;
        this.list = list;
        this.emitter.firePropertyChange("list", old, this.list);    
    }
	/**
     * @param listener
     */
    public void addPropertyChangeListener(IPropertyChangeListener listener) {
        emitter.addPropertyChangeListener(listener);
    }

    /**
     * @param listener
     */
    public void removePropertyChangeListener(IPropertyChangeListener listener) {
        emitter.removePropertyChangeListener(listener);
    }

	@Override
	public boolean hasPropertyChangeListener(IPropertyChangeListener listener) {
		return this.emitter.hasPropertyChangeListener(listener);
	}	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (addToday ^ (addToday >>> 32));
		result = prime * result + (int) (bal ^ (bal >>> 32));
		result = prime * result + ((list == null) ? 0 : list.hashCode());
		result = prime * result + (int) (reduceToday ^ (reduceToday >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VoucherDetailsBean other = (VoucherDetailsBean) obj;
		if (addToday != other.addToday)
			return false;
		if (bal != other.bal)
			return false;
		if (list == null) {
			if (other.list != null)
				return false;
		} else if (!list.equals(other.list))
			return false;
		if (reduceToday != other.reduceToday)
			return false;
		return true;
	}

}
