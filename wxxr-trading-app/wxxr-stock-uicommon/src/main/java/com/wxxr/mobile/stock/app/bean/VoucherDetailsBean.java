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
        List old = this.list;
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

}
