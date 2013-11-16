/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import com.wxxr.mobile.core.ui.api.ISimpleSelection;

/**
 * @author neillin
 *
 */
public class SimpleSelectionImpl implements ISimpleSelection {

	private Object selected;
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ISelection#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return this.selected == null;
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ISimpleSelection#getSelected()
	 */
	@Override
	public Object getSelected() {
		return this.selected;
	}


	/**
	 * @param selected the selected to set
	 */
	public void setSelected(Object selected) {
		this.selected = selected;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((selected == null) ? 0 : selected.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleSelectionImpl other = (SimpleSelectionImpl) obj;
		if (selected == null) {
			if (other.selected != null)
				return false;
		} else if (!selected.equals(other.selected))
			return false;
		return true;
	}

}
