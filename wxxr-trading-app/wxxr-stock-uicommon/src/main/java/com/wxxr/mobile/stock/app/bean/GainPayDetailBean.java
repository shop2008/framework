package com.wxxr.mobile.stock.app.bean;

import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.bean.api.IPropertyChangeListener;
import com.wxxr.mobile.core.ui.common.BindableBeanSupport;

public class GainPayDetailBean implements IBindableBean {

    private final BindableBeanSupport emitter = new BindableBeanSupport(this);
    private String id; //id
    private String comment;
    private long amount;
    private long time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        String old = this.id;
        this.id = id;
        this.emitter.firePropertyChange("id", old, this.id);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        String old = this.comment;
        this.comment = comment;
        this.emitter.firePropertyChange("comment", old, this.comment);
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        long old = this.amount;
        this.amount = amount;
        this.emitter.firePropertyChange("amount", old, this.amount);
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        long old = this.time;
        this.time = time;
        this.emitter.firePropertyChange("time", old, this.time);
    }

    @Override
    public void addPropertyChangeListener(IPropertyChangeListener listener) {
        emitter.addPropertyChangeListener(listener);

    }

    @Override
    public void removePropertyChangeListener(IPropertyChangeListener listener) {
        emitter.removePropertyChangeListener(listener);

    }

    @Override
    public String toString() {
        return "GainPayDetailBean [id=" + id + ", comment=" + comment + ", amount=" + amount + ", time=" + time + "]";
    }

}
