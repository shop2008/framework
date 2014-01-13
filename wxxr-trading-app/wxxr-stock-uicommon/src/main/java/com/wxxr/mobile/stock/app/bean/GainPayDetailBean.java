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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (amount ^ (amount >>> 32));
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (int) (time ^ (time >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GainPayDetailBean other = (GainPayDetailBean) obj;
		if (amount != other.amount)
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (time != other.time)
			return false;
		return true;
	}

}
